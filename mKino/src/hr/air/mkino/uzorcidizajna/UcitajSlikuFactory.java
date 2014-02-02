package hr.air.mkino.uzorcidizajna;

import java.io.File;

import android.content.Context;
import android.os.Environment;

import hr.air.mkino.sd.LokalnaSlika;
import hr.air.mkino.server.SlikaSaServera;
import hr.air.mkino.sucelja.ISlikaFilma;

public class UcitajSlikuFactory {
	public static ISlikaFilma ucitaj(Context c, int idFilma, boolean velikaSlika)
	{
		File slika;
		
		if (velikaSlika)
		{
			slika = new File(Environment.getExternalStorageDirectory().getPath() + "/mKino/" + idFilma + ".jpg");
		} else
		{
			slika = new File(Environment.getExternalStorageDirectory().getPath() + "/mKino/" + idFilma + "_mala.jpg");
		}
		
		if (slika.exists())
		{
			return new LokalnaSlika(idFilma);
		} else
		{
			return new SlikaSaServera(c, idFilma);
		}
	}
}
