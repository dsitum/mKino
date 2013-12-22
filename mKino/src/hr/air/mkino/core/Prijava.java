package hr.air.mkino.core;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import hr.air.mkino.R;
import hr.air.mkino.tipovi.Korisnik;

/*klasa poslovne logike koja služi za prikaz dijaloga prijave te samu prijavu*/
public class Prijava {
	//statièki objekt koji oznaèava prijavljenog korisnika, ukoliko korisnik nije prijavljen tada je vrijednost null
	static Korisnik prijavljeniKorisnik;
	
	
		
	//Metode
	
	//metoda koja prikazuje dijalog za prijavu u aplikaciju
	public void prikaziDijalog(final Context context) {
			final Dialog dialogPrijava = new Dialog(context);
			dialogPrijava.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogPrijava.setContentView(R.layout.dialog_prijava);			
			dialogPrijava.show();
			
			//dohvaæanje unešenih podataka
			
			Button btnPrijaviSe = (Button)dialogPrijava.findViewById(R.id.dialog_prijava_btnPrijaviSe);
			Button btnOdustani = (Button) dialogPrijava.findViewById(R.id.registracija_btnOdustani);
			Button btnRegistracija = (Button) dialogPrijava.findViewById(R.id.prijava_btnRegistrirajSe);
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
					
					//pozovi metodu za prijavu
					//if !=0
						//prikaži poruku o pogreški
					//else 
					dialogPrijava.dismiss();
					
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
}
