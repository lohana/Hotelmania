/**
 * Query to Hotel number of clients
 * @author Lohanna Lema, EMSE
 * @version $Date: 2014/05/12 $ $Revision: 1.0 $
 **/

package hotelmania.group3.platform.client.behaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.content.onto.basic.Action;
import hotelmania.group3.platform.AgClient3;
import hotelmania.group3.platform.CompleteOffer;
import hotelmania.ontology.BookRoom;
import hotelmania.ontology.Price;

@SuppressWarnings("serial")
public class Booking_BookARoom extends SimpleBehaviour {
	
	AID[] hotelmania = new AID[20];
	AID ag;
	boolean end = false;
	int current = 0;

	public Booking_BookARoom(Agent agent) {
		super(agent);
	}
	
	public void action(){
		AgClient3 agent = (AgClient3)this.myAgent;
		
		try{
								
			// Request  Book a room.
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.setProtocol(AgClient3.BOOKAROOM_REQUEST);
			//msg.addReceiver(agent.getHotelAID());
			msg.setLanguage(agent.codec.getName());
			msg.setOntology(agent.ontology.getName());
			
			BookRoom br = new BookRoom();
			CompleteOffer offer = agent.getSelectedOffer();
			if (offer != null)
			{
				br.setStay(agent.getStay());
				Price price = new Price();
				price.setAmount(offer.getPrice());
				br.setPrice(price);
				
				// Wrap the message with action
				AID hotelID = offer.getHotel();
				agent.hotelAID = hotelID;
				Action agAction = new Action(agent.hotelAID,  br);
				msg.addReceiver(agent.hotelAID);
				agent.getContentManager().fillContent(msg, agAction);
				
				agent.send(msg);
				System.out.println(agent.getLocalName() + ": REQUEST BOOK a ROOM to " + agent.getHotel());
			}
						
		}catch (Exception e){
			e.printStackTrace();
		}
		end = true;
	}

	@Override
	public boolean done() {
		return end;
	} 
	
}
