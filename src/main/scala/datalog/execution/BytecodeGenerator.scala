package datalog.execution

import java.lang.constant.ConstantDescs.*
import java.lang.constant.*
import java.lang.invoke.*
import MethodHandles.Lookup.ClassOption.NESTMATE
import java.nio.file.{Files, Paths}

import org.glavo.classfile.{java as _, *}
import org.glavo.classfile.components.{CodeStackTracker, ClassPrinter}

import scala.jdk.CollectionConverters.*

/**
 * Generate a class containing a single static method.
 *
 * Implementors of this class should implement `traverse` to perform the code generation
 * inside the method using the `CodeBuilder` argument.
 */
trait BytecodeGenerator[A](clsName: String, methType: MethodType) {
  import BytecodeGenerator.*

  /** Set to true in subclasses to print debug output and write generated classfile to disk. */
  protected val debug: Boolean = false

  /** The name of the static method in the class. */
  val methName = "entryPoint"
  /**
   * This method will be called from `generate` to generate the body
   * of the class static method.
   */
  protected def traverse(xb: CodeBuilder, input: A): Unit

  private val resolver = ClassHierarchyResolver.ofCached(cd =>
    this.getClass.getClassLoader.getResourceAsStream(impl.Util.toInternalName(cd) + ".class"))
  private val options = List(Classfile.Option.classHierarchyResolver(resolver))
  private val stackTracker = CodeStackTracker.of()
  private val resType = TypeKind.fromDescriptor(methType.describeConstable.get().returnType.descriptorString)

  /**
   * Generate the classfile, classload it, and return a handle to the static method.
   *
   * @pre The caller must be a class defined in the same package as `clsName`.
   */
  def generateAndLoad(input: A): MethodHandle = {
    val bytes = generate(input)
    val lookup = MethodHandles.lookup().defineHiddenClass(bytes, true, NESTMATE)
    val cls = lookup.lookupClass
    lookup.findStatic(cls, methName, methType)
  }

  /** Generate the classfile. */
  def generate(input: A): Array[Byte] = {
    val bytes = Classfile.build(ClassDesc.of(clsName), options.asJava, cb =>
      cb.withMethod(methName,  methType.describeConstable.get(),
        Classfile.ACC_PUBLIC | Classfile.ACC_STATIC, mb => mb.withCode(xb =>
          xb.transforming(stackTracker, xxb =>
            traverse(xxb, input)

            // If we need to return something but nothing is left on the stack,
            // return BoxedUnit like the Scala compiler.
            if (curStackSize() == 0 && resType != TypeKind.VoidType)
              emitBoxedUnit(xb)
            xb.returnInstruction(resType)
          )
      )))
    if (debug) {
      val cm = Classfile.parse(bytes, options*)
      ClassPrinter.toYaml(cm, ClassPrinter.Verbosity.TRACE_ALL, print);
      val debugClassName = clsName.split("\\.").last
      val path = Paths.get(s"debug/$debugClassName.class")
      Files.createDirectories(path.getParent)
      Files.write(path, bytes)
      println(s"Generated classfile written to disk: $path")
    }
    bytes
  }

  /** The current size of the stack of instructions. */
  protected final def curStackSize(): Int =
    stackTracker.stack.get.size

  /**
   * Run `op` then emit `pop` instructions until the stack
   * does not contain more elements than we started with.
   */
  protected final def discardResult[T](xb: CodeBuilder, op: => T): T = {
    val initialSize = curStackSize()
    val res = op
    var newSize = curStackSize()
    while (newSize > initialSize) {
      xb.pop()
      newSize -= 1
    }
    res
  }
}

object BytecodeGenerator {
  def clsDesc(cls: Class[?]): ClassDesc =
    ClassDesc.ofDescriptor(cls.descriptorString)

  def methDesc(meth: java.lang.reflect.Method): MethodTypeDesc =
    MethodTypeDesc.of(clsDesc(meth.getReturnType), meth.getParameterTypes.map(clsDesc)*)

  def constrDesc(meth: java.lang.reflect.Constructor[?]): MethodTypeDesc =
    MethodTypeDesc.of(CD_void, meth.getParameterTypes.map(clsDesc)*)

  /**
   * Call `methName` defined in `cls`.
   *
   *  @pre The stack has the shape [... clsObj methArgs*]
   */
  def emitCall(xb: CodeBuilder, cls: Class[?], methName: String, methParameterTypes: Class[?]*): Unit =
    val cd = clsDesc(cls)
    // Using the reflective `getMethod` to construct the MethodTypeDesc saves
    // us from having to pass the result type by hand. This is slightly less
    // efficient but doesn't seem to show up in benchmarks.
    val md = methDesc(cls.getMethod(methName, methParameterTypes*))
    if (cls.isInterface) then
      xb.invokeinterface(cd, methName, md)
    else
      xb.invokevirtual(cd, methName, md)

  /** Construct an instance of `cls` passing `emitArgs` as arguments. */
  def emitNew(xb: CodeBuilder, cls: Class[?], emitArgs: CodeBuilder => Any): Unit =
    val cd = clsDesc(cls)
    xb.new_(cd).dup()
    emitArgs(xb)
    xb.invokespecial(cd, "<init>", constrDesc(cls.getConstructors()(0)))

  /** Create an Array[$elemCls] filled with `emitElems` */
  def emitArray(xb: CodeBuilder, elemCls: Class[?], emitElems: Seq[CodeBuilder => Any]): Unit =
    xb.constantInstruction(emitElems.length)
      .anewarray(clsDesc(elemCls))
    emitElems.zipWithIndex.foreach: (emitElem, idx) =>
      xb.dup()
      xb.constantInstruction(idx)
      emitElem(xb)
      xb.aastore()

  /** Create an ArraySeq[Object] filled with `emitElems` */
  def emitSeq(xb: CodeBuilder, emitElems: Seq[CodeBuilder => Any]): Unit =
    emitNew(xb, classOf[scala.collection.immutable.ArraySeq.ofRef[?]],
      emitArray(_, classOf[Object], emitElems))

  /** Emit `Integer.valueOf($value)`. */
  def emitInteger(xb: CodeBuilder, value: Int): Unit =
    xb.constantInstruction(value)
      .invokestatic(clsDesc(classOf[Integer]), "valueOf",
        MethodTypeDesc.of(clsDesc(classOf[Integer]), clsDesc(classOf[Int])))

  val CD_BoxedUnit = clsDesc(classOf[scala.runtime.BoxedUnit])

  /** Emit `BoxedUnit.UNIT`. */
  def emitBoxedUnit(xb: CodeBuilder): Unit =
    xb.getstatic(CD_BoxedUnit, "UNIT", CD_BoxedUnit)
}
