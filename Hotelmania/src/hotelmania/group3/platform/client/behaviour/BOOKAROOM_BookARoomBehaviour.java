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
import hotelmania.ontology.BookingOffer;
import hotelmania.ontology.Price;

@SuppressWarnings("serial")
public class BOOKAROOM_BookARoomBehaviour extends SimpleBehaviour {
	
	AID[] hotelmania = new AID[20];
	AID ag;
	boolean end = false;
	int current = 0;

	public BOOKAROOM_BookARoomBehaviour(Agent agent) {
		super(agent);
	}
	
	public void action(){
		AgClient3 agent = (AgClient3)this.myAgent;
		
		try{
								
			// Inform for opinion
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.setProtocol(AgClient3.BOOKAROOM_REQUEST);
			msg.addReceiver(agent.getHotelAID());
			msg.setLanguage(agent.codec.getName());
			msg.setOntology(agent.ontology.getName());
			
			BookRoom br = new BookRoom();
			CompleteOffer offer = agent.getSelectedOffer();
			br.setStay(agent.getStay());
			BookingOffer bo = new BookingOffer();
			Price price = new Price();
			price.setPrice(offer.getPrice());
			bo.setRoomPrice(price);
			br.setBookingOffer(bo);
			
			// Wrap the message with action
			Action agAction = new Action(agent.getHotelAID(),  br);
			//try{
				// The ContentManager transforms the java objects into strings
				//myAgent.getContentManager().fillContent(msg, agAction);
				msg.setContentObject(agAction);
				//agent.getContentManager().fillContent(msg, agAction);
				agent.send(msg);
				System.out.println(agent.getLocalName() + ": REQUEST BOOK a ROOM to" + agent.getHotel());
			//}
			/*catch (CodecException ce){
				ce.printStackTrace();
			}
			catch (OntologyException oe){
				oe.printStackTrace();
			}*/
				
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
