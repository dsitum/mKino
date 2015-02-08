package hr.air.mkino.server;

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


import android.os.AsyncTask;
/**
 * klasa koja slu�i za prijavu korisnika u sustav uz pomo� asinkrone komunikacije 
 * izme�u aplikacije i servisa koji provjerava da li postoji korisnik u bazi 
 * podataka sa navedenim korisni�kim imenom i lozinkom
 * @author bstivic
 * */
public class JsonDohvatiSjedala extends AsyncTask<Integer, List<Integer>, String> {


		public List<Integer> dohvatiSjedala(int idProjekcije)
		{			
			this.execute(idProjekcije);
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
		 * Parsira json string dohva�en s web servisa
		 * @param jsonRezultat
		 * @return popunjeni objekt tipa Korisnik(korisnickoIme, "", ime, prezime, email, telefon) 
		 * ukoliko je prijava uspjesna ili null ukoliko prijava nije uspjesna
		 */
		
		private List<Integer> parsirajJson(String jsonRezultat) {		

			List<Integer> listaZauzetih = new ArrayList<Integer>() ;
			try {
					JSONArray rezultati = new JSONArray(jsonRezultat);
					int n = rezultati.length();
					for(int i=0; i<n; i++)
					{
						JSONObject rezultat = rezultati.getJSONObject(i);
						
						String sjedaloStrng = rezultat.getString("brojSjedala");
						listaZauzetih.add(Integer.parseInt(sjedaloStrng));
						
					}
			}			
			catch (JSONException e) {	
			/*
			 * izvr�ava se ukoliko prijava nije uspje�na, odnosno ne postoji
			 *  korisnik sa tra�enim korisni�kim imenom i lozinkom
			 *  */
				e.printStackTrace();				
			}
			
			return listaZauzetih;
		}

		/**
		 * Metoda za asinkronu komunikaciju izme�u aplikacije i servisa.
		 * @param korisnicko ime i lozinka u obliku ArrayList
		 * @return odgovor servisa u json obliku
		 * */
		protected String doInBackground(Integer... idProjekcije) {
			HttpClient httpKlijent = new DefaultHttpClient();
		 
			HttpGet httpGetZahtjev = new HttpGet("http://mkinoairprojekt.me.pn/skripte/index.php?tip=sjedalaProjekcija&projekcija="+idProjekcije[0]);
			String jsonResult = "";
			ResponseHandler<String> handler = new BasicResponseHandler();
			
			
			try {			
					  
				jsonResult = httpKlijent.execute(httpGetZahtjev, handler);
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
