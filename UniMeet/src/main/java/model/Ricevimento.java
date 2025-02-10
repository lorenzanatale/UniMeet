package model;

import java.sql.Date;


//giorno qui è  il giorno specifico nella classe 

public class Ricevimento {
	private String giorno;
	private String ora;
	private int codice;
	private String note;
	private String codiceProfessore;
	
	
	//Giornoin nel costruttore prende in Input
	public Ricevimento(int codice, String giornoIn,String oraIn,String noteIn,String codiceProfessoreIn) {
		this.codice = codice;
		this.giorno=giornoIn;
		this.ora=oraIn;
		this.note=noteIn;
		this.codiceProfessore=codiceProfessoreIn;
	}
	public String getGiorno() {
		return giorno;
	}
	public String getOra() {
		return ora;
	}
	public int getCodice() {
		return codice;
	}
	public String getNote() {
		return note;
	}
	public String getCodiceProfessore() {
		return codiceProfessore;
	}
	public void setData(String giornoIn){
		this.giorno=giornoIn;
	}
	public void setOra(String oraIn) {
		this.ora=oraIn;
	}
	public void setNote(String note) {
		this.note=note;
	}
	public void setCodiceProfessore(String codiceProfessore2) {
		this.codiceProfessore=codiceProfessore2;
		
	}
	public void setGiorno(String giorno) {
		 this.giorno=giorno;
		
	}
	public void setCodice(int codice) {
		this.codice=codice;
	}

}
