package com.car.rent.reservation.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.car.rent.domain.Person;
import com.car.rent.domain.Reservation;
import com.car.rent.domain.Vehicle;
import com.car.rent.reservation.service.PersonService;
import com.car.rent.reservation.service.ReservationService;
import com.car.rent.vehicle.dao.VehicleDAO;

//vhicle controller
@RequestMapping("/reservation/")
@Controller
public class ReservationController {
	final private String URL = "/reservation/";

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
	}

	private Logger logger = Logger.getLogger(ReservationController.class);
	@Autowired
	ReservationService reservationService;
	@Autowired
	VehicleDAO vehicleService;
	@Autowired
	PersonService personService;

	@GetMapping("add/{carid}")
	public String showForm(@PathVariable("carid") int carNumber, Reservation reservation, Model model) {
		model.addAttribute("carNumber", carNumber);
		return "addreservation";
	}

	@PostMapping("add/{carid}")
	public String add(@PathVariable("carid") int carNumber, @ModelAttribute Reservation reservation, Model model,
			BindingResult bindingResult, HttpSession sessionRev, @Param("addPayment") String addPayment) {
		// Person person = (Person) session.getAttribute("person");
		Vehicle vehicle = vehicleService.findByVehicleId(carNumber);
		//Person person = personService.findById(1);
		Person person = (Person) sessionRev.getAttribute("person");
		reservation.setPerson(person);
		reservation.setVehicle(vehicle);
		reservationService.save(reservation);
		vehicle.setIsAvailable(false);
		vehicleService.save(vehicle);
		sessionRev.setAttribute("reservationObject", reservation);
		double totalDay = reservation.getReturnDateTime().getDay() - reservation.getPickUpDateTime().getDay();
		double dayPrice = vehicle.getDailyPrice();
		double totalPrice = totalDay * dayPrice;
		System.out.println("mum"+totalPrice);
		sessionRev.setAttribute("totalPriceSession", totalPrice);
		if(addPayment.equals("Yes")){
			return "redirect:/payment/add-payment";
		}
		model.addAttribute("reservations", reservationService.getAll());
		return "redirect:/reservation/list";
	}

	@PostMapping("update/{carid}")
	public String update(@PathVariable("carid") int carNumber, @ModelAttribute Reservation reservation, Model model,
			BindingResult bindingResult, HttpSession session) {
		// Person person = (Person) session.getAttribute("person");
		Reservation res = reservationService.findById(reservation.getReservationId());
		res.setPickUpDateTime(reservation.getPickUpDateTime());
		res.setReservationDateTime(reservation.getReservationDateTime());
		res.setReturnDateTime(reservation.getReturnDateTime());
		reservationService.update(reservation);
		return "redirect:/reservation/list";
	}

	@GetMapping("list")
	public String showList(Model model) {
		model.addAttribute("reservations", reservationService.getAll());
		return "reservationList";
	}

	@GetMapping("delete/{resid}")
	public String delete(@PathVariable("resid") int resId) {
		Reservation reservation = reservationService.findById(resId);
		Vehicle vehicle =  reservation.getVehicle();
		vehicle.setIsAvailable(true);
		vehicleService.save(vehicle);
		reservationService.delete(resId);

		return "redirect:/reservation/list";
	}

	@GetMapping("edit/{resid}")
	public String edit(@PathVariable("resid") int resId, Model model) {
		Reservation res = reservationService.findById(resId);
		model.addAttribute("carNumber", res.getVehicle().getVehicleId());
		model.addAttribute("reservation", res);
		return "editReservation";
	}
}
