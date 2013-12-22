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

public class Registracija {

	// metoda koja prikazuje dijalog za registraciju
	public void prikaziDijalog(final Context context) {
		// otvori dijalog za registraciju
		final Dialog dialogRegistracija = new Dialog(context);
		dialogRegistracija.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogRegistracija.setContentView(R.layout.dialog_registracija);
		dialogRegistracija.show();

		// dohva�anje une�enih podataka

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

		// izlazimo iz dijaloga prilikom pritiska na odustani
		btnOdustani.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogRegistracija.dismiss();
			}
		});

		btnRegistrirajSe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// pozovi metodu za izvr�avanje registracije
				String korisnickoIme = txtKorisnickoIme.getText().toString();
				String lozinka = txtLozinka.getText().toString();
				String ponovljenaLozinka = txtPonovljenaLozinka.getText().toString();
				String ime = txtIme.getText().toString();
				String prezime = txtPrezime.getText().toString();
				String email = txtEmail.getText().toString();
				String telefon = txtTelefon.getText().toString();
				
				//izvr�avanje registracije i obavje�travanje korisnika o uspje�nosti
				Korisnik korisnik = new Korisnik(korisnickoIme, lozinka, ime, prezime, email, telefon);
				
//samo probno postavljanje korisnika kao logiranoG!!!
				Prijava.prijavljeniKorisnik = korisnik;
				
				
				switch(IzvrsiRegistraciju(korisnik, ponovljenaLozinka))
				{
					case 0: 
						Toast.makeText(context, "Uspje�na registracija!", Toast.LENGTH_SHORT).show();
						dialogRegistracija.dismiss();
						break;
					case 1: 
						Toast.makeText(context, "Lozinke nisu jednake!", Toast.LENGTH_SHORT).show();
						break;
				
					case 4: 
						Toast.makeText(context, "Korisni�ko ime zauzeto!", Toast.LENGTH_SHORT).show();
						break;
					case 5: 
						Toast.makeText(context, "e-mail zauzet!", Toast.LENGTH_SHORT).show();
						break;
					case 6:
						Toast.makeText(context, "Uspje�na registracija!", Toast.LENGTH_SHORT).show();
						break;
					case 7: 
						Toast.makeText(context, "Registracija nije uspjela! Poku�ajte ponovno.", Toast.LENGTH_SHORT).show();
						break;
					case 8: 
						Toast.makeText(context, "Molimo Vas, unesite korisni�ko ime!", Toast.LENGTH_SHORT).show();
						break;
					case 9: 
						Toast.makeText(context, "Molimo Vas, unesite  ime!", Toast.LENGTH_SHORT).show();
						break;
					case 10: 
						Toast.makeText(context, "Molimo Vas, unesite prezime!", Toast.LENGTH_SHORT).show();
						break;
					case 11: 
						Toast.makeText(context, "Molimo Vas, unesite lozinku!", Toast.LENGTH_SHORT).show();
						break;
					case 12: 
						Toast.makeText(context, "Molimo Vas, unesite email!", Toast.LENGTH_SHORT).show();
						break;
					case 13: 
						Toast.makeText(context, "Molimo Vas, unesite telefon!", Toast.LENGTH_SHORT).show();
						break;
					default: 
						Toast.makeText(context, "Registracija nije uspjela! Poku�ajte ponovno.", Toast.LENGTH_SHORT).show();
						break;
				
				}
								
			}
		});

	}

	/*
	 * metoda koja izvr�ava registraciju korisnika putem POST metode �alje
	 * podatke na servis prvo se podaci validiraju na korisni�koj strani, a
	 * ukolko do�e do pogre�ke vra�a se broj pogre�ke ovisno o odgovoru kojeg
	 * dobije od servisa vra�a se rezultat unosa u bazu podataka
	 * 
	 @return 
	 * 0 - uspje�na registracija 
	 * 1 - ponovljena lozinka nije jednaka
	 * 2 -
	 * 3 - 
	 * 4 - korisnik ve� postoji 
	 * 5 - email ve� postoji 
	 * 6 - telefonski broj
	 * ve� postoji 
	 * 7 - neuspje�no dodavanje, razlog nepoznat
	 * 8 - korisni�ko ime je prazno 
	 * 9 - ime je prazno 
	 * 10 - prezime je prazno 
	 * 11 - lozinka je prazna
	 * 12 - email je prazan 
	 * 13 - telefon je prazan
	 */
	public int IzvrsiRegistraciju(Korisnik korisnik, String ponovljenaLozinka) {
		
		
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
		//provjera na korisni�koj strani je uspje�no izvr�ena, izvr�avamo registraciju putem post metode
		int uspjesnaRegistracija = jsonReg.registriraj(korisnik);
		
		
		return uspjesnaRegistracija;
	}

}
