package com.kokabmedia.rest.ws.post;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kokabmedia.rest.ws.user.User;

/*
 * This is a class for posts that the user can create and that will the stored in the
 * database.
 * 
 * This is a model class for the purpose of retrieving, creating, updating, deleting 
 * data with REST resources as well as with the database, mapping HTTP POST request 
 * body data to java objects with controller methods in the PostJPAResource class.  
 * 
 * The at @Entity annotation from javax.persistence enables the JPA framework to manage 
 * the Post class as a JPA entity. The Post class is an entity and will be  mapped to a 
 * database table. 
 * 
 * The @Entity annotation will automatically with Hibernate, JPA and Spring auto 
 * configuration create a Post table in the H2 in memory database with the following SQL 
 * statement: create table post (id integer not null, description varchar(255), user_id 
 * integer, primary key (id).
 * 
 * This class also acts as an mapped entity class for handling data in a database with the 
 * PostRepository class that extends JpaRepository.
 */
@Entity
public class Post {

	/*
	 * The @Id annotation makes this field a primary key in the database table.
	 * 
	 * The @GeneratedValue annotation makes the Hibernate generate the 
	 * primary key value.
	 * 
	 * Primary key will uniquely identify each row in a database table.
	 */
	@Id
	@GeneratedValue
	private Integer id;
	
	private String description;
	
	/*
	 * This field is for relation mapping purposes, it will hold an User object
	 * when it is mapped to it with the @OneToMany(mappedBy) annotation in the 
	 * User class and when the setUser() method is called. The Post table will have 
	 * link to the User table with a user_id column containing foreign key value.
	 * 
	 * The @ManyToOne annotation indicates that Post has a many to one relation 
	 * to User. One user can have many posts. 
	 * 
	 * JPA and Hibernate will with the @ManyToOne annotation on this field create
	 * a user_id column in the Post table a foreign key value. The Post table is 
	 * now owning the relationship.
	 * 
	 * The user_id column will link to a specific row in User table. Multiple post 
	 * objects (rows) can be linked to the same User row with user_id column. This 
	 * entity is the owning side of the relationship.
	 * 
	 * The fetch strategy for the ManyToOne side of the relations is Eager Fetch
	 * and the details of User entity will be fetched with the Post entity 
	 * automatically. The fetch type parameter indicates that the fetch type is 
	 * Lazy so if we do not call post.getUser method the framework will not retrieve 
	 * the details of User.
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	/*
	 * The @JsonIgnore annotation makes the framework ignore user details
	 * when retrieving a post bean with HTTP request. This field will be ignored.
	 * A user fetches posts, a post does not fetch users.
	 */
	@JsonIgnore
	private User user;

	/*
	 * The names of the fields and getter and setter methods must match the names of the
	 * data that is passed in the body of the the HTTP POST request, or else null will be 
	 * returned.
	 */
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
	/*
	 * The purpose of this method is to returns a textual representation 
	 * of the object, instead of for example hash code in the logger.
	 * 
	 * We do not print the details of the User, or user will try to print 
	 * post and post will try to print user and we would end up out of memory.
	 * 
	 */
	@Override
	public String toString() {
		return "Post [id=" + id + ", descriprion=" + description + "]";
	}
	
	
	
}
