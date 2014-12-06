package ca.liqwidice.pong.level;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import ca.liqwidice.pong.Pong;
import ca.liqwidice.pong.sound.Sound;

public class Ball extends Rectangle { //a ball that's really a rectangle
	private static final long serialVersionUID = 1L;

	public static final int DEFAULT_X = 1;
	public static final int DEFAULT_Y = 1;
	public static final int WIDTH = 0;
	
	private double xv, yv;
	private int lastX, lastY;
	private boolean offScreen = false;

	public Ball(int x, int y, double xv, double yv, int width) {
		super(x, y, width, width);
		this.xv = xv;
		this.yv = yv;
	}

	public void update() {
		lastX = x;
		lastY = y;

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

	/** @return a ball at the centre of the sceen with an xv towards player1 if towardsPlayer1 is true */
	public static Ball newBall(Level level, boolean towardsPlayer1) {
		double yv = (Math.random() * Level.BALL_XV) - Level.BALL_XV / 2;
		double xv = Level.BALL_XV;
		if (towardsPlayer1) xv = -xv;
		return new Ball(348, 225, xv, yv, 20);
	}

	public boolean isOffScreen() {
		return offScreen;
	}

	public int getLastX() {
		return lastX;
	}

	public int getLastY() {
		return lastY;
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
