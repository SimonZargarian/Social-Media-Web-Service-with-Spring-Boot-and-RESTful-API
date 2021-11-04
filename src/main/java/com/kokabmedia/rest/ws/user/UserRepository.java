package com.kokabmedia.rest.ws.user;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * This interface is used for handling data to and from the H2 in memory database.
 * 
 * The @Repository annotation allows the Spring framework to creates an instance (bean) 
 * of this interface and manage it with the Spring Application Context (the IOC container)
 * that maintains all the beans for the application.  
 *
 * The @Repository annotation lets the Spring framework manage the UserRepository 
 * interface as a Spring bean. The Spring framework will find the bean with auto-detection 
 * when scanning the class path with component scanning. It turns the class into a 
 * Spring bean at the auto-scan time.
 * 
 * @Repository annotation allows the UserRepository interface and its implementation to 
 * be wired in as dependency to a another object or a bean with the @Autowired annotation.
 * 
 * The @Repository annotation is a specialisation of @Component annotation for more specific 
 * use cases. 
 */
@Repository
/*
 * The entity that needs to be managed is User and the primary key is an Integer.
 * 
 * We can use all of the methods in the JpaRepository class to retrieve, store, update
 * and delete data in the embedded H2 database. JpaRepository is part the the Spring 
 * Data JPA. JpaRepository is an abstraction over EntityManager.
 */
public interface UserRepository extends JpaRepository<User, Integer>{
	

}
