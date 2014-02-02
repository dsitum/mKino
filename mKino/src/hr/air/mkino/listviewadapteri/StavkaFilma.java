package hr.air.mkino.listviewadapteri;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import hr.air.mkino.R;
import hr.air.mkino.sucelja.ISlikaFilma;
import hr.air.mkino.tipovi.FilmInfo;
import hr.air.mkino.uzorcidizajna.UcitajSlikuFactory;

/**
 * Klasa je adapter koji preslikava osnovne podatke o filmu (ukljuèujuæi i sliku) u ListView.
 * Napravljena je kako bi se omoguæio prikaz slika u ListView-u.
 * @author domagoj
 */
public class StavkaFilma extends ArrayAdapter<FilmInfo> {
	private List<FilmInfo> filmovi;
	LayoutInflater li;

	public StavkaFilma(Context context, int stavkaFilma, List<FilmInfo> filmovi) {
		super(context, stavkaFilma, filmovi);
		this.filmovi = filmovi;
		li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  // dohvaæanje usluge "napuhivanja" layouta
	}
	
	@Override
	public View getView(int pozicija, View stavkaFilma, ViewGroup roditelj) {
		// dohvaæamo stavku filma s listview-a
		View stavka = stavkaFilma;
		if (stavka == null)
		{
			stavka = li.inflate(R.layout.stavka_filma, roditelj, false);
		}
		
		// dohvaæamo trenutni film
		FilmInfo trenutniFilm = filmovi.get(pozicija);
		
		// postavljamo sliku filma
		ImageView slikaFilma = (ImageView)stavka.findViewById(R.id.mala_slika_filma);
		ISlikaFilma slika = UcitajSlikuFactory.ucitaj(getContext(), trenutniFilm.getIdFilma(), false);
		slikaFilma.setImageBitmap(slika.dohvatiMaluSliku());
		
		// postavljamo ostale podatke
		TextView idFilma = (TextView) stavka.findViewById(R.id.id_filma_u_bazi);
		idFilma.setText(String.valueOf(trenutniFilm.getIdFilma()));
		TextView nazivFilma = (TextView) stavka.findViewById(R.id.naziv_filma_mali);
		nazivFilma.setText(trenutniFilm.getNaziv());
		TextView glavneUloge = (TextView) stavka.findViewById(R.id.uloge_filma_male);
		glavneUloge.setText(trenutniFilm.getGlavneUloge());
		
		return stavka;
	}
}
