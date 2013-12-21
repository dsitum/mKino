package hr.air.mkino.baza;

import java.util.ArrayList;
import java.util.List;
import hr.air.mkino.tipovi.MultipleksInfo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MultipleksAdapter {

	private static String DATABASE="baza";
	private static String TABLE="multipleksi";
	private static int VERSION = 1;
	private static String KEY="idMultipleksa";
	
	private DbHelper dbHelper;
	private SQLiteDatabase db;
	
	public MultipleksAdapter(Context c)
	{
		dbHelper=new DbHelper(c, DATABASE, null, VERSION);
	}
	
	public List<MultipleksInfo> dohvatiMultiplekse()
	{
		List<MultipleksInfo> multipleksi = new ArrayList<MultipleksInfo>();
		String[] stupci = new String[]{KEY,"naziv","oznaka","zemljopisnaDuzina","zemljopisnaSirina"};
		db = dbHelper.getReadableDatabase();
		
		Cursor c = db.query(TABLE, stupci, null, null, null, null, null);
		for(c.moveToFirst(); !(c.isAfterLast()); c.moveToNext())
		{
			int id = c.getInt(c.getColumnIndex(KEY));
			String naziv = c.getString(c.getColumnIndex("naziv"));
			String oznaka = c.getString(c.getColumnIndex("oznaka"));
			float zemljopisnaDuzina = c.getFloat(c.getColumnIndex("zemljopisnaDuzina"));
			float zemljopisnaSirina = c.getFloat(c.getColumnIndex("zemljopisnaSirina"));
			MultipleksInfo multipleks = new MultipleksInfo(id, naziv, oznaka , zemljopisnaDuzina, zemljopisnaSirina);
			multipleksi.add(multipleks);
		}
		dbHelper.close();
		return multipleksi;
	}
	
	public long unosMultipleksa(MultipleksInfo multipleks)
	{
		ContentValues redak = new ContentValues();
		
		redak.put("idMultipleksa", multipleks.getIdMultipleksa());
		redak.put("naziv", multipleks.getNaziv());
		redak.put("oznaka", multipleks.getOznaka());
		redak.put("zemljopisnaDuzina", multipleks.getZemljopisnaDuzina());
		redak.put("zemljopisnaSirina", multipleks.getZemljopisnaSirina());
		
		db = dbHelper.getWritableDatabase();
		long rezultatUnosa = db.insert(TABLE, null, redak);
		dbHelper.close();
		return rezultatUnosa;
	}
}