package hr.air.mkino.lokacija;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Ova klasa slu�i za dohva�anje trenutne lokacije.
 * @author domagoj
 */
public class PratiteljLokacije implements LocationListener {
	private final Context kontekst;
	
	private boolean gpsUkljucen;
	private boolean mrezaUkljucena;
	
	private Location lokacija;
	private LocationManager upraviteljLokacije;
	
	private double zemljopisnaSirina;
	private double zemljopisnaDuzina;
	private boolean lokacijaDostupna = false;
	
	public PratiteljLokacije(Context c) {
		kontekst = c;
		dohvatiLokaciju();
	}
	
	/**
	 * Dohva�a trenutnu korisnikovu lokaciju putem GSM-a i GPS-a. 
	 * Najprije poku�ava dohvatiti lokaciju putem GSM-a (ukoliko je mre�a uklju�ena).
	 * Zatim dohva�a lokaciju sa GPS-a, ako je dostupan.
	 * Dohva�enu lokaciju zapisuje u klasnu varijablu.
	 */
	public void dohvatiLokaciju()
	{
		upraviteljLokacije = (LocationManager) kontekst.getSystemService(Context.LOCATION_SERVICE);
		
		gpsUkljucen = upraviteljLokacije.isProviderEnabled(LocationManager.GPS_PROVIDER);
		mrezaUkljucena = upraviteljLokacije.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		
		//najprije dohva�amo lokaciju koriste�i mre�nog operatera / internet
		if (mrezaUkljucena)
		{
			upraviteljLokacije.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 10, this);
			lokacija = upraviteljLokacije.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (lokacija != null)
			{
				lokacijaDostupna = true;
				zemljopisnaSirina = lokacija.getLatitude();
				zemljopisnaDuzina = lokacija.getLongitude();
			}
		}

		if (gpsUkljucen)
		{
			upraviteljLokacije.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 10, this);
			lokacija = upraviteljLokacije.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (lokacija != null)
			{
				lokacijaDostupna = true;
				zemljopisnaSirina = lokacija.getLatitude();
				zemljopisnaDuzina = lokacija.getLongitude();
			}
		}
		
		
	}
	
	/**
	 * Ra�una zra�nu udaljenost izme�u odabrane to�ke i dohva�ene lokacije.
	 * Odabrana to�ka je, u primjeni, jedan od multipleksa.
	 * @param zemljopisnaDuzina
	 * @param zemljopisnaSirina
	 * @return udaljenost u metrima
	 */
	public float udaljenostDo(float zemljopisnaDuzina, float zemljopisnaSirina)
	{
		float[] udaljenost = new float[3];
		Location.distanceBetween(this.zemljopisnaSirina, this.zemljopisnaDuzina, zemljopisnaSirina, zemljopisnaDuzina, udaljenost);
		
		return udaljenost[0];
	}
	
	public double dohvatiZemljopisnuSirinu()
	{
		return zemljopisnaSirina;
	}
	
	public double dohvatiZemljopisnuDuzinu()
	{
		return zemljopisnaDuzina;
	}
	
	public boolean lokacijaDostupna()
	{
		return lokacijaDostupna;
	}
	
	@Override
	public void onLocationChanged(Location lokacija) {
		// TODO Auto-generated method stub
		upraviteljLokacije.removeUpdates(this);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
