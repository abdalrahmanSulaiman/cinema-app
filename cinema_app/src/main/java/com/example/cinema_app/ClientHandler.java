package com.example.cinema_app;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.io.*;
import java.net.Socket;
import java.sql.*;

public class ClientHandler extends Thread{
    private Socket serviceSocket = null;
    private ResultSet movies = null;
    String[] request;
    public ClientHandler(Socket client){
        serviceSocket = client;
    }
    public void getMyBooking(){
        String sql = "with query1 as (SELECT \"movieId\",seat,\"dateTime\", email,ROW_NUMBER () OVER (ORDER BY \"bookingId\")" +
                " FROM bookings WHERE email = (?) ORDER BY \"bookingId\"), query2 AS (select * from movies) SELECT query1.\"movieId\"" +
                ",seat,\"dateTime\",email,row_number,picture, \"movieName\" ,length FROM query1 INNER JOIN query2 on query1.\"movieId\" =" +
                " query2.\"movieId\";";
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost/cinemadb";
            String user = ""; //type the name of the database user
            String pass = ""; //type the password of the database user
            Connection con = DriverManager.getConnection(url,user,pass);
            PreparedStatement prepStatement = con.prepareStatement(sql);
            prepStatement.setString(1,request[1]);
            ResultSet rs = prepStatement.executeQuery();
            ObjectOutputStream outputStream = new ObjectOutputStream(new BufferedOutputStream(serviceSocket.getOutputStream()));
            /*outputStream.writeObject(rs);*/
            outputStream.flush();
            RowSetFactory aFactory = RowSetProvider.newFactory();
            CachedRowSet crs = aFactory.createCachedRowSet();
            crs.populate(rs);
            outputStream.writeObject(crs);
            con.close();
            rs.close();
            crs.close();
            outputStream.close();
        } catch (ClassNotFoundException | SQLException | IOException e) {
            System.out.println(e);
        }

    }
    public void getMovies(){
        try {
            Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost/cinemadb";
        String user = "";
        String pass = "";
        Connection con = DriverManager.getConnection(url,user,pass);
        String query = "SELECT \"movieId\", length,\"movieName\" ,picture FROM MOVIES;";
        ResultSet rs = con.createStatement().executeQuery(query);
        ObjectOutputStream outputStream = new ObjectOutputStream(new BufferedOutputStream(serviceSocket.getOutputStream()));
            outputStream.flush();
            RowSetFactory aFactory = RowSetProvider.newFactory();
            CachedRowSet crs = aFactory.createCachedRowSet();
            crs.populate(rs);
            outputStream.writeObject(crs);
            con.close();
            rs.close();
            crs.close();
            outputStream.close();
        } catch (ClassNotFoundException | SQLException | IOException e) {
            System.out.println(e);
        }
    }
    public void getUpcomingMovies(){
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost/cinemadb";
            String user = "";
            String pass = "";
            Connection con = DriverManager.getConnection(url,user,pass);
            String query = "SELECT \"upcomingMovieId\", length,\"movieName\" ,picture FROM \"upcomingMovies\";";
            ResultSet rs = con.createStatement().executeQuery(query);
            ObjectOutputStream outputStream = new ObjectOutputStream(new BufferedOutputStream(serviceSocket.getOutputStream()));
            outputStream.flush();
            RowSetFactory aFactory = RowSetProvider.newFactory();
            CachedRowSet crs = aFactory.createCachedRowSet();
            crs.populate(rs);
            outputStream.writeObject(crs);
            con.close();
            rs.close();
            crs.close();
            outputStream.close();
        } catch (ClassNotFoundException | SQLException | IOException e) {
            System.out.println(e);
        }
    }

    public void user(){
    String signup = "INSERT INTO users (email,password) VALUES (?,?);";
    String login = "SELECT password FROM users WHERE email = (?);";
        try {



            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost/cinemadb";
            String user = "";
            String pass = "";
            Connection conn = DriverManager.getConnection(url, user,pass);
            PreparedStatement statement = null;
            String password="";

            if (request[1].equals("signup")) {
                statement = conn.prepareStatement(signup);
                statement.setString(1, request[2]);
                statement.setString(2, request[3]);
                statement.executeUpdate();
            } else if (request[1].equals("login")) {
                statement = conn.prepareStatement(login);
                statement.setString(1, request[2]);
                //statement.setString(2, request[2]);
                ResultSet receivedPassword = statement.executeQuery();

                while(receivedPassword.next()) {
                    password = receivedPassword.getString("password");
                }
                PrintWriter out;
                out = new PrintWriter(serviceSocket.getOutputStream(),true);
                if(request[3].equals(password)){
                    out.println("true");
                }else{
                    out.println("false");
                }
            }


            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void getTimings() {
        try {
            String sql = "SELECT \"dateTime\" FROM \"moviesTimings\" WHERE \"movieId\" = (?);";
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost/cinemadb";
            String user = "";
            String pass = "";
            Connection conn = DriverManager.getConnection(url, user, pass);
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(request[1]));
            ResultSet rs = statement.executeQuery();
            String result="";
            while (rs.next()){
                result += rs.getString("dateTime")+";";
            }
            PrintWriter out = new PrintWriter(serviceSocket.getOutputStream(),true);
            out.println(result);
            conn.close();
            rs.close();
            out.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void insertBooking(){
        String sql = "INSERT INTO bookings (\"movieId\",seat,\"dateTime\",email) VALUES (?,?,?,?);";
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost/cinemadb";
            String user = "";
            String pass = "";

            Connection conn = DriverManager.getConnection(url, user, pass);
            PreparedStatement prepStatement = conn.prepareStatement(sql);
            prepStatement.setInt(1, Integer.parseInt(request[1]));
            prepStatement.setString(2,request[2]);
            prepStatement.setTimestamp(3, Timestamp.valueOf(request[3]));
            prepStatement.setString(4,request[4]);
            prepStatement.executeUpdate();
            conn.close();

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getCause());
        }


}
    public void getOccupiedSeats(){
        String sql = "SELECT seat FROM bookings WHERE \"movieId\" = ? INTERSECT SELECT seat FROM bookings WHERE" +
                " \"dateTime\" = (?);";
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost/cinemadb";
            String user = "";
            String pass = "";
            Connection conn = DriverManager.getConnection(url, user, pass);

            PreparedStatement prepStatement = conn.prepareStatement(sql);
            prepStatement.setInt(1, Integer.parseInt(request[2]));
            prepStatement.setTimestamp(2, Timestamp.valueOf(request[1]));
            ResultSet rs = prepStatement.executeQuery();
            String result = "";
            while (rs.next()){
                result += ";"+rs.getString("seat");
            }
            PrintWriter out = new PrintWriter(serviceSocket.getOutputStream(),true);
            out.println(result);
            rs.close();
            conn.close();
            out.close();
        } catch (ClassNotFoundException | SQLException | IOException e) {
            System.out.println(e);
        }

    }
    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(serviceSocket.getInputStream()));
            request = in.readLine().split(";");

            if (request[0].equals("loginPage")){
                user();

            } else if (request[0].equals("movies")) {
                getMovies();
            }else if (request[0].equals("booking")){
                getTimings();
            } else if (request[0].equals("insert booking")) {
                insertBooking();
            } else if (request[0].equals("my booking")) {
                getMyBooking();
            } else if (request[0].equals("upcoming movies")) {
                getUpcomingMovies();
            } else if (request[0].equals("occupied seats")) {
                getOccupiedSeats();
            }
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }catch (NullPointerException e){
            System.out.println("null pointer as no request was received "+e);
        }


    }
}
