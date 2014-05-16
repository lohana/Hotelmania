package hotelmania.group3.platform.hotelmania.behaviuor;

import hotelmania.group3.hotel.AgHotel3;
import hotelmania.group3.platform.AgBank3;
import hotelmania.group3.platform.AgHotelmania3;
import hotelmania.ontology.Account;
import hotelmania.ontology.AccountStatus;
import hotelmania.ontology.AccountStatusQueryRef;
import hotelmania.ontology.Hotel;
import hotelmania.ontology.HotelInformation;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.ContentElementList;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class HotelmaniaInformation extends CyclicBehaviour {

	public HotelmaniaInformation(Agent agente) {
		super(agente);
	}

	@SuppressWarnings("static-access")
	@Override
	public void action() {
		// TODO Auto-generated method stub

		AgHotel3 agent = new AgHotel3();

		AgHotelmania3 agentb = (AgHotelmania3) this.myAgent;

		ACLMessage msg = agentb.receive(MessageTemplate.and(MessageTemplate
				.MatchLanguage(agentb.codec.getName()), MessageTemplate.and(
						MessageTemplate.MatchOntology(agentb.ontology.getName()),
						MessageTemplate.MatchProtocol(agentb.HOTEL_INFORMATION))));

		if (msg != null) {

			ContentElement ce = null;
			int AclMessage = msg.getPerformative();
			ACLMessage reply = msg.createReply();
			reply.setProtocol(agentb.HOTEL_INFORMATION);

			if (AclMessage == ACLMessage.QUERY_REF
					&& !agentb.RegisteredHotels.isEmpty()) {

				/*
				 * try { ce = agentb.getContentManager().extractContent(msg); }
				 * catch (UngroundedException e) { // TODO Auto-generated catch
				 * block e.printStackTrace(); } catch (CodecException e) { //
				 * TODO Auto-generated catch block e.printStackTrace(); } catch
				 * (OntologyException e) { // TODO Auto-generated catch block
				 * e.printStackTrace(); }
				 */
				// We expect an action inside the message
				// if (ce instanceof Action){
				// Action agAction = (Action) ce;
				// Concept conc = agAction.getAction();

				// if (conc instanceof AccountStatusQueryRef){

				System.out
				.println(myAgent.getLocalName()
						+ ": received HotelmaniaInformation+++++++++++++++ QUERY_REF from   "
						+ (msg.getSender()).getLocalName());

				ContentElementList cel = new ContentElementList();
				HotelInformation hi = new HotelInformation();

				if (!agentb.RegisteredHotels.isEmpty()) {

					for (int i = 0; i < agentb.RegisteredHotels.size(); i++) {
						Hotel currentHotel = (Hotel) agentb.RegisteredHotels
								.get(i);
						hi.setHotel(currentHotel);
						hi.setRating(agentb.getOpinionForHotel(currentHotel
								.getHotel_name()));
						cel.add(hi);

					}

					try {
						myAgent.getContentManager().fillContent(reply, cel);

					} catch (CodecException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (OntologyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				reply.setPerformative(ACLMessage.INFORM);

				reply.setProtocol(agentb.HOTEL_INFORMATION);

				myAgent.send(reply);
				System.out.println(myAgent.getLocalName()
						+ ":  answer sent -> INFORM");

				/*
				 * }else{ reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
				 * myAgent.send(reply);
				 * 
				 * 
				 * }
				 */

				/*
				 * }else{ reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
				 * myAgent.send(reply);
				 * 
				 * 
				 * }
				 */

			} else if (AclMessage == ACLMessage.REFUSE) {
				System.out.println(myAgent.getLocalName()
						+ ": received REFUSE  from "
						+ (msg.getSender()).getLocalName());

			} else if (AclMessage == ACLMessage.NOT_UNDERSTOOD) {
				System.out.println(myAgent.getLocalName()
						+ ": received NOT_UNDERSTOOD  from "
						+ (msg.getSender()).getLocalName());

			}else{System.out.println(myAgent.getLocalName()
					+ ": received REFUSE  from HOTELMANIA EMPTY"
					+ (msg.getSender()).getLocalName());}
			
			
		} else {
			// block();

		}
	}
}
