package hr.air.mkino.tipovi;

/*Klasa predstavlja složeni tip podataka koji predstavlja pojedinog korisnika aplikacije
 * */
public class Korisnik {
	private String korisnickoIme;
	private String lozinka;
	private String ime;
	private String prezime;
	private String email;
	private String telefon;

	//konstruktor 
	public Korisnik(String korisnickoIme, String lozinka, String ime,
			String prezime, String email, String telefon) {
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
		this.ime = ime;
		this.prezime = prezime;
		this.email = email;
		this.telefon = telefon;

	}
	
	public String getKorisnickoIme()
	{
		return korisnickoIme;
	}
	public String getLozinka()
	{
		return lozinka;
	}
	public String getIme()
	{
		return ime;
	}
	public String getPrezime()
	{
		return prezime;
	}
	public String getEmail()
	{
		return email;
	}
	public String getTelefon()
	{
		return telefon;
	}
	
}
