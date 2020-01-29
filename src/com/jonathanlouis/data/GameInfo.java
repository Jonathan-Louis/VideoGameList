package com.jonathanlouis.data;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class GameInfo {
    private SimpleIntegerProperty id;
    private SimpleStringProperty description;
    private SimpleBooleanProperty finished;
    private SimpleIntegerProperty game;

    public GameInfo() {
        id = new SimpleIntegerProperty();
        description = new SimpleStringProperty();
        finished = new SimpleBooleanProperty();
        game = new SimpleIntegerProperty();
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public boolean isFinished() {
        return finished.get();
    }

    public SimpleBooleanProperty finishedProperty() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished.set(finished);
    }

    public int getGame() {
        return game.get();
    }

    public SimpleIntegerProperty gameProperty() {
        return game;
    }

    public void setGame(int game) {
        this.game.set(game);
    }
}
