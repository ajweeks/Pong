package ca.liqwidice.pong.state;

import java.awt.Color;
import java.awt.Graphics;

import ca.liqwidice.pong.Pong;
import ca.liqwidice.pong.button.ButtonManager;
import ca.liqwidice.pong.button.ImageButton;
import ca.liqwidice.pong.level.Level;
import ca.liqwidice.pong.paddle.AIPaddle;
import ca.liqwidice.pong.sound.Sound;

public class MainMenuState extends BasicState {

	private static final String VSAI = "VS AI";
	private static final String EASY = "E";
	private static final String MEDIUM = "M";
	private static final String HARD = "H";
	private static final String VSHUMAN = "VS HUMAN";
	private static final String MULTIPLAYER = "MULTIPLAYER";
	private static final String QUIT = "QUIT";
	private static final String VOLUME_UP = " + ";
	private static final String VOUME_DOWN = " - ";

	private Pong pong;
	private ButtonManager manager = new ButtonManager();

	private boolean showingSP = true; //whether or not the big single player button is being shown rn (if false, the small buttons are being shown)

	public MainMenuState(Pong pong) {
		this.pong = pong;

		int buttonWidth = 250;
		manager.addButton(new ImageButton(VSAI, Pong.SIZE.width / 2 - buttonWidth / 2, 60, buttonWidth, 85));

		manager.addButton(new ImageButton(EASY, Pong.SIZE.width / 2 - buttonWidth / 2, 60, 74, 85)); //74x85 with a 14px gap
		manager.addButton(new ImageButton(MEDIUM, Pong.SIZE.width / 2 - 74 / 2, 60, 74, 85));
		manager.addButton(new ImageButton(HARD, Pong.SIZE.width / 2 + buttonWidth / 2 - 74, 60, 74, 85));
		manager.getButton(EASY).setVisible(false);
		manager.getButton(MEDIUM).setVisible(false);
		manager.getButton(HARD).setVisible(false);

		manager.addButton(new ImageButton(VSHUMAN, Pong.SIZE.width / 2 - buttonWidth / 2, 150, buttonWidth, 85));

		manager.addButton(new ImageButton(MULTIPLAYER, Pong.SIZE.width / 2 - buttonWidth / 2, 240, buttonWidth, 85));

		manager.addButton(new ImageButton(QUIT, Pong.SIZE.width / 2 - buttonWidth / 2, 330, buttonWidth, 85));

		manager.addButton(new ImageButton(VOLUME_UP, Pong.SIZE.width - 115, 10, 50, 50, ImageButton.SMALL_BTN,
				ImageButton.SMALL_BTN_HOV));
		manager.addButton(new ImageButton(VOUME_DOWN, Pong.SIZE.width - 60, 10, 50, 50, ImageButton.SMALL_BTN,
				ImageButton.SMALL_BTN_HOV));
	}

	public void update() {
		manager.updateAll();

		if (manager.getButton(VSAI).isClicked()) {
			showingSP = false;
		} else if (!showingSP) {
			if (manager.getButton(EASY).isClicked()) {
				pong.getStateManager().addState(new GameState(pong, Level.getDefaultPVAILevel()));
				((GameState) pong.getStateManager().getCurrentState()).setDifficulty(AIPaddle.EASY_SPEED);
				((GameState) pong.getStateManager().getCurrentState()).setPaused(false);
				showingSP = true;
			} else if (manager.getButton(MEDIUM).isClicked()) {
				pong.getStateManager().addState(new GameState(pong, Level.getDefaultPVAILevel()));
				((GameState) pong.getStateManager().getCurrentState()).setDifficulty(AIPaddle.MEDIUM_SPEED);
				((GameState) pong.getStateManager().getCurrentState()).setPaused(false);
				showingSP = true;
			} else if (manager.getButton(HARD).isClicked()) {
				pong.getStateManager().addState(new GameState(pong, Level.getDefaultPVAILevel()));
				((GameState) pong.getStateManager().getCurrentState()).setDifficulty(AIPaddle.HARD_SPEED);
				((GameState) pong.getStateManager().getCurrentState()).setPaused(false);
				showingSP = true;
			}
		}
		if (manager.getButton(VSHUMAN).isClicked()) {
			pong.getStateManager().addState(new GameState(pong, Level.getDefaultPVPLevel()));
			((GameState) pong.getStateManager().getCurrentState()).setPaused(false);
			showingSP = true;
		} else if (manager.getButton(MULTIPLAYER).isClicked()) {
			pong.getStateManager().addState(new ServerBrowserState(pong));
			showingSP = true;
		} else if (manager.getButton(QUIT).isClicked()) {
			pong.stop();
		} else if (manager.getButton(VOLUME_UP).isClicked()) {
			Sound.increaseVolume();
			Sound.boop.play();
		} else if (manager.getButton(VOUME_DOWN).isClicked()) {
			Sound.decreaseVolume();
			Sound.boop.play();
		}

		if (showingSP) {
			manager.getButton(VSAI).setVisible(true);
			manager.getButton(EASY).setVisible(false);
			manager.getButton(MEDIUM).setVisible(false);
			manager.getButton(HARD).setVisible(false);
		} else {
			manager.getButton(VSAI).setVisible(false);
			manager.getButton(EASY).setVisible(true);
			manager.getButton(MEDIUM).setVisible(true);
			manager.getButton(HARD).setVisible(true);
		}
	}

	public void render(Graphics g) {
		manager.renderAll(g);

		g.setColor(Color.WHITE);
		g.setFont(Pong.font16);
		g.drawString("volume: " + Sound.getVolume(), Pong.SIZE.width - 115, 75);
	}
}
