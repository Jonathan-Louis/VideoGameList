package com.jonathanlouis.data;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//singleton class to control data management in database
public class DataSource {

    //SQL Constants
    public static final String DB_NAME = "VideoGameList.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\Jonathan\\IdeaProjects\\VideoGameList\\" + DB_NAME;

    //tables
    public static final String TABLE_PUBLISHERS = "publishers";
    public static final String TABLE_GAMES = "games";
    public static final String TABLE_GAMEINFO = "gameinfo";

    //columns
    public static final String COLUMN_PUBLISHER_ID = "_id";
    public static final String COLUMN_PUBLISHER_NAME = "name";
    public static final int INDEX_PUBLISHER_ID = 1;
    public static final int INDEX_PUBLISHER_NAME = 2;

    public static final String COLUMN_GAMES_ID = "_id";
    public static final String COLUMN_GAMES_TITLE = "title";
    public static final String COLUMN_GAMES_GENRE = "genre";
    public static final String COLUMN_GAMES_PUBLISHER = "publisher";
    public static final int INDEX_GAMES_ID = 1;
    public static final int INDEX_GAMES_TITLE = 2;
    public static final int INDEX_GAMES_GENRE = 3;
    public static final int INDEX_GAMES_PUBLISHER = 4;

    public static final String COLUMN_GAMEINFO_ID = "_id";
    public static final String COLUMN_GAMEINFO_DESCRIPTION = "description";
    public static final String COLUMN_GAMEINFO_FINISHED = "finished";
    public static final String COLUMN_GAMEINFO_GAME = "game";
    public static final int INDEX_GAMEINFO_ID = 1;
    public static final int INDEX_GAMEINFO_DESCRIPTION = 2;
    public static final int INDEX_GAMEINFO_FINISHED = 3;
    public static final int INDEX_GAMEINFO_GAME = 4;

    //ORDER BY constants
    public static final int ORDER_BY_NONE = 1;
    public static final int ORDER_BY_ASC = 2;
    public static final int ORDER_BY_DESC = 3;

    public static final String INSERT_INTO_PUBLISHER = "INSERT INTO " + TABLE_PUBLISHERS + " (" +  COLUMN_PUBLISHER_NAME
            + " )" + " VALUES(?)";

    public static final String QUERY_PUBLISHER_FOR_ID = "SELECT " + COLUMN_PUBLISHER_ID + " FROM " + TABLE_PUBLISHERS + " WHERE " + COLUMN_PUBLISHER_NAME +
            " = ?";

    public static final String QUERY_PUBLISHER_WHERE_NAME = "SELECT " + COLUMN_PUBLISHER_NAME + " FROM " +
            TABLE_PUBLISHERS + " WHERE " + COLUMN_PUBLISHER_NAME + " = ?";

    public static final String INSERT_INTO_GAMES ="INSERT INTO " + TABLE_GAMES + "(" + COLUMN_GAMES_TITLE + ", " +
            COLUMN_GAMES_GENRE + ", " + COLUMN_GAMES_PUBLISHER + ") VALUES (?, ?, ?)";

    public static final String QUERY_GAMES_WHERE_TITLE = "SELECT " + COLUMN_GAMES_TITLE + " FROM " + TABLE_GAMES +
            " WHERE " + COLUMN_GAMES_TITLE + " = ?";

    public static final String INSERT_INTO_GAMEINFO = "INSERT INTO " + TABLE_GAMEINFO + "(" + COLUMN_GAMEINFO_DESCRIPTION
            + ", " + COLUMN_GAMEINFO_GAME + ") VALUES (?, ?)";

    public static final String QUERY_GAMES_FOR_ID = "SELECT " + COLUMN_GAMES_ID + " FROM " + TABLE_GAMES + " WHERE " +
            COLUMN_GAMES_TITLE + " = ?";

    public static final String QUERY_GAMEIFO_WHERE_GAMEID = "SELECT " + COLUMN_GAMEINFO_ID + " FROM " + TABLE_GAMEINFO +
             " WHERE " + COLUMN_GAMEINFO_GAME + " = ?";

    public static final String QUERY_GAMES_WHERE_PUBLISHER = "SELECT * FROM " + TABLE_GAMES + " WHERE " +
            COLUMN_GAMES_PUBLISHER + " = ?" + " ORDER BY " + COLUMN_GAMES_TITLE + " ASC";

    public static final String QUERY_GAMEINFO_FOR_DESCRIPTION_WHERE_GAMEID = "SELECT " + COLUMN_GAMEINFO_DESCRIPTION
            + " FROM " + TABLE_GAMEINFO + " WHERE " + COLUMN_GAMEINFO_GAME + " = ?";

    public static final String QUERY_GAMES_FOR_ALL_WHERE_TITLE = "SELECT * FROM " + TABLE_GAMES + " WHERE "
            + COLUMN_GAMES_TITLE + " = ?";

    public static final String DELETE_GAME_FROM_TABLE = "DELETE FROM " + TABLE_GAMES + " WHERE " + COLUMN_GAMES_TITLE
            + " = ?";

    public static final String DELETE_PUBLISHER_FROM_TABLE = "DELETE FROM " + TABLE_PUBLISHERS + " WHERE "
            + COLUMN_PUBLISHER_NAME + " = ?";

    public static final String DELETE_GAMEINFO_FROM_TABLE = "DELETE FROM " + TABLE_GAMEINFO + " WHERE " +
            COLUMN_GAMEINFO_DESCRIPTION + " = ?";

    public static final String QUERY_PUBLISHER_FOR_NAME_WHERE_ID = "SELECT " + COLUMN_PUBLISHER_NAME + " FROM "
            + TABLE_PUBLISHERS + " WHERE " + COLUMN_PUBLISHER_ID + " = ?";



    private Connection connection;

    private PreparedStatement insertIntoPublisher;
    private PreparedStatement queryPublisherByName;
    private PreparedStatement queryGamesByTitle;
    private PreparedStatement insertIntoGames;
    private PreparedStatement queryPublisherForID;
    private PreparedStatement insertIntoGameInfo;
    private PreparedStatement queryGameInfoByGameID;
    private PreparedStatement queryGamesForID;
    private PreparedStatement queryGamesByPublisher;
    private PreparedStatement queryGameInfoByGameIDForDescription;
    private PreparedStatement queryGamesByTitleForAll;
    private PreparedStatement deleteGameByTitle;
    private PreparedStatement deletePublisherByName;
    private PreparedStatement deleteGameInfoByDescription;
    private PreparedStatement queryPublisherById;


    private static DataSource instance = new DataSource();

    //singleton
    private DataSource(){
    }

    public static DataSource getInstance(){
        return instance;
    }

    //create connection to DB and prepared statements
    public boolean open(){
        try{
            connection = DriverManager.getConnection(CONNECTION_STRING);
            insertIntoPublisher = connection.prepareStatement(INSERT_INTO_PUBLISHER);
            queryPublisherByName = connection.prepareStatement(QUERY_PUBLISHER_WHERE_NAME);
            insertIntoGames = connection.prepareStatement(INSERT_INTO_GAMES);
            queryGamesByTitle = connection.prepareStatement(QUERY_GAMES_WHERE_TITLE);
            queryPublisherForID = connection.prepareStatement(QUERY_PUBLISHER_FOR_ID);
            queryGamesForID = connection.prepareStatement(QUERY_GAMES_FOR_ID);
            queryGameInfoByGameID = connection.prepareStatement(QUERY_GAMEIFO_WHERE_GAMEID);
            insertIntoGameInfo = connection.prepareStatement(INSERT_INTO_GAMEINFO);
            queryGamesByPublisher = connection.prepareStatement(QUERY_GAMES_WHERE_PUBLISHER);
            queryGameInfoByGameIDForDescription = connection.prepareStatement(QUERY_GAMEINFO_FOR_DESCRIPTION_WHERE_GAMEID);
            queryGamesByTitleForAll = connection.prepareStatement(QUERY_GAMES_FOR_ALL_WHERE_TITLE);
            deleteGameByTitle = connection.prepareStatement(DELETE_GAME_FROM_TABLE);
            deletePublisherByName = connection.prepareStatement(DELETE_PUBLISHER_FROM_TABLE);
            deleteGameInfoByDescription = connection.prepareStatement(DELETE_GAMEINFO_FROM_TABLE);
            queryPublisherById = connection.prepareStatement(QUERY_PUBLISHER_FOR_NAME_WHERE_ID);

            return true;
        } catch (SQLException e){
            System.out.println("Could not connect to database: " + e.getMessage());
            return false;
        }
    }

    //close all connections to DB that are open
    public void close(){
        try{
            if(insertIntoPublisher != null){
                insertIntoPublisher.close();
            }
            if(queryPublisherByName != null){
                queryPublisherByName.close();
            }
            if(insertIntoGames != null){
                insertIntoGames.close();
            }
            if(queryGamesByTitle != null){
                queryGamesByTitle.close();
            }
            if(queryPublisherForID != null){
                queryPublisherForID.close();
            }
            if(queryGameInfoByGameID != null){
                queryGameInfoByGameID.close();
            }
            if(queryGamesForID != null){
                queryGamesForID.close();
            }
            if(insertIntoGameInfo != null){
                insertIntoGameInfo.close();
            }
            if(queryGamesByPublisher != null){
                queryGamesByPublisher.close();
            }
            if(queryGameInfoByGameIDForDescription != null){
                queryGameInfoByGameIDForDescription.close();
            }
            if(queryGamesByTitleForAll != null){
                queryGamesByTitleForAll.close();
            }
            if(deleteGameByTitle != null){
                deleteGameByTitle.close();
            }
            if(deletePublisherByName != null){
                deletePublisherByName.close();
            }
            if(deleteGameInfoByDescription != null){
                deleteGameInfoByDescription.close();
            }
            if(queryPublisherById != null){
                queryPublisherById.close();
            }

            if(connection != null){
                connection.close();
            }
        } catch (SQLException e){
            System.out.println("Error closing database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //insert data from text file to create database
    //used only when database does not exist
    public void insertFromTextToDatabase(){
        try{
            Path path = Paths.get("C:\\Users\\Jonathan\\IdeaProjects\\VideoGameList\\gamelist");
            BufferedReader bufferedReader = Files.newBufferedReader(path);
            bufferedReader.readLine(); //throw out first txt file description line
            String line;
            while ((line = bufferedReader.readLine()) != null){
                String publisher = line;
                String title = bufferedReader.readLine();
                String genre = bufferedReader.readLine();
                String description = bufferedReader.readLine();

                //check if publisher already in DB
                queryPublisherByName.setString(1, publisher);
                ResultSet resultSet = queryPublisherByName.executeQuery();
                if (!resultSet.next()){
                    insertIntoPublisher.setString(1, publisher);
                    insertIntoPublisher.execute();
                }

                //checking if game already in DB
                queryGamesByTitle.setString(1, title);
                resultSet = queryGamesByTitle.executeQuery();
                if(!resultSet.next()){
                    queryPublisherForID.setString(1, publisher);
                    ResultSet pubID = queryPublisherForID.executeQuery();
                    insertIntoGames.setString(1, title);
                    insertIntoGames.setString(2, genre);
                    insertIntoGames.setInt(3, pubID.getInt(1));
                    insertIntoGames.execute();
                }

                //checking if game already in DB
                queryGamesForID.setString(1, title);
                ResultSet gameID = queryGamesForID.executeQuery();
                queryGameInfoByGameID.setInt(1, gameID.getInt(1));
                resultSet = queryGameInfoByGameID.executeQuery();
                if(!resultSet.next()){
                    insertIntoGameInfo.setString(1, description);
                    insertIntoGameInfo.setInt(2 , gameID.getInt(1));
                    insertIntoGameInfo.execute();
                }

            }

        } catch (IOException | SQLException e){
            System.out.println("Error reading text file to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //selecting all publishers
    public List<Publisher> queryPublishers(int sortOrder){
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_PUBLISHERS);
        if(sortOrder != ORDER_BY_NONE){
            sb.append(" ORDER BY ");
            sb.append(COLUMN_PUBLISHER_NAME);
            sb.append(" COLLATE NOCASE ");
            if(sortOrder == ORDER_BY_DESC){
                sb.append("DESC");
            } else{
                sb.append("ASC");
            }
        }

        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sb.toString())){

            List<Publisher> publishers = new ArrayList<>();
            while (resultSet.next()){
                Publisher publisher = new Publisher();
                publisher.setId(resultSet.getInt(INDEX_PUBLISHER_ID));
                publisher.setName(resultSet.getString(INDEX_PUBLISHER_NAME));
                publishers.add(publisher);
            }

            return publishers;
        } catch (SQLException e){
            System.out.println("Failed to load publisher list: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    //selecting all games
    public List<Game> queryGames(int sortOrder){
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_GAMES);
        if(sortOrder != ORDER_BY_NONE){
            sb.append(" ORDER BY ");
            sb.append(COLUMN_GAMES_TITLE);
            sb.append(" COLLATE NOCASE ");
            if(sortOrder == ORDER_BY_DESC){
                sb.append("DESC");
            } else{
                sb.append("ASC");
            }
        }

        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sb.toString())){

            List<Game> games = new ArrayList<>();
            while (resultSet.next()){
                Game game = new Game();
                game.setId(resultSet.getInt(INDEX_GAMES_ID));
                game.setTitle(resultSet.getString(INDEX_GAMES_TITLE));
                game.setGenre(resultSet.getString(INDEX_GAMES_GENRE));
                game.setPublisher(resultSet.getInt(INDEX_GAMES_PUBLISHER));
                games.add(game);
            }

            return games;
        } catch (SQLException e){
            System.out.println("Failed to load publisher list: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    //finding all games by 1 publisher
    public List<Game> queryGamesByPublisher(Publisher publisher){

        try {
            queryGamesByPublisher.setInt(1, publisher.getId());
            ResultSet resultSet = queryGamesByPublisher.executeQuery();

            List<Game> games = new ArrayList<>();
            while(resultSet.next()){
                Game game =new Game();
                game.setId(resultSet.getInt(INDEX_GAMES_ID));
                game.setTitle(resultSet.getString(INDEX_GAMES_TITLE));
                game.setGenre(resultSet.getString(INDEX_GAMES_GENRE));
                game.setPublisher(resultSet.getInt(INDEX_GAMES_PUBLISHER));
                games.add(game);
            }

            return games;
        } catch (SQLException e){
            System.out.println("Query Failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    //getting game description for a game
    public String gameDescription(Game game){
        try{
            queryGameInfoByGameIDForDescription.setInt(1, game.getId());
            ResultSet resultSet = queryGameInfoByGameIDForDescription.executeQuery();

            String output = "";
            while(resultSet.next()){
                output = resultSet.getString(1);
            }

            return output;
        } catch (SQLException e){
            System.out.println("Query Failed: " +  e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    //insert new game
    public boolean addNewGame(NewGame newGame){

        try {
            //only commit if all tables updated correctly
            connection.setAutoCommit(false);

            //check if publisher already in DB
            queryPublisherByName.setString(1, newGame.getPublisher());
            ResultSet resultSet = queryPublisherByName.executeQuery();
            if (!resultSet.next()){
                insertIntoPublisher.setString(1, newGame.getPublisher());
                insertIntoPublisher.execute();
            }

            //checking if game already in DB
            queryGamesByTitle.setString(1, newGame.getTitle());
            resultSet = queryGamesByTitle.executeQuery();
            if(!resultSet.next()){
                queryPublisherForID.setString(1, newGame.getPublisher());
                ResultSet pubID = queryPublisherForID.executeQuery();
                insertIntoGames.setString(1, newGame.getTitle());
                insertIntoGames.setString(2, newGame.getGenre());
                insertIntoGames.setInt(3, pubID.getInt(1));
                insertIntoGames.execute();
            }

            //checking if game already in DB
            queryGamesForID.setString(1, newGame.getTitle());
            ResultSet gameID = queryGamesForID.executeQuery();
            queryGameInfoByGameID.setInt(1, gameID.getInt(1));
            resultSet = queryGameInfoByGameID.executeQuery();
            if(!resultSet.next()){
                insertIntoGameInfo.setString(1, newGame.getDescription());
                insertIntoGameInfo.setInt(2 , gameID.getInt(1));
                insertIntoGameInfo.execute();
            }

            //commit new game
            connection.commit();

            return true;
        } catch(Exception e){
            System.out.println("Could not add new game: " + e.getMessage());
            System.out.println("Rolling back database");
            try{
                //rollback if add game failed
                connection.rollback();
            } catch (SQLException er){
                System.out.println("Failed to roll back: " + er.getMessage());
            }
            return false;
        } finally {
            try{
                //reset connection auto commit to default(true)
                connection.setAutoCommit(true);
            } catch (SQLException e){
                System.out.println("Failed to reset auto commit in connection: " + e.getMessage());
                e.printStackTrace();
            }
        }

    }

    //create a new game class for a given game
    public NewGame getNewGame(Game game){
        if(game == null){
            return null;
        }

        try{
            //checking if game is in DB
            queryGamesByTitleForAll.setString(1, game.getTitle());
            ResultSet resultSet = queryGamesByTitleForAll.executeQuery();
            int publisher = resultSet.getInt(INDEX_GAMES_PUBLISHER);


            if(resultSet.next()){
                String title = game.getTitle();
                String genre = game.getGenre();


                //get publisher string
                queryPublisherById.setInt(1, publisher);
                resultSet = queryPublisherById.executeQuery();
                String publisherName = resultSet.getString(1);

                //get description
                queryGameInfoByGameIDForDescription.setInt(1, game.getId());
                resultSet = queryGameInfoByGameIDForDescription.executeQuery();
                String description = resultSet.getString(1);


                NewGame newGame = new NewGame();
                newGame.setPublisher(publisherName);
                newGame.setTitle(title);
                newGame.setGenre(genre);
                newGame.setDescription(description);

                return newGame;
            } else{
                System.out.println("Game not found");
                return null;
            }

        } catch (SQLException e){
            System.out.println("Query Failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }



    //remove game from database
    public boolean removeGame(NewGame game){

        try {
            //commit delete if all tables processed
            connection.setAutoCommit(false);

            //get publisher id
            queryGamesByTitleForAll.setString(1, game.getTitle());
            ResultSet resultSet = queryGamesByTitleForAll.executeQuery();
            int publisher = resultSet.getInt(INDEX_GAMES_PUBLISHER);

            //remove game from Games Table
            deleteGameByTitle.setString(1, game.getTitle());
            deleteGameByTitle.execute();

            //remove game from GameInfo table
            deleteGameInfoByDescription.setString(1, game.getDescription());
            deleteGameInfoByDescription.execute();

            //check if publisher has another game
            //if false delete publisher
            queryGamesByPublisher.setInt(1, publisher);
            resultSet = queryGamesByPublisher.executeQuery();
            if(!resultSet.next()){
                deletePublisherByName.setString(1, game.getPublisher());
                deletePublisherByName.execute();
            }

            //commit delete
            connection.commit();

            return true;

        } catch (Exception e){
            //roll database back if delete failed
            System.out.println("Failed to delete game: " + e.getMessage());
            e.printStackTrace();
            System.out.println("Rolling database back");
            try{
                connection.rollback();
            } catch (SQLException er){
                System.out.println("Error rolling back: " + er.getMessage());
                er.printStackTrace();
            }
            return false;
        } finally {
            //reset connection auto commit to default(true)
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e){
                System.out.println("Error resetting connection autocommit: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    //update game info
    public boolean updateGameInfo(NewGame prevGame, NewGame newGame) {
        if (newGame == null) {
            return false;
        }

        removeGame(prevGame);

        return addNewGame(newGame);
    }


}