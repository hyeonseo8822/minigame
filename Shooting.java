package project;

import java.awt.*;
import java.util.prefs.Preferences;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import javax.swing.*;

import project.Form;

@SuppressWarnings("serial")
public class Shooting extends JFrame implements Runnable, KeyListener {
	private BufferedImage bi = null;
	private ArrayList<Missile> missileList = null;
	private ArrayList<Enemy> enemyList = null;
	private ArrayList<SplitPiece> splitList = null;
	private boolean left = false, right = false, up = false, down = false, fire = false;
	private boolean start = false, end = false, playing = false;
	private int w = 300, h = 500, x = 130, y = 450, xw = 20, xh = 20;
	private int score = 0;
	private int time = 0;
	static private ImageIcon enemy,bullet,fighter;
	static JPanel panelNorth;
	static JPanel panelCenter;
	static JLabel labelTitle;
	static JLabel labelMessage;
	
	long timeTemp = 0;
    private Database database;
    String userId;
    private int highScore;
	
	public Shooting(String userId) {
        this.database = new Database();
        this.userId = userId;
        this.highScore = database.getShootScore(userId); 
        System.out.println(highScore);
        bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		// 리스트에 무한히 담을 수 있도록 ArrayList를 사용하였음.
		missileList = new ArrayList<Missile>();
		enemyList = new ArrayList<Enemy>();
		splitList = new ArrayList<SplitPiece>();

		this.setResizable(true);
		this.addKeyListener(this);
		this.setSize(w, h);
		this.setTitle("Shooting Game");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);  
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		enemy = resizeIcon(new ImageIcon(".//src//project//enemy.png"),xw, xh);
		bullet = resizeIcon(new ImageIcon(".//src//project//bullet.png"),xw, xh);
		fighter = resizeIcon(new ImageIcon(".//src//project//fighter.png"),xw, xh);
	}

	private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
		Image img = icon.getImage();
		return new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH));
	}

	// 쓰레드로 동작하는 부분 (미사일 발사, 키 입력, 적들의 공격, 부딪혀서 점수 증가 등이 개별적으로 이루어져야 하기 때문)
	public void run() {
		try {
			int missileCount = 0;
			int enemyCount = 0;
			int count = 0;
			reset();
			test();
			while(true) {
				Thread.sleep(10);

				if(start) {
					count = 0;
					time++;
					// 적의 등장 시간 조정하는 부분 숫자 줄이면 빨리등장
					if(enemyCount > 500-score) {
						enCreate();
						enemyCount = 0;
					}

					// 총알의 발사 간격 조정 숫자 줄이면 빠르게 발사
					if(missileCount >= 100) {
						fireMs();
						missileCount = 0;
					}
					missileCount += 10;
					enemyCount += 10;
					keyControl();
					crashChk();
				}

				// GAME OVER 후 시간을 셈
				if(end && count < 2000)
					count += 10;
				// GAME OVER 후 1초가 되면 화면 멈춤
				if (count < 1000)
					draw();      

			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	// 미사일 리스트의 전체 크기가 100 이하일 때, 새로운 미사일을 리스트에 추가함
	public void fireMs() {
		if(fire) {
			if(missileList.size() < 100) {
				Missile m = new Missile(this.x, this.y);
				missileList.add(m);
			}
		}
	}

	// 랜덤하게 적의 위치를 추가하여 리스트에 x값, y값을 추가함
	public void enCreate() {
		for(int i = 0; i < 1; i++) {
			double rx = Math.random() * (w - xw);
			double ry = Math.random() * 50;
			Enemy en = new Enemy((int)rx, (int)ry);
			enemyList.add(en);
		}
	}

	// 미사일과 적이 닿았는지 체크하는 부분
	public void crashChk() {
		//Graphics g = this.getGraphics();
		Polygon p = null;

		// 현재 화면에 뿌려진 미사일의 전체 리스트와 내려오는 적들의 전체 리스트를 가져옴.
		for(int i = 0; i < missileList.size(); i++) {
			Missile m = (Missile)missileList.get(i);

			for(int j = 0; j < enemyList.size(); j++) {
				Enemy e = (Enemy)enemyList.get(j);
				// 미사일과 적의 교차점을 계산함.
				int[] xpoints = {m.x, (m.x + m.w), (m.x + m.w), m.x};
				int[] ypoints = {m.y, m.y, (m.y + m.h), (m.y + m.h)};
				p = new Polygon(xpoints, ypoints, 4);

				// 해당 폴리곤이 교차점으로 계산되었으면 그 미사일과 적을 리스트에서 제거하고 점수에 1을 더해줌
				if(p.intersects((double)e.x, (double)e.y, (double)e.w, (double)e.h)) {
					missileList.remove(i);
					enemyList.remove(j);
					score += 10;
				}
			}
		}

		// 적의 전체 리스트 가져옴
		for(int i = 0; i < enemyList.size(); i++) {
			Enemy e = (Enemy)enemyList.get(i);
			int[] xpoints = {x, (x + xw), (x + xw), x};
			int[] ypoints = {y, y, (y + xh), (y + xh)};
			p = new Polygon(xpoints, ypoints, 4);

			// 위와 동일하나 미사일과 적이 아닌 사용자와 적의 위치 교차점을 계산하여 닿게 된다면 게임이 종료되게 함.
			if(p.intersects((double)e.x, (double)e.y, (double)e.w, (double)e.h)) {
				enemyList.remove(i);
				start = false;
				end = true;
			}
		}
	}

	public void test() {

	}
	// 이부분이 화면에 그리는 부분.
	public void draw() {
		// Graphics 객체 얻기
		Graphics gs = bi.getGraphics();

		// 배경을 검은색으로 칠하기
		gs.setColor(Color.black);
		gs.fillRect(0, 0, w, h);

		// 게임 시간과 점수 출력
		gs.setColor(Color.white);  // 글자색을 흰색으로 설정
		gs.drawString("시간: " + (double)time / 100, 30, 50);
		gs.drawString("점수 : " + score, 30, 70);
		gs.drawString("최고점수 : " + highScore, 30, 90);

		// 게임 오버 상태일 때
		if (end) {
			if (score > highScore) {
	            database.updateShootScore(userId, score);
	        }
			Form.setUserId(userId);
			gs.setColor(Color.red);  // 게임 오버 메시지 색을 빨간색으로 설정
			gs.drawString("G A M E     O V E R", 100, 250); // 게임 오버 메시지 그리기
			gs.setColor(Color.white);
			gs.drawString("Click Enter", 120, 270);

			// 피 남은 상태 처리 (playing이 true일 때만 스플릿 처리)
			if (playing) {
				// 화면에서 분리된 피스들을 추가
				SplitPiece piece1 = new SplitPiece(x, y, xw / 2, xh / 2);
				SplitPiece piece2 = new SplitPiece(x, y + xh / 2, xw / 2, xh / 2);
				SplitPiece piece3 = new SplitPiece(x + xw / 2, y, xw / 2, xh / 2);
				SplitPiece piece4 = new SplitPiece(x + xw / 2, y + xh / 2, xw / 2, xh / 2);
				splitList.add(piece1);
				splitList.add(piece2);
				splitList.add(piece3);
				splitList.add(piece4);
				playing = false; // 한 번만 실행되도록 설정
			}

			// 분할된 조각들을 화면에 그리기
			for (int i = 0; i < splitList.size(); i++) {
				SplitPiece p = splitList.get(i);
				gs.setColor(Color.red); // 조각 색은 빨간색으로
				gs.fillRect(p.x, p.y, p.w, p.h);

				// 조각 분할 처리
				if (i == 0) p.split(true, true);
				if (i == 1) p.split(true, false);
				if (i == 2) p.split(false, true);
				if (i == 3) p.split(false, false);
			}
		} else {
			// 게임 중일 때 객체 그리기
			gs.setColor(Color.white); // 미사일과 플레이어 객체는 흰색
			gs.fillRect(x, y, xw, xh); // 플레이어 (미사일 발사 위치)
		}

		// 미사일 리스트 가져와서 미사일을 파란색으로 만들고 화면 위에 뿌리기
		for (int i = 0; i < missileList.size(); i++) {
			Missile m = missileList.get(i);
			gs.setColor(Color.blue);
			gs.drawOval(m.x, m.y, m.w, m.h);

			// 화면 끝에 도달했으면 미사일을 리스트에서 지우고 삭제
			if (m.y < 0) {
				missileList.remove(i);
			}

			// 미사일 계속 y축 위로 움직임
			m.moveMs();
		}

		// 적들 그리기
		gs.setColor(Color.red); // 적은 빨간색으로 그리기
		for (int i = 0; i < enemyList.size(); i++) {
			Enemy e = enemyList.get(i);
			gs.fillRect(e.x, e.y, e.w, e.h);

			// 바닥까지 도달했으면 적을 리스트에서 지움
			if (e.y >= h) {
				enemyList.remove(i);
			}

			// 적은 계속 y축 아래로 움직임
			e.moveEn();
		}

		// 그려진 이미지 화면에 적용
		Graphics ge = this.getGraphics();
		ge.drawImage(bi, 0, 0, w, h, this);
	}


	public void reset() {
		time = 0;
		score = 0;
		start = true;
		end = false;
		playing = true;
		w = 300;
		h = 500;
		x = 130;
		y = 450;
		xw = 20;
		xh = 20;
		enemyList.clear();
		missileList.clear();
		splitList.clear();
	}

	// 키 입력시 한번에 움직이는 거리 조정하는 부분
	public void keyControl() {
		if(0 < x) {
			if(left) x -= 3;
		}

		if(w > x + xw) {
			if(right) x += 3;
		}

		if(25 < y) {
			if(up) y -= 3;
		}

		if(h > y + xh) {
			if(down) y += 3;
		}
	}

	// 키 누를때
	public void keyPressed(KeyEvent ke) {
		switch(ke.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			left = true;
			break;
		case KeyEvent.VK_RIGHT:
			right = true;
			break;
		case KeyEvent.VK_A:
			fire = false;
			if(timeTemp == 0 || System.currentTimeMillis()  >  timeTemp + 5000) {
				timeTemp = System.currentTimeMillis();
				fire = true;
			}
			break;
		case KeyEvent.VK_ENTER:  // 엔터 키 입력
			if (end) {  // 게임이 끝났을 때만 실행
				// Form.java로 이동하는 코드 추가
				new Form();  // Form 클래스를 새로 열기 (예: JFrame 전환)
	            Form.updateLoginLabel();
				this.dispose();  // 현재 창을 닫기 (옵션)
			}
			break;
		}
	}

	// 키 뗄때
	public void keyReleased(KeyEvent ke) {
		switch(ke.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			left = false;
			break;
		case KeyEvent.VK_RIGHT:
			right = false;
			break;
		case KeyEvent.VK_A:
			fire = false;
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}

// 미사일 클래스 미사일 기본 크기와 생성자, 계속 아래로 움직이게 하는 메소드 moveMs()
class Missile {
	int x;
	int y;
	int w = 5;
	int h = 5;

	public Missile(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void moveMs() {
		y--;
	}
}

// 적 클래스 적 기본 크기와 생성자, 계속 위로 움직이게 하는 메소드 moveEn()
class Enemy {
	int x;
	int y;
	int w = 15;
	int h = 15;

	public Enemy(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void moveEn() {
		y++;
	} 
}
class SplitPiece {
	int x;
	int y;
	int w;
	int h;
	public SplitPiece(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	public void split(boolean left, boolean up) {
		if(left) x -= 3;
		else x += 3;
		if(up) y -= 3;
		else y += 3;
	}
	public static void main(String[] args) {
		
	}
}