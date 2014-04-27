/**
 * This agent has the following functionality: 
 * <ul>
 * <li> It registers itself in the DF as HOTELMANIA
 * <li> Waits for requests of its Hotel Registration service
 * <li> If any registration request arrives, it register Hotel in their platform 
 * 		and send the answer to the Hotel
 * <li> Finally, it continues waiting for a new register requests.
 * </ul>
 * @author Lohana Lema Moreta, EMSE
 * @version $Date: 2014/04/19 19:28 $ $Revision: 1.0 $
 **/

package hotelmania.group3.platform;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.content.lang.Codec;
import jade.content.lang.sl.*;
import jade.content.onto.*;
import hotelmania.group3.platform.hotelmania.behaviuor.RegistrationBehaviour;
import hotelmania.ontology.*;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class AgHotelmania3 extends Agent {
	
    public Codec codec = new SLCodec();
    public Ontology ontology = SharedAgentsOntology.getInstance();
    public static final String REGISTRATION_SERVICE = "Registration";
	public ArrayList<Hotel> RegisteredHotels = new ArrayList<Hotel>(); 
    
	protected void setup() {
		System.out.println(getLocalName()+": Hotelmania has entered into the system");

        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);		
        
		try{
			// Creates its own description
			DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setName(this.getName()); 
			sd.setType(REGISTRATION_SERVICE);
			dfd.addServices(sd);
			
			// Registers its description in the DF
			DFService.register(this, dfd);
			System.out.println(getLocalName()+": HOTELMANIA REGISTRATION SERVICE is registered in the DF");
			dfd = null;
			sd = null;
			doWait(10000);
			
		} catch (FIPAException e){
			e.printStackTrace();
		}
		
		// Adds a behavior to answer the estimation requests
		addBehaviour(new RegistrationBehaviour(this));
    }
}
