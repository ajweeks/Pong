package ca.liqwidice.pong.button;

import java.awt.Color;
import java.awt.Graphics;

import ca.liqwidice.pong.Colour;
import ca.liqwidice.pong.Pong;
import ca.liqwidice.pong.sound.Sound;

public class Button {

	protected String text;
	protected int x, y, width, height;
	protected boolean hover = false;
	protected boolean clicked = false;
	protected boolean down = false;
	protected boolean visible = true;
	private Color col, hovcol;

	public Button(String text, int x, int y, int height, int width, Color col, Color hovcol) { //TODO add text rendering
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.col = col;
		this.hovcol = hovcol;
	}

	public Button(String text, int x, int y, int width, int height) {
		this(text, x, y, height, width, Colour.btn, Colour.btn_hov);
	}

	public void update() {
		if (!visible) return;
		if (Pong.mouse.getX() > x && Pong.mouse.getX() < x + width && Pong.mouse.getY() > y
				&& Pong.mouse.getY() < y + height) {
			hover = true;
			if (down) clicked = false;
			else if (Pong.mouse.isLeftDown()) {
				Sound.click.play();
				clicked = true;
			}
			down = Pong.mouse.isLeftDown();
			return;
		} else hover = false;
		down = false;
		clicked = false;
		return;
	}

	public void render(Graphics g) {
		if (!visible) return;
		if (hover) g.setColor(hovcol);
		else g.setColor(col);
		g.fillRect(x, y, width, height);

		g.setColor(Color.WHITE);
		g.setFont(Pong.font32);
		g.drawString(text, x + (width / 2) - (g.getFontMetrics().stringWidth(text) / 2), y + (height / 2) + 8);
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isHovering() {
		if (!visible) return false;
		return hover;
	}

	public boolean isClicked() {
		if (!visible) return false;
		return clicked;
	}

	public boolean isDown() {
		if (!visible) return false;
		return down;
	}

}
