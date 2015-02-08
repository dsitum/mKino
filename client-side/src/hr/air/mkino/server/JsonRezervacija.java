package hr.air.mkino.server;

import hr.air.mkino.tipovi.RezervacijaInfo;

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
 * Klasa koja služi za asinkronu komunikaciju sa web servisom sa ciljem rezervacije ulaznica.
 * @author bstivic
 *
 */
public class JsonRezervacija extends AsyncTask<String, Void, String> {
	
	public RezervacijaInfo rezerviraj(String korisnickoIme, int idProjekcije, List<Integer> sjedala)
	{	
		// pretvaramo listu s id-evima filmova u JSON string (moramo ruèno, buduæi da ne postoji neka standardna metoda za to
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(int i=0; i<sjedala.size(); i++)
		{
			sb.append("{\"sjedalo\":\"" + sjedala.get(i) + "\"}");
			// ako se ne radi o posljednjem elementu, dodat æemo zarez
			if (i != sjedala.size() - 1)
			{
				sb.append(",");
			}
		}
		sb.append("]");
		// finalni JSON string koji æemo slati web servisu putem HTTP-POST metode
		String jsonString = sb.toString();
				
		this.execute(korisnickoIme, Integer.toString(idProjekcije), jsonString);
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
	 * Metoda koja parsira JsonRezultat.
	 * @param jsonRezultat
	 * @return vraæa kod rezervacije koji se gradi na temelju "idKorisnika"-"idProjekcije"
	 */
	private RezervacijaInfo parsirajJson(String jsonRezultat) {		
		RezervacijaInfo rezervacija = null;
		
		
		try {

				String kodRezervacije="";
				JSONArray rezultati = new JSONArray(jsonRezultat);
				
				int n = rezultati.length();
				for(int i=0; i<n; i++) 
				{
					JSONObject rezultat = rezultati.getJSONObject(i);	
					kodRezervacije = rezultat.getString("povratnaInformacijaId");

				}
				if(n>0)	rezervacija = new RezervacijaInfo(0, -1, null, kodRezervacije, null);
				else rezervacija = null;
		}			
		catch (JSONException e) {	
		/*
		 * izvršava se ukoliko prijava nije uspješna, odnosno ne postoji
		 *  korisnik sa traženim korisnièkim imenom i lozinkom
		 *  */
			e.printStackTrace();				
		}
		
		return rezervacija;
	}

	/**
	 * Metoda za asinkronu komunikaciju izmeðu aplikacije i servisa.
	 * @param korisnicko ime i lozinka u obliku ArrayList
	 * @return odgovor servisa u json obliku
	 * */
	protected String doInBackground(String... podaciPrijava) {
		HttpClient httpKlijent = new DefaultHttpClient();
	 
		HttpPost httpPostZahtjev = new HttpPost("http://mkinoairprojekt.me.pn/skripte/index.php?tip=rezervacija");
		String jsonResult = "";
		ResponseHandler<String> handler = new BasicResponseHandler();			
		
		
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		    nameValuePairs.add(new BasicNameValuePair("projekcija", podaciPrijava[1]));
		    nameValuePairs.add(new BasicNameValuePair("korisnik", podaciPrijava[0]));
		    nameValuePairs.add(new BasicNameValuePair("sjedala", podaciPrijava[2])); 
		  
	
		// ono što dohvatimo u jsonRezultat æe biti odgovor servera (web servisa)
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
