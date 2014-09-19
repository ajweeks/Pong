package ca.liqwidice.pong;

import ca.liqwidice.pong.input.Mouse;

public class PlayerPaddle extends Paddle {
	private static final long serialVersionUID = 1L;

	public PlayerPaddle(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public void update() {
		if (Mouse.isMouseStill()) {
			if (Pong.keyboard.up.down) y -= Level.PLAYER_PADDLE_SPEED;
			else if (Pong.keyboard.down.down) y += Level.PLAYER_PADDLE_SPEED;
		} else if (Pong.mouse.getY() < y + height / 2) { //mouse is above paddle's midpoint
			y = (int) Math.min(y - Level.PLAYER_PADDLE_SPEED, y - Pong.mouse.getY());
		} else if (Pong.mouse.getY() > y + height / 2) { //mouse is below paddle's midpoint
			y = (int) Math.min(y + Level.PLAYER_PADDLE_SPEED, Pong.mouse.getY() - y);
		}

		clamp();
	}
}
