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

import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ProjekcijeActivity extends Activity {
	private Spinner odabirGrada;
	private Spinner odabirDatuma;
	private ListView popisProjekcija;
	private List<ProjekcijaInfo> projekcije;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_projekcije);
		
		// inicijaliziramo klasne varijable
		odabirGrada = (Spinner) findViewById(R.id.rezervacije_spinner_grad);
		odabirDatuma = (Spinner) findViewById(R.id.rezervacije_spiner_datum);
		popisProjekcija= (ListView) findViewById(R.id.rezervacije_popis_projekcija);
	
		int odabraniMultipleks = dohvatiOdabraniMultipleks();

		// postavljamo spinner na trenutno odabrani multipleks
		odabirGrada.setSelection(odabraniMultipleks-1);
		
		// dohva�amo aktualne filmove s web servisa, i pritom punimo/modificiramo lokalnu bazu filmova iz koje �e se kasnije vu�i podaci
		JsonFilmovi jf = new JsonFilmovi();
		jf.dohvatiFilmove(this);
		
		//postavljamo spinner na dana�nji datum i popunjavamo ga sa datumima u idu�a dva tjedna
		ArrayList<String> dani = new ArrayList<String>();
		
		    Date sadasnjiDatum = new Date();
		    SimpleDateFormat formatDatuma = new SimpleDateFormat("dd.MM.yyyy");

		    //String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		    //int danasnjiDan = Calendar.getInstance().get(Calendar.DATE);
		    for (int i = 0; i < 14; i++)
		    {			
		    	dani.add(formatDatuma.format(sadasnjiDatum)+" " +vratiDanUTjednu(sadasnjiDatum));
		    	sadasnjiDatum = Rezervacija.dodajDan(sadasnjiDatum, 1);
		    }
		    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		            android.R.layout.simple_spinner_item, dani);

		    
		    odabirDatuma.setAdapter(adapter);
		    odabirDatuma.setSelection(0);
		    
		   //racunamo koji je datum u spinneru s datumom
		    sadasnjiDatum = new Date();
		    odabirDatuma.setOnItemSelectedListener(new OnItemSelectedListener() {
		    	@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long indeksDana) {										
		    		Date sadasnjiDatum = new Date();
		    		sadasnjiDatum = Rezervacija.dodajDan(sadasnjiDatum,(int)indeksDana);		    		
		   		    SimpleDateFormat formatDatuma2 = new SimpleDateFormat("yyyy-MM-dd");
		    		ucitajProjekcijeUListView(formatDatuma2.format(sadasnjiDatum));
		    		
				}
				
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {}
		    
		    
		    });
		    
		    odabirGrada.setOnItemSelectedListener(new OnItemSelectedListener() {
		    	@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long idOdabranogMultipleksa) {										
					// pohranjujemo vrijednost spinnera u bazu podataka
		    		//multipleksAdapter.pohraniOdabraniMultipleks((int)idOdabranogMultipleksa+1);
		    		//ponovno zahtjevamo projekcije za odabrani grad
		    		projekcije = dohvatiProjekcije((int)idOdabranogMultipleksa+1);		
		    		Date sadasnjiDatum = new Date();
		    		sadasnjiDatum = Rezervacija.dodajDan(sadasnjiDatum,(int)odabirDatuma.getSelectedItemPosition());
		    		 SimpleDateFormat formatDatuma2 = new SimpleDateFormat("yyyy-MM-dd");
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
				Intent i = new Intent(ProjekcijeActivity.this, DetaljiProjekcijeActivity.class);
				i.putExtra("idProjekcijeUBazi", idProjekcijeUBazi);
				startActivity(i);
			}
		});
		
		// dohva�amo filmove i pohranjujemo ih u ListView "popisFilmova". Tu se automatski prikazuju na zaslon
		projekcije = dohvatiProjekcije(odabraniMultipleks);
		SimpleDateFormat formatDatuma2 = new SimpleDateFormat("yyyy-MM-dd");
		ucitajProjekcijeUListView(formatDatuma2.format(sadasnjiDatum));
	
	}
	
	 
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pocetna, menu);
		return true;
	}
	
	/**
	 * Postavlja spinner za izbor multipleksa na vrijednost trenutnog multipleksa u bazi. Ukoliko on ne postoji ondje, odabire se prvi multipleks (indeks 0) koji je Zagreb
	 */
	private int dohvatiOdabraniMultipleks() {
		final int ZAGREB = 1;
		int odabraniMultipleks = 1;//oma.dohvatiOdabraniMultipleks();
		
		if (odabraniMultipleks >= 1)
			return odabraniMultipleks;
		else
			return ZAGREB;
	}
	
	/**
	 * Ova metoda pokre�e dohva�anje projekcije na dva na�ina. Prvo se dohva�aju iz baze indeksi svih projekcija.
	 * Potom se oblikuju u JSON i �alje se zahtjev web servisu. Servis vra�a samo one projekcije 
	 * kojih nema u lokalnoj bazi, kao i one koje treba ukloniti iz lokalne baze.
	 * Nakon toga se projekcije prikazuju na zaslon.
	 * Ovaj princip je odabran kako bi se korisnicima u�tedio podatkovni promet, odnosno,
	 * kako se ne bi uvijek dohva�ali svi filmovi sa servisa
	 * @return a�urirane projekcije
	 */
	private List<ProjekcijaInfo> dohvatiProjekcije(int multipleks)
	{	
		JsonProjekcije jf = new JsonProjekcije();
		
		List<ProjekcijaInfo> pinf = jf.dohvatiProjekcije(this, multipleks);		
		return pinf;
	}


	/**
	 * Dohva�a projekcije i u�itava ih u ListView
	 */
	private void ucitajProjekcijeUListView(String datum)
	{
		List<ProjekcijaInfo> danasnjeProjekcije = filtrirajProjekcijeZaDatum(datum);
		ArrayAdapter<ProjekcijaInfo> adapter = new StavkaProjekcije(this, R.layout.stavka_projekcije, danasnjeProjekcije);
		ListView lv = (ListView) findViewById(R.id.rezervacije_popis_projekcija);
		lv.setAdapter(adapter);
	}
	
	
	/**
	 * Iz popisa svih projekcija filtrira samo dana�nje projekcije.
	 * S ovim sprje�avamo nova dohva�anja projekcija kada se promijeni datum prikazivanja projekcija
	 * @return lista dana�njih projekcija
	 */
	private List<ProjekcijaInfo> filtrirajProjekcijeZaDatum(String datum) {
		List<ProjekcijaInfo> danasnjeProjekcije = new ArrayList<ProjekcijaInfo>();
		
		for(ProjekcijaInfo projekcija : projekcije)
		{
			if (datum.equals(projekcija.getVrijemePocetka().substring(0, 10)))
			{
				danasnjeProjekcije.add(projekcija);
			}
		}
		
		return danasnjeProjekcije;
	}
	
	 public String vratiDanUTjednu(Date datum)
	 {   
		 String danOriginal;
		 SimpleDateFormat formatDana = new SimpleDateFormat("E");

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
