package ca.liqwidice.pong.input;

import java.awt.Canvas;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseMotionListener, MouseListener {

	private int x, y;
	private boolean leftDown, rightDown, leftClicked, rightClicked;
	private static boolean still = true;

	public Mouse(Canvas canvas) {
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
	}

	public void update() {
		leftClicked = false;
		rightClicked = false;
	}

	public void releaseAll() {
		leftDown = false;
		rightDown = false;
		leftClicked = false;
		rightClicked = false;
	}

	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			leftClicked = true;
			leftDown = true;
		}
		if (e.getButton() == MouseEvent.BUTTON3) {
			rightClicked = false;
			rightDown = true;
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			leftClicked = false;
			leftDown = false;
		}
		if (e.getButton() == MouseEvent.BUTTON3) {
			rightClicked = false;
			rightDown = false;
		}
	}

	public void mouseMoved(MouseEvent e) {
		still = false;
		x = e.getX();
		y = e.getY();
	}

	public void mouseDragged(MouseEvent e) {
		still = false;
		x = e.getX();
		y = e.getY();
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (!leftDown) leftClicked = true;
			else leftClicked = false;
			leftDown = false;
		}
		if (e.getButton() == MouseEvent.BUTTON3) {
			if (!rightDown) rightClicked = true;
			else rightClicked = false;
			rightDown = false;
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isLeftClicked() {
		return leftClicked;
	}

	public boolean isRightClicked() {
		return rightClicked;
	}

	public boolean isLeftDown() {
		return leftDown;
	}

	public boolean isRightDown() {
		return rightDown;
	}

	public static void setStill(boolean still) {
		Mouse.still = still;
	}

	public static boolean isStill() {
		return Mouse.still;
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mouseClicked(MouseEvent e) {}
}
