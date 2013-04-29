package com.srbdev.dbproject.model;

import java.sql.Date;


/**
 * This model is the object passed back from the back-end to the front-end in
 * the JSON format.
 */
public class Actor 
{
	private int id;
	private String aka;
	private String name;
	private Date birthday;
	private Date deathday;
	private String birthplace;
	private String pictureURL;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAka() {
		return aka;
	}
	public void setAka(String aka) {
		this.aka = aka;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public Date getDeathday() {
		return deathday;
	}
	public void setDeathday(Date deathday) {
		this.deathday = deathday;
	}
	public String getBirthplace() {
		return birthplace;
	}
	public void setBirthplace(String birthplace) {
		this.birthplace = birthplace;
	}
	public String getPictureURL() {
		return pictureURL;
	}
	public void setPictureURL(String pictureURL) {
		this.pictureURL = pictureURL;
	}
}
