package com.dietideals24.demo.models;

import com.dietideals24.demo.enums.TipoUtente;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
public class Utente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotEmpty(message = "Il campo username non può essere vuoto")
	private String username;
	@NotEmpty(message = "Il campo email non può essere vuoto")
	@Email(message = "Deve essere un indirizzo email valido")
	private String email;
	@NotEmpty(message = "Il campo password non può essere vuoto")
	@Size(min = 4, message = "La password deve contenere almeno 4 caratteri")
	private String password;
	private String biografia;
	private String sitoweb;
	private String paese;
	@Enumerated
	private TipoUtente tipo;
	private byte[] avatar;
	
	public Utente(String username, String email, String password) {
		this.username = username;
		this.password = password;
		this.email = email;
	}
	
	public Utente() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBiografia() {
		return biografia;
	}

	public void setBiografia(String biografia) {
		this.biografia = biografia;
	}

	public String getSitoweb() {
		return sitoweb;
	}

	public void setSitoweb(String sitoweb) {
		this.sitoweb = sitoweb;
	}

	public String getPaese() {
		return paese;
	}

	public void setPaese(String paese) {
		this.paese = paese;
	}

	public byte[] getAvatar() {
		return avatar;
	}

	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}

	public TipoUtente getTipo() {
		return tipo;
	}

	public void setTipo(TipoUtente tipo) {
		this.tipo = tipo;
	}
}