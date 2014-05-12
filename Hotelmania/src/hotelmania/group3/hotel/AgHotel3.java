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
 **/

package hotelmania.group3.hotel;

import jade.content.lang.Codec;
import jade.content.lang.sl.*;
import jade.content.onto.*;
import hotelmania.group3.hotel.behaviour.*;
import hotelmania.group3.hotel.behaviour.CreateAccount;
import hotelmania.group3.platform.DayDependentAgent;
import hotelmania.ontology.*;

@SuppressWarnings("serial")
public class AgHotel3 extends DayDependentAgent {
	
	// Code for the SL language used and instance of the ontology
	// SharedAgentsOntology that we have created
	public Codec codec = new SLCodec();
	public Ontology ontology = SharedAgentsOntology.getInstance();

	//Sprint 1
	public final static String REGISTRATION_SERVICE = "Registration";
	public final static String HOTEL_NAME = "Hotel3";
	//Sprint 2
	public static final String SIGN_CONTRACT = "SignContract";
	//Sprint 3
	public static final String CREATEACCOUNT_SERVICE = "CreateAccount";
	
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
    	//addBehaviour(new  SIGNCONTRACT_SignContract(this));
    	
    	// Adds a behavior to process the ACEPTATION answer to a sign contract request
     	addBehaviour(new SIGNCONTRACT_ExpectAcceptation(this));

     	// Adds a behavior to process the REJECTION answer to a sign contract request
    	addBehaviour(new SIGNCONTRACT_ExpectRejection(this));
    	
    	// Adds a behavior to process the NOT_UNDERSTOOD answer to a sign contract request
    	addBehaviour(new  SIGNCONTRACT_ExpectNotUnderstood(this));
    	
    	// Adds behavior for day communication
    	addDayBehaviour();
    	
    	addBehaviour(new CreateAccount(this));
    	
    	addBehaviour(new ExpectAccount(this));
	}
	
	public void ChangesOnDayChange()
    {
    	System.out.println(getLocalName() + ": Day changed to " + currentDay);
    	addBehaviour(new  SIGNCONTRACT_SignContract(this));
    }
}
