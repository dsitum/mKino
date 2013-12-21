package hr.air.mkino.server;

import hr.air.mkino.tipovi.MultipleksInfo;

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

public class JsonMultipleksi extends AsyncTask<Void, Void, String> {
	public List<MultipleksInfo> dohvatiMultiplekse()
	{
		// dohvatiti podatke s web servsa		
		this.execute();
		String jsonRezultat = "";
		try {
			jsonRezultat = this.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<MultipleksInfo> multipleksi = parsirajJson(jsonRezultat);
		return multipleksi;
	}

	private List<MultipleksInfo> parsirajJson(String jsonRezultat) {
		List<MultipleksInfo> multipleksi = new ArrayList<MultipleksInfo>();
		int idMultipleksa;
		String naziv;
		String oznaka;
		float zemljopisnaDuzina;
		float zemljopisnaSirina;
		
		try {
			JSONArray mpleksi = new JSONArray(jsonRezultat);
			int n = mpleksi.length();
			for(int i=0; i<n; i++)
			{
				JSONObject mpleks = mpleksi.getJSONObject(i);
				idMultipleksa = mpleks.getInt("idMultipleksa");
				naziv = mpleks.getString("naziv");
				oznaka = mpleks.getString("oznaka");
				zemljopisnaDuzina = Float.valueOf(mpleks.getString("zemljopisnaDuzina"));
				zemljopisnaSirina = Float.valueOf(mpleks.getString("zemljopisnaSirina"));
				MultipleksInfo multipleks = new MultipleksInfo(idMultipleksa, naziv, oznaka, zemljopisnaDuzina, zemljopisnaSirina);
				
				multipleksi.add(multipleks);
			}
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		return multipleksi;
	}

	@Override
	protected String doInBackground(Void... params) {
		HttpClient httpKlijent = new DefaultHttpClient();
		HttpGet httpZahtjev = new HttpGet("http://mkinoairprojekt.me.pn/skripte/index.php?tip=multipleksi");
		
		String jsonResult = "";
		ResponseHandler<String> handler = new BasicResponseHandler();
		
		try {
			jsonResult = httpKlijent.execute(httpZahtjev, handler);
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
