package com.example.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Button;
import com.example.repository.ButtonRepository;

@Service("buttonService")
public class ButtonServiceImpl implements ButtonService {

	@Autowired
	private ButtonRepository buttonRepository;

	@Override
	public List<Button> findAll() {
		return buttonRepository.findAll();
	}

	@Override
	public Button getOne(long id) {
		return buttonRepository.findOne(id);
	}

	@Override
	public void saveButton(Button button) {
		buttonRepository.save(button);
	}

	public void deletButton(Button button) {
		buttonRepository.delete(button);
	}

	@PersistenceContext
	private EntityManager manager;

	public List<String> diferentButtons() {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.");
		List<String> z = manager.createQuery("select distinct e.name from Button e ").getResultList();

		return z;
	}
}
