package dev.nheggoe.boardgame.core.io.json;

import com.google.gson.GsonBuilder;

public interface GsonContributor {
  void contribute(GsonBuilder builder);
}
