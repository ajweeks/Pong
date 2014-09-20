package ca.liqwidice.pong.state;

import java.awt.Color;
import java.awt.Graphics;

import ca.liqwidice.pong.Button;
import ca.liqwidice.pong.ButtonManager;
import ca.liqwidice.pong.Pong;
import ca.liqwidice.pong.Sound;

public class MainMenuState extends BasicState {

	private static final int SINGLEPLAYER = 0;
	private static final int MULTIPLAYER = 1;
	private static final int QUIT = 2;
	private static final int VOLUME_UP = 3;
	private static final int VOUME_DOWN = 4;

	private ButtonManager manager = new ButtonManager();
	private Pong pong;

	public MainMenuState(Pong pong) {
		this.pong = pong;

		manager.addButton(new Button("Single Player", Pong.SIZE.width / 2 - 250 / 2, 0 + 120, 250, 85));
		manager.addButton(new Button("Multi Player", Pong.SIZE.width / 2 - 230 / 2, 90 + 120, 230, 85));

		manager.addButton(new Button("QUIT", Pong.SIZE.width / 2 - 100 / 2, 180 + 120, 100, 85));

		manager.addButton(new Button(" + ", Pong.SIZE.width - 115, 10, 50, 50));
		manager.addButton(new Button(" - ", Pong.SIZE.width - 60, 10, 50, 50));
	}

	public void update() {
		manager.updateAll();
		if (manager.getButton(SINGLEPLAYER).isClicked()) {
			pong.getStateManager().enterState(StateManager.GAME_STATE);
			((GameState) pong.getStateManager().getCurrentState()).setPaused(false);
		}
		if (manager.getButton(MULTIPLAYER).isClicked()) {
			pong.getStateManager().enterState(StateManager.SERVER_BROWSER_STATE);
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
		manager.renderAll(g);

		g.setColor(Color.WHITE);
		g.setFont(Pong.font16);
		g.drawString("volume: " + Sound.getVolume(), Pong.SIZE.width - 115, 75);
	}

	public int getID() {
		return StateManager.MAIN_MENU_STATE;
	}

}
