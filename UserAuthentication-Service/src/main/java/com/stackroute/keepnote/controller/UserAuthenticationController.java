package com.stackroute.keepnote.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import com.stackroute.keepnote.exception.UserAlreadyExistsException;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.service.UserAuthenticationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/*
 * As in this assignment, we are working on creating RESTful web service, hence annotate
 * the class with @RestController annotation. A class annotated with the @Controller annotation
 * has handler methods which return a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */
@RestController
public class UserAuthenticationController {

	/*
	 * Autowiring should be implemented for the UserAuthenticationService. (Use
	 * Constructor-based autowiring) Please note that we should not create an object
	 * using the new keyword
	 */
	static final long EXPIRATION = 5000000;
	Map<String, String> map = new HashMap<>();
	@Autowired
	UserAuthenticationService authService;

	public UserAuthenticationController(UserAuthenticationService authicationService) {
		this.authService = authicationService;
	}

	/*
	 * Define a handler method which will create a specific user by reading the
	 * Serialized object from request body and save the user details in the
	 * database. This handler method should return any one of the status messages
	 * basis on different situations: 1. 201(CREATED) - If the user created
	 * successfully. 2. 409(CONFLICT) - If the userId conflicts with any existing
	 * user
	 * 
	 * This handler method should map to the URL "/api/v1/auth/register" using HTTP
	 * POST method
	 */

	@PostMapping(value = "/api/v1/auth/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) throws UserAlreadyExistsException {
		try {
			authService.saveUser(user);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (UserAlreadyExistsException e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

	/*
	 * Define a handler method which will authenticate a user by reading the
	 * Serialized user object from request body containing the username and
	 * password. The username and password should be validated before proceeding
	 * ahead with JWT token generation. The user credentials will be validated
	 * against the database entries. The error should be return if validation is not
	 * successful. If credentials are validated successfully, then JWT token will be
	 * generated. The token should be returned back to the caller along with the API
	 * response. This handler method should return any one of the status messages
	 * basis on different situations: 1. 200(OK) - If login is successful 2.
	 * 401(UNAUTHORIZED) - If login is not successful
	 * 
	 * This handler method should map to the URL "/api/v1/auth/login" using HTTP
	 * POST method
	 */

	@PostMapping
	public ResponseEntity<?> login(@RequestBody User user) throws ServletException {

		String token = "";
		try {
			token = getToken(user.getUserId(), user.getUserPassword());
			map.clear();
			map.put("message", "user Successful logged In");
			map.put("token", token);
			return new ResponseEntity<>(map, HttpStatus.OK);
		} catch (Exception e) {
			String errorMessage = e.getMessage();
			map.clear();
			map.put("message", errorMessage);
			map.put("token", null);
			return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
		}
	}

	// Generate JWT token
	public String getToken(String username, String password) throws Exception {

		if (username == null || password == null)
			throw new ServletException();

		User user = authService.findByUserIdAndPassword(username, password);

		if (user == null)
			throw new ServletException();

		String jwt = Jwts.builder().setSubject(username).signWith(SignatureAlgorithm.HS256, "secretkey").compact();
		return jwt;
	}

}
