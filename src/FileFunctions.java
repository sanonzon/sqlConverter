import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by kaffefe on 2016-01-04.
 */
public class FileFunctions {
    public  String[] chooseFile(Stage window) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQlite Files", "*.db"));
        File selectedFile = fileChooser.showOpenDialog(window);
        String[] info = new String[2];
        if (selectedFile != null) {
            info[0] = selectedFile.getAbsolutePath();
            info[1] = selectedFile.getName();
            return info;
        } else {
            return null;
        }
    }


    public  ArrayList<String> getTableInfo(String s) throws ClassNotFoundException, SQLException {
        ArrayList<String> arrayList = new ArrayList<>();

        Connection c = null;
        Statement stmt = null;
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + s);
        c.setAutoCommit(false);
        stmt = c.createStatement();

        DatabaseMetaData md = c.getMetaData();

        ResultSet rs = md.getTables(null, null, "%", null);
        while (rs.next()) {
            arrayList.add(rs.getObject(3).toString());
        }



        stmt.close();
        c.commit();
        c.close();
        return arrayList;
    }


}
