package ca.liqwidice.pong;

import java.awt.Color;
import java.awt.Graphics;

public class Button {

	private String text;
	private int x, y, width, height;
	private boolean hover = false, clicked = false, down = false, visible = true;
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
			else if (Pong.mouse.isLeftDown()) clicked = true;
			down = Pong.mouse.isLeftDown();
			return;
		} else hover = false;
		down = false;
		clicked = false;
		return;
	}

	public void render(Graphics g) {
		if (!visible) return;
		if (hover) g.setColor(col);
		else g.setColor(hovcol);
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
		return hover;
	}

	public boolean isClicked() {
		return clicked;
	}

	public boolean isDown() {
		return down;
	}

}
