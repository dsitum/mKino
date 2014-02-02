package hr.air.mkino.baza;

import hr.air.mkino.tipovi.Korisnik;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Klasa koja služi za dohvaæanje, spremanje i brisanje podataka o prijavljenom korisniku u bazi.
 * Postoji samo jedan redak u tablici prijavljenikorisnik.
 */
public class PrijavljeniKorisnikAdapter {
	
		private static String DATABASE="baza";
		private static String TABLE="prijavljenikorisnik";
		private static int VERSION = 1;
		
		private DbHelper dbHelper;
		private SQLiteDatabase db;
		
		public PrijavljeniKorisnikAdapter(Context c) {
			dbHelper = new DbHelper(c, DATABASE, null, VERSION);
		}
		
		/**
		 * Metoda koja služi za dohvaæanje podataka o prijavljenom korisniku iz lokalne baze.		 * 
		 * @return objekt Korisnik("korisnickoIme", "lozinka",  null, null, null, nulls)
		 */
		public Korisnik dohvatiPrijavljenogKorisnika()
		{
			db = dbHelper.getReadableDatabase();			
			String[] stupac = new String[] {"korisnickoIme", "lozinka"};
		
			Cursor c = db.query(TABLE, stupac, null, null, null, null, null);
			String  korisnickoIme;
			String lozinka;
			Korisnik korisnik;
			/*ako tablica "prijavljenikorisnik" nije prazna*/
			if (c.getCount() > 0)
			{
				c.moveToFirst();
				korisnickoIme = c.getString(c.getColumnIndex("korisnickoIme"));
				lozinka = c.getString(c.getColumnIndex("lozinka"));
				
				korisnik = new Korisnik(korisnickoIme, lozinka, null, null, null, null);
			}
			else
			{
				korisnik = null;
			}
			
			dbHelper.close();
			return korisnik;
		}
		
		/**
		 * Pohranjuje korisnièke podatke u lokalnu bazu prilikom prijave u aplikaciju 
		 * @param korisnièko ime i lozinka
		 * @return rezultat unosa u bazu podataka
		 */
		public long pohraniKorisnickePodatke(String korisnickoIme, String lozinka)
		{		
			ContentValues redak = new ContentValues();
			redak.put("korisnickoIme", korisnickoIme);
			redak.put("lozinka", lozinka);
			
			db = dbHelper.getWritableDatabase();
			db.delete(TABLE, null, null);
			long rezultatUnosa = db.insert(TABLE, null, redak);
			dbHelper.close();
			
			return rezultatUnosa;
		}
	
		/**brisanje retka u tablici prijavljenikorisnik što je ekvivalentno odjavi iz aplikacije*/
		public void obrisiPrijavljenogKorisnika()
		{
			db = dbHelper.getWritableDatabase();
			db.delete(TABLE, null, null);
			dbHelper.close();
		}
}
