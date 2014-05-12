package hotelmania.group3.platform;

import jade.domain.FIPAAgentManagement.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.util.leap.HashMap;
import jade.util.leap.Map;
import jade.content.lang.Codec;
import jade.content.lang.sl.*;
import jade.content.onto.*;
import jade.core.Agent;
import hotelmania.ontology.*;


@SuppressWarnings("serial")
public class AgBank3 extends Agent{
	
	public Codec codec = new SLCodec();
    public Ontology ontology = SharedAgentsOntology.getInstance();
    public static final String CREATEACCOUNT_SERVICE = "CreateAccount";

    private Map accounts = new HashMap();
    
    protected void setup() {
    	System.out.println(getLocalName() + ": Bank3 has entered into the system");
    	getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);
        
        try{
			// Creates its own description
			DFAgentDescription dfd = new DFAgentDescription();
			// Add registration service
			ServiceDescription sd = new ServiceDescription();
			sd.setName(this.getName()); 
			sd.setType(CREATEACCOUNT_SERVICE);
			dfd.addServices(sd);
			
			// Registers its description in the DF
			DFService.register(this, dfd);
			System.out.println(getLocalName()+": Bank CREATE ACCOUNT SERVICE is registered in the DF");
			dfd = null;
			sd = null;
			doWait(10000);
			
        }catch (FIPAException e){
			e.printStackTrace();
		}
    }
    
    public float getStatusForHotel(String account)
    {
    	return Float.parseFloat((String)accounts.get(account));
    }
}
