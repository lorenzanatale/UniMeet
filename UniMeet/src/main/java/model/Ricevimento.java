package model;

import java.sql.Date;

public class Ricevimento {
	private Date data;
	private String ora;
	private int codice;
	private String note;
	private String codiceProfessore;
	
	
	public Ricevimento(Date dataIn,String oraIn,String noteIn,String codiceProfessoreIn) {
		this.data=dataIn;
		this.ora=oraIn;
		this.note=noteIn;
		this.codiceProfessore=codiceProfessoreIn;
	}
	public Date getGiorno() {
		return data;
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
	public void setData(Date dataIn){
		this.data=dataIn;
	}
	public void setOra(String oraIn) {
		this.ora=oraIn;
	}
	public void setNote(String codiceProfessoreIn) {
		this.codiceProfessore=codiceProfessoreIn;
	}

}
