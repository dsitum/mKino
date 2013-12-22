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
import hr.air.mkino.server.JsonPrijava;
import hr.air.mkino.tipovi.Korisnik;

/*klasa poslovne logike koja slu�i za prikaz dijaloga prijave te samu prijavu*/
public class Prijava {
	
	//stati�ki objekt koji ozna�ava prijavljenog korisnika, ukoliko korisnik nije prijavljen tada je vrijednost null
	static public Korisnik prijavljeniKorisnik;
	
	
		
	//Metode
	
	//metoda koja prikazuje dijalog za prijavu u aplikaciju
	public void prikaziDijalog(final Context context) {
			final Dialog dialogPrijava = new Dialog(context);
			dialogPrijava.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogPrijava.setContentView(R.layout.dialog_prijava);			
			dialogPrijava.show();
			
			//dohva�anje une�enih podataka
			
			Button btnPrijaviSe = (Button)dialogPrijava.findViewById(R.id.dialog_prijava_btnPrijaviSe);
			Button btnOdustani = (Button) dialogPrijava.findViewById(R.id.dijalog_prijava_btnOdustani);
			Button btnRegistracija = (Button)dialogPrijava.findViewById(R.id.prijava_btnRegistrirajSe);
			final EditText txtKorisnickoIme = (EditText) dialogPrijava.findViewById(R.id.dialog_registracija_txtKorisnickoIme);
			final EditText txLozinka = (EditText)dialogPrijava.findViewById(R.id.dialog_registracija_txtLozinka);
			
			//odustani od prijave
			btnOdustani.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialogPrijava.dismiss();

					}
				});
						
			btnPrijaviSe.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
				
					String tKorisnickoIme= txtKorisnickoIme.getText().toString();
					String tLozinka = txLozinka.getText().toString();
					
					if(tKorisnickoIme == null || tKorisnickoIme.length() ==0)
						Toast.makeText(context, "Unesite korisni�ko ime", Toast.LENGTH_SHORT).show();						
					else if(tLozinka == null || tLozinka.length() ==0)
						Toast.makeText(context, "Unesite lozinku", Toast.LENGTH_SHORT).show();
					else{
						JsonPrijava novaPrijava = new JsonPrijava();
						Korisnik prijava = novaPrijava.prijavi(tKorisnickoIme, tLozinka);
						if(prijava != null)
						{
							Toast.makeText(context, "Uspje�no ste prijavljeni!", Toast.LENGTH_SHORT).show();
							prijavljeniKorisnik = prijava;
							dialogPrijava.dismiss();
						}
						else
							Toast.makeText(context, "Neuspje�na prijava!", Toast.LENGTH_SHORT).show();
						
					}
					
					
					
				}
			});
			
			btnRegistracija.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					//zatvaramo dijalog za prijavu
					dialogPrijava.dismiss();
					Registracija registracije = new Registracija();
					registracije.prikaziDijalog(context);
					
				}

				
			});
		}
	//metoda koja odjavljuje korisnika iz aplikacije
	public void odjava(Context context)
	{
		prijavljeniKorisnik = null;
		Toast.makeText(context, "Uspje�no ste se odjavili", Toast.LENGTH_SHORT).show();
	}
	
}
