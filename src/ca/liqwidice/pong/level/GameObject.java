package ca.liqwidice.pong.level;

import java.awt.Graphics;

import ca.liqwidice.pong.Pong;
import ca.liqwidice.pong.button.ButtonManager;
import ca.liqwidice.pong.button.ImageButton;
import ca.liqwidice.pong.input.Keyboard.Key;
import ca.liqwidice.pong.input.Mouse;

/** Holds a level object and also has buttons to navigate */
public class GameObject {

	private static final String RESUME = "RESUME";
	private static final String MAIN_MENU = "MAIN MENU";
	private static final String NEW_GAME = "NEW GAME";

	private Pong pong;
	private Level level;
	private ButtonManager manager;

	public GameObject(Pong pong, Level level) {
		//TODO let players select which input method they want to use (using radio buttons?)
		this.pong = pong;
		this.level = level;
		manager = new ButtonManager();

		manager.addButton(new ImageButton(RESUME, Pong.SIZE.width / 2 - 175 / 2, 230, 175, 80));
		manager.addButton(new ImageButton(MAIN_MENU, Pong.SIZE.width / 2 - 175 / 2, 320, 175, 80));

		manager.addButton(new ImageButton(NEW_GAME, Pong.SIZE.width / 2 - 175 / 2, 230, 175, 80));
	}

	public void update() {
		if (Key.ESC.clicked) level.setPaused(!level.isPaused());
		if (Mouse.isStill() && !level.isGameOver() && !level.isPaused()) {
			pong.setCursor(Pong.blankCursor);
		} else pong.setCursor(null);

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

	public Level getLevel() {
		return level;
	}

	public void setDifficulty(float speed) {
		level.setDifficulty(speed);
	}

	public void setPaused(boolean paused) {
		level.setPaused(paused);
	}
}
