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

import hr.air.mkino.tipovi.Korisnik;
import android.os.AsyncTask;

public class JsonPrijava extends AsyncTask<String, Korisnik, String> {

		/**
		 * metoda koja izvršava prijavu putem POST zahtjeva
		 * @return int sa uspjesnoId
		 */
		public Korisnik prijavi(String korisnickoIme, String lozinka)
		{			
			this.execute(korisnickoIme,lozinka );
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
		 * Parsira JSON string dohvaæen s web servisa
		 * @param jsonRezultat
		 * @return integer koji oznaèava uspješnost
		 */
		private Korisnik parsirajJson(String jsonRezultat) {		
			Korisnik korisnik = null;
			//String povratnaInformacijaTekst;	
			
			try {
				JSONArray rezultati = new JSONArray(jsonRezultat);
				int n = rezultati.length();
				
					for(int i=0; i<n; i++)
					{
					JSONObject rezultat = rezultati.getJSONObject(i);
					
					String korisnickoIme = rezultat.getString("korisnickoIme");
					String lozinka = "";
					String ime = rezultat.getString("ime");
					String prezime = rezultat.getString("prezime");
					String email = rezultat.getString("email");
					String telefon = rezultat.getString("telefon");
					
					korisnik =  new Korisnik(korisnickoIme, lozinka, ime, prezime, email, telefon);
					
					}
				}
			
			catch (JSONException e) {
				e.printStackTrace();				
			}
			
			return korisnik;
		}

		
		protected String doInBackground(String... podaciPrijava) {
			HttpClient httpKlijent = new DefaultHttpClient();
		 
			HttpPost httpPostZahtjev = new HttpPost("http://mkinoairprojekt.me.pn/skripte/index.php?tip=prijava");
			String jsonResult = "";
			ResponseHandler<String> handler = new BasicResponseHandler();
			
			
			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			    nameValuePairs.add(new BasicNameValuePair("korisnickoIme", podaciPrijava[0]));
			    nameValuePairs.add(new BasicNameValuePair("lozinka", podaciPrijava[1]));
			    
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
