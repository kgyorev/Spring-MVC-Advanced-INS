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
import org.junit.Before;
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
    private Person person1984;
    private Person personNow;
    private Product productLife1;
    private Contract contractDuration10Years;


    @Before
    public void setUp() {


        this.person1984 = new Person();
        this.person1984.setStartDate(LocalDate.ofYearDay(1984, 10));

        this.personNow = new Person();
        this.personNow.setStartDate(LocalDate.now());

        this.contractDuration10Years = new Contract();
        this.contractDuration10Years.setOwner(this.person1984);
        this.contractDuration10Years.setFrequency(Frequency.TRIMESTER);
        this.contractDuration10Years.setStartDt(LocalDate.ofYearDay(2015, 10));
        this.contractDuration10Years.setDuration(10);

        this.productLife1 = this.productService.findByIdntfr("LIFE1");

    }

    @Test
    public void OwnerLessThen18years() {
        Contract contract = new Contract();
        contract.setStartDt(LocalDate.now());
        contract.setFrequency(Frequency.MONTHLY);
        contract.setOwner(this.personNow);
        boolean condition = this.productService.checkProductRules(this.productLife1, contract);
        Assert.assertEquals(condition, false);
    }

    @Test
    public void OwnerMoreThen18years() {
        Contract contract = new Contract();
        contract.setOwner(this.person1984);
        contract.setFrequency(Frequency.ANUAL);
        contract.setStartDt(LocalDate.now());
        boolean condition = this.productService.checkProductRules(this.productLife1, contract);
        Assert.assertEquals(condition, true);
    }

    @Test
    public void FrequencyNotAllowed() {
        Contract contract = new Contract();
        contract.setOwner(this.person1984);
        contract.setFrequency(Frequency.MONTHLY);
        contract.setStartDt(LocalDate.now());
        boolean condition = this.productService.checkProductRules(this.productLife1, contract);
        Assert.assertEquals(condition, false);
    }
    @Test
    public void FrequencyAllowed() {
        Contract contract = new Contract();
        contract.setOwner(this.person1984);
        contract.setFrequency(Frequency.TRIMESTER);
        contract.setStartDt(LocalDate.now());
        boolean condition = this.productService.checkProductRules(this.productLife1, contract);
        Assert.assertEquals(condition, true);
    }
    @Test
    public void PremiumCalcuationCorrectBenfAmount70000() {
         this.contractDuration10Years.setAmount(70000d);
         Double premium = this.contractService.calculatePremiumAmount( this.contractDuration10Years);
        Assert.assertEquals(48.13,premium,0);
    }
    @Test
    public void PremiumCalcuationCorrectBenfAmount50000() {
        this.contractDuration10Years.setAmount(50000d);
        Double premium = this.contractService.calculatePremiumAmount(this.contractDuration10Years);
        Assert.assertEquals(34.38,premium,0);
    }
}
