package com.jonathanlouis;

import com.jonathanlouis.data.DataSource;
import com.jonathanlouis.data.Game;
import com.jonathanlouis.data.NewGame;
import com.jonathanlouis.data.Publisher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Optional;

public class Controller {

    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private TableView gamesTable;
    @FXML
    private TableView publisherTable;
    @FXML
    private BorderPane mainBorderPane;

    @FXML
    public void initialize(){
        //set tables constraints
        gamesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        gamesTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        publisherTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        publisherTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }


    @FXML
    public void listPublishers(){
        Task<ObservableList<Publisher>> task = new GetAllPublishersTask();
        publisherTable.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();
    }

    @FXML
    public void listGames(){
        Task<ObservableList<Game>> gamesTask = new GetAllGamesTask();
        gamesTable.itemsProperty().bind(gamesTask.valueProperty());

        new Thread(gamesTask).start();
    }

    @FXML
    public void listGamesPublisher(){
        Publisher publisher = (Publisher) publisherTable.getSelectionModel().getSelectedItem();

        Task<ObservableList<Game>> publisherGameTask = new Task<ObservableList<Game>>() {
            @Override
            protected ObservableList<Game> call() throws Exception {
                return FXCollections.observableArrayList(DataSource.getInstance().queryGamesByPublisher(publisher));
            }
        };

        gamesTable.itemsProperty().bind(publisherGameTask.valueProperty());

        new Thread(publisherGameTask).start();
    }

    @FXML
    public void gameDescription(){
        descriptionTextArea.setVisible(true);
        Game game = (Game) gamesTable.getSelectionModel().getSelectedItem();
        descriptionTextArea.setText(DataSource.getInstance().gameDescription(game));
    }

    //dialog for adding new game
    @FXML
    public void showNewGameDialog(){
        //create new dialog box
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add New Game");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("newGameDialog.fxml"));

        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e){
            System.out.println("Couldn't load dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        //process result of user button selection
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            NewGameDialogController controller = fxmlLoader.getController();
            NewGame newGame = controller.processDialog();
            if(newGame != null) {
                DataSource.getInstance().addNewGame(newGame);
            }
        }
    }

    @FXML
    public void deleteGame(){

        //ensure game is selected
        Game game = (Game) gamesTable.getSelectionModel().getSelectedItem();
        if(game == null){
            return;
        }

        //create new game
        NewGame newGame = DataSource.getInstance().getNewGame(game);

        //alert if error in database
        if(newGame == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error deleting game");
            alert.setHeaderText("Error no game found in database");
            alert.setContentText("Press OK to continue");
            Optional<ButtonType> result = alert.showAndWait();
            return;
        }

        //create dialog box to prompt user to confirm delete
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Delete Game");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("newGameDialog.fxml"));

        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e){
            System.out.println("Couldn't load dialog");
            e.printStackTrace();
            return;
        }

        dialog.setHeaderText("Press OK to delete game or cancel");

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        NewGameDialogController controller = fxmlLoader.getController();
        controller.loadGameInfo(newGame);

        //process result of user button selection
        //OK proceed to delete
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            if(DataSource.getInstance().removeGame(newGame)){
                listPublishers();
                listGames();
            }
        }
    }

    @FXML
    public void updateGame(){
        Game game = (Game) gamesTable.getSelectionModel().getSelectedItem();

        if(game == null){
            return;
        }

        //create new game
        NewGame prevGame = DataSource.getInstance().getNewGame(game);

        //alert if error in database
        if(prevGame == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error updating game");
            alert.setHeaderText("Error no game found in database");
            alert.setContentText("Press OK to continue");
            Optional<ButtonType> result = alert.showAndWait();
            return;
        }

        //create dialog box to prompt user to confirm delete
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Update Game");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("newGameDialog.fxml"));

        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e){
            System.out.println("Couldn't load dialog");
            e.printStackTrace();
            return;
        }

        dialog.setHeaderText("Update current game info");

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        NewGameDialogController controller = fxmlLoader.getController();
        controller.loadGameInfo(prevGame);

        //process result of user button selection
        //OK proceed to update
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            NewGame newGame = controller.processDialog();
            if(DataSource.getInstance().updateGameInfo(prevGame, newGame)){
                listPublishers();
                listGames();
            }
        }
    }



}

//task for thread to get publishers
class GetAllPublishersTask extends Task{
    @Override
    protected ObservableList<Publisher> call() throws Exception {
        return FXCollections.observableArrayList(DataSource.getInstance().queryPublishers(DataSource.ORDER_BY_ASC));
    }
}

//task for thread to get games
class GetAllGamesTask extends Task{
    @Override
    protected Object call() throws Exception {
        return FXCollections.observableArrayList(DataSource.getInstance().queryGames(DataSource.ORDER_BY_ASC));
    }
}