package ca.liqwidice.pong.button;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

import ca.liqwidice.pong.Colour;
import ca.liqwidice.pong.Pong;

public class ImageButton extends Button {

	public static final Image BTN = new ImageIcon("res/sprites/btn.png").getImage();
	public static final Image BTN_HOV = new ImageIcon("res/sprites/btn_hov.png").getImage();

	public static final Image SMALL_BTN = new ImageIcon("res/sprites/small_btn.png").getImage();
	public static final Image SMALL_BTN_HOV = new ImageIcon("res/sprites/small_btn_hov.png").getImage();

	private Image image, hovImage;

	public ImageButton(String text, int x, int y, int width, int height, Image image, Image hovImage) {
		super(text, x, y, width, height);
		this.image = image;
		this.hovImage = hovImage;
	}

	public ImageButton(String text, int x, int y, int width, int height) {
		this(text, x, y, width, height, BTN, BTN_HOV);
	}

	@Override
	public void render(Graphics g) {
		if (!visible) return;
		Image i;
		if (hover) {
			i = hovImage;
			g.setColor(Colour.GRAY_TEXT);
		} else {
			i = image;
			g.setColor(Color.WHITE);
		}
		g.drawImage(i, x, y, width, height, null);

		g.setFont(Pong.font32);
		g.drawString(text, x + (width / 2) - (g.getFontMetrics().stringWidth(text) / 2), y + (height / 2) + 8);
	}

}
