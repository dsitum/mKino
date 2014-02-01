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

public class SlikaFilma extends AsyncTask<String, Void, Bitmap> {
	public Bitmap preuzmiVelikuSliku(int idFilma)
	{
		String url = "http://mkinoairprojekt.me.pn/skripte/index.php?tip=slike&id=" + idFilma;
		this.execute(url);
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
	
	public Bitmap preuzmiMaluSliku(int idFilma)
	{
		String url = "http://mkinoairprojekt.me.pn/skripte/index.php?tip=slikemale&id=" + idFilma;
		this.execute(url);
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
	protected Bitmap doInBackground(String... parametri) {
		String urlString = parametri[0];
		URL url;
		HttpURLConnection veza;
		Bitmap slika = null;
		
		try {
			url = new URL(urlString);
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
