package ca.liqwidice.pong.level;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

import ca.liqwidice.pong.Pong;
import ca.liqwidice.pong.sound.Sound;

public class Ball extends Rectangle { //a ball that's really a rectangle
	private static final long serialVersionUID = 1L;

	public static final int DEFAULT_X = 348;
	public static final int DEFAULT_Y = 225;
	public static final int WIDTH = 16;
	public static final double DEFAULT_XV = 4.5f;

	private double xv, yv;
	private boolean offScreen = false;
	public static Random random;

	static {
		random = new Random();
	}

	public Ball(int x, int y, double xv, double yv, int width, byte seed) {
		super(x, y, width, width);
		this.xv = xv;
		this.yv = yv;
		if (seed != -1) {
			random = new Random(seed);
			this.yv = random.nextDouble();
		}
	}

	public void update() {
		x += xv;
		y += yv;

		clamp();
	}

	public void render(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(x, y, width, height);
		if (Pong.renderDebug) {
			g.setFont(Pong.font16);
			g.drawString("Ball:", 80, 35);
			g.drawString("XV:" + xv, 80, 50);
			g.drawString("YV:" + yv, 80, 65);
			g.drawString("X: " + x, 80, 80);
			g.drawString("Y: " + y, 80, 95);
		}
	}

	public void clamp() {
		if (x + width > Pong.SIZE.width || x < 0) {
			offScreen = true; //someone scored a point
		} else {
			offScreen = false;
		}
		if (y >= Pong.SIZE.height - height) {
			y = Pong.SIZE.height - height;
			yv = -yv;
			Sound.boop2.play();
		} else if (y < 0) {
			y = 0;
			yv = -yv;
			Sound.boop2.play();
		}
	}

	//Should only be called at the start of every game, not after it has gone off the screen
	/** @return a ball at the center of the screen with a random yv, and an xv towards player1 (left side of screen) if towardsPlayer1 is true */
	public static Ball newBall(boolean towardsPlayer1, byte seed) {
		double yv = (random.nextDouble() * DEFAULT_XV) - DEFAULT_XV / 2; //yv is anywhere between -xv/2 and +xv/2
		double xv = towardsPlayer1 ? DEFAULT_XV : -DEFAULT_XV;
		return new Ball(DEFAULT_X, DEFAULT_Y, xv, yv, WIDTH, seed);
	}

	//Should only be called at the start of every game, not after it has gone off the screen
	/** @return a ball at the center of the screen with a random yv, and an xv towards player1 (left side of screen) if towardsPlayer1 is true */
	public static Ball newBall(boolean towardsPlayer1) {
		return Ball.newBall(towardsPlayer1, (byte) -1);
	}

	/** Moves ball back to center of screen with an xv towards player1 if true (left) */
	public void reset(boolean towardsPlayer1) {
		this.yv = (random.nextDouble() * DEFAULT_XV) - DEFAULT_XV / 2; //yv is anywhere between -xv/2 and +xv/2
		this.xv = towardsPlayer1 ? -DEFAULT_XV : DEFAULT_XV;
		this.x = DEFAULT_X;
		this.y = DEFAULT_Y;
	}

	public boolean isOffScreen() {
		return offScreen;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
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
