package ca.liqwidice.pong;

public class AIPaddle extends Paddle {
	private static final long serialVersionUID = 1L;

	public AIPaddle(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public void update(Ball ball) {
		//		if (ball.y + ball.height / 2 > y + height / 2) { //ball is below paddle
		y = (int) Math.min(Level.AI_PADDLE_SPEED, Math.abs((ball.y + ball.height / 2) - y + height / 2));
		//		} else if (ball.y + ball.height / 2 < y + height / 2) { //ball is above paddle
		//			y = (int) Math.min(y + height / 2 - Level.AI_PADDLE_SPEED, (ball.y + ball.height / 2) - y + height / 2);
		//		}
		clamp();
	}

}
