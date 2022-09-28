import sbt._

import java.io.File

case class GenerateCombineStreams(
  sourceDir: File,
  from: Int,
  to: Int
) extends SourceGenerator(
  sourceDir / "scala" / "com" / "raquo" / "airstream" / "combine" / "generated" / s"CombineStreams.scala"
) {

  override def apply(): Unit = {
    line("package com.raquo.airstream.combine.generated")
    line()
    line("import com.raquo.airstream.combine.CombineStreamN")
    line("import com.raquo.airstream.core.Source.EventSource")
    line()
    line("// #Warning do not edit this file directly, it is generated by GenerateCombineStreams.scala")
    line()
    line("// These are implementations of CombineStreamN used for EventStream's `combine` and `combineWith` methods")
    line()
    for (n <- from to to) {
      line("/** @param combinator Must not throw! */")
      enter(s"class CombineStream${n}[${tupleType(n)}, Out](")
      for (i <- 1 to n) {
        line(s"parent${i}: EventSource[T${i}],")
      }
      line(s"combinator: (${tupleType(n)}) => Out")
      leave()
      enter(s") extends CombineStreamN[Any, Out](")
      line("parents = " + tupleTypeRaw(n, "parent", ".toObservable").mkString(" :: ") + " :: Nil,")
      enter("combinator = seq => combinator(")
      for (i <- 1 to n) {
        line(s"seq(${i - 1}).asInstanceOf[T${i}],")
      }
      leave(")")
      leave(")")
      line()
    }
  }

}