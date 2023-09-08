package com.redhat.appfoundation.camel;

import java.util.Date;

public class Registration {


    private String date;
    private String event;
    private String guest;

    public String getGuest() {
        return guest;
    }
    public void setGuest(String guest) {
        this.guest = guest;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getEvent() {
        return event;
    }
    public void setEvent(String event) {
        this.event = event;
    }


}
