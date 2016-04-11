/**
 * Created by kaffefe on 2016-01-04.
 */

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * "projectWhisky";
 * <p>
 * "L2ZFK8uztn36cuqT";
 */


public class GUI extends Application {
    String mySQL_username = "";
    String mySQL_password = "";

    String mySQL_driver = "jdbc:mysql://";
    String mySQL_ip = "";
    String mySQL_port = "3306";
    String mySQL_database = "";
    String mySQL_connectURL = "";
    String mySQL_table = "";


    // 0 is MySQL  -> SQLite
    // 1 is SQLite -> MySQL
    int source_to_destination = -1;

    String SQLite_table;


  /*  String bunchOfErrors = "";
    Label errors = new Label("");
*/
    String[] fileInfo;
    public ArrayList<String> tableInfo = new ArrayList<>();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FileFunctions fileFunctions = new FileFunctions();

        Stage window = primaryStage;
        VBox verticalStack = new VBox(10);
        Scene mainScene = new Scene(verticalStack, 600, 100);

        /******************Copy from X to Y choice*******/
        HBox sqlStuff = new HBox(10);
        Label dynamicLabel = new Label("<- Choose copy option");
        dynamicLabel.setPrefWidth(250);
        final String[] labelBox = new String[]{"From   MySQL-server   to   SQLite", "From   SQLite   to   MySQL-server"};
        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList("MySQL  -> SQLite", "SQLite  -> MySQL"));
        cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                                Number oldValue, Number newValue) {
                dynamicLabel.setText(labelBox[newValue.intValue()]);
                source_to_destination = newValue.intValue();

            }
        });
        sqlStuff.getChildren().addAll(cb, dynamicLabel);
        /*********************MySQL INFO***************************/
        Button setMySQL = new Button("Set MySQL info");
        setMySQL.setOnAction(e -> setupMySQLInfo());
        sqlStuff.getChildren().add(setMySQL);
        verticalStack.getChildren().add(sqlStuff);
        /******************************************************/


        /************ START OF FILE STUFF************/

        Label l1 = new Label("<- Choose local .db file");
        Label l2 = new Label();
        HBox hb1 = new HBox(10);
        hb1.setPrefWidth(mainScene.getWidth());

        Button fileButton = new Button("Choose File");
        fileButton.setOnAction(e -> {
            fileInfo = fileFunctions.chooseFile(window);
            if (fileInfo != null) {
                l2.setText(fileInfo[1]);
            } else {
                l2.setText("No file chosen.");
            }
        });

        l2.setPrefWidth(100);
        hb1.getChildren().addAll(fileButton, l1, l2);


        /************ END OF FILE STUFF************/

        /*********** START OF FILE CONTENTS********/

        Button generateTableList = new Button("Collect Tables");
        tableInfo = new ArrayList<>();
        ChoiceBox<String> choiceBox = new ChoiceBox<>();

        generateTableList.setOnAction(e -> {
            try {
                tableInfo = fileFunctions.getTableInfo(fileInfo[0]);
                setTableInfo(hb1, choiceBox);
            } catch (ClassNotFoundException e1) {
                JOptionPane.showMessageDialog(null, e1.getMessage());
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(null, e1.getMessage());
            } catch (NullPointerException e1) {
                JOptionPane.showMessageDialog(null, "Select a file first!");
            }

        });

        hb1.getChildren().add(generateTableList);

        verticalStack.getChildren().add(hb1);


        Button commit = new Button("Submit");
        commit.setOnAction(e -> {
            // Check which COPY ORDER IS CHOSEN
            masterFunction(source_to_destination);

        });
        verticalStack.getChildren().add(commit);

/*
        Button test = new Button("Test Collected Data");
        test.setOnAction(e -> testFunctions(describedTable));
        verticalStack.getChildren().add(test);

        Label errorHeader = new Label("Errors below");
        errorHeader.prefWidth(mainScene.getWidth());
        errors = new Label(bunchOfErrors);
        errors.setPrefSize(mainScene.getWidth(), 300);
        verticalStack.getChildren().addAll(errorHeader, errors);
*/

        window.setResizable(false);
        window.setScene(mainScene);
        window.setTitle("Copy Database: MySQL <-> SQLite");
        window.show();
    }

    /***********************
     * END OF MAIN WINDOW
     * END OF MAIN WINDOW
     * END OF MAIN WINDOW
     * END OF MAIN WINDOW
     ***********************/
    public void testFunctions(ArrayList<ArrayList<Object>> al) {


        for (int i = 0; i < al.size(); i++) {
            for (int j = 0; j < al.get(i).size(); j++) {
                System.out.print(al.get(i).get(j) + "\t");
            }
            System.out.println("");
        }

/*
        try {
            MySQL.showDatabase(mySQL_table,mySQL_connectURL,mySQL_username,mySQL_password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }

  */
    }

    public void setTableInfo(HBox hb1, ChoiceBox<String> choiceBox) {
        hb1.getChildren().remove(choiceBox);

        choiceBox.getItems().clear();
        for (int i = 0; i < tableInfo.size(); i++) {
            choiceBox.getItems().add(tableInfo.get(i));
        }

        choiceBox.setOnAction(e -> sqlLiteTable(choiceBox));
        hb1.getChildren().add(choiceBox);

    }


    public void setupMySQLInfo() {
        Stage window = new Stage();
        window.setTitle("Input MySQL information");
        VBox vBox = new VBox(5);

        HBox user = new HBox();
        Label l1 = new Label("Username: ");
        l1.setPrefWidth(70);
        TextField username = new TextField(mySQL_username);
        username.setPrefWidth(200);
        user.getChildren().addAll(l1, username);

        HBox pwd = new HBox();
        Label l2 = new Label("Password: ");
        l2.setPrefWidth(70);
        PasswordField password = new PasswordField();
        password.setPrefWidth(200);
        pwd.getChildren().addAll(l2, password);

        HBox url = new HBox();
        Label l3 = new Label("IP: ");
        l3.setPrefWidth(70);
        TextField ip = new TextField(mySQL_ip);
        ip.setPrefWidth(200);
        Label l4 = new Label(" Port: ");
        TextField port = new TextField(mySQL_port);
        port.setMaxWidth(70);
        url.getChildren().addAll(l3, ip, l4, port);

        HBox db = new HBox();
        Label l5 = new Label("Database: ");
        l5.setPrefWidth(70);
        TextField database = new TextField(mySQL_database);
        database.setPrefWidth(200);
        db.getChildren().addAll(l5, database);

        HBox tableStuff = new HBox();
        Label l6 = new Label("Table: ");
        l6.setPrefWidth(70);
        TextField table = new TextField(mySQL_table);
        table.setPrefWidth(200);
        tableStuff.getChildren().addAll(l6, table);


        HBox buttons = new HBox(70);

        Label saveLabel = new Label("");
        saveLabel.setPrefWidth(200);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            saveInfo(username.getText(), password.getText(), ip.getText(), port.getText(), database.getText(), table.getText());
            saveLabel.setText("Information saved, press Close.");
        });
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> window.close());
        buttons.getChildren().addAll(saveButton, saveLabel, closeButton);


        vBox.getChildren().addAll(user, pwd, url, db, tableStuff, buttons);
        Scene scene = new Scene(vBox, 500, 300);


        window.setResizable(false);
        window.setScene(scene);
        window.show();
    }

    // Set mySQL_table to current selected dropDown String
    public void sqlLiteTable(ChoiceBox<String> choiceBox) {
        SQLite_table = choiceBox.getValue();
    }

    // Set MySQL information
    public void saveInfo(String username, String password, String ip, String port, String db, String table) {
        mySQL_username = username;
        mySQL_password = password;
        mySQL_ip = ip;
        mySQL_port = port;
        mySQL_database = db;
        mySQL_table = table;
    }

    public void masterFunction(int x) {
        ArrayList<ArrayList<Object>> queryResult;
        ArrayList<ArrayList<Object>> describedTable;


        MySQL mysql = new MySQL();
        SQLite sqlite = new SQLite();
        mySQL_connectURL = mySQL_driver + mySQL_ip + ":" + mySQL_port + "/" + mySQL_database;

        //System.out.println(mySQL_connectURL);

        String sQLiteDB;
        String sqliteTable;
        if (fileInfo != null) { // FILE CHOSEN
            sQLiteDB = fileInfo[0];

            if (SQLite_table != null) { // TABLE CHOSEN
                sqliteTable = SQLite_table;
            }

            else { // FILE CHOSEN BUT NOT TABLE CHOSEN
                sqliteTable = mySQL_table;
            }
        }

        else { // NOTHING CHOSEN
            sQLiteDB = mySQL_database + ".db";
            sqliteTable = mySQL_table;
        }


        // If local file is chosen but not table


        switch (x) {
            case 0: // MYSQL -> SQLITE
                try {
                    describedTable = mysql.getSchema(mySQL_table, mySQL_connectURL, mySQL_username, mySQL_password);
                    sqlite.createTable(describedTable, sQLiteDB, mySQL_table);

                    queryResult = mysql.Select(mySQL_table, mySQL_connectURL, mySQL_username, mySQL_password);
                    sqlite.Insert(queryResult, sQLiteDB, mySQL_table);

                    JOptionPane.showMessageDialog(null,"Insert successful");

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "MySQL ERROR: " + e.getMessage());
                } catch (ClassNotFoundException e) {
                    JOptionPane.showMessageDialog(null, "SQLite ERROR: " + e.getMessage());
                }

                break;

            case 1: // SQLITE -> MYSQL

                try {

                    ArrayList<ArrayList<Object>> toBeInserted = sqlite.Select(sQLiteDB, sqliteTable);
                    mysql.Insert(toBeInserted, mySQL_table, mySQL_connectURL, mySQL_username, mySQL_password);
                    JOptionPane.showMessageDialog(null,"Insert successful");

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "MySQL ERROR: " + e.getMessage());
                } catch (ClassNotFoundException e) {
                    JOptionPane.showMessageDialog(null, "SQLite ERROR: " + e.getMessage());
                }

                break;


            default:

                JOptionPane.showMessageDialog(null, "You need to choose an option, Top-left Corner.");
                break;
        }
    }
}


