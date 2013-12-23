package hr.air.mkino;

import hr.air.mkino.R;
import hr.air.mkino.baza.PrijavljeniKorisnikAdapter;
import hr.air.mkino.core.Prijava;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;
/**klasa koja služi za prikaz poèetnog zaslona aplikacije*/
public class PocetnaActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pocetna);
        final Context context = this;
        
        ImageButton btnOtvoriMojeRezervacije= (ImageButton) findViewById(R.id.pocetna_btnMojeRezervacije);
        ImageButton btnOtvoriMapu = (ImageButton) findViewById(R.id.pocetni_btnMapa);
        ImageButton btnOtvoriAktualno= (ImageButton) findViewById(R.id.pocetna_btnAktualno);
        ImageButton btnOtvoriRezervacije= (ImageButton) findViewById(R.id.pocetna_brnRezerviraj);
    			
		btnOtvoriMapu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, MojaMapaActivity.class);
				startActivity(i);
				
			}
		});       
		
        btnOtvoriAktualno.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, AktualnoActivity.class);
				startActivity(i);
				
			}
		});           
      
        btnOtvoriMojeRezervacije.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, DetaljiFilmaActivity.class);
				startActivity(i);
				
			}
		});         
     	
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
    
    /*odabir opcija iz glavnog izbornika*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Context context = this;
    	Prijava prijava = new Prijava();
    	
    	switch (item.getItemId()) {
    	
    	/*prikazivanje dijaloga za prijavu */
		case R.id.menu_pocetna_prijava:
			prijava.prikaziDijalog(context);
			break;
			
		/*prikazivanje dijaloga za odjavu*/
		case R.id.menu_pocetna_odjava:		
			prijava.odjava(this);			
			break;			
			
		/*prikazivanje preference izbornika*/
		case R.id.action_settings:
			Toast.makeText(this, "postavke..", Toast.LENGTH_SHORT).show();
			break;	

		}
    	
    	return true;
    }

    /*uklanja opciju prijava/odjava ovisno o stanju aplikacije*/
    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
    	PrijavljeniKorisnikAdapter prijavljeniKorisnik = new PrijavljeniKorisnikAdapter(this);
    	
    	/*ako je korisnik prijavljen, odnosno spremljeni su njegovi podaci u bazu*/
        if (prijavljeniKorisnik.dohvatiPrijavljenogKorisnika() != null)
        {
          /*prikaži i uljuèi opciju odjava*/
          menu.getItem(1).setEnabled(true);
          menu.getItem(1).setVisible(true);
          /*iskljuèi i sakrij opciju prijava*/
          menu.getItem(0).setEnabled(false);
          menu.getItem(0).setVisible(false);
          
        }
        else 
        {
        	/*prikaži i uljuèi opciju prijava */     	
        	menu.getItem(0).setEnabled(true);
        	menu.getItem(0).setVisible(true);
        	/*iskljuèi i sakrij opciju odjava */
        	menu.getItem(1).setVisible(false);
        	menu.getItem(1).setEnabled(false);            
        }
        return true;
    }	
    
}
