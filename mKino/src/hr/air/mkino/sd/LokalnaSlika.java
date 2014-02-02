package hr.air.mkino.sd;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import hr.air.mkino.sucelja.ISlikaFilma;

/**
 * Slu�i za dohva�anje slike filma sa SD kartice.
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
	 * Dohva�a sliku filma u velikom formatu.
	 * @return Bitmap objekt slike
	 */
	public Bitmap dohvatiVelikuSliku() {
		Bitmap slika = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath() + "/mKino/" + idFilma + ".jpg");
		return slika;
	}

	/**
	 * Dohva�a sliku filma u malom formatu (naj�e��e za prikaz u ListView-u).
	 * @return Bitmap objekt slike
	 */
	public Bitmap dohvatiMaluSliku() {
		Bitmap slika = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath() + "/mKino/" + idFilma + "_mala.jpg");
		return slika;
	}
}
