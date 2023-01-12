package chat_projet;

import java.io.IOException;
import java.sql.SQLException;

public class MainServeur {
	public static void main(String[] args) throws IOException, SQLException {
		Serveur serveur = new Serveur(5001);
		while(true) {
			serveur.receiveAndSend();
		}
	}
}
