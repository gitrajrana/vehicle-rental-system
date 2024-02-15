package com.car.rent.payment;

import java.util.List;

import com.car.rent.domain.Payment;
import com.car.rent.domain.Reservation;

public interface PaymentService {
	public void addPayment(Payment payment, Reservation obj);

	public List<Payment> findPaymentByID(String paymentId);

	public List<Payment> findAllPayment();

	public void cancelPayment(String paymentId);

	public Payment getPaymentObject(String paymentId);

	public void paymentUpdated(Payment payment, double amount);

	public double findTotalAmount(List<Payment> list);

	public List<Payment> searchPaymentByCustomerName(String customerName);
}
