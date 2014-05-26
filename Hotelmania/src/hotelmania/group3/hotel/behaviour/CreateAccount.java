// Creating an account

package hotelmania.group3.hotel.behaviour;

import jade.core.Agent;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.DFService;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.content.lang.Codec.*;
import jade.content.onto.*;
import jade.content.onto.basic.*;
import hotelmania.group3.hotel.AgHotel3;
import hotelmania.ontology.*;

import java.util.Date;

@SuppressWarnings("serial")
public class CreateAccount extends SimpleBehaviour{
	
	boolean end = false;
	AID[] bank = new AID[20];
	AID ag;
	boolean ignore = false;
	int last = 0;
	int i, j;

	public CreateAccount(Agent agent) {
		super(agent);
	}
	
	public void action(){
		AgHotel3 agent = (AgHotel3)this.myAgent;
		
	     // From this moment, it will be searching bank for a minute		
     	final Date registerTime = new Date();
		
		// Creates the description for the type of agent to be searched
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(AgHotel3.CREATEACCOUNT_SERVICE);
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
						if (bank[j].compareTo(ag) == 0){
							ignore = true;
						}
					}
					// If we have not contacted yet with this bank
					if (!ignore){
						bank[last++] = ag;
						
						// Asks for registration
						ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
						msg.setProtocol(AgHotel3.CREATEACCOUNT_SERVICE);
						msg.addReceiver(ag);
						msg.setLanguage(agent.codec.getName());
						msg.setOntology(agent.ontology.getName());
						CreateAccountRequest rr = new CreateAccountRequest();
						//Hotel Information for the request
						Hotel hotel = new Hotel();
						hotel.setHotelAgent(agent.getAID());
						hotel.setHotel_name(AgHotel3.HOTEL_NAME);
						//Add hotel information to the registration request
						rr.setHotel(hotel);
						
						// As it is an action and the encoding language the SL, it must be wrapped
						// into an Action
						Action agAction = new Action(ag,rr);
						try{
							// The ContentManager transforms the java objects into strings
							agent.getContentManager().fillContent(msg, agAction);
							agent.send(msg);
							System.out.println(agent.getLocalName()+": CREATE ACCOUNT REQUEST SEND");
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
			} else {
				// If no new BANK has been found, it waits 1 second
				Thread.sleep(1000);
			}	
		}catch (Exception e){
			e.printStackTrace();
		}
	} 
	
	public boolean done (){
     	return end;
    }
}
