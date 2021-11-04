package com.kokabmedia.rest.ws.user;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.persistence.OneToMany;
import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.kokabmedia.rest.ws.exception.UserNotFoundException;
import com.kokabmedia.rest.ws.post.Post;
import com.kokabmedia.rest.ws.post.PostRepository;

/*
 * This class can also be named UserJPAController and its function is to handle HTTP requests, 
 * responses and expose recourses to other applications, functioning as a servlet and a as 
 * class that communicates with the repository class and the embed database retrieving,  
 * deleting, updating and sending data, this class will also convert a JSON payload to a 
 * java object of the entity class that is mapped to a database table.
 * 
 * The dispatcher servlet is the Front Controller for the Spring MVC framework handles all 
 * the requests of the root (/) of the web application. 
 * 
 * Dispatcher servlet knows all the HTTP request methods GET, POST, PUT AND DELETE 
 * and what java methods they are mapped to with annotations. Dispatcher servlet will 
 * delegate which controller should handle a specific request. Dispatcher servlet looks 
 * at the URL and the request method. 
 * 
 * The @RestController annotation will register this class as a rest controller it will be
 * able to receive HTTP request when they are sent and match the URL path. With this annotation 
 * the class can now handle REST requests. 
 * 
 * @Response body annotation which is part of the @RestController annotation is responsible 
 * for sending information back from the application to another application. 
 * 
 * When we put @ResponseBody on a controller, the response from that will be mapped by a 
 * http message converter(Jackson) into another format, for example a java object to JSON, 
 * XML or HTML. Response body converts the java object and sends the response back. 
 */
@RestController
public class UserJPAResource {
	
	
	/*
	 * The @Autowired annotation tells the Spring framework that the userRepository bean 
	 * and its implementation is an dependency of UserJPAResource class. It is a mechanism 
	 * for implementing Spring dependency injection.
	 * 
	 * @Autowired annotation enables dependency injection with Spring framework to avoid 
	 * tight coupling and enable loose coupling by calling a interface or the implementation 
	 * of an interface.
	 * 
	 * The Spring framework creates a instance (bean) of the UserRepository or its implementation 
	 * and inject (autowires) that instance into the UserJPAResource object when it is instantiated 
	 * as a autowired dependency.
	 * 
	 * The UserRepository and its implementation is now a dependency of the UserJPAResource class.
	 */
	@Autowired
	private UserRepository userRepository;
	
	
	/*
	 * This method will return a collection of users from the database using JPA.
	 * 
	 * When HTTP request is sent to a certain URL and that URL contains a path which
	 * is declared on the @GetMapping annotation, in this case the appended "jpa/users" this method 
	 * will be called.
	 * 
	 * The @GetMapping annotation will bind and make retrieveAllUsers method respond to a HTTP GET
	 * request.
	 */
	@GetMapping("/jpa/users") // http://localhost:8080/jpa/users
	public List<User> retrieveAllUsers(){
		
		/*
		 * This userRepository bean is managed by the Spring framework with
		 * dependency injection with autowiring.
		 * 
		 * findAll() method retrieves all users from the H2 in memory database.
		 */
		return userRepository.findAll();
	}
	
	
	/*
	 * This method returns an user with a specific id from the database using JPA.
	 * 
	 * When a GET HTTP request is sent to a certain URL and that URL contains a path which
	 * is declared on the @GetMapping annotation, in this case the appended "/jpa/users", this 
	 * method will be called.
	 * 
	 * The @GetMapping annotation will bind and make retrieveUser() respond to a HTTP GET
	 * request.
	 * 
	 * The ("/jpa/users/{id}") parameter allows the method to read the appended integer after 
	 * the URL http://localhost:8080/jpa/users/ as a path variable that is attached, so when a 
	 * int is appended after http://localhost:8080/jpa/users/ with a GET HTTP request the 
	 * retrieveUser(PathVariable int id) method is called. 
	 * 
	 * The name of the "/{id}" parameter must match the @PathVariable annotation argument int id.
	 */
	@GetMapping("/jpa/users/{id}") // http://localhost:8080/jpa/users/id
	/*
	 * EntityModel lest us create resources with links for HATEOAS in a model object,
	 * we return user data and the specified links. This allows us to perform actions
	 * on the response data.
	 * 
	 * The @PathVariable annotation will make the path variable in the URL available
	 * for this retrieveUser() method via the method argument. When a user id int is
	 * appended to http://localhost:8080/jpa/users/ it can be handled by the retrieveUser()
	 * method.
	 */
	public EntityModel<User> retrieveUser(@PathVariable int id) {
		
		/*
		 * Retrieves a a specific user from the database with the @PathVariable annotation 
		 * parameter id.
		 * 
		 * Optional ensures that even if User is null a proper object will be returned.
		 */
		Optional<User> user = userRepository.findById(id);
		
		/*
		 * Displays exception error message 400 Not Found in the HTTP response body with 
		 * the UserNotFoundException class, this is done with the Spring Boot framework 
		 * and Spring MVC auto configuration exception handling.
		 */
		if(!user.isPresent())
			throw new UserNotFoundException("id-"+id);
		
		/* 
		 * Creates an EntityModel of User.
		 * 
		 * EntityModel lest us create resources with links for HATEOAS, so we can
		 * return user data and the specified links. The HATEOAS framework manages 
		 * the link for us in the model object, this way we get a resource with the 
		 * links stored in them.
		 */
		EntityModel<User> model = EntityModel.of(user.get());
		
		/* 
		 * The purpose of WebMvcLinkBuilder is building links without hard coding them, 
		 * we get the link ("/users") from retrieveAllUsers() and build a new HATEOAS 
		 * link to it.
		 * 
		 * HATEOAS framework picks up the URL from the @GetMappping("/users")
		 * annotation as link related to retrieveAllUsers().
		 */
		WebMvcLinkBuilder linkToAllUsers = 
				linkTo(methodOn(this.getClass()).retrieveAllUsers()); 
		
		
		/* 
		 * Adds HATEOAS links to the EntityModel User, so it can be viewed in the HTTP GET
		 * request response body
		 * 
		 * The related string "all-users" is added for clarification purposes, so we now 
		 * where the link is pointing to. 
		 */
		model.add(linkToAllUsers.withRel("all-users"));
		
		//return EntityModel of user for HATEOAS
		return model;		
	}
	
	/*
	 * This method creates a new user with HTTP POST request containing a JSON body and stores
	 * it in the H2 in memory database using JPA. We get the content of the user coming is as 
	 * part of the request body.
	 * 
	 * The createUser() method will be a web service endpoint that converts JSON
	 * paylod into a java object.
	 * 
	 * When HTTP POST request is sent to a certain URL and that URL contains a path which
	 * is declared on the @PostMapping annotation then this method will be called, 
	 * in this case the appended "jpa/users".
	 * 
	 * The @PostMapping annotation will bind and make createUser() method respond to
	 * a HTTP POST request. The HTTP POST request body will be passed to the @RequestBody
	 * in this case User user.
	 */
	@PostMapping("/jpa/users")
	/*
	 * The @Valid annotation enables validation on User class.
	 * 
	 * The @RequestBody annotation enables this method to read information from a
	 * HTTP POST request that is coming in, when the JSON payload is read from the POST
	 * request body it will be converted to a java object by the HTTP message converter 
	 * (Jackson) and mapped to User bean and the createUser(@RequestBody User user) method 
	 * can use the user object. 
	 * 
	 * The JSON payload will be converted into a java object.
	 */	
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		
		// Save a user in the H2 in memory database.
		User savedUser = userRepository.save(user);
		
		/*
		 * ServletUriComponentsBuilder creates a URI for the location of a new created
		 * User resource. 
		 * 
		 * It shows the the URI of the newly created User resource the Location section
		 * of the HTTP response header.
		 * 
		 * The path() method appends the id to the @PostMapping("/users") URI, this way
		 * we do not have to hard code the URI.
		 * 
		 * buildAndExpand passes the value that we want replace in the path("/{id}" method.
		 * 
		 * Return current request URI.
		 */
		URI location = ServletUriComponentsBuilder.fromCurrentRequest() 
		.path("/{id}").buildAndExpand(savedUser.getId()).toUri();
		
		/* 
		 * Return HTTP status code 201 CREATED, as per HTTP best practices.
		 * 
		 * Pass in the URI location of the newly created User resource, so it can be returned
		 * as a HTTP response.
		 * 
		 * If the consumer wants to know where the resource was created they see in the 
		 * Location from the header of the response.
		 */
		return ResponseEntity.created(location).build();	
	}
	
	/*
	 * This method will delete a user with a specific id from the database using jPA.
	 * 
	 * When a HTTP DELETE request is sent to a certain URL and that URL contains a path which
	 * is declared on the @DeleteMapping annotation, in this case the appended "/jpa/users", this 
	 * method will be called.
	 * 
	 * The @DeleteMapping annotation will bind and make deleteUser() respond to a HTTP DELETE
	 * request.
	 * 
	 * The ("/jpa/users/{id}") parameter allows the method to read the appended
	 * int after the URL http://localhost:8080/jpa/users/ as a path variable that is
	 * attached, so when a id is appended after http://localhost:8080/jpa/users/
	 * with a DELETE HTTP request the deleteUser(@PathVariable int id) method is called. The 
	 * name of the "/{id}" parameter must match the @PathVariable annotation argument int id.
	 */
	@DeleteMapping("/jpa/users/{id}")
	public void deleteUser(@PathVariable int id) {
		
		/* 
		 * Delete a user from the database. 
		 * 
		 * Displays an automatic error message if user is not found. 
		 */
		userRepository.deleteById(id);			
	}
	
	
}
