package test.examples.inputoutput

import buildinfo.BuildInfo
import datalog.dsl.{Constant, Program}
import test.{ExampleTestGenerator, Tags}

import java.nio.file.Paths
class inputoutput_test extends ExampleTestGenerator("inputoutput") with inputoutput
trait inputoutput {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/inputoutput/facts"
  val toSolve = "_"
  def pretest(program: Program): Unit = {
    // input, i.e. defined in facts+here, i.e. named
    val InputOutputNumberSymbol = program.namedRelation[Constant]("InputOutputNumberSymbol")
    val InputOutputReceiveNumberSymbol = program.namedRelation[Constant]("InputOutputReceiveNumberSymbol")
    val InputOutputReceiveSendNumberSymbol = program.namedRelation[Constant]("InputOutputReceiveSendNumberSymbol")
    val InputOutputReceiveSendSymbolNumber = program.namedRelation[Constant]("InputOutputReceiveSendSymbolNumber")
    val InputOutputReceiveSymbolNumber = program.namedRelation[Constant]("InputOutputReceiveSymbolNumber")
    val InputOutputSendNumberSymbol = program.namedRelation[Constant]("InputOutputSendNumberSymbol")
    val InputOutputSendSymbolNumber = program.namedRelation[Constant]("InputOutputSendSymbolNumber")
    val InputOutputSymbolNumber = program.namedRelation[Constant]("InputOutputSymbolNumber")

    // input, i.e. defined in facts
    val InputNumberSymbol = program.namedRelation[Constant]("InputNumberSymbol")
    val InputReceiveNumberSymbol = program.namedRelation[Constant]("InputReceiveNumberSymbol")
    val InputReceiveSendNumberSymbol = program.namedRelation[Constant]("InputReceiveSendNumberSymbol")
    val InputReceiveSendSymbolNumber = program.namedRelation[Constant]("InputReceiveSendSymbolNumber")
    val InputReceiveSymbolNumber = program.namedRelation[Constant]("InputReceiveSymbolNumber")
    val InputSendNumberSymbol = program.namedRelation[Constant]("InputSendNumberSymbol")
    val InputSendSymbolNumber = program.namedRelation[Constant]("InputSendSymbolNumber")
    val InputSymbolNumber = program.namedRelation[Constant]("InputSymbolNumber")

    // output, i.e. new relations
    val OutputNumberSymbol = program.relation[Constant]("OutputNumberSymbol")
    val OutputReceiveNumberSymbol = program.relation[Constant]("OutputReceiveNumberSymbol")
    val OutputReceiveSendNumberSymbol = program.relation[Constant]("OutputReceiveSendNumberSymbol")
    val OutputReceiveSendSymbolNumber = program.relation[Constant]("OutputReceiveSendSymbolNumber")
    val OutputReceiveSymbolNumber = program.relation[Constant]("OutputReceiveSymbolNumber")
    val OutputSendNumberSymbol = program.relation[Constant]("OutputSendNumberSymbol")
    val OutputSendSymbolNumber = program.relation[Constant]("OutputSendSymbolNumber")
    val OutputSymbolNumber = program.relation[Constant]("OutputSymbolNumber")

    val ReceiveNumberSymbol = program.relation[Constant]("ReceiveNumberSymbol")
    val ReceiveSendNumberSymbol = program.relation[Constant]("ReceiveSendNumberSymbol")
    val ReceiveSendSymbolNumber = program.relation[Constant]("ReceiveSendSymbolNumber")
    val ReceiveSymbolNumber = program.relation[Constant]("ReceiveSymbolNumber")
    val ReceiverNumberSymbol = program.relation[Constant]("ReceiverNumberSymbol")
    val ReceiverSymbolNumber = program.relation[Constant]("ReceiverSymbolNumber")
    val SendNumberSymbol = program.namedRelation[Constant]("SendNumberSymbol")
    val SendSymbolNumber = program.relation[Constant]("SendSymbolNumber")

    val SenderNumberSymbol = program.namedRelation[Constant]("SenderNumberSymbol")
    val SenderSymbolNumber = program.namedRelation[Constant]("SenderSymbolNumber")

    val x, y = program.variable()
    
    InputNumberSymbol(1, "b") :- ()
    InputOutputNumberSymbol(1, "b") :- ()
    InputOutputReceiveNumberSymbol(1, "b") :- ()
    InputOutputReceiveNumberSymbol(x, y) :- ( SenderNumberSymbol(x, y) )
    InputOutputReceiveSendNumberSymbol(1, "b") :- ()
    InputOutputReceiveSendNumberSymbol(x, y) :- ( SenderNumberSymbol(x, y) )
    InputOutputReceiveSendSymbolNumber("b", 1) :- ()
    InputOutputReceiveSendSymbolNumber(x, y) :- ( SenderSymbolNumber(x, y) )
    InputOutputReceiveSymbolNumber("b", 1) :- ()
    InputOutputReceiveSymbolNumber(x, y) :- ( SenderSymbolNumber(x, y) )
    InputOutputSendNumberSymbol(1, "b") :- ()
    InputOutputSendSymbolNumber("b", 1) :- ()
    InputOutputSymbolNumber("b", 1) :- ()
    InputReceiveNumberSymbol(1, "b") :- ()
    InputReceiveNumberSymbol(x, y) :- ( SenderNumberSymbol(x, y) )
    InputReceiveSendNumberSymbol(1, "b") :- ()
    InputReceiveSendNumberSymbol(x, y) :- ( SenderNumberSymbol(x, y) )
    InputReceiveSendSymbolNumber("b", 1) :- ()
    InputReceiveSendSymbolNumber(x, y) :- ( SenderSymbolNumber(x, y) )
    InputReceiveSymbolNumber("b", 1) :- ()
    InputReceiveSymbolNumber(x, y) :- ( SenderSymbolNumber(x, y) )
    InputSendNumberSymbol(1, "b") :- ()
    InputSendSymbolNumber("b", 1) :- ()
    InputSymbolNumber("b", 1) :- ()
    OutputNumberSymbol(1, "b") :- ()
    OutputReceiveNumberSymbol(1, "b") :- ()
    OutputReceiveNumberSymbol(x, y) :- ( SenderNumberSymbol(x, y) )
    OutputReceiveSendNumberSymbol(1, "b") :- ()
    OutputReceiveSendNumberSymbol(x, y) :- ( SenderNumberSymbol(x, y) )
    OutputReceiveSendSymbolNumber("b", 1) :- ()
    OutputReceiveSendSymbolNumber(x, y) :- ( SenderSymbolNumber(x, y) )
    OutputReceiveSymbolNumber("b", 1) :- ()
    OutputReceiveSymbolNumber(x, y) :- ( SenderSymbolNumber(x, y) )
    OutputSendNumberSymbol(1, "b") :- ()
    OutputSendSymbolNumber("b", 1) :- ()
    OutputSymbolNumber("b", 1) :- ()
    ReceiveNumberSymbol(1, "b") :- ()
    ReceiveNumberSymbol(x, y) :- SenderNumberSymbol(x, y)
    ReceiveSendNumberSymbol(1, "b") :- ()
    ReceiveSendNumberSymbol(x, y) :- ( SenderNumberSymbol(x, y) )
    ReceiveSendSymbolNumber("b", 1) :- ()
    ReceiveSendSymbolNumber(x, y) :- ( SenderSymbolNumber(x, y) )
    ReceiveSymbolNumber("b", 1) :- ()
    ReceiveSymbolNumber(x, y) :- ( SenderSymbolNumber(x, y) )
    ReceiverNumberSymbol(x, y) :- ( InputOutputReceiveSendNumberSymbol(x, y) )
    ReceiverNumberSymbol(x, y) :- ( InputOutputSendNumberSymbol(x, y) )
    ReceiverNumberSymbol(x, y) :- ( InputReceiveSendNumberSymbol(x, y) )
    ReceiverNumberSymbol(x, y) :- ( InputSendNumberSymbol(x, y) )
    ReceiverNumberSymbol(x, y) :- ( OutputReceiveSendNumberSymbol(x, y) )
    ReceiverNumberSymbol(x, y) :- ( OutputSendNumberSymbol(x, y) )
    ReceiverNumberSymbol(x, y) :- ( SendNumberSymbol(x, y) )
    ReceiverSymbolNumber(x, y) :- ( InputOutputReceiveSendSymbolNumber(x, y) )
    ReceiverSymbolNumber(x, y) :- ( InputOutputSendSymbolNumber(x, y) )
    ReceiverSymbolNumber(x, y) :- ( InputReceiveSendSymbolNumber(x, y) )
    ReceiverSymbolNumber(x, y) :- ( InputSendSymbolNumber(x, y) )
    ReceiverSymbolNumber(x, y) :- ( OutputReceiveSendSymbolNumber(x, y) )
    ReceiverSymbolNumber(x, y) :- ( OutputSendSymbolNumber(x, y) )
    ReceiverSymbolNumber(x, y) :- ( SendSymbolNumber(x, y) )
    SendNumberSymbol(1, "b") :- ()
    SendSymbolNumber("b", 1) :- ()
  }
}
