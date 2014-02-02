package hr.air.mkino;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import hr.air.mkino.baza.MultipleksAdapter;
import hr.air.mkino.baza.PrijavljeniKorisnikAdapter;

import hr.air.mkino.baza.ProjekcijeAdapter;
import hr.air.mkino.core.Prijava;
import hr.air.mkino.server.JsonDohvatiSjedala;
import hr.air.mkino.server.JsonRezervacija;
import hr.air.mkino.sucelja.ISlikaFilma;
import hr.air.mkino.tipovi.Korisnik;
import hr.air.mkino.tipovi.MultipleksInfo;
import hr.air.mkino.tipovi.ProjekcijaInfo;
import hr.air.mkino.tipovi.RezervacijaInfo;
import hr.air.mkino.uzorcidizajna.UcitajSlikuFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import android.view.Window;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

public class RezervacijaActivity extends Activity {
	
	CharSequence[] sjedalaPrikaz = {};
	boolean [] sjedalaBool = {};
	List<Integer> sjedalaZauzeta = new ArrayList<Integer>();
	List<Integer> listaOdabranih = new ArrayList<Integer>();
	final int BROJ_SJEDALA = 60;
	final int OGRANICENJE_SJEDALA = 30;
	final Context con = this;
	boolean moguceRezervirati = true;
	int idProjekcije;
	ProjekcijaInfo detaljiProjekcije;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rezervacije);
		
		TextView sjedalaTV = (TextView) findViewById(R.id.rezervacije_sjedala_txt);	
		Button btnMapaSjedala = (Button) findViewById(R.id.btn_rezervacije_mapa_sjedala);
		Button btnRezerviraj = (Button) findViewById(R.id.btn_rezervacija_rezerviraj);
		detaljiProjekcije = dohvatiPodatkeZaProjekciju();
		prikaziPodatkeZaProjekciju(detaljiProjekcije);
		Intent i = getIntent();	

		final int idProjekcijeUBazi = i.getIntExtra("idProjekcijeUBazi", 0);
		idProjekcije = idProjekcijeUBazi;
		JsonDohvatiSjedala dhSj = new JsonDohvatiSjedala();		
		sjedalaZauzeta = dhSj.dohvatiSjedala(idProjekcijeUBazi);
		
		moguceRezervirati = provjeriZauzetost();
	
		filtrirajSlobodnaSjedala();

		/* *
		 * Ukoliko nije moguæe rezervirati sjedala za odabranu projekciju iz pojedinih razloga, onda æemo korisniku
		 * zabraniti odabir sjedala, pregled mape i klik na rezervaciju!
		 *  */
		if(moguceRezervirati)
		{
			sjedalaTV.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {			
					prikaziDijalog();
				}
			});
			
			
			btnMapaSjedala.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					final Dialog dialogPrijava = new Dialog(con);
					dialogPrijava.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialogPrijava.setContentView(R.layout.dialog_mapa_sjedala);
					
					dialogPrijava.show();
					
				
				}
			});
			btnRezerviraj.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					rezerviraj();					
				}
			});
		}
		else
		{
			Toast.makeText(this, R.string.rezervacija_nije_moguce_rezervirati, Toast.LENGTH_LONG).show();
		}
	}
	
	/*
	 * Metoda koja prvo utvrðuje je li moguæe izvršiti rezervaciju,
	 * ukoliko nije moguæe izvršiti rezervaciju korisniku se prikazuje poruka. 
	 * Ako korisnik nije odabrao sjedala za projekciju rezervacija se obustavlja i prikazuje se poruka.
	 * Ukoliko su svi preuvjeti zadovoljeni nastavlja se korak rezervacije.
	 * */
	public void rezerviraj()
	{
		PrijavljeniKorisnikAdapter prijavljeniKorisnik = new PrijavljeniKorisnikAdapter(this);
		Korisnik korisnik = prijavljeniKorisnik.dohvatiPrijavljenogKorisnika();
		Context context = this;
    	Prijava prijava = new Prijava();
		
		if(!moguceRezervirati)
		{
			Toast.makeText(this, R.string.rezervacija_nije_moguce_rezervirati, Toast.LENGTH_LONG).show();
		}
		else if(listaOdabranih.size() == 0)
		{
			Toast.makeText(this, R.string.rezervacija_odaberite_sjedala, Toast.LENGTH_LONG).show();
		}
		else if( korisnik == null)
		{
			prijava.prikaziDijalog(context);
		}
		else
		{
			JsonRezervacija jsonRezervacije = new JsonRezervacija();
			RezervacijaInfo rezervacija = jsonRezervacije.rezerviraj(korisnik.getKorisnickoIme(), idProjekcije, listaOdabranih);
			if(rezervacija == null)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
		        builder.setMessage(R.string.rezervacija_pogreska_kod_registracije)
		               .setPositiveButton(R.string.rezervacija_shvacam, new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		                	   
		                      
		                   }
		               });
		        builder.show();
			}
			else
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
		        builder.setTitle(R.string.rezervacija_uspjesna)
		        		.setMessage("Kod rezervacije: " + rezervacija.getKodRezervacije())
		                .setPositiveButton(R.string.rezervacija_nastavi, new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		                	   RezervacijaActivity.this.finish();
		                	   Intent intent = new Intent(RezervacijaActivity.this, PocetnaActivity.class);
		                       startActivity(intent);
		                   }
		               });
		        builder.show();
		        
		      
		       
		       
			}
		}
	}
	
	public float IzracunajCijenu()
	{
		return listaOdabranih.size() * detaljiProjekcije.getCijena();
	}
	/**
	 * Metoda koja provjerava je li moguæe rezervirati sjedala za projekciju. Ako je projekcija rezervirana više od 30% nije moguæe rezervirati ulaznice
	 * @return true ako je moguæe, false ako je nemoguæe
	 */
	public boolean provjeriZauzetost()
	{
		float postotakZauzetosti = 0;
		if(sjedalaZauzeta.size() != 0 )  
			postotakZauzetosti = BROJ_SJEDALA / sjedalaZauzeta.size()*100;
		else 
			return true;
		if(postotakZauzetosti <= 100-OGRANICENJE_SJEDALA)
			return false;
		else
			return true;
	}

	
	/**
	 * Metoda koja služi za dohvaæanje podataka o projekciji
	 * @return projekcija
	 */
	private ProjekcijaInfo dohvatiPodatkeZaProjekciju() {
		Intent i = getIntent();
		int idProjekcijeUBazi = i.getIntExtra("idProjekcijeUBazi", 0);
		
		ProjekcijeAdapter pa = new ProjekcijeAdapter(this);
	
		return pa.dohvatiProjekciju(idProjekcijeUBazi, this);
	}
	
	/**
	 * Metoda koja služi za prikaz podataka o projekciji na osnovu proslijeðene projekcije
	 * @param projekcija
	 */
	private void prikaziPodatkeZaProjekciju(ProjekcijaInfo detaljiProjekcije) {
		TextView naslov = (TextView) findViewById(R.id.projekcija_naslov_filma);

		ImageView slika = (ImageView) findViewById(R.id.projekcija_slikaFilma);
		TextView dvorana = (TextView) findViewById(R.id.txt_rezervacije_dvorana);
		TextView vrijeme = (TextView) findViewById(R.id.txt_rezervacije_vrijeme);
		TextView multipleks = (TextView) findViewById(R.id.txt_rezervacije_grad);
		TextView cijena = (TextView) findViewById(R.id.rezervacija_cijena);
		naslov.setText(detaljiProjekcije.getNaziv());
		cijena.setText("0.00");
		dvorana.setText("Dvorana "+detaljiProjekcije.getDvorana());		
		String vrijePocetkString = detaljiProjekcije.getVrijemePocetka();
		vrijeme.setText(vrijePocetkString);
		
		//dohvaæanje multipleksa iz lokalne baze podatka
		MultipleksAdapter ma = new MultipleksAdapter(this);
		MultipleksInfo multipl = ma.dohvatiMultipleks(detaljiProjekcije.getMultipleks());
		if(multipl != null) multipleks.setText(multipl.getNaziv());		
		
		ISlikaFilma sf = UcitajSlikuFactory.ucitaj(this, detaljiProjekcije.getIdFilma(), true);
		slika.setImageBitmap(sf.dohvatiVelikuSliku());
		
	}

	/**
	 * Metoda koja služi za prikaz dijaloga sa popisom slobodnih sjedala
	 * Nakon odabira sjedala navedeni se spremaju u listu odabranih sjedala
	 */
	private void prikaziDijalog(){			
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.odabir_sjedala_txt_odaberi_sjedalo)   
             .setMultiChoiceItems(sjedalaPrikaz, sjedalaBool,
                      new DialogInterface.OnMultiChoiceClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which,
                       boolean isChecked) {
            	   String parsiran = (String) sjedalaPrikaz[which];
            	   String[] pars = parsiran.split(" ");
                   if (isChecked) {
                       // If the user checked the item, add it to the selected items                	
                	   listaOdabranih.add(Integer.parseInt(pars[1]));
                   } else 
                   {
                	   for (Iterator<Integer> iter = listaOdabranih.listIterator(); iter.hasNext(); ) {
                		    int a = iter.next();
                		    int b = Integer.parseInt(pars[1]);
                		    if ( b == a) {
                		        iter.remove();
                		    }
                		}
                                  	
                   }
                   azurirajOdabranaMjesta();
                   /*TODO prikazi cijenu na layoutu i invalidate napravi*/
                   
                   
               }
            }) 
            .setPositiveButton(R.string.odabir_sjedala_btn_prihvati, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                   // RezervacijaActivity.this.finish();
                }
            });
       
        builder.create().show();    

	}

	/**
	 * Metoda koja ažurira popis odabranih mjesta
	 */
	public void azurirajOdabranaMjesta()
	{
		TextView sjedalaTV = (TextView) findViewById(R.id.rezervacije_sjedala_txt);	
		TextView cijena = (TextView) findViewById(R.id.rezervacija_cijena);
		if(listaOdabranih.size() == 0)
		{
			sjedalaTV.setText(R.string.odabir_sjedala_txt_odaberi_sjedalo);
			cijena.setText("0.00");
			sjedalaTV.invalidate();
		}
		else
		{
			cijena.setText(Float.toString(IzracunajCijenu()));
			cijena.invalidate();
			String text = "";
			for (Integer sjedalo : listaOdabranih) {
				text += (""+ Integer.toString(sjedalo)+",  ");
			}
			text = text.substring(0,text.length()-3);
			sjedalaTV.setText(text);
			sjedalaTV.invalidate();
		}
		
	}

	/**
	 * Metoda koja ubacuje slobodna sjedala u listu slobodnih sjedala, te dodjeljuje niz odabranih vrijednosti u checkboxu
	 */
	public void filtrirajSlobodnaSjedala()
	{
		
		int indeksNiza = 0;
		if(sjedalaZauzeta != null)
		{
			int brojSlobodnihSj = BROJ_SJEDALA - sjedalaZauzeta.size();
			sjedalaBool = new boolean[brojSlobodnihSj];
			sjedalaPrikaz = new CharSequence[brojSlobodnihSj];
			
			for (int i = 1; i<=BROJ_SJEDALA; i++)
			{
				if(!sjedalaZauzeta.contains(i))
				{
					sjedalaPrikaz[indeksNiza] = "Sjedalo " + Integer.toString(i);
					sjedalaBool[indeksNiza] = false;
					indeksNiza += 1;
				}
				
			}
		}
		else
		{
			int brojSlobodnihSj = BROJ_SJEDALA;
			sjedalaBool = new boolean[brojSlobodnihSj];
			sjedalaPrikaz = new CharSequence[brojSlobodnihSj];
			
			//punimo nizove sa prikazom slobodnih sjedala i opcijom odabira checkboxa
			for (int i = 1; i<=BROJ_SJEDALA; i++)
			{				
				sjedalaPrikaz[indeksNiza] = "Sjedalo " +Integer.toString(i);
				sjedalaBool[indeksNiza] = false;
				indeksNiza += 1;
			}
		}
		

	
	}
}
