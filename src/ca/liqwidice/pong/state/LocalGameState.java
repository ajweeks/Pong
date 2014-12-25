package ca.liqwidice.pong.state;

import java.awt.Graphics;

import ca.liqwidice.pong.Pong;
import ca.liqwidice.pong.level.GameObject;
import ca.liqwidice.pong.level.Level;

public class LocalGameState extends BasicState {

	private GameObject game;

	public LocalGameState(Pong pong, Level level) {
		game = new GameObject(pong, level);
	}

	@Override
	public void update() {
		game.update();
	}

	@Override
	public void render(Graphics g) {
		game.render(g);
	}
	
	public GameObject getGameObject() {
		return game;
	}
}
