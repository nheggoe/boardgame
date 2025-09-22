package dev.nheggoe.boardgame.core.io.json;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class JsonType {
  private JsonType() {}

  public static <T> Type listOf(Class<T> elem) {
    return TypeToken.getParameterized(List.class, elem).getType();
  }

  public static <T> Type setOf(Class<T> elem) {
    return TypeToken.getParameterized(Set.class, elem).getType();
  }

  public static <K, V> Type mapOf(Class<K> key, Class<V> val) {
    return TypeToken.getParameterized(Map.class, key, val).getType();
  }
}
