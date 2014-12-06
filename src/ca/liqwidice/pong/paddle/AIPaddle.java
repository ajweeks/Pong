package ca.liqwidice.pong.paddle;

import ca.liqwidice.pong.level.Ball;

public class AIPaddle extends Paddle {
	private static final long serialVersionUID = 1L;

	public static final float EASY_SPEED = 4.0f;
	public static final float MEDIUM_SPEED = 5.0f;
	public static final float HARD_SPEED = 6.2f;

	public AIPaddle(int x, int y, int width, int height, float speed) {
		super(x, y, width, height);
		this.speed = speed;
	}

	public void update(Ball ball) {
		int ballMid = ball.y + ball.height / 2;
		int playerMid = y + height / 2;
		int difference = Math.abs(ballMid - playerMid);
		//TODO MAKE MORE DIFFICULT AI FOR MEDIUM AND HARD

		if (ballMid > playerMid) { //ball is below paddle
			move((int) Math.min(speed, difference));
		} else if (ballMid < playerMid) {
			move(-(int) Math.min(speed, difference));
		}

		clamp();
	}
}
