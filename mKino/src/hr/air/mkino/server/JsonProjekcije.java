package hr.air.mkino.server;

import hr.air.mkino.baza.FilmoviAdapter;

import hr.air.mkino.baza.ProjekcijeAdapter;
import hr.air.mkino.tipovi.FilmInfo;
import hr.air.mkino.tipovi.ProjekcijaInfo;

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


public class JsonProjekcije extends AsyncTask <String, Void, String> {

ProjekcijeAdapter bazaProjekcija;
	
	/**
	 * Dohvaæa projekcije sa web servisa. To ne ukljuèuje one projekcije koji se veæ nalaze u lokalnoj bazi
	 * @return projekcije u Jsonformatu
	 */
	public List<ProjekcijaInfo> dohvatiProjekcije(Context c, int multipleks)
	{
		// najprije dohvaæamo id-eve projekcija iz lokalne baze podataka, kako bi znali koje projekcije ne trebamo dohvaæati sa webservisa
		bazaProjekcija = new ProjekcijeAdapter(c);
		List<Integer> idProjekcija = bazaProjekcija.dohvatiIdProjekcija();
		
		// pretvaramo listu s id-evima projekcija u JSON string 
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(int i=0; i<idProjekcija.size(); i++)
		{
			sb.append("{\"idProjekcije\":\"" + idProjekcija.get(i) + "\"}");
			// ako se ne radi o posljednjem elementu, dodat æemo zarez
			if (i != idProjekcija.size() - 1)
			{
				sb.append(",");
			}
		}
		sb.append("]");
		// finalni JSON string koji æemo slati web servisu putem HTTP-POST metode
		String jsonString = sb.toString();
		
		String mult = Integer.toString(multipleks);
		this.execute(jsonString,mult);
		String jsonRezultat = "";
		try {
			jsonRezultat = this.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}


			List<ProjekcijaInfo> projekcije = parsirajJson(jsonRezultat, c, multipleks);
			return projekcije;
		
	}
	
	/**
	 * Parsira JSON string dohvaæen s web servisa 
	 * @param jsonRezultat
	 * @return projekcije
	 */
	private List<ProjekcijaInfo> parsirajJson(String jsonRezultat, Context c, int multipleksOdabrani) {
		List<ProjekcijaInfo> projekcije	= new ArrayList<ProjekcijaInfo>();
		FilmoviAdapter filmAd = new FilmoviAdapter(c);
		int film;
		int dvorana;
		String vrijemePocetka;
		int cijena;		
		int multipleks;	
		int idProjekcije;
		
		try {
			JSONArray projekcijeJson = new JSONArray(jsonRezultat);
			int n = projekcijeJson.length();
			for(int i=0; i<n; i++)
			{
				JSONObject projekcijaJson = projekcijeJson.getJSONObject(i);
				film = projekcijaJson.getInt("film");
				dvorana = projekcijaJson.getInt("brojDvorane");
				vrijemePocetka = projekcijaJson.getString("vrijemePocetka");
				cijena = projekcijaJson.getInt("cijena");
				idProjekcije = projekcijaJson.getInt("idProjekcije");
				multipleks = projekcijaJson.getInt("multipleks");
				FilmInfo filmInf = filmAd.dohvatiDetaljeFilma(film);	
				/*TODO test ovog poziva"!!!*/
				ProjekcijaInfo projekcija = new ProjekcijaInfo(idProjekcije, dvorana, filmInf,vrijemePocetka, multipleks, cijena); 	
				
				projekcije.add(projekcija);
			}
		}
		catch (JSONException e) {
		
			e.printStackTrace();
		}

		
	    bazaProjekcija.azurirajBazuProjekcija(projekcije);

		// nakon ažuriranja baze, dohvaæamo ponovno sve filmove iz baze
		/*TODO provjeri jel ovo radi sa multiplekosm*/
		projekcije = bazaProjekcija.dohvatiProjekcije(multipleksOdabrani, c);
		
		return projekcije;
	}
	
	// pomoæna metoda koja u pozadini obraðuje http zahtjev (dohvaæanje podataka)
	@Override
	protected String doInBackground(String... parametri) {
		String jsonString = parametri[0];
		String multipleks = parametri[1];
	
		HttpClient httpKlijent = new DefaultHttpClient();
		// HTTP-POST metodom æemo poslati JSON string. U tom JSON stringu se nalaze indeksi svih projekcija koji se nalaze u lokalnoj bazi. Web servis æe vratiti sve one projekcije èiji se indeksi ne nalaze u tom JSON stringu
		HttpPost httpPost = new HttpPost("http://mkinoairprojekt.me.pn/skripte/index.php?tip=projekcijeMultipleks&multipleks="+multipleks);
		
		// dodajemo JSON string u "httpPost" objekt
		try {
			List<NameValuePair> podaci = new ArrayList<NameValuePair>();
			podaci.add(new BasicNameValuePair("bezOvihProjekcija", jsonString));
			httpPost.setEntity(new UrlEncodedFormEntity(podaci));
		} catch (UnsupportedEncodingException e1) {
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
