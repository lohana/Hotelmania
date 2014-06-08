// Processes the ACEPTATION answer to request

package hotelmania.group3.hotel.behaviour;

import hotelmania.group3.hotel.AgHotel3;
import hotelmania.group3.platform.AgClient3;
import hotelmania.ontology.Hotel;
import hotelmania.ontology.NumberOfClients;
import hotelmania.ontology.NumberOfClientsQueryRef;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class NUMBEROFCLIENTS_ExpectQueryRef extends CyclicBehaviour {

	public NUMBEROFCLIENTS_ExpectQueryRef(Agent agent)
	{
		super(agent);
	}
	
	public void action()
	{
		AgHotel3 agent = (AgHotel3)this.myAgent;
		
		// Waits for sign contract acceptation
		ACLMessage msg = agent.receive(MessageTemplate.and(MessageTemplate.MatchLanguage(agent.codec.getName()), 
				MessageTemplate.and(MessageTemplate.MatchOntology(agent.ontology.getName()),
						MessageTemplate.MatchProtocol(AgClient3.NUMBEROFCLIENTS_QUERY))));
		
		
		if(msg!=null){
			try{
				ContentElement ce = null;
				int AclMessage = msg.getPerformative();
				ACLMessage reply = msg.createReply();
				reply.setProtocol(AgClient3.NUMBEROFCLIENTS_QUERY);

								
				if (AclMessage == ACLMessage.QUERY_REF){
										
					// The ContentManager transforms the message content (string)
					// in java objects
					ce = agent.getContentManager().extractContent(msg);
					// We expect an action inside the message
				
						if (ce instanceof NumberOfClientsQueryRef){
							   
																				
							NumberOfClientsQueryRef  noc = (NumberOfClientsQueryRef )ce;
							int day = noc.getDay();							
							
							AID receiverAgent = msg.getSender();
							String requestedHotelName = receiverAgent.getLocalName();
							
							Hotel requestedHotel = new Hotel();
							requestedHotel.setHotelAgent(agent.getAID());
							requestedHotel.setHotel_name(requestedHotelName);
							
							//Calculate the number of clients of the requestedHotel.
							int numberOfclientsRequestedHotel = agent.getNumberOfClients(day);
							
							System.out.println(myAgent.getLocalName()+": received NUMBER OF CLIENTS QUERY from "+(msg.getSender()).getLocalName() );
							
							reply.setPerformative(ACLMessage.INFORM);
							NumberOfClients nocI = new NumberOfClients();
							nocI.setNum_clients(numberOfclientsRequestedHotel);
							myAgent.getContentManager().fillContent(reply, nocI);
							System.out.println(myAgent.getLocalName()+ ": Number of Clients INFORM has been send");
							
							
						} 
						else {
							reply.setPerformative(ACLMessage.FAILURE);
							System.out.println(myAgent.getLocalName()+ ": Number of Clients Query FAILED: Wrong Concept");
						}
					
				} 
				else{
					reply.setPerformative(ACLMessage.FAILURE);
					System.out.println(myAgent.getLocalName()+ ": Number of Clients Query FAILED: Request send it's wrong");
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
