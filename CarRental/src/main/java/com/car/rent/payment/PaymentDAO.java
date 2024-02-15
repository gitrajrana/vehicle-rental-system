package com.car.rent.payment;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.car.rent.domain.Payment;



public interface PaymentDAO extends JpaRepository<Payment, Long> {
	List<Payment> findByPaymentId(int paymentId);
	Page<Payment> findAll(Pageable pageable);
	
	
}
