package model;

public class Professore {
	private String nome;
	private String cognome;
	private String email;
	private String password;
	private String codiceProfessore;
	
	public Professore() {
		
	}

	public Professore(String nome, String cognome, String email, String password, String codiceProfessore) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
		this.password = password;
		this.codiceProfessore = codiceProfessore;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
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

	public String getCodiceProfessore() {
		return codiceProfessore;
	}

	public void setCodiceProfessore(String codiceProfessore) {
		this.codiceProfessore = codiceProfessore;
	}
}
