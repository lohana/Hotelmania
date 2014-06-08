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
import hotelmania.group3.ontology.Ontology3;
import hotelmania.group3.platform.hotelmania.behaviuor.HotelsInformation_ExpectRequest;
import hotelmania.group3.platform.hotelmania.behaviuor.Rating_ReceiveEvaluation;
import hotelmania.group3.platform.hotelmania.behaviuor.Registration_ExpectRequest;
import hotelmania.group3.platform.hotelmania.behaviuor.SimulationEnd_SubscribeForSimulationEnd;
import hotelmania.group3.platform.hotelmania.behaviuor.SimulationEnd_ExpectSimulationEnd;
import hotelmania.ontology.*;

import java.util.ArrayList;
import java.util.Dictionary;

@SuppressWarnings("serial")
public class AgHotelmania3 extends Agent {
	
    public Codec codec = new SLCodec();
    public Ontology ontology = SharedAgentsOntology.getInstance();
    public Ontology innerOntology = Ontology3.getInstance();
    public static final String REGISTRATION_SERVICE = "Registration";
    public static final String EVALUATION_SERVICE = "Evaluation";
    public static final String HOTEL_INFORMATION = "QueryHotelmaniaInformation";
    //Hotels Registered in Hotelmania during execution 
    public ArrayList<Hotel> RegisteredHotels = new ArrayList<Hotel>();
    
    public Dictionary<String, ArrayList<Opinion>> hotelsOpinions;
	private ArrayList<Opinion> opinions = new ArrayList<Opinion>();
    
	protected void setup() {
		
		System.out.println(getLocalName() + ": Hotelmania has entered into the system");

        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);		
        getContentManager().registerOntology(innerOntology);		
        
		try{
			// Creates its own description
			DFAgentDescription dfd = new DFAgentDescription();
			
			// Add registration service
			ServiceDescription sd = new ServiceDescription();
			sd.setName(this.getName()); 
			sd.setType(REGISTRATION_SERVICE);
			dfd.addServices(sd);
			
			// Add evaluation service
			ServiceDescription es = new ServiceDescription();
			es.setName(this.getName()); 
			es.setType(EVALUATION_SERVICE);
			dfd.addServices(es);
			
			// Add evaluation service
			ServiceDescription hi = new ServiceDescription();
			hi.setName(this.getName()); 
			hi.setType(HOTEL_INFORMATION);
			dfd.addServices(hi);
			
			// Registers its description in the DF
			DFService.register(this, dfd);
			System.out.println(getLocalName()+": HOTELMANIA REGISTRATION SERVICE is registered in the DF");
			System.out.println(getLocalName()+": HOTELMANIA HOTEL INFORMATION SERVICE is registered in the DF");	
					
			dfd = null;
			sd = null;
			Thread.sleep(5000);
			
		} catch (FIPAException | InterruptedException e){
			e.printStackTrace();
		}
		
		// Adds a behavior to answer the estimation requests
		addBehaviour(new Registration_ExpectRequest(this));
		
		// Adds a behavior to receive evaluation from clients
		addBehaviour(new Rating_ReceiveEvaluation(this));	
		
		addBehaviour(new HotelsInformation_ExpectRequest(this));
		
    	// EndSimulation Behaviors 
    	addBehaviour (new SimulationEnd_SubscribeForSimulationEnd(this));
    	addBehaviour (new SimulationEnd_ExpectSimulationEnd(this));
    }
	
	public void addOpinion(String clientId, String hotel, float rate)
	{
		opinions.add(new Opinion(clientId, hotel, rate));
	}
	
	public int getOpinionForHotel(String hotel)
	{
		int n = opinions.size();
		int sum = 0;
		int count = 0;
		for (int i = 0; i < n; i++)
		{
			Opinion opinion = opinions.get(i);
			if (opinion.getHotel().equals(hotel))
			{
				sum += opinion.getRate();
				count++;
			}
		}
		return count == 0 ? 5 : sum / count;
	}
}
