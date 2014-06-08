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
import java.util.HashMap;

import jade.content.lang.Codec;
import jade.content.lang.sl.*;
import jade.content.onto.*;
import hotelmania.group3.hotel.behaviour.*;
import hotelmania.group3.platform.DayDependentAgent;
import hotelmania.ontology.*;

@SuppressWarnings("serial")
public class AgHotel3 extends DayDependentAgent {
	
	// Code for the SL language used and instance of the ontology
	// SharedAgentsOntology that we have created
	public Codec codec = new SLCodec();
	public Ontology ontology = SharedAgentsOntology.getInstance();
	
	public IStrategy strategy = new FinalStrategy(this);
	
	public int id_account = 0;

	public final static String REGISTRATION_SERVICE = "Registration";
	public final static String HOTEL_NAME = "Hotel3";
	public static final String SIGN_CONTRACT = "SignContract";
	public static final String NUMBEROFCLIENTS_QUERY = "NumberOfClients";
	public static final String CREATEACCOUNT_SERVICE = "CreateAccount";
	public static final String BOOKING_OFFER = "BookingOffer";
	private HashMap<String, BookingOffer> offers = new HashMap<String, BookingOffer>();
	public ArrayList<String> BookingClients = new ArrayList<String>();
	public static final String BOOKAROOM_REQUEST = "BookARoom";
	public int numberOfRooms = 6;
	private HashMap<String, Stay> bookings = new HashMap<String, Stay>();
	
	private ArrayList<HotelInformation> hotelsInformation = new ArrayList<HotelInformation>();
	
	protected void setup(){
		System.out.println(this.getLocalName());
		System.out.println(this.getName());
		
		System.out.println(getLocalName()+": HOTEL HAS ENTERED");
		
		// Register of the codec and the ontology to be used in the ContentManager
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);
	
    	// Register in the bank
    	addBehaviour(new BankAccount_ReceiveAccount(this));
    	addBehaviour(new BankAccount_CreateAccount(this));
        
        // Register in Hotelmania
    	addBehaviour(new Registration_ExpectReqistrationAgree(this));
    	addBehaviour(new Registration_ExpectRegistrationRefuse(this));
    	addBehaviour(new Registration_ExpectRegistrationNotUnderstood(this));
        addBehaviour(new Registration_RegisterInHotelmania(this));  	
    	
    	// Sign contract for the first day
        addBehaviour(new  SignContract_ExpectFailure(this));
        addBehaviour(new SignContract_ExpectAccept(this));
    	addBehaviour(new SignContract_ExpectReject(this));
    	addBehaviour(new  SignContract_ExpectNotUnderstood(this));
    	addBehaviour(new  SignContract_ExpectInform(this));
    	addBehaviour(new SignContract_SignContract(this)); 	
    	
    	//Adds a behavior to process the Number of Clients Query Ref
    	addBehaviour(new NumberOfClients_ExpectQueryRef(this));
  	
    	 // Adds a behavior to queryref account status
       	addBehaviour(new BankAccount_ExpectAccountStatus(this));
       	
       	// Adds a behavior to expect Client Booking Request
       	addBehaviour(new Booking_RequestBookingARoom(this));
    	
    	// Adds behavior for day communication
    	addDayBehaviour();
    	
    	addBehaviour(new Offers_ReceiveOfferRequest(this));
    	
    	// EndSimulation Behaviors 
    	addBehaviour (new SimulationEnd_SubscribeForSimulationEnd(this));
    	addBehaviour (new SimulationEnd_ExpectSimulationEnd(this));
	}
	
	public void ChangesOnDayChange()
    {
    	System.out.println(getLocalName() + ": Day changed to " + currentDay);
    	addBehaviour(new  SignContract_SignContract(this));
       	addBehaviour(new BankAccount_RequestAccountStatus(this)); 	
    }
	
	public int getNumberOfClients(int day) {
		int result = 0;
		for (Stay stay : bookings.values()) {
			if (day >= stay.getCheckIn() && day <= stay.getCheckOut()) {
				result++;
			}
		}
		return result;
	}
	
	public void makeOffer(String client, BookingOffer offer) {
		offers.put(client, offer);
	}
	
	public boolean isValidOffer(String client, Price price) {
		if (offers.containsKey(client)) {
			BookingOffer currentOffer = offers.get(client);
			if (price.getAmount() == currentOffer.getRoomPrice().getAmount()) {
				return true;
			}
		}
		
		return false;
	}

	public boolean checkStay(Stay stay) {
		for (int i = stay.getCheckIn(); i < stay.getCheckOut(); i++) {
			if (getNumberOfClients(i) > 5) {
				return false;
			}
		}
		return true;
	}
	
	public void addHotelInformation(HotelInformation info) {
		for (int i = 0; i < hotelsInformation.size(); i++) {
			if (hotelsInformation.get(i).getHotel().getHotel_name().equals(info.getHotel().getHotel_name())) {
				hotelsInformation.get(i).setRating(info.getRating());
				return;
			}
		}
		HotelInformation hi = new HotelInformation();
		hi.setRating(info.getRating());
		Hotel hotel = new Hotel();
		hotel.setHotel_name(info.getHotel().getHotel_name());
		hotel.setHotelAgent(info.getHotel().getHotelAgent());
		hi.setHotel(hotel);
		hotelsInformation.add(hi);
	}
}

