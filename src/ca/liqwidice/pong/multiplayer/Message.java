package ca.liqwidice.pong.multiplayer;

public class Message {

	public static final String MOVE = "M";
	public static final String INTERSECT = "I";

	private String id;
	private int y;

	public Message(String id, int y) {
		this.id = id;
		this.y = y;
	}

	public int getY() {
		return y;
	}

	public String getID() {
		return id;
	}

	public String send() {
		return id + y;
	}
	
}
