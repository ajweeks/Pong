package ca.liqwidice.pong.state;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import ca.liqwidice.pong.Pong;
import ca.liqwidice.pong.input.Keyboard.Key;
import ca.liqwidice.pong.level.Level;

public class ServerHostState extends BasicState implements Runnable {

	private Pong pong;
	private DatagramSocket socket;
	private Level level;

	private boolean isConnected = false;

	public ServerHostState(Pong pong) {
		this.pong = pong;
		level = Level.getDefaultPVPLevel();
		new Thread(this).start();
	}

	public void update() {
		if (Key.ESC.clicked) pong.getStateManager().addState(new ServerBrowserState(pong));
	}

	public void render(Graphics g) {
		if (isConnected) {
			g.setColor(Color.white);
			g.setFont(Pong.font32);
			g.drawString("Waiting for someone to join port #" + socket.getLocalPort(), 15, Pong.SIZE.height / 2 - 20);
		} else {
			level.render(g);
		}
	}

	public void serv() {
		if (socket != null) {
			byte[] recieveData = new byte[1024];
			byte[] sendData = new byte[1024];
			try {
				DatagramPacket recivePacket = new DatagramPacket(recieveData, recieveData.length);
				socket.receive(recivePacket);
				System.out.println("recieved: " + new String(recivePacket.getData()));
				InetAddress port = recivePacket.getAddress();
				sendData = recivePacket.getData();
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length);
				socket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void run() {
		try {
			socket = new DatagramSocket(0);
			while (true) {
				serv();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
