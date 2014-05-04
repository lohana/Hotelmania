/**
 * This action is of the Simulator agent for subscribing other agents for day change event.
 * @author Eleonora Adova, EMSE
 * @version $Date: 2014/05/04 00:28 $ $Revision: 1.0 $
 **/

package hotelmania.group3.platform.simulator.behaviour;

import hotelmania.group3.platform.AgSimulator3;
import hotelmania.ontology.SubscribeToDayEvent;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class SubscribeAgents extends CyclicBehaviour {

	public SubscribeAgents(Agent agent) {
		super(agent);
	}
	
	public void action()
	{
		AgSimulator3 agent = (AgSimulator3)this.myAgent;
		
		ACLMessage msg = agent.receive(MessageTemplate.and(MessageTemplate.MatchLanguage(agent.codec.getName()), 
				MessageTemplate.MatchOntology(agent.ontology.getName())) );
		
		if(msg!=null){
			try{
				ContentElement ce = null;
				int AclMessage = msg.getPerformative();
				ACLMessage reply = msg.createReply();
				
				if (!MessageTemplate.MatchProtocol(AgSimulator3.TIMECHANGE_SERVICE).match(msg)) {
					reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
					System.out.println(myAgent.getLocalName()+ ": answer sent -> NOT_UNDERSTOOD");
				}
				else if (AclMessage == ACLMessage.SUBSCRIBE){
					
					ce = agent.getContentManager().extractContent(msg);

					if (ce instanceof Action){
						Action agAction = (Action) ce;
						Concept conc = agAction.getAction();
						AID receiverAgent = msg.getSender();
						String receiver = receiverAgent.getLocalName();
						
						if (conc instanceof SubscribeToDayEvent){
							System.out.println(myAgent.getLocalName()+": SUSCRIBE DayEvent from " + receiver);    
							
							if (!agent.isRegistered(receiverAgent)){
								agent.registerAgent(receiverAgent);
								reply.setPerformative(ACLMessage.AGREE);
								System.out.println(myAgent.getLocalName()+ ": answer sent to " + receiver + " -> AGREE");
							}		 
							else {
								reply.setPerformative(ACLMessage.REFUSE);
								System.out.println(myAgent.getLocalName()+ ": answer sent to " + receiver + " -> REFUSE");	
							}
						} 
						else {
							reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
							System.out.println(myAgent.getLocalName()+ ": answer sent -> NOT_UNDERSTOOD");
						}
					}
				} 
				else {
					reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
					System.out.println(myAgent.getLocalName()+ ": answer sent -> NOT_UNDERSTOOD");
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
