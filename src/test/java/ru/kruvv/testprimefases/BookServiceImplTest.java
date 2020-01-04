package ru.kruvv.testprimefases;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import ru.kruvv.primefaces.services.BookServiceImpl;

/**
 * @author Viktor Krupkin
 **/

public class BookServiceImplTest {

	private BookServiceImpl service;
	private Date date;

	@Before
	public void before() {
		service = new BookServiceImpl();
		date = new Date("Sat, 12 Aug 1995 13:30:00 GMT");

	}

	@Test
	public final void testFindAllBooks() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testFormatDate() {
		assertTrue(service.formatDate(date).equals("1995-08-12"));
	}

	@Test
	public final void testOnCellEdit() {
		fail("Not yet implemented"); // TODO
	}

}
