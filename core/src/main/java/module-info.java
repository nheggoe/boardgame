module dev.nheggoe.boardgame.core {
  requires java.logging;
  requires com.google.gson;

  exports dev.nheggoe.boardgame.core;
  exports dev.nheggoe.boardgame.core.event;
  exports dev.nheggoe.boardgame.core.event.type;
  exports dev.nheggoe.boardgame.core.model;
  exports dev.nheggoe.boardgame.core.model.dice;
  exports dev.nheggoe.boardgame.core.io;
  exports dev.nheggoe.boardgame.core.io.csv;
  exports dev.nheggoe.boardgame.core.io.json;
  exports dev.nheggoe.boardgame.core.io.json.adapter;
  exports dev.nheggoe.boardgame.core.repository;
  exports dev.nheggoe.boardgame.core.util;
}
