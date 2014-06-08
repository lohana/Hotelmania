
package hotelmania.group3.ontology;

import jade.content.*;

@SuppressWarnings("serial")
public class Client implements Concept {
	
	private String clientId;
	private String hotel;
	private float rate;
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
	
	public void setRate(float value) { 
		this.rate = value;
	}
	   
	public float getRate() {
		return this.rate;
	}
	
	public String getInformation()
	{
		return String.format("Opinion of client %s for hotel %s: %f", this.clientId, this.hotel, this.rate);
	}
	
	public void setBudget(float budget)
	{
		this.budget = budget;
	}
	
	public float getBudget(){
		return this.budget;
	}
	
}
