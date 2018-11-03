package com.example.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional
	public void updateButton(Button button) {
		System.out.println("MEU NOME" + button.getName());
		System.out.println("MEU VALOR" + button.getValue());
		System.out.println("MEU ID" + button.getId());

		buttonRepository.updateButton(button.getName(), button.getValue(), button.getId());
	}

	@Override
	public void saveButton(Button button) {
		buttonRepository.save(button);
	}

	public void deletButton(Button button) {
		button.setDeleted(true);
		updateButton(button);
	}

	@PersistenceContext
	private EntityManager manager;

	public List<String> diferentButtons() {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.");
		List<String> z = manager.createQuery("select distinct e.name from Button e ").getResultList();

		return z;
	}
}
