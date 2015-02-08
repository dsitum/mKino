package hr.air.mkino.tipovi;

/**
 * Ova klasa predstavlja tip podataka koji opisuje pojedini multipleks. Izmeðu ostalih podataka se nalaze i svi getteri, dok se jednom uneseni podaci (koristeæi konstruktor) ne mogu više promijeniti 
 * @author domagoj
 *
 */
public class MultipleksInfo {
	private int idMultipleksa;
	private String naziv;
	private String oznaka;
	private float zemljopisnaDuzina;
	private float zemljopisnaSirina;
	
	public MultipleksInfo(int idMultipleksa, String naziv, String oznaka, float zemljopisnaDuzina, float zemljopisnaSirina) {
		this.idMultipleksa = idMultipleksa;
		this.naziv = naziv;
		this.oznaka = oznaka;
		this.zemljopisnaDuzina = zemljopisnaDuzina;
		this.zemljopisnaSirina = zemljopisnaSirina;
	}
	
	public int getIdMultipleksa() {
		return idMultipleksa;
	}
	public String getNaziv() {
		return naziv;
	}
	public String getOznaka() {
		return oznaka;
	}
	public float getZemljopisnaDuzina() {
		return zemljopisnaDuzina;
	}
	public float getZemljopisnaSirina() {
		return zemljopisnaSirina;
	}
}
