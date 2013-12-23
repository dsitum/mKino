package hr.air.mkino.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class SlikaFilma extends AsyncTask<Integer, Void, Bitmap> {
	public Bitmap preuzmiSliku(int idFilma)
	{
		this.execute(idFilma);
		Bitmap slika = null;
		try {
			slika = this.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return slika;
	}

	@Override
	protected Bitmap doInBackground(Integer... parametri) {
		int idFilma = parametri[0];
		URL url;
		HttpURLConnection veza;
		Bitmap slika = null;
		
		try {
			url = new URL("http://mkinoairprojekt.me.pn/skripte/index.php?tip=slike&id=" + idFilma);
			veza = (HttpURLConnection) url.openConnection();
			veza.setDoInput(true);
			veza.connect();
			InputStream tok = veza.getInputStream();
			slika = BitmapFactory.decodeStream(tok);
			veza.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return slika;
	}

}
