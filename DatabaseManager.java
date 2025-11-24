package com.example._207062_gpa_calculator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:gpa.db";

    public static void createTables() {
        String sql = "CREATE TABLE IF NOT EXISTS courses ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "courseName TEXT NOT NULL, "
                + "courseCode TEXT, "
                + "credit INTEGER NOT NULL, "
                + "teacher TEXT, "
                + "grade TEXT NOT NULL, "
                + "gradePoint REAL NOT NULL"
                + ");";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertCourse(Course c) {
        String sql = "INSERT INTO courses(courseName, courseCode, credit, teacher, grade, gradePoint) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getCourseName());
            ps.setString(2, c.getCourseCode());
            ps.setInt(3, c.getCourseCredit());
            ps.setString(4, c.getTeacher());
            ps.setString(5, c.getGrade());
            ps.setDouble(6, c.getGradePoint());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Course> loadAllCourses() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT courseName, courseCode, credit, teacher, grade FROM courses";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Course c = new Course(
                        rs.getString("courseName"),
                        rs.getString("courseCode"),
                        rs.getInt("credit"),
                        rs.getString("teacher"),
                        rs.getString("grade")
                );
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void clearAllCourses() {
        String sql = "DELETE FROM courses";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
