package ca.liqwidice.pong.state;

import java.awt.Graphics;

import ca.liqwidice.pong.Button;
import ca.liqwidice.pong.ButtonManager;
import ca.liqwidice.pong.Pong;
import ca.liqwidice.pong.TextBox;
import ca.liqwidice.pong.input.Keyboard.Key;

public class ServerBrowserState extends BasicState {

	public static int MAIN_MENU = 0;

	private ButtonManager manager;
	private Pong pong;
	private TextBox tb1 = new TextBox(20, 300, 180, 26);
	private TextBox tb2 = new TextBox(20, 250, 375, 26);

	public ServerBrowserState(Pong pong) {
		this.pong = pong;
		manager = new ButtonManager();
		manager.addButton(new Button("Main Menu", 15, 15, 215, 120));
	}

	public void update() {
		manager.updateAll();

		if (manager.getButton(MAIN_MENU).isClicked() || Key.ESC.clicked) {
			pong.getStateManager().enterState(StateManager.MAIN_MENU_STATE);
		}

		tb1.update();
		tb2.update();
	}

	public void render(Graphics g) {
		manager.renderAll(g);

		tb1.render(g);
		tb2.render(g);
	}

	public int getID() {
		return StateManager.SERVER_BROWSER_STATE;
	}
}
