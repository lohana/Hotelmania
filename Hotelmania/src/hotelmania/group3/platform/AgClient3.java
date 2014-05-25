/**
 * Client agent
 * @author Eleonora Adova, EMSE
 * @version $Date: 2014/04/27 20:28 $ $Revision: 1.0 $
 **/

package hotelmania.group3.platform;

import java.util.ArrayList;

import jade.content.lang.Codec;
import jade.content.lang.sl.*;
import jade.content.onto.*;
import jade.core.AID;
 
import hotelmania.group3.ontology.Ontology3;
import hotelmania.group3.platform.client.behaviour.*;
import hotelmania.ontology.SharedAgentsOntology;
import hotelmania.ontology.Stay;

@SuppressWarnings("serial")
public class AgClient3 extends DayDependentAgent {
	
	public static final String EVALUATION_SERVICE = "Evaluation";
	public static final String NUMBEROFCLIENTS_QUERY = "NumberOfClients";
	public static final String BOOKAROOM_REQUEST = "BookARoom";
	public static final String BOOKING_OFFER = "BookingOffer";
	
	public Codec codec = new SLCodec();
	public Ontology innerOntology = Ontology3.getInstance();
	
	public String id;
	
	
	// Information about the hotel where the client stayed
	public String hotel = "";
	public int rate = 0;
	private float budget = 0.0f;
	private int arrivalDay = 1;
	private int nightsToStay = 1;
	public AID hotelAID;

	private Stay stay;
	private CompleteOffer selectedOffer = new CompleteOffer();
	private boolean isBooked = false;
	
	// All hotels in hotelmania
	private ArrayList<AID> hotels= new ArrayList<AID>();

	protected void setup(){
		
		Object[] args = getArguments();
        String name= (String) args[0];
        float budget = (float) args[1];
        int arrivalDay = (int) args[2];
        int nightsToStay = (int) args[3];
		
		this.id = name;
		this.budget = budget;
		this.arrivalDay = arrivalDay;
		this.nightsToStay = nightsToStay;
		
		this.stay = new Stay();
		stay.setCheckIn(arrivalDay);
		stay.setCheckOut(arrivalDay + nightsToStay);
		
		ontology = SharedAgentsOntology.getInstance();
		Object[] pr = this.getArguments();
		this.id = (String)pr[0];
		
		System.out.println(getLocalName()+": New client entered");

        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(innerOntology);	
        getContentManager().registerOntology(ontology);		
        
        // TOREMOVE
        // Random generation of rate, should be assign when going to the hotel 
        rate = randomValue();
        hotel = "Hotel3";
        hotelAID =  new AID("Hotel",AID.ISLOCALNAME);
        
        // Adds a behavior to evaluate a hotel
        // addBehaviour(new SendRate(this));
        
        // Adds behavior for day communication
    	addDayBehaviour();
    	
    	//addBehaviour(new NUMBEROFCLIENTS_NumberOfClientsBehaviour(this) );
    	
    	//addBehaviour(new NUMBEROFCLIENTS_ExpectInform(this) );
    	
    	//addBehaviour(new NUMBEROFCLIENTS_ExpectFailure(this) );
    	
    	addBehaviour(new GetHotelInformation_ExpectforMessages(this));
    	
    	addBehaviour(new ReceiveOffers(this));
    	
    	// EndSimulation Behaviors 
    	addBehaviour (new SubscribeForEndSimulation(this));
    	addBehaviour (new SubscribeFrEndSimulation_ExpectforMessages(this));
    	
    	// Change THIS - Eli
    	hotels.add(hotelAID);
    	
    	//System.out.println("Client generated - " + budget + " " + stay.getCheckIn() + "/" + stay.getCheckOut());
    }
	
	public int getRate()
	{
		return rate;
	}
	
	public String getHotel()
	{
		return hotel;
	}
	
	public AID getHotelAID()
	{
		return hotelAID;
	}

	private int randomValue() {
		return (int)(Math.random() * 11);
	}
	
	public void ChangesOnDayChange()
    {
		System.out.println(getLocalName() + ": Day changed to " + currentDay);
		//addBehaviour(new NUMBEROFCLIENTS_NumberOfClientsBehaviour(this) );
    	addBehaviour(new GetHotelInformation(this));
    	
    	// If the day is before the stay period ask all hotels for offers
    	if (currentDay < stay.getCheckIn()) {
	    	for (AID aidHotel : hotels) {
	    		addBehaviour(new SendRequestForOffer(this, aidHotel));
	    	}
    	}
    	if (currentDay == 2) {
    		isBooked = true;
    		addBehaviour(new BOOKAROOM_BookARoomBehaviour(this));
    	}
    }
	
	public Stay getStay() {
		return stay;
	}

	public void setStay(Stay stay) {
		this.stay = stay;
	}
	
	public void viewOffer(CompleteOffer offer) {
		if (!isBooked && ((selectedOffer.getPrice() == 0) || offer.getPrice() < selectedOffer.getPrice())) {
			selectedOffer = offer;
		}
	}

	public CompleteOffer getSelectedOffer() {
		return selectedOffer;
	}
}
