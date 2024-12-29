package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.EmptyBorder;

public class Login extends JFrame {
    private Database database = new Database();  // Database 객체 생성

    public Login() {
        JFrame frame = new JFrame("로그인");

        // 아이디와 비밀번호 입력 필드
        JTextField usernameField = new JTextField(20);
        usernameField.setBackground(new Color(255, 255, 255));
        usernameField.setBounds(124, 105, 294, 57);
        usernameField.setOpaque(false);  // 투명
        usernameField.setBorder(new EmptyBorder(0, 0, 0, 0));  // 기본 테두리 제거
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));  // 폰트 설정

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setBounds(124, 185, 294, 57);
        passwordField.setOpaque(false);  // 투명
        passwordField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));  // 기본 테두리 제거
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));  // 폰트 설정

        // 로그인 버튼
        JButton loginButton = new JButton("login");
        loginButton.setForeground(new Color(255, 255, 255));
        loginButton.setBackground(new Color(128, 128, 255));
        loginButton.setFont(new Font("Arial", Font.BOLD, 17));
        loginButton.setBounds(124, 269, 294, 50);

        // 로그인 버튼 클릭 시 동작
        loginButton.addActionListener(e -> {
            String userId = usernameField.getText();
            String userPassword = new String(passwordField.getPassword());

            // 입력값 검증
            if (userId.isEmpty() || userPassword.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "아이디와 비밀번호를 모두 입력해주세요.");
                return;
            }

            // 데이터베이스 연결 및 로그인 `검증
            if (database.isUserValid(userId, userPassword)) {
                // 로그인 성공 시
                JOptionPane.showMessageDialog(frame, "로그인 성공!");

                // 로그인한 userId를 Form 객체에 전달
                Form form = new Form();
                form.setUserId(userId); // userId를 Form에 전달
                Form.updateLoginLabel();
                frame.dispose(); // 로그인 창 숨기기
            } else {
                // 로그인 실패 시
                JOptionPane.showMessageDialog(frame, "아이디 또는 비밀번호가 잘못되었습니다.");
            }
        });

        // 프레임에 컴포넌트 추가
        frame.getContentPane().setLayout(null);

        // JLabel 생성 (HTML로 링크처럼 보이도록 설정, text-decoration: none 적용)
        JLabel linkLabel = new JLabel("<html><a href='' style='text-decoration:none; color:white; font-weight : bold;'>sign up</a></html>");
        linkLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        linkLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // 커서가 손 모양으로 바뀌게 설정

        // 링크 클릭 이벤트 처리
        linkLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 클릭 시 동작
                new Sign_up();
                frame.dispose();  // 현재 로그인 화면을 닫기
            }
        });

        // 프레임에 추가
        frame.getContentPane().add(linkLabel);
        linkLabel.setBounds(124, 330, 294, 30);  // 위치 설정

        JLabel minigame = new JLabel("MINIGAME");
        minigame.setForeground(new Color(255, 255, 255));
        minigame.setHorizontalAlignment(SwingConstants.CENTER);
        minigame.setFont(new Font("Arial", Font.BOLD, 24));
        minigame.setBounds(194, 39, 147, 39);
        frame.getContentPane().add(minigame);

        JLabel username = new JLabel("LOGIN");
        username.setForeground(new Color(128, 128, 255));
        username.setFont(new Font("Arial", Font.BOLD, 13));
        username.setBounds(124, 105, 93, 15);
        frame.getContentPane().add(username);
        frame.getContentPane().add(usernameField);
        frame.getContentPane().add(passwordField);
        frame.getContentPane().add(loginButton);

        // 플레이스홀더 기능 추가
        setPlaceholder(usernameField, "username");
        setPlaceholder(passwordField, "password");

        // 밑줄 추가
        addUnderline(frame, usernameField);
        addUnderline(frame, passwordField);

        // 배경 이미지 추가
        JLabel img = new JLabel("");
        img.setIcon(new ImageIcon(".//src//project//space.jpg"));
        img.setBounds(0, 0, 600, 400);
        frame.getContentPane().add(img);

        // 프레임 설정
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // 플레이스홀더 설정 메소드
    private static void setPlaceholder(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(new Color(128, 128, 128));

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.white);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.gray);
                }
            }
        });
    }

    private static void setPlaceholder(JPasswordField field, String placeholder) {
        field.setEchoChar((char) 0);  // 비밀번호는 보이게 설정
        field.setText(placeholder);
        field.setForeground(Color.gray);

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (new String(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.white);
                    field.setEchoChar('*');  // 비밀번호 숨기기
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (new String(field.getPassword()).isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.gray);
                    field.setEchoChar((char) 0);  // 비밀번호는 다시 보이게 설정
                }
            }
        });
    }

    // Add white underline below text field
    private static void addUnderline(JFrame frame, JTextField field) {
        JSeparator separator = new JSeparator();
        separator.setBounds(field.getBounds().x, field.getBounds().y + field.getHeight() - 5, field.getWidth(), 2);
        separator.setBackground(Color.WHITE);  // 흰색 테두리 설정
        separator.setForeground(Color.WHITE);
        frame.getContentPane().add(separator);
    }

    private static void addUnderline(JFrame frame, JPasswordField field) {
        JSeparator separator = new JSeparator();
        separator.setBounds(field.getBounds().x, field.getBounds().y + field.getHeight() - 5, field.getWidth(), 2);
        separator.setBackground(Color.WHITE);  // 흰색 테두리 설정
        separator.setForeground(Color.WHITE);
        frame.getContentPane().add(separator);
    }

    public static void main(String[] args) {
        new Login();
    }
}
