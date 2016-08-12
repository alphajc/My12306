package com.neuedu.my12306.order;

/**
 * Created by Gavin on 2016/1/8.
 */
public class OrderFrom {
    private String order_number;
    private String train_number;
    private String datetime;
    private String inter_zone;
    private String pay_status;
    private String total_price;

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getTrain_number() {
        return train_number;
    }

    public void setTrain_number(String train_number) {
        this.train_number = train_number;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getInter_zone() {
        return inter_zone;
    }

    public void setInter_zone(String inter_zone) {
        this.inter_zone = inter_zone;
    }

    public String getPay_status() {
        return pay_status;
    }

    public void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }
}
