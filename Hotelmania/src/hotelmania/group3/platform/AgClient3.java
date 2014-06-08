/**
 * Client agent
 * @author Eleonora Adova, EMSE
 * @version $Date: 2014/04/27 20:28 $ $Revision: 1.0 $
 **/

package hotelmania.group3.platform;

import java.util.ArrayList;
import java.util.HashMap;

import jade.content.lang.Codec;
import jade.content.lang.sl.*;
import jade.content.onto.*;
import jade.core.AID;
 
import hotelmania.group3.ontology.Ontology3;
import hotelmania.group3.platform.client.behaviour.*;
import hotelmania.ontology.Contract;
import hotelmania.ontology.Hotel;
import hotelmania.ontology.HotelContract;
import hotelmania.ontology.HotelInformation;
import hotelmania.ontology.NumberOfClients;
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
	private float budget_per_day = 0.0f;
	private int arrivalDay = 1;
	private int nightsToStay = 1;
	public AID hotelAID = null;
	private HashMap<Integer, Contract> staffInfo = new HashMap<Integer, Contract>();
	private ArrayList<Integer> clientsInfo = new ArrayList<Integer>();

	private Stay stay;
	private CompleteOffer selectedOffer = null;
	
	//For Booking
	private boolean hasbooked = false;
	private boolean stayathotel = false; 
	
	// All hotels in hotelmania
	private ArrayList<HotelInformation> hotelsInformation = new ArrayList<HotelInformation>();
	private HashMap<String, CompleteOffer> currentOffers = new HashMap<String, CompleteOffer>();

	protected void setup(){

		Object[] args = getArguments();
        String name= (String) args[0];
        float budget = (float) args[1];
        int arrivalDay = (int) args[2];
        int nightsToStay = (int) args[3];
		
		this.id = name;
		this.budget_per_day = budget;
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
    	
    	//addBehaviour(new NUMBEROFCLIENTS_ExpectInform(this));
    	
    	//addBehaviour(new NUMBEROFCLIENTS_ExpectFailure(this));
    	
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
    	
    	System.out.println("Client generated - " + budget + " " + stay.getCheckIn() + "/" + stay.getCheckOut());
    }
	
	public float getRate()
	{
		float totalRateSum = 0;
		int contracts = 0;
		for (int i = this.stay.getCheckIn(), j = 0; i < this.stay.getCheckOut(); i++, j++) {
			Contract dayContract = staffInfo.get(i);
			int numberOfClients = 0;
			if (j >= clientsInfo.size()) {
				System.out.println("Number of clients missing for day " + i);
				// Information is missing, assuming that the hotel is hiding it
				numberOfClients = 6;
			} else {
				numberOfClients = clientsInfo.get(j);
			}
			float dayRate = 0;
			
			if (dayContract != null) {
				contracts++;
				
				// Cleanliness
				float cleanliness = (((float)dayContract.getRoom_service_staff()) / (float)numberOfClients) * 10f;
				cleanliness = cleanliness < 10f ? cleanliness : 10f;
				dayRate += 0.2f * cleanliness;
				
				// Kitchen
				float kitchen = 0;
				if (dayContract.getChef_3stars() > 0) {
					kitchen = 10f;
				} else if (dayContract.getChef_2stars() > 0) {
					kitchen = 6.66f;
				} else if (dayContract.getChef_1stars() > 0) {
					kitchen = 3.33f;
				}
				dayRate += 0.2 * kitchen;
				
				// Staff
				float staff = (((float)dayContract.getRecepcionist_experienced() * 3f + (float)dayContract.getRecepcionist_novice() * 2f) * 10f / (float)numberOfClients);
				staff = staff < 10f ? staff : 10f;
				dayRate += 0.2f * staff;
			} else {
				System.out.println("Client misses a contract for day " + i);
			}

			// Price
			dayRate += 0.4f * this.calculatePriceRate(selectedOffer.getPrice() / this.nightsToStay);
			
			totalRateSum += dayRate;
		}
		return contracts > 0 ? totalRateSum / contracts : -1;
	}
	
	public String getHotel()
	{
		return getHotelName(this.hotelAID);
	}
	
	public AID getHotelAID()
	{
		return hotelAID;
	}

	public int getcurrentday(){
		return currentDay; 
	}
		
	public boolean gethasbooked()
	{
		return hasbooked;
	}
	
	public boolean getstayathotel()
	{
		return stayathotel; 
	}
	
	public void sethasbooked(boolean b){
		
		this.hasbooked = b;
	}
	
	public void setstayathotel(boolean b){
		
		this.stayathotel = b;
	}
	
	public void ChangesOnDayChange() {
		System.out.println(getLocalName() + ": Day changed to " + currentDay);
		
    	if(hasbooked && currentDay >= stay.getCheckIn() && currentDay <= stay.getCheckOut()){
    		this.setstayathotel(true);
    	}
    	
    	// If the day is before the stay period ask all hotels for offers
    	if (!hasbooked && currentDay < stay.getCheckIn() - 1) {
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

    	} else if (!hasbooked && currentDay <= stay.getCheckIn()) {
    		addBehaviour(new BOOKAROOM_BookARoomBehaviour(this));
    	} else if (stayathotel && hotelAID != null && currentDay <= stay.getCheckOut()) {
    		// Ask for #clients and staff
    		addBehaviour(new NUMBEROFCLIENTS_NumberOfClientsBehaviour(this));
    	}
    	if (stayathotel && currentDay == stay.getCheckOut()) {
    		// Pay after waiting for two seconds to get the opinion of the last day and send the final rate
    		try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		// Pay
    		addBehaviour(new Payments_Request(this));
    		
            // Adds a behavior to evaluate a hotel
            addBehaviour(new SendRate(this));
            
            /*this.setstayathotel(false);
            this.sethasbooked(false);*/
    	}	
    }
	
	public Stay getStay() {
		return stay;
	}

	public void setStay(Stay stay) {
		this.stay = stay;
	}
	
	public void storeContract(HotelContract contract) {
		staffInfo.put(contract.getContract().getDay(), contract.getContract());
	}
	
	public void storeNumberOfClients(NumberOfClients numberOfClients) {
		clientsInfo.add(numberOfClients.getNum_clients());
	}
	
	public void viewOffer(CompleteOffer offer) {
		if (hasbooked) {
			return;
		}
		
		String hotelName = getHotelName(offer.getHotel());
		evaluateOffer(offer);
		currentOffers.put(hotelName, offer);
		
		// Recalculates the best offer
		CompleteOffer bestOffer = null;
		for (CompleteOffer currentOffer : currentOffers.values()) {
			if (bestOffer == null || currentOffer.getOfferRate() > bestOffer.getOfferRate()) {
				bestOffer = currentOffer;
				this.hotelAID = offer.getHotel();
			}
		}
		
		selectedOffer = bestOffer;
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
	
	// Gets the last rate received from Hotelmania for the specified hotel
	private float getHotelCurrentRate(String hotelName) {
		for (HotelInformation hotelInfo : this.hotelsInformation) {
			if (hotelInfo.getHotel().getHotel_name().equals(hotelName)) {
				return hotelInfo.getRating();
			}
		}
		
		return 5;
	}
	
	// Calculates the rate for certain price per day!
	private float calculatePriceRate(float price) {
		if (price > this.budget_per_day || price == 0) {
			return -1;
		}
		
		int result = (int)((this.budget_per_day - price) / 5);
		
		return result < 10 ? result : 10;
	}

	// Calculates the offer rate and set its value in the offer object
	private void evaluateOffer(CompleteOffer offer) {
		float rate = 0;
		
		// 50% from the hotel rate
		rate += 0.5 * getHotelCurrentRate(getHotelName(offer.getHotel()));
		
		// 50% from the price rate
		rate += 0.5 * calculatePriceRate(offer.getPrice() / this.nightsToStay);
		
		offer.setOfferRate(rate);
	}
}
