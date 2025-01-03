package model;

import java.sql.Date;

public class Ricevimento {
	private Date giorno;
	private String ora;
	private int codice;
	private String note;
	private String codiceProfessore;
	
	
	public Ricevimento(Date giornoIn,String oraIn,String noteIn,String codiceProfessoreIn) {
		this.giorno=giornoIn;
		this.ora=oraIn;
		this.note=noteIn;
		this.codiceProfessore=codiceProfessoreIn;
	}
	public Date getGiorno() {
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
	public void setData(Date giornoIn){
		this.giorno=giornoIn;
	}
	public void setOra(String oraIn) {
		this.ora=oraIn;
	}
	public void setNote(String codiceProfessoreIn) {
		this.codiceProfessore=codiceProfessoreIn;
	}

}
