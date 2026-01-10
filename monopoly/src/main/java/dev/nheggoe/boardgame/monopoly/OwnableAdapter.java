package dev.nheggoe.boardgame.monopoly;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import dev.nheggoe.boardgame.monopoly.model.ownable.Ownable;
import dev.nheggoe.boardgame.monopoly.model.ownable.Property;
import dev.nheggoe.boardgame.monopoly.model.ownable.Railroad;
import dev.nheggoe.boardgame.monopoly.model.ownable.Utility;
import java.lang.reflect.Type;

/// Specialized Gson adapter for the Ownable sealed interface. Handles serialization/deserialization
/// of Property, Railroad, and Utility implementations.
///
/// @author Nick Heggø
/// @version 2025.05.18
public class OwnableAdapter implements JsonSerializer<Ownable>, JsonDeserializer<Ownable> {

  private static final String TYPE_KEY = "type";
  private static final String DATA_KEY = "data";

  @Override
  public JsonElement serialize(Ownable ownable, Type type, JsonSerializationContext context) {
    JsonObject result = new JsonObject();

    switch (ownable) {
      case Property property -> {
        result.addProperty(TYPE_KEY, "property");
        result.add(DATA_KEY, context.serialize(property, Property.class));
      }
      case Railroad railroad -> {
        result.addProperty(TYPE_KEY, "railroad");
        result.add(DATA_KEY, context.serialize(railroad, Railroad.class));
      }
      case Utility utility -> {
        result.addProperty(TYPE_KEY, "utility");
        result.add(DATA_KEY, context.serialize(utility, Utility.class));
      }
    }

    return result;
  }

  @Override
  public Ownable deserialize(JsonElement json, Type type, JsonDeserializationContext context)
      throws JsonParseException {
    JsonObject jsonObject = json.getAsJsonObject();
    String typeString = jsonObject.get(TYPE_KEY).getAsString();
    JsonElement data = jsonObject.get(DATA_KEY);

    return switch (typeString) {
      case "property" -> context.deserialize(data, Property.class);
      case "railroad" -> context.deserialize(data, Railroad.class);
      case "utility" -> context.deserialize(data, Utility.class);
      default -> throw new JsonParseException("Unknown Ownable type: " + typeString);
    };
  }
}
