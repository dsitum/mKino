package hr.air.mkino.core;

import hr.air.mkino.R;
import hr.air.mkino.server.JsonRegistracija;
import hr.air.mkino.tipovi.Korisnik;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 * Klasa koja sadr�i metode za prikazivanje obrasca za registraciju
 * */
public class Registracija {
	/*konstante za ozna�avanje identifikacijskog broja pogre�ke prilikom registracije*/
	final static public  int USPJESNA_REGISTRACIJA = 0;   /*uspje�na registracija */
	final static public int PONOVLJENA_LOZINKA = 1;      /*ponovljena lozinka nije jednaka*/
	final static public int KORISNIK_POSTOJI = 4;        /*korisnik ve� postoji */
	final static public int EMAIL_POSTOJI = 5;           /*email ve� postoji*/
	final static public int TELEFON_POSTOJI = 6;		   /*telefonski broj ve� postoji */
	final static public int NEUSPJESNO_REG = 7;          /*neuspje�no dodavanje kojemu je razlog nepoznat*/
	final static public int KORIME_PRAZNO = 8;           /*korisni�ko ime je prazno */
	final static public int IME_PRAZNO = 9;              /*ime je prazno*/
	final static public int PREZIME_PRAZNO = 10;         /*prezime je prazno*/
	final static public int LOZINKA_PRAZNA = 11;         /*lozinka je prazna*/
	final static public int EMAIL_PRAZAN = 12;           /*email je prazan*/
	final static public int TELEFON_PRAZAN = 13;         /*telefon je prazan*/
	
	 
	/** 
	 * Metoda koja prikazuje obrazac za registraciju
	 * @param trenutni Context
	 * */
	public void prikaziDijalog(final Context context) {
		/*otvori dijalog za registraciju*/
		final Dialog dialogRegistracija = new Dialog(context);
		dialogRegistracija.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogRegistracija.setContentView(R.layout.dialog_registracija);
		dialogRegistracija.show();

		/*dohva�anje une�enih podataka*/
		Button btnRegistrirajSe = (Button) dialogRegistracija
				.findViewById(R.id.registracija_btnRegistrirajSe);
		Button btnOdustani = (Button) dialogRegistracija
				.findViewById(R.id.registracija_btnOdustani);

		final EditText txtKorisnickoIme = (EditText) dialogRegistracija
				.findViewById(R.id.dialog_registracija_txtKorisnickoIme);
		final EditText txtLozinka = (EditText) dialogRegistracija
				.findViewById(R.id.dialog_registracija_txtLozinka);
		final EditText txtPonovljenaLozinka = (EditText) dialogRegistracija
				.findViewById(R.id.dialog_registracija_txtPonovljenaLozinka);
		final EditText txtIme = (EditText) dialogRegistracija
				.findViewById(R.id.dialog_registracija_txtIme);
		final EditText txtPrezime = (EditText) dialogRegistracija
				.findViewById(R.id.dialog_registracija_txtPrezime);
		final EditText txtEmail = (EditText) dialogRegistracija
				.findViewById(R.id.dialog_registracija_txtEmail);
		final EditText txtTelefon = (EditText) dialogRegistracija
				.findViewById(R.id.dialog_registracija_txtTelefon);

		/*izlazak iz dijaloga*/
		btnOdustani.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogRegistracija.dismiss();
			}
		});
		
		/*registracija*/
		btnRegistrirajSe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*parsiranje korisni�kog unosa u formu*/
				String korisnickoIme = txtKorisnickoIme.getText().toString();
				String lozinka = txtLozinka.getText().toString();
				String ponovljenaLozinka = txtPonovljenaLozinka.getText().toString();
				String ime = txtIme.getText().toString();
				String prezime = txtPrezime.getText().toString();
				String email = txtEmail.getText().toString();
				String telefon = txtTelefon.getText().toString();			
				
				Korisnik korisnik = new Korisnik(korisnickoIme, lozinka, ime, prezime, email, telefon);	
				
				/*izvr�avanje registracije i obavje�tavanje korisnika o uspje�nosti*/
				switch(IzvrsiRegistraciju(korisnik, ponovljenaLozinka))
				{
					case USPJESNA_REGISTRACIJA: 
						Toast.makeText(context, "Uspje�na registracija!", Toast.LENGTH_SHORT).show();
						dialogRegistracija.dismiss();
						break;
					case PONOVLJENA_LOZINKA: 
						Toast.makeText(context, "Lozinke nisu jednake!", Toast.LENGTH_SHORT).show();
						break;				
					case KORISNIK_POSTOJI: 
						Toast.makeText(context, "Korisni�ko ime zauzeto!", Toast.LENGTH_SHORT).show();
						break;
					case EMAIL_POSTOJI: 
						Toast.makeText(context, "e-mail zauzet!", Toast.LENGTH_SHORT).show();
						break;
					case TELEFON_POSTOJI:
						Toast.makeText(context, "telefon zauzet!", Toast.LENGTH_SHORT).show();
						break;
					case NEUSPJESNO_REG: 
						Toast.makeText(context, "Registracija nije uspjela! Poku�ajte ponovno.", Toast.LENGTH_SHORT).show();
						break;
					case KORIME_PRAZNO: 
						Toast.makeText(context, "Molimo Vas, unesite korisni�ko ime!", Toast.LENGTH_SHORT).show();
						break;
					case IME_PRAZNO: 
						Toast.makeText(context, "Molimo Vas, unesite  ime!", Toast.LENGTH_SHORT).show();
						break;
					case PREZIME_PRAZNO: 
						Toast.makeText(context, "Molimo Vas, unesite prezime!", Toast.LENGTH_SHORT).show();
						break;
					case LOZINKA_PRAZNA: 
						Toast.makeText(context, "Molimo Vas, unesite lozinku!", Toast.LENGTH_SHORT).show();
						break;
					case EMAIL_PRAZAN: 
						Toast.makeText(context, "Molimo Vas, unesite email!", Toast.LENGTH_SHORT).show();
						break;
					case TELEFON_PRAZAN: 
						Toast.makeText(context, "Molimo Vas, unesite telefon!", Toast.LENGTH_SHORT).show();
						break;
					default: 
						Toast.makeText(context, "Registracija nije uspjela! Poku�ajte ponovno.", Toast.LENGTH_SHORT).show();
						break;
				
				}
								
			}
		});

	}

	/**
	 * Metoda koja izvr�ava registraciju korisnika putem POST metode. 
	 * Prvo se podaci validiraju na korisni�koj strani, a
	 * ukoliko do�e do pogre�ke vra�a se broj pogre�ke ovisno o odgovoru kojeg
	 * vra�a servis prilikom validacije i unosa u bazu
	 * 
	 * @param objekt tipka Korisnik sa popunjenim podacima i ponovljena lozinka za validaciju
	 * @return 
	 *	 USPJESNA_REGISTRACIJA = 0;  
	 *	 PONOVLJENA_LOZINKA = 1;
	 *	 KORISNIK_POSTOJI = 4;
	 *	 EMAIL_POSTOJI = 5;
	 *	 TELEFON_POSTOJI = 6;
	 *	 NEUSPJESNO_REG = 7;
	 *	 KORIME_PRAZNO = 8;
	 *	 IME_PRAZNO = 9;
	 *	 PREZIME_PRAZNO = 10;
	 *	 LOZINKA_PRAZNA = 11;
	 *	 EMAIL_PRAZAN = 12;
	 * 	 TELEFON_PRAZAN = 13;
	 */
	public int IzvrsiRegistraciju(Korisnik korisnik, String ponovljenaLozinka) {
		
		/*validacija korisni�kog unosa*/
		if (korisnik.getKorisnickoIme().length() == 0 || korisnik.getKorisnickoIme() == null) 
			return 8;
		if (ponovljenaLozinka.length() == 0 || ponovljenaLozinka == null)
			return 11;
		if(korisnik.getLozinka().length() == 0 || korisnik.getLozinka() == null)
			return 11;
		if (korisnik.getIme().length() == 0 || korisnik.getIme() == null)
			return 9;
		if (korisnik.getPrezime().length() == 0 || korisnik.getPrezime() == null)
			return 10;
		if (korisnik.getLozinka().length() == 0 || korisnik.getLozinka() == null)
			return 11;		
		if (korisnik.getEmail().length() == 0 || korisnik.getEmail() == null)
			return 12;
		if(korisnik.getTelefon().length() == 0 || korisnik.getTelefon() == null)
			return 13;		
		if(!ponovljenaLozinka.equals(korisnik.getLozinka()))
			return 1;
		
		JsonRegistracija jsonReg = new JsonRegistracija();
		/*provjera na korisni�koj strani je uspje�no izvr�ena, izvr�avamo registraciju putem post metode*/
		int uspjesnaRegistracija = jsonReg.registriraj(korisnik);
		
		return uspjesnaRegistracija;
	}

}
