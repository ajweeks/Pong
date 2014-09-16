package ca.liqwidice.pong.state;

import java.awt.Graphics;

import ca.liqwidice.pong.Button;
import ca.liqwidice.pong.ButtonManager;
import ca.liqwidice.pong.Colour;
import ca.liqwidice.pong.Pong;

public class MainMenuState extends BasicState {

	private static final int PLAY = 0;
	private static final int QUIT = 1;

	private ButtonManager manager = new ButtonManager();
	private Pong pong;

	public MainMenuState(Pong pong) {
		this.pong = pong;

		manager.addButton(new Button("PLAY", Pong.SIZE.width / 2 - 150 / 2, 0 + 145, 150, 85));
		manager.addButton(new Button("QUIT", Pong.SIZE.width / 2 - 150 / 2, 95 + 145, 150, 85));
	}

	public void update() {
		manager.updateAll();
		if (manager.getButton(PLAY).isClicked()) {
			pong.getStateManager().enterState(StateManager.GAME_STATE);
		} else if (manager.getButton(QUIT).isClicked()) {
			pong.stop();
		}
	}

	public void render(Graphics g) {
		g.setColor(Colour.offBlack);
		g.fillRect(0, 0, Pong.SIZE.width, Pong.SIZE.height);
		manager.renderAll(g);
	}

}
