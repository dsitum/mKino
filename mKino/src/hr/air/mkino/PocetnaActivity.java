package hr.air.mkino;

import hr.air.mkino.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class PocetnaActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pocetna);
        
        final Context context = this;
		ImageButton btnOtvoriMapu = (ImageButton) findViewById(R.id.pocetni_btnMapa);
		
		btnOtvoriMapu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, MojaMapaActivity.class);
				startActivity(i);
				
			}
		});
        ImageButton btnOtvoriAktualno= (ImageButton) findViewById(R.id.pocetna_btnAktualno);
		
        btnOtvoriAktualno.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, AktualnoActivity.class);
				startActivity(i);
				
			}
		});   
        ImageButton btnOtvoriMojeRezervacije= (ImageButton) findViewById(R.id.pocetna_btnMojeRezervacije);
		
        btnOtvoriMojeRezervacije.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, DetaljiFilmaActivity.class);
				startActivity(i);
				
			}
		}); 
        ImageButton btnOtvoriRezervacije= (ImageButton) findViewById(R.id.pocetna_brnRezerviraj);
		
        btnOtvoriRezervacije.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, RezervacijeActivity.class);
				startActivity(i);
				
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pocetna, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch (item.getItemId()) {
    	
    	//prikazivanje dijaloga za prijavu 
		case R.id.menu_pocetna_prijava:
			prikaziDialogPrijava();
			break;
			
		//prikazivanje dijaloga za odjavu
		case R.id.menu_pocetna_odjava:
			//odjaviti se iz aplikacije
			Toast.makeText(this, "Odjava pritisnuta", Toast.LENGTH_SHORT).show();
			break;	
		
		//prikazivanje preference izbornika
		case R.id.action_settings:
			Toast.makeText(this, "postavke stisnute", Toast.LENGTH_SHORT).show();
			break;	
		default:
			break;
		}
    	
    	return true;
    }

    //metoda koja prikazuje dijalog za prijavu u aplikaciju
	public void prikaziDialogPrijava() {
		final Dialog dialogPrijava = new Dialog(this);
		dialogPrijava.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogPrijava.setContentView(R.layout.dialog_prijava);			
		dialogPrijava.show();
		
		//dohvaæanje unešenih podataka
		
		Button btnPrijaviSe = (Button)dialogPrijava.findViewById(R.id.dialog_prijava_btnPrijaviSe);
		Button btnOdustani = (Button) dialogPrijava.findViewById(R.id.registracija_btnOdustani);
		Button btnRegistracija = (Button) dialogPrijava.findViewById(R.id.prijava_btnRegistrirajSe);
		final EditText txtKorisnickoIme = (EditText) dialogPrijava.findViewById(R.id.dialog_prijava_txtKorisnickoIme);
		final EditText txLozinka = (EditText)dialogPrijava.findViewById(R.id.dialog_prijava_txtLozinka);
		
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
				
				prikaziDijalogRegistracija();
			}

			
		});
	}
	
	//metoda koja prikazuje dijalog za registraciju
	public void prikaziDijalogRegistracija() {
		//otvori dijalog za registraciju
		final Dialog dialogRegistracija = new Dialog(this);
		dialogRegistracija.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogRegistracija.setContentView(R.layout.dialog_registracija);			
		dialogRegistracija.show();
		
		//dohvaæanje unešenih podataka
		
		Button btnRegistrirajSe = (Button)dialogRegistracija.findViewById(R.id.registracija_btnRegistrirajSe);
		Button btnOdustani = (Button) dialogRegistracija.findViewById(R.id.registracija_btnOdustani);
		
		final EditText txtKorisnickoIme = (EditText) dialogRegistracija.findViewById(R.id.dialog_prijava_txtKorisnickoIme);
		final EditText txLozinka = (EditText)dialogRegistracija.findViewById(R.id.dialog_prijava_txtLozinka);
		final EditText txPonovljenaLozinka = (EditText)dialogRegistracija.findViewById(R.id.dialog_prijava_txtLozinka);
		final EditText txIme = (EditText)dialogRegistracija.findViewById(R.id.dialog_prijava_txtLozinka);
		final EditText txPrezime = (EditText)dialogRegistracija.findViewById(R.id.dialog_prijava_txtLozinka);
		final EditText txEmail = (EditText)dialogRegistracija.findViewById(R.id.dialog_prijava_txtLozinka);
		final EditText txtTelefon = (EditText)dialogRegistracija.findViewById(R.id.dialog_prijava_txtLozinka);
		
		
		//izlazimo iz dijaloga prilikom pritiska na odustani
		btnOdustani.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogRegistracija.dismiss();				
			}
		});
		
		btnRegistrirajSe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//pozovi metodu za izvršavanje registracije
				
				//if !=0
					//ispisi obavijest sta ne valja
				//else
					//poruka o uspjesnoj reg
					//dialogRegistracija.dismiss();
				dialogRegistracija.dismiss();	
			}
		});
		
		
	}
    
    
}
