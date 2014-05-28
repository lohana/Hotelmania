/**
 * This is action of the Hotelmania agent for registering hotels.
 * @author Eleonora Adova, EMSE
 * @version $Date: 2014/04/27 12:28 $ $Revision: 1.0 $
 **/

package hotelmania.group3.platform.bank.behaviour;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.content.lang.Codec.*;
import jade.content.*;
import jade.content.onto.*;
import jade.content.onto.basic.*;
import hotelmania.group3.platform.AgBank3;
import hotelmania.ontology.*;

@SuppressWarnings("serial")
public class CreateAccountForHotel extends CyclicBehaviour {
    
	public CreateAccountForHotel(Agent agent) {
		super(agent);
	}
			
	public void action() {
		AgBank3 agent = (AgBank3)this.myAgent;
		
		// Waits for estimation requests
		ACLMessage msg = agent.receive(MessageTemplate.and(MessageTemplate.MatchLanguage(agent.codec.getName()), 
				MessageTemplate.and(MessageTemplate.MatchOntology(agent.ontology.getName()),
						MessageTemplate.MatchProtocol(AgBank3.CREATEACCOUNT_SERVICE))));
		
		if(msg!=null){
			try{
				ContentElement ce = null;
				int AclMessage = msg.getPerformative();
				ACLMessage reply = msg.createReply();
				reply.setProtocol(AgBank3.CREATEACCOUNT_SERVICE);
				
				if (AclMessage == ACLMessage.REQUEST){
					// If an REGISTRATION request arrives (type REQUEST)
					// it answers with the acceptance o deny
					
					// The ContentManager transforms the message content (string)
					// in java objects
					ce = agent.getContentManager().extractContent(msg);
					// We expect an action inside the message
					if (ce instanceof Action){
						Action agAction = (Action) ce;
						Concept conc = agAction.getAction();
						if (conc instanceof CreateAccountRequest){
							System.out.println(myAgent.getLocalName()+": received CREATE ACCOUNT REGISTRATION REQUEST from "+(msg.getSender()).getLocalName());    
																				
							CreateAccountRequest re = (CreateAccountRequest)conc;
							Hotel newHotel = re.getHotel();
							boolean repeated = false;
							boolean blank_name = false;
							boolean wrong_name = false;
							
							if (!agent.hotelsWithAccount.isEmpty()){
								for (int i = 0; i < agent.hotelsWithAccount.size(); i++){
									Hotel currentHotel = (Hotel)agent.hotelsWithAccount.get(i);
									if (currentHotel.getHotel_name().toLowerCase().compareTo(newHotel.getHotel_name().toLowerCase()) == 0)
										repeated = true;
								}
							}
							
							if (newHotel.getHotel_name().compareTo("")==0)
								blank_name = true;
							
							if (!newHotel.getHotel_name().toLowerCase().contains("hotel"))
								wrong_name = true;						
							
							if ( blank_name || wrong_name || repeated  ){
								reply.setPerformative(ACLMessage.FAILURE);
								System.out.println(myAgent.getLocalName()+ ": CreateAccount Request of "+ newHotel.getHotel_name() + " is DENIED. Failure sent");
							} 
							else {								
								int id = agent.createAccount(newHotel);
								reply.setPerformative(ACLMessage.INFORM);
								Account a = new Account();
								a.setHotel(newHotel);
								a.setId_account(id);
								AccountStatus as = new AccountStatus();
								as.setAccount(a);
								reply.setOntology(agent.ontology.getName());
								reply.setLanguage(agent.codec.getName());
								agent.getContentManager().fillContent(reply, as);
								System.out.println(myAgent.getLocalName()+ ": CreateAccount Request of "+ newHotel.getHotel_name() + " is ACCEPTED");
								System.out.println(myAgent.getLocalName()+ ": "+ newHotel.getHotel_name() + " is REGISTERED in Bank");	
							}
						} 
						else {
							reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
							System.out.println(myAgent.getLocalName()+ ": CreateAccount Request DOES NOT UNDERSTOOD");
						}
					}
				} 
				else{
					reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
					System.out.println(myAgent.getLocalName()+ ": CreateAccount request DOES NOT UNDERSTOOD");
				}
				
				reply.setProtocol(AgBank3.CREATEACCOUNT_SERVICE);
				myAgent.send(reply);
				System.out.println(myAgent.getLocalName()+": Answer Sent");
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
