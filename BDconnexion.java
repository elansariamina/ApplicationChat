package chat_projet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BDconnexion {
	public BDconnexion() {
		
	}
	
	public Statement etablirConnexion() throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/projet_chat", "root", "");
		System.out.println("Connected With the database successfully ");
	    Statement state = connection.createStatement();
		return state;
	}
	
	public ResultSet getData(Statement state, String requete) throws SQLException {
		ResultSet rs = state.executeQuery(requete);
		ResultSetMetaData resultMeta = rs.getMetaData();	
		return rs;
	}
	
	public void setData(Statement state, String requete) throws SQLException {	
		state.executeUpdate(requete);
	}

	
	
	public static void main(String[] args) {
		   try {
			   BDconnexion bd = new BDconnexion();
			   Statement state = bd.etablirConnexion();
			   //bd.setData(state, "INSERT INTO `client`(`nom_clt`, `password`) VALUES ('amal','amal123') ");
			   //bd.setData(state, "delete from `client` where id_clt=1");
				ResultSet res = bd.getData(state, "SELECT password FROM client where id_clt = 2");

				while(res.next()) {
					System.out.println(res.getString("password"));
				}
				
				//System.out.println(res.getString("login_clt"));
				
				
				
			   } catch (SQLException e) {
			   System.out.println("Error while connecting to the database");
			   e.getMessage();
			   e.getErrorCode();
			   }
}}

