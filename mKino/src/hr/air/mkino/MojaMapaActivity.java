package hr.air.mkino;

import java.util.List;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import hr.air.mkino.R;
import hr.air.mkino.baza.MultipleksAdapter;
import hr.air.mkino.server.JsonMultipleksi;
import hr.air.mkino.tipovi.MultipleksInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;

public class MojaMapaActivity extends FragmentActivity {
	private GoogleMap mapa;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_moja_mapa);
		
		mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment)).getMap();
		
		List<MultipleksInfo> multipleksi = dohvatMultipleksa();
		prikazMultipleksaNaKarti(multipleksi);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pocetna, menu);
		return true;
	}
	
	private List<MultipleksInfo> dohvatMultipleksa() {
		MultipleksAdapter multipleksAdapter = new MultipleksAdapter(this);
		List<MultipleksInfo> multipleksi = multipleksAdapter.dohvatiMultiplekse();
		
		if (multipleksi.size() == 0)
		{
			Log.d("AIR", "Lokalna baza prazna! Dohvaæam sa servisa..");
			JsonMultipleksi jsonMultipleksi = new JsonMultipleksi();
			multipleksi = jsonMultipleksi.dohvatiMultiplekse();
			Log.d("AIR", "Dohvaæeni multipleksi sa servisa. Broj: " + multipleksi.size());
			
			// unos dohvaæenih multipleksa u lokalnu bazu
			for (MultipleksInfo multipleks : multipleksi)
			{
				multipleksAdapter.unosMultipleksa(multipleks);
			}
		} else
		{
			Log.d("AIR", "Multipleksi postoje u bazi. Broj: " + multipleksi.size());
		}
		
		return multipleksi;
	}
	
	private void prikazMultipleksaNaKarti(List<MultipleksInfo> multipleksi) {
		for (MultipleksInfo multipleks : multipleksi)
		{
			MarkerOptions marker = new MarkerOptions();
			marker.title(multipleks.getNaziv());
			marker.position(new LatLng(multipleks.getZemljopisnaSirina(), multipleks.getZemljopisnaDuzina()));
			mapa.addMarker(marker);
		}
	}
}
