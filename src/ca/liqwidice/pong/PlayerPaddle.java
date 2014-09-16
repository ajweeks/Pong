package ca.liqwidice.pong;

import ca.liqwidice.pong.input.Mouse;

public class PlayerPaddle extends Paddle {
	private static final long serialVersionUID = 1L;

	private Level level;

	public PlayerPaddle(Level level, int x, int y, int width, int height) {
		super(x, y, width, height);
		this.level = level;
	}

	public void update() {
		if (Mouse.isMouseStill()) {
			if (Pong.keyboard.up.down) y -= level.getPlayerPaddleSpeed();
			else if (Pong.keyboard.down.down) y += level.getPlayerPaddleSpeed();
		} else if (Pong.mouse.getY() < y + height / 2) { //mouse is above paddle's midpoint
			y = (int) Math.min(level.getPlayerPaddleSpeed(), y - Pong.mouse.getY());
		} else if (Pong.mouse.getY() > y + height / 2) { //mouse is below paddle's midpoint
			y = (int) Math.min(level.getPlayerPaddleSpeed(), Pong.mouse.getY() - y);
		}

		clamp();
	}
}
