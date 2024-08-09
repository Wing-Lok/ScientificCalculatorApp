package scientific.calculator.app

class CalculatorLogic {
  // Basic arithmetic operations
  def add(a: Double, b: Double): Double = a + b
  def subtract(a: Double, b: Double): Double = a - b
  def multiply(a: Double, b: Double): Double = a * b
  def divide(a: Double, b: Double): Double = {
    if (b == 0) throw new ArithmeticException("Division by zero")
    else a / b
  }

  // Scientific functions
  def sin(x: Double): Double = math.sin(x)
  def cos(x: Double): Double = math.cos(x)
  def tan(x: Double): Double = math.tan(x)
  def log(x: Double): Double = {
    if (x <= 0) throw new ArithmeticException("Logarithm of non-positive number")
    else math.log(x)
  }
  def sqrt(x: Double): Double = {
    if (x < 0) throw new ArithmeticException("Square root of negative number")
    else math.sqrt(x)
  }
  def exp(x: Double): Double = math.exp(x)
  def asin(x: Double): Double = math.asin(x)
  def acos(x: Double): Double = math.acos(x)
  def atan(x: Double): Double = math.atan(x)
}
