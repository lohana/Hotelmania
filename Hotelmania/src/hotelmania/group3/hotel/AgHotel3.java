/*****************************************************************
Agent Client demanding a painting service: AgClient.java

 *****************************************************************/

package hotelmania.group3.hotel;

import jade.core.Agent;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.DFService;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.content.lang.Codec;
import jade.content.lang.Codec.*;
import jade.content.lang.sl.*;
import jade.content.onto.*;
import jade.content.onto.basic.*;
import hotelmania.ontology.*;

import java.util.Date;


//import searchPainter.paintServOntology.PaintServOntology;


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


@SuppressWarnings("serial")
public class AgHotel3 extends Agent {
	// Code for the SL language used and instance of the ontology
	// SharedAgentsOntology that we have created
	private Codec codec = new SLCodec();
	private Ontology ontology = SharedAgentsOntology.getInstance();

	public final static String REGISTRATION_SERVICE = "Registration";
	public final static String HOTEL_NAME = "Hotel3";
	
	protected void setup(){
		
		System.out.println(getLocalName()+": HOTEL HAS ENTERED");
		
//      Register of the codec and the ontology to be used in the ContentManager
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);
        
     // From this moment, it will be searching hotelmania for a minute		
     	final Date registerTime = new Date();
     	
     	
     	
     	//Adds a behavior to search a hotelmania agent
		//IF it is found, asks it a registration request
		//ELSE, waits 5 seconds and tries again
     	addBehaviour(new SimpleBehaviour(this){
     		boolean end = false;
			AID[] hotelmania = new AID[20];
			AID ag;
			boolean ignore = false;
			int last = 0;
			int i, j;
			
			public void action(){
				// Creates the description for the type of agent to be searched
				DFAgentDescription dfd = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType(REGISTRATION_SERVICE);
				dfd.addServices(sd);
				
				try{
					// If has been searching for estimations for more than a minute, it does not search any more
					if ((new Date()).getTime() - registerTime.getTime() >= 60000){
						end = true;
					}
					
					// It finds agents of the required type
					DFAgentDescription[] res = new DFAgentDescription[20];
					res = DFService.search(myAgent, dfd);
					
					// Gets the first occurrence, if there was success
					if (res.length > 0){
						for (i=0; i < res.length; i++){
							ag = (AID)res[i].getName();

							for (j=0; j<last; j++){
								if (hotelmania[j].compareTo(ag) == 0){
									ignore = true;
								}
							}
							// If we have not contacted yet with this hotelmania
							if (!ignore){
								hotelmania[last++] = ag;
								
								// Asks for registration
								ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
								msg.setProtocol(REGISTRATION_SERVICE);
								msg.addReceiver(ag);
								msg.setLanguage(codec.getName());
								msg.setOntology(ontology.getName());
								RegistrationRequest rr = new RegistrationRequest();
								//Hotel Information for the request
								Hotel hotel = new Hotel();
								hotel.setHotel_name(HOTEL_NAME);
								//Add hotel information to the registration request
								rr.setHotel(hotel);
								
								// As it is an action and the encoding language the SL, it must be wrapped
								// into an Action
								Action agAction = new Action(ag,rr);
								try{
									// The ContentManager transforms the java objects into strings
									getContentManager().fillContent(msg, agAction);
									send(msg);
									System.out.println(getLocalName()+": REGISTATION REQUEST SEND");
								}
								catch (CodecException ce){
									ce.printStackTrace();
								}
								catch (OntologyException oe){
									oe.printStackTrace();
								}
							}
							ignore = false;
						}
						doWait(5000);
					} else {
						// If no new HOTELMANIA has been found, it waits 5 seconds
						doWait(5000);
					}
					
				}catch (Exception e){
					e.printStackTrace();
				}
				
			} 
			
			public boolean done (){
		     	return end;
		    }
			
     	});
     	
		
     	// Adds a behavior to process the ACEPTATION answer to an registration request
     	addBehaviour(new CyclicBehaviour(this)
    	{
    		private static final long serialVersionUID =1L;
    		public void action()
    		{
    			// Waits for estimation acceptations
    			ACLMessage msg = receive(MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL));
    			if (msg != null)
    			{
    				// If an acceptation arrives...
    				System.out.println(myAgent.getLocalName()+": received request ACCEPTATION from "+(msg.getSender()).getLocalName());
    			}
    			else
    			{
    				// If no message arrives
    				block();
    			}

    		}

    	});

     	// Adds a behavior to process the ACEPTATION answer to an registration request
    	addBehaviour(new CyclicBehaviour(this)
    	{
    		private static final long serialVersionUID =1L;
    		public void action()
    		{
    			// Waits for estimation rejections
    			ACLMessage msg = receive(MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL));
    			if (msg != null)
    			{
    				// If a rejection arrives...
    				System.out.println(myAgent.getLocalName()+": received request REJECTION from "+(msg.getSender()).getLocalName());
    			}
    			else
    			{
    				// If no message arrives
    				block();
    			}

    		}

    	});

    	addBehaviour(new CyclicBehaviour(this)
    	{
    		private static final long serialVersionUID =1L;
    		public void action()
    		{
    			// Waits for estimations not understood
    			ACLMessage msg = receive(MessageTemplate.MatchPerformative(ACLMessage.NOT_UNDERSTOOD));
    			if (msg != null)
    			{
    				// If a not understood message arrives...
    				System.out.println(myAgent.getLocalName()+": received NOT_UNDERSTOOD response from "+(msg.getSender()).getLocalName());
    			}
    			else
    			{
    				// If no message arrives
    				block();
    			}

    		}

    	});
		
	}

	

}
