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
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MojeRezervacijeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_moje_rezervacije);
				final Context context = this;
    
		PrijavljeniKorisnikAdapter prijavljeniKorisnik = new PrijavljeniKorisnikAdapter(this);
		Korisnik korisnik = prijavljeniKorisnik.dohvatiPrijavljenogKorisnika();
		
		if( korisnik == null)
		{
			/*TODO string izmjenit*/
			Toast.makeText(this, R.string.registracija_email, Toast.LENGTH_SHORT).show();
			this.finish();			
		}
		
		
		//1. Pokreni servis za dohvaæanje mojih rezervacija
		JsonMojeRezervacije jsonRezervacije = new JsonMojeRezervacije();
		List<RezervacijaInfo> rezervacije = jsonRezervacije.dohvati(korisnik.getKorisnickoIme(), this);
			
		ucitajUListView(rezervacije);
		
		//3. dodaj longClick za brisanje rezervacije!
		ListView popisRezervacija = (ListView) findViewById(R.id.moje_rezervacije_list_view);
		popisRezervacija.setOnItemLongClickListener(new OnItemLongClickListener() {
			

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View kliknutaProjekcija, int pozicijaKliknuteProjekcije, long idKli) {
				JsonObrisiRegistraciju jsonObrisi = new JsonObrisiRegistraciju();
				TextView tv = (TextView) kliknutaProjekcija.findViewById(R.id.rezervacija_kod);
				String kod = tv.getText().toString();
				List<RezervacijaInfo> rezervacije = jsonObrisi.dohvati(kod, context);
				ucitajUListView(rezervacije);
				return true;
			}
		});
		
	}
	
	
	
	
	public void ucitajUListView(List<RezervacijaInfo> rezervacije)
	{
		
			String[] iz = new String[] {"idRezervacije", "naziv", "dvorana", "vrijeme", "kod"};
			int[] u = new int[] {R.id.rezervacija_id_rezervacije, R.id.rezervacija_naziv_projekcije, R.id.rezervacija_dvorana, R.id.rezervacija_vrijeme_projekcije, R.id.rezervacija_kod};
			String vrijeme;
		
			Map<String, String> stavka;
			List<Map<String, String>> podaci = new ArrayList<Map<String, String>>();
			
		
			for (RezervacijaInfo rezervacija : rezervacije)
			{
				
				stavka = new HashMap<String, String>();
				stavka.put("idProjekcije", String.valueOf(rezervacija.getIdProjekcije()));
				stavka.put("naziv",  rezervacija.getNaziv());
				vrijeme = rezervacija.getVrijeme();
				stavka.put("vrijeme", vrijeme.substring(0, 16));
				stavka.put("dvorana", "Dvorana " + rezervacija.getDvorana());
				
				
					podaci.add(stavka);
				
				
			}
			
			ListView popisRezervacija = (ListView) findViewById(R.id.moje_rezervacije_list_view);
			ListAdapter adapter = new SimpleAdapter(this, podaci, R.layout.stavka_rezervacije, iz, u);
			popisRezervacija.invalidateViews();
			popisRezervacija.setAdapter(adapter);
	}
}
