import java.sql.*;
import java.util.ArrayList;

/**
 * Created by kaffefe on 2016-01-01.
 */
public class MySQL {
    /**
     * Insert all the info into database.
     * From the
     */
    public String fixSytnax(String s) {
        if (s.trim().equals("PRI")) {
            return "PRIMARY KEY";
        }
        else if (s.trim().equals("NO")){
            return "";
        }
        else
            return s;

    }

    public void Insert(ArrayList<ArrayList<Object>> arrayList, String tableName, String url, String username, String password) throws SQLException {
        Connection c = null;
        Statement stmt = null;

        c = DriverManager.getConnection(url, username, password);
        c.setAutoCommit(false);
        stmt = c.createStatement();

        /**
         * First row in the arrayList contains column names.
         * So basically:
         * INSERT INTO appended_table_name ( all the different columns ) VALUES ( all the values on the correct position )
         * We create a bunch of these and lastly, after the loop, we commit changes to the table.
         * Columns starts at 1 and arrays at 0.
         */

        String sql = "INSERT INTO " + tableName + " ( "; // PRE-loop of column names
        for (int i = 0; i < arrayList.get(0).size(); i++) {
            if (i == arrayList.get(0).size() - 1) { // Last column, no more comma's needed
                sql += arrayList.get(0).get(i) + " ) ";
            } else {
                sql += arrayList.get(0).get(i) + " , ";
            }
        }
        // End of column name loop, continue with actual data
        sql += "VALUES ( ";

        // Loops for data Start on row 1 for actual data
        for (int i = 1; i < arrayList.size(); i++) {
            String data = sql;   // Reset String for new values.
            for (int j = 0; j < arrayList.get(i).size(); j++) {
                if (j == arrayList.get(i).size() - 1) { // Last data, add ')'
                    data += "\'" + arrayList.get(i).get(j) + "\' );";
                } else {
                    data += "\'" + arrayList.get(i).get(j) + "\', ";
                }
            }
            //System.out.println(data);
            stmt.executeUpdate(data); // Insert once per row.
        }
        // Commit changes
        c.commit();

        // Close all the connections
        stmt.close();
        c.close();

    }

    public ArrayList<ArrayList<Object>> getSchema(String tableName, String url, String username, String password) throws SQLException {
        ArrayList<ArrayList<Object>> result = new ArrayList<>();



        Connection c = null;
        Statement stmt = null;
        c = DriverManager.getConnection(url, username, password);
        c.setAutoCommit(false);
        stmt = c.createStatement();

        String sql = "DESCRIBE " + tableName;


        ResultSet resultSet = stmt.executeQuery(sql);
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (resultSet.next()) {
            ArrayList<Object> temp = new ArrayList<>();
            for (int i = 1; i < columnsNumber; i++) {
                if (resultSet.getObject(i) != null) {
                   // System.out.println(resultSet.getObject(i));
                    // FIX ABBREVIATIONS TO PROPER SQL SYNTAX
                    String s1 = fixSytnax(resultSet.getObject(i).toString());
                    temp.add(s1);
                }

            }
            result.add(temp);

        }


        // Commit changes
        c.commit();

        // Close all the connections
        stmt.close();
        c.close();
        return result;
    }


    public ArrayList<ArrayList<Object>> Select(String tableName, String url, String username, String password) throws SQLException {
        Connection c = null;
        Statement stmt = null;

        c = DriverManager.getConnection(url, username, password);
        c.setAutoCommit(false);
        stmt = c.createStatement();

        String sql = "SELECT * FROM " + tableName; // PRE-loop of column names
        ResultSet rs = stmt.executeQuery(sql);

        ArrayList<ArrayList<Object>> fetched = new ArrayList<>();
        /**
         *  Saves collected data to temporary ArrayList and adds that ArrayList to the collection.
         *  Returns collection on completion
         */
        while (rs.next()) {
            ResultSetMetaData rsmd = rs.getMetaData();
            if (fetched.size() == 0) {
                // Get column names
                ArrayList<Object> temp = new ArrayList<>();
                for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
                    temp.add(rsmd.getColumnName(i));
                }

                fetched.add(temp);
            }

            ArrayList<Object> temp = new ArrayList<>();
            for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
                temp.add(rs.getObject(i));
            }

            fetched.add(temp);
        }


        // Commit changes
        c.commit();

        // Close all the connections
        stmt.close();
        c.close();

        return fetched;
    }

    public void createTable(ArrayList<ArrayList<Object>> arrayList, String tableName, String url, String username, String password) throws SQLException {
        Connection c = null;
        Statement stmt = null;

        c = DriverManager.getConnection(url, username, password);
        c.setAutoCommit(false);
        stmt = c.createStatement();

        String sql = "CREATE TABLE " + tableName + " ( "; // PRE-loop of column names
        for (int i = 0; i < arrayList.size(); i++) {
            for (int j = 0; j < arrayList.get(i).size(); j++) {
                if (i == arrayList.size() - 1 && j == arrayList.get(i).size() - 1) {
                    sql += arrayList.get(i).get(j) + ") ";
                } else if (j == arrayList.get(i).size() - 1) { // Last of row, add a comma
                    sql += arrayList.get(i).get(j) + ", ";
                } else {
                    sql += arrayList.get(i).get(j) + " ";
                }
            }
        }


        stmt.executeUpdate(sql);

        // Commit changes
        c.commit();

        // Close all the connections
        stmt.close();
        c.close();

    }
}
