package com.neuedu.my12306.contact;

/**
 * Created by Gavin on 2016/1/8.
 */
public class Contact {
    private String card_number;
    private String name;
    private String traveller_type;
    private String card_type;
    private String phone;

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTraveller_type() {
        return traveller_type;
    }

    public void setTraveller_type(String traveller_type) {
        this.traveller_type = traveller_type;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
