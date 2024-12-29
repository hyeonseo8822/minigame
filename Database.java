package project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Database {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/login_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    // DB 연결 생성 메서드
    private Connection getConnection() throws Exception {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // 유저 아이디 중복 체크
    public boolean isUsernameExists(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // 존재하면 true 반환
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // 기본값
    }

    // 이메일 중복 체크
    public boolean isEmailExists(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // 존재하면 true 반환
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false; // 기본값
    }

    // 회원가입 처리
    public boolean insertUser(String username, String password, String email) {
        String query = "INSERT INTO users (username, password, email, shoot, snake) VALUES (?, ?, ?, 0, 0)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);
            return stmt.executeUpdate() > 0; // 성공하면 true 반환
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // 실패 시
    }

    // 사용자 유효성 검사 (로그인 시 사용)
    public boolean isUserValid(String username, String password) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // 유효한 사용자 있으면 true 반환
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // 유효하지 않은 사용자
    }

    // shoot 점수 가져오기 (로그인한 사용자)
    public int getShootScore(String username) {
        String query = "SELECT shoot FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("shoot"); // shoot 값 반환
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0; // 기본값 0
    }

    // shoot 점수 업데이트
    public boolean updateShootScore(String username, int newScore) {
        String query = "UPDATE users SET shoot = ? WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, newScore); // 새로운 점수로 업데이트
            stmt.setString(2, username); // 사용자명 설정
            return stmt.executeUpdate() > 0; // 성공적으로 업데이트 되면 true 반환
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // 실패 시
    }

    // snake 점수 가져오기
    public int getSnakeScore(String username) {
        String query = "SELECT snake FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("snake"); // snake 값 반환
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0; // 기본값 0
    }
    public boolean updateSnakeScore(String username, int newScore) {
        String query = "UPDATE users SET snake = ? WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, newScore); // 새로운 점수로 업데이트
            stmt.setString(2, username); // 사용자명 설정
            return stmt.executeUpdate() > 0; // 성공적으로 업데이트 되면 true 반환
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // 실패 시
    }

}
