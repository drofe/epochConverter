package epochconverter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

public class Calculators {

    private Calculators() {
        // This is a static methods only class.
    }

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

    public static LocalDateTime convertToTimeZone(LocalDateTime pLDT,
    		ZoneId pFromZone, ZoneId pToZone) {
    	if (null != pLDT && null != pFromZone && null != pToZone) {

    		ZonedDateTime tZonedTime = ZonedDateTime.of(pLDT, pFromZone);
    		ZonedDateTime tConvertedTime = tZonedTime.withZoneSameInstant(pToZone);
			return tConvertedTime.toLocalDateTime();
    	} else {
    		return null;
    	}

    }

    public static LocalDateTime getDateFromEpochDay(long pEpochDay) {
    	return LocalDateTime.of(LocalDate.ofEpochDay(pEpochDay), LocalTime.MIDNIGHT);
    }

    public static LocalDateTime getLocalDateTimeFromStamp(String pStamp, String pSuffix) {
    	if (null == pSuffix) {
    		pSuffix = "";
    	}
    	long tDivisor = 0;
    	switch (pSuffix) {
    		case "ns":
    			tDivisor = 1_000_000_000;
    			break;
    		case "ms":
    			tDivisor = 1_000;
    			break;
    		case "mics":
    			tDivisor = 1_000_000;
    			break;
    		case "s":
    		default:
    			tDivisor = 1;
    	}
    	long tStamp = Long.parseLong(pStamp);
    	long tSec = tStamp / tDivisor;
    	int tNano = (int) ((tStamp % (tDivisor)) * 1_000_000_000/tDivisor);
    	return LocalDateTime.ofEpochSecond(tSec, tNano, ZoneOffset.UTC);
   }

}
