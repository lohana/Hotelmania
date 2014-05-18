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
import hotelmania.group3.platform.AgClient3;
import hotelmania.ontology.NumberOfClientsQueryRef;

@SuppressWarnings("serial")
public class NUMBEROFCLIENTS_NumberOfClientsBehaviour extends SimpleBehaviour {
	
	AID[] hotelmania = new AID[20];
	AID ag;
	boolean end = false;
	int current = 0;

	public NUMBEROFCLIENTS_NumberOfClientsBehaviour(Agent agent) {
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
			
			// Wrap the message with action
			Action agAction = new Action(agent.getHotelAID(),  noc);
			try{
				// The ContentManager transforms the java objects into strings
				agent.getContentManager().fillContent(msg, agAction);
				agent.send(msg);
				System.out.println(agent.getLocalName() + ": Query for Number of Clients of " + agent.getHotelAID().getLocalName());
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
