package ca.liqwidice.pong.state;

import java.awt.Graphics;

import ca.liqwidice.pong.Button;
import ca.liqwidice.pong.ButtonManager;
import ca.liqwidice.pong.Pong;

public class ServerBrowserState extends BasicState {

	public static int MAIN_MENU = 0;

	private ButtonManager manager;
	private Pong pong;

	public ServerBrowserState(Pong pong) {
		this.pong = pong;
		manager = new ButtonManager();
		manager.addButton(new Button("Main Menu", 15, 15, 215, 120));
	}

	public void update() {
		manager.updateAll();

		if (manager.getButton(MAIN_MENU).isClicked()) {
			pong.getStateManager().enterState(StateManager.MAIN_MENU_STATE);
		}

	}

	public void render(Graphics g) {
		manager.renderAll(g);
	}

	public int getID() {
		return StateManager.SERVER_BROWSER_STATE;
	}

}
