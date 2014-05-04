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
import hotelmania.group3.platform.hotelmania.behaviuor.ReceiveEvaluation;
import hotelmania.group3.platform.hotelmania.behaviuor.RegistrationBehaviour;
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
		
		// Adds a behavior to receive evaluation from clients
		addBehaviour(new ReceiveEvaluation(this));
    }
	
	public void addOpinion(String clientId, String hotel, int rate)
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
		return count == 0 ? 0 : sum / count;
	}
}
