package com.publicratings;

public class Place {

	private Long id;
	private String name;
	private String detail;
	private String picture;
	private String longitude;
	private String latitude;
	private Boolean isFavorite;
	private int clicksPos;
	private int clicksNeg;
	private int clicksTotal;
	private ObjectLinks _links;
	private int rating;

	public Place(){

	}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
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

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public ObjectLinks get_links() {
		return _links;
	}

	public void set_links(ObjectLinks _links) {
		this._links = _links;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public Boolean getIsFavorite() {
		return isFavorite;
	}

	public void setIsFavorite(Boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

	public int getClicksPos() {
		return clicksPos;
	}

	public void setClicksPos(int clicksPos) {
		this.clicksPos = clicksPos;
	}

	public int getClicksNeg() {
		return clicksNeg;
	}

	public void setClicksNeg(int clicksNeg) {
		this.clicksNeg = clicksNeg;
	}

	public int getClicksTotal() {
		return clicksTotal;
	}

	public void setClicksTotal(int clicksTotal) {
		this.clicksTotal = clicksTotal;
	}
}
