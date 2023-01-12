package chat_projet;

import java.awt.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import javax.swing.*;

class View1 extends JFrame{
		JFrame window = new JFrame();
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		JLabel label = new JLabel("BIENVENUS, veuillez entrer vos informations:", JLabel.CENTER);
		JLabel login = new JLabel("Login:");
		JLabel password = new JLabel("Password:");
		JLabel res = new JLabel("", JLabel.CENTER);
		JTextField field1 = new JTextField(20);
		JPasswordField field2 = new JPasswordField(20);
		JButton connexion = new JButton("Se connecter");
		JButton signup = new JButton("S'enregistrer");
		
		
	public View1() {
		window.setVisible(true);
		window.setSize(600, 500);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLayout(new FlowLayout());
		window.setTitle("APPLICATION CHAT");
		window.setLocation(300, 70);
		window.setLayout(new GridLayout(5, 1));
		res.setSize(100, 50);
		window.add(label);
		
		connexion.setBackground(new Color(50, 200, 100));
		signup.setBackground(new Color(17, 181, 228));
		
		panel1.add(login);
		panel1.add(field1);
		panel2.add(password);
		panel2.add(field2);
		panel3.add(connexion);
		panel3.add(signup);
		
		window.add(panel1);
		window.add(panel2);
		window.add(res);
		window.add(panel3);
		
		
		
		connexion.addActionListener((ae)->{
			String log = field1.getText();
			String pass = field2.getText(); 
			Client c = null ;
			String reponse = "";
				try { 
					c = new Client(log, pass);
					reponse = c.login();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 if(reponse.equals("true")) {
				 new View2(c); 
				 window.setVisible(false);
				 
			 }else {
				 field2.setText("");
				 field1.setText("");
				 res.setText("Login or Password incorrect!");
			 }
			
			
		});
		signup.addActionListener((ae)->{
			String log = field1.getText();
			String pass = field2.getText();
			String reponse = "";
			try {
				Client c2 = new Client(log, pass);
				reponse = c2.enregistrer();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(reponse.equals("true")) {
				field1.setText("");
				field2.setText("");
				JLabel l = new JLabel("Enregistrement fait avec succès, veuillez se connecter maintenant", JLabel.CENTER);
				panel2.add(l);
				signup.setVisible(false);
			}else {
				JLabel l = new JLabel("Une erreur est survenue ! Réessayer!", JLabel.CENTER);
				panel2.add(l);
			}
					});
	}
}


public class View1_client{
	public static void main(String[] args) {
		View1 v = new View1();
	}
}