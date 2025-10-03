# Portfolio project IDATG2003

### Authors:

[@nheggoe](https://github.com/nheggoe)

[@Mihailohrani](https://github.com/Mihailohrani)

## Project description

This project is an academic exam project developed for the course IDATG2003 – Programming 2.
It is a JavaFX-based desktop application that implements a digital board game, designed to demonstrate principles of
modular software architecture, object-oriented programming, and user interface development in Java.

## Project structure

```
boardgame
├── app
│   └── src
│       └── main
│           └── java
│               ├── dev
│               │   └── nheggoe
│               │       └── boardgame
│               │           └── app
│               │               ├── component
│               │               │   ├── DiceView.java
│               │               │   ├── EndDialog.java
│               │               │   ├── FigureAnimator.java
│               │               │   ├── MessagePanel.java
│               │               │   ├── MonopolyBoardView.java
│               │               │   ├── PlayerDashboard.java
│               │               │   ├── PlayerRender.java
│               │               │   ├── RollDiceButton.java
│               │               │   ├── SettingButton.java
│               │               │   ├── SettingDialog.java
│               │               │   ├── SnakeAndLadderBoardRender.java
│               │               │   └── SnakeBoardLayout.java
│               │               ├── controller
│               │               │   ├── MainController.java
│               │               │   ├── MonopolyGameController.java
│               │               │   ├── PlayerSetupController.java
│               │               │   └── SnakeGameController.java
│               │               ├── ui
│               │               │   ├── Component.java
│               │               │   ├── Controller.java
│               │               │   ├── EventListeningComponent.java
│               │               │   ├── GameView.java
│               │               │   └── View.java
│               │               ├── util
│               │               │   ├── AlertFactory.java
│               │               │   ├── GameFactory.java
│               │               │   └── PlayerManager.java
│               │               ├── view
│               │               │   ├── MainView.java
│               │               │   ├── MonopolyGameView.java
│               │               │   ├── PlayerSetupView.java
│               │               │   ├── SnakeGameView.java
│               │               │   └── SnakeSetupView.java
│               │               ├── Launcher.java
│               │               └── SceneSwitcher.java
│               └── module-info.java
├── core
│   └── src
│       └── main
│           └── java
│               ├── dev
│               │   └── nheggoe
│               │       └── boardgame
│               │           └── core
│               │               ├── event
│               │               │   ├── type
│               │               │   │   ├── CoreEvent.java
│               │               │   │   ├── Event.java
│               │               │   │   └── UserInterfaceEvent.java
│               │               │   ├── EventBus.java
│               │               │   ├── EventListener.java
│               │               │   ├── EventPublisher.java
│               │               │   └── UnhandledEventException.java
│               │               ├── io
│               │               │   ├── csv
│               │               │   │   ├── CSVHandler.java
│               │               │   │   ├── CSVReader.java
│               │               │   │   └── CSVWriter.java
│               │               │   ├── json
│               │               │   │   ├── adapter
│               │               │   │   │   └── BoardAdapter.java
│               │               │   │   ├── CustomGson.java
│               │               │   │   ├── GsonContributor.java
│               │               │   │   ├── JsonException.java
│               │               │   │   ├── JsonReader.java
│               │               │   │   ├── JsonService.java
│               │               │   │   ├── JsonType.java
│               │               │   │   └── JsonWriter.java
│               │               │   ├── DAO.java
│               │               │   └── FileUtil.java
│               │               ├── model
│               │               │   ├── dice
│               │               │   │   ├── Dice.java
│               │               │   │   └── DiceRoll.java
│               │               │   ├── Board.java
│               │               │   ├── Game.java
│               │               │   ├── IllegalTilePositionException.java
│               │               │   ├── InvalidBoardLayoutException.java
│               │               │   ├── Player.java
│               │               │   ├── Tile.java
│               │               │   ├── TileAction.java
│               │               │   └── TurnManager.java
│               │               ├── repository
│               │               │   ├── DataRepository.java
│               │               │   └── JsonRepository.java
│               │               ├── util
│               │               │   └── StringFormatter.java
│               │               └── GameEngine.java
│               └── module-info.java
├── monopoly
│   └── src
│       └── main
│           └── java
│               ├── dev
│               │   └── nheggoe
│               │       └── boardgame
│               │           └── monopoly
│               │               ├── model
│               │               │   ├── board
│               │               │   │   ├── MonopolyBoard.java
│               │               │   │   └── MonopolyBoardFactory.java
│               │               │   ├── ownable
│               │               │   │   ├── InsufficientFundsException.java
│               │               │   │   ├── MonopolyPlayer.java
│               │               │   │   ├── Ownable.java
│               │               │   │   ├── Property.java
│               │               │   │   ├── Railroad.java
│               │               │   │   └── Utility.java
│               │               │   ├── tile
│               │               │   │   ├── CornerMonopolyTile.java
│               │               │   │   ├── FreeParkingMonopolyTile.java
│               │               │   │   ├── GoToJailMonopolyTile.java
│               │               │   │   ├── JailMonopolyTile.java
│               │               │   │   ├── MonopolyTile.java
│               │               │   │   ├── OwnableMonopolyTile.java
│               │               │   │   ├── StartMonopolyTile.java
│               │               │   │   ├── TaxMonopolyTile.java
│               │               │   │   └── TileFactory.java
│               │               │   └── upgrade
│               │               │       ├── Upgrade.java
│               │               │       └── UpgradeType.java
│               │               ├── view
│               │               ├── MonopolyBoardAdapter.java
│               │               ├── MonopolyEvent.java
│               │               ├── MonopolyGame.java
│               │               ├── MonopolyGsonContributor.java
│               │               ├── MonopolyTileAdapter.java
│               │               └── OwnableAdapter.java
│               └── module-info.java
└── snake
    └── src
        └── main
            └── java
                ├── dev
                │   └── nheggoe
                │       └── boardgame
                │           └── snake
                │               ├── model
                │               │   ├── tile
                │               │   │   ├── LadderTile.java
                │               │   │   ├── NormalTile.java
                │               │   │   ├── SnakeAndLadderTile.java
                │               │   │   └── SnakeTile.java
                │               │   ├── SnakeAndLadderBoard.java
                │               │   ├── SnakeAndLadderBoardFactory.java
                │               │   └── SnakeAndLadderPlayer.java
                │               ├── SnakeAndLadderEvent.java
                │               ├── SnakeAndLadderGame.java
                │               ├── SnakeAndLadderTileAdapter.java
                │               └── SnakeGsonContributor.java
                └── module-info.java

56 directories, 105 files
```

Project source code is organized into packages:

* `core` package provides the abstract class that concrete implementation extends from.
* `games` package is where the individual game lives.

## Data Files

The application uses external CSV files located in the `/app/data/csv/` directory to configure board elements and player
profiles.

- `player.csv` can be edited directly through the application’s built-in interface.
- `snakeAndLadder.csv` and `monopoly.csv` must be manually edited outside the application.

To modify the Snake and Ladder board, you may add or remove lines in `snakeAndLadder.csv`.

To modify the Monopoly board, you may add or remove lines in `monopoly.csv`. Ensure the total number of lines is a
multiple of 4 to maintain a square layout.

If anything goes wrong, delete the corresponding CSV file in the `/app/data/csv/` folder — a default version will be
automatically regenerated on the next launch.

Ensure that the file structure and formatting are preserved when editing these files to avoid runtime errors.

## Link to repository

[GitHub Repository](https://github.com/nheggoe/boardgame)

## How to run the project

This project requires JDK 25 to be installed on the system.
It is being developed and tested using the [Liberica JDK 25](https://bell-sw.com/libericajdk/).

Before running any commands, check if the correct version is installed on the system by typing:

```bash
java --version
```

If you have the correct java version installed, you may proceed to build the project from source.

1. Go to the project directory.

```bash
cd <project-root>
```

2. Run the following command in the terminal to build and run the project.

```bash
./gradlew run
```

## References

### Observer Design Pattern

[Refactoring Guru](https://refactoring.guru/design-patterns/observer/java/example)
