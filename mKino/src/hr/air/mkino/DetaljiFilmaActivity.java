package hr.air.mkino;

import hr.air.mkino.baza.FilmoviAdapter;
import hr.air.mkino.server.SlikaFilma;
import hr.air.mkino.tipovi.FilmInfo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class DetaljiFilmaActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalji_filma);
		
		FilmInfo detaljiFilma = dohvatiPodatkeZaFilm();
		prikaziPodatkeZaFilm(detaljiFilma);
	}

	private FilmInfo dohvatiPodatkeZaFilm() {
		Intent i = getIntent();
		int idFilmaUBazi = i.getIntExtra("idFilmaUBazi", 0);
		
		FilmoviAdapter fa = new FilmoviAdapter(this);
		return fa.dohvatiDetaljeFilma(idFilmaUBazi);
	}
	
	private void prikaziPodatkeZaFilm(FilmInfo detaljiFilma) {
		TextView naslov = (TextView) findViewById(R.id.naslov_filma);
		TextView trajanje = (TextView) findViewById(R.id.detalji_filma_trajanje);
		TextView zanr = (TextView) findViewById(R.id.detalji_filma_zanr);
		TextView redatelj = (TextView) findViewById(R.id.detalji_filma_redatelj);
		TextView glavneUloge = (TextView) findViewById(R.id.detalji_filma_glumci);
		TextView opis = (TextView) findViewById(R.id.detalji_filma_detalji);
		ImageView slika = (ImageView) findViewById(R.id.slikaFilma);
		
		naslov.setText(detaljiFilma.getNaziv());
		trajanje.setText(detaljiFilma.getTrajanje() + " minuta");
		zanr.setText(detaljiFilma.getZanr());
		redatelj.setText(detaljiFilma.getRedatelj());
		glavneUloge.setText(detaljiFilma.getGlavneUloge());
		opis.setText(detaljiFilma.getOpis());
		SlikaFilma sf = new SlikaFilma();
		slika.setImageBitmap(sf.preuzmiSliku(detaljiFilma.getIdFilma()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pocetna, menu);
		return true;
	}
}
