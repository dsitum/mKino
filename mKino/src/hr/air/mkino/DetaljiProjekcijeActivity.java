package hr.air.mkino;


import hr.air.mkino.baza.ProjekcijeAdapter;
import hr.air.mkino.server.SlikaFilma;

import hr.air.mkino.tipovi.ProjekcijaInfo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class DetaljiProjekcijeActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalji_projekcije);
		
		ProjekcijaInfo detaljiProjekcije = dohvatiPodatkeZaProjekciju();
		prikaziPodatkeZaProjekciju(detaljiProjekcije);
	}

	private ProjekcijaInfo dohvatiPodatkeZaProjekciju() {
		Intent i = getIntent();
		int idProjekcijeUBazi = i.getIntExtra("idProjekcijeUBazi", 0);
		
		ProjekcijeAdapter pa = new ProjekcijeAdapter(this);
	
		return pa.dohvatiProjekciju(idProjekcijeUBazi, this);
	}
	
	private void prikaziPodatkeZaProjekciju(ProjekcijaInfo detaljiProjekcije) {
		TextView naslov = (TextView) findViewById(R.id.projekcija_naslov_filma);
		TextView trajanje = (TextView) findViewById(R.id.projekcija_trajanje);
		TextView zanr = (TextView) findViewById(R.id.projekcija_zanr);
		TextView redatelj = (TextView) findViewById(R.id.projekcija_redatelj);
		TextView glavneUloge = (TextView) findViewById(R.id.projekcija_glumci);
		TextView opis = (TextView) findViewById(R.id.projekcija_detalji);
		ImageView slika = (ImageView) findViewById(R.id.projekcija_slikaFilma);
		TextView dvorana = (TextView) findViewById(R.id.projekcija_dvorana);
		TextView vrijeme = (TextView) findViewById(R.id.projekcija_vrijeme);
		TextView multipleks = (TextView) findViewById(R.id.projekcija_detalji_multipleks);
		
		naslov.setText(detaljiProjekcije.getNaziv());
		trajanje.setText(detaljiProjekcije.getTrajanje() + " minuta");
		zanr.setText(detaljiProjekcije.getZanr());
		redatelj.setText(detaljiProjekcije.getRedatelj());
		glavneUloge.setText(detaljiProjekcije.getGlavneUloge());
		opis.setText(detaljiProjekcije.getOpis());
		dvorana.setText("Dvorana "+detaljiProjekcije.getDvorana());
		vrijeme.setText(detaljiProjekcije.getVrijemePocetka());
		multipleks.setText(Integer.toString(detaljiProjekcije.getMultipleks()));
		
		SlikaFilma sf = new SlikaFilma();
		slika.setImageBitmap(sf.preuzmiSliku(detaljiProjekcije.getIdFilma()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pocetna, menu);
		return true;
	}
}
