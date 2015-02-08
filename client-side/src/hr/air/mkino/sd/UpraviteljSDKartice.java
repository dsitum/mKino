package hr.air.mkino.sd;

import java.io.File;

/**
 * Služi za izvršavanje osnovnih akcija na SD kartici
 * @author domagoj
 *
 */
public class UpraviteljSDKartice {
	/**
	 * Statièna metoda koja briše datoteku sa zadane putanje.
	 * @param putanja
	 */
	public static void obrisiDatoteku(String putanja)
	{
		File datoteka = new File(putanja);
		datoteka.delete();
	}
}
