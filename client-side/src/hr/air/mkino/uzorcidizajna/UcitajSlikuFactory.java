package hr.air.mkino.uzorcidizajna;

import java.io.File;

import android.content.Context;
import android.os.Environment;

import hr.air.mkino.sd.LokalnaSlika;
import hr.air.mkino.server.SlikaSaServera;
import hr.air.mkino.sucelja.ISlikaFilma;

/**
 * Predstavlja Factory uzorak dizajna za odabir mjesta s kojeg �e se preuzeti slika.
 * @author domagoj
 *
 */
public class UcitajSlikuFactory {
	
	/**
	 * Provjerava nalazi li se na SD kartici ve� slika filma. 
	 * Ako da, vra�a se objekt LokalnaSlika, a ako ne, vra�a se objekt SlikaSaServera.
	 * Pomo�u tih objekata se poslije mo�e dohvatiti mala odnosno velika slika. 
	 * @param c (kontekst)
	 * @param idFilma
	 * @param velikaSlika (treba li dohvatiti sliku velikoga ili malog formata)
	 * @return objekt koji implementira ISlikaFilma su�elje (LokalnaSlika ili SlikaSaServera)
	 */
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
