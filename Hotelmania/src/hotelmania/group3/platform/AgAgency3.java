package hotelmania.group3.platform;

import java.util.ArrayList;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.content.lang.Codec;
import jade.content.lang.sl.*;
import jade.content.onto.*;
import hotelmania.group3.platform.agency.behaviuor.SIGNCONTRACT_SignContractBehaviour;
import hotelmania.ontology.*;


@SuppressWarnings("serial")
public class AgAgency3 extends Agent{
	
	public Codec codec = new SLCodec();
    public Ontology ontology = SharedAgentsOntology.getInstance();
    public static final String SIGN_CONTRACT = "SignContract";
    //Contracts signing in Agency during execution
    public int currentDay;
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
        
    }
    
    
}
