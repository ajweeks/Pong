package ca.liqwidice.pong.paddle;

import ca.liqwidice.pong.Pong;
import ca.liqwidice.pong.input.Keyboard.Key;
import ca.liqwidice.pong.input.Mouse;

public class PlayerPaddle extends Paddle {
	private static final long serialVersionUID = 1L;

	public static final float SPEED = 8.0f;

	public PlayerPaddle(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public void update() {
		int midpoint = y + height / 2;
		if (Mouse.isStill()) { //use keyboard input
			if (Key.UP.down != -1 || Key.W.down != -1) y -= SPEED;
			else if (Key.DOWN.down != -1 || Key.S.down != -1) y += SPEED;
		} else if (Pong.mouse.getY() < midpoint) { //mouse is above paddle's midpoint
			y -= (int) Math.min(SPEED, midpoint - Pong.mouse.getY());
		} else if (Pong.mouse.getY() > midpoint) { //mouse is below paddle's midpoint
			y += (int) Math.min(SPEED, Pong.mouse.getY() - midpoint);
		}

		clamp();
	}
}
