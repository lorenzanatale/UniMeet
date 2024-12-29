package model;

import java.sql.Date;

public class PrenotazioneRicevimento {
	
	private int codice;
	private boolean stato;
	private Date giorno;
	private String ora;
	private String codiceProfessore;
	private String matricolaStudente;
	private String nota;
	
	public PrenotazioneRicevimento(boolean stato,Date giorno,String ora,String nota, String codiceProfessore,String matricolaStudente) {
		this.stato=stato;
		this.giorno=giorno;
		this.ora=ora;
		this.nota=nota;
		this.codiceProfessore=codiceProfessore;
		this.matricolaStudente=matricolaStudente;
	}
	
	public int getCodice() {
		return codice;
	}
	public boolean getStato() {
		return stato;
	}
	public Date getGiorno() {
		return giorno;
	}
	public String getNota() {
		return nota;
	}
	public String getOra() {
		return ora;
	}
	public String getCodiceProfessore() {
		return codiceProfessore;
	}
	public String getMatricolaStudente() {
		return matricolaStudente;
	}
	
	public void setStato(boolean stato) {
		this.stato=stato;
	}
	public void setGiorno(Date giorno) {
		this.giorno=giorno;
	}
	public void setOra(String ora) {
		this.ora=ora;
	}
	public void setCodiceProfessore(String codiceProfessore) {
		this.codiceProfessore=codiceProfessore;
	}
	public void setMatricolaStudente(String matricolaStudente) {
		this.matricolaStudente=matricolaStudente;
	}
	public void setNota(String nota) {
		this.nota=nota;
	}
}
