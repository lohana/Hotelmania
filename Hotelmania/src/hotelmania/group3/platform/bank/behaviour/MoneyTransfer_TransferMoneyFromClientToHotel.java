package hotelmania.group3.platform.bank.behaviour;

import hotelmania.group3.platform.AgBank3;
import hotelmania.ontology.ChargeAccount;
import hotelmania.ontology.Hotel;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class MoneyTransfer_TransferMoneyFromClientToHotel extends CyclicBehaviour {

	public MoneyTransfer_TransferMoneyFromClientToHotel(Agent agente) {
		super(agente);
	}

	@Override
	public void action() {

		AgBank3 agentb = (AgBank3) this.myAgent;

		ACLMessage msg = agentb
				.receive(MessageTemplate.and(MessageTemplate
						.MatchLanguage(agentb.codec.getName()), MessageTemplate
						.and(MessageTemplate.MatchOntology(agentb.ontology
								.getName()), MessageTemplate
								.MatchProtocol(AgBank3.PAYMENTS))));

		if (msg != null) {

			ContentElement ce = null;
			int AclMessage = msg.getPerformative();
			ACLMessage reply = msg.createReply();
			reply.setProtocol(AgBank3.PAYMENTS);

			if (AclMessage == ACLMessage.REQUEST) {

				try {
					ce = agentb.getContentManager().extractContent(msg);
				} catch (UngroundedException e) {
					e.printStackTrace();
				} catch (CodecException e) {
					e.printStackTrace();
				} catch (OntologyException e) {
					e.printStackTrace();
				}
				// We expect an action inside the message
				if (ce instanceof Action) {
					Action agAction = (Action) ce;
					Concept conc = agAction.getAction();

					if (conc instanceof ChargeAccount) {

						System.out.println(myAgent.getLocalName()
								+ ": received  PAYMENTS REQUEST from   "
								+ (msg.getSender()).getLocalName());

						ChargeAccount re = (ChargeAccount) conc;

						Float amount = re.getAmount();
						Hotel h = re.getHotel();

						if (agentb.transferMoney(h.getHotel_name(), amount, (msg.getSender()).getLocalName())) {
							reply.setPerformative(ACLMessage.AGREE);
							reply.setProtocol(AgBank3.PAYMENTS);
							myAgent.send(reply);
							System.out.println(myAgent.getLocalName()
									+ ":  answer sent -> AGREE");
						} else {
							reply.setPerformative(ACLMessage.REFUSE);
							reply.setProtocol(AgBank3.PAYMENTS);
							myAgent.send(reply);
							System.out.println(myAgent.getLocalName()
									+ ":  answer sent -> REFUSE");
						}
					} else {
						reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
						myAgent.send(reply);
					}
				} else {
					reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
					myAgent.send(reply);
				}

			} else if (AclMessage == ACLMessage.REFUSE) {
				System.out.println(myAgent.getLocalName()
						+ ": received REFUSE  from "
						+ (msg.getSender()).getLocalName());

			} else if (AclMessage == ACLMessage.NOT_UNDERSTOOD) {
				System.out.println(myAgent.getLocalName()
						+ ": received NOT_UNDERSTOOD  from "
						+ (msg.getSender()).getLocalName());

			}
		}
	}
}
