module dev.nheggoe.boardgame.app {
  requires java.desktop;
  requires java.logging;
  requires javafx.controls;
  requires javafx.graphics;
  requires dev.nheggoe.boardgame.core;
  requires dev.nheggoe.boardgame.monopoly;
  requires dev.nheggoe.boardgame.snake;

  opens dev.nheggoe.boardgame.app to javafx.controls, javafx.graphics;
  opens dev.nheggoe.boardgame.app.ui to javafx.controls, javafx.graphics;
  opens dev.nheggoe.boardgame.app.component to javafx.controls, javafx.graphics;
  opens dev.nheggoe.boardgame.app.controller to javafx.controls, javafx.graphics;
  opens dev.nheggoe.boardgame.app.view to javafx.controls, javafx.graphics;
}
