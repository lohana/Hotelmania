
package hotelmania.group3.platform.client.behaviour;

import jade.core.Agent;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.DFService;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.content.lang.Codec.*;
import jade.content.onto.*;
import jade.content.onto.basic.*;
import hotelmania.group3.platform.AgBank3;
import hotelmania.group3.platform.AgClient3;
import hotelmania.ontology.*;

import java.util.Date;

@SuppressWarnings("serial")
public class Payments_Request extends SimpleBehaviour{

	boolean end = false;
	AID[] bank = new AID[20];
	AID ag;
	boolean ignore = false;
	int last = 0;
	int i, j;

	public Payments_Request(Agent agent) {
		super(agent);
	}

	public void action(){
		AgClient3 client = (AgClient3)myAgent;

		// From this moment, it will be searching bank for a minute		
		final Date registerTime = new Date();

		// Creates the description for the type of agent to be searched
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(AgBank3.PAYMENTS);
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
						msg.setProtocol(AgBank3.PAYMENTS);
						msg.addReceiver(ag);
						msg.setLanguage(client.codec.getName());
						msg.setOntology(client.ontology.getName());

						ChargeAccount ca = new ChargeAccount();
						
						float amount = client.getSelectedOffer().getPrice() * (client.getStay().getCheckOut() - client.getStay().getCheckIn());
						Hotel h = new Hotel();
						h.setHotel_name(client.getHotelName(client.getHotelAID()));
						h.setHotelAgent(client.getHotelAID());
 						ca.setHotel(h);
						ca.setAmount(amount);
						ca.setDay(1);



						// As it is an action and the encoding language the SL, it must be wrapped
						// into an Action
						Action agAction = new Action(ag,ca);
						try{
							// The ContentManager transforms the java objects into strings
							client.getContentManager().fillContent(msg, agAction);
							client.send(msg);
							end = true;
							System.out.println(client.getLocalName()+": PAYMENT REQUEST SEND");
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
