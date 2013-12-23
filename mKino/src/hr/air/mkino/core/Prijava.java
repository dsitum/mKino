package hr.air.mkino.core;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import hr.air.mkino.R;
import hr.air.mkino.baza.PrijavljeniKorisnikAdapter;
import hr.air.mkino.server.JsonPrijava;
import hr.air.mkino.tipovi.Korisnik;

/**klasa poslovne logike koja služi za prikaz dijaloga prijave i izvršavanje prijave*/
public class Prijava {
	
	/**
	 * Metoda koja prikazuje dijalog za prijavu u aplikaciju
	 * @param aktivni Context
	 * */
	public void prikaziDijalog(final Context context) {
			final Dialog dialogPrijava = new Dialog(context);
			dialogPrijava.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogPrijava.setContentView(R.layout.dialog_prijava);			
			dialogPrijava.show();
			
			/*dohvaæanje unešenih podataka*/			
			Button btnPrijaviSe = (Button)dialogPrijava.findViewById(R.id.dialog_prijava_btnPrijaviSe);
			Button btnOdustani = (Button) dialogPrijava.findViewById(R.id.dijalog_prijava_btnOdustani);
			Button btnRegistracija = (Button)dialogPrijava.findViewById(R.id.prijava_btnRegistrirajSe);
			
			final EditText txtKorisnickoIme = (EditText) dialogPrijava.findViewById(R.id.dialog_registracija_txtKorisnickoIme);
			final EditText txLozinka = (EditText)dialogPrijava.findViewById(R.id.dialog_registracija_txtLozinka);
			
			/*odustani od prijave*/
			btnOdustani.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialogPrijava.dismiss();

					}
				});
					
			/*klik na prijavu*/
			btnPrijaviSe.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {				
					String tKorisnickoIme= txtKorisnickoIme.getText().toString();
					String tLozinka = txLozinka.getText().toString();
					
					/*validacija na klijentskoj strani*/
					if(tKorisnickoIme == null || tKorisnickoIme.length() ==0)
						Toast.makeText(context, "Unesite korisnièko ime", Toast.LENGTH_SHORT).show();
					
					else if(tLozinka == null || tLozinka.length() ==0)
						Toast.makeText(context, "Unesite lozinku", Toast.LENGTH_SHORT).show();
					
					else{
						JsonPrijava novaPrijava = new JsonPrijava();
						/*instanciranje objekta Korisnik koji ce sluziti za prijavu*/
						Korisnik prijava = novaPrijava.prijavi(tKorisnickoIme, tLozinka);
						
						/*uspješna prijava*/
						if(prijava != null)
						{
							Toast.makeText(context, "Uspješno ste prijavljeni!", Toast.LENGTH_SHORT).show();
							PrijavljeniKorisnikAdapter prijavljeniKorisnikAdapter = new PrijavljeniKorisnikAdapter(context);
							if(prijavljeniKorisnikAdapter.pohraniKorisnickePodatke(tKorisnickoIme, tLozinka) > 	0)
								Toast.makeText(context, "zapamtio u bazu", Toast.LENGTH_SHORT);
							dialogPrijava.dismiss();
						}
						else
						{
							/*neuspješna prijava*/
							Toast.makeText(context, "Neuspješna prijava!", Toast.LENGTH_SHORT).show();
						}
					}							
				}
			});
			
			/*klik na registraciju koji otvara dijalog za registraciju korisnika*/
			btnRegistracija.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					/*zatvaramo dijalog za prijavu*/
					dialogPrijava.dismiss();
					
					/*prikazujemo dijalog za registraciju*/
					Registracija registracije = new Registracija();
					registracije.prikaziDijalog(context);					
				}				
			});
		}
	
	/**
	 * Metoda koja odjavljuje korisnika iz aplikacije
	 * @param trenutni Context
	 * */
	public void odjava(Context context)
	{
		PrijavljeniKorisnikAdapter prijavljeniKorisnik = new PrijavljeniKorisnikAdapter(context);
		/*brišemo prijavljenog korisnika iz lokalne baze podataka*/
		prijavljeniKorisnik.obrisiPrijavljenogKorisnika();
		Toast.makeText(context, "Uspješno ste se odjavili", Toast.LENGTH_SHORT).show();
	}
	
}
