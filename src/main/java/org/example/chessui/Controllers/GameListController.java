package org.example.chessui.Controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.chessui.auth.SessionManager;
import org.example.chessui.game.GameDTO;
import org.example.chessui.game.GameService;

import java.util.List;

public class GameListController {
    public Button backButton;
    @FXML
    private TableView<GameDTO> gameTable;
    @FXML
    private TableColumn<GameDTO, Long> idColumn;
    @FXML
    private TableColumn<GameDTO, String> whiteColumn;
    @FXML
    private TableColumn<GameDTO, String> blackColumn;
    @FXML
    private TableColumn<GameDTO, Void> actionColumn;
    @FXML
    private Button refreshButton;

    private final ObservableList<GameDTO> games = FXCollections.observableArrayList();
    private final GameService gameService = GameService.getInstance();

    @FXML
    public void initialize() {
        // bind columns
        idColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getId()));
        whiteColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getWhiteUsername()));
        blackColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getBlackUsername()));

        // Action column with Delete and Analyse buttons
        // Action column with Analyse button
        actionColumn.setCellFactory((Callback<TableColumn<GameDTO, Void>, TableCell<GameDTO, Void>>) col -> new TableCell<>() {
            private final Button analyseButton = new Button("Analyse");

            {
                analyseButton.setOnAction(e -> {
                    // get the GameDTO for the current row
                    GameDTO game = getTableView().getItems().get(getIndex());
                    analyseGame(e, game.getId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    // layout button horizontally
                    HBox box = new HBox(5, analyseButton);
                    setGraphic(box);
                }
            }
        });

        gameTable.setItems(games);
        onRefresh();
    }

    @FXML
    private void onRefresh() {
        try {
            List<GameDTO> l = gameService.getGamesByUser();
            loadSampleData(l);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadSampleData(List<GameDTO> l) {

        games.setAll(l);
    }

    @FXML void goBack (ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/chessui/main_menu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene c = new Scene(root);
            c.getStylesheets().add(getClass().getResource("/org/example/chessui/styles.css").toExternalForm());
            stage.setScene(c);
            stage.show();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void analyseGame(ActionEvent e, Long id) {
        gameService.setIndex(id);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/chessui/gameReplay.fxml"));
            ;
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene c = new Scene(root);
            c.getStylesheets().add(getClass().getResource("/org/example/chessui/styles.css").toExternalForm());
            stage.setScene(c);
            stage.setFullScreen(true);
            stage.show();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
