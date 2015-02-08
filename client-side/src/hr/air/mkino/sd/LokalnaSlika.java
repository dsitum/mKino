package hr.air.mkino.sd;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import hr.air.mkino.sucelja.ISlikaFilma;

/**
 * Služi za dohvaæanje slike filma sa SD kartice.
 * Za konstruktor prima ID filma kojeg dalje koristi u metodama.
 * @author domagoj
 *
 */
public class LokalnaSlika implements ISlikaFilma {
	int idFilma;
	
	public LokalnaSlika(int idFilma) {
		this.idFilma = idFilma;
	}
	
	/**
	 * Dohvaæa sliku filma u velikom formatu.
	 * @return Bitmap objekt slike
	 */
	public Bitmap dohvatiVelikuSliku() {
		Bitmap slika = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath() + "/mKino/" + idFilma + ".jpg");
		return slika;
	}

	/**
	 * Dohvaæa sliku filma u malom formatu (najèešæe za prikaz u ListView-u).
	 * @return Bitmap objekt slike
	 */
	public Bitmap dohvatiMaluSliku() {
		Bitmap slika = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath() + "/mKino/" + idFilma + "_mala.jpg");
		return slika;
	}
}
