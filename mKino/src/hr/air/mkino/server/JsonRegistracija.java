package hr.air.mkino.server;

import hr.air.mkino.tipovi.Korisnik;

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

import android.os.AsyncTask;
/**
 * klasa uz pomoæ koje se obavlja komunikacija sa web servisom u svrhu registracije korisnika
 * 
 * */
public class JsonRegistracija extends AsyncTask<Korisnik, Integer, String> {

	/**
	 * Metoda koja služi za registraciju korisnika.
	 * @param popunjen objekt tipa Korisnik sa podacima za prijavu korisnika
	 * @return identifikacijski broj poruke o uspješnosti registracije
	 */
	public int registriraj(Korisnik korisnik)
	{			
		this.execute(korisnik);
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
	 * Parsira json string dohvaæen s web servisa
	 * @param jsonRezultat web servisa
	 * @return identifikacijski broj poruke o uspješnosti registracije
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
			return 7;
		}
		
		return povratnaInformacijaId;
	}

	/**
	 * Metoda za asinkronu komunikaciju izmeðu aplikacije i web servisa
	 * @param popunjen objekt tipa Korisnik sa podacima za prijavu korisnika
	 * @return rezultat web servisa u json formatu
	 */
	protected String doInBackground(Korisnik... korisnik) {
		HttpClient httpKlijent = new DefaultHttpClient();	 
		HttpPost httpPostZahtjev = new HttpPost("http://mkinoairprojekt.me.pn/skripte/index.php?tip=registracija");
		String jsonResult = "";
		ResponseHandler<String> handler = new BasicResponseHandler();		
		
		try {
			/*definiranje POST parametara*/
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		    nameValuePairs.add(new BasicNameValuePair("korisnickoIme", korisnik[0].getKorisnickoIme()));
		    nameValuePairs.add(new BasicNameValuePair("lozinka", korisnik[0].getLozinka()));
		    nameValuePairs.add(new BasicNameValuePair("ime", korisnik[0].getIme()));
		    nameValuePairs.add(new BasicNameValuePair("prezime", korisnik[0].getPrezime()));
		    nameValuePairs.add(new BasicNameValuePair("email", korisnik[0].getEmail()));
		    nameValuePairs.add(new BasicNameValuePair("telefon", korisnik[0].getTelefon()));
		    httpPostZahtjev.setEntity(new UrlEncodedFormEntity(nameValuePairs));		  
		    
		    /*slanje uz pomoæ POST zahtjeva*/
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
