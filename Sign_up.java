package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;

public class Sign_up extends JFrame {
    private Database db = new Database(); // Database 객체 생성

    public Sign_up() {
        JFrame frame = new JFrame("Sign up");

        // 아이디와 비밀번호 입력 필드
        JTextField usernameField = new JTextField(20);
        usernameField.setBackground(new Color(255, 255, 255));
        usernameField.setBounds(124, 105, 294, 57);
        usernameField.setOpaque(false);
        usernameField.setBorder(new EmptyBorder(0, 0, 0, 0));
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setBounds(124, 185, 294, 57);
        passwordField.setOpaque(false);
        passwordField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));

        JTextField emailField = new JTextField(20);
        emailField.setBounds(124, 265, 294, 57);
        emailField.setOpaque(false);
        emailField.setBorder(new EmptyBorder(0, 0, 0, 0));
        emailField.setFont(new Font("Arial", Font.PLAIN, 16));

        // 회원가입 버튼
        JButton registerButton = new JButton("Sign up");
        registerButton.setForeground(new Color(255, 255, 255));
        registerButton.setBackground(new Color(128, 128, 255));
        registerButton.setFont(new Font("Arial", Font.BOLD, 17));
        registerButton.setBounds(124, 355, 294, 50);

        // 회원가입 버튼 클릭 시 동작
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String userId = usernameField.getText();
                String userPassword = new String(passwordField.getPassword());
                String email = emailField.getText();

                // 플레이스홀더 확인
                if (userId.equals("username") || userPassword.equals("password") || email.equals("email")
                        || userId.isEmpty() || userPassword.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "모든 필드를 입력해주세요.");
                    return;
                }

                // DB 연결 및 회원가입 처리
                if (db.isUsernameExists(userId)) {
                    JOptionPane.showMessageDialog(frame, "이미 존재하는 아이디입니다. 다른 아이디를 사용하세요.");
                    return;
                }

                if (db.isEmailExists(email)) {
                    JOptionPane.showMessageDialog(frame, "이미 등록된 이메일입니다. 다른 이메일을 사용하세요.");
                    return;
                }

                if (db.insertUser(userId, userPassword, email)) {
                    JOptionPane.showMessageDialog(frame, "회원가입이 완료되었습니다.");
                    new Login(); // 로그인 화면으로 이동
                    frame.dispose(); // 현재 회원가입 화면 닫기
                } else {
                    JOptionPane.showMessageDialog(frame, "회원가입에 실패했습니다.");
                }
            }
        });

        // 프레임에 컴포넌트 추가
        frame.getContentPane().setLayout(null);

        // 타이틀 레이블
        JLabel registrationTitle = new JLabel("MINIGAME");
        registrationTitle.setForeground(new Color(255, 255, 255));
        registrationTitle.setHorizontalAlignment(SwingConstants.CENTER);
        registrationTitle.setFont(new Font("Arial", Font.BOLD, 24));
        registrationTitle.setBounds(194, 39, 147, 39);
        frame.getContentPane().add(registrationTitle);

        // 레이블 설정
        JLabel usernameLabel = new JLabel("Sign up");
        usernameLabel.setForeground(new Color(128, 128, 255));
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 15));
        usernameLabel.setBounds(124, 105, 93, 15);
        frame.getContentPane().add(usernameLabel);

        // 프레임에 필드 추가
        frame.getContentPane().add(usernameField);
        frame.getContentPane().add(passwordField);
        frame.getContentPane().add(emailField);
        frame.getContentPane().add(registerButton);

        // 플레이스홀더 기능 추가
        setPlaceholder(usernameField, "username");
        setPlaceholder(passwordField, "password");
        setPlaceholder(emailField, "email");

        // 밑줄 추가
        addUnderline1(frame, usernameField);
        addUnderline(frame, passwordField);
        addUnderline2(frame, emailField);

        // 배경 이미지 추가
        JLabel img = new JLabel("");
        img.setIcon(new ImageIcon(".//src//project//space.jpg"));
        img.setBounds(0, 0, 600, 500);
        frame.getContentPane().add(img);

        // 프레임 설정
        frame.setSize(600, 500);
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
    private static void addUnderline1(JFrame frame, JTextField field) {
        JSeparator separator = new JSeparator();
        separator.setBounds(field.getBounds().x, field.getBounds().y + field.getHeight() - 5, field.getWidth(), 2);
        separator.setBackground(Color.WHITE);
        separator.setForeground(Color.WHITE);
        frame.getContentPane().add(separator);
    }

    private static void addUnderline(JFrame frame, JPasswordField field) {
        JSeparator separator = new JSeparator();
        separator.setBounds(field.getBounds().x, field.getBounds().y + field.getHeight() - 5, field.getWidth(), 2);
        separator.setBackground(Color.WHITE);
        separator.setForeground(Color.WHITE);
        frame.getContentPane().add(separator);
    }

    private static void addUnderline2(JFrame frame, JTextField emailField) {
        JSeparator separator = new JSeparator();
        separator.setBounds(emailField.getBounds().x, emailField.getBounds().y + emailField.getHeight() - 5, emailField.getWidth(), 2);
        separator.setBackground(Color.WHITE);
        separator.setForeground(Color.WHITE);
        frame.getContentPane().add(separator);
    }

    public static void main(String[] args) {
        new Sign_up();
    }
}
