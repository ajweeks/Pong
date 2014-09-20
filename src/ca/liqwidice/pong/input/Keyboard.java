package ca.liqwidice.pong.input;

import java.awt.Canvas;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import ca.liqwidice.pong.input.Mouse;

public class Keyboard implements KeyListener {

	public class Key {
		public boolean clicked, down;

		public Key() {
			keys.add(this);
		}

		public void update() {
			if (clicked) clicked = false;
		}

		public void update(boolean pressed) {
			if (!down && pressed) clicked = true;
			else clicked = false;
			down = pressed;
		}
	}

	private ArrayList<Key> keys = new ArrayList<>();

	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	public Key esc = new Key();

	public void update() {
		for (int i = 0; i < keys.size(); i++) {
			keys.get(i).update();
		}
	}

	public Keyboard(Canvas canvas) {
		canvas.addKeyListener(this);
	}

	public void releaseAll() {
		for (int i = 0; i < keys.size(); i++) {
			keys.get(i).clicked = false;
			keys.get(i).down = false;
		}
	}

	private void keyUpdated(KeyEvent e, boolean pressed) {
		Mouse.setMouseStill(true);

		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
		case KeyEvent.VK_UP:
			up.update(pressed);
			break;
		case KeyEvent.VK_A:
		case KeyEvent.VK_LEFT:
			left.update(pressed);
			break;
		case KeyEvent.VK_S:
		case KeyEvent.VK_DOWN:
			down.update(pressed);
			break;
		case KeyEvent.VK_D:
		case KeyEvent.VK_RIGHT:
			right.update(pressed);
			break;
		case KeyEvent.VK_ESCAPE:
			esc.update(pressed);
			break;
		}
	}

	public void keyPressed(KeyEvent e) {
		keyUpdated(e, true);
	}

	public void keyReleased(KeyEvent e) {
		keyUpdated(e, false);
	}

	public void keyTyped(KeyEvent e) {}

}
