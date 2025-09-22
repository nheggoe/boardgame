package dev.nheggoe.boardgame.monopoly;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import dev.nheggoe.boardgame.monopoly.model.board.MonopolyBoard;
import java.lang.reflect.Type;

public final class MonopolyBoardAdapter
    implements JsonSerializer<MonopolyBoard>, JsonDeserializer<MonopolyBoard> {

  private static final String TYPE_KEY = "boardType";
  private static final String DATA_KEY = "data";

  @Override
  public JsonElement serialize(
      MonopolyBoard monopolyBoard, Type type, JsonSerializationContext context) {
    JsonObject result = new JsonObject();
    result.addProperty(TYPE_KEY, "monopoly");
    result.add(DATA_KEY, context.serialize(monopolyBoard, MonopolyBoard.class));
    return result;
  }

  @Override
  public MonopolyBoard deserialize(
      JsonElement element, Type type, JsonDeserializationContext context)
      throws JsonParseException {
    JsonObject jsonObject = element.getAsJsonObject();
    String typeString = jsonObject.get(TYPE_KEY).getAsString();
    JsonElement data = jsonObject.get(DATA_KEY);
    return context.deserialize(data, MonopolyBoard.class);
  }
  // serialize/deserialize MonopolyBoard only
}
