/**
 * This action is of the Simulator agent for subscribing other agents for day change event.
 * @author Jorge Aviles, EMSE
 * @version $Date: 2014/05/18 00:28 $ $Revision: 1.0 $
 **/

package hotelmania.group3.platform.simulator.behaviour;

import hotelmania.group3.platform.AgSimulator3;
import hotelmania.ontology.EndSimulation;
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
public class SubscribeEndSimulation extends CyclicBehaviour {

	public SubscribeEndSimulation(Agent agent) {
		super(agent);
	}
	
	public void action()
	{
		AgSimulator3 agent = (AgSimulator3)this.myAgent;
		
		ACLMessage msg = agent.receive(MessageTemplate.and(MessageTemplate.MatchLanguage(agent.codec.getName()), 
				MessageTemplate.and(MessageTemplate.MatchOntology(agent.ontology.getName()),
						MessageTemplate.MatchProtocol(AgSimulator3.END_SIMULATION))));
		
		if(msg!=null){
			try{
				ContentElement ce = null;
				int AclMessage = msg.getPerformative();
				ACLMessage reply = msg.createReply();
				
				if (AclMessage == ACLMessage.SUBSCRIBE){
					
					ce = agent.getContentManager().extractContent(msg);

					if (ce instanceof Action){
						Action agAction = (Action) ce;
						Concept conc = agAction.getAction();
						AID receiverAgent = msg.getSender();
						String receiver = receiverAgent.getLocalName();
						
						if (conc instanceof EndSimulation){
							System.out.println(myAgent.getLocalName()+": SUSCRIBE END_SIMULATION from " + receiver);    
							
							if (!agent.isRegistered_EndSimulation(receiverAgent)){
								agent.registerAgent_EndSimulation(receiverAgent);
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
					System.out.println(myAgent.getLocalName()+ ": answer sent -> NOT_UNDERSTOOD ES");
				}
				
				reply.setProtocol(AgSimulator3.END_SIMULATION);
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
			block();
		}
	}
}
