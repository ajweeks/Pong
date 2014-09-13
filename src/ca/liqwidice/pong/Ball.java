package ca.liqwidice.pong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Ellipse2D;

public class Ball extends Ellipse2D.Double {
	private static final long serialVersionUID = 1L;

	private double xv, yv;
	private boolean offScreen = false;

	public Ball(double x, double y, double xv, double yv, double diameter) {
		super(x, y, diameter, diameter);
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
		g.fillOval((int) x, (int) y, (int) width, (int) height);
	}

	public void clamp() {
		if (x > Pong.SIZE.width || x + width < 0) {
			offScreen = true; //someone scored a point
		}
		if (y > Pong.SIZE.height - height) {
			y = Pong.SIZE.height - height;
			yv = -yv;
		} else if (y < 0) {
			y = 0;
			yv = -yv;
		}
	}

	/** @return a ball at x, y with a randomized xv and yv */
	public static Ball newBall(double x, double y, double diameter) {
		return new Ball(x, y, Level.BALL_SPEED, Math.random() * (Level.BALL_SPEED - 1) + 1, diameter);
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
