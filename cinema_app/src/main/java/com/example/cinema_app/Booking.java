package com.example.cinema_app;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Booking {
    @FXML
    private ChoiceBox time;
    @FXML//
    private Node rootNode;
    private Socket client;
    @FXML
     protected Rectangle Screen;
    @FXML
    protected Label MovieName;
    protected String email = "";
    protected int movieId;
    protected String selectedSeat = "";
    protected Stage stage;
    String[] occupiedSeats;

    private void initializeSocket(){
        try {
            client = new Socket("localhost",5000);
        } catch (IOException e) {
            System.out.println(e.getCause());
        }
    }
    protected void populateChoiceBox(){
        PrintWriter out = null;
        try {
            out = new PrintWriter(client.getOutputStream(),true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        out.println("booking;"+movieId);
        ArrayList<String> timings = new ArrayList<>();
        try {
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String result = inputStream.readLine();
            String[] resultArray = result.split(";");
            for (int i =0;i<resultArray.length;i++){
                timings.add(resultArray[i]);
            }
            ObservableList list = FXCollections.observableList(timings);
            time.setItems(list);
            out.close();
            client.close();
            client = null;
            time.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observableValue, Object o, Object t1) {
                    if(occupiedSeats != null){
                        for (int i = 1;i<occupiedSeats.length;i++){
                            Node seat = stage.getScene().getRoot().lookup("#"+occupiedSeats[i]);
                            seat.setDisable(false);
                        }
                    }
                    initializeSocket();
                    try {
                        PrintWriter out = new PrintWriter(client.getOutputStream(),true);
                        out.println("occupied seats;"+t1+";"+movieId);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        String result = reader.readLine();
                        occupiedSeats = result.split(";");
                        for (int i = 1;i<occupiedSeats.length;i++){
                            Node seat = stage.getScene().getRoot().lookup("#"+occupiedSeats[i]);
                            seat.setDisable(true);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (IOException e) {
            e.getCause();
        }
    }

    @FXML
    public void SeatPicked(ActionEvent event) {
        Node source = (Node) event.getSource();

        if (selectedSeat.equals("")){
                selectedSeat =source.getId();
                source.setStyle("-fx-background-color: #ff0000;");
        } else if (selectedSeat.equals(source.getId())) {
            selectedSeat = "";
            source.setStyle("-fx-background-color: ;");
        }
    }


    @FXML
    public void initialize(){
        initializeSocket();

    }

    public void submitBookingData(ActionEvent actionEvent) {
    client = null;
        initializeSocket();
        try {
            PrintWriter out = new PrintWriter(client.getOutputStream(),true);

            out.println("insert booking;"+movieId+";"+selectedSeat+";"+time.getSelectionModel().getSelectedItem()
            +";"+email);
            out.close();
            client.close();
            stage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
