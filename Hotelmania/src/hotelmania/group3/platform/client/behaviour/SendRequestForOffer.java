/**
 * Send request for offer to a hotel
 * @author Eleonora Adova, EMSE
 * @version $Date: 2014/05/17 22:36:23 $ $Revision: 1.0 $
 **/

package hotelmania.group3.platform.client.behaviour;

import hotelmania.group3.platform.AgClient3;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class SendRequestForOffer extends SimpleBehaviour {
	AID hotel;
	boolean end = false;

	public SendRequestForOffer(Agent agent, AID hotel) {
		super(agent);
		this.hotel = hotel;
	}
	
	public void action(){
		AgClient3 agent = (AgClient3)this.myAgent;
	
		try{
			ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
			msg.setProtocol(AgClient3.BOOKING_OFFER);
			msg.addReceiver(hotel);
			msg.setLanguage(agent.codec.getName());
			msg.setOntology(agent.ontology.getName());
			
			try{
				agent.getContentManager().fillContent(msg, agent.getStay());
				agent.send(msg);
				System.out.println(agent.getLocalName() + ": QUERY_REF BookingOffer " + agent.getHotel());
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
	
	public boolean done (){
     	return end;
    }
}
