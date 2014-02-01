package hr.air.mkino.tipovi;

public class ProjekcijaInfo {
	private int idProjekcije;
	private int dvorana;
	private FilmInfo film;
	private String vrijemePocetka;
	private int multipleks;
	private float cijena;
	
	public ProjekcijaInfo(int idProjekcije, int dvorana, FilmInfo film, String vrijemePocetka, int multipleks, float cijena){
		this.idProjekcije = idProjekcije;
		this.dvorana = dvorana;
		this.film = film;
		this.vrijemePocetka = vrijemePocetka;
		this.multipleks = multipleks;
		this.cijena = cijena;
	}
	
	public float getCijena(){
		return cijena;
	}
	public int getidProjekcije(){
		return idProjekcije;
	}
	public int getDvorana(){
		return dvorana;
	}
	public String getVrijemePocetka()
	{
		return vrijemePocetka;
	}
	public int getMultipleks(){
		return multipleks;
	}
	
	public int getIdFilma() {
		return film.getIdFilma();
	}

	public String getNaziv() {
		return film.getNaziv();
	}

	public String getOpis() {
		return film.getOpis();
	}

	public String getRedatelj() {
		return film.getRedatelj();
	}

	public String getGlavneUloge() {
		return film.getGlavneUloge();
	}

	public int getTrajanje() {
		return film.getTrajanje();
	}

	public int getGodina() {
		return film.getGodina();
	}

	public String getZanr() {
		return film.getZanr();
	}

	public int getAktualno() {
		return film.getAktualno();
	}
}
