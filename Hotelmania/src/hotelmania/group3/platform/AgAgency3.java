package hotelmania.group3.platform;

import java.util.ArrayList;

import jade.domain.FIPAAgentManagement.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.content.lang.Codec;
import jade.content.lang.sl.*;
import jade.content.onto.*;
import hotelmania.group3.commonbehaviour.ReceiveDayNotification;
import hotelmania.group3.commonbehaviour.ReceiveSubscriptionAgree;
import hotelmania.group3.commonbehaviour.ReceiveSubscriptionRefuse;
import hotelmania.group3.commonbehaviour.SubscribeForDayNotification;
import hotelmania.group3.platform.agency.behaviuor.SIGNCONTRACT_SignContractBehaviour;
import hotelmania.ontology.*;


@SuppressWarnings("serial")
public class AgAgency3 extends DayDependentAgent{
	
	public Codec codec = new SLCodec();
    public Ontology ontology = SharedAgentsOntology.getInstance();
    public static final String SIGN_CONTRACT = "SignContract";
    //Contracts signing in Agency during execution
    public ArrayList<SignContract> signedContracts = new ArrayList<SignContract>();
    
    protected void setup() {
    	System.out.println(getLocalName() + ": Agency3 has entered into the system");
    	getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);	
        
        try{
			// Creates its own description
			DFAgentDescription dfd = new DFAgentDescription();
			// Add registration service
			ServiceDescription sd = new ServiceDescription();
			sd.setName(this.getName()); 
			sd.setType(SIGN_CONTRACT);
			dfd.addServices(sd);
			
			// Registers its description in the DF
			DFService.register(this, dfd);
			System.out.println(getLocalName()+": AGENCY SIGN CONTRACT SERVICE is registered in the DF");
			dfd = null;
			sd = null;
			doWait(10000);
			
        }catch (FIPAException e){
			e.printStackTrace();
		}
        
        // Adds a behavior to answer the sign contract requests
     	addBehaviour(new SIGNCONTRACT_SignContractBehaviour(this));
        
     	// Adds a behavior to subscribe for day event
    	addBehaviour(new  SubscribeForDayNotification(this));
		
    	// Adds a behavior to process day notification
    	addBehaviour(new  ReceiveDayNotification(this));
    	
    	// Adds a behavior to process subscription answer receive
    	addBehaviour(new  ReceiveSubscriptionAgree(this));
    	
    	// Adds a behavior to process subscription answer receive
    	addBehaviour(new  ReceiveSubscriptionRefuse(this));
    }
    
    public void ChangesOnDayChange()
    {
    	System.out.println(getLocalName() + ": Day changed to " + currentDay);
    }
}
