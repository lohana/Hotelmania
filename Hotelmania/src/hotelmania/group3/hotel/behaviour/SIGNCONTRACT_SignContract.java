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
public class SIGNCONTRACT_SignContract extends SimpleBehaviour{
	
	boolean end = false;
	AID[] agency = new AID[20];
	AID ag;
	boolean ignore = false;
	int last = 0;
	int i, j;

	public SIGNCONTRACT_SignContract(Agent agent) {
		super(agent);
	}
	
	public void action(){
		AgHotel3 agent = (AgHotel3)this.myAgent;
		
	     // From this moment, it will be searching agency for a minute		
     	//final Date registerTime = new Date();
		
		// Creates the description for the type of agent to be searched
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(AgHotel3.SIGN_CONTRACT);
		dfd.addServices(sd);
		
		try{
			// If has been searching for estimations for more than a minute, it does not search any more
			/*if ((new Date()).getTime() - registerTime.getTime() >= 60000){
				end = true;
			}*/
			
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
						hotelRequesting.setHotel_name(AgHotel3.HOTEL_NAME);
						
						//Information for the request: Contract
						Contract contractRequesting = new Contract();
						contractRequesting.setChef_1stars(1);
						contractRequesting.setChef_2stars(1);
						contractRequesting.setChef_3stars(1);
						contractRequesting.setRoom_service_staff(2);
						contractRequesting.setRecepcionist_novice(0);
						contractRequesting.setRecepcionist_experienced(2);
						
						//Information about the contract day 
						int contractDay = agent.currentDay+1;
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
				agent.doWait(5000);
			} else {
				// If no new Agency has been found, it waits 5 seconds
				agent.doWait(5000);
			}	
		}catch (Exception e){
			e.printStackTrace();
		}
	} 
	
	public boolean done (){
     	return end;
    }
}
