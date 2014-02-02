package hr.air.mkino.baza;

import hr.air.mkino.tipovi.FilmInfo;
import hr.air.mkino.tipovi.ProjekcijaInfo;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/**
 * Klasa koja služi za manipulaciju projekcijama u lokalnoj bazi.
 * Glavni razlog korištenja ove klase je cahcing podataka.
 * @author bstivic
 *
 */
public class ProjekcijeAdapter {
	private static String DATABASE="baza";
	private static String TABLE="projekcije";
	private static int VERSION = 1;
	private static String KEY="idProjekcije";
	
	private DbHelper dbHelper;
	private SQLiteDatabase db;
	
	public ProjekcijeAdapter(Context c)
	{
		dbHelper = new DbHelper(c, DATABASE, null, VERSION);
	}
	/**
	 * Koristi se za dohvaæanje svih projekcija iz lokalne baze za odreðeni multipleks. Dohvaæeni podaci služe za prikaz na zaslon
	 * @param id multipleksa, trenutni kontekst koji služi za DBHelper
	 * @return lista projekcija
	 */
	public List<ProjekcijaInfo> dohvatiProjekcije(int multipleks, Context con)
	{
		List<ProjekcijaInfo> projekcije = new ArrayList<ProjekcijaInfo>();
		String[] stupci = new String[]{KEY,"dvorana","film","vrijemePocetka","cijena"};
		db = dbHelper.getReadableDatabase();
		FilmoviAdapter filmAd = new FilmoviAdapter(con);
		
		// dohvaæamo sve projekcije iz baze. 
		/*TODO stavi uvjet da su projekcije aktualne!!*/
		Cursor c = db.query(TABLE, stupci,  "multipleks = ?", new String[] {String.valueOf(multipleks)}, null, null, "vrijemePocetka");
		for(c.moveToFirst(); !(c.isAfterLast()); c.moveToNext())
		{
			int idProjekcije = c.getInt(c.getColumnIndex(KEY));
			int dvorana = c.getInt(c.getColumnIndex("dvorana"));
			int idFilma = c.getInt(c.getColumnIndex("film"));
			String vrijemePocetka = c.getString(c.getColumnIndex("vrijemePocetka"));
			int cijena = c.getInt(c.getColumnIndex("cijena"));
			
				
			FilmInfo film = filmAd.dohvatiDetaljeFilma(idFilma);				
			ProjekcijaInfo projekcija = new ProjekcijaInfo(idProjekcije, dvorana, film, vrijemePocetka, multipleks, cijena); 			
			projekcije.add(projekcija);
		}
		dbHelper.close();
		return projekcije;
	}
	
	/**
	 * Metoda koja služi za dohvaæanje projekcije iz baze podataka.
	 * @param id projekcije
	 * @param context
	 * @return projekcija
	 */
	public ProjekcijaInfo dohvatiProjekciju(int id, Context con)
	{
		ProjekcijaInfo projekcija = null;
		String[] stupci = new String[]{KEY,"dvorana","film","vrijemePocetka","cijena", "multipleks"};
		db = dbHelper.getReadableDatabase();
		FilmoviAdapter filmAd = new FilmoviAdapter(con);
		
		// dohvaæamo projekciju iz baze koja ima traženi Id 

		Cursor c = db.query(TABLE, stupci,  "idProjekcije = ?", new String[] {String.valueOf(id)}, null, null, null);
		for(c.moveToFirst(); !(c.isAfterLast()); c.moveToNext())
		{
			int idProjekcije = c.getInt(c.getColumnIndex(KEY));
			int dvorana = c.getInt(c.getColumnIndex("dvorana"));
			int idFilma = c.getInt(c.getColumnIndex("film"));
			String vrijemePocetka = c.getString(c.getColumnIndex("vrijemePocetka"));
			int cijena = c.getInt(c.getColumnIndex("cijena"));
			int multipleks = c.getInt(c.getColumnIndex("multipleks"));
				
			FilmInfo film = filmAd.dohvatiDetaljeFilma(idFilma);				
			projekcija = new ProjekcijaInfo(idProjekcije, dvorana, film, vrijemePocetka, multipleks, cijena); 			
			
		}
		dbHelper.close();
		return projekcija;
	}
	/**
	 * Služi za ažuriranje trenutne baze projekcija.
	 * Baza se ažurira pomoæu podataka primljenih sa web servisa.
	 * Pod pojmom ažuriranje se misli na dodavanje projekcija koji ne postoje i brisanje onih koji više nisu aktualne
	 */
	public void azurirajBazuProjekcija(List<ProjekcijaInfo> noveProjekcije)
	{
		int idProjekcije;
		for (ProjekcijaInfo novaProjekcija : noveProjekcije)
		{
			idProjekcije = novaProjekcija.getIdFilma();
			// ako film ne postoji u bazi, dodajemo ga
			if (! projekcijaPostojiUBazi(idProjekcije))
			{
				unosProjekcije(novaProjekcija);
			}
			else {
				// a ako projekcija postoji u bazi, oèito je da se radi o projekciji koja više nije aktualna
				brisanjeProjekcija(idProjekcije);
			}
		}
	}
	/**
	 * Koristi se za dohvaæanje identifikatora projekcija.
	 * Ova funkcija može poslužiti kako bi pomogla da se ne dohvaæaju svi projekcija sa web servisa.
	 */
	public List<Integer> dohvatiIdProjekcija()
	{
		List<Integer> indeksiProjekcija = new ArrayList<Integer>();
		String[] stupac = new String[]{KEY};
		db = dbHelper.getReadableDatabase();
		Cursor c = db.query(TABLE, stupac, null, null, null, null, null);
		for(c.moveToFirst(); !(c.isAfterLast()); c.moveToNext())
		{
			int idProjekcije = c.getInt(c.getColumnIndex(KEY));			
			indeksiProjekcija.add(idProjekcije);
		}
		dbHelper.close();
		return indeksiProjekcija;
	}
	
	/**
	 * Za dani identifikacijski broj projekcije, vratit æe TRUE ako projekcija postoji u lokalnoj bazi i FALSE ako ne postoji
	 * @param idProjekcije
	 * @return TRUE ili FALSE, ovisno o tome postoji li projekcija u bazi
	 */
	private boolean projekcijaPostojiUBazi(int idProjekcije)
	{
		String[] stupac = new String[]{KEY};
		db = dbHelper.getReadableDatabase();
		String[] whereArgument = {String.valueOf(idProjekcije)};
		Cursor c = db.query(TABLE, stupac, "idProjekcije = ?", whereArgument, null, null, null);
		
		boolean projekcijaPostoji;
		if (c.getCount() > 0)
			projekcijaPostoji = true;
		else
			projekcijaPostoji = false;
		
		dbHelper.close();
		return projekcijaPostoji;
	}
	
	/**
	 * Služi za unos nove projekcije u bazu podataka
	 * @param Projekcija
	 * @return broj zahvaæenih redaka
	 */
	private long unosProjekcije(ProjekcijaInfo	projekcija)
	{
		ContentValues redak = new ContentValues();
		
		redak.put("idProjekcije", projekcija.getidProjekcije());
		redak.put("dvorana", projekcija.getDvorana());
		redak.put("film", projekcija.getIdFilma());
		redak.put("vrijemePocetka", projekcija.getVrijemePocetka());
		redak.put("cijena", projekcija.getCijena());
		redak.put("multipleks", projekcija.getMultipleks());

						
		db = dbHelper.getWritableDatabase();
		long rezultatUnosa = db.insert(TABLE, null, redak);
		dbHelper.close();
		return rezultatUnosa;
	}
	
	/**
	 * Briše projekciju s identifikacijskim brojem "idProjekcije"
	 * @param idProjekcije
	 * @return broj zahvaæenih redaka
	 */
	private long brisanjeProjekcija(int idProjekcije)
	{
		db = dbHelper.getWritableDatabase();
		String[] whereArgument = {String.valueOf(idProjekcije)};
		long rezultatBrisanja = db.delete(TABLE, "idProjekcije = ?", whereArgument);
		dbHelper.close();
		return rezultatBrisanja;
	}
		
	
	
}
