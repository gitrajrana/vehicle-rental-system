package com.car.rent.reservation.service;

import java.util.List;

import com.car.rent.domain.Reservation;

public interface ReservationService {
	void save(Reservation reservation);
	List<Reservation> getAll();
	void delete(Reservation reservation);
	void delete(int id);
	Reservation findById(int id);
	void update(Reservation res);
	
}
