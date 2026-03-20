package com.lab.repository;

import com.lab.entity.Test;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {

	List<Test> findByTestNameContainingIgnoreCase(String keyword);
	
}