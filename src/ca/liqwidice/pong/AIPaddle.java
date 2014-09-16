package ca.liqwidice.pong;

public class AIPaddle extends Paddle {
	private static final long serialVersionUID = 1L;

	Level level;

	public AIPaddle(Level level, int x, int y, int width, int height) {
		super(x, y, width, height);
		this.level = level;
	}

	public void update(Ball ball) {
		//		if (ball.y + ball.height / 2 > y + height / 2) { //ball is below paddle
		y = (int) Math.min(level.getAiPaddleSpeed(), Math.abs((ball.y + ball.height / 2) - y + height / 2));
		//		} else if (ball.y + ball.height / 2 < y + height / 2) { //ball is above paddle
		//			y = (int) Math.min(y + height / 2 - Level.AI_PADDLE_SPEED, (ball.y + ball.height / 2) - y + height / 2);
		//		}
		clamp();
	}

}
