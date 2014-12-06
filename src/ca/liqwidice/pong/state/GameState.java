package ca.liqwidice.pong.state;

import java.awt.Graphics;

import ca.liqwidice.pong.Pong;
import ca.liqwidice.pong.button.ButtonManager;
import ca.liqwidice.pong.button.ImageButton;
import ca.liqwidice.pong.input.Keyboard.Key;
import ca.liqwidice.pong.input.Mouse;
import ca.liqwidice.pong.level.Level;

/** Single player game state */
public class GameState extends BasicState {

	private static final String RESUME = "RESUME";
	private static final String MAIN_MENU = "MAIN MENU";
	private static final String NEW_GAME = "NEW GAME";

	private Level level;
	private ButtonManager manager;
	private Pong pong;

	public GameState(Pong pong, Level level) {
		this.pong = pong;
		this.level = level;
		manager = new ButtonManager();

		manager.addButton(new ImageButton(RESUME, Pong.SIZE.width / 2 - 175 / 2, 135, 175, 80));
		manager.addButton(new ImageButton(MAIN_MENU, Pong.SIZE.width / 2 - 175 / 2, 220, 175, 80));

		manager.addButton(new ImageButton(NEW_GAME, Pong.SIZE.width / 2 - 175 / 2, 305, 175, 80));
	}

	public void update() {
		if (Key.ESC.clicked) level.setPaused(!level.isPaused());
		if (Mouse.isStill() && !level.isGameOver() && !level.isPaused()) pong.setCursor(Pong.blankCursor);
		else pong.setCursor(null);

		manager.updateAll();

		if (level.isGameOver()) {
			if (Key.ESC.clicked) {
				level.resetGame();
				pong.getStateManager().enterPreviousState();
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
			pong.getStateManager().enterPreviousState();
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

	public void setDifficulty(float speed) {
		level.setDifficulty(speed);
	}

	public void setPaused(boolean paused) {
		level.setPaused(paused);
	}
}
