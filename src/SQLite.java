import java.sql.*;
import java.util.ArrayList;

/**
 * Created by kaffeknark on 2015-12-20.
 */
public class SQLite {

    public String fixSytnax(String s) {
        if (s.trim().equals("PRI")) {
            return "PRIMARY KEY";
        } else if (s.trim().equals("NO")) {
            return "";
        } else
            return s;

    }


    public void Insert(ArrayList<ArrayList<Object>> arrayList, String db, String tableName) throws SQLException, ClassNotFoundException {
        Connection c = null;
        Statement stmt = null;
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + db);
        c.setAutoCommit(false);
        stmt = c.createStatement();


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

            stmt.executeUpdate(data); // Insert once per row.
        }

        // Close all the connections
        stmt.close();
        c.commit();
        c.close();

    }

    public ArrayList<ArrayList<Object>> Select(String db, String tableName) throws SQLException, ClassNotFoundException {
        /**
         * Change path to your own database File
         * Set name of the table you want to collect from
         */

        Connection c = null;
        Statement stmt = null;
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + db);
        c.setAutoCommit(false);
        stmt = c.createStatement();


        String s = "Select * from " + tableName;
        ResultSet rs = stmt.executeQuery(s);


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

        // Close all the connections
        stmt.close();
        c.commit();
        c.close();

        return fetched;
    }
    public void createTable(ArrayList<ArrayList<Object>> arrayList, String db, String tableName) throws SQLException, ClassNotFoundException {
        Connection c = null;
        Statement stmt = null;
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + db);
        c.setAutoCommit(false);
        stmt = c.createStatement();


        String sql = "CREATE TABLE \"" + tableName + "\" ( ";


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



        // Close all the connections
        stmt.close();
        c.commit();
        c.close();



    }




}