package hr.air.mkino.core;



import java.util.Calendar;
import java.util.Date;


public class Rezervacija{
	
	 /**
	  * Metoda koja slu�i za uve�avanje broja dana od navedenog datuma.
	  * 
	  * @param Datum kojeg �elimo uve�ati
	  * @param Broj dana za koliko �elimo uve�ati datum
	  * @return Datum uve�an za tra�eni broj dana
	  */
	 public static Date dodajDan(Date datum, int dana)
	 {
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(datum);
	        cal.add(Calendar.DATE, dana); 
	        return cal.getTime();
	 }
	 

		 
		 
		 
	 
	 
	 
}
