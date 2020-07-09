package UnemployedVoodooFamily.Data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.*;

public class DateRangeTest {


    LocalDate date1 = LocalDate.of(2019, 1, 1);
    LocalDate date2 = LocalDate.of(2019, 1, 31);

    DateRange range = new DateRange(date1, date2);

    @Before
    public void setUp() throws Exception {
        date1 = LocalDate.of(2019, 1, 1);
        date2 = LocalDate.of(2019, 1, 31);
        range = new DateRange(date1, date2);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getFrom() {
        assertEquals(date1, range.getFrom());
    }

    @Test
    public void getTo() {
        assertEquals(date2, range.getTo());
    }

    @Test
    public void setFrom() {
        LocalDate date3 = LocalDate.of(2019, 1, 15);
        range.setFrom(date3);
        assertEquals(date3, range.getFrom());

        LocalDate date4 = LocalDate.of(2019, 3, 1);
        range.setFrom(date4);
        assertEquals(date4, range.getFrom());
    }

    @Test
    public void setTo() {
    }

    @Test
    public void fromValueinRange() {
        LocalDate date4 = LocalDate.of(2019, 3, 15);
        LocalDate date3 = LocalDate.of(2019, 1, 15);

        DateRange otherRange = new DateRange(date3, date4);
        assertFalse(range.fromValueinRange(otherRange));
    }

    @Test
    public void toValueInRange() {
    }

    @Test
    public void isEncapsulating() {
        LocalDate d1 = LocalDate.of(2020, 6, 1);
        LocalDate d2 = LocalDate.of(2020, 6, 3);
        LocalDate d3 = LocalDate.of(2020, 6, 5);
        LocalDate d4 = LocalDate.of(2020, 6, 10);

        assertFalse(DateRange.of(d1, d2).isEncapsulating(DateRange.of(d3, d4)));
        assertFalse(DateRange.of(d1, d2).isEncapsulating(DateRange.of(d2, d3)));
        assertFalse(DateRange.of(d1, d3).isEncapsulating(DateRange.of(d2, d4)));
        assertFalse(DateRange.of(d3, d4).isEncapsulating(DateRange.of(d1, d2)));
        assertFalse(DateRange.of(d3, d4).isEncapsulating(DateRange.of(d1, d3)));
        assertFalse(DateRange.of(d2, d4).isEncapsulating(DateRange.of(d1, d3)));
        assertTrue(DateRange.of(d3, d4).isEncapsulating(DateRange.of(d3, d4)));
        assertTrue(DateRange.of(d1, d4).isEncapsulating(DateRange.of(d1, d3)));
        assertTrue(DateRange.of(d1, d4).isEncapsulating(DateRange.of(d2, d3)));
        assertTrue(DateRange.of(d1, d4).isEncapsulating(DateRange.of(d2, d4)));
    }

    @Test
    public void ofString() {
    }

    @Test
    public void contains() {
        for(LocalDate d = date1; d.isBefore(date2.plusDays(1)) ; d = d.plusDays(1)) {
            assertTrue(range.contains(d));
        }
        assertFalse(range.contains(date2.plusDays(1)));
        assertFalse(range.contains(date1.minusDays(1)));
    }

}