/**
 * This agent launches all the agents that are inner for the platform.
 * @author Lohana Lema Moreta, EMSE
 * @version $Date: 2014/04/27 12:28 $ $Revision: 1.0 $
 **/

package hotelmania.group3.platform.agency.behaviuor;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.content.lang.Codec.*;
import jade.content.*;
import jade.content.onto.*;
import jade.content.onto.basic.*;
import hotelmania.group3.platform.AgAgency3;
import hotelmania.group3.platform.agency.behaviuor.SignContract_SignContract;
import hotelmania.ontology.*;

@SuppressWarnings("serial")
public class SignContract_SignContract extends CyclicBehaviour {
    
	public SignContract_SignContract(Agent agent) {
		super(agent);
	}
		
	public void action() {
		AgAgency3 agent = (AgAgency3)this.myAgent;
		
		// Waits for estimation requests
		ACLMessage msg = agent.receive(MessageTemplate.and(MessageTemplate.MatchLanguage(agent.codec.getName()), 
				MessageTemplate.and(MessageTemplate.MatchOntology(agent.ontology.getName()),
						MessageTemplate.MatchProtocol(AgAgency3.SIGN_CONTRACT))));
		
		if(msg!=null){
			try{
				ContentElement ce = null;
				int AclMessage = msg.getPerformative();
				ACLMessage reply = msg.createReply();
				reply.setProtocol(AgAgency3.SIGN_CONTRACT);
				
				ACLMessage reply2 = msg.createReply();
				reply2.setProtocol(AgAgency3.SIGN_CONTRACT);

				if (AclMessage == ACLMessage.REQUEST){
					// If an SIGN CONTRACT request arrives (type REQUEST)
					// it answers with the acceptance o deny
					
					// The ContentManager transforms the message content (string)
					// in java objects
					ce = agent.getContentManager().extractContent(msg);
					// We expect an action inside the message
					if (ce instanceof Action){
						Action agAction = (Action) ce;
						Concept conc = agAction.getAction();
						// If the action is SignContract...
						if (conc instanceof SignContract){
							   
																				
							SignContract sc = (SignContract)conc;
							Hotel requestedHotel = sc.getHotel();
							int requestedContractDay = sc.getContract().getDay();
							boolean hotel_day_contract_repeated = false;
							boolean correct_contract_day = false;
							
							System.out.println(myAgent.getLocalName()+": received SIGN CONTRACT REQUEST from "+(msg.getSender()).getLocalName() + "for a " + requestedContractDay + " day");
							
							//Validate if contract request day is the greater equal than the agency day
							if (requestedContractDay >= agent.currentDay){
								correct_contract_day = true;
							}
							//One contract per day per hotel
							if (!agent.signedContracts.isEmpty()){
								int contractedDay = 0;
								for (int i = 0; i < agent.signedContracts.size(); i++){
									if (i!=0){
										Hotel contractedHotel = (Hotel)agent.signedContracts.get(i).getHotel();
										contractedDay = agent.signedContracts.get(i).getContract().getDay();
										if ( (contractedHotel.getHotel_name().toLowerCase().compareTo(requestedHotel.getHotel_name().toLowerCase()) == 0) && (contractedDay==requestedContractDay)){
											hotel_day_contract_repeated = true;
										}
									}
								}
							}
												
							
							if (!correct_contract_day){
								reply.setPerformative(ACLMessage.REFUSE);
								System.out.println(myAgent.getLocalName()+ ": Sign Contract Request of "+ requestedHotel.getHotel_name() + " for a " + requestedContractDay + " day " + " is REFUSED");
								reply2.setPerformative(ACLMessage.FAILURE);
								System.out.println(myAgent.getLocalName()+ ": Sign Contract FAILURE to the "+ requestedHotel.getHotel_name() + " for a " + requestedContractDay + " day " + ". Reason: Day of requested contract it is wrong");
							}
							else {
								reply.setPerformative(ACLMessage.AGREE);
								System.out.println(myAgent.getLocalName()+ ": Sign Contract Request of "+ requestedHotel.getHotel_name() + " for a " + requestedContractDay + " day " + " is AGREED");
								
								if ( hotel_day_contract_repeated  ){
									reply2.setPerformative(ACLMessage.FAILURE);
									System.out.println(myAgent.getLocalName()+ ": Sign Contract FAILURE to the "+ requestedHotel.getHotel_name() + " for a " + requestedContractDay + " day " + ". Reason: Contract exits for this Hotel in this day");
								} else {
									//charge to account
									myAgent.addBehaviour(new ChargeAccount_RequestCharge(myAgent, sc));
									agent.signedContracts.add(sc);
									reply2.setPerformative(ACLMessage.INFORM);
									System.out.println(myAgent.getLocalName()+ ": "+ requestedHotel.getHotel_name() + " INFORM has hired Staff requested for " + requestedContractDay + " day");
								}
							}
						} 
						else {
							reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
							System.out.println(myAgent.getLocalName()+ ": Sign Contract Request DOES NOT UNDERSTOOD: Wrong Concept");
						}
					}
				} 
				else{
					reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
					System.out.println(myAgent.getLocalName()+ ": Sign Contract Request DOES NOT UNDERSTOOD: Request send it's wrong");
				}
				
				myAgent.send(reply);
				myAgent.send(reply2);
				System.out.println(myAgent.getLocalName()+": Answer about Sign Contract Sent");
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