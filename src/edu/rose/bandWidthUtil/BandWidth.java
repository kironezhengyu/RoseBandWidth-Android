package edu.rose.bandWidthUtil;

import java.io.Serializable;

/**
 * Our score data object.
 */
public class BandWidth implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mName;
	private double mReceived;
	private int mId;
	private int mDate;
	private double mSent;

	public int getDate() {
		return mDate;
	}

	public void setDate(int mDate) {
		this.mDate = mDate;
	}

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		mId = id;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}


	public String toString() {
		return getName() + " " + getReceived()+" "+getSent() + " " + getDate();
	}

	public  double getSent() {
		return mSent;
	}
	public void setSent(double int2){
		this.mSent = int2;
		
	}

	public void setReceived(double int1) {
		this.mReceived = int1;
	}
	
	public double getTotal(){
		return mReceived + mSent;
	}

	public double getReceived() {
		return mReceived;
	}
}
