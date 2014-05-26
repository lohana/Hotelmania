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
import hotelmania.ontology.Hotel;
import hotelmania.ontology.HotelInformation;
import hotelmania.ontology.SharedAgentsOntology;
import hotelmania.ontology.Stay;

@SuppressWarnings("serial")
public class AgClient3 extends DayDependentAgent {
	
	public static final String EVALUATION_SERVICE = "Evaluation";
	public static final String NUMBEROFCLIENTS_QUERY = "NumberOfClients";
	public static final String BOOKAROOM_REQUEST = "BookARoom";
	public static final String BOOKING_OFFER = "BookingOffer";
	public static final String STAFF_QUERY_REF = "HotelStaff";
	
	public Codec codec = new SLCodec();
	public Ontology innerOntology = Ontology3.getInstance();
	
	public String id;
	
	// Information about the hotel where the client stayed
	public String hotel = "";
	public int rate = 0;
	private float budget = 0.0f;
	private int arrivalDay = 1;
	private int nightsToStay = 1;
	public AID hotelAID = null;

	private Stay stay;
	private CompleteOffer selectedOffer = null;
	private boolean isBooked = false;
	
	// All hotels in hotelmania
	private ArrayList<HotelInformation> hotelsInformation = new ArrayList<HotelInformation>();

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
		stay.setCheckIn(this.arrivalDay);
		stay.setCheckOut(this.arrivalDay + this.nightsToStay);
		
		ontology = SharedAgentsOntology.getInstance();
		Object[] pr = this.getArguments();
		this.id = (String)pr[0];
		
		System.out.println(getLocalName()+": New client entered");

        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(innerOntology);	
        getContentManager().registerOntology(ontology);		
        
        // Adds behavior for day communication
    	addDayBehaviour();
    	
    	//addBehaviour(new NUMBEROFCLIENTS_ExpectInform(this) );
    	
    	//addBehaviour(new NUMBEROFCLIENTS_ExpectFailure(this) );
    	
    	addBehaviour(new GetHotelInformation_ExpectforMessages(this));
    	
    	addBehaviour(new ReceiveOffers(this));
    	
    	//Book a room Behaviours
    	addBehaviour (new BOOKAROOM_ExpectAcceptation(this));
    	addBehaviour (new BOOKAROOM_ExpectNotUnderstood(this));
    	addBehaviour (new BOOKAROOM_ExpectRejection(this));
    	
    	// EndSimulation Behaviors 
    	addBehaviour (new SubscribeForEndSimulation(this));
    	addBehaviour (new SubscribeFrEndSimulation_ExpectforMessages(this));
    	
    	//Query Ref HotelStaff
    	addBehaviour (new GetHotelStaff(this));
    	addBehaviour (new GetHotelStaff_ExpectforMessages(this));
    	
    	
    	
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
	public boolean getisBooked()
	{
		return isBooked;
	}
	public int getcurrentday(){
		return currentDay; 
	}

	public void ChangesOnDayChange() {
		System.out.println(getLocalName() + ": Day changed to " + currentDay);
    	
    	// If the day is before the stay period ask all hotels for offers
    	if (currentDay < stay.getCheckIn() - 1) {
    		addBehaviour(new GetHotelInformation(this)); 
    		try {
				Thread.sleep(5000);
				if (hotelsInformation.size() == 0) {
					Thread.sleep(5000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    	for (HotelInformation h : hotelsInformation) {
	    		addBehaviour(new SendRequestForOffer(this, h.getHotel().getHotelAgent()));
	    	}

    	} else if (currentDay <= stay.getCheckIn()) {
    		isBooked = true;
    		
    		addBehaviour(new BOOKAROOM_BookARoomBehaviour(this));
    	} else if (isBooked && hotelAID != null && currentDay <= stay.getCheckOut()) {
    		// Ask for #clients and stuff
    		addBehaviour(new NUMBEROFCLIENTS_NumberOfClientsBehaviour(this));
    	}
    	if (isBooked && currentDay == stay.getCheckOut()) {
    		// Pay after waiting for two seconds to get the opinion of the last day and send the final rate
    		try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		
            // Adds a behavior to evaluate a hotel
            addBehaviour(new SendRate(this));
    	}
    }
	
	public Stay getStay() {
		return stay;
	}

	public void setStay(Stay stay) {
		this.stay = stay;
	}
	
	public void viewOffer(CompleteOffer offer) {
		if (selectedOffer == null || (!isBooked && ((selectedOffer.getPrice() == 0) || offer.getPrice() < selectedOffer.getPrice()))) {
			selectedOffer = offer;
		}
	}

	public CompleteOffer getSelectedOffer() {
		return selectedOffer;
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

	@SuppressWarnings("unchecked")
	public ArrayList<HotelInformation> getHotelsInformation() {
		return (ArrayList<HotelInformation>)hotelsInformation.clone();
	}
	
	public String getHotelName(AID hotelID){
		String hotelName = "";
		for (int i = 0; i < hotelsInformation.size(); i++) {
			if (hotelsInformation.get(i).getHotel().getHotelAgent().equals(hotelID)){
				hotelName = hotelsInformation.get(i).getHotel().getHotel_name();
			}
		}
		return hotelName;
		
	}
	
}
