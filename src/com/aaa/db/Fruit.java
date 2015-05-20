package com.aaa.db;

import java.io.Serializable;
import java.util.Date;

import com.changhong.util.db.annotation.CHPrimaryKey;
import com.changhong.util.db.annotation.CHTransient;

public class Fruit implements Serializable{
	/**
	 * 
	 */
	@CHTransient
	private static final long serialVersionUID = 1L;
	@CHPrimaryKey
	private long ID;
	private String PhotoUrl;
	private String Name;
	private Date Deadline;
	private boolean needZD;
	private boolean needAwake;
	private int AwakeTime;
	private String Date;
	private boolean isDelete = false;
	private boolean hasAwake = false;
	private Date eatDate = null;
	private long time;
	
	public long getID(){
		return ID;
	}
	
	public void setID(long id){
		ID = id;
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
	
	public Date getDeadline(){
		return Deadline; 
	}
	
	public void setDeadline(Date date){
		Deadline = date;
	}
	
	public Date geteatDate(){
		return eatDate; 
	}
	
	public void seteatDate(Date date){
		eatDate = date;
	}
	
	public boolean getneedZD(){
		return needZD; 
	}
	
	public void setneedZD(boolean b){
		needZD = b;
	}
	
	public boolean getneedAwake(){
		return needAwake; 
	}
	
	public void setneedAwake(boolean b){
		needAwake = b;
	}
	
	public int getAwakeTime(){
		return AwakeTime; 
	}
	
	public void setAwakeTime(int i){
		AwakeTime = i;
	}
	
	public String getDate(){
		return Date; 
	}
	
	public void setDate(String d){
		Date = d;
	}

	public boolean gethasAwake(){
		return hasAwake; 
	}
	
	public void sethasAwake(boolean b){
		hasAwake = b;
	}
	
	public boolean getIsDelete(){
		return isDelete; 
	}
	
	public void setIsDelete(boolean b){
		isDelete = b;
	}
	
	public long getOrder(){
		return time;
	}
	
	public void setOrder(long l){
		time = l;
	}
}
