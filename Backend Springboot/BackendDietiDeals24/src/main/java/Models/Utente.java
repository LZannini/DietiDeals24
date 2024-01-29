package Models;

import jakarta.persistence.*;

@Entity
public class Utente {
	
	private @Id int id;
	private String username;
	private String email;
	private String password;
	private String biografia;
	private String sitoweb;
	private String paese;
	private String tipo;
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

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
}