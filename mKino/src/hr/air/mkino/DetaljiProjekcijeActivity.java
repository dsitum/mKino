package hr.air.mkino;

import hr.air.mkino.baza.MultipleksAdapter;
import hr.air.mkino.baza.ProjekcijeAdapter;
import hr.air.mkino.sucelja.ISlikaFilma;
import hr.air.mkino.tipovi.MultipleksInfo;
import hr.air.mkino.tipovi.ProjekcijaInfo;
import hr.air.mkino.uzorcidizajna.UcitajSlikuFactory;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * Klasa koja služi za prikaz detalja odabrane projekcije.
 * @author bstivic
 *
 */
public class DetaljiProjekcijeActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalji_projekcije);
		
		//dohvaæamo podatke odabrane projekcije
		ProjekcijaInfo detaljiProjekcije = dohvatiPodatkeZaProjekciju();
		prikaziPodatkeZaProjekciju(detaljiProjekcije);
		
		//prilikom klika na rezervaciju otvara se prozor za rezervaciju ulaznica
		Button btnRezerviraj = (Button) findViewById(R.id.projekcije_btnRezerviraj);
		btnRezerviraj.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		
				//tražimo id projekcije 
				Intent ij = getIntent();
				int idProjekcijeUBazi = ij.getIntExtra("idProjekcijeUBazi", 0);
			
				//pokreæemo activity za rezervaciju
				Intent i = new Intent(DetaljiProjekcijeActivity.this, RezervacijaActivity.class);
				i.putExtra("idProjekcijeUBazi", idProjekcijeUBazi);
				startActivity(i);
				
			}
		});	
	}

	/**
	 * Metoda koja dohvaæa podatke za odabranu projekcije iz baze i sa web servisa.
	 * @return  projekcija
	 */
	private ProjekcijaInfo dohvatiPodatkeZaProjekciju() {
		Intent i = getIntent();
		int idProjekcijeUBazi = i.getIntExtra("idProjekcijeUBazi", 0);
		
		//provjeravamo projekciju u lokalnoj bazi
		ProjekcijeAdapter pa = new ProjekcijeAdapter(this);
		return pa.dohvatiProjekciju(idProjekcijeUBazi, this);
	}
	
	/**
	 * Metoda koja služi za prikaz podataka projekcije na formi.
	 * @param projekcija
	 */
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
		
		
		String vrijePocetkString = detaljiProjekcije.getVrijemePocetka();
		vrijeme.setText(vrijePocetkString);
		
		//dohvaæanje multipleksa iz lokalne baze podatka
		MultipleksAdapter ma = new MultipleksAdapter(this);
		MultipleksInfo multipl = ma.dohvatiMultipleks(detaljiProjekcije.getMultipleks());
		if(multipl != null) multipleks.setText(multipl.getNaziv());		
		
		ISlikaFilma sf = UcitajSlikuFactory.ucitaj(this, detaljiProjekcije.getIdFilma(), true);
		slika.setImageBitmap(sf.dohvatiVelikuSliku());		
	
	}

}
