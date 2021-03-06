/**
 * This is action of the Hotelmania agent for registering hotels.
 * @author Lohana Lema Moreta, EMSE
 * @version $Date: 2014/04/27 12:28 $ $Revision: 1.0 $
 **/

package hotelmania.group3.platform.hotelmania.behaviuor;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.content.lang.Codec.*;
import jade.content.*;
import jade.content.onto.*;
import jade.content.onto.basic.*;
import hotelmania.group3.platform.AgHotelmania3;
import hotelmania.group3.platform.hotelmania.behaviuor.Registration_ExpectRequest;
import hotelmania.ontology.*;

@SuppressWarnings("serial")
public class Registration_ExpectRequest extends CyclicBehaviour {
    
	public Registration_ExpectRequest(Agent agent) {
		super(agent);
	}
		
	// Adds a behavior to answer the estimation requests
	// Waits for a request and, when it arrives, answers with			  
	// the ESTIMATION and waits again.
	// If arrives a DECISION, it takes it (at this point, the painter would begin painting
	// if it is accepted...)	
	public void action() {
		AgHotelmania3 agent = (AgHotelmania3)this.myAgent;
		
		// Waits for estimation requests
		ACLMessage msg = agent.receive(MessageTemplate.and(MessageTemplate.MatchLanguage(agent.codec.getName()), 
				MessageTemplate.and(MessageTemplate.MatchOntology(agent.ontology.getName()),
						MessageTemplate.MatchProtocol(AgHotelmania3.REGISTRATION_SERVICE))));
		
		if(msg!=null){
			try{
				ContentElement ce = null;
				int AclMessage = msg.getPerformative();
				ACLMessage reply = msg.createReply();
				reply.setProtocol(AgHotelmania3.REGISTRATION_SERVICE);
				
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
						// If the action is RegistrationRequest...
						if (conc instanceof RegistrationRequest){
							System.out.println(myAgent.getLocalName()+": received REGISTRATION REQUEST from "+(msg.getSender()).getLocalName());    
																				
							RegistrationRequest re = (RegistrationRequest)conc;
							Hotel newHotel = re.getHotel();
							boolean repeated = false;
							boolean blank_name = false;
							boolean wrong_name = false;
												
							/*Hotel testHotel = new Hotel();
							testHotel.setHotel_name("Hotel3");
							agent.RegisteredHotels.add(testHotel);*/
							
							if (!agent.RegisteredHotels.isEmpty()){
								for (int i = 0; i < agent.RegisteredHotels.size(); i++){
									Hotel currentHotel = (Hotel)agent.RegisteredHotels.get(i);
									if (currentHotel.getHotel_name().toLowerCase().compareTo(newHotel.getHotel_name().toLowerCase()) == 0)
										repeated = true;
								}
							}
							
							if (newHotel.getHotel_name().compareTo("")==0)
								blank_name = true;
							
							if (!newHotel.getHotel_name().toLowerCase().contains("hotel"))
								wrong_name = true;
							
							
							if ( blank_name || wrong_name || repeated  ){
								reply.setPerformative(ACLMessage.REFUSE);
								System.out.println(myAgent.getLocalName()+ ": Registration Request of "+ newHotel.getHotel_name() + " is DENIED");
							} 
							else {
								agent.RegisteredHotels.add(newHotel);
								//initial Rate for new Hotel, default 5
								 agent.addOpinion("", newHotel.getHotel_name(), 5);
								
								reply.setPerformative(ACLMessage.AGREE);
								System.out.println(myAgent.getLocalName()+ ": Registration Request of "+ newHotel.getHotel_name() + " is ACCEPTED");
								System.out.println(myAgent.getLocalName()+ ": "+ newHotel.getHotel_name() + " is REGISTERED in Hotelmania");	
							}
						} 
						else {
							reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
							System.out.println(myAgent.getLocalName()+ ": Registration Request DOES NOT UNDERSTOOD");
						}
					}
				} 
				else{
					reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
					System.out.println(myAgent.getLocalName()+ ": Registration request DOES NOT UNDERSTOOD");
				}
				
				reply.setProtocol(AgHotelmania3.REGISTRATION_SERVICE);
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
