package epochConverter;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.Before;
import org.junit.Test;

import epochconverter.Calculators;

public class CalculatorsTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetLocalDateTimeFromStamp() {
		//1466641922 is 2016-06-23T00:32:02 in UTC seconds
		LocalDateTime tLDT = Calculators.getLocalDateTimeFromStamp("1466641922", null);
		assertEquals(6,tLDT.getMonth().getValue());
		assertEquals(2016,tLDT.getYear());
		assertEquals(23,tLDT.getDayOfMonth());
		assertEquals(00,tLDT.getHour());
		assertEquals(32,tLDT.getMinute());
		assertEquals(02,tLDT.getSecond());

		//1466641922020 is 2016-06-23T00:32:02.020 in UTC milli seconds
		tLDT = Calculators.getLocalDateTimeFromStamp("1466641922020", "ms");
		assertEquals(6,tLDT.getMonth().getValue());
		assertEquals(2016,tLDT.getYear());
		assertEquals(23,tLDT.getDayOfMonth());
		assertEquals(00,tLDT.getHour());
		assertEquals(32,tLDT.getMinute());
		assertEquals(02,tLDT.getSecond());
		assertEquals(20 * 1_000_000,tLDT.toLocalTime().getNano());

		// 1466641922000100 is 2016-06-23T00:32:02.000100 in UTC micro seconds
		tLDT = Calculators.getLocalDateTimeFromStamp("1466641922000100", "mics");
		assertEquals(6, tLDT.getMonth().getValue());
		assertEquals(2016, tLDT.getYear());
		assertEquals(23, tLDT.getDayOfMonth());
		assertEquals(00, tLDT.getHour());
		assertEquals(32, tLDT.getMinute());
		assertEquals(02, tLDT.getSecond());
		assertEquals(100 * 1_000,tLDT.toLocalTime().getNano());

		// 1466641922000000300 is 2016-06-23T00:32:02.000000300 in UTC nano seconds
		tLDT = Calculators.getLocalDateTimeFromStamp("1466641922000000300", "ns");
		assertEquals(6, tLDT.getMonth().getValue());
		assertEquals(2016, tLDT.getYear());
		assertEquals(23, tLDT.getDayOfMonth());
		assertEquals(00, tLDT.getHour());
		assertEquals(32, tLDT.getMinute());
		assertEquals(02, tLDT.getSecond());
		assertEquals(300, tLDT.toLocalTime().getNano());
	}

	@Test
	public void testTimeZoneConverter() {
		LocalDateTime tInputLDT = LocalDateTime.of(2016, 6, 24, 11, 23, 15);
		LocalDateTime tLDT = Calculators.convertToTimeZone(tInputLDT, ZoneId.of("Z"), ZoneId.of("Z"));
		assertEquals(2016, tLDT.getYear());
		assertEquals(6, tLDT.getMonthValue());
		assertEquals(24, tLDT.getDayOfMonth());
		assertEquals(11, tLDT.getHour());
		assertEquals(23, tLDT.getMinute());
		assertEquals(15, tLDT.getSecond());

		tLDT = Calculators.convertToTimeZone(tInputLDT, ZoneId.of("Australia/Sydney"), ZoneId.of("Z"));
		assertEquals(2016, tLDT.getYear());
		assertEquals(6, tLDT.getMonthValue());
		assertEquals(24, tLDT.getDayOfMonth());
		assertEquals(1, tLDT.getHour());
		assertEquals(23, tLDT.getMinute());
		assertEquals(15, tLDT.getSecond());
	}

}
