package hr.air.mkino.uzorcidizajna;

import java.io.File;

import android.content.Context;
import android.os.Environment;

import hr.air.mkino.sd.LokalnaSlika;
import hr.air.mkino.server.SlikaSaServera;
import hr.air.mkino.sucelja.ISlikaFilma;

/**
 * Predstavlja Factory uzorak dizajna za odabir mjesta s kojeg æe se preuzeti slika.
 * @author domagoj
 *
 */
public class UcitajSlikuFactory {
	
	/**
	 * Provjerava nalazi li se na SD kartici veæ slika filma. 
	 * Ako da, vraæa se objekt LokalnaSlika, a ako ne, vraæa se objekt SlikaSaServera.
	 * Pomoæu tih objekata se poslije može dohvatiti mala odnosno velika slika. 
	 * @param c (kontekst)
	 * @param idFilma
	 * @param velikaSlika (treba li dohvatiti sliku velikoga ili malog formata)
	 * @return objekt koji implementira ISlikaFilma suèelje (LokalnaSlika ili SlikaSaServera)
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
