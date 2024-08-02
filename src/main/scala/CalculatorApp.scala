import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.{Button, TextField}
import javafx.scene.layout.{GridPane, VBox}
import javafx.stage.Stage

object CalculatorApp {
  def main(args: Array[String]): Unit = {
    Application.launch(classOf[CalculatorApp], args: _*)
  }
}

class CalculatorApp extends Application {
  private val calculator = new CalculatorLogic
  private val display = new TextField

  override def start(primaryStage: Stage): Unit = {
    primaryStage.setTitle("Scientific Calculator App")

    display.setEditable(false)
    display.setStyle("-fx-font-size: 40px;")

    val grid = new GridPane
    grid.setPadding(new Insets(10, 10, 10, 10))
    grid.setVgap(5)
    grid.setHgap(5)

    val buttons = List(
      "7", "8", "9", "/", "sin", "cos",
      "4", "5", "6", "*", "tan", "log",
      "1", "2", "3", "-", "sqrt", "exp",
      "0", ".", "=", "+", "pow", "clear"
    )

    buttons.zipWithIndex.foreach {
      case (text, index) =>
        val button = new Button(text)
        button.setStyle("-fx-font-size: 18px;")
        button.setOnAction(_ => handleButtonPress(text))
        grid.add(button, index % 6, index / 6)
    }

    val layout = new VBox(10)
    layout.getChildren.addAll(display, grid)

    val scene = new Scene(layout, 400, 450)
    primaryStage.setScene(scene)
    primaryStage.show()
  }

  private def handleButtonPress(text: String): Unit = {
    text match {
      case "=" => calculateResult()
      case "clear" => display.clear()
      case _ => display.appendText(text)
    }
  }

  private def calculateResult(): Unit = {
    val input = display.getText
    println(s"Evaluating: $input")  // Debugging line
    try {
      val result = evaluateExpression(input)
      display.setText(result.toString)
    } catch {
      case e: ArithmeticException =>
        println(s"Math Error: ${e.getMessage}")  // Debugging line
        display.setText("Math Error")
      case e: IllegalArgumentException =>
        println(s"Syntax Error: ${e.getMessage}")  // Debugging line
        display.setText("Syntax Error")
      case e: Exception =>
        println(s"Error: ${e.getMessage}")  // Debugging line
        display.setText("Error")
    }
  }

  private def evaluateExpression(expression: String): Double = {
    val operators = List("+", "-", "*", "/", "pow")
    val functions = List("sin", "cos", "tan", "log", "sqrt", "exp", "asin", "acos", "atan")

    // Find the position of the first operator or function
    val opPos = operators.map(op => expression.indexOf(op)).filter(_ >= 0).sorted.headOption
    val funcPos = functions.map(func => expression.indexOf(func)).filter(_ >= 0).sorted.headOption

    (opPos, funcPos) match {
      case (Some(opIndex), _) if opIndex > 0 =>
        val (left, rightWithOp) = expression.splitAt(opIndex)
        val operator = rightWithOp.head.toString
        val right = rightWithOp.tail

        val a = left.trim.toDouble
        val b = right.trim.toDouble

        operator match {
          case "+" => calculator.add(a, b)
          case "-" => calculator.subtract(a, b)
          case "*" => calculator.multiply(a, b)
          case "/" => calculator.divide(a, b)
          case "pow" => calculator.pow(a, b)
          case _ => throw new IllegalArgumentException("Invalid operator")
        }

      case (_, Some(funcIndex)) if funcIndex >= 0 =>
        val func = expression.takeWhile(_.isLetter)
        val value = expression.drop(funcIndex + func.length).trim.toDouble

        func match {
          case "sin" => calculator.sin(value)
          case "cos" => calculator.cos(value)
          case "tan" => calculator.tan(value)
          case "log" => calculator.log(value)
          case "sqrt" => calculator.sqrt(value)
          case "exp" => calculator.exp(value)
          case "asin" => calculator.asin(value)
          case "acos" => calculator.acos(value)
          case "atan" => calculator.atan(value)
          case _ => throw new IllegalArgumentException("Invalid function")
        }

      case _ => throw new IllegalArgumentException("Invalid expression")
    }
  }

  private def parseExpression(expression: String, operators: List[String]): List[String] = {
    val escapedOperators = operators.map(java.util.regex.Pattern.quote).mkString("|")
    val operatorPattern = s"($escapedOperators)".r

    val tokens = operatorPattern.split(expression).filter(_.nonEmpty).toList
    val ops = operatorPattern.findAllIn(expression).toList

    if (tokens.length != ops.length + 1) {
      throw new IllegalArgumentException("Invalid expression format")
    }

    tokens.zipAll(ops, "", "").flatMap { case (num, op) => List(num, op) }.filter(_.nonEmpty)
  }
}
