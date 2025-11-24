package com.example._207062_gpa_calculator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class GPAResultController {

    @FXML private Label gpaLabel;
    @FXML private Label creditLabel;
    @FXML private VBox courseList;
    @FXML private Button backButton;

    private List<Course> courses;
    private int totalCredits;

    public void setCourses(List<Course> courses, int totalCredits) {
        this.courses = courses;
        this.totalCredits = totalCredits;
        displayResults();
    }

    private void displayResults() {
        double gpa = calculateGPA();
        gpaLabel.setText(String.format("%.2f", gpa));
        creditLabel.setText("Total Credits: " + totalCredits);

        for (Course course : courses) {
            HBox row = new HBox(15);
            row.setPadding(new Insets(8));
            row.setStyle("-fx-background-color: #f9f9f9; -fx-background-radius: 3;");

            Label nameLabel = new Label(course.getCourseName());
            nameLabel.setPrefWidth(150);

            Label codeLabel = new Label(course.getCourseCode());
            codeLabel.setPrefWidth(80);

            Label creditLabel = new Label(course.getCourseCredit() + " cr");
            creditLabel.setPrefWidth(50);

            Label gradeLabel = new Label(course.getGrade());
            gradeLabel.setPrefWidth(50);

            Label pointsLabel = new Label(String.format("%.2f", course.getGradePoint()));
            pointsLabel.setPrefWidth(50);

            row.getChildren().addAll(nameLabel, codeLabel, creditLabel, gradeLabel, pointsLabel);
            courseList.getChildren().add(row);
        }
    }

    private double calculateGPA() {
        double totalPoints = 0;
        int totalCreditCount = 0;

        for (Course course : courses) {
            totalPoints += course.getGradePoint() * course.getCourseCredit();
            totalCreditCount += course.getCourseCredit();
        }

        return totalCreditCount > 0 ? totalPoints / totalCreditCount : 0.0;
    }

    @FXML
    private void handleBack() {
        try {
            // Clear the database for new calculation
            DatabaseManager.clearAllCourses();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 800, 500);

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}