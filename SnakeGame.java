
package project;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SnakeGame extends JFrame {
	class XY {
		int x;
		int y;

		public XY(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	JPanel panelNorth;
	JPanel panelCenter;
	JLabel labelTitle;
	JLabel labelMessage;
	JLabel[][] panels = new JLabel[20][20];
	int[][] map = new int[20][20]; // 과일 위치 9
	LinkedList<XY> snake = new LinkedList<XY>();
	int dir = 3; // 움직이는 방향 0 : 위 1 : down 2 : left 3:right
	int score = 0;
	int time = 0; // 게임 시간 1초 단위로 움직임
	int timeTickCount = 0;
	Timer timer = null;
	final int fruit = 9;
	

	private ImageIcon headIcon_f, bodyIcon_f, tailIcon_f, headIcon_b, bodyIcon_b, tailIcon_b, headIcon_r, bodyIcon_r,
			tailIcon_r, headIcon_l, bodyIcon_l, tailIcon_l, apple;// background;
	
    private Database database;
    String userId;
    private int highScore;

	public SnakeGame(String title, String userId) {
		super(title);
        this.database = new Database();
        this.userId = userId;
        this.highScore = database.getSnakeScore(userId); 
		this.setSize(412, 535);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setFocusable(true); // 창이 키 입력을 받을 수 있도록 설정
		this.requestFocusInWindow(); // 창이 기본적으로 포커스를 받도록 설정

		getContentPane().setLayout(null); // 레이아웃 관리자를 null로 설정

		// 이미지 아이콘 설정 및 UI 초기화 함수 호출
		initUI();
		makeSnakeList(); // 스네이크 몸 만듬
		startTimer(); // 타이머 시작
		setKeyListener(); // 키보드 리스너 설정
		makeFruit(); // 과일 만들기

		int size = 20;
		headIcon_f = resizeIcon(new ImageIcon(".//src//project//head_f.png"), size, size);
		bodyIcon_f = resizeIcon(new ImageIcon(".//src//project//body_f.png"), size, size);
		tailIcon_f = resizeIcon(new ImageIcon(".//src//project//tail_f.png"), size, size);

		headIcon_b = resizeIcon(new ImageIcon(".//src//project//head_b.png"), size, size);
		bodyIcon_b = resizeIcon(new ImageIcon(".//src//project//body_b.png"), size, size);
		tailIcon_b = resizeIcon(new ImageIcon(".//src//project//tail_b.png"), size, size);

		headIcon_r = resizeIcon(new ImageIcon(".//src//project//head_r.png"), size, size);
		bodyIcon_r = resizeIcon(new ImageIcon(".//src//project//body_r.png"), size, size);
		tailIcon_r = resizeIcon(new ImageIcon(".//src//project//tail_r.png"), size, size);

		headIcon_l = resizeIcon(new ImageIcon(".//src//project//head_l.png"), size, size);
		bodyIcon_l = resizeIcon(new ImageIcon(".//src//project//body_l.png"), size, size);
		tailIcon_l = resizeIcon(new ImageIcon(".//src//project//tail_l.png"), size, size);
		apple = resizeIcon(new ImageIcon(".//src//project//apple.png"), size, size);
		// background= resizeIcon(new ImageIcon(".//src//project//background.jpg"),
		// size, size);
	}

	private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
		Image img = icon.getImage();
		Image resizedImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new ImageIcon(resizedImage);
	}

	public void makeFruit() {
		Random rand = new Random();
		int randX = rand.nextInt(19);
		int randY = rand.nextInt(19);
		map[randX][randY] = fruit;
	}

	public void setKeyListener() {
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					if (dir != 1) {
						dir = 0;
					}
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					if (dir != 0) {
						dir = 1;
					}
				} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					if (dir != 3) {
						dir = 2;
					}
				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					if (dir != 2) {
						dir = 3;
					}
				}
			}
		});
	}

	public void startTimer() {
		timer = new Timer(50, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				timeTickCount += 1;

				if (timeTickCount % 20 == 0) {
					time++; // 1초 증가
				}
				if (timeTickCount % 3 == 0) {
					moveSnake();
					updateUI();
				}
			}
		});
		timer.start();
	}

	public void moveSnake() {
		XY headXY = snake.get(0); // 머리 가져옴
		int headX = headXY.x;
		int headY = headXY.y;

		if (dir == 0) {
			boolean isColl = checkCollision(headX, headY - 1);
			if (isColl == true) {
				if (score > highScore) {
		            database.updateSnakeScore(userId, score);
		        } 
				Form.setUserId(userId);
				JButton gameOver = new JButton("Game over!");
				gameOver.setBackground(new Color(128, 0, 0));
				gameOver.setBounds(132, 60, 144, 30);
				gameOver.setPreferredSize(new Dimension(80, 30));
				panelNorth.add(gameOver);
				timer.stop();
				gameOver.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						new Form().setVisible(true); // SnakeGame 불러오기
		                Form.updateLoginLabel();
						setVisible(false);
					}
				});
				return;
			}
			snake.add(0, new XY(headX, headY - 1));
			snake.remove(snake.size() - 1);
		} else if (dir == 1) {
			if (score > highScore) {
	            database.updateSnakeScore(userId, score);
	        } 
			Form.setUserId(userId);
			boolean isColl = checkCollision(headX, headY + 1);
			if (isColl == true) {
				JButton gameOver = new JButton("Game over!");
				gameOver.setBackground(new Color(128, 0, 0));
				gameOver.setBounds(132, 60, 144, 30);
				gameOver.setPreferredSize(new Dimension(80, 30));
				panelNorth.add(gameOver);
				timer.stop();
				gameOver.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						new Form().setVisible(true); // SnakeGame 불러오기
		                Form.updateLoginLabel();
						setVisible(false);
					}
				});
				return;
			}
			snake.add(0, new XY(headX, headY + 1));
			snake.remove(snake.size() - 1);
		} else if (dir == 2) {
			if (score > highScore) {
	            database.updateSnakeScore(userId, score);
	        } 
			Form.setUserId(userId);
			boolean isColl = checkCollision(headX - 1, headY);
			if (isColl == true) {
				JButton gameOver = new JButton("Game over!");
				gameOver.setBackground(new Color(128, 0, 0));
				gameOver.setBounds(132, 60, 144, 30);
				gameOver.setPreferredSize(new Dimension(80, 30));
				panelNorth.add(gameOver);
				timer.stop();
				gameOver.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						new Form().setVisible(true); // SnakeGame 불러오기
		                Form.updateLoginLabel();
						setVisible(false);
					}
				});
				return;
			}
			snake.add(0, new XY(headX - 1, headY));
			snake.remove(snake.size() - 1);
		} else if (dir == 3) {
			if (score > highScore) {
	            database.updateSnakeScore(userId, score);
	        } 
			Form.setUserId(userId);
			boolean isColl = checkCollision(headX + 1, headY);
			if (isColl == true) {
				JButton gameOver = new JButton("Game over!");
				gameOver.setBackground(new Color(128, 0, 0));
				gameOver.setBounds(132, 60, 144, 30);
				gameOver.setPreferredSize(new Dimension(80, 30));
				panelNorth.add(gameOver);
				timer.stop();
				gameOver.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						new Form().setVisible(true); // SnakeGame 불러오기
		                Form.updateLoginLabel();
						setVisible(false);
					}
				});
				return;
			}
			snake.add(0, new XY(headX + 1, headY));
			snake.remove(snake.size() - 1);
		}

	}

	public boolean checkCollision(int x, int y) {
		if (x < 0 || x > 19 || y < 0 || y > 19) { // 충돌
			return true;
		}
		// 몸통과 충돌
		for (XY xy : snake) {
			if (x == xy.x && y == xy.y) {
				return true;
			}
		}
		if (map[y][x] == 9) {
			map[y][x] = 0;
			addTail();
			makeFruit();
			score += 10;
		}
		return false;
	}

	public void addTail() {
		int tailX = snake.get(snake.size() - 1).x;
		int tailY = snake.get(snake.size() - 1).y;
		int tailX2 = snake.get(snake.size() - 2).x;
		int tailY2 = snake.get(snake.size() - 2).y;

		if (tailX < tailX2) { // Moving right
			snake.add(new XY(tailX - 1, tailY));
		} else if (tailX > tailX2) { // Moving left
			snake.add(new XY(tailX + 1, tailY));
		} else if (tailY < tailY2) { // Moving down
			snake.add(new XY(tailX, tailY - 1));
		} else if (tailY > tailY2) { // Moving up
			snake.add(new XY(tailX, tailY + 1));
		}
	}

	public void updateUI() {
		labelTitle.setText("Score : " + score + "    Time : " + time);
		labelMessage.setText("HighScore :" + highScore);
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				if (map[i][j] == 0) { // 다 비었다면
					panels[i][j].setIcon(null);
					// panels[i][j].setIcon(background);
					int r,g,b;
					r =  154;
					g = 205;
					b = 50;
					panels[i][j].setBackground(new Color(r,g,b));
				} else if (map[i][j] == 9) {
					panels[i][j].setIcon(apple);
				}
			}
		}
		// 뱀 그림
		int index = 0;
		XY prev = snake.get(0);
//		int headX = snake.get(1).x;
//		int headY = snake.get(1).y;
//		int headX2 = snake.get(2).x;
//		int headY2 = snake.get(2).y;
		for (XY xy : snake) {
			// if(prev.x == xy.x) {
			// 이미지 16개
			// }

			int headX = prev.x;
			int headY = prev.y;
			int headX2 = xy.x;
			int headY2 = xy.y;

			if (index == 0) { // 머리
				// 0 : 위 1 : down 2 : left 3:right
				if (dir == 1) {
					panels[xy.y][xy.x].setIcon(headIcon_b);
				} else if (dir == 0) {
					panels[xy.y][xy.x].setIcon(headIcon_f);
				} else if (dir == 3) {
					panels[xy.y][xy.x].setIcon(headIcon_r);
				} else if (dir == 2) {
					panels[xy.y][xy.x].setIcon(headIcon_l);
				}
				// 꼬리
			} else if (snake.size() - 1 == index) {
				if (headY > headY2) {
					panels[xy.y][xy.x].setIcon(tailIcon_b);
				} else if (headY < headY2) {
					panels[xy.y][xy.x].setIcon(tailIcon_f);
				} else if (headX > headX2) {
					panels[xy.y][xy.x].setIcon(tailIcon_r);
				} else if (headX < headX2) {
					panels[xy.y][xy.x].setIcon(tailIcon_l);
				}

			} else { //꼬리
				if (headY > headY2) {
					panels[xy.y][xy.x].setIcon(bodyIcon_b);
				} else if (headY < headY2) {
					panels[xy.y][xy.x].setIcon(bodyIcon_f);
				} else if (headX > headX2) {
					panels[xy.y][xy.x].setIcon(bodyIcon_r);
				} else if (headX < headX2) {
					panels[xy.y][xy.x].setIcon(bodyIcon_l);
				}

			}
			index++;
			// prev = xy;
			prev = xy;

		}
	}

	public void makeSnakeList() {
		snake.add(new XY(10, 10)); // 머리
		snake.add(new XY(9, 10)); // 몸통
		snake.add(new XY(8, 10)); // 꼬리
	}

	public void initUI() {

		panelNorth = new JPanel();
		panelNorth.setBounds(0, 0, 400, 100);
		panelNorth.setPreferredSize(new Dimension(400, 100));
		panelNorth.setBackground(Color.BLACK);
		panelNorth.setLayout(null);

		labelTitle = new JLabel();
		labelTitle.setBounds(0, 5, 400, 50);
		labelTitle.setPreferredSize(new Dimension(400, 50));
		labelTitle.setFont(new Font("TimesRoman", Font.BOLD, 20));
		labelTitle.setForeground(Color.WHITE);
		labelTitle.setHorizontalAlignment(JLabel.CENTER);
		panelNorth.add(labelTitle);
		
		int r,g,b;
		r =  173;
		g = 216;
		b = 255;
		
		labelMessage = new JLabel();
		labelMessage.setBounds(0, 45, 400, 50);
		labelMessage.setPreferredSize(new Dimension(400, 20));
		labelMessage.setFont(new Font("TimesRoman", Font.BOLD, 20));
		labelMessage.setForeground(new Color(r,g,b));
		labelMessage.setHorizontalAlignment(JLabel.CENTER);
		panelNorth.add(labelMessage);

		// Center
		getContentPane().add(panelNorth);

		panelCenter = new JPanel();
		panelCenter.setBounds(0, 100, 400, 400);
		panelCenter.setLayout(new GridLayout(20, 20)); // 셀의 갯수
		for (int i = 0; i < 20; i++) { // 행
			for (int j = 0; j < 20; j++) { // 열
				map[i][j] = 0; // 초기화
				panels[i][j] = new JLabel();
				panels[i][j].setOpaque(true);
				panels[i][j].setPreferredSize(new Dimension(20, 20));
				panels[i][j].setBackground(Color.GRAY);
				panelCenter.add(panels[i][j]);
			}
		}

		getContentPane().add(panelCenter);
	}

}
