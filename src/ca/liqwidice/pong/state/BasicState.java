package ca.liqwidice.pong.state;

import java.awt.Graphics;

public abstract class BasicState {

	public abstract void update();

	public abstract void render(Graphics g);

	public abstract int getID();
}
