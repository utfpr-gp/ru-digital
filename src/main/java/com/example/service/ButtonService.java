package com.example.service;

import java.util.List;

import com.example.model.Button;

public interface ButtonService {
	public List<Button> findAll();

	public void saveButton(Button button);

	public void deletButton(Button button);

	public Button getOne(long id);

}
