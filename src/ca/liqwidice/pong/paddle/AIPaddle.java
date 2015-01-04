package ca.liqwidice.pong.paddle;

import ca.liqwidice.pong.level.Ball;

public class AIPaddle extends Paddle {
	private static final long serialVersionUID = 1L;

	public static final short EASY_SPEED = 2;
	public static final short MEDIUM_SPEED = 4;
	public static final short HARD_SPEED = 6;

	public AIPaddle(short x, short y, short width, short height, short speed) {
		super(x, y, width, height);
		this.speed = speed;
	}

	public AIPaddle(short x, short speed) {
		this(x, DEFAULT_Y, WIDTH, HEIGHT, speed);
	}

	public void update(Ball ball) {
		int ballMid = ball.y + ball.height / 2;
		int playerMid = y + height / 2;
		int difference = Math.abs(ballMid - playerMid);
		//TODO MAKE MORE DIFFICULT AI FOR MEDIUM AND HARD

		if (ballMid > playerMid) { //ball is below paddle
			moveY((short) Math.min(speed, difference));
		} else if (ballMid < playerMid) {
			moveY((short) -Math.min(speed, difference));
		}

		clamp();
	}
}
