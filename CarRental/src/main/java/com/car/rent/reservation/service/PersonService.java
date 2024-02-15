package com.car.rent.reservation.service;

import java.util.List;

import com.car.rent.domain.Person;

public interface PersonService {
	void save(Person person);
	List<Person> getAll();
	void delete(Person person);
	void delete(int id);
	Person findById(int id);
	
}
