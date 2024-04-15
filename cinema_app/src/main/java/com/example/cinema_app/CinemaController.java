package com.example.cinema_app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


import javax.imageio.ImageIO;
import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.awt.image.BufferedImage;


public class CinemaController {
    protected Stage stage = null;
    public String email = "";
    private ResultSet myBookings;

    int[] movieId = new int[3];
    byte[][] images = new byte[3][];
    ResultSet movies;
    ResultSet upcomingMovies;
    String[] movieName = new String[3];
    byte[][] upcomingimages = new byte[3][];
    String[] upcomingmovies = new String[3];;
    int[] upcomingmovieLength = new int[3];
    int[] movieLength = new int[3];
    Socket client = null;
    @FXML
    private Rectangle rectangle1;
    @FXML
    private Rectangle rectangle2;
    @FXML
    private Rectangle rectangle3;
    @FXML
    private Rectangle menu_rectangle;
    @FXML
    private AnchorPane anchor;
    @FXML
    private AnchorPane anchor2;
    @FXML
    private AnchorPane anchor3;
    @FXML
    private Button book;
    @FXML
    private Button myBooking;
    @FXML
    private Button upcoming;
    @FXML
    private Button logOut;
    @FXML
    private Button hide;
    @FXML
    private Label top_label;
    @FXML
    private VBox vBox;
    @FXML
    private Label Movie1;
    @FXML
    private Label Movie1Len;
    @FXML
    private Label Movie1Seat;
    @FXML
    private Label Movie1Time;
    @FXML
    private Button Movie1Button;
    @FXML
    private Label Movie2;
    @FXML
    private Label Movie2Len;
    @FXML
    private Button Movie2Button;
    @FXML
    private Label Movie3;
    @FXML
    private Label Movie3Len;
    @FXML
    private Button Movie3Button;
    @FXML
    private Button next;
    @FXML
    private Button prev;




    public void intializeSocket(){
        try {
            client = new Socket("localhost",5000);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    private void populateUpcomingMovieSet(){
        intializeSocket();
        try {
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            out.println("upcoming movies");
            ObjectInputStream inputStream = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
            upcomingMovies = (ResultSet) inputStream.readObject();

            for (int n = 0;n < 3;n++) {
                if (upcomingMovies.next()) {
                    upcomingimages[n] = upcomingMovies.getBytes("picture");
                    upcomingmovies[n] = upcomingMovies.getString("movieName");
                    upcomingmovieLength[n] = upcomingMovies.getInt("length");
                }
            }

            inputStream.close();
            client.close();
            client = null;
        } catch (IOException | SQLException | ClassNotFoundException e) {
            System.out.println(e);
        }
    }
    private void populateMovieSets(){
        try {

            ObjectInputStream inputStream = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
            movies = (ResultSet) inputStream.readObject();

            for (int n = 0;n < 3;n++) {
                if (movies.next()) {
                    images[n] = movies.getBytes("picture");
                    movieId[n] = movies.getInt("movieId");
                    movieName[n] = movies.getString("movieName");
                    movieLength[n] = movies.getInt("length");
                }
            }
            inputStream.close();
            client.close();
            client = null;
        } catch (IOException | ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
    }
    @FXML
    public void initialize() {
        intializeSocket();
        PrintWriter out = null;
        try {
            out = new PrintWriter(client.getOutputStream(),true);
            out.println("movies");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        populateMovieSets();
        populateUpcomingMovieSet();
        out.close();
        ByteArrayInputStream bis = new ByteArrayInputStream(images[0]);
        Image img = new Image(bis);
        rectangle1.setFill(new ImagePattern(img));
        bis = new ByteArrayInputStream(images[1]);
        Image img2 = new Image(bis);
        rectangle2.setFill(new ImagePattern(img2));
        bis = new ByteArrayInputStream(images[2]);
        Image img3 = new Image(bis);
        rectangle3.setFill(new ImagePattern(img3));
        Movie1.setText(movieName[0]);
        Movie1Len.setText("Length: " +String.valueOf(movieLength[0]) +" mins");
        Movie2.setText(movieName[1]);
        Movie2Len.setText("Length: " +String.valueOf(movieLength[1]) + " mins");
        Movie3.setText(movieName[2]);
        Movie3Len.setText("Length: " +String.valueOf(movieLength[2]) + " mins");


    }
    @FXML
    protected void hide(){
        if(this.hide.getText().equals("Hide Menu")) {
            menu_rectangle.setVisible(false);
            book.setVisible(false);
            myBooking.setVisible(false);
            upcoming.setVisible(false);
            logOut.setVisible(false);
            menu_rectangle.setManaged(false);
            book.setManaged(false);
            myBooking.setManaged(false);
            upcoming.setManaged(false);
            logOut.setManaged(false);
            this.hide.setText("Show");
            this.hide.setPrefHeight(50);
            this.hide.setPrefWidth(50);
            this.hide.setStyle("-fx-font-size: 11;");
            if(top_label.getText().equals("My Booking")){
                rectangle1.setWidth(593);
                AnchorPane.setLeftAnchor(next,327.0);
                next.setPrefWidth(250);
                prev.setPrefWidth(250);
            } else if (top_label.getText().equals("Book Movies" )|| top_label.getText().equals("Upcoming Movies")) {
                rectangle1.setWidth(350);
                AnchorPane.setLeftAnchor(Movie1,370.0);
                AnchorPane.setLeftAnchor(Movie1Len,370.0);
                AnchorPane.setLeftAnchor(Movie1Button,370.0);
                rectangle2.setWidth(350);
                AnchorPane.setLeftAnchor(Movie2,370.0);
                AnchorPane.setLeftAnchor(Movie2Len,370.0);
                AnchorPane.setLeftAnchor(Movie2Button,370.0);
                rectangle3.setWidth(350);
                AnchorPane.setLeftAnchor(Movie3,370.0);
                AnchorPane.setLeftAnchor(Movie3Len,370.0);
                AnchorPane.setLeftAnchor(Movie3Button,370.0);
            }
        } else if (this.hide.getText().equals("Show")){
            menu_rectangle.setVisible(true);
            book.setVisible(true);
            myBooking.setVisible(true);
            upcoming.setVisible(true);
            logOut.setVisible(true);
            menu_rectangle.setManaged(true);
            book.setManaged(true);
            myBooking.setManaged(true);
            upcoming.setManaged(true);
            logOut.setManaged(true);
            this.hide.setText("Hide Menu");
            this.hide.setPrefHeight(80);
            this.hide.setPrefWidth(197);
            this.hide.setStyle("-fx-font-size: 18;");
            if(top_label.getText().equals("My Booking")){
                rectangle1.setWidth(446);
                AnchorPane.setLeftAnchor(next,230.0);
                next.setPrefWidth(200);
                prev.setPrefWidth(200);
            }else if (top_label.getText().equals("Book Movies") ||top_label.getText().equals("Upcoming Movies")) {
                rectangle1.setWidth(200);
                AnchorPane.setLeftAnchor(Movie1,220.0);
                AnchorPane.setLeftAnchor(Movie1Len,220.0);
                AnchorPane.setLeftAnchor(Movie1Button,220.0);
                rectangle2.setWidth(200);
                AnchorPane.setLeftAnchor(Movie2,220.0);
                AnchorPane.setLeftAnchor(Movie2Len,220.0);
                AnchorPane.setLeftAnchor(Movie2Button,220.0);
                rectangle3.setWidth(200);
                AnchorPane.setLeftAnchor(Movie3,220.0);
                AnchorPane.setLeftAnchor(Movie3Len,220.0);
                AnchorPane.setLeftAnchor(Movie3Button,220.0);
            }
        }

    }
    @FXML
    protected void myBooking(){
        intializeSocket();
        try {
            anchor.setVisible(true);
            rectangle1.setVisible(true);
            Movie1.setVisible(true);
            Movie1Len.setVisible(true);
            PrintWriter out = new PrintWriter(client.getOutputStream(),true);
            out.println("my booking;"+email);
            ObjectInputStream inputStream = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
            myBookings = (ResultSet) inputStream.readObject();
            if(myBookings.next()){
                ByteArrayInputStream bis = new ByteArrayInputStream(myBookings.getBytes("picture"));
                Image img = new Image(bis);
                rectangle1.setFill(new ImagePattern(img));
                Movie1Len.setText("Length: "+myBookings.getInt("length")+" min");
                Movie1.setText(myBookings.getString("movieName"));
                Movie1Time.setText("Date and Time: "+myBookings.getString("dateTime"));
                Movie1Seat.setText("Seat "+myBookings.getString("seat"));
            }else {
                anchor.setVisible(false);
                rectangle1.setVisible(false);
                Movie1.setVisible(false);
                Movie1Len.setVisible(false);
            }

            inputStream.close();
            client.close();
            client = null;
        } catch (IOException | SQLException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }

        /*Image image1 = new Image(
                "C:\\Users\\abdal\\OneDrive\\Desktop\\cinema_app\\src\\main\\pirates.jpeg");
        rectangle1.setFill(new ImagePattern(image1));*/
        //Movie1.setText("Pirates Of The Caribbean The Curse Of The Black Pearl");
        /*Movie1Len.setText("Length: 143 min");*/
        anchor2.setVisible(false);
        anchor2.setManaged(false);
        anchor3.setVisible(false);
        anchor3.setManaged(false);
        Movie1Button.setVisible(false);
        Movie1Button.setManaged(false);
        top_label.setText("My Booking");
        anchor.setPrefHeight(500);
        rectangle1.setWidth(446);
        rectangle1.setHeight(250);
        AnchorPane.setTopAnchor(Movie1,270.0);
        AnchorPane.setLeftAnchor(Movie1,10.0);
        Movie1.setMaxWidth(446);
        Movie1.setStyle("-fx-font-size: 18;");
        AnchorPane.setTopAnchor(Movie1Len,300.0);
        AnchorPane.setLeftAnchor(Movie1Len,10.0);
        Movie1Len.setStyle("-fx-font-size: 18;");
        Movie1Seat.setManaged(true);
        AnchorPane.setTopAnchor(Movie1Seat,330.0);
        AnchorPane.setLeftAnchor(Movie1Seat,10.0);
        Movie1Seat.setStyle("-fx-font-size: 18;");
        Movie1Time.setManaged(true);
        AnchorPane.setTopAnchor(Movie1Time,360.0);
        AnchorPane.setLeftAnchor(Movie1Time,10.0);
        Movie1Time.setStyle("-fx-font-size: 18;");
        Movie1Seat.setVisible(true);
        Movie1Time.setVisible(true);

    }
    @FXML
    protected void bookMovies(){
        anchor.setVisible(true);
        rectangle1.setVisible(true);
        Movie1.setVisible(true);
        Movie1Len.setVisible(true);
        rectangle1.setWidth(200);
        rectangle1.setHeight(158);
        ByteArrayInputStream bis = new ByteArrayInputStream(images[0]);
        Image img = new Image(bis);
        rectangle1.setFill(new ImagePattern(img));
        bis = new ByteArrayInputStream(images[1]);
        Image img2 = new Image(bis);
        rectangle2.setFill(new ImagePattern(img2));
        bis = new ByteArrayInputStream(images[2]);
        Image img3 = new Image(bis);
        rectangle3.setFill(new ImagePattern(img3));
        Movie1.setText(movieName[0]);
        Movie1Len.setText("Length: " +String.valueOf(movieLength[0]) +" mins");
        Movie2.setText(movieName[1]);
        Movie2Len.setText("Length: " +String.valueOf(movieLength[1]) + " mins");
        Movie3.setText(movieName[2]);
        Movie3Len.setText("Length: " +String.valueOf(movieLength[2]) + " mins");
        anchor2.setVisible(true);
        anchor2.setManaged(true);
        anchor3.setVisible(true);
        anchor3.setManaged(true);
        Movie1Button.setVisible(true);
        Movie1Button.setManaged(true);
        top_label.setText("Book Movies");
        anchor.setPrefHeight(-1.0);
        AnchorPane.setTopAnchor(Movie1,22.5);
        AnchorPane.setLeftAnchor(Movie1,220.0);
        Movie1.setMaxWidth(220);
        Movie1.setStyle("-fx-font-size: 16;");
        AnchorPane.setTopAnchor(Movie1Len,75.0);
        AnchorPane.setLeftAnchor(Movie1Len,220.0);
        Movie1Len.setStyle("-fx-font-size: 14;");
        Movie1Seat.setManaged(false);
        AnchorPane.setTopAnchor(Movie1Seat,330.0);
        AnchorPane.setLeftAnchor(Movie1Seat,10.0);
        Movie1Seat.setStyle("-fx-font-size: 18;");




        Movie1Time.setManaged(false);
        AnchorPane.setTopAnchor(Movie1Time,390.0);
        AnchorPane.setLeftAnchor(Movie1Time,10.0);
        Movie1Time.setStyle("-fx-font-size: 18;");
        Movie1Seat.setVisible(false);
        Movie1Time.setVisible(false);
        Movie2Button.setManaged(true);
        Movie2Button.setVisible(true);
        Movie3Button.setVisible(true);
        Movie3Button.setManaged(true);
    }
    @FXML
    protected void upcomingMovies(){
        anchor.setVisible(true);
        rectangle1.setVisible(true);
        Movie1.setVisible(true);
        Movie1Len.setVisible(true);
        ByteArrayInputStream bis = new ByteArrayInputStream(upcomingimages[0]);
        Image img = new Image(bis);
        rectangle1.setFill(new ImagePattern(img));
        bis = new ByteArrayInputStream(upcomingimages[1]);
        Image img2 = new Image(bis);
        rectangle2.setFill(new ImagePattern(img2));
        bis = new ByteArrayInputStream(upcomingimages[2]);
        Image img3 = new Image(bis);
        rectangle3.setFill(new ImagePattern(img3));
        Movie1.setText(upcomingmovies[0]);
        Movie1Len.setText("Length: " +String.valueOf(upcomingmovies[0]) +" mins");
        Movie2.setText(upcomingmovies[1]);
        Movie2Len.setText("Length: " +String.valueOf(upcomingmovies[1]) + " mins");
        Movie3.setText(upcomingmovies[2]);
        Movie3Len.setText("Length: " +String.valueOf(upcomingmovies[2]) + " mins");

        top_label.setText("Upcoming Movies");
        anchor2.setVisible(true);
        anchor2.setManaged(true);
        anchor3.setVisible(true);
        anchor3.setManaged(true);
        Movie1Button.setVisible(false);
        Movie1Button.setManaged(false);
        anchor.setPrefHeight(-1.0);
        rectangle1.setWidth(200);
        rectangle1.setHeight(158);
        AnchorPane.setTopAnchor(Movie1,22.5);
        AnchorPane.setLeftAnchor(Movie1,220.0);
        Movie1.setMaxWidth(220);
        Movie1.setStyle("-fx-font-size: 16;");
        AnchorPane.setTopAnchor(Movie1Len,75.0);
        AnchorPane.setLeftAnchor(Movie1Len,220.0);
        Movie1Len.setStyle("-fx-font-size: 14;");
        Movie1Seat.setManaged(false);
        AnchorPane.setTopAnchor(Movie1Seat,330.0);
        AnchorPane.setLeftAnchor(Movie1Seat,10.0);
        Movie1Seat.setStyle("-fx-font-size: 18;");
        Movie1Time.setManaged(false);
        AnchorPane.setTopAnchor(Movie1Time,390.0);
        AnchorPane.setLeftAnchor(Movie1Time,10.0);
        Movie1Time.setStyle("-fx-font-size: 18;");
        Movie1Seat.setVisible(false);
        Movie1Time.setVisible(false);
        Movie2Button.setManaged(false);
        Movie2Button.setVisible(false);
        Movie3Button.setVisible(false);
        Movie3Button.setManaged(false);
    }
    @FXML
    public void pressButton(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("booking.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            Booking bookingController = fxmlLoader.getController();
            bookingController.email = this.email;
            Button clickedButton = (Button) actionEvent.getSource();
            if (clickedButton == Movie1Button) {
                bookingController.MovieName.setText(Movie1.getText());
                bookingController.movieId = this.movieId[0];
                stage.setUserData(this.email+";"+this.movieId[0]);
            } else if ((clickedButton == Movie2Button)) {
                bookingController.MovieName.setText(Movie2.getText());
                bookingController.movieId = this.movieId[1];
                stage.setUserData(this.email+";"+this.movieId[1]);
            }else if ((clickedButton == Movie3Button)) {
                bookingController.MovieName.setText(Movie3.getText());
                bookingController.movieId = this.movieId[2];
                stage.setUserData(this.email+";"+this.movieId[2]);
            }
            bookingController.stage = stage;
            bookingController.populateChoiceBox();
            stage.show();

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public void previous(ActionEvent actionEvent) {
        try {
            if(top_label.getText().equals("My Booking")) {
                if (myBookings.previous()) {
                    ByteArrayInputStream bis = new ByteArrayInputStream(myBookings.getBytes("picture"));
                    Image img = new Image(bis);
                    rectangle1.setFill(new ImagePattern(img));
                    Movie1Len.setText("Length: " + myBookings.getInt("length") + " min");
                    Movie1.setText(myBookings.getString("movieName"));
                    Movie1Time.setText("Date and Time: " + myBookings.getString("dateTime"));
                    Movie1Seat.setText("Seat " + myBookings.getString("seat"));
                }
            } else if (top_label.getText().equals("Book Movies")) {
                for (int n = 0;n < 3;n++) {
                    if (movies.previous()) {
                        images[n] = movies.getBytes("picture");
                        movieId[n] = movies.getInt("movieId");
                        movieName[n] = movies.getString("movieName");
                        movieLength[n] = movies.getInt("length");
                    }
                }
                ByteArrayInputStream bis = new ByteArrayInputStream(images[0]);
                Image img = new Image(bis);
                rectangle1.setFill(new ImagePattern(img));
                bis = new ByteArrayInputStream(images[1]);
                Image img2 = new Image(bis);
                rectangle2.setFill(new ImagePattern(img2));
                bis = new ByteArrayInputStream(images[2]);
                Image img3 = new Image(bis);
                rectangle3.setFill(new ImagePattern(img3));
                Movie1.setText(movieName[0]);
                Movie1Len.setText("Length: " +String.valueOf(movieLength[0]) +" mins");
                Movie2.setText(movieName[1]);
                Movie2Len.setText("Length: " +String.valueOf(movieLength[1]) + " mins");
                Movie3.setText(movieName[2]);
                Movie3Len.setText("Length: " +String.valueOf(movieLength[2]) + " mins");
            } else if (top_label.getText().equals("Upcoming Movies")) {
                for (int n = 0;n < 3;n++) {
                    if (upcomingMovies.previous()) {
                        upcomingimages[n] = upcomingMovies.getBytes("picture");
                        upcomingmovies[n] = upcomingMovies.getString("movieName");
                        upcomingmovieLength[n] = upcomingMovies.getInt("length");
                    }
                }
                ByteArrayInputStream bis = new ByteArrayInputStream(upcomingimages[0]);
                Image img = new Image(bis);
                rectangle1.setFill(new ImagePattern(img));
                bis = new ByteArrayInputStream(upcomingimages[1]);
                Image img2 = new Image(bis);
                rectangle2.setFill(new ImagePattern(img2));
                bis = new ByteArrayInputStream(upcomingimages[2]);
                Image img3 = new Image(bis);
                rectangle3.setFill(new ImagePattern(img3));
                Movie1.setText(upcomingmovies[0]);
                Movie1Len.setText("Length: " +String.valueOf(upcomingmovies[0]) +" mins");
                Movie2.setText(upcomingmovies[1]);
                Movie2Len.setText("Length: " +String.valueOf(upcomingmovies[1]) + " mins");
                Movie3.setText(upcomingmovies[2]);
                Movie3Len.setText("Length: " +String.valueOf(upcomingmovies[2]) + " mins");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void next(ActionEvent actionEvent) {
        try {
            if(top_label.getText().equals("My Booking")) {
                if (myBookings.next()) {
                    ByteArrayInputStream bis = new ByteArrayInputStream(myBookings.getBytes("picture"));
                    Image img = new Image(bis);
                    rectangle1.setFill(new ImagePattern(img));
                    Movie1Len.setText("Length: " + myBookings.getInt("length") + " min");
                    Movie1.setText(myBookings.getString("movieName"));
                    Movie1Time.setText("Date and Time: " + myBookings.getString("dateTime"));
                    Movie1Seat.setText("Seat " + myBookings.getString("seat"));
                }
            }else if (top_label.getText().equals("Book Movies")) {
                for (int n = 0;n < 3;n++) {
                    if (movies.next()) {
                        images[n] = movies.getBytes("picture");
                        movieId[n] = movies.getInt("movieId");
                        movieName[n] = movies.getString("movieName");
                        movieLength[n] = movies.getInt("length");
                    }
                }
                ByteArrayInputStream bis = new ByteArrayInputStream(images[0]);
                Image img = new Image(bis);
                rectangle1.setFill(new ImagePattern(img));
                bis = new ByteArrayInputStream(images[1]);
                Image img2 = new Image(bis);
                rectangle2.setFill(new ImagePattern(img2));
                bis = new ByteArrayInputStream(images[2]);
                Image img3 = new Image(bis);
                rectangle3.setFill(new ImagePattern(img3));
                Movie1.setText(movieName[0]);
                Movie1Len.setText("Length: " +String.valueOf(movieLength[0]) +" mins");
                Movie2.setText(movieName[1]);
                Movie2Len.setText("Length: " +String.valueOf(movieLength[1]) + " mins");
                Movie3.setText(movieName[2]);
                Movie3Len.setText("Length: " +String.valueOf(movieLength[2]) + " mins");
            } else if (top_label.getText().equals("Upcoming Movies")) {
                for (int n = 0;n < 3;n++) {
                    if (upcomingMovies.next()) {
                        upcomingimages[n] = upcomingMovies.getBytes("picture");
                        upcomingmovies[n] = upcomingMovies.getString("movieName");
                        upcomingmovieLength[n] = upcomingMovies.getInt("length");
                    }
                }
                ByteArrayInputStream bis = new ByteArrayInputStream(upcomingimages[0]);
                Image img = new Image(bis);
                rectangle1.setFill(new ImagePattern(img));
                bis = new ByteArrayInputStream(upcomingimages[1]);
                Image img2 = new Image(bis);
                rectangle2.setFill(new ImagePattern(img2));
                bis = new ByteArrayInputStream(upcomingimages[2]);
                Image img3 = new Image(bis);
                rectangle3.setFill(new ImagePattern(img3));
                Movie1.setText(upcomingmovies[0]);
                Movie1Len.setText("Length: " +String.valueOf(upcomingmovies[0]) +" mins");
                Movie2.setText(upcomingmovies[1]);
                Movie2Len.setText("Length: " +String.valueOf(upcomingmovies[1]) + " mins");
                Movie3.setText(upcomingmovies[2]);
                Movie3Len.setText("Length: " +String.valueOf(upcomingmovies[2]) + " mins");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void logout(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(CinemaApplication.class.getResource("signIn.fxml"));
        Scene scene = null;
        myBookings = null;
        try {
            scene = new Scene(fxmlLoader.load(), 250, 200);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Real Cinema");
            stage.setScene(scene);
            stage.show();
            this.stage.close();
        } catch (IOException e) {
            System.out.println(e);
        }

    }
}
