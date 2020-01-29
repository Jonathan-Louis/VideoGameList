package com.jonathanlouis.data;

import javafx.beans.property.SimpleStringProperty;

public class NewGame {
    SimpleStringProperty publisher;
    SimpleStringProperty title;
    SimpleStringProperty genre;
    SimpleStringProperty description;

    public NewGame() {
        publisher = new SimpleStringProperty();
        title = new SimpleStringProperty();
        genre = new SimpleStringProperty();
        description = new SimpleStringProperty();
    }

    public String getPublisher() {
        return publisher.get();
    }

    public SimpleStringProperty publisherProperty() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher.set(publisher);
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

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }
}
