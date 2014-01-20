package hr.air.mkino;

import hr.air.mkino.core.Rezervacija;
import hr.air.mkino.server.JsonProjekcije;
import hr.air.mkino.tipovi.ProjekcijaInfo;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ProjekcijeActivity extends Activity {
	private Spinner spinnerGrad;
	private Spinner spinnerDatum;
	private ListView popisProjekcija;
	private List<ProjekcijaInfo> projekcije;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_projekcije);
		
		// inicijaliziramo klasne varijable
		spinnerGrad = (Spinner) findViewById(R.id.rezervacije_spinner_grad);
		spinnerDatum = (Spinner) findViewById(R.id.rezervacije_spiner_datum);
		popisProjekcija= (ListView) findViewById(R.id.rezervacije_popis_projekcija);
	
		int odabraniMultipleks = dohvatiOdabraniMultipleks();

		// postavljamo spinner na trenutno odabrani multipleks
		spinnerGrad.setSelection(odabraniMultipleks-1);
		
		//postavljamo spinner na današnji datum i popunjavamo ga sa datumima u iduæa dva tjedna
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

		    
		    spinnerDatum.setAdapter(adapter);
		    spinnerDatum.setSelection(0);
		    
		   //racunamo koji je datum u spinneruDatum
		    sadasnjiDatum = new Date();
		    spinnerDatum.setOnItemSelectedListener(new OnItemSelectedListener() {
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
		    
		    spinnerGrad.setOnItemSelectedListener(new OnItemSelectedListener() {
		    	@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long idOdabranogMultipleksa) {										
					// pohranjujemo vrijednost spinnera u bazu podataka
		    		//multipleksAdapter.pohraniOdabraniMultipleks((int)idOdabranogMultipleksa+1);
		    		//ponovno zahtjevamo projekcije za odabrani grad
		    		projekcije = dohvatiProjekcije((int)idOdabranogMultipleksa+1);		
		    		Date sadasnjiDatum = new Date();
		    		sadasnjiDatum = Rezervacija.dodajDan(sadasnjiDatum,(int)spinnerDatum.getSelectedItemPosition());
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
		
		// dohvaæamo filmove i pohranjujemo ih u ListView "popisFilmova". Tu se automatski prikazuju na zaslon
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
	 * Ova metoda pokreæe dohvaæanje projekcije na dva naèina. Prvo se dohvaæaju iz baze indeksi svih projekcija.
	 * Potom se oblikuju u JSON i šalje se zahtjev web servisu. Servis vraæa samo one projekcije 
	 * kojih nema u lokalnoj bazi, kao i one koje treba ukloniti iz lokalne baze.
	 * Nakon toga se projekcije prikazuju na zaslon.
	 * Ovaj princip je odabran kako bi se korisnicima uštedio podatkovni promet, odnosno,
	 * kako se ne bi uvijek dohvaæali svi filmovi sa servisa
	 * @return ažurirane projekcije
	 */
	private List<ProjekcijaInfo> dohvatiProjekcije(int multipleks)
	{	
		JsonProjekcije jf = new JsonProjekcije();
		
		List<ProjekcijaInfo> pinf = jf.dohvatiProjekcije(this, multipleks);		
		return pinf;
	}
	
	/**
	 * Dohvaæa projekcije i uèitava ih u ListView
	 */
	private void ucitajProjekcijeUListView(String danProjekcije)
	{
		String[] iz = new String[] {"idProjekcije", "naziv", "vrijeme", "dvorana"};
		int[] u = new int[] {R.id.id_projekcije_u_bazi, R.id.naziv_projekcije_mali, R.id.vrijeme_projekcije_mali, R.id.dvorana_projekcije_mali};
		String vrijeme;

		Map<String, String> stavka;
		List<Map<String, String>> podaci = new ArrayList<Map<String, String>>();
		

		for (ProjekcijaInfo projekcija : projekcije)
		{
			
			stavka = new HashMap<String, String>();
			stavka.put("idProjekcije", String.valueOf(projekcija.getidProjekcije()));
			stavka.put("naziv",  projekcija.getNaziv());
			vrijeme= projekcija.getVrijemePocetka();			
			stavka.put("vrijeme", vrijeme.substring(11, 16));
			stavka.put("dvorana", "Dvorana " + projekcija.getDvorana());
			
			/*TODO parsirat datum i provjerit jel najviše pola sata do projekcije, inace se ne moze prikazati*/			
			if(0==vrijeme.substring(0, 10).compareTo(danProjekcije.substring(0, 10)))
			{
				podaci.add(stavka);
			}
			
		}
		ListAdapter adapter = new SimpleAdapter(this, podaci, R.layout.stavka_projekcije, iz, u);
		popisProjekcija.invalidateViews();
		popisProjekcija.setAdapter(adapter);
		
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
