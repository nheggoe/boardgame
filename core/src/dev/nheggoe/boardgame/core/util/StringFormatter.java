package dev.nheggoe.boardgame.core.util;

import java.util.Arrays;
import java.util.stream.Collectors;

/// The StringFormatter class provides utility methods for string manipulation. It is designed to
/// work with specific use cases such as formatting enum constants into a more human-readable form.
/// This class contains only static methods and cannot be instantiated.
public class StringFormatter {

  private StringFormatter() {}

  /// Formats an enum constant's name to a more readable string by capitalizing the first letter of
  /// each word and replacing underscores with spaces.
  ///
  /// @param em the enum constant to format cannot be null
  /// @return a human-readable string representation of the enum constant
  /// @throws NullPointerException if the provided enum is null
  public static String formatEnum(Enum<?> em) {
    return Arrays.stream(em.name().split("_"))
        .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
        .collect(Collectors.joining(" "));
  }
}
