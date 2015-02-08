package hr.air.mkino;

import java.util.List;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import hr.air.mkino.R;
import hr.air.mkino.baza.MultipleksAdapter;
import hr.air.mkino.baza.OdabraniMultipleksAdapter;
import hr.air.mkino.lokacija.PratiteljLokacije;
import hr.air.mkino.server.JsonMultipleksi;
import hr.air.mkino.tipovi.MultipleksInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

/**
 * Ova klasa slu�i za prikaz multipleksa i odabir istog. 
 * Odabrani multipleks �e se pohraniti u lokalnoj bazi i biti �e kori�ten svaki puta kada to bude bilo potrebno u aplikaciji
 * @author domagoj
 */
public class MojaMapaActivity extends FragmentActivity {
	private GoogleMap mapa;
	private Spinner spinner;
	private List<MultipleksInfo> multipleksi;
	private OdabraniMultipleksAdapter odabraniMultipleks;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_moja_mapa);
		setTitle("Prikaz multipleksa");
		
		mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment)).getMap();
		spinner = (Spinner) findViewById(R.id.odabir_multipleksa);
		multipleksi = dohvatiMultiplekse();
		
		odabraniMultipleks = new OdabraniMultipleksAdapter(this);
		// postavljamo spinner na vrijednost iz baze podataka
		postaviSpinner(odabraniMultipleks);
		
		// omogu�ujemo da se svaka promjena spinnera prika�e na karti i tako�er odmah prikazujemo multiplekse na karti
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long idOdabranogMultipleksa) {
				prikaziMultipleksaNaKarti(multipleksi);
				
				// pohranjujemo vrijednost spinnera u bazu podataka
				odabraniMultipleks.pohraniOdabraniMultipleks((int)idOdabranogMultipleksa);
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.moja_mapa_activity, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.geolokacija:
				// bri�emo postoje�i multipleks iz baze i postavljamo novi (koji �e biti dobiven geolokacijom)
				odabraniMultipleks.obrisiMultiplekseIzBaze();
				postaviSpinner(odabraniMultipleks);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Dohva�a multiplekse. 
	 * Ukoliko postoje u lokalnoj bazi, dohva�a ih iz nje, a ukoliko ne, dohva�a ih sa servisa. To je u�injeno u svrhu �tednje podatkovnog prometa
	 * @return lista s dohva�enim multipleksima
	 */
	private List<MultipleksInfo> dohvatiMultiplekse() {
		MultipleksAdapter multipleksAdapter = new MultipleksAdapter(this);
		List<MultipleksInfo> multipleksi = multipleksAdapter.dohvatiMultiplekse();
		
		if (multipleksi.size() == 0)
		{
			JsonMultipleksi jsonMultipleksi = new JsonMultipleksi();
			multipleksi = jsonMultipleksi.dohvatiMultiplekse();
			
			// unos dohva�enih multipleksa u lokalnu bazu
			for (MultipleksInfo multipleks : multipleksi)
			{
				multipleksAdapter.unosMultipleksa(multipleks);
			}
		} 
		
		return multipleksi;
	}
	
	/**
	 * Prikazuje multiplekse na karti (prije toga �isti kartu od svih postavljenih multipleksa)
	 * @param multipleksi
	 */
	private void prikaziMultipleksaNaKarti(List<MultipleksInfo> multipleksi) {
		long idMultipleksaSpinner = spinner.getSelectedItemId();
		
		mapa.clear();
		for (MultipleksInfo multipleks : multipleksi)
		{
			MarkerOptions marker = new MarkerOptions();
			marker.title(multipleks.getNaziv());
			marker.position(new LatLng(multipleks.getZemljopisnaSirina(), multipleks.getZemljopisnaDuzina()));
			
			if (multipleks.getIdMultipleksa() == idMultipleksaSpinner + 1)
			{
				marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.kino_selektirano));
				
				float lat = multipleks.getZemljopisnaSirina();
				float lng = multipleks.getZemljopisnaDuzina();
				CameraPosition cp = new CameraPosition(new LatLng(lat, lng), 6, 0, 0);
				CameraUpdate cu = CameraUpdateFactory.newCameraPosition(cp);
				mapa.animateCamera(cu);
			}
			else
			{
				marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.kino));
			}
			
			mapa.addMarker(marker);
		}
	}
	
	/**
	 * Postavlja po�etnu vrijednost na spinner za odabir lokacije. Ukoliko taj podatak ne postoji u bazi, postavljamo spinner na index 0 (grad Zagreb)
	 * @param odabraniMultipleks koji �e biti postavljen na spinner
	 */
	private void postaviSpinner(OdabraniMultipleksAdapter odabraniMultipleks) {
		int trenutnaVrijednost = odabraniMultipleks.dohvatiOdabraniMultipleks();
		
		if (trenutnaVrijednost == -1)
			spinner.setSelection(indeksNajblizegMultipleksa());
		else
			spinner.setSelection(trenutnaVrijednost);
	}

	/**
	 * Ova metoda vra�a indeks najbli�eg multipleksa kako bi se on mogao koristiti u spinner-u. Pod pojmom "najbli�i" se misli prostorno najbli�i od trenutne lokacije korisnika
	 * @return indeks najbli�eg multipleksa
	 */
	private int indeksNajblizegMultipleksa() {
		PratiteljLokacije pratitelj = new PratiteljLokacije(this);
		
		if (pratitelj.lokacijaDostupna())
		{
			int indeksNajblizegMultipleksa = 0;
			float udaljenostDoNajblizegMultipleksa = 999999999;  // za po�etnu udaljenost stavimo neki jako veliki broj (jer se tra�i najmanja udaljenost)
			
			for (MultipleksInfo multipleks : multipleksi)
			{
				float tmpUdaljenost = pratitelj.udaljenostDo(multipleks.getZemljopisnaDuzina(), multipleks.getZemljopisnaSirina());
				if (tmpUdaljenost < udaljenostDoNajblizegMultipleksa)
				{
					udaljenostDoNajblizegMultipleksa = tmpUdaljenost;
					indeksNajblizegMultipleksa = multipleks.getIdMultipleksa();
				}
			}
			
			return indeksNajblizegMultipleksa - 1;  // oduzimamo 1 jer u bazi podaci po�inju od 1, a nama za spinner trebaju podaci od 0
		}
		else
		{
			return 0;
		}
	}
}
