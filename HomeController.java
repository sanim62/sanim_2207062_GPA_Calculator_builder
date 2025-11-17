package com.example._207062_gpa_calculator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HomeController {

    @FXML
    private Button startButton;

    @FXML
    private void handleStart() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("courseEntry.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 800, 600);

            Stage stage = (Stage) startButton.getScene().getWindow();
            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
