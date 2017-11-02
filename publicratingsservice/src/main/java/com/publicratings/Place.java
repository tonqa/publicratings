package com.publicratings;

import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "place")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private String detail;
    private String picture;
    private String longitude;
    private String latitude;

    @Column(nullable=false)
    private Integer isFavorite = 0;
    
    private Integer clicksPos = 0;
    private Integer clicksNeg = 0;
    private Integer clicksTotal = 0;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "place")
    private Set<Click> clicks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Integer getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Integer isFavorite) {
        this.isFavorite = isFavorite != null ? isFavorite : 0;
    }

    public Integer getClicksPos() {
        return clicksPos;
    }
    
    public void increaseClicksPos() {
    	clicksPos += 1;
    	clicksTotal += 1;
    }
    
    public void increaseClicksNeg() {
    	clicksNeg += 1;
    	clicksTotal += 1;
    }

    public Integer getClicksNeg() {
        return clicksNeg;
    }

    public Integer getClicksTotal() {
        return clicksTotal;
    }
}