package chat_projet;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class View2 extends JFrame{
	Client client;
	JFrame window = new JFrame();
	JPanel panel = new JPanel();
	JPanel panell = new JPanel();
	JPanel panel2 = new JPanel();
	JPanel panelll = new JPanel();
	JLabel label = new JLabel("");
	JButton button1 = new JButton("Conversation");
	JButton button2 = new JButton("Supprimer un ami");
	JButton button3 = new JButton("Ajouter un ami");
	JButton button4 = new JButton("Se déconnecter");
	
	
	public View2(Client c) {
		this.client = c;
		window.setVisible(true);
		window.setSize(600, 500);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLayout(new FlowLayout());
		window.setTitle("APPLICATION CHAT");
		window.setLocation(300, 70);
		window.setLayout(new GridLayout(5, 1));
		window.add(new JLabel("Connexion passed successfully!", JLabel.CENTER));
		
		button1.setBackground(new Color(56, 145, 17));
		button2.setBackground(new Color(246, 88, 21));
		button3.setBackground(new Color(182, 104, 172));
		button4.setBackground(new Color(255, 78, 71));
		
		
		String[] list = c.mesAmis().split(",");
		ArrayList<JCheckBox> checkBoxes = new ArrayList<JCheckBox>();
		for(int i=0; i<list.length; i++) {
			JCheckBox but = new JCheckBox(list[i]);
			//checkBoxGroup.add(but);
			panell.add(but);
			checkBoxes.add(but);
			System.out.println(list[i]);
		}
		
		panell.add(label);
		panel2.add(button1);
		panel2.add(button2);
		
	    window.add(panell);
	    window.add(panel2);
		
	    ButtonGroup checkBoxGroup2 = new ButtonGroup();
		String[] list2 = c.tousClients().split(",");
		ArrayList<JCheckBox> checkBoxes2 = new ArrayList<JCheckBox>();
		for(int i=0; i<list2.length; i++) {
			JCheckBox but = new JCheckBox(list2[i]);
			checkBoxGroup2.add(but);
			panel.add(but);
			checkBoxes2.add(but);
			System.out.println(list2[i]);
		}
			
		panel.add(button3);
		panelll.add(button4);
		
		window.add(panel);
		window.add(panelll);
		
		
		
				
		
		//JOptionPane.showMessageDialog(null, s[0]);
		 
		button1.addActionListener((ae) -> {
			String noms = "";
			for(JCheckBox box : checkBoxes) {
				if(box.isSelected()) {
					String nomPourEnvoyerMsg = box.getText();
					noms += nomPourEnvoyerMsg+"-";
				}
			}
			if(!noms.equals("")) {
				new View3(c, noms);
			window.setVisible(false);
			}else {
				JOptionPane.showMessageDialog(null, "Vous devez selectionner au moins un nom!");
			}
			
		});
		
		button2.addActionListener((ae) -> {
			for(JCheckBox box : checkBoxes) {
				if(box.isSelected()) {
					String nomAmiASupprime = box.getText();
					String resp = c.supprimerAmi(nomAmiASupprime);
					if(resp.equals("true")) {
						box.setVisible(false);
					}else {
						JOptionPane.showMessageDialog(button2, "Operation refusée!");
					}
				}
			}
		});
		
		
		button3.addActionListener((ae) -> {
			for(JCheckBox box : checkBoxes2) {
				if(box.isSelected()){
					String nomAmiAAjoute = box.getText();
					String resp = c.ajouterAmi(nomAmiAAjoute);
					System.out.println("eeeeee "+ resp);
					if(resp.equals("true")) {
						JCheckBox nameAdded = new JCheckBox(nomAmiAAjoute);
						checkBoxes.add(nameAdded);
						panell.add(nameAdded);
						
					}else {
						JOptionPane.showMessageDialog(button3, "Vous êtes déja ami de " + nomAmiAAjoute);
					}
				}
			}
		});
		
		button4.addActionListener((ae) -> {
			c.logout();
			new View1();
			window.setVisible(false);
		});
	}
}


public class View2_client {
	public static void main(String[] args) {
		
	}
	
}
