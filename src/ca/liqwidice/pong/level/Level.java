package ca.liqwidice.pong.level;

import java.awt.Color;
import java.awt.Graphics;

import ca.liqwidice.pong.Colour;
import ca.liqwidice.pong.Pong;
import ca.liqwidice.pong.paddle.AIPaddle;
import ca.liqwidice.pong.paddle.Paddle;
import ca.liqwidice.pong.paddle.PlayerPaddle;
import ca.liqwidice.pong.sound.Sound;

public class Level {

	public static final int WINNING_SCORE = 11;

	private Ball ball;
	private Paddle player1; //the paddle on the left side of the screen
	private Paddle player2; //the paddle on the right of the screen

	private int ticks = 0;
	private boolean paused = false;
	private short player1Score = 0, player2Score = 0;
	private boolean gameOver = false;
	private boolean wasReset = false;

	/** @param player1 is the paddle on the left, 
	 *  @param player2 is the paddle on the right */
	public Level(Paddle player1, Paddle player2, byte seed, Ball ball) {
		this.player1 = player1;
		this.player2 = player2;
		this.ball = ball;
	}

	/** @param player1 is the paddle on the left, 
	 *  @param player2 is the paddle on the right */
	public Level(Paddle player1, Paddle player2, byte seed) {
		this(player1, player2, seed, Ball.newBall(false));
	}

	public boolean updateBall = true;

	public void update() {
		ticks++;
		if (player1Score >= WINNING_SCORE || player2Score >= WINNING_SCORE) gameOver = true;
		if (paused || gameOver) return;

		if (updateBall) ball.update();
		player1.update(ball);
		player2.update(ball);
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
			ball.setYv(-angle * ball.getXv());
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
			ball.setYv(angle * ball.getXv());
			Sound.boop.play();
		}
	}

	public void resetBall() {
		if (ball.x <= 0) {
			player2Score++;
			Sound.lose.play();
			ball.reset(false);
		} else if (ball.x + ball.width >= Pong.SIZE.width) {
			player1Score++;
			Sound.win.play();
			ball.reset(true);
		} else {
			System.err.println("no one scored!!");
			return;
		}
	}

	public void resetGame() {
		wasReset = true;
		gameOver = false;
		player1Score = 0;
		player1.reset();
		player2Score = 0;
		player2.reset();
	}

	public void render(Graphics g) {
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

	public boolean wasReset() {
		return wasReset;
	}

	public void setWasReset(boolean wasReset) {
		this.wasReset = wasReset;
	}
	
	public void setDifficulty(float speed) {
		if (player2 instanceof AIPaddle) {
			((AIPaddle) player2).setSpeed(speed);
		} else System.err.println("player2 isn't an ai! You can't change it's difficulty");
	}

	public static Level getDefaultPVAILevel() {
		return new Level(new PlayerPaddle(Paddle.DEFAULT_X_1, true, true, true), new AIPaddle(Paddle.DEFAULT_X_2,
				AIPaddle.MEDIUM_SPEED), (byte) -1);
	}

	public static Level getDefaultLocalPVPLevel() {
		return new Level(new PlayerPaddle(Paddle.DEFAULT_X_1, false, true, false), new PlayerPaddle(Paddle.DEFAULT_X_2,
				false, false, true), (byte) -1);
	}

	public static Level getDefaultNetworkPVPLevel(byte seed) {
		return new Level(new PlayerPaddle(Paddle.DEFAULT_X_1, true, true, true), new PlayerPaddle(Paddle.DEFAULT_X_2,
				false, false, false), seed);
	}

	public static Level getDefaultAIVAILevel() {
		return new Level(new AIPaddle(Paddle.DEFAULT_X_1, AIPaddle.HARD_SPEED), new AIPaddle(Paddle.DEFAULT_X_2,
				AIPaddle.HARD_SPEED), (byte) -1);
	}

	public Paddle getPlayer1() {
		return player1;
	}

	public Paddle getPlayer2() {
		return player2;
	}

	public short getPlayer1Score() {
		return player1Score;
	}

	public short getPlayer2Score() {
		return player2Score;
	}

	public void setPlayer1Score(short player1Score) {
		this.player1Score = player1Score;
	}

	public void setPlayer2Score(short player2Score) {
		this.player2Score = player2Score;
	}

	public Ball getBall() {
		return ball;
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