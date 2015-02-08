package hr.air.mkino;

import hr.air.mkino.baza.FilmoviAdapter;
import hr.air.mkino.sucelja.ISlikaFilma;
import hr.air.mkino.tipovi.FilmInfo;
import hr.air.mkino.uzorcidizajna.UcitajSlikuFactory;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Ova klasa predstavlja detalje pojedinog filma.
 * @author domagoj
 *
 */
public class DetaljiFilmaActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalji_filma);
		setTitle("Detalji filma");
		
		FilmInfo detaljiFilma = dohvatiPodatkeZaFilm();
		prikaziPodatkeZaFilm(detaljiFilma);
	}

	/**
	 * Dohvaæa podatke filma koji ukljuèuju naziv, glavne uloge, autora itd.
	 * ID filma prima pomoæu podataka iz intenta, koji su poslani klikom na film sa liste svih aktualnih filmova (klasa AkutalnoActivity).
	 * @return podaci o filmu
	 */
	private FilmInfo dohvatiPodatkeZaFilm() {
		Intent i = getIntent();
		int idFilmaUBazi = i.getIntExtra("idFilmaUBazi", 0);
		
		FilmoviAdapter fa = new FilmoviAdapter(this);
		return fa.dohvatiDetaljeFilma(idFilmaUBazi);
	}
	
	/**
	 * Ispisuje na zaslonu informacije sve dohvaæene o filmu, ukljuèujuæi i sliku filma.
	 * @param detaljiFilma predstavlja informacije o filmu koje treba ispisati na zaslon
	 */
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
		ISlikaFilma sf = UcitajSlikuFactory.ucitaj(getBaseContext(), detaljiFilma.getIdFilma(), true);
		slika.setImageBitmap(sf.dohvatiVelikuSliku());
	}
}
