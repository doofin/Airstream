import sbt._

import java.io.File

case class GenerateCombineSignalsTest(
  testSourceDir: File,
  from: Int,
  to: Int
) extends SourceGenerator(
  testSourceDir / "scala" / "com" / "raquo" / "airstream" / "combine" / "generated" / s"CombineSignalsSpec.scala"
) {

  override def apply(): Unit = {
    line("package com.raquo.airstream.combine.generated")
    line()
    line("import com.raquo.airstream.UnitSpec")
    line("import com.raquo.airstream.core.{Observer, Signal}")
    line("import com.raquo.airstream.fixtures.TestableOwner")
    line("import com.raquo.airstream.state.Var")
    line()
    line("import scala.collection.mutable")
    line()
    line("// #Warning do not edit this file directly, it is generated by GenerateCombineSignalsTest.scala")
    line()
    enter(s"class CombineSignalsSpec extends UnitSpec {")
    line()
    for (i <- 1 to to) {
      line(s"case class T${i}(v: Int) { def inc: T${i} = T${i}(v+1) }")
    }
    line()
    for (n <- from to to) {
      enter(s"""it("CombineSignal${n} works") {""")
      line()
      line("implicit val testOwner: TestableOwner = new TestableOwner")
      line()
      for (i <- 1 to n) {
        line(s"val var${i} = Var(T${i}(1))")
      }
      line()
      line(s"val combinedSignal = Signal.combine(${tupleType(n, "var")})")
      line()
      line(s"val effects = mutable.Buffer[(${tupleType(n)})]()")
      line()
      line(s"val observer = Observer[(${tupleType(n)})](effects += _)")
      line()
      line("// --")
      line()
      line("effects.shouldBeEmpty")
      line()
      line("// --")
      line()
      line("val subscription = combinedSignal.addObserver(observer)")
      line()
      line("// --")
      line()
      enter("effects.toList shouldBe (List(")
      line(s"(${(1 to n).map(i => s"T${i}(1)").mkString(", ")})")
      leave("))")
      line()
      line("// --")
      line()

      enter("for (iteration <- 0 until 10) {")
      line("effects.clear()")
      for (i <- 1 to n) {
        line(s"var${i}.update(_.inc)")
      }
      enter("effects.toList shouldBe (")
      enter("List(")
      for (i <- 1 to n) {
        line(s"(${(1 to n).map(j => s"T${j}(1 + iteration${if (j <= i) " + 1" else ""})").mkString(", ")})${if (i < n) "," else ""}")
      }
      leave(")")
      leave(")")
      leave("}")
      line()
      line("subscription.kill()")

      leave("}")
      line()
    }
    line()
    leave("}")
  }

}
