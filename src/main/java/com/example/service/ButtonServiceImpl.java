package com.example.service;

import java.util.List;

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

}
