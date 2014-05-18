// Processes the ACEPTATION answer to an registration request

package hotelmania.group3.hotel.behaviour;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec.*;
import jade.content.onto.*;
import jade.content.onto.basic.*;
import hotelmania.group3.hotel.AgHotel3;
import hotelmania.group3.platform.AgClient3;
import hotelmania.ontology.*;


@SuppressWarnings("serial")
public class BOOKAROOM_BookARoomExpectRequest extends CyclicBehaviour{
	
	public BOOKAROOM_BookARoomExpectRequest(Agent agent) {
		super(agent);
	}
	
	public void action(){
		AgHotel3 agent = (AgHotel3)this.myAgent;
		
		boolean hotelIsFull = false;
		boolean clientRepeated = false;
		boolean differentPrice = false;
		
		// Waits for sign contract acceptation
		ACLMessage msg = agent.receive(MessageTemplate.and(MessageTemplate.MatchLanguage(agent.codec.getName()), 
				MessageTemplate.and(MessageTemplate.MatchOntology(agent.ontology.getName()),
						MessageTemplate.MatchProtocol(AgClient3.BOOKAROOM_REQUEST))));
		
		if (msg!=null){
			try{
				ContentElement ce = null;
				int AclMessage = msg.getPerformative();
				ACLMessage reply = msg.createReply();
				reply.setProtocol(AgClient3.BOOKAROOM_REQUEST);
					
				if (AclMessage == ACLMessage.REQUEST){
					// The ContentManager transforms the message content (string)
					// in java objects
					ce = agent.getContentManager().extractContent(msg);
					// We expect an action inside the message
					if (ce instanceof Action){
						Action agAction = (Action) ce;
						Concept conc = agAction.getAction();
						// If the action is BookRoom
						if (conc instanceof BookRoom){
							BookRoom  br = (BookRoom)conc;
							
							System.out.println(myAgent.getLocalName()+": received BOOK A ROOM Request from "+(msg.getSender()).getLocalName() );
							
							AID receiverAgent = msg.getSender();
							String requestedClientName = receiverAgent.getLocalName();
							
							
							if (!agent.BookingClients.isEmpty()){
								//Review if client exist in BookingList
								for (String currentClient : agent.BookingClients){
									if (currentClient.equals(requestedClientName))
										clientRepeated = true;
								}
								//review if Hotel is full
								if (agent.BookingClients.size() == agent.numberOfRooms)
									hotelIsFull = true;
							} else {
								System.out.println(myAgent.getLocalName()+": does not have rooms" );
							}
							
							//Review if requestPrice is different to dictionary offers price
							if (!agent.isValidOffer(requestedClientName, br.getBookingOffer()))
								differentPrice = true;
								
													
							if (hotelIsFull){
								reply.setPerformative(ACLMessage.REFUSE);
								System.out.println(myAgent.getLocalName()+": Sorry " + requestedClientName + " All rooms are occupied ");
							} else if (clientRepeated){
								reply.setPerformative(ACLMessage.REFUSE);
								System.out.println(myAgent.getLocalName()+": Client: " + requestedClientName + " is already hosted ");
							} else if (differentPrice){
								reply.setPerformative(ACLMessage.REFUSE);
								System.out.println(myAgent.getLocalName()+": Price has been change.");
							} else {
								reply.setPerformative(ACLMessage.AGREE);
								agent.BookingClients.add(requestedClientName);
								//System.out.println(myAgent.getLocalName()+": BOOK A ROOM Request has been accepted");
								System.out.println(myAgent.getLocalName()+": Welcome to "+ agent.getName() + " " + requestedClientName);
							}
						}
					}
					
				} else {
					reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
					System.out.println(myAgent.getLocalName()+ ": BOOK ROOM REQUEST - NOT UNDERSTOOD: wrong protocol");
				}	
				
			}catch (CodecException e){
				e.printStackTrace();
			} catch (UngroundedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
	}
	 
	
	
}
