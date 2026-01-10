package dev.nheggoe.boardgame.monopoly;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import dev.nheggoe.boardgame.monopoly.model.tile.FreeParkingMonopolyTile;
import dev.nheggoe.boardgame.monopoly.model.tile.GoToJailMonopolyTile;
import dev.nheggoe.boardgame.monopoly.model.tile.JailMonopolyTile;
import dev.nheggoe.boardgame.monopoly.model.tile.MonopolyTile;
import dev.nheggoe.boardgame.monopoly.model.tile.OwnableMonopolyTile;
import dev.nheggoe.boardgame.monopoly.model.tile.StartMonopolyTile;
import dev.nheggoe.boardgame.monopoly.model.tile.TaxMonopolyTile;
import java.lang.reflect.Type;

/// Specialized Gson adapter for the MonopolyTile sealed interface. Handles
/// serialization/deserialization of all MonopolyTile implementations.
///
/// @author Nick Heggø
/// @version 2025.05.18
public class MonopolyTileAdapter
    implements JsonSerializer<MonopolyTile>, JsonDeserializer<MonopolyTile> {

  private static final String TYPE_KEY = "tileType";
  private static final String DATA_KEY = "data";

  @Override
  public JsonElement serialize(MonopolyTile tile, Type type, JsonSerializationContext context) {
    JsonObject result = new JsonObject();

    switch (tile) {
      case StartMonopolyTile startTile -> {
        result.addProperty(TYPE_KEY, "start");
        result.add(DATA_KEY, context.serialize(startTile, StartMonopolyTile.class));
      }
      case JailMonopolyTile jailTile -> {
        result.addProperty(TYPE_KEY, "jail");
        result.add(DATA_KEY, context.serialize(jailTile, JailMonopolyTile.class));
      }
      case FreeParkingMonopolyTile freeParkingTile -> {
        result.addProperty(TYPE_KEY, "freeParking");
        result.add(DATA_KEY, context.serialize(freeParkingTile, FreeParkingMonopolyTile.class));
      }
      case GoToJailMonopolyTile goToJailTile -> {
        result.addProperty(TYPE_KEY, "goToJail");
        result.add(DATA_KEY, context.serialize(goToJailTile, GoToJailMonopolyTile.class));
      }
      case OwnableMonopolyTile ownableTile -> {
        result.addProperty(TYPE_KEY, "ownable");
        result.add(DATA_KEY, context.serialize(ownableTile, OwnableMonopolyTile.class));
      }
      case TaxMonopolyTile taxTile -> {
        result.addProperty(TYPE_KEY, "tax");
        result.add(DATA_KEY, context.serialize(taxTile, TaxMonopolyTile.class));
      }
    }

    return result;
  }

  @Override
  public MonopolyTile deserialize(JsonElement json, Type type, JsonDeserializationContext context)
      throws JsonParseException {
    JsonObject jsonObject = json.getAsJsonObject();
    String typeString = jsonObject.get(TYPE_KEY).getAsString();
    JsonElement data = jsonObject.get(DATA_KEY);

    return switch (typeString) {
      case "start" -> context.deserialize(data, StartMonopolyTile.class);
      case "jail" -> context.deserialize(data, JailMonopolyTile.class);
      case "freeParking" -> context.deserialize(data, FreeParkingMonopolyTile.class);
      case "goToJail" -> context.deserialize(data, GoToJailMonopolyTile.class);
      case "ownable" -> context.deserialize(data, OwnableMonopolyTile.class);
      case "tax" -> context.deserialize(data, TaxMonopolyTile.class);
      default -> throw new JsonParseException("Unknown MonopolyTile type: " + typeString);
    };
  }
}
