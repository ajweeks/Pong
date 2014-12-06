package ca.liqwidice.pong.paddle;

import ca.liqwidice.pong.Pong;
import ca.liqwidice.pong.input.Keyboard.Key;
import ca.liqwidice.pong.input.Mouse;
import ca.liqwidice.pong.level.Ball;

public class PlayerPaddle extends Paddle {
	private static final long serialVersionUID = 1L;

	public static final float SPEED = 8.0f;

	private boolean usesMouse, usesKeyboard;

	public PlayerPaddle(int x, int y, int width, int height, boolean usesMouse, boolean usesKeyboard) {
		super(x, y, width, height);
		this.usesMouse = usesMouse;
		this.usesKeyboard = usesKeyboard;
		this.speed = SPEED;
	}

	@Override
	public void update(Ball ball) {
		if (usesMouse && !Mouse.isStill()) useMouse(); //Mouse has priority over keyboard
		else if (usesKeyboard) useKeyboard();

		clamp();
	}

	private void useKeyboard() {
		if (Key.UP.down != -1 || Key.W.down != -1) y -= SPEED; //up or w is being pressed
		else if (Key.DOWN.down != -1 || Key.S.down != -1) y += SPEED; //down or s is being pressed
	}

	private void useMouse() {
		int midpoint = y + height / 2;
		int dist = (int) Math.min(SPEED, Math.abs(Pong.mouse.getY() - midpoint));

		if (Pong.mouse.getY() < midpoint) { //mouse is above paddle's midpoint
			move(-dist); //raise paddle
		} else if (Pong.mouse.getY() > midpoint) { //mouse is below paddle's midpoint
			move(dist); //lower paddle
		}
	}
}
