/**
 * This agent has the following functionality: 
 * <ul>
 * <li> Asks in DF if "hotelmania" agent. 
 * <li> If there is any, it ask this agent for a registration service;
 *      if not, waits and try it again later
 * <li> Waits for an answer to the request
 * </ul>
 * @author Lohana Lema Moreta, EMSE
 * @version $Date: 2014/04/18 22:36:23 $ $Revision: 1.0 $
 * 
 **/

package hotelmania.group3.hotel;

import java.util.ArrayList;

import jade.content.lang.Codec;
import jade.content.lang.sl.*;
import jade.content.onto.*;
import jade.util.leap.HashMap;
import jade.util.leap.Map;
import hotelmania.group3.hotel.behaviour.*;
import hotelmania.group3.platform.DayDependentAgent;
 
import hotelmania.group3.platform.client.behaviour.NUMBEROFCLIENTS_ExpectFailure;
import hotelmania.group3.platform.client.behaviour.NUMBEROFCLIENTS_ExpectInform;
import hotelmania.ontology.*;

@SuppressWarnings("serial")
public class AgHotel3 extends DayDependentAgent {
	
	// Code for the SL language used and instance of the ontology
	// SharedAgentsOntology that we have created
	public Codec codec = new SLCodec();
	public Ontology ontology = SharedAgentsOntology.getInstance();
	
	public IStrategy strategy = new SimpleFixedStrategy();
	
	public int id_account = 0;

	public final static String REGISTRATION_SERVICE = "Registration";
	public final static String HOTEL_NAME = "Hotel3";
	public static final String SIGN_CONTRACT = "SignContract";
	public static final String NUMBEROFCLIENTS_QUERY = "NumberOfClients";
	public static final String CREATEACCOUNT_SERVICE = "CreateAccount";
	public static final String BOOKING_OFFER = "BookingOffer";
	private Map offers = new HashMap();
	public ArrayList<String> BookingClients = new ArrayList<String>();
	public static final String BOOKAROOM_REQUEST = "BookARoom";
	public int numberOfRooms = 6;
	
	protected void setup(){
		
		System.out.println(getLocalName()+": HOTEL HAS ENTERED");
		
		// Register of the codec and the ontology to be used in the ContentManager
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);
	
        // Adds a behaviour to search Hotelmania
        addBehaviour(new SearchHotelmania(this));
		
     	// Adds a behavior to process the ACEPTATION answer to an registration request
     	addBehaviour(new ExpectReqistrationAgree(this));

     	// Adds a behavior to process the REJECTION answer to an registration request
    	addBehaviour(new ExpectRegistrationRefuse(this));
    	
    	// Adds a behavior to process the NOT_UNDERSTOOD answer to an registration request
    	addBehaviour(new  ExpectRegistrationNotUnderstood(this));
    	
    	// Adds a behavior to request sign contract to Agency
    	addBehaviour(new  SIGNCONTRACT_SignContract(this));
    	
    	// Adds a behavior to process the ACEPTATION answer to a sign contract request
     	addBehaviour(new SIGNCONTRACT_ExpectAcceptation(this));

     	// Adds a behavior to process the REJECTION answer to a sign contract request
    	addBehaviour(new SIGNCONTRACT_ExpectRejection(this));
    	
    	// Adds a behavior to process the NOT_UNDERSTOOD answer to a sign contract request
    	addBehaviour(new  SIGNCONTRACT_ExpectNotUnderstood(this));
    	
    	//Adds a behavior to process the Number of Clients Query Ref
    	//addBehaviour(new  NUMBEROFCLIENTS_ExpectQueryRef(this));
    	
  	
    	 // Adds a behavior to queryref account status
       	addBehaviour(new GetAccountStatus_ExpectforMessages(this));
       	
       	// Adds a behavior to expect Client Booking Request
       	addBehaviour(new BOOKAROOM_BookARoomExpectRequest(this));
    	
    	// Adds behavior for day communication
    	addDayBehaviour();
    	
    	addBehaviour(new CreateAccount(this));
    	
    	addBehaviour(new ExpectAccount(this));
    	
    	addBehaviour(new ReceiveOfferRequests(this));
    	
    	
    	// EndSimulation Behaviors 
    	addBehaviour (new SubscribeForEndSimulation(this));
    	addBehaviour (new SubscribeFrEndSimulation_ExpectforMessages(this));

    	
	}
	
	public void ChangesOnDayChange()
    {
    	System.out.println(getLocalName() + ": Day changed to " + currentDay);
    	addBehaviour(new  SIGNCONTRACT_SignContract(this));
       	addBehaviour(new GetAccountStatus(this)); 	

    }
	
	public void makeOffer(String client, BookingOffer offer) {
		offers.put(client, offer);
	}
	
	public boolean isValidOffer(String client, Price price) {
		if (offers.containsKey(client)) {
			BookingOffer currentOffer = (BookingOffer)(offers.get(client));
			if (price.getAmount() == currentOffer.getRoomPrice().getAmount()) {
				return true;
			}
		}
		
		return false;
	}
}

