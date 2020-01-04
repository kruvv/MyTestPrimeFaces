package ru.kruvv.testprimefases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ru.kruvv.primefaces.models.User;
import ru.kruvv.primefaces.services.UserService;
import ru.kruvv.primefaces.services.UserServiceImpl;

/**
 * @author Viktor Krupkin
 **/

public class UserServiceImplTest {

	private UserServiceImpl userServiceImpl;
	UserService user;
	List<User> defaultUser;

	@Before
	public void before() {
		userServiceImpl = new UserServiceImpl();
		user = Mockito.mock(UserService.class);
		defaultUser = new ArrayList<>();
		defaultUser.add(new User(1, "Jhon Dou", "jd", "123"));
		defaultUser.add(new User(2, "Mike Dou", "md", "123"));
		defaultUser.add(new User(3, "Elen Dou", "ed", "123"));

		when(user.findUser("jho")).thenReturn(defaultUser);
		when(user.findUser("mik")).thenReturn(defaultUser);
		when(user.findUser("ele")).thenReturn(defaultUser);
	}

	@After
	public void after() {

	}

	@Test
	public void testFindUser() {

		assertEquals(userServiceImpl.findUser("tom"), "Tom King");

//		List<String> find = new ArrayList<>();
//		find.add("jho");
//		find.add("mik");
//		find.add("ele");
//
//		find.stream().forEach(f -> {
//			assertEquals("[Jhon Dou, Mike Dou, Elen Dou]", user.findUser(f).toString());
//		});

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
