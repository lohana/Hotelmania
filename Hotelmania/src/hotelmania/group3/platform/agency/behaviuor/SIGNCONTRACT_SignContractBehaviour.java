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
import hotelmania.group3.platform.agency.behaviuor.SIGNCONTRACT_SignContractBehaviour;
import hotelmania.ontology.*;

@SuppressWarnings("serial")
public class SIGNCONTRACT_SignContractBehaviour extends CyclicBehaviour {
    
	public SIGNCONTRACT_SignContractBehaviour(Agent agent) {
		super(agent);
	}
		
		
	public void action() {
		AgAgency3 agent = (AgAgency3)this.myAgent;
		
		// Waits for estimation requests
		ACLMessage msg = agent.receive(MessageTemplate.and(MessageTemplate.MatchLanguage(agent.codec.getName()), 
				MessageTemplate.MatchOntology(agent.ontology.getName())) );
		
		if(msg!=null){
			try{
				ContentElement ce = null;
				int AclMessage = msg.getPerformative();
				ACLMessage reply = msg.createReply();
				
				if (!MessageTemplate.MatchProtocol(AgAgency3.SIGN_CONTRACT).match(msg)) {
					reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
					System.out.println(myAgent.getLocalName()+ ": Sign Contract Request DOES NOT UNDERSTOOD: Protocol isn't set it");
				}
				else if (AclMessage == ACLMessage.REQUEST){
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
							System.out.println(myAgent.getLocalName()+": received SIGN CONTRACT REQUEST from "+(msg.getSender()).getLocalName());    
																				
							SignContract sc = (SignContract)conc;
							Hotel requestedHotel = sc.getHotel();
							boolean repeated_day_hotel = false;
							
							
							/*Simulation Exist Contract in day 0 for a Hotel3*/
							/*SignContract testSc = new SignContract();
							Hotel testHotel = new Hotel();
							testHotel.setHotel_name("Hotel3");
							
							Contract testContract = new Contract();
							testContract.setChef_1stars(1);
							testContract.setChef_2stars(1);
							testContract.setChef_3stars(1);
							testContract.setRoom_service_staff(2);
							testContract.setRecepcionist_novice(0);
							testContract.setRecepcionist_experienced(2);
								
							//Add hotel information to the registration request
							testSc.setHotel(testHotel);
							testSc.setContract(testContract);
							agent.signedContracts.add(agent.currentDay,testSc);
							
							agent.currentDay++;*/
							/*SIMULATION ENDS*/
							
							
							//One contract per day per hotel
							if (!agent.signedContracts.isEmpty()){
								for (int i = 0; i < agent.signedContracts.size(); i++){
									Hotel currentHotel = (Hotel)agent.signedContracts.get(i).getHotel();
									if ( (currentHotel.getHotel_name().toLowerCase().compareTo(requestedHotel.getHotel_name().toLowerCase()) == 0) && (i==agent.currentDay))
										repeated_day_hotel = true;
								}
							}
												
							
							if ( repeated_day_hotel  ){
								reply.setPerformative(ACLMessage.REFUSE);
								System.out.println(myAgent.getLocalName()+ ": Sign Contract Request of "+ requestedHotel.getHotel_name() + " is REFUSED: Contract exits for this Hotel in this day");
							} 
							else {
								agent.signedContracts.add(agent.currentDay, sc);
								reply.setPerformative(ACLMessage.AGREE);
								System.out.println(myAgent.getLocalName()+ ": Sign Contract Request of "+ requestedHotel.getHotel_name() + " is AGREED");
								System.out.println(myAgent.getLocalName()+ ": "+ requestedHotel.getHotel_name() + " has hired Staff requested");	
							}
						} 
						else {
							reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
							System.out.println(myAgent.getLocalName()+ ": Sign Contract Request DOES NOT UNDERSTOOD: Protocol isn't set it.");
						}
					}
				} 
				else{
					reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
					System.out.println(myAgent.getLocalName()+ ": Sign Contract Request DOES NOT UNDERSTOOD: Request send it's wrong");
				}
				
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
			// If no message arrives
			block();
		}
	}
}
