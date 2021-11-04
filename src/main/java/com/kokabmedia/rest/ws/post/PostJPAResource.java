package com.kokabmedia.rest.ws.post;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.kokabmedia.rest.ws.exception.UserNotFoundException;
import com.kokabmedia.rest.ws.user.User;
import com.kokabmedia.rest.ws.user.UserRepository;

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
public class PostJPAResource {
	
	/*
	 * The @Autowired annotation tells the Spring framework that the userRepository bean 
	 * and its implementation is an dependency of UserController class. It is a mechanism 
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
	
	@Autowired
	PostRepository postRepository;
	
	/*
	 * This method retrieves posts that are related to a specific user.
	 * 
	 * When a GET HTTP request is sent to a certain URL and that URL contains a path which
	 * is declared on the @GetMapping annotation, in this case the appended "/jpa/users/{id}/posts", 
	 * this  method will be called.
	 * 
	 * The @GetMapping annotation will bind and make retrieveAllPosts() respond to a HTTP GET
	 * request.
	 * 
	 * The ("/jpa/users/{id}") parameter allows the method to read the appended integer after 
	 * the URL http://localhost:8080/jpa/users/ as a path variable that is attached, so when a 
	 * int is appended after http://localhost:8080/jpa/users/ with a GET HTTP request the 
	 * retrieveAllPosts(PathVariable int id) method is called. 
	 * 
	 * The name of the "/{id}" parameter must match the @PathVariable annotation argument int id.
	 * 
	 * The @PathVariable annotation collects a user with a specific id from the database, with the 
	 * HTTP GET request URL.
	 */
	@GetMapping("/jpa/users/{id}/posts")
	public List<Post> retrieveAllPosts(@PathVariable int id){
		
		/* 
		 * Retrieves a a specific user from the H2 in memory database with 
		 * the @PathVariable annotation parameter id
		 */
		Optional<User> userOptional = userRepository.findById(id);
		
		/*
		 * Displays exception error message 400 Not Found in the HTTP response body with 
		 * the UserNotFoundException class, this is done with the Spring Boot framework 
		 * and Spring MVC auto configuration exception handling.
		 */
		 if(!userOptional.isPresent()) {
			throw new UserNotFoundException("id " + id);
		}
		
		// Retrieves and returns posts form a specific user in the database
		return userOptional.get().getPosts();
	}

	/*
	 * This method creates a new post for a specific user with HTTP POST request containing a JSON 
	 * body and stores it in the database using JPA. We get the content of the post coming in as part 
	 * of the request body.
	 *
	 * The @PathVariable annotation collects a user a specific id from URL and retrieves it from 
	 * the database.
	 * 
	 * The ("/jpa/users/{id}") parameter allows the method to read the appended integer after 
	 * the URL http://localhost:8080/jpa/users/ as a path variable that is attached, so when a 
	 * int is appended after http://localhost:8080/jpa/users/ with a POST HTTP request the 
	 * createPost(PathVariable int id) method is called. 
	 * 
	 * The @RequestBody annotation enables this method to read information from a
	 * HTTP POST request that is coming in, when the JSON payload is read from the POST
	 * request body it will be converted to a java object by the HTTP message converter 
	 * (Jackson) and mapped to Post bean and the createPost(@RequestBody Post post) method 
	 * can use the post object.
	 */
	@PostMapping("/jpa/users/{id}/posts")
	public ResponseEntity<Object> createPost(@PathVariable int id, @RequestBody Post post) {
		
		/* 
		 * Retrieves a specific user from the database with the @PathVariable annotation 
		 * parameter id.
		 * 
		 * Optional ensures that even if User is null a proper object will be returned.
		 */
		Optional<User> userOptional = userRepository.findById(id);
		
		if(!userOptional.isPresent()) {
			throw new UserNotFoundException("id " + id);
		}
		
		// Passes the user that is retrieved from the database.
		User user = userOptional.get();
		
		/*
		 *  Map the the user bean to the post bean with the help of the @OneToMany(mappedBy="user") 
		 *  annotation in the User class. The Post table will have link to the User table with a 
		 *  user_id column. 
		 */
		post.setUser(user);
		
		// Saves the new post to H2 in memory database.
		postRepository.save(post);
			
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
		.path("/{id}").buildAndExpand(post.getId()).toUri();
		
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

}
