package hr.air.mkino;

import hr.air.mkino.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

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
    
    
    
}
