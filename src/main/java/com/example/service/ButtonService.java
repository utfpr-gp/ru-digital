package com.example.service;

import java.util.List;

import com.example.model.Button;

public interface ButtonService {
	public List<Button> findAll();

	public List<String> diferentButtons();

	public void saveButton(Button button);

	public void deletButton(Button button);

	public void updateButton(Button button);

	public Button getOne(long id);

}
