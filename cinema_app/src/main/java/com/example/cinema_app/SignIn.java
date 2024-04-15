package com.example.cinema_app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Pattern;

import javafx.scene.Node;
public class SignIn {
    @FXML//
    private Node rootNode;
    @FXML
    TextField email;
    @FXML
    PasswordField pass;
    @FXML
    ToggleButton login;
    @FXML
    ToggleButton signup;
    @FXML
    protected Button submit;
    @FXML
    ToggleGroup group;
    Socket client = null;
    public void intializeSocket(){
        try {
            client = new Socket("localhost",5000);
        } catch (IOException e) {
            System.out.println(e.getCause());
        }
    }
    public void Submit(ActionEvent actionEvent) {
        intializeSocket();
        FXMLLoader fxmlLoader = null;
        PrintWriter out = null;
        Toggle selectedToggle = group.getSelectedToggle();
        String regex = "^([a-zA-Z0-9_\\.]+)@([a-zA-Z0-9_\\.]+)\\.([a-zA-Z]{2,10})$";
        if (selectedToggle == signup){
            try {
                if (Pattern.matches(regex,email.getText())){
                out = new PrintWriter(client.getOutputStream(),true);
                String request = "signup;" + email.getText() +";" +pass.getText();
                out.println("loginPage;"+request);

                /*String request = "signup;" + email.getText() +";" +pass.getText();
                out.println(request);*/
                out.close();
                fxmlLoader = new FXMLLoader(getClass().getResource("Cinema.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 690, 700);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                CinemaController controller = fxmlLoader.getController();
                controller.email = email.getText();
                controller.stage = stage;
                stage.show();
                Stage currentstage = (Stage) rootNode.getScene().getWindow();
                currentstage.close();
                out.close();
                client.close();
                }else{
                    email.setText("Incorrect patten");
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (selectedToggle == login) {
            try {
                if (Pattern.matches(regex,email.getText())) {
                    out = new PrintWriter(client.getOutputStream(), true);
                    String request = "login;" + email.getText() + ";" + pass.getText();
                    out.println("loginPage;" + request);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    String status = reader.readLine();
                    if (status.equals("true")) {
                        fxmlLoader = new FXMLLoader(getClass().getResource("Cinema.fxml"));
                        Scene scene = new Scene(fxmlLoader.load(), 690, 700);
                        Stage stage = new Stage();
                        stage.setResizable(false);
                        CinemaController controller = fxmlLoader.getController();
                        controller.email = email.getText();
                        controller.stage = stage;
                        stage.setScene(scene);
                        stage.show();
                        Stage currentstage = (Stage) rootNode.getScene().getWindow();
                        currentstage.close();
                    } else {
                        email.setStyle("-fx-text-fill: red;");
                    }
                    reader.close();
                    out.close();
                    client.close();
                }else {
                    email.setText("Incorrect patten");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }




    }

}
