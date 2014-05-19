/**
 * Receives offers from hotels
 * @author Eleonora Adova, EMSE
 * @version $Date: 2014/05/17 22:36:23 $ $Revision: 1.0 $
 **/

package hotelmania.group3.platform.client.behaviour;

import hotelmania.group3.platform.AgClient3;
import hotelmania.group3.platform.CompleteOffer;
import hotelmania.ontology.BookingOffer;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class ReceiveOffers extends CyclicBehaviour {

	public ReceiveOffers(Agent agent)
	{
		super(agent);
	}
	
	public void action()
	{
		AgClient3 agent = (AgClient3)this.myAgent;
		
		ACLMessage msg = agent.receive(MessageTemplate.and(
									   MessageTemplate.MatchLanguage(agent.codec.getName()), 
									   MessageTemplate.and(MessageTemplate.MatchProtocol(AgClient3.BOOKING_OFFER),
									   MessageTemplate.MatchOntology(agent.ontology.getName()))));
		
		if(msg!=null)
		{
			try
			{
				ContentElement content = null;
				int message = msg.getPerformative();
				
				if (message == ACLMessage.INFORM) 
				{
					content = agent.getContentManager().extractContent(msg);
					if (content instanceof BookingOffer)
					{
						AID sender = msg.getSender();
						BookingOffer n = (BookingOffer)content;
						CompleteOffer offer = new CompleteOffer();
						offer.setHotel(sender);
						offer.setPrice(n.getRoomPrice().getAmount());
						agent.viewOffer(offer);
						System.out.println(agent.getLocalName() + ": received BookingOffer from " + sender.getLocalName() + " price: " + offer.getPrice());
					}
				}
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