package io.fabio.crud.db.operation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import javax.faces.context.FacesContext;

import io.fabio.crud.UsuarioBean;

public class DatabaseOperation {
	public static Statement stmtObj;
	public static Connection connObj;
	public static ResultSet resultSetObj;
	public static PreparedStatement pstmt;

	public static Connection getConnection(){  
		try{  
			Class.forName("com.mysql.cj.jdbc.Driver");     
			String db_url ="jdbc:mysql://localhost:3306/exemplo1",
					db_userNome = "root",
					db_password = "12345678";
			connObj = DriverManager.getConnection(db_url,db_userNome,db_password);  
		} catch(Exception sqlException) {  
			sqlException.printStackTrace();
		}  
		return connObj;
	}
	
	public static void inicializar() {
		try {
			
			stmtObj = getConnection().createStatement();    
			resultSetObj = stmtObj.executeQuery(" select count(1) as rowcount FROM information_schema.tables WHERE table_schema = 'exemplo1'  AND table_name = 'usuario' LIMIT 1; " );    
			resultSetObj.next();		
			
			
			if (resultSetObj.getInt("rowcount") == 0) {
				connObj.close();
				
				pstmt = getConnection().prepareStatement("CREATE TABLE `usuario` (\n" + 
														"  `id` int(11) NOT NULL AUTO_INCREMENT,\n" + 
														"  `nome` varchar(100) DEFAULT NULL,\n" + 
														"  `email` varchar(50) DEFAULT NULL,\n" + 
														"  PRIMARY KEY (`id`)\n" + 
														"); ");
				pstmt.executeUpdate();		
				connObj.close();
				
				pstmt = getConnection().prepareStatement("INSERT INTO `usuario` VALUES (1,'fabio','fabio@gmail.com'),(2,'gustavo','gustavo@gmail.com');");
				pstmt.executeUpdate();		
				connObj.close();
			}
			else
			{
				connObj.close();
			}
			
		} catch(Exception sqlException) {
			sqlException.printStackTrace();
		} 
		
	}

	public static ArrayList getUsuariosListFromDB() {
		ArrayList usuariosList = new ArrayList();  
		try {
			stmtObj = getConnection().createStatement();    
			resultSetObj = stmtObj.executeQuery("select * from usuario");    
			while(resultSetObj.next()) {  
				UsuarioBean stuObj = new UsuarioBean(); 
				stuObj.setId(resultSetObj.getInt("id"));  
				stuObj.setNome(resultSetObj.getString("nome"));  
				stuObj.setEmail(resultSetObj.getString("email"));  
				usuariosList.add(stuObj);  
			}   
			System.out.println("Total Records Fetched: " + usuariosList.size());
			connObj.close();
		} catch(Exception sqlException) {
			sqlException.printStackTrace();
		} 
		return usuariosList;
	}

	public static String saveUsuarioDetailsInDB(UsuarioBean newUsuarioObj) {
		int saveResult = 0;
		String navigationResult = "";
		try {      
			pstmt = getConnection().prepareStatement("insert into usuario (nome, email) values (?, ?)");			
			pstmt.setString(1, newUsuarioObj.getNome());
			pstmt.setString(2, newUsuarioObj.getEmail());
			saveResult = pstmt.executeUpdate();
			connObj.close();
		} catch(Exception sqlException) {
			sqlException.printStackTrace();
		}
		if(saveResult !=0) {
			navigationResult = "usuariosLista.xhtml?faces-redirect=true";
		} else {
			navigationResult = "criarUsuario.xhtml?faces-redirect=true";
		}
		return navigationResult;
	}

	public static String editUsuarioRecordInDB(int usuarioId) {
		UsuarioBean editRecord = null;
		System.out.println("editUsuarioRecordInDB() : Usuario Id: " + usuarioId);

		/* Setting The Particular Usuario Details In Session */
		Map<String,Object> sessionMapObj = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();

		try {
			stmtObj = getConnection().createStatement();    
			resultSetObj = stmtObj.executeQuery("select * from usuario where id = "+usuarioId);    
			if(resultSetObj != null) {
				resultSetObj.next();
				editRecord = new UsuarioBean(); 
				editRecord.setId(resultSetObj.getInt("id"));
				editRecord.setNome(resultSetObj.getString("nome"));
				editRecord.setEmail(resultSetObj.getString("email"));
			}
			sessionMapObj.put("editRecordObj", editRecord);
			connObj.close();
		} catch(Exception sqlException) {
			sqlException.printStackTrace();
		}
		return "/editarUsuario.xhtml?faces-redirect=true";
	}

	public static String updateUsuarioDetailsInDB(UsuarioBean updateUsuarioObj) {
		try {
			pstmt = getConnection().prepareStatement("update usuario set nome=?, email=? where id=?");    
			pstmt.setString(1,updateUsuarioObj.getNome());  
			pstmt.setString(2,updateUsuarioObj.getEmail());  
			pstmt.setInt(3,updateUsuarioObj.getId());  
			pstmt.executeUpdate();
			connObj.close();			
		} catch(Exception sqlException) {
			sqlException.printStackTrace();
		}
		return "/usuariosLista.xhtml?faces-redirect=true";
	}

	public static String deleteUsuarioRecordInDB(int usuarioId){
		try {
			pstmt = getConnection().prepareStatement("delete from usuario where id = "+usuarioId);  
			pstmt.executeUpdate();  
			connObj.close();
		} catch(Exception sqlException){
			sqlException.printStackTrace();
		}
		return "/usuariosLista.xhtml?faces-redirect=true";
	}	
}
