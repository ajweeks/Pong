package ca.liqwidice.pong.state;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.net.Socket;

import ca.liqwidice.pong.Pong;
import ca.liqwidice.pong.level.Level;
import ca.liqwidice.pong.sound.Sound;

public class ServerJoinState extends BasicState {

	private Pong pong;

	private Socket socket;
	private Level level;

	public ServerJoinState(Pong pong) {
		this.pong = pong;
		level = Level.getDefaultPVPLevel();
	}

	public void setPort(String name, int port) {
		try {
			socket = new Socket("", port);
		} catch (Exception e) {
			pong.getStateManager().addState(new ServerBrowserState(pong));
			Sound.lose.play();
			((ServerBrowserState) pong.getStateManager().getCurrentState()).clearPort(); //TODO catch incorrect ports better
		}
	}

	public void update() { //Don't actually update anything, except for this paddle, just listen to what the server tells us
		if (socket == null) return;
		if (!socket.isBound()) return;

		level.update();
		//tell server our position

		//see if the other player's position has changed
		try {
			int a = 0;
			while ((a = socket.getInputStream().read()) != -1) {
				System.out.println(a);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void render(Graphics g) {
		if (!socket.isBound()) {
			g.setColor(Color.WHITE);
			g.setFont(Pong.font16);
			g.drawString("connecting...", 200, 200);
		} else {
			level.render(g);
		}
	}
}
