package hr.air.mkino.tipovi;
import java.util.List;

public class RezervacijaInfo {
	int idRezervacije;
	int idProjekcije;
	List<Integer> sjedala;
	String korisnickoIme;	
	String kodRezervacije;
	ProjekcijaInfo projekcija;
	
	public RezervacijaInfo(int idRezervacije, int idProjekcije, String korisnickoIme, String kodRezervacije, List<Integer> sjedala )
	{
		this.idRezervacije = idRezervacije;
		this.idProjekcije = idProjekcije;
		this.sjedala = sjedala;
		this.korisnickoIme = korisnickoIme;
		this.kodRezervacije = kodRezervacije;
		
	}
	public RezervacijaInfo(int idRezervacije, int idProjekcije, String korisnickoIme, String kodRezervacije, List<Integer> sjedala,ProjekcijaInfo projekcija)
	{
		this.idRezervacije = idRezervacije;
		this.idProjekcije = idProjekcije;
		this.sjedala = sjedala;
		this.korisnickoIme = korisnickoIme;
		this.kodRezervacije = kodRezervacije;
		this.projekcija = projekcija;
	}
	public String getNaziv()
	{
		return projekcija.getNaziv();
	}
	public int getDvorana()
	{
		return projekcija.getDvorana();
	}
	public String getVrijeme()
	{
		return projekcija.getVrijemePocetka();
	}
	public int getIdRezervacije()
	{
		return idRezervacije;
	}
	public int getIdProjekcije()
	{
		return idProjekcije;
	}
	public List<Integer> getSjedala()
	{
		return sjedala;
	}
	public String getKorisnickoIme()
	{
		 return korisnickoIme;
	}
	public String getKodRezervacije()
	{
		return kodRezervacije;
	}
}
