package dev.nheggoe.boardgame.monopoly.ownable;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import dev.nheggoe.boardgame.core.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PropertyTest {

  private Property property;
  private MonopolyPlayer player;

  @BeforeEach
  void setup() {
    property = new Property("Test property", Property.Color.DARK_BLUE, 100); // value property 100
    player = new MonopolyPlayer("Duke", Player.Figure.CAT); // player start with balance 0
  }

  @Test
  void testInvalidData() {
    assertThatThrownBy(() -> new Property("test", Property.Color.BROWN, -1))
        .withFailMessage("Property with negative value should not be created")
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> new Property("", Property.Color.BROWN, 10))
        .withFailMessage("Property with empty name should not be created")
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> new Property(null, Property.Color.BROWN, 0))
        .withFailMessage("Property name cannot be null")
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void testBasic() {
    assertThat(property.getName()).isEqualTo("Test property");
  }

  @Test
  void testPurchaseProperty() {
    player.addBalance(100);

    assertThatCode(() -> player.purchase(property)).doesNotThrowAnyException();

    assertThat(player.getBalance()).isZero();
  }

  @Test
  void testPurchasePropertyWithInefficientBalance() {
    assertThatThrownBy(() -> player.purchase(property))
        .isInstanceOf(InsufficientFundsException.class);

    assertThat(player.getBalance()).isZero();
  }
}
