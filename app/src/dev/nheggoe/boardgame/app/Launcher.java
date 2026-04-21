package dev.nheggoe.boardgame.app;

/// Entry point of the program.
///
/// This class intentionally does NOT extend [javafx.application.Application]. When the JVM's
/// entry-point class extends `Application`, the `java` launcher swaps it for an internal
/// `FXHelper` that requires JavaFX to be on the JPMS module path. By keeping this class plain
/// and delegating to [MainApp], JavaFX boots from the regular classpath without any
/// `--module-path` / `--add-modules` flags.
///
/// @author Nick Heggø
/// @version 2025.04.15
public final class Launcher {
  public static void main(String[] args) {
    MainApp.main(args);
  }
}
