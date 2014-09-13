package ca.liqwidice.pong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class PlayerPaddle extends Rectangle {
	private static final long serialVersionUID = 1L;

	public PlayerPaddle(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public void update() {
		if (Pong.input.up.down) y -= Level.PADDLE_SPEED;
		else if (Pong.input.down.down) y += Level.PADDLE_SPEED;

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
