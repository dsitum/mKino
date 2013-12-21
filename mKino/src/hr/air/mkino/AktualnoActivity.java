package hr.air.mkino;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class AktualnoActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.acitivity_aktualno);
		
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	//	getMenuInflater().inflate(R.menu.pocetni_zaslon, menu);
		return true;
	}
}
