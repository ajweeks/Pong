package ca.liqwidice.pong.state;

import java.awt.Graphics;

import ca.liqwidice.pong.Pong;
import ca.liqwidice.pong.TextBox;
import ca.liqwidice.pong.button.ButtonManager;
import ca.liqwidice.pong.button.ImageButton;
import ca.liqwidice.pong.input.Keyboard.Key;

public class ServerBrowserState extends BasicState {

	public static final String MAIN_MENU = "MAIN MENU";
	public static final String NEW_SERVER = "NEW SERVER";
	public static final String JOIN_SERVER = "JOIN SERVER";

	private ButtonManager manager;
	private TextBox portInput;

	public ServerBrowserState(Pong pong) {
		super(pong);
		manager = new ButtonManager();
		manager.addButton(new ImageButton(MAIN_MENU, 15, 15, 250, 85));
		manager.addButton(new ImageButton(NEW_SERVER, Pong.SIZE.width / 2 - 250 / 2,
				Pong.SIZE.height / 2 - 85 / 2 - 50, 250, 85));
		portInput = new TextBox(Pong.SIZE.width / 2 - 250 / 2, Pong.SIZE.height / 2 - 85 / 2 + 115, 250, 25);
		portInput.setAcceptsNumbers(true);
		portInput.setAcceptsLetters(false);
		portInput.setAcceptsSpecialCharacters(false);
		portInput.setPromptText("    port number (1-63885)");
		manager.addButton(new ImageButton(JOIN_SERVER, Pong.SIZE.width / 2 - 250 / 2, Pong.SIZE.height / 2 - 85 / 2
				+ 150, 250, 85));
	}

	public void update() {
		manager.updateAll();

		if (manager.getButton(MAIN_MENU).isClicked() || Key.ESC.clicked) {
			pong.getStateManager().enterPreviousState();
		} else if (manager.getButton(NEW_SERVER).isClicked()) {
			pong.getStateManager().addState(new ServerState(pong));
		} else if (manager.getButton(JOIN_SERVER).isClicked()) {
			try {
				pong.getStateManager().addState(new ClientState(pong, Integer.parseInt(portInput.getText())));
			} catch (NumberFormatException e) {
				System.out.println("Invalid port!");
				portInput.clear();
			}
		}

		portInput.update();
	}

	public void render(Graphics g) {
		manager.renderAll(g);
		portInput.render(g);
	}

	public void clearPort() {
		portInput.clear();
	}
}
