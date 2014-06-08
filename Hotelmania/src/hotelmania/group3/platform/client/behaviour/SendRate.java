/**
 * Informs Hotelmania for the client's rate
 * @author Eleonora Adova, EMSE
 * @version $Date: 2014/04/27 20:28 $ $Revision: 1.0 $
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

@SuppressWarnings("serial")
public class SendRate extends SimpleBehaviour {
	
	AID[] hotelmania = new AID[20];
	AID ag;
	boolean end = false;
	int current = 0;

	public SendRate(Agent agent) {
		super(agent);
	}
	
	public void action(){
		AgClient3 agent = (AgClient3)this.myAgent;
		
		// The client will search for hotelmania 60 times for total of 60 seconds
	    int count = 0;
		
		// Creates the description for the type of agent to be searched
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(AgClient3.EVALUATION_SERVICE);
		dfd.addServices(sd);
		
		try{
			// If has been searching for estimations for more than a minute, it does not search any more
			if (count >= 12){
				end = true;
			}
			
			// Finds other agents
			DFAgentDescription[] res = new DFAgentDescription[32];
			res = DFService.search(myAgent, dfd);
			
			// Gets the first occurrence if there was success
			if (res.length > 0){
				
				for (int i = 0; i < res.length; i++){
					ag = (AID)res[i].getName();

					boolean skip = false;
					for (int j = 0; j < current; j++){
						if (hotelmania[j].compareTo(ag) == 0){
							skip = true;
						}
					}
					
					// If we have not contacted yet with this hotelmania
					if (!skip){
						hotelmania[current++] = ag;
						
						// Inform for opinion
						ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
						msg.setProtocol(AgClient3.EVALUATION_SERVICE);
						msg.addReceiver(ag);
						msg.setLanguage(agent.codec.getName());
						msg.setOntology(agent.innerOntology.getName());
						Evaluation e = new Evaluation();
						
						// Client information
						Client client = new Client();
						client.setClientId(agent.id);
						client.setHotel(agent.getHotel());
						client.setRate(agent.getRate());
						
						if (client.getRate() > 0) {
							//Add client information to the evaluation
							e.setClient(client);
							
							// Wrap the message with action
							Action agAction = new Action(ag, e);
							try{
								// The ContentManager transforms the java objects into strings
								agent.getContentManager().fillContent(msg, agAction);
								agent.send(msg);
								System.out.println(agent.getLocalName() + ": Evaluation sent with rate: " + agent.getRate());
							}
							catch (CodecException ce){
								ce.printStackTrace();
							}
							catch (OntologyException oe){
								oe.printStackTrace();
							}
						}
					}
				}
				count++;
				Thread.sleep(1000);
			} else {
				count++;
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
