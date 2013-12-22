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
/**
 * klasa koja služi za prijavu korisnika u sustav uz pomoæ asinkrone komunikacije 
 * izmeðu aplikacije i servisa koji provjerava da li postoji korisnik u bazi 
 * podataka sa navedenim korisnièkim imenom i lozinkom
 *  
 * */
public class JsonPrijava extends AsyncTask<String, Korisnik, String> {

		/**
		 * Metoda koja izvršava provjeru korisnika uz pomoæ servisa
		 * @param korisnicko ime i odgovarajuæa lozinka
		 * @return popunjeni objekt tipa Korisnik(korisnickoIme, "", ime, prezime, email, telefon) 
		 * ukoliko je prijava uspjesna  ili null ukoliko prijava nije uspješna
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
		 * Parsira json string dohvaæen s web servisa
		 * @param jsonRezultat
		 * @return popunjeni objekt tipa Korisnik(korisnickoIme, "", ime, prezime, email, telefon) 
		 * ukoliko je prijava uspjesna ili null ukoliko prijava nije uspjesna
		 */
		private Korisnik parsirajJson(String jsonRezultat) {		
			Korisnik korisnik = null;
			
			try {
					JSONArray rezultati = new JSONArray(jsonRezultat);
					int n = rezultati.length();
					for(int i=0; i<n; i++)
					{
						JSONObject rezultat = rezultati.getJSONObject(i);
						
						String korisnickoIme = rezultat.getString("korisnickoIme");
						/*lozinka se ne dohvaæa putem web servisa*/
						String lozinka = "";
						String ime = rezultat.getString("ime");
						String prezime = rezultat.getString("prezime");
						String email = rezultat.getString("email");
						String telefon = rezultat.getString("telefon");
						/*unošenje primljenih korisnièkih podataka u objekt*/
						korisnik =  new Korisnik(korisnickoIme, lozinka, ime, prezime, email, telefon);
						
					}
			}			
			catch (JSONException e) {	
			/*
			 * izvršava se ukoliko prijava nije uspješna, odnosno ne postoji
			 *  korisnik sa traženim korisnièkim imenom i lozinkom
			 *  */
				e.printStackTrace();				
			}
			
			return korisnik;
		}

		/**
		 * Metoda za asinkronu komunikaciju izmeðu aplikacije i servisa.
		 * @param korisnicko ime i lozinka u obliku ArrayList
		 * @return odgovor servisa u json obliku
		 * */
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
