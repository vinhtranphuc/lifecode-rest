package com.lifecode.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lifecode.jpa.entity.ConfirmationToken;
import com.lifecode.jpa.entity.Role;
import com.lifecode.jpa.entity.RoleName;
import com.lifecode.jpa.entity.User;
import com.lifecode.jpa.repository.ConfirmationTokenRepository;
import com.lifecode.jpa.repository.RoleRepository;
import com.lifecode.jpa.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	ConfirmationTokenRepository confirmationTokenRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	public boolean isExistsByUsername(String userName) {
		return userRepository.existsByUsername(userName);
	}

	public boolean isExistsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	public Optional<Role> getRoleByName(RoleName roleName) {
		return roleRepository.findByName(roleName);
	}

	public User saveUser(User user) {
		return userRepository.save(user);
	}

	public void saveConfirmToken(ConfirmationToken confirmationToken) {
		confirmationTokenRepository.save(confirmationToken);
	}
	
	public boolean isUserDisabled(String usernameOrEmail) {
		Optional<User> user = userRepository.findByUsernameOrEmail(usernameOrEmail,usernameOrEmail);
		return user.isPresent() && !user.get().isEnabled();
	}
	
	public Optional<ConfirmationToken> findByConfirmationToken(String token) {
		return confirmationTokenRepository.findByToken(token);
	}
	
	public void setEnabledUserByToken(ConfirmationToken confirmationToken) {
		User user = userRepository.findByEmailIgnoreCase(confirmationToken.getUser().getEmail()).get();
		user.setEnabled(true);
		userRepository.save(user);
	}
}
