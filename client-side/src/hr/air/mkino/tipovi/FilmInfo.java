package hr.air.mkino.tipovi;

/**
 * Ova klasa predstavlja tip podataka koji opisuje pojedini film. Izmeðu ostalih podataka se nalaze i svi getteri, dok se jednom uneseni podaci (koristeæi konstruktor) ne mogu više promijeniti.
 * @author domagoj
 *
 */
public class FilmInfo {
	private int idFilma;
	private String naziv;
	private String opis;
	private String redatelj;
	private String glavneUloge;
	private int trajanje;
	private int godina;
	private String zanr;
	private int aktualno;
	
	public FilmInfo(int idFilma, String naziv, String opis, String redatelj, String glavneUloge, int trajanje, int godina, String zanr, int aktualno) {
		this.idFilma = idFilma;
		this.naziv = naziv;
		this.opis = opis;
		this.redatelj = redatelj;
		this.glavneUloge = glavneUloge;
		this.trajanje = trajanje;
		this.godina = godina;
		this.zanr = zanr;
		this.aktualno = aktualno;
	}

	public int getIdFilma() {
		return idFilma;
	}

	public String getNaziv() {
		return naziv;
	}

	public String getOpis() {
		return opis;
	}

	public String getRedatelj() {
		return redatelj;
	}

	public String getGlavneUloge() {
		return glavneUloge;
	}

	public int getTrajanje() {
		return trajanje;
	}

	public int getGodina() {
		return godina;
	}

	public String getZanr() {
		return zanr;
	}

	public int getAktualno() {
		return aktualno;
	}
}
