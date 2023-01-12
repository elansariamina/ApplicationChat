package chat_projet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Serveur {
	//les attributs
	BDconnexion bd;
	Statement state;
	DatagramSocket data;
	DatagramPacket packetIn;
	byte[] tampon = new byte[8000];
	int port;
	List<Session> listeSession = new ArrayList<Session>();
	
	//le constructeur
	public Serveur(int port) throws SocketException, SQLException {
		data = new DatagramSocket(5001);
		this.port = port;
		bd = new BDconnexion();
		state = bd.etablirConnexion();
	}
	
	//methode pour enregistrer le compte dans la BD
	public String enregistrement(String[] infos) {
		String nom = infos[1];
		String password = infos[2];
		try {
			String requete = "INSERT INTO `client`(`login_clt`, `password`) VALUES ('"+nom+"','"+password+"') ";
			bd.setData(state, requete);
			return "true";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "false";
		}
		
	}
	
	//methode pour verifier l'authentification
	public String verifierLogin(String[] infos) throws SQLException {
		String nom = infos[1];
		String password = infos[2];
		String requete = "SELECT * FROM `client` WHERE `login_clt` = '"+nom+"'";
		ResultSet res = bd.getData(state, requete);
		String tampon = "";
		while(res.next()) {
			//Si le password est correct, on enregistre le client dans la session des personnes connectés
			if((res.getString("password")).equals(password)) {
				enregistrerSession(res.getString("login_clt"));
				tampon = "true";
			}else {
				tampon = "false";
			}
		}
		if(tampon.equals("")) {
			tampon = "false";
		}
		return tampon;
	}
	
	//methode pour supprimer le client de la liste session(deconnexion)
	public void deconnexion(String[] infos) throws SQLException {
		String nom = infos[1];
		System.out.println("----------- " + listeSession.size());
		for(int i=0; i< listeSession.size(); i++) {
			System.out.println("ssss " + listeSession.get(i).login + " >>> " + nom);
			if(listeSession.get(i).login.equals(nom)) {
				listeSession.remove(i);
			}
		}
		System.out.println("----------- " + listeSession.size());
	}
	
	//methode pour ajouter ou supprimer un ami de la base de données
	public String gererAmis(String[] infos) throws SQLException{
		String fonction = infos[1];
		String nomClient = infos[2];
		String nomAmi = infos[3];
		//chercher l'idClient depuis le nom du client
		String req1 = "SELECT id_clt FROM `client` WHERE login_clt = '"+nomClient+"'";
		ResultSet res1 = bd.getData(state, req1);
		int idClient = 0; int idAmi = 0;
		while(res1.next() ) {
			idClient = res1.getInt("id_clt");
		}
		// chercher l'idAmi de puis son nom
		String req2 = "SELECT id_clt FROM `client` WHERE login_clt = '"+nomAmi+"'";
		ResultSet res2 = bd.getData(state, req2);
		while(res2.next() ) {
			idAmi = res2.getInt("id_clt");
		}
		System.out.println("a..." + idClient);
		System.out.println("b..." + idAmi);
		//tester est ce qu'on veut ajouter ou supprimer 1-->l'ajout, 2-->suppression
		if(fonction.equals("1")) {
			String requete = "SELECT * FROM `amis` WHERE (id_clt1 = "+idClient+" AND id_clt2 = "+idAmi+") OR (id_clt1 = "+idAmi+" AND id_clt2 = "+idClient+")";
			ResultSet res = bd.getData(state, requete);
			//tester si ils sont déja amis
			if(res.next()) {
				System.out.println("Vous êtes déjà amis de " + nomAmi);
				return "false";
			}else {
				String req3 = "INSERT INTO `amis`(`id_clt1`, `id_clt2`) VALUES ('"+idClient+"', '"+idAmi+"') ";
				bd.setData(state, req3);
				System.out.println(nomAmi + " est votre ami maintenant");
				return "true";
			}
			//suppression
		}else {
			String requete = "DELETE FROM `amis` WHERE (id_clt1 = "+idClient+" AND id_clt2 = "+idAmi+") OR (id_clt1 = "+idAmi+" AND id_clt2 = "+idClient+")";
			try {
				bd.setData(state, requete);
				System.out.println("Vous n'êtes plus amis de " + nomAmi);
				return "true";
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "false";
			}
		}
	}
	
	//methode pour afficher la liste des amis d'un client
	public String listeAmis(String[] infos) throws SQLException {
		String nom = infos[1];
		//chercher l'id
		String requete = "SELECT * FROM `client` WHERE `login_clt` = '"+nom+"'";
		ResultSet res = bd.getData(state, requete);
		int id = 0;;
		while(res.next()) {
			id = res.getInt("id_clt");
		}
		
		String req = "SELECT login_clt FROM `client`, `amis` WHERE (`id_clt1` = `id_clt` OR `id_clt2` = `id_clt`) AND (`id_clt1` = '"+id+"' OR `id_clt2` = '"+id+"') AND `login_clt` != '"+nom+"'";
			ResultSet ress = bd.getData(state, req);
			String noms = " ";
			while(ress.next()) { 
				System.out.println(ress.getString("login_clt"));
				noms += ress.getString("login_clt")+",";
			}
		return noms;
	}
	//methode pour afficher la liste de tous les utilisateurs de l'application
	public String listeClients(String[] infos) throws SQLException{
		String nom = infos[1];
		//selectionner tous les clients sauf le client connecté maintenant
		String requete = "SELECT login_clt FROM `client` WHERE `login_clt` != '"+nom+"'";
		ResultSet res = bd.getData(state, requete);
		String noms = "";
		while(res.next()) {
			noms += res.getString("login_clt")+",";
		}
		System.out.println("Les utilisateurs connectés: ");
		for(Session s : listeSession) {
			System.out.println(s.login + ", " + s.address + ", " + s.port);
		}
		return noms;
	}
	
	//methode pour transferer un message vers un client
	public String sendMsg(String[] infos) {
		String sender = infos[1];
		String receivers = infos[2];
		String message = infos[3];
		String[] receiver = receivers.split("-");
		for(int j=0; j<receiver.length; j++) {
			try {
				//chercher l'id du sender
				String requete1 = "SELECT * FROM `client` WHERE `login_clt` = '"+sender+"'";
				ResultSet res1 = bd.getData(state, requete1);
				int idSender = 0;
				while(res1.next()) {
					idSender = res1.getInt("id_clt");
				}
				//chercher l'id du receiver
				ResultSet res2 = bd.getData(state, "SELECT * FROM `client` WHERE `login_clt` = '"+receiver[j].trim()+"'");
				int idReceiver = 0;
				while(res2.next()) {
					idReceiver = res2.getInt("id_clt");
				}
				System.out.println("s: " + idSender + " receiver: " + receiver[j] + " r: " + idReceiver);
				if(message.contains("fichier"));
				//stocker les messages dans la base de données
				else{String requete = "INSERT INTO `message`(`message`, `sender`, `receiver`) VALUES ('"+message+"', '"+idSender+"', '"+idReceiver+"') ";
				bd.setData(state, requete);}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int i=0; i<listeSession.size();i++) {
				//chercher si le receiver est connecté pour lui envoyer le message
				if(listeSession.get(i).login.equals(receiver[j].trim())) {
					byte[] tomp = new byte[1024];
					tomp=("chat-"+message+"-"+sender+"-").getBytes();
					System.out.println("__ "+infos[0]+"__ "+sender+"__ "+receiver[j]+"__ "+message+"__ ");
					System.out.println("Adresse du receiver: " + listeSession.get(i).address);
					System.out.println("Port du receiver: " + listeSession.get(i).port);
					try {
						DatagramPacket packetIn = new DatagramPacket(tomp, tomp.length, listeSession.get(i).address, listeSession.get(i).port);
						data.send(packetIn);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}
		
		return message;
	}
	
	//methode pour ajouter une ligne dans la table session
	public void enregistrerSession(String login) throws SQLException {
		InetAddress address = packetIn.getAddress();
		int port = packetIn.getPort();
		Session ses = new Session(login, address, port);
		listeSession.add(ses);
	}
	
	//methode pour recuperer les messages que j'ai reçu
	public String MsgsNonLu(String nom) throws SQLException {
		String str = "";
		ResultSet res2 = bd.getData(state, "SELECT * FROM `client` WHERE `login_clt` = '"+nom+"'");
		int idReceiver = 0;
		while(res2.next()) {
			idReceiver = res2.getInt("id_clt");
		}
		System.out.println(" receiver: " + nom + " r: " + idReceiver);
		
		String requete = "SELECT * FROM `message`, `client` WHERE `id_clt` = `sender` AND `receiver` = '"+idReceiver+"';";
		ResultSet res = bd.getData(state, requete);
		while(res.next()) {
			String msg = res.getString("message");
			String send = res.getString("login_clt");
			System.out.println("Message: " + msg + " , sender: " + send);
			str += msg + ";" + send + ";";
		}
		System.out.println("Message non lu : " + str);
		return str;
	}
	
	//methode pour recuperer les messages que j'ai envoyé
	public String MesMsgs(String nom) throws SQLException {
		String str = "";
		ResultSet res2 = bd.getData(state, "SELECT * FROM `client` WHERE `login_clt` = '"+nom+"'");
		int idSender = 0;
		while(res2.next()) {
			idSender = res2.getInt("id_clt");
		}
		System.out.println(" sender: " + nom + " r: " + idSender);
		
		String requete = "SELECT * FROM `message`, `client` WHERE `id_clt` = `receiver` AND `sender` = '"+idSender+"';";
		ResultSet res = bd.getData(state, requete);
		while(res.next()) {
			String msg = res.getString("message");
			String rec = res.getString("login_clt");
			System.out.println("Message: " + msg + " , receiver: " + rec);
			str += msg + ";" + rec + ";";
		}
		System.out.println("Message de ma part : " + str);
		return str;
	}
	
	//methodes d'envoi et reception entre le client et le serveur
	public void receiveAndSend() throws SQLException  {
		try {
			packetIn = new DatagramPacket(tampon, tampon.length);
			data.receive(packetIn);
			InetAddress adres = packetIn.getAddress();
			int port = packetIn.getPort();
			String msg = new String(packetIn.getData(), 0, packetIn.getLength());
			System.out.println("Le message envoyé est : " + msg);
			String[] tabMsg = msg.split(",");
			String tomp = "";
			if(tabMsg[0].equals("enregistrer")) {
				tomp = "signup-"+this.enregistrement(tabMsg)+"-";
			}else if(tabMsg[0].equals("connexion")) {
				tomp = "login-"+this.verifierLogin(tabMsg)+"-";
			}else if(tabMsg[0].equals("listeAmis")){
				tomp = "listeA-"+this.listeAmis(tabMsg)+"-";
			}else if(tabMsg[0].equals("gestionAmi")) {
				tomp = "gestion-"+this.gererAmis(tabMsg)+"-";
			}else if(tabMsg[0].equals("listeClients")){
				tomp = "listeC-"+this.listeClients(tabMsg)+"-";
			}else if(tabMsg[0].equals("Envoi")){
				String ss=this.sendMsg(tabMsg);
				System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh: " + ss);
				tomp = "send-"+ss+"-";
			}else if(tabMsg[0].equals("demande")) {
				tomp = "nonlu-"+this.MsgsNonLu(tabMsg[1])+"-";
			}else if(tabMsg[0].equals("mesmsgs")) {
				tomp = "msgss-"+this.MesMsgs(tabMsg[1])+"-";
			}else if(tabMsg[0].equals("deconnexion")) {
				this.deconnexion(tabMsg);
				tomp = "logout-true-";
			}
			System.out.println(tomp);
		byte[] tabByte = tomp.getBytes();
			System.out.println("Adresse du sender: " + adres);
			System.out.println("port du sender: " + port);
		DatagramPacket packetIn = new DatagramPacket(tabByte, tabByte.length, adres, port);
		data.send(packetIn);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
}
	
}
