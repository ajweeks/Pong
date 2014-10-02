package ca.liqwidice.pong.state;

import java.awt.Graphics;

import ca.liqwidice.pong.Pong;
import ca.liqwidice.pong.button.ButtonManager;
import ca.liqwidice.pong.button.ImageButton;
import ca.liqwidice.pong.input.Keyboard.Key;
import ca.liqwidice.pong.level.Level;

/** Single player game state */
public class GameState extends BasicState {

	private static final int RESUME = 0;
	private static final int MAIN_MENU = 1;
	private static final int NEW_GAME = 2;

	private Level level;
	private ButtonManager manager;
	private Pong pong;

	public GameState(Pong pong) {
		this.pong = pong;
		level = new Level();
		manager = new ButtonManager();

		manager.addButton(new ImageButton("Resume", Pong.SIZE.width / 2 - 175 / 2, 135, 175, 80));
		manager.addButton(new ImageButton("Main Menu", Pong.SIZE.width / 2 - 175 / 2, 220, 175, 80));

		manager.addButton(new ImageButton("New Game", Pong.SIZE.width / 2 - 175 / 2, 305, 175, 80));
		//TODO ADD DIFFICULTY SWITCHING!
	}

	public void update() {
		if (Key.ESC.clicked) level.setPaused(!level.isPaused());

		manager.updateAll();

		if (level.isGameOver()) {
			if (Key.ESC.clicked) {
				level.resetGame();
				pong.getStateManager().enterState(StateManager.MAIN_MENU_STATE);
			}
			manager.getButton(NEW_GAME).setVisible(true);
			manager.getButton(MAIN_MENU).setVisible(true);
		} else {
			manager.getButton(NEW_GAME).setVisible(false);

			if (level.isPaused()) {
				manager.getButton(MAIN_MENU).setVisible(true);
				manager.getButton(RESUME).setVisible(true);
			} else {
				manager.getButton(MAIN_MENU).setVisible(false);
				manager.getButton(RESUME).setVisible(false);
			}
		}

		if (manager.getButton(MAIN_MENU).isClicked()) {
			pong.getStateManager().enterState(StateManager.MAIN_MENU_STATE);
		} else if (manager.getButton(RESUME).isClicked()) {
			level.setPaused(false);
		} else if (manager.getButton(NEW_GAME).isClicked()) {
			level.resetGame();
		}

		level.update();
	}

	public void render(Graphics g) {
		level.render(g);
		manager.renderAll(g);
	}

	public void setPaused(boolean paused) {
		level.setPaused(paused);
	}

	public int getID() {
		return StateManager.GAME_STATE;
	}
}
