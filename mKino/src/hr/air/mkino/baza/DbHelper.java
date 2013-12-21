package hr.air.mkino.baza;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper{

	public DbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		List<String> tablice = new ArrayList<String>();
		tablice.add("CREATE TABLE IF NOT EXISTS multipleksi (idMultipleksa INTEGER PRIMARY KEY, naziv TEXT, oznaka TEXT, zemljopisnaDuzina REAL, zemljopisnaSirina REAL)");
		tablice.add("CREATE TABLE IF NOT EXISTS korisnik (idKorisnika INTEGER PRIMARY KEY, korisnickoIme TEXT, ime TEXT, prezime TEXT, email TEXT, telefon TEXT)");
		tablice.add("CREATE TABLE IF NOT EXISTS filmovi (idFilma INTEGER PRIMARY KEY, naziv TEXT, opis TEXT, glavneUloge TEXT, trajanje INTEGER, godina INTEGER, aktualno INTEGER, zanr TEXT)");
		
		for (String tablica : tablice)
			db.execSQL(tablica);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// mora biti ovdje
	}
}