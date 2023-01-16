package test.examples.java_pointsto

import datalog.dsl.{Constant, Program}
import test.ExampleTestGenerator

import java.nio.file.Paths
class java_pointsto_test extends ExampleTestGenerator("java_pointsto") with java_pointsto
trait java_pointsto {
  def pretest(program: Program): Unit = {
    val ActualParam = program.namedRelation[Constant]("ActualParam")

    val AssignCast = program.namedRelation[Constant]("AssignCast")

    val AssignHeapAllocation = program.namedRelation[Constant]("AssignHeapAllocation")

    val AssignLocal = program.namedRelation[Constant]("AssignLocal")

    val AssignReturnValue = program.namedRelation[Constant]("AssignReturnValue")

    val DirectFieldIndexSignature = program.namedRelation[Constant]("DirectFieldIndexSignature")

    val DirectSuperclass = program.namedRelation[Constant]("DirectSuperclass")

    val DirectSuperinterface = program.namedRelation[Constant]("DirectSuperinterface")

    val FormalParam = program.namedRelation[Constant]("FormalParam")

    val HeapAllocationType = program.namedRelation[Constant]("HeapAllocationType")

    val LoadInstanceFieldIndex = program.namedRelation[Constant]("LoadInstanceFieldIndex")

    val ReturnVar = program.namedRelation[Constant]("ReturnVar")

    val SpecialMethodInvocationBase = program.namedRelation[Constant]("SpecialMethodInvocationBase")

    val SpecialMethodInvocationSignature = program.namedRelation[Constant]("SpecialMethodInvocationSignature")

    val StaticMethodInvocationSignature = program.namedRelation[Constant]("StaticMethodInvocationSignature")

    val StoreInstanceFieldIndex = program.namedRelation[Constant]("StoreInstanceFieldIndex")

    val ThisVar = program.namedRelation[Constant]("ThisVar")

    val VarType = program.namedRelation[Constant]("VarType")

    val VirtualMethodInvocationBase = program.namedRelation[Constant]("VirtualMethodInvocationBase")

    val VirtualMethodInvocationSignature = program.namedRelation[Constant]("VirtualMethodInvocationSignature")

    val VarPointsTo = program.relation[Constant]("VarPointsTo")

    val InstanceVar = program.relation[Constant]("InstanceVar")
    val Superclass = program.relation[Constant]("Superclass")
    val Supertype = program.relation[Constant]("Supertype")
    val Invocation = program.relation[Constant]("Invocation")
    val InvocationBase = program.relation[Constant]("InvocationBase")
    val InstanceVarType = program.relation[Constant]("InstanceVarType")
    val InstanceTypes = program.relation[Constant]("InstanceTypes")
    val IsInstanceTypeCompatible = program.relation[Constant]("IsInstanceTypeCompatible")
    val InstanceTypeComparable = program.relation[Constant]("InstanceTypeComparable")
    val ComparableInstanceVar = program.relation[Constant]("ComparableInstanceVar")
    val Assign = program.relation[Constant]("Assign")
    val InstanceVarPointsTo = program.relation[Constant]("InstanceVarPointsTo")
    val LoadInstanceVarPointsTo = program.relation[Constant]("LoadInstanceVarPointsTo")
    val StoreInstanceVarPointsTo = program.relation[Constant]("StoreInstanceVarPointsTo")
    val Alias = program.relation[Constant]("Alias")

    val varVar, varVar1, varVar2, superVar, typeVar, typeVar1, typeVar2, interface, sub, x, thisVar, callsite, calledMethod, t = program.variable()
    val any1, any2, any3 = program.variable()

    InstanceVar(varVar) :- ( LoadInstanceFieldIndex(varVar, any1, any2, any3) )
    InstanceVar(varVar) :- ( StoreInstanceFieldIndex(any1, varVar, any2, any3) )
    
    Superclass(sub, superVar) :- ( DirectSuperclass(sub, superVar) )
    Superclass(sub, superVar) :- ( Superclass(sub, x), DirectSuperclass(x, superVar) )
    
    Supertype(sub, superVar) :- ( Superclass(sub, superVar) )
    Supertype(typeVar, interface) :- ( DirectSuperinterface(typeVar, interface) )
    
    Invocation(callsite, calledMethod) :- ( SpecialMethodInvocationSignature(callsite, calledMethod) )
    Invocation(callsite, calledMethod) :- ( StaticMethodInvocationSignature(callsite, calledMethod) )
    Invocation(callsite, calledMethod) :- ( VirtualMethodInvocationSignature(callsite, calledMethod) )
    
    InvocationBase(callsite, varVar) :- ( VirtualMethodInvocationBase(callsite, varVar) )
    InvocationBase(callsite, varVar) :- ( SpecialMethodInvocationBase(callsite, varVar) )
    
    InstanceVarType(varVar, typeVar) :- (
      VarType(varVar, typeVar),
      InstanceVar(varVar) )
    
    InstanceTypes(x) :- (
      Supertype(x, any1),
      InstanceVarType(any2, x) )
    
    InstanceTypes(x) :- (
      Supertype(any1, x),
      InstanceVarType(any2, x) )
    
    IsInstanceTypeCompatible(sub, superVar) :- (
      Supertype(sub, superVar) )
    
    IsInstanceTypeCompatible(t, t) :- (
      InstanceTypes(t) )
    
    InstanceTypeComparable(typeVar1, typeVar2) :- (
      IsInstanceTypeCompatible(typeVar1, typeVar2) )
    
    InstanceTypeComparable(typeVar1, typeVar2) :- (
      IsInstanceTypeCompatible(typeVar2, typeVar1) )
    
    ComparableInstanceVar(varVar1, varVar2) :- (
     InstanceVarType(varVar1, typeVar1),
     InstanceVarType(varVar2, typeVar2),
     InstanceTypeComparable(typeVar1, typeVar2) )

    val localVarAssigned, localVarAssignedTo, actualParam, formalParam, returnVar, localVar, base, heap,
      heapType, storedVar, varAssignedTo, varVarType, v1, v2, method, index, h, t1, t2, iLoadVar, iStoreVar, field = program.variable()
    
    Assign(localVarAssigned, localVarAssignedTo) :- ( AssignLocal(localVarAssigned, localVarAssignedTo, any1) )
    Assign(actualParam, formalParam) :- ( Invocation(callsite, method), FormalParam(index, method, formalParam), ActualParam(index, callsite, actualParam) )
    Assign(returnVar, localVar) :- ( ReturnVar(returnVar, method), Invocation(callsite, method), AssignReturnValue(callsite, localVar) )
    Assign(base, thisVar) :- ( InvocationBase(callsite, base), Invocation(callsite, method), ThisVar(method, thisVar) )
    
    VarPointsTo(varVar, heap)  :- (
      AssignHeapAllocation(heap, varVar, any1) )
    
    VarPointsTo(varVar1, heap) :- (
      Assign(varVar2, varVar1),
      VarPointsTo(varVar2, heap) )
    
    VarPointsTo(varVar1, heap) :- (
      AssignCast(typeVar, varVar2, varVar1, any1),
      VarPointsTo(varVar2, heap),
      HeapAllocationType(heap, typeVar) )
    
    VarPointsTo(varVar1, heap) :- (
      AssignCast(varVarType, varVar2, varVar1, any1),
      VarPointsTo(varVar2, heap),
      HeapAllocationType(heap, heapType),
      Supertype(heapType, varVarType) )
    
    InstanceVarPointsTo(varVar1, varVar2) :- (
      InstanceVar(varVar1),
      VarPointsTo(varVar1, varVar2) )
    
    LoadInstanceVarPointsTo(varVar, heap)  :- (
      InstanceVarPointsTo(varVar, heap),
      LoadInstanceFieldIndex(varVar, any1, any2, any3) )
    
    StoreInstanceVarPointsTo(varVar, heap) :- (
      InstanceVarPointsTo(varVar, heap),
      StoreInstanceFieldIndex(any1, varVar, any2, any3) )
    
    Alias(v1, v2) :- (
      LoadInstanceVarPointsTo(v1, h),
      StoreInstanceVarPointsTo(v2, h),
      InstanceVarType(v1, t1),
      InstanceVarType(v2, t2),
      InstanceTypeComparable(t1, t2) )
    
    Assign(storedVar, varAssignedTo) :- (
      Alias(iLoadVar, iStoreVar),
      StoreInstanceFieldIndex(storedVar, iStoreVar, field, any1),
      LoadInstanceFieldIndex(iLoadVar, field, varAssignedTo, any2) )
  }
}
