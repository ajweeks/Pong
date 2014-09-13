package ca.liqwidice.pong;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Level {

	public static final double BALL_SPEED = 5.5;
	public static final double PADDLE_SPEED = 4.75;

	private Ball ball;
	private PlayerPaddle player;
	private AIPaddle ai;
	private boolean paused = false;
	private int playerScore = 0, aiScore = 0;

	public Level() {
		ball = Ball.newBall(200, 200, 30);
		player = new PlayerPaddle(50, 120, 25, 100);
		ai = new AIPaddle(Pong.SIZE.width - 75, 120, 25, 100);
	}

	public void update() {
		if (paused) return;

		ball.update();
		player.update();
		ai.update(ball);

		if (ball.isOffScreen()) {
			resetGame();
		} else if (ball.intersects(player)) {
			if (ball.x < player.x + player.width - 5) {//ball is hitting top or bottom of paddle, don't bounce it
				return;
			}
			double angle = ((player.y + (player.height / 2)) - (ball.y + (ball.height / 2))) / 7;
			ball.x = player.x + player.width;
			ball.setXv(-ball.getXv());
			ball.setYv(-angle);
		} else if (ball.intersects(ai)) {
			if (ball.x + ball.width > ai.x + 5) {//ball is hitting top or bottom of paddle, don't bounce it
				return;
			}
			double angle = ((ai.y + (ai.height / 2)) - (ball.y + (ball.height / 2))) / 7;
			ball.x = ai.x - ball.width;
			ball.setXv(-ball.getXv());
			ball.setYv(-angle);
		}
	}

	private void resetGame() {
		if (ball.x - ball.width < 0) aiScore++;
		else if (ball.x > Pong.SIZE.width) playerScore++;
		ball = Ball.newBall(200, 200, 30);
	}

	public void render(Graphics g) {
		ball.render(g);
		player.render(g);
		ai.render(g);

		g.setFont(new Font("Consolas", Font.BOLD, 32));
		g.setColor(Color.WHITE);
		g.drawString(playerScore + "", Pong.SIZE.width / 2 - 50 - g.getFontMetrics().stringWidth(playerScore + ""), 24);
		g.drawString(aiScore + "", Pong.SIZE.width / 2 + 50, 24);

		//TODO draw midfield lines

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
