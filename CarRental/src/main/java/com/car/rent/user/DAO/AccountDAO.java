package com.car.rent.user.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.car.rent.domain.Account;
import com.car.rent.domain.AccountType;

 
public interface AccountDAO extends JpaRepository<Account, Integer> {

	public abstract Account findByUsername(String username);
	
	@Modifying
	@Query("update Account a set a.username = ?1, a.password = ?2, a.active = ?3, a.accountType = ?4  where accountId =?5")
	 void setAccountByAccountId(String username, String password, Boolean active, AccountType accountType, Integer accountId);
	 
}
