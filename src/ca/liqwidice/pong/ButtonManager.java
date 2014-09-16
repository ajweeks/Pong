package ca.liqwidice.pong;

import java.awt.Graphics;
import java.util.ArrayList;

public class ButtonManager {

	private ArrayList<Button> buttons = new ArrayList<>();

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

	public Button getButton(int index) {
		return buttons.get(index);
	}

}
