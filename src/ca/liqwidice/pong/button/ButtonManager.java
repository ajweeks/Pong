package ca.liqwidice.pong.button;

import java.awt.Graphics;
import java.util.Vector;

public class ButtonManager {

	private Vector<Button> buttons = new Vector<>();

	public void updateAll() {
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).update();
		}
	}

	public void renderAll(Graphics g) {
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).render(g);
		}
	}

	public void addButton(Button button) {
		buttons.add(button);
	}

	public Button getButton(String s) {
		for (Button b : buttons) {
			if (b.text.equals(s)) return b;
		}
		return null;
	}

}
