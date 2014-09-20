package ca.liqwidice.pong;

import java.awt.Color;
import java.awt.Graphics;

public class Level {

	public static final float BALL_XV = 9.0f;
	public static final float PLAYER_PADDLE_SPEED = 8.0f;

	private Ball ball;
	private PlayerPaddle player;
	private AIPaddle ai;
	private boolean paused = false;
	private int playerScore = 0, aiScore = 0;

	public Level() {
		ball = Ball.newBall(this, false);
		player = new PlayerPaddle(50, Pong.SIZE.height / 2 - 50, 25, 100);
		ai = new AIPaddle(Pong.SIZE.width - 75, Pong.SIZE.height / 2 - 50, 25, 100, AIPaddle.EASY_SPEED);
	}

	public void update() {
		if (paused) return;

		player.update();
		ball.update();
		ai.update(ball);

		if (ball.isOffScreen()) {
			resetGame();
		}

		//PLAYER
		if (ball.x < player.x + player.width && (ball.y + ball.height > player.y && ball.y < player.y + player.height)) { //ball is hitting top or bottom of paddle
			float halfHeight = player.height / 2;
			float angle = ((player.y + (player.height / 2)) - (ball.y + (ball.height / 2))) / halfHeight;
			ball.x = player.x + player.width;
			ball.setXv(-ball.getXv());
			ball.setYv(-angle * Level.BALL_XV);
			Sound.boop.play();
		}

		//AI
		if (ball.x + ball.width > ai.x && (ball.y + ball.height > ai.y && ball.y < ai.y + ai.height)) { //ball is hitting top or bottom of paddle
			float halfHeight = ai.height / 2;
			float angle = ((ai.y + (ai.height / 2)) - (ball.y + (ball.height / 2))) / halfHeight;
			ball.x = ai.x - ball.width;
			ball.setXv(-ball.getXv());
			ball.setYv(-angle);
			Sound.boop.play();
		}
	}

	private void resetGame() {
		if (ball.x - ball.width < 0) {
			aiScore++;
			Sound.lose.play();
			ball = Ball.newBall(this, false);
		} else if (ball.x > Pong.SIZE.width) {
			playerScore++;
			Sound.win.play();
			ball = Ball.newBall(this, true);
		} else {
			System.out.println("no one scored!!");
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

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}
}