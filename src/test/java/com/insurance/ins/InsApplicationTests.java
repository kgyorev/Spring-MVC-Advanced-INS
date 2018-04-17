package com.insurance.ins;

import com.insurance.ins.business.entites.Contract;
import com.insurance.ins.business.entites.Product;
import com.insurance.ins.business.enums.Frequency;
import com.insurance.ins.business.services.ContractService;
import com.insurance.ins.business.services.DistributorService;
import com.insurance.ins.business.services.ProductService;
import com.insurance.ins.financial.services.MoneyInService;
import com.insurance.ins.financial.services.PremiumService;
import com.insurance.ins.prsnorg.entites.prsn.Person;
import com.insurance.ins.prsnorg.entites.prsn.services.PersonService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InsApplicationTests {
	@Autowired
	private ContractService contractService;
	@Autowired
	private ProductService productService;
	@Autowired
	private DistributorService distributorService;
	@Autowired
	private PersonService personService;
	@Autowired
	private PremiumService premiumService;
	@Autowired
	private MoneyInService moneyInService;
	@Test
	public void contextLoads() {
	}

	@Test
	public void OwnerLessThen18years() {
		Contract contract = new Contract();
		contract.setStartDt(LocalDate.now());
		contract.setFrequency(Frequency.MONTHLY);
		Person person = new Person();
		person.setStartDate(LocalDate.now());
		contract.setOwner(person);
		Product product = this.productService.findByIdntfr("LIFE1");
			boolean condition = this.productService.checkProductRules(product, contract);
		Assert.assertEquals(condition, false);
	}
	@Test
	public void OwnerMoreThen18years() {
		Contract contract = new Contract();
		Person person = new Person();
		person.setStartDate(LocalDate.ofYearDay(1984,10));
		contract.setOwner(person);
		Product product = this.productService.findByIdntfr("LIFE1");
		contract.setFrequency(Frequency.ANUAL);
		contract.setStartDt(LocalDate.now());
		boolean condition = this.productService.checkProductRules(product, contract);
		Assert.assertEquals(condition, true);
	}
	@Test
	public void FrequencyNotAllowed() {
		Contract contract = new Contract();
		Person person = new Person();
		person.setStartDate(LocalDate.ofYearDay(1984,10));
		contract.setOwner(person);
		Product product = this.productService.findByIdntfr("LIFE1");
		contract.setFrequency(Frequency.MONTHLY);
		contract.setStartDt(LocalDate.now());
		boolean condition = this.productService.checkProductRules(product, contract);
		Assert.assertEquals(condition, false);
	}
}
