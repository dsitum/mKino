package hr.air.mkino.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;
import android.os.AsyncTask;
/**
 * Klasa koja služi za brisanje rezervacije na udaljenoj bazi podataka.
 * @author bstivic
 *
 */
public class JsonObrisiRegistraciju extends AsyncTask<String, Void, String> {
	
	/**
	 * Metoda koja služi za brisanje rezervacije.
	 * komunicira sa web servisom
	 * @param korisnickoIme
	 * @param idProjekcije
	 * @param context
	 * @return uspjesnost 0-uspjesno, - neuspjesno
	 */
	public int dohvati(String korisnickoIme, String idProjekcije, Context c)
	{					
		this.execute(korisnickoIme, idProjekcije);
		String jsonRezultat = "";
		try {
			jsonRezultat = this.get();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		} catch (ExecutionException e) {
			
			e.printStackTrace();
		}
		
		return  parsirajJson(jsonRezultat);			
	}

/**
 * Metoda koja parsira rezultat web servisa.
 * @param jsonRezultat
 * @return 0 uspjesno, <0 neuspjesno
 */
	private int parsirajJson(String jsonRezultat) {		
		int povratnaInformacijaId = 7;
		
		try {
			JSONArray rezultati = new JSONArray(jsonRezultat);
			int n = rezultati.length();
			
				for(int i=0; i<n; i++)
				{
				JSONObject rezultat = rezultati.getJSONObject(i);				
				povratnaInformacijaId = rezultat.getInt("povratnaInformacijaId");
				}
			}
		
		catch (JSONException e) {
			e.printStackTrace();
			return -7;
		}
		
		return povratnaInformacijaId;
	}
	
	/**
	 * Metoda koja služi za asinkronu komunikaciju sa web servisom.
	 */
	protected String doInBackground(String... podaciPrijava) {
		HttpClient httpKlijent = new DefaultHttpClient();
	 
		HttpPost httpPostZahtjev = new HttpPost("http://mkinoairprojekt.me.pn/skripte/index.php?tip=obrisirezervaciju");
		String jsonResult = "";
		ResponseHandler<String> handler = new BasicResponseHandler();			
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("korisnickoime", podaciPrijava[0]));
		nameValuePairs.add(new BasicNameValuePair("projekcija", podaciPrijava[1]));

		try {
			  httpPostZahtjev.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			  jsonResult = httpKlijent.execute(httpPostZahtjev, handler);
		}
		catch(ClientProtocolException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		httpKlijent.getConnectionManager().shutdown();
		return jsonResult;
	}


}
