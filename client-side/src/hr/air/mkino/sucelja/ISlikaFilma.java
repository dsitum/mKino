package hr.air.mkino.sucelja;

import android.graphics.Bitmap;

/**
 * Suèelje koje implementiraju klasa za dohvat slike sa SD kartice i klasa za dohvat slike sa servera. 
 * @author domagoj
 *
 */
public interface ISlikaFilma {
	public Bitmap dohvatiVelikuSliku();
	public Bitmap dohvatiMaluSliku();
}
