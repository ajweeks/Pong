package ca.liqwidice.pong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Ball extends Rectangle {
	private static final long serialVersionUID = 1L;

	private double xv, yv;
	private boolean offScreen = false;
	private Level level;

	public Ball(Level level, int x, int y, double xv, double yv, int width) {
		super(x, y, width, width);
		this.level = level;
		this.xv = xv;
		this.yv = yv;
	}

	public void update() {
		x += xv;
		y += yv;

		clamp();
	}

	public void render(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(x, y, width, height);
	}

	public void clamp() {
		if (x > Pong.SIZE.width || x + width < 0) {
			offScreen = true; //someone scored a point
		}
		if (y > Pong.SIZE.height - height) {
			y = Pong.SIZE.height - height;
			yv = -yv;
			Sound.boop2.play();
		} else if (y < 0) {
			y = 0;
			yv = -yv;
			Sound.boop2.play();
		}
	}

	/** @return a ball at x, y with a randomized xv and yv */
	public static Ball newBall(Level level, boolean towardsPlayer) {
		double yv = (Math.random() * (level.getBallXv()) - level.getBallXv() / 2);
		double xv = level.getBallXv();
		if (towardsPlayer) xv = -xv;
		return new Ball(level, 345, 225, xv, yv, 20);
	}

	public boolean isOffScreen() {
		return offScreen;
	}

	public double getXv() {
		return xv;
	}

	public double getYv() {
		return yv;
	}

	public void setXv(double xv) {
		this.xv = xv;
	}

	public void setYv(double yv) {
		this.yv = yv;
	}
}
