/**
 * Wait for requests of offers
 * @author Eleonora Adova, EMSE
 * @version $Date: 2014/05/17 22:36:23 $ $Revision: 1.0 $
 * 
 **/

package hotelmania.group3.hotel.behaviour;

import hotelmania.group3.hotel.AgHotel3;
import hotelmania.ontology.BookingOffer;
import hotelmania.ontology.Price;
import hotelmania.ontology.Stay;
import hotelmania.ontology.StayQueryRef;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class ReceiveOfferRequests extends CyclicBehaviour {
    
	public ReceiveOfferRequests(Agent agent) {
		super(agent);
	}
			
	public void action() {
		AgHotel3 agent = (AgHotel3)this.myAgent;

		ACLMessage msg = agent.receive(MessageTemplate.and(MessageTemplate.MatchLanguage(agent.codec.getName()), 
				MessageTemplate.and(MessageTemplate.MatchOntology(agent.ontology.getName()),
						MessageTemplate.MatchProtocol(AgHotel3.BOOKING_OFFER))));
		
		if(msg!=null){
			try{
				ContentElement ce = null;
				int AclMessage = msg.getPerformative();
				ACLMessage reply = msg.createReply();
				reply.setProtocol(AgHotel3.BOOKING_OFFER);
				
				if (AclMessage == ACLMessage.QUERY_REF){

					ce = agent.getContentManager().extractContent(msg);

					if (ce instanceof StayQueryRef){
						StayQueryRef stayQueryRef = (StayQueryRef) ce;  
						Stay stay = stayQueryRef.getStay();
						
						if (stay.getCheckIn() < agent.currentDay) {
							reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
							System.out.println(myAgent.getLocalName()+ ": Booking offer for past day");
						} else if (stay.getCheckIn() >= stay.getCheckOut()) {
							reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
							System.out.println(myAgent.getLocalName()+ ": Booking offer checkin day >= checkout day");
						} else {
							BookingOffer offer = new BookingOffer();
							float price = agent.strategy.getCurrentPrice();
							int nightsStayed = stay.getCheckOut() - stay.getCheckIn();
							Price totalPrice = new Price();
							totalPrice.setAmount(nightsStayed * price);
							offer.setRoomPrice(totalPrice);
							agent.makeOffer((msg.getSender()).getLocalName(), offer);
							reply.setPerformative(ACLMessage.INFORM);
							reply.setOntology(agent.ontology.getName());
							reply.setLanguage(agent.codec.getName());
							agent.getContentManager().fillContent(reply, offer);
							System.out.println(myAgent.getLocalName()+": received BookingOffer Request from cliet "+(msg.getSender()).getLocalName());
							System.out.println(myAgent.getLocalName()+ ": answer sent ->INFORM");
						}
					}
				} 
				else{
					reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
					System.out.println(myAgent.getLocalName()+ ": Offer request NOT UNDERSTOOD expected QUERY_REF does not match");
				}
				
				myAgent.send(reply);
			}
			catch (CodecException e){
				e.printStackTrace();
			}
			catch (OntologyException oe){
				oe.printStackTrace();
			}
		}
		else
		{
			// If no message arrives
			block();
		}
	}

}
