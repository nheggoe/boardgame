package dev.nheggoe.boardgame.monopoly;

import com.google.gson.GsonBuilder;
import dev.nheggoe.boardgame.core.io.json.GsonContributor;
import dev.nheggoe.boardgame.monopoly.model.ownable.Ownable;
import dev.nheggoe.boardgame.monopoly.model.tile.MonopolyTile;

public final class MonopolyGsonContributor implements GsonContributor {
  @Override
  public void contribute(GsonBuilder builder) {
    builder.registerTypeAdapter(Ownable.class, new OwnableAdapter());
    builder.registerTypeAdapter(MonopolyTile.class, new MonopolyTileAdapter());
  }
}
