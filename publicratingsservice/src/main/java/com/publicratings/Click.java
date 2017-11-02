package com.publicratings;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.*;

@Entity
public class Click {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private Integer type;
    private String timestamp;

    @ManyToOne(optional = false)
    private Place place;

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        // TODO: change to delivered date, if weemos d1 mini is able to do this
        // this.timestamp = timestamp;
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");

        this.timestamp = ft.format(dNow).toString();
    }
}
