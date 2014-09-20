package ca.liqwidice.pong;

import ca.liqwidice.pong.input.Mouse;

public class PlayerPaddle extends Paddle {
	private static final long serialVersionUID = 1L;

	public PlayerPaddle(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public void update() {
		int midpoint = y + height / 2;
		if (Mouse.isMouseStill()) { //use keyboard input
			if (Pong.keyboard.up.down) y -= Level.PLAYER_PADDLE_SPEED;
			else if (Pong.keyboard.down.down) y += Level.PLAYER_PADDLE_SPEED;
		} else if (Pong.mouse.getY() < midpoint) { //mouse is above paddle's midpoint
			y -= (int) Math.min(Level.PLAYER_PADDLE_SPEED, midpoint - Pong.mouse.getY());
		} else if (Pong.mouse.getY() > midpoint) { //mouse is below paddle's midpoint
			y += (int) Math.min(Level.PLAYER_PADDLE_SPEED, Pong.mouse.getY() - midpoint);
		}

		clamp();
	}
}
