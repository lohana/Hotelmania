// Processes the ACEPTATION answer to an registration request

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

@SuppressWarnings("serial")
public class SignContract_SignContract extends SimpleBehaviour{
	
	boolean end = false;
	AID[] agency = new AID[20];
	AID ag;
	boolean ignore = false;
	int last = 0;
	int i, j;

	public SignContract_SignContract(Agent agent) {
		super(agent);
	}
	
	public void action(){
		AgHotel3 agent = (AgHotel3)this.myAgent;
		if (agent.id_account != 0){		
		
			// Creates the description for the type of agent to be searched
			DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType(AgHotel3.SIGN_CONTRACT);
			dfd.addServices(sd);
			
			try{
				
				// It finds agents of the required type
				DFAgentDescription[] res = new DFAgentDescription[20];
				res = DFService.search(myAgent, dfd);
				
				// Gets the first occurrence, if there was success
				if (res.length > 0){
					for (i=0; i < res.length; i++){
						ag = (AID)res[i].getName();
	
						for (j=0; j<last; j++){
							if (agency[j].compareTo(ag) == 0){
								ignore = true;
							}
						}
						// If we have not contacted yet with this agency
						if (!ignore){
							agency[last++] = ag;
							
							// Asks for registration
							ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
							msg.setProtocol(AgHotel3.SIGN_CONTRACT);
							msg.addReceiver(ag);
							msg.setLanguage(agent.codec.getName());
							msg.setOntology(agent.ontology.getName());
													
							SignContract sc = new SignContract();
							
							//Information for the request: Hotel
							Hotel hotelRequesting = new Hotel();
							hotelRequesting.setHotelAgent(agent.getAID());
							hotelRequesting.setHotel_name(AgHotel3.HOTEL_NAME);
							
							//Information for the request: Contract
							Contract contractRequesting = agent.strategy.getNextContract();
							
							//Information about the contract day 
							int contractDay = agent.currentDay + 1;
							contractRequesting.setDay(contractDay);
								
							//Add hotel information to the registration request
							sc.setHotel(hotelRequesting);
							sc.setContract(contractRequesting);
							
							// As it is an action and the encoding language the SL, it must be wrapped
							// into an Action
							Action agAction = new Action(ag,sc);
							try{
								// The ContentManager transforms the java objects into strings
								agent.getContentManager().fillContent(msg, agAction);
								
								agent.send(msg);
								end = true;
								System.out.println(agent.getLocalName()+": SIGN CONTRACT REQUEST for " + contractDay +" day has been SEND");
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
					Thread.sleep(1000);
				} else {
					// If no new Agency has been found, it waits 1 second
					Thread.sleep(1000);
				}	
			}catch (Exception e){
				e.printStackTrace();
			}
		} else {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	} 
	
	public boolean done (){
     	return end;
    }
}
