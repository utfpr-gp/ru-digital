package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.UserBalance;
import com.example.repository.UserBalanceRepository;

@Service("userbalanceService")
public class UserBalanceServiceImpl implements UserBalanceService {

	@Override
	public void saveUserBalance(UserBalance ub) {
		userbalanceRepository.save(ub);
	}

	@Autowired
	private UserBalanceRepository userbalanceRepository;

	public void updateUserBalance(UserBalance ub) {
		UserBalance x = userbalanceRepository.getOne(ub.getId());
		x.setBalance(ub.getBalance());
		userbalanceRepository.save(x);

	}

}
