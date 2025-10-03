package dev.nheggoe.boardgame.app.view;

import dev.nheggoe.boardgame.app.component.SettingButton;
import dev.nheggoe.boardgame.app.SceneSwitcher;
import dev.nheggoe.boardgame.app.ui.View;
import dev.nheggoe.boardgame.core.io.FileUtil;
import dev.nheggoe.boardgame.core.io.csv.CSVHandler;
import dev.nheggoe.boardgame.core.model.Player;
import dev.nheggoe.boardgame.snake.model.SnakeAndLadderPlayer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

/**
 * Represents a view for setting up players in an application. Provides user interface elements for
 * managing player entities, including adding, editing, removing, saving players, and editing player
 * attributes. The view is backed by a {@code PlayerSetupPresenter} and utilizes a {@code TableView}
 * to display the players in a structured manner.
 *
 * <p>The class manages layout components and functionality for: - Displaying a list of players in a
 * table view. - Adding new players using input fields for player name and figure. - Editing and
 * deleting existing players via context menus and dialogs. - Saving player information
 * persistently.
 *
 * <p>The view is designed with JavaFX components and follows an MVP architecture, communicating
 * with its presenter to handle data-related operations.
 */
public class PlayerSetupView extends View {
  private final TableView<Player> playerTableView;
  private final ObservableList<Player> players;
  private final PlayerSetupPresenter presenter;

  /**
   * Constructs a {@code PlayerSetupView} instance, initializes and organizes the UI components for
   * the player setup screen, including a table view for managing players and other layout elements.
   * It also sets up the presenter and loads the initial player data from a CSV file.
   *
   * @param sceneSwitcher the {@code SceneSwitcher} instance used to handle scene transitions
   */
  public PlayerSetupView(SceneSwitcher sceneSwitcher) {
    super();

    // Create the presenter
    this.presenter = new PlayerSetupPresenter(new PlayerSetupModel());

    // Initialize empty players list
    this.players = FXCollections.observableArrayList();

    // Set up the main layout
    BorderPane root = new BorderPane();
    setRoot(root);

    // Create table view for players
    this.playerTableView = createPlayerTableView();

    // Create the UI components
    root.setCenter(createCenterPane());
    root.setRight(createRightPane(sceneSwitcher));

    // Load players from CSV
    loadPlayers();
  }

  private Pane createRightPane(SceneSwitcher sceneSwitcher) {
    VBox right = new VBox();
    right.getChildren().add(new SettingButton(sceneSwitcher));
    return right;
  }

  private VBox createCenterPane() {
    VBox center = new VBox(10);
    center.setAlignment(Pos.CENTER);
    center.setPadding(new Insets(10));
    center.prefWidthProperty().bind(this.widthProperty().multiply(0.9));
    center.prefHeightProperty().bind(this.heightProperty().multiply(0.8));

    // This is critical - make VBox let the TableView grow
    VBox.setVgrow(playerTableView, Priority.ALWAYS);

    // Add the player table
    center.getChildren().add(playerTableView);

    // Add player management controls
    center.getChildren().add(createPlayerControls());

    return center;
  }

  private HBox createPlayerControls() {
    HBox controls = new HBox(10);
    controls.setAlignment(Pos.CENTER);
    controls.setPadding(new Insets(10));

    // Create name input field
    TextField nameField = new TextField();
    nameField.setPromptText("Player Name");
    nameField.setPrefWidth(150);

    // Create figure dropdown
    ComboBox<Player.Figure> figureComboBox = new ComboBox<>();
    figureComboBox.getItems().addAll(Player.Figure.values());
    figureComboBox.setValue(Player.Figure.HAT);
    figureComboBox.setPrefWidth(150);

    // Create add button
    Button addButton = new Button("Add Player");
    addButton.setOnAction(
        e -> {
          String name = nameField.getText().trim();
          Player.Figure figure = figureComboBox.getValue();

          if (!name.isEmpty() && figure != null) {
            presenter.addPlayer(name, figure);
            nameField.clear();
            figureComboBox.setValue(Player.Figure.HAT);
            updatePlayersTable();
          } else {
            showAlert("Input Error", "Please enter a player name and select a figure.");
          }
        });

    // Create remove button
    Button removeButton = new Button("Remove Player");
    removeButton.setOnAction(
        e -> {
          Player selectedPlayer = playerTableView.getSelectionModel().getSelectedItem();
          if (selectedPlayer != null) {
            presenter.removePlayer(selectedPlayer);
            updatePlayersTable();
          } else {
            showAlert("Selection Error", "Please select a player to remove.");
          }
        });

    // Create save button
    Button saveButton = new Button("Save Players");
    saveButton.setOnAction(
        e -> {
          try {
            presenter.savePlayers();
            showAlert("Success", "Players saved successfully!");
          } catch (IOException ex) {
            showAlert("Error", "Failed to save players: " + ex.getMessage());
          }
        });

    // Add components to control panel
    controls
        .getChildren()
        .addAll(
            new Label("Name:"),
            nameField,
            new Label("Figure:"),
            figureComboBox,
            addButton,
            removeButton,
            saveButton);

    return controls;
  }

  private TableView<Player> createPlayerTableView() {
    TableView<Player> tableView = new TableView<>();
    tableView.setPadding(new Insets(10));
    tableView.prefWidthProperty().bind(this.widthProperty().multiply(0.8));
    tableView.prefHeightProperty().bind(this.heightProperty().multiply(0.6));

    // Make the TableView fill available width
    tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    tableView.setItems(players);
    tableView.getColumns().addAll(createTableColumns());

    // Add context menu for editing
    tableView.setRowFactory(
        tv -> {
          TableRow<Player> row = new TableRow<>();

          row.setOnContextMenuRequested(
              event -> {
                if (!row.isEmpty()) {
                  ContextMenu contextMenu = new ContextMenu();

                  MenuItem editItem = new MenuItem("Edit");
                  editItem.setOnAction(e -> showEditDialog(row.getItem()));

                  MenuItem deleteItem = new MenuItem("Delete");
                  deleteItem.setOnAction(
                      e -> {
                        presenter.removePlayer(row.getItem());
                        updatePlayersTable();
                      });

                  contextMenu.getItems().addAll(editItem, deleteItem);
                  contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
              });

          return row;
        });

    return tableView;
  }

  private List<TableColumn<Player, ?>> createTableColumns() {
    TableColumn<Player, String> nameColumn = new TableColumn<>("Name");
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    nameColumn.setPrefWidth(200);

    TableColumn<Player, Player.Figure> figureColumn = new TableColumn<>("Figure");
    figureColumn.setCellValueFactory(new PropertyValueFactory<>("figure"));
    figureColumn.setPrefWidth(200);

    return List.of(nameColumn, figureColumn);
  }

  private void showEditDialog(Player player) {
    Dialog<Pair<String, Player.Figure>> dialog = new Dialog<>();
    dialog.setTitle("Edit Player");
    dialog.setHeaderText("Edit player information");

    // Set the button types
    ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

    // Create the name and figure fields
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 150, 10, 10));

    ComboBox<Player.Figure> figureComboBox = new ComboBox<>();
    figureComboBox.getItems().addAll(Player.Figure.values());
    figureComboBox.setValue(player.getFigure());

    TextField nameField = new TextField(player.getName());
    grid.add(new Label("Name:"), 0, 0);
    grid.add(nameField, 1, 0);
    grid.add(new Label("Figure:"), 0, 1);
    grid.add(figureComboBox, 1, 1);

    dialog.getDialogPane().setContent(grid);

    // Convert the result
    dialog.setResultConverter(
        dialogButton -> {
          if (dialogButton == saveButtonType) {
            return new Pair<>(nameField.getText(), figureComboBox.getValue());
          }
          return null;
        });

    Optional<Pair<String, Player.Figure>> result = dialog.showAndWait();

    result.ifPresent(
        pair -> {
          String name = pair.key();
          Player.Figure figure = pair.value();

          if (!name.isEmpty()) {
            presenter.updatePlayer(player, name, figure);
            updatePlayersTable();
          }
        });
  }

  private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.initStyle(StageStyle.UTILITY);
    alert.showAndWait();
  }

  private void loadPlayers() {
    try {
      List<Player> loadedPlayers = presenter.getPlayers();
      updatePlayersTable(loadedPlayers);
    } catch (IOException e) {
      showAlert("Error", "Failed to load players: " + e.getMessage());
    }
  }

  private void updatePlayersTable() {
    updatePlayersTable(presenter.getPlayersList());
  }

  private void updatePlayersTable(List<Player> playerList) {
    players.clear();
    players.addAll(playerList);
  }

  // Helper class for the edit dialog
  private record Pair<K, V>(K key, V value) {}

  // Model class to handle player data
  private static class PlayerSetupModel {
    private final List<Player> players;
    private final CSVHandler csvHandler;

    public PlayerSetupModel() {
      this.players = new ArrayList<>();
      var csvFile = FileUtil.generateFilePath("players", "csv");
      this.csvHandler = new CSVHandler(csvFile);
    }

    public List<Player> getPlayers() throws IOException {
      var rows = csvHandler.readAll();

      for (var row : rows) {
        try {
          String name = row[0].trim();
          Player.Figure figure = Player.Figure.valueOf(row[1].trim().toUpperCase());
          players.add(new SnakeAndLadderPlayer(name, figure));
        } catch (IllegalArgumentException ignored) {
          // ignored
        }
      }

      return new ArrayList<>(players);
    }

    public void savePlayers() throws IOException {
      var rows = new ArrayList<String[]>();
      rows.add(new String[] {"Name", "Figure"});
      for (Player player : players) {
        rows.add(Player.toCsvLine(player));
      }

      csvHandler.writeLines(rows);
    }

    public void addPlayer(String name, Player.Figure figure) {
      players.add(new SnakeAndLadderPlayer(name, figure));
    }

    public void removePlayer(Player player) {
      players.remove(player);
    }

    public void updatePlayer(Player player, String name, Player.Figure figure) {
      int index = players.indexOf(player);
      if (index >= 0) {
        player.setName(name);
        player.setFigure(figure);
      }
    }

    public List<Player> getPlayersList() {
      return new ArrayList<>(players);
    }
  }

  // Presenter class to handle the business logic
  private record PlayerSetupPresenter(PlayerSetupModel model) {

    public List<Player> getPlayers() throws IOException {
      return model.getPlayers();
    }

    public void savePlayers() throws IOException {
      model.savePlayers();
    }

    public void addPlayer(String name, Player.Figure figure) {
      model.addPlayer(name, figure);
    }

    public void removePlayer(Player player) {
      model.removePlayer(player);
    }

    public void updatePlayer(Player player, String name, Player.Figure figure) {
      model.updatePlayer(player, name, figure);
    }

    public List<Player> getPlayersList() {
      return model.getPlayersList();
    }
  }
}
