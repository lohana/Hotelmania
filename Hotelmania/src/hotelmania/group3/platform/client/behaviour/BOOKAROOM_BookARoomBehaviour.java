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
import jade.content.lang.Codec.*;
import jade.content.onto.*;
import jade.content.onto.basic.*;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import hotelmania.group3.ontology.*;
import hotelmania.group3.platform.AgClient3;
import hotelmania.ontology.BookRoom;
import hotelmania.ontology.BookingOffer;
import hotelmania.ontology.NumberOfClientsQueryRef;
import hotelmania.ontology.Price;
import hotelmania.ontology.Stay;

@SuppressWarnings("serial")
public class BOOKAROOM_BookARoomBehaviour extends CyclicBehaviour {
	
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
			//FIX VALUES
			Stay stayTest = new Stay();
			stayTest.setCheckIn(3);
			stayTest.setCheckOut(5);
			BookingOffer bookingOfferTest = new BookingOffer();
			Price priceTest = new Price();
			priceTest.setPrice(20.30f);
			bookingOfferTest.setRoomPrice(priceTest);
			//////////
			
			br.setStay(stayTest);
			br.setBookingOffer(bookingOfferTest);
			
			// Wrap the message with action
			Action agAction = new Action(agent.getHotelAID(),  br);
			try{
				// The ContentManager transforms the java objects into strings
				//myAgent.getContentManager().fillContent(msg, agAction);
				agent.getContentManager().fillContent(msg, agAction);
				agent.send(msg);
				System.out.println(agent.getLocalName() + ": REQUEST BOOK a ROOM to" + agent.getHotel());
			}
			catch (CodecException ce){
				ce.printStackTrace();
			}
			catch (OntologyException oe){
				oe.printStackTrace();
			}
				
		}catch (Exception e){
			e.printStackTrace();
		}
	} 
	
}
