package ca.liqwidice.pong.input;

import java.awt.Canvas;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseMotionListener, MouseListener {

	private int x, y;
	private boolean leftDown, rightDown;
	private static boolean mouseStill = true;

	public Mouse(Canvas canvas) {
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
	}

	public void releaseAll() {
		leftDown = false;
		rightDown = false;
	}

	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) leftDown = true;
		if (e.getButton() == MouseEvent.BUTTON3) rightDown = true;
	}

	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) leftDown = false;
		if (e.getButton() == MouseEvent.BUTTON3) rightDown = false;
	}

	public void mouseMoved(MouseEvent e) {
		mouseStill = false;
		x = e.getX();
		y = e.getY();
	}

	public void mouseDragged(MouseEvent e) {
		mouseStill = false;
		x = e.getX();
		y = e.getY();
		if (e.getButton() == MouseEvent.BUTTON1) leftDown = false;
		if (e.getButton() == MouseEvent.BUTTON3) rightDown = false;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isLeftDown() {
		return leftDown;
	}

	public boolean isRightDown() {
		return rightDown;
	}

	public static void setMouseStill(boolean mouseStill) {
		Mouse.mouseStill = mouseStill;
	}

	public static boolean isMouseStill() {
		return Mouse.mouseStill;
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mouseClicked(MouseEvent e) {}
}
