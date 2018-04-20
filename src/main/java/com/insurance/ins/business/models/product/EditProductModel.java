package com.insurance.ins.business.models.product;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EditProductModel {

    private Long id;

    @NotNull(message="Please enter Product Label")
    @Size(min=5,max = 30,message="Please enter Product Label minimum 5 and maximum 30 symbols ")
    private String label;

    @NotNull(message="Please enter Minimum Age")
    @Max(value = 100,message = "Maximum age could be 100")
    @Min(value = 0,message = "Minimum age could be 0")
    private int minAge;

    @NotNull(message="Please enter Maximum Age")
    @Max(value = 100,message = "Maximum age could be 100")
    @Min(value = 0,message = "Minimum age could be 0")
    private int maxAge;

    @NotNull(message="Please enter allowed Frequencies")
    @Size(min=1,message="Please enter allowed Frequencies")
    private String frequencyRule;

    public EditProductModel() {
        this.frequencyRule="";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public String getFrequencyRule() {
        return frequencyRule;
    }

    public void setFrequencyRule(String frequencyRule) {
        this.frequencyRule = frequencyRule;
    }
}
