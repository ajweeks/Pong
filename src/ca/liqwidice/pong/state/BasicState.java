package ca.liqwidice.pong.state;

import java.awt.Graphics;

import ca.liqwidice.pong.Pong;

public abstract class BasicState {

	protected Pong pong;

	public BasicState(Pong pong) {
		this.pong = pong;
	}

	public abstract void update();

	public abstract void render(Graphics g);
}
