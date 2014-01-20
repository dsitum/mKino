package hr.air.mkino.core;



import java.util.Calendar;
import java.util.Date;


public class Rezervacija{
	
	 /**
	  * Metoda koja služi za uveæavanje broja dana od navedenog datuma.
	  * 
	  * @param Datum kojeg želimo uveæati
	  * @param Broj dana za koliko želimo uveæati datum
	  * @return Datum uveæan za traženi broj dana
	  */
	 public static Date dodajDan(Date datum, int dana)
	 {
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(datum);
	        cal.add(Calendar.DATE, dana); 
	        return cal.getTime();
	 }
	 

		 
		 
		 
	 
	 
	 
}
