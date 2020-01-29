package com.jonathanlouis.data;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Game {
    private SimpleIntegerProperty id;
    private SimpleStringProperty title;
    private SimpleStringProperty genre;
    private SimpleIntegerProperty publisher;

    public Game() {
        id = new SimpleIntegerProperty();
        title = new SimpleStringProperty();
        genre = new SimpleStringProperty();
        publisher = new SimpleIntegerProperty();
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

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getGenre() {
        return genre.get();
    }

    public SimpleStringProperty genreProperty() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }

    public int getPublisher() {
        return publisher.get();
    }

    public SimpleIntegerProperty publisherProperty() {
        return publisher;
    }

    public void setPublisher(int publisher) {
        this.publisher.set(publisher);
    }
}
