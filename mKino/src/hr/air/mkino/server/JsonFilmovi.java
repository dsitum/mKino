package hr.air.mkino.server;

import hr.air.mkino.baza.FilmoviAdapter;
import hr.air.mkino.tipovi.FilmInfo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import android.util.Log;

/**
 * Ova klasa služi za dohvat filmova sa web servisa.
 * @author domagoj
 *
 */
public class JsonFilmovi extends AsyncTask<String, Void, String> {
	/**
	 * Dohvaæa filmove sa web servisa. To ne ukljuèuje one filmove koji se veæ nalaze u lokalnoj bazi
	 * @return filmovi
	 */
	public List<FilmInfo> dohvatiFilmove(Context c)
	{
		// najprije dohvaæamo id-eve filmova iz lokalne baze podataka, kako bi znali koje filmove ne trebamo dohvaæati sa webservisa
		FilmoviAdapter fa = new FilmoviAdapter(c);
		List<Integer> idFilmova = fa.dohvatiIdFilmova();
		
		// pretvaramo listu s id-evima filmova u JSON string (moramo ruèno, buduæi da ne postoji neka standardna metoda za to
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(int i=0; i<idFilmova.size(); i++)
		{
			sb.append("{\"idFilma\":\"" + idFilmova.get(i) + "\"}");
			// ako se ne radi o posljednjem elementu, dodat æemo zarez
			if (i != idFilmova.size() - 1)
			{
				sb.append(",");
			}
		}
		sb.append("]");
		// finalni JSON string koji æemo slati web servisu putem HTTP-POST metode
		String jsonString = sb.toString();
		
		// dohvatiti podatke s web servsa	
		this.execute(jsonString);
		String jsonRezultat = "";
		try {
			jsonRezultat = this.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		List<FilmInfo> filmovi = parsirajJson(jsonRezultat);
		return filmovi;
	}
	
	/**
	 * Parsira JSON string dohvaæen s web servisa
	 * @param jsonRezultat
	 * @return filmovi
	 */
	private List<FilmInfo> parsirajJson(String jsonRezultat) {
		List<FilmInfo> filmovi = new ArrayList<FilmInfo>();
		int idFilma;
		String naziv;
		String opis;
		String redatelj;
		String glavneUloge;
		int trajanje;
		int godina;
		String zanr;
		int aktualno;
		
		try {
			JSONArray filmoviJson = new JSONArray(jsonRezultat);
			int n = filmoviJson.length();
			for(int i=0; i<n; i++)
			{
				JSONObject filmJson = filmoviJson.getJSONObject(i);
				idFilma = filmJson.getInt("idFilma");
				naziv = filmJson.getString("naziv");
				opis = filmJson.getString("opis");
				redatelj = filmJson.getString("redatelj");
				glavneUloge = filmJson.getString("glavneUloge");
				trajanje = filmJson.getInt("trajanje");
				godina = filmJson.getInt("godina");
				zanr = filmJson.getString("zanr");
				aktualno = filmJson.getInt("aktualno");
				FilmInfo film = new FilmInfo(idFilma, naziv, opis, redatelj, glavneUloge, trajanje, godina, zanr, aktualno);
				
				filmovi.add(film);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		return filmovi;
	}
	
	// pomoæna metoda koja u pozadini obraðuje http zahtjev (dohvaæanje podataka)
	@Override
	protected String doInBackground(String... parametri) {
		String jsonString = parametri[0];
		
		HttpClient httpKlijent = new DefaultHttpClient();
		// HTTP-POST metodom æemo poslati JSON string. U tom JSON stringu se nalaze indeksi svih filmova koji se nalaze u lokalnoj bazi. Web servis æe vratiti sve one filmove èiji se indeksi ne nalaze u tom JSON stringu
		HttpPost httpPost = new HttpPost("http://mkinoairprojekt.me.pn/skripte/index.php?tip=nekifilmovi&aktualno=1");
		
		// dodajemo JSON string u "httpPost" objekt
		try {
			List<NameValuePair> podaci = new ArrayList<NameValuePair>();
			podaci.add(new BasicNameValuePair("bezOvihFilmova", jsonString));
			httpPost.setEntity(new UrlEncodedFormEntity(podaci));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String jsonRezultat = "";
		ResponseHandler<String> handler = new BasicResponseHandler();
		
		// ono što dohvatimo u jsonRezultat æe biti odgovor servera (web servisa)
		try {
			jsonRezultat = httpKlijent.execute(httpPost, handler);
		}
		catch(ClientProtocolException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		httpKlijent.getConnectionManager().shutdown();
		return jsonRezultat;
	}

}
