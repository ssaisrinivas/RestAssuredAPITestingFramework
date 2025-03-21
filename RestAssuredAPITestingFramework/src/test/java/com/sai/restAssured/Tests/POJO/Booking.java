package com.sai.restAssured.Tests.POJO;

public class Booking {

    private String firstname;
    private String lastname;
    private String additionalneeds;
    private int totalprice;
    private boolean depositpaid;
    private BookingDates bookingdates;
   
    
    public Booking() {}


    public String getFirstname() {
        return firstname;
    }


    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }


    public String getLastname() {
        return lastname;
    }


    public void setLastname(String lastname) {
        this.lastname = lastname;
    }


    public String getAdditionalneeds() {
        return additionalneeds;
    }


    public void setAdditionalneeds(String additionalneeds) {
        this.additionalneeds = additionalneeds;
    }


    public int getTotalprice() {
        return totalprice;
    }


    public void setTotalprice(int totalprice) {
        this.totalprice = totalprice;
    }


    public boolean isDepositpaid() {
        return depositpaid;
    }


    public void setDepositpaid(boolean depositpaid) {
        this.depositpaid = depositpaid;
    }


    public BookingDates getBookingdates() {
        return bookingdates;
    }


    public void setBookingdates(BookingDates bookingdates) {
        this.bookingdates = bookingdates;
    }


    public Booking(String firstname, String lastname, String additionalneeds, int totalprice, boolean depositpaid,
	    BookingDates bookingdates) {
	super();
	this.firstname = firstname;
	this.lastname = lastname;
	this.additionalneeds = additionalneeds;
	this.totalprice = totalprice;
	this.depositpaid = depositpaid;
	this.bookingdates = bookingdates;
    }


    @Override
    public String toString() {
	return "Booking [firstname=" + firstname + ", lastname=" + lastname + ", additionalneeds=" + additionalneeds
		+ ", totalprice=" + totalprice + ", depositpaid=" + depositpaid + ", bookingdates=" + bookingdates
		+ "]";
    }
    
    
    
    
  
    
    
    
}
