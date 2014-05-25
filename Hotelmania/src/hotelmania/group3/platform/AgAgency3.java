
package hotelmania.group3.platform;

import java.util.ArrayList;

import jade.domain.FIPAAgentManagement.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.content.lang.Codec;
import jade.content.lang.sl.*;
import jade.content.onto.*;
import hotelmania.group3.platform.agency.behaviuor.*;
import hotelmania.ontology.*;


@SuppressWarnings("serial")
public class AgAgency3 extends DayDependentAgent{
	
	public Codec codec = new SLCodec();
    public Ontology ontology = SharedAgentsOntology.getInstance();
    public static final String SIGN_CONTRACT = "SignContract";
    //Contracts signing in Agency during execution
    public ArrayList<SignContract> signedContracts = new ArrayList<SignContract>();
    public ChargeAccount cta = new ChargeAccount();
    
    
    protected void setup() {
    	System.out.println(getLocalName() + ": Agency3 has entered into the system");
    	getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);
        
        this.signedContracts.add(0,null); 
        this.cta =null;
        
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
			Thread.sleep(5000);
			
        }catch (FIPAException | InterruptedException e){
			e.printStackTrace();
		}
        
        // Adds a behavior to answer the sign contract requests
        // addBehaviour(new SIGNCONTRACT_SignContractBehaviour(this));
        
        // Adds behavior for day communication
    	addDayBehaviour();
    	
    	// EndSimulation Behaviors 
    	addBehaviour (new SubscribeForEndSimulation(this));
    	addBehaviour (new SubscribeFrEndSimulation_ExpectforMessages(this));
    }
    
    public void ChangesOnDayChange()
    {
    	System.out.println(getLocalName() + ": Day changed to " + currentDay);
    	addBehaviour(new   SIGNCONTRACT_SignContractBehaviour(this));
    }
}
