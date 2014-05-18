/**
 * StoresOffer and its hotel
 * @author Eleonora Adova, EMSE
 * @version $Date: 2014/05/17 22:36:23 $ $Revision: 1.0 $
 **/
package hotelmania.group3.platform;

import jade.core.AID;

public class CompleteOffer {
	
	   private AID hotel;
	   
	   private float price;
	   
	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public void setHotel(AID value) { 
	    this.hotel=value;
	   }
	   
	   public AID getHotel() {
	     return this.hotel;
	   }
}
