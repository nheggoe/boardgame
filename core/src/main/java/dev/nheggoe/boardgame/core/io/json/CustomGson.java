package dev.nheggoe.boardgame.core.io.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.nheggoe.boardgame.core.io.json.adapter.BoardAdapter;
import dev.nheggoe.boardgame.core.model.Board;
import java.util.ServiceLoader;

/// Singleton utility class that provides a single instance of [Gson]. This class ensures that
/// the same instance of the custom gson is used throughout the application.
///
/// @author Nick Heggø
/// @version 2025.05.20
public final class CustomGson {
  private CustomGson() {}

  private static final class Holder {
    static final Gson INSTANCE = build();
  }

  public static Gson getInstance() {
    return Holder.INSTANCE;
  }

  private static Gson build() {
    GsonBuilder b =
        new GsonBuilder()
            .registerTypeAdapter(Board.class, new BoardAdapter()) // core-only adapter
            .setPrettyPrinting();

    ServiceLoader.load(GsonContributor.class).forEach(c -> c.contribute(b));
    return b.create();
  }
}
