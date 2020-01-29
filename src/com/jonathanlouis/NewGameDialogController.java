package com.jonathanlouis;

import com.jonathanlouis.data.NewGame;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.Optional;

public class NewGameDialogController {

    @FXML
    private TextField publisherTextField;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private TextField genreTextField;

    //attempt to add new game once OK selected in dialog box
    public NewGame processDialog(){

        String publisher = publisherTextField.getText().trim();
        String title = titleTextField.getText().trim();
        String genre = genreTextField.getText().trim();
        String description = descriptionTextArea.getText().trim();

        //if field was left empty, alert without adding game
        if(publisher.isEmpty() || title.isEmpty() || genre.isEmpty() || description.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error adding new game");
            alert.setHeaderText("Field missing information");
            alert.setContentText("Press OK to continue");
            Optional<ButtonType> result = alert.showAndWait();
            return null;
        }

        //new game to add to database
        NewGame newGame = new NewGame();
        newGame.setPublisher(publisher);
        newGame.setTitle(title);
        newGame.setGenre(genre);
        newGame.setDescription(description);

        return newGame;
    }

    //load game info into dialog box
    public void loadGameInfo(NewGame newGame){
        publisherTextField.setText(newGame.getPublisher());
        titleTextField.setText(newGame.getTitle());
        genreTextField.setText(newGame.getGenre());
        descriptionTextArea.setText(newGame.getDescription());
    }
}