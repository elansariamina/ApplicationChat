package chat_projet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class Client {
	String login;
	String password;
	Session session;
	DatagramSocket data;
	DatagramPacket packetIn;
	byte[] buffer;
	InetAddress inetaddress;
	
	


	//constructeur
	public Client(String login, String password) throws IOException {
		this.login = login;
		this.password = password;
		buffer = new byte[8000];
		data = new DatagramSocket();
		inetaddress = InetAddress.getByName("Localhost");
	}
	
	
	
	//methode pour enregistrer le compte
	public String enregistrer() {
		sendToServeur("enregistrer"+","+login+","+password+",");
		String reponse = receiveMessage(); String res = "";
		String[] conv = reponse.split("-");
		System.out.println(conv[0]+" --- "+conv[1]);
		if(conv[0].equals("signup")) {
			res = conv[1];
		}else;
		return res;
	}
	
	//methode poue se connecter à l'application
	public String login(){
		sendToServeur("connexion,"+login+","+password+",");
		String reponse = receiveMessage(); String res = "";
		String[] conv = reponse.split("-");
		System.out.println(conv[0]+" --- "+conv[1]);
		if(conv[0].equals("login")) {
			res = conv[1];
		}else;
		return res;
	}
	
	//methode pour se déconnecter de l'application
	public void logout() {
		sendToServeur("deconnexion,"+login+",");
	}
	
	//methode pour ajouter un ami à la liste des amis de l'utilisateur
	public String ajouterAmi(String nomAmi) {
		sendToServeur("gestionAmi,1,"+login+","+nomAmi+",");
		String reponse = receiveMessage(); String res = "";
		String[] conv = reponse.split("-");
		System.out.println(conv[0]+" --- "+conv[1]);
		if(conv[0].equals("gestion")) {
			res = conv[1];
		}else;
		return res;
	}
	
	//methode pour supprimer un ami de la liste des amis de l'utilisateur
	public String supprimerAmi(String nomAmi) {
		sendToServeur("gestionAmi,2,"+login+","+nomAmi+",");
		String reponse = receiveMessage(); String res = "";
		String[] conv = reponse.split("-");
		System.out.println(conv[0]+" --- "+conv[1]);
		if(conv[0].equals("gestion")) {
			res = conv[1];
		}else;
		return res;
	}
	
	//methode pour afficher mes amis
	public String mesAmis() {
		sendToServeur("listeAmis,"+login+",amiamiamiamiamiamiamiamiamiamiamiami,");
		String reponse = receiveMessage(); String res = "";
		String[] conv = reponse.split("-");
		System.out.println(conv[0]+" --- "+conv[1]);
		if(conv[0].equals("listeA")) {
			res = conv[1];
		}else;
		return res;
	}
	
	//methode pour afficher tous les clients de l'application
	public String tousClients() {
		sendToServeur("listeClients,"+login+",listelistelistelistelistelisteliste,");
		String reponse = receiveMessage(); String res = "";
		String[] conv = reponse.split("-");
		System.out.println(conv[0]+" --- "+conv[1]);
		if(conv[0].equals("listeC")) {
			res = conv[1];
		}else;
		return res;
	}
	
	//methode pour envoyer un message
	public void sendMessage(String mesAmis, String message) {
		sendToServeur("Envoi,"+login+","+mesAmis+","+message+",");
	}
	
	//
	public void AnciensMsgs() {
		sendToServeur("demande,"+login+",");
	}
	public void MesMsgs() {
		sendToServeur("mesmsgs,"+login+",");
	}
	//methode pour recevoir du serveur
	public String receiveMessage(){
		byte[] b = new byte[1024];
		String conversation = "";
		DatagramPacket packet = new DatagramPacket(b, b.length);
		try {
			data.receive(packet);
			conversation = new String(packet.getData(), 0, packet.getLength());
			System.out.println("recu: " + conversation );
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conversation;
	}
	
	//methode d'envoi de données au serveur
	public void sendToServeur(String msg) {
			try {
				String msgToSend = msg;
				buffer = msgToSend.getBytes();
				DatagramPacket packetOut = new DatagramPacket(buffer, buffer.length, inetaddress, 5001);
				data.send(packetOut); 
			}catch(IOException e) {
				e.getStackTrace();
			}
		
	}
	
}
