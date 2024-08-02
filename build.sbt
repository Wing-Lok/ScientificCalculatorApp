name := "ScientificCalculatorApp"

version := "0.1"

scalaVersion := "2.13.8"

libraryDependencies ++= Seq(
  "org.openjfx" % "javafx-controls" % "22.0.1",
  "org.openjfx" % "javafx-fxml" % "22.0.1"
)

// Specify JavaFX module paths
javaOptions ++= Seq(
  "--module-path", "/Users/yapwinglok/Downloads/javafx-sdk-22.0.2/lib",
  "--add-modules", "javafx.controls,javafx.fxml"
)
