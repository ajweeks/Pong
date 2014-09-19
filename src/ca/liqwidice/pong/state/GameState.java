package ca.liqwidice.pong.state;

import java.awt.Graphics;

import ca.liqwidice.pong.Button;
import ca.liqwidice.pong.ButtonManager;
import ca.liqwidice.pong.Level;
import ca.liqwidice.pong.Pong;
import ca.liqwidice.pong.state.BasicState;

public class GameState extends BasicState {

	private static final int MAIN_MENU = 0;
	private static final int QUIT = 1;

	private Level level;
	private ButtonManager manager;
	private Pong pong;

	public GameState(Pong pong) {
		this.pong = pong;
		level = new Level();
		manager = new ButtonManager();

		manager.addButton(new Button("Main Menu", 253, 138, 175, 80));
		manager.addButton(new Button("Quit", 270, 235, 145, 80));
	}

	public void update() {
		manager.updateAll();

		if (Pong.keyboard.esc.clicked) level.setPaused(!level.isPaused());

		if (level.isPaused()) {
			manager.getButton(MAIN_MENU).setVisible(true);
			manager.getButton(QUIT).setVisible(true);
			if (manager.getButton(MAIN_MENU).isClicked()) {
				pong.getStateManager().enterState(StateManager.MAIN_MENU_STATE);
			} else if (manager.getButton(QUIT).isClicked()) {
				pong.stop();
			}
		} else {
			manager.getButton(MAIN_MENU).setVisible(false);
			manager.getButton(QUIT).setVisible(false);
		}

		level.update();
		Pong.keyboard.update();
	}

	public void render(Graphics g) {
		level.render(g);
		manager.renderAll(g);
	}
}
