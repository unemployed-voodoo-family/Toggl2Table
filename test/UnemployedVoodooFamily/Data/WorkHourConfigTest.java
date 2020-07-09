package UnemployedVoodooFamily.Data;

import org.junit.Test;

import java.time.LocalDate;
import java.util.ListIterator;

import static org.junit.Assert.*;

public class WorkHourConfigTest {
    /**
     * Test adding the first work-hour entry
     */
    @Test
    public void testFirst() {
        // Must clear and make sure it is empty before each test - WorkHours is a singleton!
        WorkHourConfig config = WorkHourConfig.getInstance();
        config.clear();
        assertTrue(config.isEmpty());

        LocalDate d = LocalDate.of(2020, 06, 22);
        config.add(new WorkHours(d, d, 7.0));
        assertFalse(config.isEmpty());
        WorkHours wh = config.getFor(d);
        assertNotNull(wh);
        assertEquals(7.0, wh.getHours(), 0.01);
    }

    /**
     * Test adding a second entry
     */
    @Test
    public void testAddSecond() {
        // Must clear and make sure it is empty before each test - WorkHours is a singleton!
        WorkHourConfig config = WorkHourConfig.getInstance();
        config.clear();
        assertTrue(config.isEmpty());

        LocalDate d1 = LocalDate.of(2020, 6, 22);
        LocalDate d2 = LocalDate.of(2020, 6, 23);
        LocalDate d3 = LocalDate.of(2020, 6, 24);
        LocalDate d5 = LocalDate.of(2020, 6, 25);
        LocalDate d4 = LocalDate.of(2020, 6, 26);

        config.add(new WorkHours(d1, d3, 7.0));
        config.add(new WorkHours(d4, d4, 8.0));

        // Two periods should be added
        assertFalse(config.isEmpty());
        assertEquals(7.0, config.getFor(d1).getHours(), 0.01);
        assertEquals(7.0, config.getFor(d3).getHours(), 0.01);
        assertEquals(7.0, config.getFor(d2).getHours(), 0.01);
        assertEquals(8.0, config.getFor(d4).getHours(), 0.01);
        // Empty in the middle
        assertNull(config.getFor(d5));
    }

    /**
     * Test adding a second entry
     */
    @Test
    public void testAddThird() {
        // Must clear and make sure it is empty before each test - WorkHours is a singleton!
        WorkHourConfig config = WorkHourConfig.getInstance();
        config.clear();
        assertTrue(config.isEmpty());

        LocalDate d1 = LocalDate.of(2020, 6, 1);
        LocalDate d2 = LocalDate.of(2020, 7, 10);
        LocalDate d3 = LocalDate.of(2020, 8, 20);
        LocalDate d4 = LocalDate.of(2021, 2, 25);
        LocalDate d5 = LocalDate.of(2022, 1, 1);
        LocalDate d6 = LocalDate.of(2030, 12, 31);

        config.add(new WorkHours(d1, d2, 6.0));
        config.add(new WorkHours(d3, d4, 7.0));
        config.add(new WorkHours(d5, d6, 8.0));

        // Two periods should be added
        assertFalse(config.isEmpty());
        assertEquals(6.0, config.getFor(d1).getHours(), 0.01);
        assertEquals(7.0, config.getFor(d3).getHours(), 0.01);
        assertEquals(8.0, config.getFor(d6).getHours(), 0.01);
        assertEquals(8.0, config.getFor(LocalDate.of(2025, 1, 10)).getHours(), 0.01);
        // Empty in the middle
        assertNull(config.getFor(LocalDate.of(2019, 12, 31)));
    }

    /**
     * Test sorting
     */
    @Test
    public void testSort() {
        // Must clear and make sure it is empty before each test - WorkHours is a singleton!
        WorkHourConfig config = WorkHourConfig.getInstance();
        config.clear();
        assertTrue(config.isEmpty());

        LocalDate d1 = LocalDate.of(2020, 6, 1);
        LocalDate d2 = LocalDate.of(2020, 7, 10);
        LocalDate d3 = LocalDate.of(2020, 8, 20);
        LocalDate d4 = LocalDate.of(2021, 2, 25);
        LocalDate d5 = LocalDate.of(2022, 1, 1);
        LocalDate d6 = LocalDate.of(2030, 12, 10);
        LocalDate d7 = LocalDate.of(2030, 12, 11);
        LocalDate d8 = LocalDate.of(2030, 12, 31);

        config.add(new WorkHours(d7, d8, 9.0));
        config.add(new WorkHours(d3, d4, 7.0));
        config.add(new WorkHours(d1, d2, 6.0));
        config.add(new WorkHours(d5, d6, 8.0));

        // Test sorting
        config.sort();

        ListIterator<WorkHours> it = config.listIterator();
        assertTrue(it.hasNext());
        WorkHours wh1 = it.next();
        assertTrue(it.hasNext());
        WorkHours wh2 = it.next();
        assertTrue(it.hasNext());
        WorkHours wh3 = it.next();
        assertTrue(it.hasNext());
        WorkHours wh4 = it.next();

        // Two periods should be added
        assertEquals(6.0, wh1.getHours(), 0.01);
        assertEquals(7.0, wh2.getHours(), 0.01);
        assertEquals(8.0, wh3.getHours(), 0.01);
        assertEquals(9.0, wh4.getHours(), 0.01);
    }

    /**
     * Test case: add a work-hour period which overlaps with he beginning of an existing period.
     * The first period should be truncated - the second one "erases the first days of it"
     */
    @Test
    public void testOverlapBeginning() {
        // Must clear and make sure it is empty before each test - WorkHours is a singleton!
        WorkHourConfig config = WorkHourConfig.getInstance();
        config.clear();
        assertTrue(config.isEmpty());

        LocalDate d1 = LocalDate.of(2020, 6, 1);
        LocalDate d2 = LocalDate.of(2020, 6, 10);

        LocalDate d3 = LocalDate.of(2020, 5, 20);
        LocalDate d4 = LocalDate.of(2020, 6, 3);

        LocalDate d5 = LocalDate.of(2020, 6, 4);

        config.add(new WorkHours(d1, d2, 6.0));
        config.add(new WorkHours(d3, d4, 7.0));

        config.sort();

        ListIterator<WorkHours> it = config.listIterator();
        assertTrue(it.hasNext());
        WorkHours wh1 = it.next();
        assertTrue(it.hasNext());
        WorkHours wh2 = it.next();
        assertFalse(it.hasNext());

        // Two periods should be added
        assertEquals(7.0, wh1.getHours(), 0.01);
        assertEquals(d3, wh1.getFrom());
        assertEquals(d4, wh1.getTo());
        assertEquals(6.0, wh2.getHours(), 0.01);
        assertEquals(d5, wh2.getFrom());
        assertEquals(d2, wh2.getTo());
    }

    /**
     * Test case: add a work-hour period which overlaps with the end of an existing period.
     * The first period should be truncated - the second one "erases the last days of it"
     */
    @Test
    public void testOverlapEnd() {
        // Must clear and make sure it is empty before each test - WorkHours is a singleton!
        WorkHourConfig config = WorkHourConfig.getInstance();
        config.clear();
        assertTrue(config.isEmpty());

        LocalDate d1 = LocalDate.of(2020, 6, 1);
        LocalDate d2 = LocalDate.of(2020, 6, 10);

        LocalDate d3 = LocalDate.of(2020, 6, 5);
        LocalDate d4 = LocalDate.of(2020, 6, 30);

        LocalDate d5 = LocalDate.of(2020, 6, 4);

        config.add(new WorkHours(d1, d2, 6.0));
        config.add(new WorkHours(d3, d4, 7.0));

        config.sort();

        ListIterator<WorkHours> it = config.listIterator();
        assertTrue(it.hasNext());
        WorkHours wh1 = it.next();
        assertTrue(it.hasNext());
        WorkHours wh2 = it.next();
        assertFalse(it.hasNext());

        // Two periods should be added
        assertEquals(6.0, wh1.getHours(), 0.01);
        assertEquals(d1, wh1.getFrom());
        assertEquals(d5, wh1.getTo());
        assertEquals(7.0, wh2.getHours(), 0.01);
        assertEquals(d3, wh2.getFrom());
        assertEquals(d4, wh2.getTo());
    }

    /**
     * Test case: add a work-hour period which fits inside an existing period.
     * The first period should be split into two
     */
    @Test
    public void testFitInMiddle() {
        // Must clear and make sure it is empty before each test - WorkHours is a singleton!
        WorkHourConfig config = WorkHourConfig.getInstance();
        config.clear();
        assertTrue(config.isEmpty());

        LocalDate june_1 = LocalDate.of(2020, 6, 1);
        LocalDate june_20 = LocalDate.of(2020, 6, 20);

        LocalDate june_5 = LocalDate.of(2020, 6, 5);
        LocalDate june_10 = LocalDate.of(2020, 6, 10);

        LocalDate june_4 = LocalDate.of(2020, 6, 4);
        LocalDate june_11 = LocalDate.of(2020, 6, 11);

        config.add(new WorkHours(june_1, june_20, 6.0));
        config.add(new WorkHours(june_5, june_10, 7.0));

        config.sort();

        ListIterator<WorkHours> it = config.listIterator();
        assertTrue(it.hasNext());
        WorkHours wh1 = it.next();
        assertTrue(it.hasNext());
        WorkHours wh2 = it.next();
        assertTrue(it.hasNext());
        WorkHours wh3 = it.next();
        assertFalse(it.hasNext());

        // Two periods should be added
        assertEquals(6.0, wh1.getHours(), 0.01);
        assertEquals(june_1, wh1.getFrom());
        assertEquals(june_4, wh1.getTo());
        assertEquals(7.0, wh2.getHours(), 0.01);
        assertEquals(june_5, wh2.getFrom());
        assertEquals(june_10, wh2.getTo());
        assertEquals(6.0, wh3.getHours(), 0.01);
        assertEquals(june_11, wh3.getFrom());
        assertEquals(june_20, wh3.getTo());
    }

    /**
     * Test case: add a work-hour period which spans over an existing period.
     * The first period should disappear, only the second should remain
     */
    @Test
    public void testIncludes() {
        // Must clear and make sure it is empty before each test - WorkHours is a singleton!
        WorkHourConfig config = WorkHourConfig.getInstance();
        config.clear();
        assertTrue(config.isEmpty());

        LocalDate june_1 = LocalDate.of(2020, 6, 1);
        LocalDate june_5 = LocalDate.of(2020, 6, 5);
        LocalDate june_10 = LocalDate.of(2020, 6, 10);
        LocalDate june_20 = LocalDate.of(2020, 6, 20);

        config.add(new WorkHours(june_5, june_10, 6.0));
        config.add(new WorkHours(june_1, june_20, 7.0));

        config.sort();

        ListIterator<WorkHours> it = config.listIterator();
        assertTrue(it.hasNext());
        WorkHours wh1 = it.next();
        assertFalse(it.hasNext());

        // Two periods should be added
        assertEquals(7.0, wh1.getHours(), 0.01);
        assertEquals(june_1, wh1.getFrom());
        assertEquals(june_20, wh1.getTo());
    }

    /**
     * Test case: add a work-hour period which overlaps only with the first day of an existing period.
     * The first period should be truncated by the one day - the second one "erases the first day of it"
     */
    @Test
    public void testOverlapFirstDay() {
        // Must clear and make sure it is empty before each test - WorkHours is a singleton!
        WorkHourConfig config = WorkHourConfig.getInstance();
        config.clear();
        assertTrue(config.isEmpty());

        LocalDate d1 = LocalDate.of(2020, 6, 1);
        LocalDate d2 = LocalDate.of(2020, 6, 10);

        LocalDate d3 = LocalDate.of(2020, 5, 20);
        LocalDate d4 = LocalDate.of(2020, 6, 1);

        LocalDate d5 = LocalDate.of(2020, 6, 2);

        config.add(new WorkHours(d1, d2, 6.0));
        config.add(new WorkHours(d3, d4, 7.0));

        config.sort();

        ListIterator<WorkHours> it = config.listIterator();
        assertTrue(it.hasNext());
        WorkHours wh1 = it.next();
        assertTrue(it.hasNext());
        WorkHours wh2 = it.next();
        assertFalse(it.hasNext());

        // Two periods should be added
        assertEquals(7.0, wh1.getHours(), 0.01);
        assertEquals(d3, wh1.getFrom());
        assertEquals(d4, wh1.getTo());
        assertEquals(6.0, wh2.getHours(), 0.01);
        assertEquals(d5, wh2.getFrom());
        assertEquals(d2, wh2.getTo());
    }

    /**
     * Test case: add a 1-day period which overlaps with the first day of an existing period.
     * Should truncate the original period, get two periods as a result.
     */
    @Test
    public void testOverlapOneDayFirst() {
        // Must clear and make sure it is empty before each test - WorkHours is a singleton!
        WorkHourConfig config = WorkHourConfig.getInstance();
        config.clear();
        assertTrue(config.isEmpty());

        LocalDate d1 = LocalDate.of(2020, 6, 1);
        LocalDate d2 = LocalDate.of(2020, 6, 10);

        LocalDate d5 = LocalDate.of(2020, 6, 2);

        config.add(new WorkHours(d1, d2, 6.0));
        config.add(new WorkHours(d1, d1, 7.0));

        config.sort();

        ListIterator<WorkHours> it = config.listIterator();
        assertTrue(it.hasNext());
        WorkHours wh1 = it.next();
        assertTrue(it.hasNext());
        WorkHours wh2 = it.next();
        assertFalse(it.hasNext());

        // Two periods should be added
        assertEquals(7.0, wh1.getHours(), 0.01);
        assertEquals(d1, wh1.getFrom());
        assertEquals(d1, wh1.getTo());
        assertEquals(6.0, wh2.getHours(), 0.01);
        assertEquals(d5, wh2.getFrom());
        assertEquals(d2, wh2.getTo());
    }

    /**
     * Test case: add a 1-day period which overlaps with the first day of an existing period.
     * Should get three periods: first and last are the original period parts, second is the new 1-day period
     */
    @Test
    public void testOverlapOneDayMiddle() {
        // Must clear and make sure it is empty before each test - WorkHours is a singleton!
        WorkHourConfig config = WorkHourConfig.getInstance();
        config.clear();
        assertTrue(config.isEmpty());

        LocalDate d1 = LocalDate.of(2020, 6, 1);
        LocalDate d2 = LocalDate.of(2020, 6, 10);

        LocalDate d3 = LocalDate.of(2020, 6, 3);

        LocalDate d5 = LocalDate.of(2020, 6, 2);
        LocalDate d6 = LocalDate.of(2020, 6, 4);

        config.add(new WorkHours(d1, d2, 6.0));
        config.add(new WorkHours(d3, d3, 7.0));

        config.sort();

        ListIterator<WorkHours> it = config.listIterator();
        assertTrue(it.hasNext());
        WorkHours wh1 = it.next();
        assertTrue(it.hasNext());
        WorkHours wh2 = it.next();
        assertTrue(it.hasNext());
        WorkHours wh3 = it.next();
        assertFalse(it.hasNext());

        // Two periods should be added
        assertEquals(6.0, wh1.getHours(), 0.01);
        assertEquals(d1, wh1.getFrom());
        assertEquals(d5, wh1.getTo());
        assertEquals(7.0, wh2.getHours(), 0.01);
        assertEquals(d3, wh2.getFrom());
        assertEquals(d3, wh2.getTo());
        assertEquals(6.0, wh3.getHours(), 0.01);
        assertEquals(d6, wh3.getFrom());
        assertEquals(d2, wh3.getTo());
    }

    /**
     * Test case: add a 1-day period which overlaps with the last day of an existing period.
     * Should truncate the original period, get two periods as a result.
     */
    @Test
    public void testOverlapOneDayEnd() {
        // Must clear and make sure it is empty before each test - WorkHours is a singleton!
        WorkHourConfig config = WorkHourConfig.getInstance();
        config.clear();
        assertTrue(config.isEmpty());

        LocalDate d1 = LocalDate.of(2020, 6, 1);
        LocalDate d2 = LocalDate.of(2020, 6, 10);

        LocalDate d5 = LocalDate.of(2020, 6, 9);

        config.add(new WorkHours(d1, d2, 6.0));
        config.add(new WorkHours(d2, d2, 7.0));

        config.sort();

        ListIterator<WorkHours> it = config.listIterator();
        assertTrue(it.hasNext());
        WorkHours wh1 = it.next();
        assertTrue(it.hasNext());
        WorkHours wh2 = it.next();
        assertFalse(it.hasNext());

        // Two periods should be added
        assertEquals(6.0, wh1.getHours(), 0.01);
        assertEquals(d1, wh1.getFrom());
        assertEquals(d5, wh1.getTo());
        assertEquals(7.0, wh2.getHours(), 0.01);
        assertEquals(d2, wh2.getFrom());
        assertEquals(d2, wh2.getTo());
    }


    /**
     * Test case: add a work-hour period which overlaps with the beginning of an existing period, but has same number of hours
     * Should simply extend the period
     */
    @Test
    public void testOverlapBeginningSameHours() {
        // Must clear and make sure it is empty before each test - WorkHours is a singleton!
        WorkHourConfig config = WorkHourConfig.getInstance();
        config.clear();
        assertTrue(config.isEmpty());

        LocalDate d1 = LocalDate.of(2020, 6, 1);
        LocalDate d2 = LocalDate.of(2020, 6, 10);

        LocalDate d3 = LocalDate.of(2020, 5, 20);
        LocalDate d4 = LocalDate.of(2020, 6, 3);

        config.add(new WorkHours(d1, d2, 6.0));
        config.add(new WorkHours(d3, d4, 6.0));

        config.sort();

        ListIterator<WorkHours> it = config.listIterator();
        assertTrue(it.hasNext());
        WorkHours wh1 = it.next();
        assertFalse(it.hasNext());

        // Two periods should be added
        assertEquals(6.0, wh1.getHours(), 0.01);
        assertEquals(d3, wh1.getFrom());
        assertEquals(d2, wh1.getTo());
    }

    /**
     * Test case: add a work-hour period which overlaps with the end of an existing period, but has same number of hours
     * Should simply extend the period
     */
    @Test
    public void testOverlapEndSameHours() {
        // Must clear and make sure it is empty before each test - WorkHours is a singleton!
        WorkHourConfig config = WorkHourConfig.getInstance();
        config.clear();
        assertTrue(config.isEmpty());

        LocalDate d1 = LocalDate.of(2020, 6, 1);
        LocalDate d2 = LocalDate.of(2020, 6, 10);

        LocalDate d3 = LocalDate.of(2020, 6, 3);
        LocalDate d4 = LocalDate.of(2020, 6, 20);

        config.add(new WorkHours(d1, d2, 6.0));
        config.add(new WorkHours(d3, d4, 6.0));

        config.sort();

        ListIterator<WorkHours> it = config.listIterator();
        assertTrue(it.hasNext());
        WorkHours wh1 = it.next();
        assertFalse(it.hasNext());

        // Two periods should be added
        assertEquals(6.0, wh1.getHours(), 0.01);
        assertEquals(d1, wh1.getFrom());
        assertEquals(d4, wh1.getTo());
    }

    /**
     * Test case: add a work-hour period which overlaps with the first day of an existing period, but has same number of hours
     * Should simply extend the period
     */
    @Test
    public void testOverlapFirstDaySameHours() {
        // Must clear and make sure it is empty before each test - WorkHours is a singleton!
        WorkHourConfig config = WorkHourConfig.getInstance();
        config.clear();
        assertTrue(config.isEmpty());

        LocalDate d1 = LocalDate.of(2020, 6, 1);
        LocalDate d2 = LocalDate.of(2020, 6, 10);

        LocalDate d3 = LocalDate.of(2020, 5, 20);

        config.add(new WorkHours(d1, d2, 6.0));
        config.add(new WorkHours(d3, d1, 6.0));

        config.sort();

        ListIterator<WorkHours> it = config.listIterator();
        assertTrue(it.hasNext());
        WorkHours wh1 = it.next();
        assertFalse(it.hasNext());

        // Two periods should be added
        assertEquals(6.0, wh1.getHours(), 0.01);
        assertEquals(d3, wh1.getFrom());
        assertEquals(d2, wh1.getTo());
    }

    /**
     * Test case: add a work-hour period which overlaps with the last day of an existing period, but has same number of hours
     * Should simply extend the period
     */
    @Test
    public void testOverlapLastDaySameHours() {
        // Must clear and make sure it is empty before each test - WorkHours is a singleton!
        WorkHourConfig config = WorkHourConfig.getInstance();
        config.clear();
        assertTrue(config.isEmpty());

        LocalDate d1 = LocalDate.of(2020, 6, 1);
        LocalDate d2 = LocalDate.of(2020, 6, 10);

        LocalDate d3 = LocalDate.of(2020, 6, 20);

        config.add(new WorkHours(d1, d2, 6.0));
        config.add(new WorkHours(d2, d3, 6.0));

        config.sort();

        ListIterator<WorkHours> it = config.listIterator();
        assertTrue(it.hasNext());
        WorkHours wh1 = it.next();
        assertFalse(it.hasNext());

        // Two periods should be added
        assertEquals(6.0, wh1.getHours(), 0.01);
        assertEquals(d1, wh1.getFrom());
        assertEquals(d3, wh1.getTo());
    }

    /**
     * Test case: add a work-hour period which fits within an existing period, but has same number of hours
     * The original period should remain the same
     */
    @Test
    public void testFitsWithinSameHours() {
        // Must clear and make sure it is empty before each test - WorkHours is a singleton!
        WorkHourConfig config = WorkHourConfig.getInstance();
        config.clear();
        assertTrue(config.isEmpty());

        LocalDate d1 = LocalDate.of(2020, 6, 1);
        LocalDate d2 = LocalDate.of(2020, 6, 10);

        LocalDate d3 = LocalDate.of(2020, 6, 3);
        LocalDate d4 = LocalDate.of(2020, 6, 5);

        config.add(new WorkHours(d1, d2, 6.0));
        config.add(new WorkHours(d3, d4, 6.0));

        config.sort();

        ListIterator<WorkHours> it = config.listIterator();
        assertTrue(it.hasNext());
        WorkHours wh1 = it.next();
        assertFalse(it.hasNext());

        // Two periods should be added
        assertEquals(6.0, wh1.getHours(), 0.01);
        assertEquals(d1, wh1.getFrom());
        assertEquals(d2, wh1.getTo());
    }

    /**
     * Test case: add a work-hour period which spans over an existing period, but has same number of hours
     * The new period should be the only one remaining
     */
    @Test
    public void testIncludesSameHours() {
        // Must clear and make sure it is empty before each test - WorkHours is a singleton!
        WorkHourConfig config = WorkHourConfig.getInstance();
        config.clear();
        assertTrue(config.isEmpty());

        LocalDate d1 = LocalDate.of(2020, 6, 5);
        LocalDate d2 = LocalDate.of(2020, 6, 8);

        LocalDate d3 = LocalDate.of(2020, 6, 1);
        LocalDate d4 = LocalDate.of(2020, 6, 10);

        config.add(new WorkHours(d1, d2, 6.0));
        config.add(new WorkHours(d3, d4, 6.0));

        config.sort();

        ListIterator<WorkHours> it = config.listIterator();
        assertTrue(it.hasNext());
        WorkHours wh1 = it.next();
        assertFalse(it.hasNext());

        // Two periods should be added
        assertEquals(6.0, wh1.getHours(), 0.01);
        assertEquals(d3, wh1.getFrom());
        assertEquals(d4, wh1.getTo());
    }

    /**
     * Test case: |----|    |----|    |----|
     *              |-------------------|
     */
    @Test
    public void testTouchOverlapTouch() {
        // Must clear and make sure it is empty before each test - WorkHours is a singleton!
        WorkHourConfig config = WorkHourConfig.getInstance();
        config.clear();
        assertTrue(config.isEmpty());

        LocalDate june_1 = LocalDate.of(2020, 6, 1);
        LocalDate june_30 = LocalDate.of(2020, 6, 30);
        LocalDate july_1 = LocalDate.of(2020, 7, 1);
        LocalDate july_30 = LocalDate.of(2020, 7, 30);
        LocalDate august_1 = LocalDate.of(2020, 8, 1);
        LocalDate august_30 = LocalDate.of(2020, 8, 30);

        LocalDate june_9 = LocalDate.of(2020, 6, 9);
        LocalDate june_10 = LocalDate.of(2020, 6, 10);
        LocalDate august_10 = LocalDate.of(2020, 8, 10);
        LocalDate august_11 = LocalDate.of(2020, 8, 11);


        config.add(new WorkHours(june_1, june_30, 6.0));
        config.add(new WorkHours(july_1, july_30, 7.0));
        config.add(new WorkHours(august_1, august_30, 8.0));
        config.add(new WorkHours(june_10, august_10, 10.0));

        config.sort();

        ListIterator<WorkHours> it = config.listIterator();
        assertTrue(it.hasNext());
        WorkHours wh1 = it.next();
        assertTrue(it.hasNext());
        WorkHours wh2 = it.next();
        assertTrue(it.hasNext());
        WorkHours wh3 = it.next();
        assertFalse(it.hasNext());

        // Two periods should be added
        assertEquals(6.0, wh1.getHours(), 0.01);
        assertEquals(june_1, wh1.getFrom());
        assertEquals(june_9, wh1.getTo());
        assertEquals(10.0, wh2.getHours(), 0.01);
        assertEquals(june_10, wh2.getFrom());
        assertEquals(august_10, wh2.getTo());
        assertEquals(8.0, wh3.getHours(), 0.01);
        assertEquals(august_11, wh3.getFrom());
        assertEquals(august_30, wh3.getTo());
    }

    /**
     * Test case:    |----|    |----|    |----|
     *            |------------------------------|
     */
    @Test
    public void testOverlapThree() {
        // Must clear and make sure it is empty before each test - WorkHours is a singleton!
        WorkHourConfig config = WorkHourConfig.getInstance();
        config.clear();
        assertTrue(config.isEmpty());

        LocalDate june_1 = LocalDate.of(2020, 6, 1);
        LocalDate june_30 = LocalDate.of(2020, 6, 30);
        LocalDate july_1 = LocalDate.of(2020, 7, 1);
        LocalDate july_30 = LocalDate.of(2020, 7, 30);
        LocalDate august_1 = LocalDate.of(2020, 8, 1);
        LocalDate august_30 = LocalDate.of(2020, 8, 30);

        LocalDate may_1 = LocalDate.of(2020, 5, 1);
        LocalDate september_30 = LocalDate.of(2020, 9, 30);


        config.add(new WorkHours(june_1, june_30, 6.0));
        config.add(new WorkHours(july_1, july_30, 7.0));
        config.add(new WorkHours(august_1, august_30, 8.0));
        config.add(new WorkHours(may_1, september_30, 10.0));

        config.sort();

        ListIterator<WorkHours> it = config.listIterator();
        assertTrue(it.hasNext());
        WorkHours wh1 = it.next();
        assertFalse(it.hasNext());

        // Two periods should be added
        assertEquals(10.0, wh1.getHours(), 0.01);
        assertEquals(may_1, wh1.getFrom());
        assertEquals(september_30, wh1.getTo());
    }

    /**
     * Test case:    |----|    |----|    |----|
     *               |------------------------|  - starts and ends at exactly the same dates
     */
    @Test
    public void testOverlapThreeExactly() {
        // Must clear and make sure it is empty before each test - WorkHours is a singleton!
        WorkHourConfig config = WorkHourConfig.getInstance();
        config.clear();
        assertTrue(config.isEmpty());

        LocalDate june_1 = LocalDate.of(2020, 6, 1);
        LocalDate june_30 = LocalDate.of(2020, 6, 30);
        LocalDate july_1 = LocalDate.of(2020, 7, 1);
        LocalDate july_30 = LocalDate.of(2020, 7, 30);
        LocalDate august_1 = LocalDate.of(2020, 8, 1);
        LocalDate august_30 = LocalDate.of(2020, 8, 30);

        config.add(new WorkHours(june_1, june_30, 6.0));
        config.add(new WorkHours(july_1, july_30, 7.0));
        config.add(new WorkHours(august_1, august_30, 8.0));
        config.add(new WorkHours(june_1, august_30, 10.0));

        config.sort();

        ListIterator<WorkHours> it = config.listIterator();
        assertTrue(it.hasNext());
        WorkHours wh1 = it.next();
        assertFalse(it.hasNext());

        // Two periods should be added
        assertEquals(10.0, wh1.getHours(), 0.01);
        assertEquals(june_1, wh1.getFrom());
        assertEquals(august_30, wh1.getTo());
    }


    /**
     * Test case:    |----|    |----|    |----|
     *                |----------------------|  - starts and ends -1 day inside the existing intervals
     */
    @Test
    public void testOverlapThreeMinusOne() {
        // Must clear and make sure it is empty before each test - WorkHours is a singleton!
        WorkHourConfig config = WorkHourConfig.getInstance();
        config.clear();
        assertTrue(config.isEmpty());

        LocalDate june_1 = LocalDate.of(2020, 6, 1);
        LocalDate june_30 = LocalDate.of(2020, 6, 30);
        LocalDate july_1 = LocalDate.of(2020, 7, 1);
        LocalDate july_30 = LocalDate.of(2020, 7, 30);
        LocalDate august_1 = LocalDate.of(2020, 8, 1);
        LocalDate august_30 = LocalDate.of(2020, 8, 30);
        LocalDate june_2 = LocalDate.of(2020, 6, 2);
        LocalDate august_29 = LocalDate.of(2020, 8, 29);

        config.add(new WorkHours(june_1, june_30, 6.0));
        config.add(new WorkHours(july_1, july_30, 7.0));
        config.add(new WorkHours(august_1, august_30, 8.0));
        config.add(new WorkHours(june_2, august_29, 10.0));

        config.sort();

        ListIterator<WorkHours> it = config.listIterator();
        assertTrue(it.hasNext());
        WorkHours wh1 = it.next();
        assertTrue(it.hasNext());
        WorkHours wh2 = it.next();
        assertTrue(it.hasNext());
        WorkHours wh3 = it.next();
        assertFalse(it.hasNext());

        // Two periods should be added
        assertEquals(6.0, wh1.getHours(), 0.01);
        assertEquals(june_1, wh1.getFrom());
        assertEquals(june_1, wh1.getTo());
        assertEquals(10.0, wh2.getHours(), 0.01);
        assertEquals(june_2, wh2.getFrom());
        assertEquals(august_29, wh2.getTo());
        assertEquals(8.0, wh3.getHours(), 0.01);
        assertEquals(august_30, wh3.getFrom());
        assertEquals(august_30, wh3.getTo());
    }

    /**
     * Test case:    |----|    |----|    |----|
     *                      |--------------|
     */
    @Test
    public void testOverlapTwoAndThree() {
        // Must clear and make sure it is empty before each test - WorkHours is a singleton!
        WorkHourConfig config = WorkHourConfig.getInstance();
        config.clear();
        assertTrue(config.isEmpty());

        LocalDate june_1 = LocalDate.of(2020, 6, 1);
        LocalDate june_20 = LocalDate.of(2020, 6, 20);
        LocalDate july_1 = LocalDate.of(2020, 7, 1);
        LocalDate july_30 = LocalDate.of(2020, 7, 30);
        LocalDate august_1 = LocalDate.of(2020, 8, 1);
        LocalDate august_30 = LocalDate.of(2020, 8, 30);

        LocalDate june_25 = LocalDate.of(2020, 6, 25);
        LocalDate august_20 = LocalDate.of(2020, 8, 20);
        LocalDate august_21 = LocalDate.of(2020, 8, 21);

        config.add(new WorkHours(june_1, june_20, 6.0));
        config.add(new WorkHours(july_1, july_30, 7.0));
        config.add(new WorkHours(august_1, august_30, 8.0));
        config.add(new WorkHours(june_25, august_20, 10.0));

        config.sort();

        ListIterator<WorkHours> it = config.listIterator();
        assertTrue(it.hasNext());
        WorkHours wh1 = it.next();
        assertTrue(it.hasNext());
        WorkHours wh2 = it.next();
        assertTrue(it.hasNext());
        WorkHours wh3 = it.next();
        assertFalse(it.hasNext());

        // Two periods should be added
        assertEquals(6.0, wh1.getHours(), 0.01);
        assertEquals(june_1, wh1.getFrom());
        assertEquals(june_20, wh1.getTo());

        assertEquals(10.0, wh2.getHours(), 0.01);
        assertEquals(june_25, wh2.getFrom());
        assertEquals(august_20, wh2.getTo());

        assertEquals(8.0, wh3.getHours(), 0.01);
        assertEquals(august_21, wh3.getFrom());
        assertEquals(august_30, wh3.getTo());
    }

    /**
     * Test case:    |----|    |----|    |----|
     *                 |--------------|
     */
    @Test
    public void testOverlapOneAndTwo() {
        // Must clear and make sure it is empty before each test - WorkHours is a singleton!
        WorkHourConfig config = WorkHourConfig.getInstance();
        config.clear();
        assertTrue(config.isEmpty());

        LocalDate june_1 = LocalDate.of(2020, 6, 1);
        LocalDate june_20 = LocalDate.of(2020, 6, 20);
        LocalDate july_1 = LocalDate.of(2020, 7, 1);
        LocalDate july_20 = LocalDate.of(2020, 7, 20);
        LocalDate august_1 = LocalDate.of(2020, 8, 1);
        LocalDate august_30 = LocalDate.of(2020, 8, 30);

        LocalDate june_5 = LocalDate.of(2020, 6, 5);
        LocalDate july_25 = LocalDate.of(2020, 7, 25);
        LocalDate june_4 = LocalDate.of(2020, 6, 4);

        config.add(new WorkHours(june_1, june_20, 6.0));
        config.add(new WorkHours(july_1, july_20, 7.0));
        config.add(new WorkHours(august_1, august_30, 8.0));
        config.add(new WorkHours(june_5, july_25, 10.0));

        config.sort();

        ListIterator<WorkHours> it = config.listIterator();
        assertTrue(it.hasNext());
        WorkHours wh1 = it.next();
        assertTrue(it.hasNext());
        WorkHours wh2 = it.next();
        assertTrue(it.hasNext());
        WorkHours wh3 = it.next();
        assertFalse(it.hasNext());

        // Two periods should be added
        assertEquals(6.0, wh1.getHours(), 0.01);
        assertEquals(june_1, wh1.getFrom());
        assertEquals(june_4, wh1.getTo());

        assertEquals(10.0, wh2.getHours(), 0.01);
        assertEquals(june_5, wh2.getFrom());
        assertEquals(july_25, wh2.getTo());

        assertEquals(8.0, wh3.getHours(), 0.01);
        assertEquals(august_1, wh3.getFrom());
        assertEquals(august_30, wh3.getTo());
    }
}
