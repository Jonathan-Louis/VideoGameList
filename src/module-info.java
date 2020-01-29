module VideoGameList {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base;

    exports com.jonathanlouis;
    opens com.jonathanlouis;
    opens com.jonathanlouis.data;
}