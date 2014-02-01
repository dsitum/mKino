package hr.air.mkino.sd;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import hr.air.mkino.sucelja.ISlikaFilma;

public class LokalnaSlika implements ISlikaFilma {

	@Override
	public Bitmap dohvatiVelikuSliku(int idFilma) {
		Bitmap slika = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath() + "/mKino/" + idFilma + ".jpg");
		return slika;
	}

	@Override
	public Bitmap dohvatiMaluSliku(int idFilma) {
		Bitmap slika = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath() + "/mKino/" + idFilma + "_mala.jpg");
		return slika;
	}
}
