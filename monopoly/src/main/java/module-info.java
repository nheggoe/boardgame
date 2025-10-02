module dev.nheggoe.boardgame.monopoly {
  requires dev.nheggoe.boardgame.core;
  requires com.google.gson;

  exports dev.nheggoe.boardgame.monopoly;
  exports dev.nheggoe.boardgame.monopoly.model.board;
  exports dev.nheggoe.boardgame.monopoly.model.ownable;
  exports dev.nheggoe.boardgame.monopoly.model.tile;
  exports dev.nheggoe.boardgame.monopoly.model.upgrade;
}
