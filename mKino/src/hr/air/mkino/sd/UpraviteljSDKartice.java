package hr.air.mkino.sd;

import java.io.File;

public class UpraviteljSDKartice {
	public static void obrisiDatoteku(String putanja)
	{
		File datoteka = new File(putanja);
		datoteka.delete();
	}
}
