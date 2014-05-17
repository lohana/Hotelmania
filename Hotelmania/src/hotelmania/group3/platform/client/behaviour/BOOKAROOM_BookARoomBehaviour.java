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
import hotelmania.group3.platform.AgClient;
import hotelmania.ontology.NumberOfClientsQueryRef;

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
		AgClient agent = (AgClient)this.myAgent;
		
		try{
								
			// Inform for opinion
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.setProtocol(AgClient.BOOKAROOM_REQUEST);
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
	
	
}
