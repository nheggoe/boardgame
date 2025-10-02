package dev.nheggoe.boardgame.monopoly.tile;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import dev.nheggoe.boardgame.core.model.Player;
import dev.nheggoe.boardgame.monopoly.model.tile.CornerMonopolyTile;
import dev.nheggoe.boardgame.monopoly.model.tile.JailMonopolyTile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JailMonopolyTileTest {

  @Test
  void jailForNumberOfRoundsShouldJailPlayerForSpecifiedRounds() {
    // Arrange
    JailMonopolyTile jailTile = new JailMonopolyTile(CornerMonopolyTile.Position.BOTTOM_LEFT);
    Player mockPlayer = mock(Player.class);

    // Act
    jailTile.jailForNumberOfRounds(mockPlayer, 3);

    // Assert
    assertThat(jailTile.isPlayerInJail(mockPlayer)).isTrue();
    assertThat(jailTile.getNumberOfRoundsLeft(mockPlayer)).isEqualTo(3);

    jailTile.releaseIfPossible(mockPlayer);

    assertThat(jailTile.isPlayerInJail(mockPlayer)).isTrue();
    assertThat(jailTile.getNumberOfRoundsLeft(mockPlayer)).isEqualTo(2);

    jailTile.releaseIfPossible(mockPlayer);

    assertThat(jailTile.isPlayerInJail(mockPlayer)).isTrue();
    assertThat(jailTile.getNumberOfRoundsLeft(mockPlayer)).isEqualTo(1);

    jailTile.releaseIfPossible(mockPlayer);

    assertThat(jailTile.isPlayerInJail(mockPlayer)).isFalse();
    assertThatThrownBy(() -> jailTile.getNumberOfRoundsLeft(mockPlayer))
        .isInstanceOf(IllegalStateException.class);
    verifyNoMoreInteractions(mockPlayer);
  }

  @Test
  void jailForNumberOfRoundsShouldThrowWhenPlayerAlreadyInJail() {
    // Arrange
    JailMonopolyTile jailTile = new JailMonopolyTile(CornerMonopolyTile.Position.BOTTOM_LEFT);
    Player mockPlayer = mock(Player.class);
    jailTile.jailForNumberOfRounds(mockPlayer, 3);

    // Act & Assert
    assertThatThrownBy(() -> jailTile.jailForNumberOfRounds(mockPlayer, 2))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Player is already in jail!");
  }
}
