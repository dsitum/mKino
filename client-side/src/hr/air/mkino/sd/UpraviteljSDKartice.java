package hr.air.mkino.sd;

import java.io.File;

/**
 * Slu�i za izvr�avanje osnovnih akcija na SD kartici
 * @author domagoj
 *
 */
public class UpraviteljSDKartice {
	/**
	 * Stati�na metoda koja bri�e datoteku sa zadane putanje.
	 * @param putanja
	 */
	public static void obrisiDatoteku(String putanja)
	{
		File datoteka = new File(putanja);
		datoteka.delete();
	}
}
