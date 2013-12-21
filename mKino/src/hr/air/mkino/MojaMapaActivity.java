package hr.air.mkino;

import hr.air.mkino.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;


public class MojaMapaActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_moja_mapa);
		
		Log.d("Debug", "UsaoMojaMapaOnCreate");
	       
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pocetna, menu);
		return true;
	}
}
