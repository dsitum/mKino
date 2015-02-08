package hr.air.mkino;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import hr.air.mkino.baza.PrijavljeniKorisnikAdapter;
import hr.air.mkino.server.JsonMojeRezervacije;
import hr.air.mkino.server.JsonObrisiRegistraciju;
import hr.air.mkino.tipovi.Korisnik;
import hr.air.mkino.tipovi.RezervacijaInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Klasa koja služi za prikaz korisničkih rezervacija.
 * Da bi se mogle pregledati vlastite rezervacije potrebno je uspješno se prijaviti u aplikaciju. 
 * Rezervacije je moguće obrisati uz pomoć long clicka.
 * @author bstivic
 *
 */
public class MojeRezervacijeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_moje_rezervacije);
		setTitle("Moje rezervacije");
		final Context context = this;
    
		PrijavljeniKorisnikAdapter prijavljeniKorisnik = new PrijavljeniKorisnikAdapter(this);
		final Korisnik korisnik = prijavljeniKorisnik.dohvatiPrijavljenogKorisnika();
		
		
		//1. Pokreni servis za dohvaćanje mojih rezervacija
		final JsonMojeRezervacije jsonRezervacije = new JsonMojeRezervacije();
		final List<RezervacijaInfo> rezervacije = jsonRezervacije.dohvati(korisnik.getKorisnickoIme(), this);
		
			
		ucitajUListView(rezervacije);		
		
		//dodajemo event listener na long click
		ListView popisRezervacija = (ListView) findViewById(R.id.moje_rezervacije_list_view);
		popisRezervacija.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0,  View kliknutaProjekcija, int pozicijaKliknuteProjekcije, long idKli) {
				final View view = kliknutaProjekcija;
				//provjera želi li korisnik obrisati rezervaciju
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
			
		        builder.setMessage(R.string.moje_rezervacije_obrisi).setPositiveButton(R.string.moje_rezervacije_da, new DialogInterface.OnClickListener() 
		        {            
	        		//ukoliko želi obrisati rezervaciju
	        	   public void onClick(DialogInterface dialog, int id) 
	        	   { 
						JsonObrisiRegistraciju jsonObrisi = new JsonObrisiRegistraciju();
						TextView tv = (TextView) view.findViewById(R.id.rezervacija_kod);
						
						//izdvajamo id projekcije
						String kod = tv.getText().toString();
						String[] separated = kod.split("-");
						
						//pozivamo metodu za brisanje rezervacije
						int rez = jsonObrisi.dohvati(korisnik.getKorisnickoIme(),separated[1],  context);
						if(rez == 0)
						{
							Toast.makeText(context, R.string.obrisi_rezervaciju_uspjesno, Toast.LENGTH_LONG).show();
						}
						else
						{
							Toast.makeText(context, R.string.obrisi_rezervaciju_neuspjesno, Toast.LENGTH_LONG).show();
						}
						//ponovno učitavamo ažurne rezervacije
						napraviRefresh();	
	               }
	            });
		        builder.show();    
		        //inaće se vraćamo
				return false;
			}
		});
	}
	
	/**
	 * Metoda koja služi za refresh aktivnosti
	 */
	public void napraviRefresh()
	{

		JsonMojeRezervacije jsonRezervacije = new JsonMojeRezervacije();
		PrijavljeniKorisnikAdapter prijavljeniKorisnik = new PrijavljeniKorisnikAdapter(this);
		final Korisnik korisnik = prijavljeniKorisnik.dohvatiPrijavljenogKorisnika();
		
		//učitavamo u list view
		List<RezervacijaInfo> rezervacije = jsonRezervacije.dohvati(korisnik.getKorisnickoIme(), this);
	    ucitajUListView(rezervacije);
	}
	/**
	 * Metoda koja služi za učitavanje rezervacija u listview.
	 * @param rezervacije
	 */
	public void ucitajUListView(List<RezervacijaInfo> rezervacije)
	{
		
			String[] iz = new String[] {"idRezervacije", "naziv", "dvorana", "vrijeme", "kod", "sjedala"};
			int[] u = new int[] {R.id.rezervacija_id_rezervacije, R.id.rezervacija_naziv_projekcije, R.id.rezervacija_dvorana, R.id.rezervacija_vrijeme_projekcije, R.id.rezervacija_kod, R.id.moje_rezervacije_sjedala};
			String vrijeme;
		
			Map<String, String> stavka;
			List<Map<String, String>> podaci = new ArrayList<Map<String, String>>();
			
			//prolazimo kroz sve rezervacije
			for (RezervacijaInfo rezervacija : rezervacije)
			{
				
				stavka = new HashMap<String, String>();
				stavka.put("idProjekcije", String.valueOf(rezervacija.getIdProjekcije()));
				stavka.put("naziv",  rezervacija.getNaziv());
				vrijeme = rezervacija.getVrijeme();
				stavka.put("vrijeme", vrijeme.substring(0, 16));
				stavka.put("kod", rezervacija.getKodRezervacije());
				stavka.put("dvorana", "Dvorana " + rezervacija.getDvorana());
				String sjedala = "";
				
				//prolazimo kroz sva sjedala rezervirana 
				for(Integer sjedalo: rezervacija.getSjedala())
				{
					sjedala += Integer.toString(sjedalo) + ",";
				}
				sjedala= sjedala.substring(0,sjedala.length()-1);
				stavka.put("sjedala",sjedala );
				podaci.add(stavka);				
				
			}
			
			ListView popisRezervacija = (ListView) findViewById(R.id.moje_rezervacije_list_view);
			ListAdapter adapter = new SimpleAdapter(this, podaci, R.layout.stavka_rezervacije, iz, u);
			
			popisRezervacija.invalidateViews();
			popisRezervacija.setAdapter(adapter);
	}
}
