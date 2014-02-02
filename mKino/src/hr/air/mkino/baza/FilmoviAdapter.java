package hr.air.mkino.baza;

import hr.air.mkino.sd.UpraviteljSDKartice;
import hr.air.mkino.tipovi.FilmInfo;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

/**
 * Ova klasa služi za dohvaæanje filmova iz lokalne baze podataka, dodavanje novih filmova i brisanje postojeæih. U ovoj bazi se uvijek trebaju nalaziti trenutno aktualni filmovi
 * @author domagoj
 *
 */
public class FilmoviAdapter {
	private static String DATABASE="baza";
	private static String TABLE="filmovi";
	private static int VERSION = 1;
	private static String KEY="idFilma";
	
	private DbHelper dbHelper;
	private SQLiteDatabase db;
	
	public FilmoviAdapter(Context c)
	{
		dbHelper = new DbHelper(c, DATABASE, null, VERSION);
	}
	
	/**
	 * Koristi se za dohvaæanje svih filmova iz lokalne baze. Dohvaæeni podaci služe za prikaz na zaslon
	 * @return lista filmova dohvaæenih iz lokalne baze
	 */
	public List<FilmInfo> dohvatiFilmove()
	{
		List<FilmInfo> filmovi = new ArrayList<FilmInfo>();
		String[] stupci = new String[]{KEY,"naziv","opis","redatelj","glavneUloge","trajanje","godina","zanr","aktualno"};
		db = dbHelper.getReadableDatabase();
		
		// dohvaæamo sve filmove iz baze. Raèunamo na to da su svi filmovi koji se veæ nalaze u lokalnoj bazi aktualni!
		Cursor c = db.query(TABLE, stupci, null, null, null, null, null);
		for(c.moveToFirst(); !(c.isAfterLast()); c.moveToNext())
		{
			int idFilma = c.getInt(c.getColumnIndex(KEY));
			String naziv = c.getString(c.getColumnIndex("naziv"));
			String opis = c.getString(c.getColumnIndex("opis"));
			String redatelj = c.getString(c.getColumnIndex("redatelj"));
			String glavneUloge = c.getString(c.getColumnIndex("glavneUloge"));
			int trajanje = c.getInt(c.getColumnIndex("trajanje"));
			int godina = c.getInt(c.getColumnIndex("godina"));
			String zanr = c.getString(c.getColumnIndex("zanr"));
			int aktualno = c.getInt(c.getColumnIndex("aktualno"));
			
			FilmInfo film = new FilmInfo(idFilma, naziv, opis, redatelj, glavneUloge, trajanje, godina, zanr, aktualno);
			filmovi.add(film);
		}
		dbHelper.close();
		return filmovi;
	}
	
	/**
	 * Koristi se za dohvaæanje identifikatora filmova. Ova funkcija može poslužiti kako bi pomogla da se ne dohvaæaju svi filmovi sa web servisa, nego samo oni koji æe poslije biti potrebni
	 * @return lista indeksa filmova iz baze podataka 
	 */
	public List<Integer> dohvatiIdFilmova()
	{
		List<Integer> indeksiFilmova = new ArrayList<Integer>();
		String[] stupac = new String[]{KEY};
		db = dbHelper.getReadableDatabase();
		Cursor c = db.query(TABLE, stupac, null, null, null, null, null);
		for(c.moveToFirst(); !(c.isAfterLast()); c.moveToNext())
		{
			int idFilma = c.getInt(c.getColumnIndex(KEY));			
			indeksiFilmova.add(idFilma);
		}
		dbHelper.close();
		return indeksiFilmova;
	}
	
	/**
	 * Dohvaæa detalje o filmu iz lokalne baze podataka.
	 * @param idFilma predstavlja film za koji se žele dohvatiti podaci.
	 * @return podaci (detalji) o filmu
	 */
	public FilmInfo dohvatiDetaljeFilma(int idFilma)
	{
		String[] stupci = new String[]{KEY,"naziv","opis","redatelj","glavneUloge","trajanje","godina","zanr","aktualno"};
		db = dbHelper.getReadableDatabase();
		Cursor c = db.query(TABLE, stupci, "idFilma = ?", new String[] {String.valueOf(idFilma)}, null, null, null);
		c.moveToFirst();
		
		String naziv = c.getString(c.getColumnIndex("naziv"));
		String opis = c.getString(c.getColumnIndex("opis"));
		String redatelj = c.getString(c.getColumnIndex("redatelj"));
		String glavneUloge = c.getString(c.getColumnIndex("glavneUloge"));
		int trajanje = c.getInt(c.getColumnIndex("trajanje"));
		int godina = c.getInt(c.getColumnIndex("godina"));
		String zanr = c.getString(c.getColumnIndex("zanr"));
		int aktualno = c.getInt(c.getColumnIndex("aktualno"));
		dbHelper.close();
		
		return new FilmInfo(idFilma, naziv, opis, redatelj, glavneUloge, trajanje, godina, zanr, aktualno);
	}
	
	/**
	 * Služi za ažuriranje trenutne baze filmova.
	 * Baza se ažurira pomoæu podataka primljenih sa web servisa.
	 * Pod pojmom ažuriranje se misli na dodavanje filmova koji ne postoje i brisanje onih koji više nisu aktualni
	 * @param noviFilmovi predstavlja nove filmove koje treba unijeti u bazu podataka.
	 */
	public void azurirajBazuFilmova(List<FilmInfo> noviFilmovi)
	{
		int idFilma;
		for (FilmInfo noviFilm : noviFilmovi)
		{
			idFilma = noviFilm.getIdFilma();
			// ako film ne postoji u bazi, dodajemo ga
			if (! filmPostojiUBazi(idFilma))
			{
				unosFilma(noviFilm);
			}
			else {
				// a ako film postoji u bazi, oèito je da se radi o filmu koji više nije aktualan (zato je web servis i vratio ponovno podatke o njemu), pa ga brišemo iz baze
				brisanjeFilma(idFilma);
				// takoðer brišemo i slike sa SD kartice
				String sdKartica = Environment.getExternalStorageDirectory().getPath();
				UpraviteljSDKartice.obrisiDatoteku(sdKartica + "/mKino/" + idFilma + ".jpg");
				UpraviteljSDKartice.obrisiDatoteku(sdKartica + "/mKino/" + idFilma + "_mala.jpg");
			}
		}
	}
	
	/**
	 * Za dani identifikacijski broj filma, vratit æe TRUE ako film postoji u lokalnoj bazi i FALSE ako ne postoji
	 * @param idFilma
	 * @return TRUE ili FALSE, ovisno o tome postoji li film u bazi
	 */
	private boolean filmPostojiUBazi(int idFilma)
	{
		String[] stupac = new String[]{KEY};
		db = dbHelper.getReadableDatabase();
		String[] whereArgument = {String.valueOf(idFilma)};
		Cursor c = db.query(TABLE, stupac, "idFilma = ?", whereArgument, null, null, null);
		
		boolean filmPostoji;
		if (c.getCount() > 0)
			filmPostoji = true;
		else
			filmPostoji = false;
		
		dbHelper.close();
		return filmPostoji;
	}
	
	/**
	 * Služi za unos novog filma u bazu podataka
	 * @param film
	 * @return rezultat unosa
	 */
	private long unosFilma(FilmInfo film)
	{
		ContentValues redak = new ContentValues();
		
		redak.put("idFilma", film.getIdFilma());
		redak.put("naziv", film.getNaziv());
		redak.put("opis", film.getOpis());
		redak.put("redatelj", film.getRedatelj());
		redak.put("glavneUloge", film.getGlavneUloge());
		redak.put("trajanje", film.getTrajanje());
		redak.put("godina", film.getGodina());
		redak.put("zanr", film.getZanr());
		redak.put("aktualno", film.getAktualno());
				
		db = dbHelper.getWritableDatabase();
		long rezultatUnosa = db.insert(TABLE, null, redak);
		dbHelper.close();
		return rezultatUnosa;
	}
	
	/**
	 * Briše film s identifikacijskim brojem "idFilma"
	 * @param idFilma
	 * @return broj obrisanih redaka iz baze 
	 */
	private long brisanjeFilma(int idFilma)
	{
		db = dbHelper.getWritableDatabase();
		String[] whereArgument = {String.valueOf(idFilma)};
		long rezultatBrisanja = db.delete(TABLE, "idFilma = ?", whereArgument);
		dbHelper.close();
		return rezultatBrisanja;
	}
}
