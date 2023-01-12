package chat_projet;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

class View3 extends JFrame {
	Client client;
	JFrame window = new JFrame();
	JLabel titre = new JLabel("Conversation", JLabel.CENTER);
	JLabel label = new JLabel("");
	JLabel fichierLabel =  new JLabel("No file ");
	
	JPanel panel = new JPanel();
	JPanel panel2 = new JPanel();
	JPanel panel3 = new JPanel();
	
	JButton file = new JButton("Choose");
	JButton send = new JButton("Envoyer");
	JButton retour = new JButton("Retour");
	JButton logout = new JButton("Se déconnecter");
	JButton show = new JButton("Show");
	
	JTextArea chat = new JTextArea();
	JTextField msg = new JTextField(50);
	JScrollPane scroll = new JScrollPane (chat, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	String msgToSend = "";
	
	public View3(Client c, String amis) {
		this.client = c;
		window.setVisible(true);
		window.setSize(600, 500);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLayout(new FlowLayout());
		window.setTitle("APPLICATION CHAT");
		window.setLocation(300, 70);
		window.setLayout(new GridLayout(6, 1));
		
		file.setBackground(new Color(0, 129, 203));
		send.setBackground(new Color(222, 185, 124));
		retour.setBackground(new Color(0, 204, 167));
		logout.setBackground(new Color(255, 85, 126));
		show.setBackground(new Color(210, 159, 75));
		
		String[] mesAmis = amis.split("-");
		
		window.add(titre);
		label.setText(amis);
		window.add(label);
		window.add(scroll);
		panel3.add(fichierLabel);
		panel3.add(show);
		panel2.add(msg);
		panel2.add(send);
		panel2.add(file);
		panel.add(retour);
		panel.add(logout);
		
		window.add(panel3);
		window.add(panel2);
		window.add(panel);
		
		c.AnciensMsgs();
		c.MesMsgs();
		
		file.addActionListener((ae) -> {
			JFileChooser f = new JFileChooser();
			int ff = f.showSaveDialog(null);
			System.out.println(ff);
			if (ff != JFileChooser.APPROVE_OPTION) 
				System.out.println("different");
			else {
				System.out.println(f.getSelectedFile().toPath());
			String path = f.getSelectedFile().toPath().toString();
			 File doc = new File(path);
			        Scanner obj;
			        String fichier = "";
					try {
						obj = new Scanner(doc);
			        while (obj.hasNextLine())
			            fichier += obj.nextLine();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					client.sendMessage(amis, "fichier:"+fichier);
			        
			}
			
		});
		
		logout.addActionListener((ae) -> {
			c.logout();
			new View1();
			window.setVisible(false);
		});
		
		
		Thread th = new Thread() {
			public void run() {
				while(true) {
					String reponse = c.receiveMessage();
					String[] conv = reponse.split("-");
					//System.out.println(reponse + "()()()" + conv[0]+" --- "+conv[1]+" --- ");
					if(conv[0].equals("chat")) {
						String fich[] = conv[1].split(":");
						if(fich[0].equals("fichier")) {
							fichierLabel.setText("One file to open");
							show.addActionListener((ae) -> {
							JOptionPane.showMessageDialog(show, fich[1]);
							fichierLabel.setText("No file");
							
							});
						}else {
							chat.append("\n "+ conv[2] + ": " + conv[1]);
							System.out.println("view3: " + conv[1]);
						}
						
					}
					if(conv[0].equals("nonlu")) {
						String fich[] = conv[1].split(";");
						String n = label.getText();
						
						for(int r=0; r<fich.length; r+=2) {
							if(n.contains(fich[r+1])) {
								chat.append("\n "+ fich[r+1] + ": " + fich[r]);
								System.out.println("view3: " + fich[r+1]);
							}
						}
					}
					if(conv[0].equals("msgss")) {
						String fich[] = conv[1].split(";");
						String n = label.getText();
						
						for(int r=0; r<fich.length; r+=2) {
							if(n.contains(fich[r+1])) {
								chat.append("\n You: " + fich[r]);
								System.out.println("yyy: " + fich[r+1]);
							}
						}
					}
				}
			}
		};
		th.start();
		
		 chat.append("Welcome:");
		 
		retour.addActionListener((ae) -> {
			th.stop();
			window.setVisible(false);
			new View2(c);
		});
		
		send.addActionListener((ae) -> {
			msgToSend = msg.getText();
			chat.append("\n You: " + msgToSend);
			msg.setText(null);
			System.out.println("Send " + msgToSend);
			client.sendMessage(amis, msgToSend);
		});
		
		 
	}
	
}

public class View3_client{
	public static void main(String[] args) {
		
	}
}
