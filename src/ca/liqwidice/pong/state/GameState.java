package ca.liqwidice.pong.state;

import java.awt.Graphics;

import ca.liqwidice.pong.Level;
import ca.liqwidice.pong.Pong;

public class GameState extends BasicState {

	private Level level;

	public GameState(float difficulty) {
		level = new Level(difficulty);
	}

	public void update() {
		if (Pong.keyboard.esc.clicked) level.setPaused(!level.isPaused());
		level.update();
		Pong.keyboard.update();
	}

	public void render(Graphics g) {
		level.render(g);
	}

}
