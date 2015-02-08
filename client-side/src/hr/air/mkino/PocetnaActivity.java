package hr.air.mkino;

import hr.air.mkino.R;
import hr.air.mkino.baza.PrijavljeniKorisnikAdapter;
import hr.air.mkino.core.Prijava;
import hr.air.mkino.tipovi.Korisnik;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Klasa koja služi za prikaz poèetnog zaslona aplikacije
 * @author SystemDesign
 */
public class PocetnaActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pocetna);
        final Context context = this;
        
        final ImageButton btnOtvoriMojeRezervacije= (ImageButton) findViewById(R.id.pocetna_btnMojeRezervacije);
        final ImageButton btnOtvoriMapu = (ImageButton) findViewById(R.id.pocetni_btnMapa);
        final ImageButton btnOtvoriAktualno= (ImageButton) findViewById(R.id.pocetna_btnAktualno);
        final ImageButton btnOtvoriRezervacije= (ImageButton) findViewById(R.id.pocetna_brnRezerviraj);
    			
		btnOtvoriMapu.setOnTouchListener(new OnTouchListener(){

        
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				 switch(arg1.getAction())
	                {
	                case MotionEvent.ACTION_DOWN :
	                	btnOtvoriMapu.setImageResource(R.drawable.mapa_down100_93);
	                    break;
	                case MotionEvent.ACTION_UP :
	                	btnOtvoriMapu.setImageResource(R.drawable.mapa100_93);
	                	
	    				Intent i = new Intent(context, MojaMapaActivity.class);
	    				startActivity(i);
	                    break;
	                }
	             
				return false;
			}

        });
		
        btnOtvoriAktualno.setOnTouchListener(new OnTouchListener(){

        
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				 switch(arg1.getAction())
	                {
	                case MotionEvent.ACTION_DOWN :
	                	btnOtvoriAktualno.setImageResource(R.drawable.aktualno_down100x93);
	                    break;
	                case MotionEvent.ACTION_UP :
	                	btnOtvoriAktualno.setImageResource(R.drawable.aktualno100x93);
	                	Intent i = new Intent(context, AktualnoActivity.class);
	    				startActivity(i);
	                    break;
	                }
	             
				return false;
			}

        });
			
        btnOtvoriMojeRezervacije.setOnTouchListener(new OnTouchListener(){

        
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				 switch(arg1.getAction())
	                {
	                case MotionEvent.ACTION_DOWN :
	                	btnOtvoriMojeRezervacije.setImageResource(R.drawable.mojerezervacije_dwon100_93);
	                    break;
	                case MotionEvent.ACTION_UP :
	                	btnOtvoriMojeRezervacije.setImageResource(R.drawable.mojerezervacije100_93);
	                	Toast.makeText(context, R.string.ucitavam, Toast.LENGTH_SHORT).show();
	    				PrijavljeniKorisnikAdapter prijavljeniKorisnik = new PrijavljeniKorisnikAdapter(context);
	    				Korisnik korisnik = prijavljeniKorisnik.dohvatiPrijavljenogKorisnika();
	    				if(korisnik != null )
	    				{
	    					Intent i = new Intent(context, MojeRezervacijeActivity.class);
	    					startActivity(i);
	    				}
	    				else
	    				{
	    					Toast.makeText(context, R.string.moje_rezervacije_prijavite_se, Toast.LENGTH_SHORT).show();					
	    				}
	                    break;
	                }
	             
				return false;
			}

        });
        
       
        btnOtvoriRezervacije.setOnTouchListener(new OnTouchListener(){

        
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				 switch(arg1.getAction())
	                {
	                case MotionEvent.ACTION_DOWN :
	                	btnOtvoriRezervacije.setImageResource(R.drawable.projekcije_down100_93);
	                    break;
	                case MotionEvent.ACTION_UP :
	                	btnOtvoriRezervacije.setImageResource(R.drawable.projekcije100_93);
	                	Toast.makeText(context, R.string.ucitavam, Toast.LENGTH_SHORT).show();
	    				Intent i = new Intent(context, ProjekcijeActivity.class);
	    				startActivity(i);	
	                    break;
	                }
	             
				return false;
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
