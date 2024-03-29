package com.car.rent.user.Controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.car.rent.domain.Account;
import com.car.rent.domain.AccountType;
import com.car.rent.domain.Address;
import com.car.rent.domain.Person;
import com.car.rent.user.Service.AccountService;
import com.car.rent.user.Service.PersonService;

/**
 * @author Tika Raj
 *
 */
@Controller
@RequestMapping("/login")
@SessionAttributes("person")
public class LoginController {

	Logger logger = Logger.getLogger(UserController.class);
	@Autowired
	private PersonService personService;
	@Autowired
	private AccountService accountService;

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	@RequestMapping("/handleLogin")
	public String handleLogin(Model model) {
		
		if (userHasAuthority("ADMIN")) {
			//model.addAttribute("user", true);
			logger.info("admin logged into system");
			return "redirect:" + "user/adminHomePage";
		} else if (userHasAuthority("CUSTOMER")) {

			// model.addAttribute("user", true);
			logger.info("user logged into system");
			// return "users/user/userHomePage";
			return "redirect:" + "user/userHomePage";
		} else {
			return "";
		}

	}

	@RequestMapping(value = "addUser", method = RequestMethod.GET)
	public String signUpPage(Model model) {

		Person person = new Person();
		Address address = new Address();
		Account account = new Account();
		model.addAttribute(person);
		model.addAttribute(address);
		model.addAttribute(account);
		return "/users/user/addUser";
	}

	@RequestMapping(value = "addUser", method = RequestMethod.POST)
	public String addUser(@Valid Person person, BindingResult result, Address address, Account account, Model model) {

		if (result.hasErrors()) {
			model.addAttribute(person);
			model.addAttribute(address);
			model.addAttribute(account);
			return "/users/user/addUser";
		}
		if (person != null) {
			Person prsn = new Person();
			prsn = personService.findByIdentificationNumber(person.getIdentificationNumber());
			if (prsn != null) {
				model.addAttribute("msg", "Person's Identification Number is Already Exists");
				return "/users/user/error";
			}
		}
		if (account != null) {
			Account acc = new Account();
			acc = accountService.findByUsername(account.getUsername());
			if (acc != null) {
				model.addAttribute("msg", "Username Already Exists");
				return "login/addUser";
			}
		}
		if (!result.hasErrors()) {
			person.setAddress(address);
			String password = accountService.MD5(account.getPassword());
			account.setPassword(password);
			account.setAccountType(AccountType.CUSTOMER);
			accountService.addAccount(account);
			person.setAccount(account);
			personService.addPerson(person);

			model.addAttribute("msg", person.getName());
			return "/users/user/thankyou";
		} 
			return "redirect:" + "/";
		

	}
	@RequestMapping(value = "admin", method = RequestMethod.GET)
	public String adminHome(Model model) {

		 
		return "/users/admin/HomePage";
	}

	public boolean userHasAuthority(String authority) {
		List<GrantedAuthority> authorities = getUserAuthorities();
		for (GrantedAuthority grantedAuthority : authorities) {
			if (authority.equals(grantedAuthority.getAuthority())) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public List<GrantedAuthority> getUserAuthorities() {
		return (List<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
	}
}
