package com.stackroute.keepnote.service;

import java.util.NoSuchElementException;

import com.stackroute.keepnote.exception.UserAlreadyExistsException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.repository.UserAutheticationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
* Service classes are used here to implement additional business logic/validation 
* This class has to be annotated with @Service annotation.
* @Service - It is a specialization of the component annotation. It doesn't currently 
* provide any additional behavior over the @Component annotation, but it's a good idea 
* to use @Service over @Component in service-layer classes because it specifies intent 
* better. Additionally, tool support and additional behavior might rely on it in the 
* future.
* */

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

	/*
	 * Autowiring should be implemented for the UserAuthenticationRepository. (Use
	 * Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword.
	 */
	@Autowired
	UserAutheticationRepository userAuthRepo;

	/*
	 * This method should be used to validate a user using userId and password. Call
	 * the corresponding method of Respository interface.
	 * 
	 */
	@Override
	public User findByUserIdAndPassword(String userId, String password) throws UserNotFoundException {
		User returned = userAuthRepo.findByUserIdAndUserPassword(userId, password);
		if (returned == null)
			throw new UserNotFoundException("NOT FOUND");
		else
			return returned;
	}

	/*
	 * This method should be used to save a new user.Call the corresponding method
	 * of Respository interface.
	 */

	@Override
	public boolean saveUser(User user) throws UserAlreadyExistsException {
		// User user1 = userAuthRepo.findByUserIdAndUserPassword(user.getUserId(),
		// user.getUserPassword());
		// if (user1 != user) {
		// userAuthRepo.saveAndFlush(user);
		// return true;
		// } else
		// return false;
		try {
			User userFound = userAuthRepo.findById(user.getUserId()).orElse(null);
			if (userFound != null)
				throw new UserAlreadyExistsException("Error !");
			else {
				userAuthRepo.save(user);
				return true;
			}
		} catch (NoSuchElementException e) {
			throw new UserAlreadyExistsException("Error !");
		}
	}
}
