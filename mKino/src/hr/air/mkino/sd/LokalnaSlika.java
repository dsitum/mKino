package hr.air.mkino.sd;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import hr.air.mkino.sucelja.ISlikaFilma;

public class LokalnaSlika implements ISlikaFilma {
	int idFilma;
	
	public LokalnaSlika(int idFilma) {
		this.idFilma = idFilma;
	}
	
	@Override
	public Bitmap dohvatiVelikuSliku() {
		Bitmap slika = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath() + "/mKino/" + idFilma + ".jpg");
		return slika;
	}

	@Override
	public Bitmap dohvatiMaluSliku() {
		Bitmap slika = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath() + "/mKino/" + idFilma + "_mala.jpg");
		return slika;
	}
}
