/**
 * Send request for offer to a hotel
 * @author Eleonora Adova, EMSE
 * @version $Date: 2014/05/17 22:36:23 $ $Revision: 1.0 $
 **/
package hotelmania.group3.platform.client.behaviour;

import hotelmania.group3.platform.AgClient3;
import hotelmania.ontology.NumberOfClientsQueryRef;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class SendRequestForOffer extends SimpleBehaviour {
	AID[] hotelmania = new AID[20];
	AID ag;
	boolean end = false;
	int current = 0;

	public SendRequestForOffer(Agent agent) {
		super(agent);
	}
	
	public void action(){
		AgClient3 agent = (AgClient3)this.myAgent;
		
		// The client will search for hotelmania 12 times for total of 60 seconds
;
		
		try{
								
			// Inform for opinion
			ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
			msg.setProtocol(AgClient3.NUMBEROFCLIENTS_QUERY);
			msg.addReceiver(agent.getHotelAID());
			msg.setLanguage(agent.codec.getName());
			msg.setOntology(agent.ontology.getName());
			
			NumberOfClientsQueryRef noc = new NumberOfClientsQueryRef();
			//noc.setHotel_name(agent.getHotel());
			
			// Wrap the message with action
			Action agAction = new Action(agent.getHotelAID(),  noc);
			try{
				// The ContentManager transforms the java objects into strings
				//myAgent.getContentManager().fillContent(msg, agAction);
				agent.getContentManager().fillContent(msg, agAction);
				agent.send(msg);
				System.out.println(agent.getLocalName() + ": Query for Number of Clients of " + agent.getHotel());
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
