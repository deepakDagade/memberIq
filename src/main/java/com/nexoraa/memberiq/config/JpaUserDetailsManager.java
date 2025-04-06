package com.nexoraa.memberiq.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexoraa.memberiq.entity.AppUser;
import com.nexoraa.memberiq.enums.Status;
import com.nexoraa.memberiq.repository.ScopeRepository;
import com.nexoraa.memberiq.repository.UserRepository;

@Service
public class JpaUserDetailsManager implements UserDetailsManager {

	private final UserRepository userRepository;

	private final ScopeRepository scopeRepository;

	public JpaUserDetailsManager(UserRepository userRepository, ScopeRepository scopeRepository) {
		this.userRepository = userRepository;
		this.scopeRepository = scopeRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Fetch user with roles
		AppUser userDetails = userRepository.findByEmailAndIsDeletedFalse(username);
		if (Objects.isNull(userDetails)) {
			throw new UsernameNotFoundException("Invalid Credentials.");
		}
		Collection<GrantedAuthority> authorities = new HashSet<>();
		// Return UserDetails with fetched authorities
		return new User(userDetails.getEmail(), userDetails.getPassword(),
				userDetails.getStatus().equals(Status.ACTIVE), true, true, true, authorities);
	}

	@Override
	public void createUser(UserDetails user) {
	}

	@Override
	public void updateUser(UserDetails user) {
	}

	@Override
	public void deleteUser(String username) {
	}

	@Override
	public void changePassword(String oldPassword, String newPassword) {
	}

	@Override
	public boolean userExists(String username) {
		AppUser appUser = userRepository.findByEmailAndIsDeletedFalse(username);
		return appUser.getEmail().equals(username);
	}

}