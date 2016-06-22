package epochconverter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Calculators {

//	private Calculators() {
//		//This is a static methods only class.
//	}

    /**
    *
    * Get the number of days from epoch
    *
    * @param pDate
    * @return
    * @throws ParseException
    */
   public static long getDaysFromEpoch(String pDate) throws ParseException {

       DateTimeFormatter tFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
       LocalDate tBusinessDate = LocalDate.parse(pDate, tFormatter);

       return tBusinessDate.toEpochDay();

   }

   public static void main(String[] pArgs) {
		try {
			System.out.println(getDaysFromEpoch("2024-12-31"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   }

}
