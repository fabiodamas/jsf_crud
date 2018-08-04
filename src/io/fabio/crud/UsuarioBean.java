package io.fabio.crud;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import io.fabio.crud.db.operation.DatabaseOperation;

@ManagedBean 
public class UsuarioBean {
	private int id;  
	private String nome;  
	private String email; 	
	
	public ArrayList usuarioListFromDB;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@PostConstruct
	public void init() {
		usuarioListFromDB = DatabaseOperation.getUsuariosListFromDB();
	}

	public ArrayList usuarioList() {
		return usuarioListFromDB;
	}
	
	public String saveUsuarioDetails(UsuarioBean newUsuarioObj) {
		return DatabaseOperation.saveUsuarioDetailsInDB(newUsuarioObj);
	}
	
	public String editUsuarioRecord(int usuarioId) {
		return DatabaseOperation.editUsuarioRecordInDB(usuarioId);
	}
	
	public String updateUsuarioDetails(UsuarioBean updateUsuarioObj) {
		return DatabaseOperation.updateUsuarioDetailsInDB(updateUsuarioObj);
	}
	
	public String deleteUsuarioRecord(int usuarioId) {
		return DatabaseOperation.deleteUsuarioRecordInDB(usuarioId);
	}
	
	public void inicializar() {
		DatabaseOperation.inicializar();
	}

	
	
}
