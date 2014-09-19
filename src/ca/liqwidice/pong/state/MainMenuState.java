package ca.liqwidice.pong.state;

import java.awt.Graphics;

import ca.liqwidice.pong.Button;
import ca.liqwidice.pong.ButtonManager;
import ca.liqwidice.pong.Colour;
import ca.liqwidice.pong.Pong;
import ca.liqwidice.pong.Sound;

public class MainMenuState extends BasicState {

	private static final int PLAY = 0;
	private static final int QUIT = 1;
	private static final int VOLUME_UP = 2;
	private static final int VOUME_DOWN = 3;

	private ButtonManager manager = new ButtonManager();
	private Pong pong;

	public MainMenuState(Pong pong) {
		this.pong = pong;

		manager.addButton(new Button("PLAY", Pong.SIZE.width / 2 - 150 / 2, 0 + 145, 150, 85));

		manager.addButton(new Button("QUIT", Pong.SIZE.width / 2 - 150 / 2, 95 + 145, 150, 85));

		manager.addButton(new Button(" + ", Pong.SIZE.width - 150, 25, 50, 50));
		manager.addButton(new Button(" - ", Pong.SIZE.width - 50, 25, 50, 50));
	}

	public void update() {
		manager.updateAll();
		if (manager.getButton(PLAY).isClicked()) {
			pong.getStateManager().enterState(StateManager.GAME_STATE);
		}
		if (manager.getButton(QUIT).isClicked()) {
			pong.stop();
		}
		if (manager.getButton(VOLUME_UP).isClicked()) {
			Sound.increaseVolume();
		} else if (manager.getButton(VOUME_DOWN).isClicked()) {
			Sound.decreaseVolume();
		}
	}

	public void render(Graphics g) {
		g.setColor(Colour.offBlack);
		g.fillRect(0, 0, Pong.SIZE.width, Pong.SIZE.height);
		manager.renderAll(g);
	}

}
