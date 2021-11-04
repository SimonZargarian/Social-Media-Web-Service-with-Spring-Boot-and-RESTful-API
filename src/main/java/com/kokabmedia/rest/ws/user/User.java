package com.kokabmedia.rest.ws.user;


import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import com.kokabmedia.rest.ws.post.Post;

/*
 * This is a model class for the purpose of retrieving, creating, updating, deleting 
 * data with REST resources as well as with the database, mapping HTTP POST request 
 * body data to java objects with controller methods in the UserResource and the 
 * UserJPAResource class.  
 * 
 * The @Entity annotation from javax.persistence enables the JPA framework to manage 
 * the User class as a JPA entity. The User class is an entity and will be  mapped to a 
 * database table with the name User. 
 * 
 * The @Entity annotation will automatically with Hibernate, JPA and Spring auto 
 * configuration create a User table in the H2 in memory database with the following SQL 
 * statement: create table user (id integer not null, birth_date timestamp, name 
 * varchar(255), primary key (id).
 * 
 * This class acts as an mapped entity class for handling data in a database with the 
 * UserRepository class that extends JpaRepository.
 */
@Entity	
public class User {
	
	/*
	 * The @Id annotation makes this field a primary key in the database table.
	 * 
	 * The @GeneratedValue annotation makes the Hibernate generate the primary 
	 * key value.
	 * 
	 * Primary key will uniquely identify each row in a database table.
	 */
	@Id
	@GeneratedValue
	private Integer id;
	
	@Size(min=2, message="Name should have atleast 2 characters")
	private String name;
	
	@Past
	private Date birthDate;
	
	/* 
	 * This field is for relation mapping purposes, a user can make a list of
	 * multiple posts.
	 * 
	 * The @OneToMany annotation indicates that User has one to many relationship 
	 * mapping with Post, one user can have multiple posts. The many side of the
	 * relationship will be the owning side. 
	 * 
	 * The select User query will also get the Post details from the database with 
	 * Eager Fetch from both the User and the Post table.
	 * 
	 * The mappedBy parameter is describing which table will be owning the relationship,
	 * The Post table will have a user_id column with a foreign key value and will 
	 * be owning the relationship. The user_id column in the Post table will have a 
	 * link to specific row in the User table. Multiple post can have the same user id.
	 * 
	 * The mappedBy parameter is set to the non owning side of the relationship. The
	 * mappedBy parameter makes sure that a post_id column is not created in the 
	 * User table.
	 * 
	 * The fetch strategy for the OneToMany side of the relations is Lazy Fetch, if we want 
	 * to get the Post details as well we need to call the user.getPosts method. We can 
	 * change the the fetch type parameter fetch=FetchType.Eager this will change the fetching 
	 * strategy of the Post entity so that the fetch type is Eager and Hibernate will fetch 
	 * the Post data when fetching the User data.
	 */
	@OneToMany(mappedBy="user")
	private List<Post> posts;
	
	/*
	 * JPA mandates a default no argument constructor, this constructor will be
	 * used by JPA to create this specific bean.
	 */
	public User(){}

	public User(Integer id, String name, Date birthDate) {
		super();
		this.id = id;
		this.name = name;
		this.birthDate = birthDate;
	}
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	/*
	 * The purpose of this method is to returns a textual representation 
	 * of the object, instead of for example hash code in the logger.
	 */
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", birthDate=" + birthDate + "]";
	}

}
