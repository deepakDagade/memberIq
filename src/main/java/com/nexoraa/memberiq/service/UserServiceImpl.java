package com.nexoraa.memberiq.service;

import java.util.Objects;

import javax.management.RuntimeErrorException;

import org.springframework.stereotype.Service;

import com.nexoraa.memberiq.entity.AppUser;
import com.nexoraa.memberiq.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

private final UserRepository userRepository;
	
	
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Transactional
	@Override
	public AppUser updateUserLastLogin(String email) {
		log.info("UserServiceImpl: updateUserLastLogin: {}", email);
		AppUser user = userRepository.findByEmailAndIsDeletedFalse(email);
		if (Objects.isNull(user)) {
			throw new RuntimeErrorException(null, "User not found");
		}
	//	userRepository.updateLastLogin(email, LocalDateTime.now());
		return user;
	}

}
