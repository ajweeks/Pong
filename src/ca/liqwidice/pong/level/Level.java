package ca.liqwidice.pong.level;

import java.awt.Color;
import java.awt.Graphics;

import ca.liqwidice.pong.Colour;
import ca.liqwidice.pong.Pong;
import ca.liqwidice.pong.input.Keyboard.Key;
import ca.liqwidice.pong.paddle.AIPaddle;
import ca.liqwidice.pong.paddle.Paddle;
import ca.liqwidice.pong.paddle.PlayerPaddle;
import ca.liqwidice.pong.sound.Sound;

public class Level {

	public static final float BALL_XV = 9.0f;

	public static final int WINNING_SCORE = 11;

	private Ball ball;
	private Paddle player1; //the paddle on the left side of the screen
	private Paddle player2; //the paddle on the right of the screen

	private int ticks = 0;
	private boolean paused = false;
	private int player1Score = 0, player2Score = 0;
	private boolean gameOver = false;

	/** @param player1 is the paddle on the left, 
	 *  @param player2 is the paddle on the right */
	public Level(Paddle player1, Paddle player2) {
		ball = Ball.newBall(this, false);
		this.player1 = player1;
		this.player2 = player2;
	}

	public void update() {
		ticks++;
		if (paused) return;

		if (player1Score >= WINNING_SCORE || player2Score >= WINNING_SCORE) {
			gameOver = true;
		}
		if (gameOver) return;

		player1.update(ball);
		player2.update(ball);
		ball.update();
		if (ball.isOffScreen()) {
			resetBall();
		}

		//PLAYER 1
		if (ball.x < player1.x + player1.width
				&& (ball.y + ball.height > player1.y && ball.y < player1.y + player1.height)) { //ball is hitting top or bottom of paddle
			if (ball.x < player1.x) return; //ball went all the way through the paddle
			float halfHeight = player1.height / 2;
			float angle = ((player1.y + (player1.height / 2)) - (ball.y + (ball.height / 2))) / halfHeight;
			ball.x = player1.x + player1.width;
			ball.setXv(-ball.getXv());
			ball.setYv(-angle * Level.BALL_XV);
			Sound.boop.play();
		}

		//PLAYER 2
		if (ball.x + ball.width > player2.x
				&& (ball.y + ball.height > player2.y && ball.y < player2.y + player2.height)) { //ball is hitting top or bottom of paddle
			if (ball.x > player2.x + player2.width) return; //ball went all the way through the paddle
			float halfHeight = player2.height / 2;
			float angle = ((player2.y + (player2.height / 2)) - (ball.y + (ball.height / 2))) / halfHeight;
			ball.x = player2.x - ball.width;
			ball.setXv(-ball.getXv());
			ball.setYv(-angle);
			Sound.boop.play();
		}
	}

	public void resetBall() {
		if (ball.x - ball.width < 0) {
			player2Score++;
			Sound.lose.play();
			ball = Ball.newBall(this, false);
		} else if (ball.x > Pong.SIZE.width) {
			player1Score++;
			Sound.win.play();
			ball = Ball.newBall(this, true);
		} else {
			System.err.println("no one scored!!");
			return;
		}
	}

	public void resetGame() {
		gameOver = false;
		player1Score = 0;
		player1.reset();
		player2Score = 0;
		player2.reset();
	}

	public void render(Graphics g) {
		if (Key.ESC.clicked) {
			resetGame();
			return;
		}
		if (gameOver) {
			g.setFont(Pong.font32);
			if (player2Score >= WINNING_SCORE) {
				g.setColor(Colour.WINNER_BG);
				g.fillRect(Pong.SIZE.width / 2, 0, Pong.SIZE.width / 2, Pong.SIZE.height);
				g.setColor(Color.WHITE);
				g.drawString("WINNER!", 500, 200);
				g.setColor(Colour.LOSER_BG);
				g.fillRect(0, 0, Pong.SIZE.width / 2, Pong.SIZE.height);
				g.setColor(Color.WHITE);
				g.drawString("LOSER", 120, 200);
			} else {
				g.setColor(Colour.WINNER_BG);
				g.fillRect(0, 0, Pong.SIZE.width / 2, Pong.SIZE.height);
				g.setColor(Color.WHITE);
				g.drawString("WINNER!", 120, 200);
				g.setColor(Colour.LOSER_BG);
				g.fillRect(Pong.SIZE.width / 2, 0, Pong.SIZE.width / 2, Pong.SIZE.height);
				g.setColor(Color.WHITE);
				g.drawString("LOSER", 500, 200);
			}
		}

		ball.render(g);
		player1.render(g);
		player2.render(g);

		g.setFont(Pong.font32);
		g.setColor(Color.WHITE);
		g.drawString(player1Score + "", Pong.SIZE.width / 2 - 50 - g.getFontMetrics().stringWidth(player1Score + ""),
				24);
		g.drawString(player2Score + "", Pong.SIZE.width / 2 + 50, 24);

		for (int i = 0; i < 8; i++) {
			g.fillRect(Pong.SIZE.width / 2 - 6, i * 65 - 10, 12, 45); //midfield lines
		}

		if (paused) {
			g.setColor(new Color(25, 25, 25, 150));
			g.fillRect(0, 0, Pong.SIZE.width, Pong.SIZE.height);
			if (ticks < 30) {
				g.setColor(Color.WHITE);
			} else {
				g.setColor(Color.GRAY);
			}
			if (ticks > 60) ticks = 0;
			g.drawString("PAUSED", (Pong.SIZE.width / 2) - (g.getFontMetrics().stringWidth("PAUSED") / 2), 200);
		}
	}

	public Paddle getPlayer1() {
		return player1;
	}

	public Paddle getPlayer2() {
		return player2;
	}

	public void movePlayer1(int newY) {
		player1.y = newY;
	}

	public void movePlayer2(int newY) {
		player2.y = newY;
	}

	public void setDifficulty(float speed) {
		if (player2 instanceof AIPaddle) {
			((AIPaddle) player2).setSpeed(speed);
		} else System.err.println("player2 isn't an ai! You can't change it's difficulty");
	}

	public static Level getDefaultPVAILevel() {
		return new Level(new PlayerPaddle(Paddle.DEFAULT_X_1, Paddle.DEFAULT_Y, Paddle.WIDTH, Paddle.HEIGHT, true,
				true, true), new AIPaddle(Paddle.DEFAULT_X_2, Paddle.DEFAULT_Y, Paddle.WIDTH, Paddle.HEIGHT,
				AIPaddle.MEDIUM_SPEED));
	}

	public static Level getDefaultLocalPVPLevel() {
		return new Level(new PlayerPaddle(Paddle.DEFAULT_X_1, Paddle.DEFAULT_Y, Paddle.WIDTH, Paddle.HEIGHT, false,
				true, false), new PlayerPaddle(Paddle.DEFAULT_X_2, Paddle.DEFAULT_Y, Paddle.WIDTH, Paddle.HEIGHT,
				false, false, true));
	}

	public static Level getDefaultNetworkPVPLevel() {
		return new Level(new PlayerPaddle(Paddle.DEFAULT_X_1, Paddle.DEFAULT_Y, Paddle.WIDTH, Paddle.HEIGHT, true,
				true, true), new PlayerPaddle(Paddle.DEFAULT_X_2, Paddle.DEFAULT_Y, Paddle.WIDTH, Paddle.HEIGHT, false,
				false, false));
	}

	public static Level getDefaultAIVAILevel() {
		return new Level(new AIPaddle(Paddle.DEFAULT_X_1, Paddle.DEFAULT_Y, Paddle.WIDTH, Paddle.HEIGHT,
				AIPaddle.HARD_SPEED), new AIPaddle(Paddle.DEFAULT_X_2, Paddle.DEFAULT_Y, Paddle.WIDTH, Paddle.HEIGHT,
				AIPaddle.HARD_SPEED));
	}

	public int getPlayer1X() {
		return player1.x;
	}

	public int getPlayer1Y() {
		return player1.y;
	}

	public int getPlayer2X() {
		return player2.x;
	}

	public int getPlayer2Y() {
		return player2.y;
	}

	public int getBallX() {
		return ball.x;
	}

	public int getBallY() {
		return ball.y;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}
}