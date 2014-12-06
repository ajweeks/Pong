package ca.liqwidice.pong.paddle;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import ca.liqwidice.pong.Pong;
import ca.liqwidice.pong.level.Ball;

public abstract class Paddle extends Rectangle {
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 25;
	public static final int HEIGHT = 100;
	public static final int DEFAULT_X_1 = 50;
	public static final int DEFAULT_X_2 = Pong.SIZE.width - 75;
	public static final int DEFAULT_Y = Pong.SIZE.height / 2 - HEIGHT / 2;

	protected float speed;

	public Paddle(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	/** attempts to move this paddle the specified amount up if negative and down if positive */
	public void move(int dist) {
		this.y += dist;
	}

	public abstract void update(Ball ball);

	public void render(Graphics g) { //abstract
		g.setColor(Color.WHITE);
		g.fillRect(x, y, width, height);
	}

	protected void clamp() {
		if (y > Pong.SIZE.height - height) y = Pong.SIZE.height - height;
		else if (y < 0) y = 0;
	}

	public void reset() {
		this.y = DEFAULT_Y;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
}
