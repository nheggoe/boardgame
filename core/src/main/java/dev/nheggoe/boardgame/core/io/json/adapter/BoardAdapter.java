package dev.nheggoe.boardgame.core.io.json.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import dev.nheggoe.boardgame.core.model.Board;
import java.lang.reflect.Type;

/// Specialized Gson adapter for the Board interface. Handles serialization/deserialization of
/// MonopolyBoard and SnakeAndLadderBoard implementations.
///
/// @author Nick Heggø
/// @version 2025.05.18
public class BoardAdapter implements JsonSerializer<Board<?>>, JsonDeserializer<Board<?>> {
  @Override
  public JsonElement serialize(Board<?> board, Type type, JsonSerializationContext ctx) {
    JsonObject obj = new JsonObject();
    obj.addProperty("className", board.getClass().getName());
    obj.add("data", ctx.serialize(board));
    return obj;
  }

  @Override
  public Board<?> deserialize(JsonElement json, Type type, JsonDeserializationContext ctx) {
    JsonObject obj = json.getAsJsonObject();
    String className = obj.get("className").getAsString();
    try {
      Class<?> clazz = Class.forName(className);
      return ctx.deserialize(obj.get("data"), clazz);
    } catch (ClassNotFoundException e) {
      throw new JsonParseException("Unknown class: " + className, e);
    }
  }
}
