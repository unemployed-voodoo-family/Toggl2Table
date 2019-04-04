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
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    DateRange range = new DateRange(date1, date2, formatter);

    @Before
    public void setUp() throws Exception {
        date1 = LocalDate.of(2019, 1, 1);
        date2 = LocalDate.of(2019, 1, 31);
        formatter = DateTimeFormatter.ISO_LOCAL_TIME;
        range = new DateRange(date1, date2, formatter);
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

        DateRange otherRange = new DateRange(date3, date4, formatter);
        assertFalse(range.fromValueinRange(otherRange));
    }

    @Test
    public void toValueInRange() {
    }

    @Test
    public void isEncapsulating() {
    }

    @Test
    public void ofString() {
    }

    @Test
    public void contains() {
        for(LocalDate d = date1; d.isBefore(date2.plusDays(1)) ; d = d.plusDays(1)) {
            System.out.println(d.toString());
            assertTrue(range.contains(d));
        }

        assertFalse(range.contains(date2.plusDays(1)));
        assertFalse(range.contains(date1.minusDays(1)));
    }

}