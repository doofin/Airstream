import sbt._

import java.io.File

case class GenerateTupleEventStreams(
  sourceDir: File,
  from: Int,
  to: Int
) extends SourceGenerator(
  sourceDir / "scala" / "com" / "raquo" / "airstream" / "misc" / "generated" / s"TupleEventStreams.scala"
) {

  override def apply(): Unit = {
    line("package com.raquo.airstream.misc.generated")
    line()
    line("import com.raquo.airstream.core.EventStream")
    line("import com.raquo.airstream.misc.{FilterEventStream, MapEventStream}")
    line()
    line("// These mapN and filterN helpers are implicitly available on streams of tuples")
    line()
    for (n <- from to to) {
      enter(s"class TupleEventStream${n}[${tupleType(n)}](val stream: EventStream[(${tupleType(n)})]) extends AnyVal {")
      line()
      enter(s"def mapN[Out](project: (${tupleType(n)}) => Out): EventStream[Out] = {")
      enter(s"new MapEventStream[(${tupleType(n)}), Out](")
      line("parent = stream,")
      line(s"project = v => project(${tupleType(n, "v._")}),")
      line(s"recover = None")
      leave(")")
      leave("}")
      line()
      enter(s"def filterN(passes: (${tupleType(n)}) => Boolean): EventStream[(${tupleType(n)})] = {")
      enter(s"new FilterEventStream[(${tupleType(n)})](")
      line("parent = stream,")
      line(s"passes = v => passes(${tupleType(n, "v._")})")
      leave(")")
      leave("}")
      leave("}")
      line()
      line("// --")
      line()
    }
  }
}