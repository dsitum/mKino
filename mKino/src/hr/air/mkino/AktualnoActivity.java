package hr.air.mkino;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.air.mkino.R;
import hr.air.mkino.baza.OdabraniMultipleksAdapter;
import hr.air.mkino.server.JsonFilmovi;
import hr.air.mkino.tipovi.FilmInfo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class AktualnoActivity extends Activity {
	private Spinner spinner;
	private ListView popisFilmova;
	private List<FilmInfo> filmovi;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivity_aktualno);
		
		// inicijaliziramo klasne varijable
		spinner = (Spinner) findViewById(R.id.odabir_multipleksa_filmovi);
		popisFilmova = (ListView) findViewById(R.id.popis_filmova);
		
		// postavljamo spinner na trenutno odabrani multipleks
		spinner.setSelection(dohvatiOdabraniMultipleks());
		
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
		ucitajFilmoveUListView();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pocetna, menu);
		return true;
	}
	
	/**
	 * Postavlja spinner za izbor multipleksa na vrijednost trenutnog multipleksa u bazi. Ukoliko on ne postoji ondje, odabire se prvi multipleks (indeks 0) koji je Zagreb
	 */
	private int dohvatiOdabraniMultipleks() {
		final int ZAGREB = 0;
		OdabraniMultipleksAdapter oma = new OdabraniMultipleksAdapter(this);
		int odabraniMultipleks = oma.dohvatiOdabraniMultipleks();
		
		if (odabraniMultipleks >= 0)
			return odabraniMultipleks;
		else
			return ZAGREB;
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
	private void ucitajFilmoveUListView()
	{
		String[] iz = new String[] {"idFilma", "naziv", "glavneUloge"};
		int[] u = new int[] {R.id.id_filma_u_bazi, R.id.naziv_filma_mali, R.id.uloge_filma_male};
		
		Map<String, String> stavka;
		List<Map<String, String>> podaci = new ArrayList<Map<String, String>>();
		
		for (FilmInfo film : filmovi)
		{
			stavka = new HashMap<String, String>();
			stavka.put("idFilma", String.valueOf(film.getIdFilma()));
			stavka.put("naziv", film.getNaziv());
			stavka.put("glavneUloge", film.getGlavneUloge());
			podaci.add(stavka);
		}
		
		ListAdapter adapter = new SimpleAdapter(this, podaci, R.layout.stavka_filma, iz, u);
		popisFilmova.invalidateViews();
		popisFilmova.setAdapter(adapter);
	}
}
