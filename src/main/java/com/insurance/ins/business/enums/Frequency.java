package com.insurance.ins.business.enums;

/**
 * Created by K on 1.4.2018 г..
 */
public enum Frequency {
    MONTHLY("Monthly"),
    TRIMESTER("Trimester"),
    SEMI_ANNUAL("Semi-Annual"),
    ANUAL("Annual");
    private final String text;

    /**
     * @param text
     */
    Frequency(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    public String getFreqLabel() {
        return this.text;
    }
}
