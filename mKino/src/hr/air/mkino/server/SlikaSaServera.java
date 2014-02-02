package hr.air.mkino.server;

import hr.air.mkino.sucelja.ISlikaFilma;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.DisplayMetrics;

public class SlikaSaServera extends AsyncTask<String, Void, Bitmap> implements ISlikaFilma {
	private Context context;
	private int idFilma;
	
	public SlikaSaServera(Context context, int idFilma) {
		this.context = context;
		this.idFilma = idFilma;
	}
	
	public Bitmap dohvatiVelikuSliku()
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
		
		pohraniSlikuNaSD(slika, true);
		return slika;
	}
	
	public Bitmap dohvatiMaluSliku()
	{
		String url = "http://mkinoairprojekt.me.pn/skripte/index.php?tip=slikemale&id=" + idFilma;
		this.execute(url);
		Bitmap slika = null;
		try {
			slika = promijeniVelicinuMaleSlike(this.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		pohraniSlikuNaSD(slika, false);
		return slika;
	}
	

	/**
	 * Nakon što se slika preuzme s interneta, potrebno ju je pohraniti na SD karticu u svrhu cachiranja
	 * @param slika koju treba pohraniti
	 * @param velikaSlika radi li se o velikoj slici ili maloj (sa ListView-a)
	 */
	private void pohraniSlikuNaSD(Bitmap slika, boolean velikaSlika) {
		String putanjaDoSlike;
		String sdKartica = Environment.getExternalStorageDirectory().getPath();
		
		if (velikaSlika)
		{
			putanjaDoSlike = sdKartica + "/mKino/" + idFilma + ".jpg";
		} else
		{
			putanjaDoSlike = sdKartica + "/mKino/" + idFilma + "_mala.jpg";
		}
		
		// najprije napravimo direktorij "mKino" na SD kartici (ukoliko taj direktorij ne postoji)
		File direktorij = new File(sdKartica + "/mkino");
		if (!direktorij.exists())
		{
			direktorij.mkdir();
		}
		
		// nakon što znamo da direktorij "mKino" postoji na SD kartici, èuvamo podatke na njoj
		try {
			FileOutputStream tok = new FileOutputStream(putanjaDoSlike);
			slika.compress(Bitmap.CompressFormat.JPEG, 90, tok);
			tok.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ova metoda mijenja velièinu malih slika. Velièinu preuzetih slika je potrebno promijeniti kako bi prikazane slike zauzimale što manje memorije
	 * @param velika slika
	 * @return mala slika ili originalna ukoliko nije potrebno mijenjati velièinu
	 */
	private Bitmap promijeniVelicinuMaleSlike(Bitmap slika) {
		int sirinaVisina = 0;
		
		// provjeravamo je li razluèivost xhdpi, hdpi, mdpi ili ldpi
		switch(context.getResources().getDisplayMetrics().densityDpi)
		{
			case DisplayMetrics.DENSITY_LOW:
				sirinaVisina = 60; break;
			case DisplayMetrics.DENSITY_MEDIUM:
				sirinaVisina = 80; break;
			case DisplayMetrics.DENSITY_HIGH:
				sirinaVisina = 120; break;
		}
		
		// ako se širina(visina) slike nije promijenila, a to je u sluèaju kada je telefon xhdpi ili xxhdpi, tada ju neæemo ni resize-ati,
		// nego æemo vratiti staru vrijednost slike (else). U suprotnom æemo vratiti resize-anu vrijednost
		if (sirinaVisina != 0)
		{
			Bitmap novaSlika = Bitmap.createScaledBitmap(slika, sirinaVisina, sirinaVisina, false);
			return novaSlika;
		} else
		{
			return slika;
		}
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
