package hr.air.mkino.baza;

import hr.air.mkino.tipovi.FilmInfo;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Ova klasa slu�i za dohva�anje filmova iz lokalne baze podataka, dodavanje novih filmova i brisanje postoje�ih. U ovoj bazi se uvijek trebaju nalaziti trenutno aktualni filmovi
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
	 * Koristi se za dohva�anje svih filmova iz lokalne baze. Dohva�eni podaci slu�e za prikaz na zaslon
	 * @return
	 */
	public List<FilmInfo> dohvatiFilmove()
	{
		List<FilmInfo> filmovi = new ArrayList<FilmInfo>();
		String[] stupci = new String[]{KEY,"naziv","opis","redatelj","glavneUloge","trajanje","godina","zanr","aktualno"};
		db = dbHelper.getReadableDatabase();
		
		// dohva�amo sve filmove iz baze. Ra�unamo na to da su svi filmovi koji se ve� nalaze u lokalnoj bazi aktualni!
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
	 * Koristi se za dohva�anje identifikatora filmova. Ova funkcija mo�e poslu�iti kako bi pomogla da se ne dohva�aju svi filmovi sa web servisa, nego samo oni koji �e poslije biti potrebni 
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
	 * Slu�i za unos novog filma u bazu podataka
	 * @param film
	 * @return rezultat unosa
	 */
	public long unosFilma(FilmInfo film)
	{
		ContentValues redak = new ContentValues();
		
		redak.put("idFilma", film.getIdFilma());
		redak.put("naziv", film.getNaziv());
		redak.put("opis", film.getNaziv());
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
}
