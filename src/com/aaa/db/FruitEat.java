package com.aaa.db;

import java.io.Serializable;

import com.changhong.util.db.annotation.CHPrimaryKey;
import com.changhong.util.db.annotation.CHTransient;

public class FruitEat implements Serializable{

	/**
	 * 
	 */
	@CHTransient
	private static final long serialVersionUID = 1L;
	@CHPrimaryKey
	private long ID;
	private long fid;
	private String PhotoUrl;
	private String Name;
	private String Date;
	
	public long getID(){
		return ID;
	}
	
	public void setID(long id){
		ID = id;
	}
	
	public long getfid(){
		return fid;
	}
	
	public void setfid(long id){
		this.fid = id;
	}
	
	public String getPhotoUrl(){
		return PhotoUrl; 
	}
	
	public void setPhotoUrl(String url){
		PhotoUrl = url;
	}
	
	public String getName(){
		return Name; 
	}
	
	public void setName(String name){
		Name = name;
	}
	
	public String getDate(){
		return Date; 
	}
	
	public void setDate(String d){
		Date = d;
	}
}
