package hr.air.mkino.server;


import hr.air.mkino.baza.ProjekcijeAdapter;
import hr.air.mkino.tipovi.ProjekcijaInfo;
import hr.air.mkino.tipovi.RezervacijaInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
/**
 * Klasa koja slu�i za dohva�anje korisni�kih rezervacija sa web servisa.
 * @author bstivic
 *
 */
public class JsonMojeRezervacije extends AsyncTask<String, Void, String> {
	
	public List<RezervacijaInfo> dohvati(String korisnickoIme, Context c)
	{					
		this.execute(korisnickoIme);
		String jsonRezultat = "";
		try {
			jsonRezultat = this.get();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		} catch (ExecutionException e) {
			
			e.printStackTrace();
		}
		
		return parsirajJson(jsonRezultat, c);			
	}

	/**
	 * Metoda koja slu�i za parsiranje json odgovora servera
	 * @param jsonRezultat
	 * @param context
	 * @return lista rezervacija
	 */
	private List<RezervacijaInfo> parsirajJson(String jsonRezultat, Context c) {		
		List<RezervacijaInfo> rezervacija = new ArrayList<RezervacijaInfo>();		
		ProjekcijaInfo projekcija = null;
		ProjekcijeAdapter projekcijaAdapter = new ProjekcijeAdapter(c);
		try {
			int idRezervacije = 0 ;
			int idProjekcije = -6;
			int pomocniIdProjekcije = -7;
	
			List<Integer> sjedala = null;
			String kodRezervacije = null;
			JSONArray rezultati = new JSONArray(jsonRezultat);
				
			int n = rezultati.length();
			for(int i=0; i<n; i++) 
			{
				JSONObject rezultat = rezultati.getJSONObject(i);
				idProjekcije = rezultat.getInt("idProjekcije");
				
				//uvjet �e se izvr�iti ukoliko imamo vi�e sjedala za istu projekciju, pa ih unosimo u listu i nije potrebno upisivat ve� poznate podatke o pr0jekciji
				if(idProjekcije == pomocniIdProjekcije)
				{
					sjedala.add(rezultat.getInt("brojSjedala"));	
					if(i == n-1)
					{
						projekcija = projekcijaAdapter.dohvatiProjekciju(idProjekcije, c);
						rezervacija.add(new RezervacijaInfo(idRezervacije, idProjekcije, "", kodRezervacije, sjedala, projekcija));
					}
				}
				//ina�e se radi o drugoj projekciji pa je potrebno unijeti nove podatke
				else
				{
					if(i != 0)
					{
						projekcija = projekcijaAdapter.dohvatiProjekciju(pomocniIdProjekcije, c);
						rezervacija.add(new RezervacijaInfo(idRezervacije, pomocniIdProjekcije, "", kodRezervacije, sjedala, projekcija));
					}
					
					sjedala = new ArrayList<Integer>();
					idRezervacije = rezultat.getInt("idRezervacije");						
					kodRezervacije = rezultat.getString("kod");
					sjedala.add(rezultat.getInt("brojSjedala"));	
					pomocniIdProjekcije = idProjekcije;
				}
				
			}
				
		}			
		catch (JSONException e) {	

			e.printStackTrace();				
		}
		
		return rezervacija;
	}

	/**
	 * Pomo�na metoda koja u pozadini obra�uje http zahtjev (dohva�anje podataka)
	 */
	@Override
	protected String doInBackground(String... parametri) {
		String korisnik = parametri[0];

		HttpClient httpKlijent = new DefaultHttpClient();
		HttpGet httpZahtjev = new HttpGet("http://mkinoairprojekt.me.pn/skripte/index.php?tip=mr&korisnik="+korisnik);
		
		String jsonResult = "";
		ResponseHandler<String> handler = new BasicResponseHandler();
		
		try {
			jsonResult = httpKlijent.execute(httpZahtjev, handler);
		}
		catch(ClientProtocolException e){
			String pogreska = e.toString();
			e.printStackTrace();
		
			pogreska.charAt(0);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		httpKlijent.getConnectionManager().shutdown();
	
		return jsonResult;
	}

}
