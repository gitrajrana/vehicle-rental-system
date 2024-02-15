package com.car.rent.reservation.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Service;

import com.car.rent.dao.ReservationDAO;
import com.car.rent.domain.Reservation;
@Service
public class ReservationServiceImpl implements ReservationService{

	@Autowired 
	ReservationDAO reservationDAO;
	@Override
	public void save(Reservation reservation) {
		// TODO Auto-generated method stub
		reservation.setReservationDateTime(new Date());
		reservationDAO.save(reservation);
	}

	@Override
	public List<Reservation> getAll() {
		// TODO Auto-generated method stub
		return reservationDAO.findAllOrderbyreservationIdDesc();
	}

	@Override
	public void delete(Reservation reservation) {
		// TODO Auto-generated method stub
		reservationDAO.delete(reservation);
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		reservationDAO.delete(id);
	}

	@Override
	public Reservation findById(int id) {
		// TODO Auto-generated method stub
		return reservationDAO.findByReservationId(id);
	}

	@Override
	public void update(Reservation res) {
		// TODO Auto-generated method stub
		java.text.SimpleDateFormat sdf = 
			     new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.format(res.getPickUpDateTime());
		sdf.format( res.getReturnDateTime());
		
		reservationDAO.update(new Date(),res.getPickUpDateTime(),res.getReturnDateTime(),res.getReservationId());
	}

}
