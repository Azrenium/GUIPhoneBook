package util;

import java.sql.*;

public class Connect {
    private static final String URL = "jdbc:mysql://localhost:3306/sakila";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "My$ql_$erveR@2024";

    public static SelectResponse executeSelectQuery(String query) {
        SelectResponse response = new SelectResponse();

        try(Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery())
        {
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                response.columns.add(rsmd.getColumnName(i));
            }

            while(rs.next()) {
                String[] row = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getString(i);
                }
                response.rows.add(row);
            }
        } catch (SQLException ex){
            System.out.println("Exception: " + ex.getMessage());
        }

        return response;
    }

    public static void addContact(String name, String phone, String email, String address) {
        String query = "insert into contact (name, phone_number, email, address) values (?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement ps = null;

        try{
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(query);

            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.setString(4, address);

            int rowsAffected = ps.executeUpdate();

            if(rowsAffected > 0) {
                conn.commit();
                System.out.println("Contact added successfully");
            } else{
                conn.rollback();
                System.out.println("Error adding contact");
            }
        } catch (SQLException ex){
            try {
                if (conn != null) conn.rollback();

                System.out.println("SQL Error: " + ex.getMessage());
            } catch (SQLException rollbackEx) {
                System.out.println("SQL Error: " + rollbackEx.getMessage());
            }
        }
    }

    public static void removeContact(int id){
        String query = "delete from sakila.contact WHERE id = ?";

        try(Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)){
            PreparedStatement pstmt = connection.prepareStatement(query);

            pstmt.setInt(1, id);

            pstmt.executeUpdate();

            System.out.println("Database row deleted for contact ID: " + id);
        } catch (SQLException ex){
            System.out.println("SQL Error: " + ex.getMessage());
        }
    }

    public static void updateContact(String id, String columnName, String newValue){
        String query = "update sakila.contact set " + columnName + " = ? where id = ?";

        Connection conn = null;

        try{
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, newValue);
            ps.setInt(2, Integer.parseInt(id));

            int rowsAffected = ps.executeUpdate();

            if(rowsAffected > 0) {
                conn.commit();
                System.out.println("Contact updated successfully for contact ID: " + id);
            } else{
                conn.rollback();
                System.out.println("Error updating contact.");
            }
        } catch (SQLException e){
            try{
                if(conn != null){
                    conn.rollback();
                }
                System.out.println("SQL Error: " + e.getMessage());
            } catch (SQLException rollbackEx) {
                System.out.println("SQL Error on rollback: " + rollbackEx.getMessage());
            }
        }
    }
}
