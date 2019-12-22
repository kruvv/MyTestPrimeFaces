package ru.kruvv.testprimefases;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ru.kruvv.primefaces.models.User;
import ru.kruvv.primefaces.services.UserServiceImpl;

/**
 * @author Viktor Krupkin
 **/

public class UserServiceImplTest {

	private UserServiceImpl userService = new UserServiceImpl();

	@Before
	public void before() {
		User user = new User();
	}

	@After
	public void after() {

	}

	@Test
	public void testFindUser() {
		User user = new User();

		fail("Not yet implemented");
	}

	@Test
	public void testCheckLogin() {
		fail("Not yet implemented");
	}

	@Test
	public void testLogout() {
		fail("Not yet implemented");
	}

}
