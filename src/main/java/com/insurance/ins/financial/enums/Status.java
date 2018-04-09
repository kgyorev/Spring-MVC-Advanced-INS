package com.insurance.ins.financial.enums;

public enum Status {
    PENDING("Pending"),
    PAID("Paid"),
    CANCELED("Canceled");
    private final String text;

    /**
     * @param text
     */
    Status(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    public String getStatusLabel() {
        return this.text;
    }
}
