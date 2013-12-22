package hr.air.mkino.baza;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Ova klasa slu�i za dohva�anje i promjenu odabranog multipleksa. Odabrani multipleks mo�e biti samo jedan (samo jedan red u tablici), a njega odabire korisnik i prema njemu se filtriraju dostupni filmovi i projekcije
 * @author domagoj
 *
 */
public class OdabraniMultipleksAdapter {
	private static String DATABASE="baza";
	private static String TABLE="odabranimultipleks";
	private static int VERSION = 1;
	
	private DbHelper dbHelper;
	private SQLiteDatabase db;
	
	public OdabraniMultipleksAdapter(Context c) {
		dbHelper = new DbHelper(c, DATABASE, null, VERSION);
	}
	
	/**
	 * Vra�a informaciju o korisnikovom odabranom multipleksu
	 * @return odabrani multipleks
	 */
	public int dohvatiOdabraniMultipleks()
	{
		db = dbHelper.getReadableDatabase();
		
		String[] stupac = new String[] {"id"};
		Cursor c = db.query(TABLE, stupac, null, null, null, null, null);
		int id;
		
		if (c.getCount() > 0)
		{
			c.moveToFirst();
			id = c.getInt(c.getColumnIndex("id"));
		}
		else
		{
			id = -1;
		}
		
		dbHelper.close();
		return id;
	}
	
	/**
	 * Pohranjuje novo-odabrani multipleks. Ova metoda bri�e multipleks koji se do tada nalazio u lokalnoj bazi
	 * @param idOdabranogMultipleksa
	 * @return rezultat unosa
	 */
	public long pohraniOdabraniMultipleks(int idOdabranogMultipleksa)
	{		
		ContentValues redak = new ContentValues();
		redak.put("id", idOdabranogMultipleksa);
		
		db = dbHelper.getWritableDatabase();
		db.delete(TABLE, null, null);
		long rezultatUnosa = db.insert(TABLE, null, redak);
		dbHelper.close();
		
		return rezultatUnosa;
	}
}
