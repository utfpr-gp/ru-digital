package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.User;
import com.example.service.UserService;

@RestController

public class EmailController {

	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private UserService userService;

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(path = "/email-send/{id}", method = RequestMethod.GET)
	public String sendMail(@PathVariable("id") long id) {
		SimpleMailMessage message = new SimpleMailMessage();
		System.out.println(id);
		User u = userService.getOne(id);

		message.setText("Ola " + u.getName()
				+ "\n  Seja bem vindo ao UTF COIN. \n Para confirmar seu cadastro faça uma recarga.");
		message.setTo(u.getEmail());
		message.setFrom("luizfelipebasile@gmail.com");
		System.out.println("OKOKOKKOOKKOOK");
		try {
			mailSender.send(message);
			return "jsonTemplate";
		} catch (Exception e) {
			e.printStackTrace();
			return "Erro ao enviar email.";
		}
	}
}