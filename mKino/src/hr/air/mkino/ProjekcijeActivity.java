package hr.air.mkino;

import hr.air.mkino.core.Rezervacija;
import hr.air.mkino.listviewadapteri.StavkaProjekcije;
import hr.air.mkino.server.JsonFilmovi;
import hr.air.mkino.server.JsonProjekcije;
import hr.air.mkino.tipovi.ProjekcijaInfo;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**Klasa koja služi za prikaz projekcija.
 *   
 * @author bstivic
 */
public class ProjekcijeActivity extends Activity {
	private Spinner odabirGrada;
	private Spinner odabirDatuma;
	private ListView popisProjekcija;
	private List<ProjekcijaInfo> projekcije;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_projekcije);
		setTitle("Popis projekcija");
		
		// inicijaliziramo klasne varijable
		odabirGrada = (Spinner) findViewById(R.id.rezervacije_spinner_grad);
		odabirDatuma = (Spinner) findViewById(R.id.rezervacije_spiner_datum);
		popisProjekcija= (ListView) findViewById(R.id.rezervacije_popis_projekcija);
	
		int odabraniMultipleks = dohvatiOdabraniMultipleks();

		// postavljamo spinner na trenutno odabrani multipleks
		odabirGrada.setSelection(odabraniMultipleks-1);
		
		// dohvaæamo aktualne filmove s web servisa, i pritom punimo/modificiramo lokalnu bazu filmova iz koje æe se kasnije vuæi podaci
		JsonFilmovi jf = new JsonFilmovi();
		jf.dohvatiFilmove(this);
		
		//postavljamo spinner na današnji datum i popunjavamo ga sa datumima u iduæa dva tjedna
		ArrayList<String> dani = new ArrayList<String>();
		
		    Date sadasnjiDatum = new Date();
		    SimpleDateFormat formatDatuma = new SimpleDateFormat("dd.MM.yyyy");

		    //Zapisujemo 14 slijedeæih kalendarskih dana u spinner
		    for (int i = 0; i < 14; i++)
		    {			
		    	dani.add(formatDatuma.format(sadasnjiDatum)+" " +vratiDanUTjednu(sadasnjiDatum));
		    	sadasnjiDatum = Rezervacija.dodajDan(sadasnjiDatum, 1);
		    }
		    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		            android.R.layout.simple_spinner_item, dani);

		    
		    odabirDatuma.setAdapter(adapter);
		    odabirDatuma.setSelection(0);
		    
		   //raèunamo koji je datum u spinneru s datumom
		    sadasnjiDatum = new Date();
		    odabirDatuma.setOnItemSelectedListener(new OnItemSelectedListener() {
		    	@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long indeksDana) {										
		    		Date sadasnjiDatum = new Date();
		    		
		    		sadasnjiDatum = Rezervacija.dodajDan(sadasnjiDatum,(int)indeksDana);		    		
		   		    SimpleDateFormat formatDatuma2 = new SimpleDateFormat("yyyy-MM-dd");
		   		    
		   		    //uèitavamo datume u listview
		    		ucitajProjekcijeUListView(formatDatuma2.format(sadasnjiDatum));		    		
				}
				
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {}		    
		    
		    });
		    
		    //prilikom odabira grada mijenja se prikaz projekcija za odabrani grad
		    odabirGrada.setOnItemSelectedListener(new OnItemSelectedListener() {
		    	@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long idOdabranogMultipleksa) {										

		    		//ponovno zahtjevamo projekcije za odabrani grad
		    		projekcije = dohvatiProjekcije((int)idOdabranogMultipleksa+1);	
		    		
		    		Date sadasnjiDatum = new Date();
		    		sadasnjiDatum = Rezervacija.dodajDan(sadasnjiDatum,(int)odabirDatuma.getSelectedItemPosition());
		    		SimpleDateFormat formatDatuma2 = new SimpleDateFormat("yyyy-MM-dd");
		    		//uèitavamo datume u listview
		    		ucitajProjekcijeUListView(formatDatuma2.format(sadasnjiDatum));

				}
				
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {}
			});
		    
		// postavljamo listener na ListView s projekcijama
		popisProjekcija.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View kliknutaProjekcija, int pozicijaKliknuteProjekcije, long idKli) {
				TextView tv = (TextView) kliknutaProjekcija.findViewById(R.id.id_projekcije_u_bazi);
				int idProjekcijeUBazi = Integer.parseInt(tv.getText().toString());
				
				//prilikom kilika otvaraju se detalji o projekciji
				Intent i = new Intent(ProjekcijeActivity.this, DetaljiProjekcijeActivity.class);
				i.putExtra("idProjekcijeUBazi", idProjekcijeUBazi);
				startActivity(i);
			}
		});
		
		// dohvaæamo filmove i pohranjujemo ih u ListView "popisFilmova". Tu se automatski prikazuju na zaslon
		projekcije = dohvatiProjekcije(odabraniMultipleks);
		SimpleDateFormat formatDatuma2 = new SimpleDateFormat("yyyy-MM-dd");
		ucitajProjekcijeUListView(formatDatuma2.format(sadasnjiDatum));
	
	}
		
	
	/**Postavlja spinner za izbor multipleksa na vrijednost trenutnog multipleksa u bazi.
	 * Ukoliko on ne postoji ondje, odabire se prvi multipleks (indeks 0) koji je Zagreb
	 */
	private int dohvatiOdabraniMultipleks() {
		final int ZAGREB = 1;
		int odabraniMultipleks = 1;//oma.dohvatiOdabraniMultipleks();
		
		if (odabraniMultipleks >= 1)
			return odabraniMultipleks;
		else
			return ZAGREB;
	}
	
	/**Dohvaæa projekcije.
	 * 
	 * Ova metoda pokreæe dohvaæanje projekcije na dva naèina. Prvo se dohvaæaju iz baze indeksi svih projekcija.
	 * Potom se oblikuju u JSON i šalje se zahtjev web servisu. Servis vraæa samo one projekcije 
	 * kojih nema u lokalnoj bazi, kao i one koje treba ukloniti iz lokalne baze.
	 * Nakon toga se projekcije prikazuju na zaslon.
	 * Ovaj princip je odabran kako bi se korisnicima uštedio podatkovni promet, odnosno,
	 * kako se ne bi uvijek dohvaæali svi filmovi sa servisa
	 * 
	 * @param id multipleksa
	 * @return ažurirane projekcije
	 */
	private List<ProjekcijaInfo> dohvatiProjekcije(int multipleks)
	{	
		JsonProjekcije jf = new JsonProjekcije();
		
		List<ProjekcijaInfo> pinf = jf.dohvatiProjekcije(this, multipleks);		
		return pinf;
	}


	/** Dohvaæa projekcije i uèitava ih u ListView.
	 * @params datum projekcija
	 */
	private void ucitajProjekcijeUListView(String datum)
	{
		List<ProjekcijaInfo> danasnjeProjekcije = filtrirajProjekcijeZaDatum(datum);
		ArrayAdapter<ProjekcijaInfo> adapter = new StavkaProjekcije(this, R.layout.stavka_projekcije, danasnjeProjekcije);
		ListView lv = (ListView) findViewById(R.id.rezervacije_popis_projekcija);
		lv.setAdapter(adapter);
	}
	
	
	/**Filtiranje projekcija za odabrani datum.
	 * Iz popisa svih projekcija filtrira samo današnje projekcije.
	 * S ovim sprjeèavamo nova dohvaæanja projekcija kada se promijeni datum prikazivanja projekcija
	 * @param odabrani datum
	 * @return lista današnjih projekcija
	 */
	private List<ProjekcijaInfo> filtrirajProjekcijeZaDatum(String datum) {
		List<ProjekcijaInfo> danasnjeProjekcije = new ArrayList<ProjekcijaInfo>();
		
		//prolazimo kroz sve projekcije i provjeravamo dan, mjesec i godinu poèetka
		for(ProjekcijaInfo projekcija : projekcije)
		{
			if (datum.equals(projekcija.getVrijemePocetka().substring(0, 10)))
			{
				danasnjeProjekcije.add(projekcija);
			}
		}
		
		return danasnjeProjekcije;
	}
	/**
	 * Metoda koja vraæa dan u tjednu.
	 * @param datum
	 * @return string dan u tjednu
	 */
	 public String vratiDanUTjednu(Date datum)
	 {   
		 String danOriginal;
		 SimpleDateFormat formatDana = new SimpleDateFormat("E");

		 //dohvaæame dane u tjednu iz resursa
		 String[] daniUTjednu = getResources().getStringArray(R.array.dani_u_tjednu);
		 
		 danOriginal = formatDana.format(datum);
		 
		 if(danOriginal.compareTo("Mon") == 0 ) return  daniUTjednu[0];
		 else if(danOriginal.compareTo("Tue") == 0 ) return  daniUTjednu[1];
		 else if(danOriginal.compareTo("Wed") == 0 ) return  daniUTjednu[2];
		 else if(danOriginal.compareTo("Thu") == 0 ) return  daniUTjednu[3];
		 else if(danOriginal.compareTo("Fri") == 0 ) return  daniUTjednu[4];
		 else if(danOriginal.compareTo("Sat") == 0 ) return  daniUTjednu[5];
		 else if(danOriginal.compareTo("Sun") == 0 ) return  daniUTjednu[6];
		 else return danOriginal.toString();
		
	 }
}
