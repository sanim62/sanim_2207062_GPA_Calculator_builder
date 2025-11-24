
package com.example._207062_gpa_calculator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CourseEntryController implements Initializable {

    @FXML private TextField courseNameField;
    @FXML private TextField courseCodeField;
    @FXML private TextField creditField;
    @FXML private TextField teacherField;
    @FXML private ComboBox<String> gradeCombo;
    @FXML private Button addButton;
    @FXML private Button calculateButton;
    @FXML private Label creditLabel;
    @FXML private VBox coursesDisplay;

    private List<Course> courses = new ArrayList<>();

    private int totalCreditsRequired = 12;
    private int currentCredits = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gradeCombo.getItems().addAll("A+", "A", "A-", "B+", "B", "B-",
                "C+", "C", "C-", "D+", "D", "F");
    }

    @FXML
    private void handleAddCourse() {
        String name = courseNameField.getText().trim();
        String code = courseCodeField.getText().trim();
        String creditStr = creditField.getText().trim();
        String teacher = teacherField.getText().trim();
        String grade = gradeCombo.getValue();

        // Show error message for incomplete inputs
        if (name.isEmpty() || code.isEmpty() || creditStr.isEmpty() ||
                teacher.isEmpty() || grade == null) {
            showAlert("Incomplete Information", "Please fill in all fields.");
            return;
        }

        try {
            int credit = Integer.parseInt(creditStr);

            if (credit <= 0) {
                showAlert("Invalid Credit", "Credit hours must be greater than 0.");
                return;
            }

            if (currentCredits + credit > totalCreditsRequired) {
                showAlert("Credit Limit Exceeded",
                        "Adding this course would exceed the " + totalCreditsRequired + " credit limit.\n" +
                                "Current credits: " + currentCredits + "\n" +
                                "Attempting to add: " + credit);
                return;
            }

            Course course = new Course(name, code, credit, teacher, grade);
            courses.add(course);
            DatabaseManager.insertCourse(course); // Fixed method name

            currentCredits += credit;

            addCourseToDisplay(course);
            creditLabel.setText("Credits: " + currentCredits + " / " + totalCreditsRequired);

            // Clear input fields
            courseNameField.clear();
            courseCodeField.clear();
            creditField.clear();
            teacherField.clear();
            gradeCombo.setValue(null);

            if (currentCredits >= totalCreditsRequired) {
                calculateButton.setDisable(false);
            }

        } catch (NumberFormatException ex) {
            showAlert("Invalid Input", "Credit hours must be a valid number.");
        }
    }

    private void addCourseToDisplay(Course course) {
        HBox courseBox = new HBox(10);
        courseBox.setPadding(new Insets(8));
        courseBox.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 5;");

        Label info = new Label(course.getCourseCode() + " - " + course.getCourseName() +
                " | " + course.getCourseCredit() + " credits | Grade: " + course.getGrade());

        courseBox.getChildren().add(info);
        coursesDisplay.getChildren().add(courseBox);
    }

    @FXML
    private void handleCalculateGPA() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gpaResult.fxml"));
            Parent root = loader.load();

            GPAResultController controller = loader.getController();
            controller.setCourses(courses, currentCredits);

            Scene scene = new Scene(root, 800, 600);

            Stage stage = (Stage) calculateButton.getScene().getWindow();
            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not load GPA results screen.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}