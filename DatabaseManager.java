package com.example._207062_gpa_calculator;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:gpa.db";

    public static void createTables() {
        String studentsSql = "CREATE TABLE IF NOT EXISTS students ("
                + "roll TEXT PRIMARY KEY, "
                + "result REAL NOT NULL, "
                + "total_credits INTEGER NOT NULL, "
                + "date TEXT NOT NULL, "
                + "semester TEXT"
                + ")";

        String coursesSql = "CREATE TABLE IF NOT EXISTS courses ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "roll TEXT NOT NULL, "
                + "courseName TEXT NOT NULL, "
                + "courseCode TEXT NOT NULL, "
                + "credit INTEGER NOT NULL, "
                + "teacher TEXT NOT NULL, "
                + "grade TEXT NOT NULL, "
                + "gradePoint REAL NOT NULL, "
                + "date TEXT NOT NULL, "
                + "FOREIGN KEY (roll) REFERENCES students(roll)"
                + ")";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(studentsSql);
            stmt.execute(coursesSql);
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_roll ON courses(roll)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_date ON students(date)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertCourse(Course c, String roll) {
        String sql = "INSERT INTO courses(roll, courseName, courseCode, credit, teacher, grade, gradePoint, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roll);
            ps.setString(2, c.getCourseName());
            ps.setString(3, c.getCourseCode());
            ps.setInt(4, c.getCourseCredit());
            ps.setString(5, c.getTeacher());
            ps.setString(6, c.getGrade());
            ps.setDouble(7, c.getGradePoint());
            ps.setString(8, LocalDate.now().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveStudentResult(String roll, double gpa, int totalCredits, String semester) {
        String sql = "INSERT OR REPLACE INTO students(roll, result, total_credits, date, semester) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roll);
            ps.setDouble(2, gpa);
            ps.setInt(3, totalCredits);
            ps.setString(4, LocalDate.now().toString());
            ps.setString(5, semester);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void clearCoursesByRoll(String roll) {
        String sql = "DELETE FROM courses WHERE roll = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roll);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
