package project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Form extends JFrame {	
	static String userId; 
	private static JLabel btnLogIn; 
	public Form() {
		
		this.setTitle("Game");
		this.setVisible(true);
		this.setSize(600, 500);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel startPage = new JPanel();
		startPage.setLocation(0, 0);
		startPage.setSize(600, 500);
		this.getContentPane().add(startPage, BorderLayout.CENTER);
		startPage.setLayout(null);

		JLabel title = new JLabel("MINIGAME");
		title.setForeground(new Color(255, 255, 255));
		title.setFont(new Font("Arial", Font.BOLD, 54));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBounds(26, 78, 535, 107);
		startPage.add(title);

		JButton shootbtn = new JButton("Shooting Game");
		shootbtn.setForeground(new Color(255, 255, 255));
		shootbtn.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
		shootbtn.setBackground(new Color(128, 128, 255));
		shootbtn.setBounds(157, 287, 258, 51);
		startPage.add(shootbtn);

		JButton snakebtn = new JButton("Snake Game");
		snakebtn.setForeground(new Color(255, 255, 255));
		snakebtn.setBackground(new Color(128, 128, 255));
		snakebtn.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
		snakebtn.setBounds(157, 226, 258, 51);
		startPage.add(snakebtn);
		
		btnLogIn = new JLabel("<html><a href='' style='text-decoration:none; color:white; font-weight : bold;'>Login</a></html>");
		btnLogIn.setHorizontalAlignment(SwingConstants.CENTER);
		btnLogIn.setFont(new Font("Arial", Font.PLAIN, 25));
		btnLogIn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		startPage.add(btnLogIn);
		btnLogIn.setBounds(445, 403, 133, 39);

		JLabel img = new JLabel("");
		img.setIcon(new ImageIcon(".//src//project//space.jpg"));
		startPage.add(img);
		img.setBounds(0, 0, 920, 532);


		btnLogIn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (userId == null) {
					new Login(); // 스레드 시작
					dispose();
				}
				else {
					logout();
				}
			}// 이 창을 안보이게
		});


		snakebtn.addActionListener(e -> {
			new SnakeGame("Snake Game",userId).setVisible(true); // SnakeGame 불러오기
			this.setVisible(false); // 이 창을 안보이게

		});
		shootbtn.addActionListener(e -> {
			Thread shootingThread = new Thread(new Shooting(userId)); 
			System.out.println(userId);// 새로운 스레드를 만들어서 실행
			shootingThread.start(); 
			this.setVisible(false);
		});

	}

	public static void main(String[] args) {
		new Form();
	}
	static void setUserId(String userId_) {
		userId = userId_;
	}

	// 사용자 ID를 사용하는 메서드
	public String getUserId() {
		return userId;
	}
	static void updateLoginLabel() {
		if (userId != null) {
			btnLogIn.setText("<html><a href='' style='text-decoration:none; color:white; font-weight : bold;'>Logout</a></html>");
		} else {
			btnLogIn.setText("<html><a href='' style='text-decoration:none; color:white; font-weight : bold;'>Login</a></html>");
		}
	}
	private void logout() {
		userId = null;
		updateLoginLabel(); // "Logout" -> "Login"으로 변경
		JOptionPane.showMessageDialog(this, "로그아웃 되었습니다.");
	}

}
