package dev.nheggoe.boardgame.monopoly.model.tile;

import dev.nheggoe.boardgame.core.model.Tile;

/// Represents a tile in the Monopoly game.
///
/// This is a sealed interface that serves as the base type for all Monopoly-specific tile types,
/// ensuring architectural constraints by limiting the permitted subtypes. Subtypes represent various
/// kinds of tiles found on the game board, such as: - Corner tiles, such as Free Parking or Jail -
/// Ownable tiles, representing properties or utilities - Tax tiles, where players must pay certain
/// amounts h
///
/// Permitted subtypes are: - [CornerMonopolyTile]: Represents corner tiles like Start,
/// Jail, etc. - [OwnableMonopolyTile]: Represents tiles that can be owned, such as properties
/// or utilities. - [TaxMonopolyTile]: Represents tax tiles where players pay specific
/// percentages.
///
/// @author Nick Heggø
/// @version 2025.05.08
public sealed interface MonopolyTile extends Tile
    permits CornerMonopolyTile, OwnableMonopolyTile, TaxMonopolyTile {}
