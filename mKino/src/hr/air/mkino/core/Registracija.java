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

		// dohvaæanje unešenih podataka

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
				// pozovi metodu za izvršavanje registracije
				String korisnickoIme = txtKorisnickoIme.getText().toString();
				String lozinka = txtLozinka.getText().toString();
				String ponovljenaLozinka = txtPonovljenaLozinka.getText().toString();
				String ime = txtIme.getText().toString();
				String prezime = txtPrezime.getText().toString();
				String email = txtEmail.getText().toString();
				String telefon = txtTelefon.getText().toString();
				
				//izvršavanje registracije i obavještravanje korisnika o uspješnosti
				Korisnik korisnik = new Korisnik(korisnickoIme, lozinka, ime, prezime, email, telefon);
				
//samo probno postavljanje korisnika kao logiranoG!!!
				Prijava.prijavljeniKorisnik = korisnik;
				
				
				switch(IzvrsiRegistraciju(korisnik, ponovljenaLozinka))
				{
					case 0: 
						Toast.makeText(context, "Uspješna registracija!", Toast.LENGTH_SHORT).show();
						dialogRegistracija.dismiss();
						break;
					case 1: 
						Toast.makeText(context, "Lozinke nisu jednake!", Toast.LENGTH_SHORT).show();
						break;
				
					case 4: 
						Toast.makeText(context, "Korisnièko ime zauzeto!", Toast.LENGTH_SHORT).show();
						break;
					case 5: 
						Toast.makeText(context, "e-mail zauzet!", Toast.LENGTH_SHORT).show();
						break;
					case 6:
						Toast.makeText(context, "Uspješna registracija!", Toast.LENGTH_SHORT).show();
						break;
					case 7: 
						Toast.makeText(context, "Registracija nije uspjela! Pokušajte ponovno.", Toast.LENGTH_SHORT).show();
						break;
					case 8: 
						Toast.makeText(context, "Molimo Vas, unesite korisnièko ime!", Toast.LENGTH_SHORT).show();
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
						Toast.makeText(context, "Registracija nije uspjela! Pokušajte ponovno.", Toast.LENGTH_SHORT).show();
						break;
				
				}
								
			}
		});

	}

	/*
	 * metoda koja izvršava registraciju korisnika putem POST metode šalje
	 * podatke na servis prvo se podaci validiraju na korisnièkoj strani, a
	 * ukolko doðe do pogreške vraæa se broj pogreške ovisno o odgovoru kojeg
	 * dobije od servisa vraæa se rezultat unosa u bazu podataka
	 * 
	 @return 
	 * 0 - uspješna registracija 
	 * 1 - ponovljena lozinka nije jednaka
	 * 2 -
	 * 3 - 
	 * 4 - korisnik veæ postoji 
	 * 5 - email veæ postoji 
	 * 6 - telefonski broj
	 * veæ postoji 
	 * 7 - neuspješno dodavanje, razlog nepoznat
	 * 8 - korisnièko ime je prazno 
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
		//provjera na korisnièkoj strani je uspješno izvršena, izvršavamo registraciju putem post metode
		int uspjesnaRegistracija = jsonReg.registriraj(korisnik);
		
		
		return uspjesnaRegistracija;
	}

}
