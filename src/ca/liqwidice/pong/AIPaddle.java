package ca.liqwidice.pong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class AIPaddle extends Rectangle {
	private static final long serialVersionUID = 1L;

	public AIPaddle(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public void update(Ball ball) {
		if (ball.y > this.y + height / 2 && ball.getYv() > 0) y += Level.PADDLE_SPEED;
		else if (ball.y < this.y + height / 2 && ball.getYv() < 0) y -= Level.PADDLE_SPEED;

		clamp();
	}

	private void clamp() {
		if (y > Pong.SIZE.height - height) y = Pong.SIZE.height - height;
		else if (y < 0) y = 0;
	}

	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(x, y, width, height);
	}
}
