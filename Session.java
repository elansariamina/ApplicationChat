package chat_projet;

import java.net.InetAddress;

public class Session {
	String login;
	InetAddress address;
	int port;
	public Session(String l, InetAddress adr, int p) {
		this.login = l;
		this.address = adr;
		this.port = p;
	}
	
}
