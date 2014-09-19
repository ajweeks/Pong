package ca.liqwidice.pong;

public class AIPaddle extends Paddle {
	private static final long serialVersionUID = 1L;

	Level level;

	public AIPaddle(Level level, int x, int y, int width, int height) {
		super(x, y, width, height);
		this.level = level;
	}

	public void update(Ball ball) {
		int ballMid = ball.y + ball.height / 2;
		int playerMid = y + height / 2;
		int difference = Math.abs(ballMid - playerMid);

		if (ballMid > playerMid) { //ball is below paddle
			if (Level.AI_PADDLE_SPEED < difference) {
				y += Level.AI_PADDLE_SPEED;
			} else {
				y += difference;
			}
		} else if (ballMid < playerMid) {
			if (Level.AI_PADDLE_SPEED < difference) {
				y -= Level.AI_PADDLE_SPEED;
			} else {
				y -= difference;
			}
		}

		clamp();
	}

}
