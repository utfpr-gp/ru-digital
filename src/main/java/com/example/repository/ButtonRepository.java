package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Button;

@Repository("buttonRepository")
public interface ButtonRepository extends JpaRepository<Button, Long> {

}
