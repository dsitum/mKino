package hr.air.mkino.listviewadapteri;

import java.util.List;
import hr.air.mkino.R;
import hr.air.mkino.sucelja.ISlikaFilma;
import hr.air.mkino.tipovi.ProjekcijaInfo;
import hr.air.mkino.uzorcidizajna.UcitajSlikuFactory;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StavkaProjekcije extends ArrayAdapter<ProjekcijaInfo> {
	private List<ProjekcijaInfo> projekcije;
	LayoutInflater li;
	
	
	public StavkaProjekcije(Context context, int stavkaProjekcije, List<ProjekcijaInfo> projekcije) {
		super(context, stavkaProjekcije, projekcije);
		this.projekcije = projekcije;
		li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  // dohvaæanje usluge "napuhivanja" layouta
	}
	
	@Override
	public View getView(int pozicija, View stavkaProjekcije, ViewGroup roditelj) {
		// dohvaæamo stavku projekcije s listview-a
		View stavka = stavkaProjekcije;
		if (stavka == null)
		{
			stavka = li.inflate(R.layout.stavka_projekcije, roditelj, false);
		}
		
		// dohvaæamo trenutnu projekciju
		ProjekcijaInfo trenutnaProjekcija = projekcije.get(pozicija);
		
		// postavljamo sliku filma
		ImageView slikaFilma = (ImageView)stavka.findViewById(R.id.mala_slika_projekcije);
		ISlikaFilma slika = UcitajSlikuFactory.ucitaj(getContext(), trenutnaProjekcija.getIdFilma(), false);
		slikaFilma.setImageBitmap(slika.dohvatiMaluSliku());
		
		// postavljamo ostale podatke
		TextView idProjekcije = (TextView) stavka.findViewById(R.id.id_projekcije_u_bazi);
		idProjekcije.setText(String.valueOf(trenutnaProjekcija.getidProjekcije()));
		TextView nazivFilma = (TextView) stavka.findViewById(R.id.naziv_projekcije_mali);
		nazivFilma.setText(trenutnaProjekcija.getNaziv());
		TextView dvoranaProjekcije = (TextView) stavka.findViewById(R.id.dvorana_projekcije_mali);
		dvoranaProjekcije.setText("Dvorana " + trenutnaProjekcija.getDvorana());
		TextView vrijemeProjekcije = (TextView) stavka.findViewById(R.id.vrijeme_projekcije_mali);
		vrijemeProjekcije.setText(trenutnaProjekcija.getVrijemePocetka().substring(11, 16));
		
		return stavka;
	}
}
