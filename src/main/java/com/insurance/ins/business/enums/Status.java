package com.insurance.ins.business.enums;

public enum Status {
    IN_FORCE("In Force"),
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
