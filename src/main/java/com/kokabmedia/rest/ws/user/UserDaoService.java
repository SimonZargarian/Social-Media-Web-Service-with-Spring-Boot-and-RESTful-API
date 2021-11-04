package com.kokabmedia.rest.ws.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/*
 * This class is responsible to work as a temporary storage in the early stages of development 
 * before the application start communicating with the database.
 * 
 * The @Component annotation allows the Spring framework to creates an instance (bean) of this 
 * class an manage it with the Spring Application Context (the IOC container) that maintains
 * all the beans for the application.  
 *
 * The @Component annotation lets the Spring framework manage the UserDaoService class as a 
 * Spring bean. The Spring framework will find the bean with auto-detection when scanning the 
 * class path with component scanning. It turns the class into a Spring bean at the auto-scan
 * time.
 * 
 * @Component annotation allows the UserDaoService to be wired in as dependency to a another 
 * object or a bean with the @Autowired annotation.
 */
@Component 
public class UserDaoService {

	private static List<User> users = new ArrayList<>();

	private static int usersCount = 3;

	// static field for temporary storage
	static {
		users.add(new User(1, "Adam", new Date()));
		users.add(new User(2, "Eve", new Date()));
		users.add(new User(3, "Jack", new Date()));
	}

	// find all users
	public List<User> findAll() {

		return users;
	}

	// Save user to list.
	public User save(User user) {

		// if id is null set id
		if (user.getId() == null) {
			user.setId(++usersCount);
		}

		users.add(user);

		return user;
	}

	// Locate a user with specific id.
	public User findById(int id) {

		for (User user : users) {
			if (user.getId() == id) {
				return user;
			}
		}
		// user id not found
		return null;
	}

	// Delete a user with specific id.
	public User deleteById(int id) {
	
		Iterator<User> iterator = users.iterator();
	
		while(iterator.hasNext()) {
			User user = iterator.next();
			if(user.getId()==id) {
				iterator.remove();
				return user;
			}
		}
		return null;
	}

}
