package Tetris;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

//WINDOW : 40 * 10
public class game extends JFrame {
	public static final int ROW = 20, COL = 10, SIDELENGTH = 30;
	// the display window
	public static int SCORE = 0, SLEEPTIME = 500;
	public static int[][] window = new int[ROW][COL];
	// the four pairs of coordinates of blocks 0:kind, (1,2)(3,4)(5,6)(7,8);
	public static final int[][] items = {
			{ 1, ROW - 3, COL / 2 + 1, ROW - 1, COL / 2 + 1, ROW - 2, COL / 2 + 1, ROW - 4, COL / 2 + 1 },
			{ 2, ROW - 1, COL / 2 + 1, ROW - 2, COL / 2 + 1, ROW - 1, COL / 2, ROW - 2, COL / 2 },
			{ 3, ROW - 2, COL / 2, ROW - 2, COL / 2 + 1, ROW - 1, COL / 2, ROW - 3, COL / 2 + 1 },
			{ 4, ROW - 2, COL / 2, ROW - 2, COL / 2 + 1, ROW - 1, COL / 2 + 1, ROW - 3, COL / 2 },
			{ 5, ROW - 2, COL / 2, ROW - 1, COL / 2, ROW - 3, COL / 2, ROW - 3, COL / 2 + 1 },
			{ 6, ROW - 2, COL / 2 + 1, ROW - 1, COL / 2 + 1, ROW - 3, COL / 2 + 1, ROW - 3, COL / 2 },
			{ 7, ROW - 2, COL / 2 + 1, ROW - 1, COL / 2 + 1, ROW - 2, COL / 2, ROW - 2, COL / 2 + 2 } };
	public static int next_item = (int) (1 + Math.random() * 7);// Math.random();
	public static int[] pos = new int[9];
	public static boolean gaming = true, gameover = false, pause = false;

	public game() {
		this.setBounds(0, 0, 1000, 1000); // position,, size,,
		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent arg0) {
				if (gaming) {
					switch (arg0.getKeyCode()) {
					case KeyEvent.VK_LEFT:
						shift(-1);
						break;
					case KeyEvent.VK_RIGHT:
						shift(1);
						break;
					case KeyEvent.VK_UP: {
						int[] temp = new int[9];
						for (int i = 0; i < 9; i++) {
							temp[i] = pos[i];
						}
						changeShape(temp);
						if (!crash(temp)) {
							changeShape(pos);
						}
						break;
					}
					case KeyEvent.VK_DOWN:
						SLEEPTIME = 100;
						break;
					case KeyEvent.VK_ENTER:
						pause = !pause;
						break;
					}
				}
			}

			public void keyReleased(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_DOWN) {
					SLEEPTIME = 500;
				}
			}
		});
		this.setTitle("Tetris");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		while (!gameover) {
			try {

				Thread.sleep(SLEEPTIME);
				if (!pause) {
					moveDown();
					repaint();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void paint(Graphics g) {

		g.setColor(Color.white);
		g.fillRect(0, 0, 2000, 2000);
		g.setColor(Color.black);
		g.drawRect(100, 100, SIDELENGTH * COL, SIDELENGTH * ROW);
		// display the window
		for (int x = ROW - 1; x >= 0; x--) {
			for (int y = 0; y < COL; y++) {
				if (window[x][y] == 1) {
					g.fillRect(100 + SIDELENGTH * y, 100 + SIDELENGTH * (ROW - x - 1), SIDELENGTH, SIDELENGTH);
				}
			}
		}
		// display current and next items
		int[] next_graph = new int[9];
		for (int i = 0; i < 9; i++) {
			next_graph[i] = items[next_item - 1][i];
		}
		for (int i = 1; i < 9; i += 2) {
			g.drawRect(100 + SIDELENGTH * (pos[i + 1]), 100 + SIDELENGTH * (ROW - pos[i] - 1), SIDELENGTH, SIDELENGTH);
			g.drawRect(350 + SIDELENGTH * (next_graph[i + 1]), 150 + SIDELENGTH * (ROW - next_graph[i] - 1), SIDELENGTH,
					SIDELENGTH);
			g.drawString("Score: " + SCORE, 450, 350);
			g.drawString("Control:", 450, 400);
			g.drawString("Up: Change shape", 450, 420);
			g.drawString("Down: Speed up", 450, 440);
			g.drawString("Left/Right: Move to left/right", 450, 460);
			g.drawString("Enter: Pause or continue", 450, 480);
		}
	}

	public void shift(int dir) {
		if (!stop(dir)) {
			for (int i = 1; i < 9; i += 2) {
				pos[i + 1] += dir;
			}
		}
	}

	public boolean crash(int[] temp_pos) {
		for (int i = 1; i < 9; i += 2) {
			if (temp_pos[i] < 0 || window[temp_pos[i]][temp_pos[i + 1]] == 1 || temp_pos[i + 1] < 0
					|| temp_pos[i + 1] >= COL) {
				return true;
			}
		}
		return false;
	}

	public void changeShape(int[] pos1) {
		switch (pos1[0]) {
		case 1: {
			if (pos1[2] == pos1[4]) {
				pos1[3] = pos1[1];
				pos1[5] = pos1[1];
				pos1[7] = pos1[1];
				pos1[4] = pos1[2] + 2;
				pos1[6] = pos1[2] + 1;
				pos1[8] = pos1[2] - 1;
			} else {
				pos1[4] = pos1[2];
				pos1[6] = pos1[2];
				pos1[8] = pos1[2];
				pos1[3] = pos1[1] + 2;
				pos1[5] = pos1[1] + 1;
				pos1[7] = pos1[1] - 1;
			}
			break;
		}
		case 3: {
			if (pos1[5] != pos1[7]) {
				pos1[5] = pos1[1] - 1;
				pos1[8] = pos1[2] - 1;
			} else {
				pos1[5] = pos1[1] + 1;
				pos1[8] = pos1[2] + 1;
			}
			break;
		}
		case 4: {
			if (pos1[5] != pos1[7]) {
				pos1[7] = pos1[1] + 1;
				pos1[6] = pos1[2] - 1;
			} else {
				pos1[7] = pos1[1] - 1;
				pos1[6] = pos1[2] + 1;
			}
			break;
		}
		case 5: {
			if (pos1[4] == pos1[6] && pos1[6] == pos1[2]) {
				if (pos1[8] == pos1[2] + 1) {
					pos1[3]--;
					pos1[4]++;
					pos1[5]++;
					pos1[6]--;
					pos1[8] -= 2;
				} else {
					pos1[3]++;
					pos1[4]--;
					pos1[5]--;
					pos1[6]++;
					pos1[8] += 2;
				}
			} else {
				if (pos1[7] == pos1[1] - 1) {
					pos1[3]--;
					pos1[4]--;
					pos1[5]++;
					pos1[6]++;
					pos1[7] += 2;
				} else {
					pos1[3]++;
					pos1[4]++;
					pos1[5]--;
					pos1[6]--;
					pos1[7] -= 2;
				}
			}
			break;
		}
		case 6: {
			if (pos1[4] == pos1[6] && pos1[6] == pos1[2]) {
				if (pos1[8] == pos1[2] - 1) {
					pos1[3]--;
					pos1[4]++;
					pos1[5]++;
					pos1[6]--;
					pos1[7] += 2;
				} else {
					pos1[3]++;
					pos1[4]--;
					pos1[5]--;
					pos1[6]++;
					pos1[7] -= 2;
				}
			} else {
				if (pos1[7] == pos1[1] + 1) {
					pos1[3]--;
					pos1[4]--;
					pos1[5]++;
					pos1[6]++;
					pos1[8] += 2;
				} else {
					pos1[3]++;
					pos1[4]++;
					pos1[5]--;
					pos1[6]--;
					pos1[8] -= 2;
				}
			}
			break;
		}
		case 7: {
			if (pos1[4] == pos1[2]) {
				if (pos1[3] == pos1[1] + 1) {
					pos1[3]--;
					pos1[4]++;
					pos1[5]++;
					pos1[6]++;
					pos1[7]--;
					pos1[8]--;
				} else {
					pos1[3]++;
					pos1[4]--;
					pos1[5]--;
					pos1[6]--;
					pos1[7]++;
					pos1[8]++;
				}
			} else {
				if (pos1[4] == pos1[2] + 1) {
					pos1[3]--;
					pos1[4]--;
					pos1[5]--;
					pos1[6]++;
					pos1[7]++;
					pos1[8]--;
				} else {
					pos1[3]++;
					pos1[4]++;
					pos1[5]++;
					pos1[6]--;
					pos1[7]--;
					pos1[8]++;
				}
			}
			break;
		}
		}
		repaint();
	}

	public static void moveDown() {
		if (!stop(0)) {
			for (int i = 1; i < 9; i += 2) {
				pos[i]--;
			}
		} else {
			for (int i = 1; i < 9; i += 2) {
				window[pos[i]][pos[i + 1]] = 1;
			}
			gaming = false;
			eliminate();
			generateNewItem();
			gaming = true;
		}
	}

	public static boolean stop(int dir) {
		if (dir == 0) {
			for (int i = 1; i < 9; i += 2) {
				if (pos[i] < 1 || window[pos[i] - 1][pos[i + 1]] == 1) {
					return true;
				}
			}
			return false;
		} else {
			for (int i = 1; i < 9; i += 2) {
				if (pos[i + 1] + dir < 0 || pos[i + 1] + dir >= COL || window[pos[i]][pos[i + 1] + dir] == 1) {
					return true;
				}
			}
			return false;
		}

	}

	public static void eliminate() {
		// at most 4 rows to be eliminated
		for (int p = 0; p < 4; p++) {
			for (int i = 0; i < ROW; i++) {
				int sum_col = 0;
				for (int j = 0; j < COL; j++) {
					sum_col += window[i][j];
				}
				if (sum_col == COL) {
					SCORE += 1;
					for (int x = i; x < ROW; x++) {
						for (int y = 0; y < COL; y++) {
							if (x + 1 < ROW)
								window[x][y] = window[x + 1][y];
						}
					}
				}
			}
		}
	}

	public static void generateNewItem() {
		for (int i = 0; i < 9; i++) {
			pos[i] = items[next_item - 1][i];
		}
		next_item = (int) (1 + Math.random() * 7);
		if (stop(0)) {
			gameover = true;
		}
	}

	public static void main(String[] args) {
		generateNewItem();
		new game();
	}
}
