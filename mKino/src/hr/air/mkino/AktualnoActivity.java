package hr.air.mkino;
import java.util.List;

import hr.air.mkino.R;
import hr.air.mkino.adapteri.StavkaFilma;
import hr.air.mkino.server.JsonFilmovi;
import hr.air.mkino.tipovi.FilmInfo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AktualnoActivity extends Activity {
	private ListView popisFilmova;
	private List<FilmInfo> filmovi;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivity_aktualno);
		
		// inicijaliziramo klasne varijable
		popisFilmova = (ListView) findViewById(R.id.popis_filmova);
		
		// postavljamo listener na ListView s filmovima
		popisFilmova.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View kliknutiFilm, int pozicijaKliknutogFilma, long idKli) {
				TextView tv = (TextView) kliknutiFilm.findViewById(R.id.id_filma_u_bazi);
				int idFilmaUBazi = Integer.parseInt(tv.getText().toString());
				Intent i = new Intent(AktualnoActivity.this, DetaljiFilmaActivity.class);
				i.putExtra("idFilmaUBazi", idFilmaUBazi);
				startActivity(i);
			}
		});
		
		// dohva�amo filmove i pohranjujemo ih u ListView "popisFilmova". Tu se automatski prikazuju na zaslon
		filmovi = dohvatiFilmove();
		ucitajFilmUListView();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pocetna, menu);
		return true;
	}
	
	/**
	 * Ova metoda pokre�e dohva�anje filmova na dva na�ina. Prvo se dohva�aju iz baze indeksi svih filmova.
	 * Potom se oblikuju u JSON i �alje se zahtjev web servisu. Servis vra�a samo one filmove 
	 * kojih nema u lokalnoj bazi, kao i one koje treba ukloniti iz lokalne baze.
	 * Nakon toga se filmovi prikazuju na zaslon.
	 * Ovaj princip je odabran kako bi se korisnicima u�tedio podatkovni promet, odnosno,
	 * kako se ne bi uvijek dohva�ali svi filmovi sa servisa
	 * @return a�urirani filmovi
	 */
	private List<FilmInfo> dohvatiFilmove()
	{	
		JsonFilmovi jf = new JsonFilmovi();
		return jf.dohvatiFilmove(this);
	}
	
	/**
	 * Dohva�a filmove i u�itava ih u ListView
	 */
	private void ucitajFilmUListView()
	{				
		ArrayAdapter<FilmInfo> adapter = new StavkaFilma(this, R.layout.stavka_filma, filmovi);
		ListView lv = (ListView) findViewById(R.id.popis_filmova);
		lv.setAdapter(adapter);
	}
}
