/**
 * Opinion of a client
 * @author Eleonora Adova, EMSE
 * @version $Date: 2014/04/27 21:28 $ $Revision: 1.0 $
 **/

package hotelmania.group3.platform;

public class Opinion {
	
	private String clientId;
	private String hotel;
	private int rate;
	
	public Opinion(String clientId, String hotel, int rate) {
		this.clientId = clientId;
		this.hotel = hotel;
		this.rate = rate;
	}
	
	public String getClientId()
	{
		return clientId;
	}
	
	public String getHotel()
	{
		return hotel;
	}
	
	public int getRate()
	{
		return rate;
	}
	
	public String getOpinion()
	{
		return String.format("Opinion of cient %s: %d", clientId, rate);
	}
}
