package hr.air.mkino.server;


import hr.air.mkino.baza.ProjekcijeAdapter;
import hr.air.mkino.tipovi.ProjekcijaInfo;
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

import android.content.Context;
import android.os.AsyncTask;

public class JsonObrisiRegistraciju extends AsyncTask<String, Void, String> {
	
	public List<RezervacijaInfo> dohvati(String kod, Context c)
	{					
		this.execute(kod);
		String jsonRezultat = "";
		try {
			jsonRezultat = this.get();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		} catch (ExecutionException e) {
			
			e.printStackTrace();
		}
		
		return  parsirajJson(jsonRezultat, c);			
	}

	
	private List<RezervacijaInfo> parsirajJson(String jsonRezultat, Context c) {		
		List<RezervacijaInfo> rezervacija = new ArrayList<RezervacijaInfo>();		
		ProjekcijaInfo projekcija = null;
		ProjekcijeAdapter projekcijaAdapter = new ProjekcijeAdapter(c);
		try {
			int idRezervacije = 0 ;
			int idProjekcije = -6;
			int pomocniIdProjekcije = -7;
	
			List<Integer> sjedala = null;
			String korisnickoIme = null;	
			String kodRezervacije = null;
			JSONArray rezultati = new JSONArray(jsonRezultat);
				
			int n = rezultati.length();
			for(int i=0; i<n; i++) 
			{
				JSONObject rezultat = rezultati.getJSONObject(i);
				idProjekcije = rezultat.getInt("idProjekcije");
				
				//uvjet æe se izvršiti ukoliko imamo više sjedala za istu projekciju, pa ih unosimo u listu i nije potrebno upisivat veæ poznate podatke o pr0jekciji
				if(idProjekcije == pomocniIdProjekcije)
				{
					sjedala.add(rezultat.getInt("sjedalo"));						
				}
				//inaèe se radi o drugoj projekciji pa je potrebno unijeti nove podatke
				else
				{
					if(i != 0)
					{
						projekcija = projekcijaAdapter.dohvatiProjekciju(idProjekcije, c);
						rezervacija.add(new RezervacijaInfo(idRezervacije, idProjekcije, korisnickoIme, kodRezervacije, sjedala, projekcija));
						
					}
					
					sjedala = new ArrayList<Integer>();
					idRezervacije = rezultat.getInt("idRezervacije");						
					korisnickoIme= rezultat.getString("korisnickoIme");
					kodRezervacije = rezultat.getString("kod");
					sjedala.add(rezultat.getInt("sjedalo"));	
				}
				
			}
				
		}			
		catch (JSONException e) {	

			e.printStackTrace();				
		}
		
		return rezervacija;
	}


	protected String doInBackground(String... podaciPrijava) {
		HttpClient httpKlijent = new DefaultHttpClient();
	 
		HttpPost httpPostZahtjev = new HttpPost("http://mkinoairprojekt.me.pn/skripte/index.php?tip=obrisiRezervaciju");
		String jsonResult = "";
		ResponseHandler<String> handler = new BasicResponseHandler();			
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("kod", podaciPrijava[0]));
  	

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
