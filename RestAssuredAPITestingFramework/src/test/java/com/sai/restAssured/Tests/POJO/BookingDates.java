package com.sai.restAssured.Tests.POJO;

public class BookingDates {

    private String checkin;
    private String checkout;

    public BookingDates() {}
    
    public BookingDates(String checkin, String checkout) {
	super();
	this.checkin = checkin;
	this.checkout = checkout;
    }

    public String getCheckin() {
	return checkin;
    }

    public void setCheckin(String checkin) {
	this.checkin = checkin;
    }

    public String getCheckout() {
	return checkout;
    }

    public void setCheckout(String checkout) {
	this.checkout = checkout;
    }

    @Override
    public String toString() {
	return "BookingDates [checkin=" + checkin + ", checkout=" + checkout + "]";
    }

    
}
