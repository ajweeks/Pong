package ca.liqwidice.pong.paddle;

import ca.liqwidice.pong.Pong;
import ca.liqwidice.pong.input.Keyboard.Key;
import ca.liqwidice.pong.input.Mouse;
import ca.liqwidice.pong.level.Ball;

public class PlayerPaddle extends Paddle {
	private static final long serialVersionUID = 1L;

	public static final short SPEED = 6;

	private boolean usesMouse, usesWASD, usesArrows;

	public PlayerPaddle(short x, short y, short width, short height, boolean usesMouse, boolean usesWASD,
			boolean usesArrows) {
		super(x, y, width, height);
		this.usesMouse = usesMouse;
		this.usesWASD = usesWASD;
		this.usesArrows = usesArrows;
		this.speed = SPEED;
	}

	public PlayerPaddle(short x, boolean usesMouse, boolean usesWASD, boolean usesArrows) {
		this(x, DEFAULT_Y, WIDTH, HEIGHT, usesMouse, usesWASD, usesArrows);
	}

	@Override
	public void update(Ball ball) {
		if (usesMouse && !Mouse.isStill()) useMouse(); //Mouse has priority over keyboard
		else {
			if (usesWASD) {
				if (!useWASD()) if (usesArrows) useArrows(); //if we use both wasd and arrows but arrows wern't touched
			} else if (usesArrows) useArrows();
		}

		clamp();
	}

	/** Applies WASD keys to player pos if any were pressed
	 *  Returns True if any WASD keys have been pressed (currently A and D are not used) */
	private boolean useWASD() {
		if (Key.W.down != -1) {
			y -= SPEED;
			return true;
		} else if (Key.S.down != -1) {
			y += SPEED;
			return true;
		} else return false;
	}

	/** Applies Arrow keys to paddle pos if any were pressed
	 *  Returns True if any Arrow keys have been pressed (currently LEFT and RIGHT are not used)  */
	private boolean useArrows() {
		if (Key.UP.down != -1) {
			y -= SPEED;
			return true;
		} else if (Key.DOWN.down != -1) {
			y += SPEED;
			return true;
		} else return false;
	}
	
	/** Moves paddle towards mouse
	 *  Returns True if paddle was moved towards mouse (False if they were already at same pos) */
	private void useMouse() {
		short midpoint = (short) (y + height / 2);
		short dist = (short) Math.min(SPEED, Math.abs(Pong.mouse.getY() - midpoint));

		if (Pong.mouse.getY() < midpoint) { //mouse is above paddle's midpoint
			moveY((short) -dist); //raise paddle
		} else if (Pong.mouse.getY() > midpoint) { //mouse is below paddle's midpoint
			moveY(dist); //lower paddle
		}
	}
}
