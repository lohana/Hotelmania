package hotelmania.group3.ontology;

import jade.content.*;

@SuppressWarnings("serial")
public class Client implements Concept {
	
	private String clientId;
	private String hotel;
	private int rate;
	private float budget;
	   
	public void setClientId(String value) { 
		this.clientId = value;
	}
	   
	public String getClientId() {
		return this.clientId;
	}
	
	public void setHotel(String value) { 
		this.hotel = value;
	}
	   
	public String getHotel() {
		return this.hotel;
	}
	
	public void setRate(int value) { 
		this.rate = value;
	}
	   
	public int getRate() {
		return this.rate;
	}
	
	public String getInformation()
	{
		return String.format("Opinion of client %s for hotel %s: %d", this.clientId, this.hotel, this.rate);
	}
	
	public void setBudget(float budget)
	{
		this.budget = budget;
	}
	
	public float getBudget(){
		return this.budget;
	}
	
}
