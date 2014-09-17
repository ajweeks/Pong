package ca.liqwidice.pong;

import java.awt.Button;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;

public class Level {

	public static final float EASY = 1.0f;
	public static final float MED = 1.5f;
	public static final float HARD = 2.0f;
	
	public static final float BALL_SPEED = 9.0f;
	public static final float PLAYER_PADDLE_SPEED = 8.0f;
	public static final float AI_PADDLE_SPEED = 4.0f;

	private Ball ball;
	private PlayerPaddle player;
	private AIPaddle ai;
	private boolean paused = false;
	private int playerScore = 0, aiScore = 0;
	private float playerPaddleSpeed;
	private float aiPaddleSpeed;
	private float ballSpeed;
	
	
	public Level(float difficulty) {
		playerPaddleSpeed = difficulty * PLAYER_PADDLE_SPEED;
		aiPaddleSpeed = difficulty * AI_PADDLE_SPEED;
		ballSpeed = difficulty * BALL_SPEED;
		
		ball = Ball.newBall(this, false);
		player = new PlayerPaddle(this, 50, Pong.SIZE.height / 2 - 50, 25, 100);
		ai = new AIPaddle(this, Pong.SIZE.width - 75, Pong.SIZE.height / 2 - 50, 25, 100);
	}

	public void update() {
		if (paused) return;

		player.update();
		ball.update();
		ai.update(ball);

		if (ball.isOffScreen()) {
			resetGame();
		} else if (ball.intersects(player)) {
			if (ball.x < player.x + player.width - BALL_SPEED) { //ball is hitting top or bottom of paddle
				if (ball.y + ball.height / 2 > player.y + player.height / 2) ball.y -= BALL_SPEED;
				else ball.y += BALL_SPEED + 1;
				ball.setYv(-ball.getYv());
				return;
			}
			double angle = ((player.y + (player.height / 2)) - (ball.y + (ball.height / 2))) / 7;
			ball.x = player.x + player.width;
			ball.setXv(-ball.getXv());
			ball.setYv(-angle);
			Sound.boop.play();
		} else if (ball.intersects(ai)) {
			if (ball.x + ball.width > ai.x + BALL_SPEED) { //ball is hitting top or bottom of paddle
				if (ball.y + ball.height / 2 > ai.y + ai.height / 2) ball.y -= BALL_SPEED;
				else ball.y += BALL_SPEED + 1;
				ball.setYv(-ball.getYv());
				return;
			}
			double angle = ((ai.y + (ai.height / 2)) - (ball.y + (ball.height / 2))) / 7;
			ball.x = ai.x - ball.width;
			ball.setXv(-ball.getXv());
			ball.setYv(-angle);
			Sound.boop.play();
		}
	}

	private void resetGame() {
		int aiScoreBefore = aiScore;
		int playerScoreBefore = playerScore;
		if (ball.x - ball.width < 0) aiScore++;
		else if (ball.x > Pong.SIZE.width) playerScore++;
		if (aiScore > aiScoreBefore) {
			Sound.lose.play();
			ball = Ball.newBall(this, false);
		} else if (playerScore > playerScoreBefore) {
			Sound.win.play();
			ball = Ball.newBall(this, true);
		} else {
			System.out.println("no one scored!!");
			ball = Ball.newBall(this, false);
			return;
		}
	}

	public void render(Graphics g) {
		ball.render(g);
		player.render(g);
		ai.render(g);

		g.setFont(Pong.font32);
		g.setColor(Color.WHITE);
		g.drawString(playerScore + "", Pong.SIZE.width / 2 - 50 - g.getFontMetrics().stringWidth(playerScore + ""), 24);
		g.drawString(aiScore + "", Pong.SIZE.width / 2 + 50, 24);

		for (int i = 0; i < 8; i++) {
			g.fillRect(Pong.SIZE.width / 2 - 6, i * 65 - 10, 12, 45);
		}

		if (paused) {
			g.setColor(new Color(25, 25, 25, 150));
			g.fillRect(0, 0, Pong.SIZE.width, Pong.SIZE.height);
			g.setColor(Color.WHITE);
			g.drawString("PAUSED", (Pong.SIZE.width / 2) - (g.getFontMetrics().stringWidth("PAUSED") / 2), 200);
		}
	}

	public static double getPlayerPaddleSpeed() {
		return PLAYER_PADDLE_SPEED;
	}
	public static double getAiPaddleSpeed() {
		return AI_PADDLE_SPEED;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}
}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     