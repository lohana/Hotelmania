package hotelmania.group3.platform.bank.behaviour;

import hotelmania.group3.hotel.AgHotel3;
import hotelmania.group3.platform.AgBank3;
import hotelmania.ontology.Account;
import hotelmania.ontology.AccountStatus;
import hotelmania.ontology.AccountStatusQueryRef;
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
public class SendAccountStatus extends CyclicBehaviour {

	public SendAccountStatus(Agent agente) {
		super(agente);
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub

		AgHotel3 agent = new AgHotel3();

		AgBank3 agentb = (AgBank3)this.myAgent;
		
		
		ACLMessage msg = agentb.receive(MessageTemplate.and(MessageTemplate.MatchLanguage(agent.codec.getName()), MessageTemplate.and(
						MessageTemplate.MatchOntology(agentb.ontology.getName()),MessageTemplate.MatchProtocol(agent.ACCOUNT_INFO))));

		if (msg != null) {

			ContentElement ce = null;
			int AclMessage = msg.getPerformative();
			ACLMessage reply = msg.createReply();
			reply.setProtocol(agent.ACCOUNT_INFO);

			if (AclMessage == ACLMessage.QUERY_REF) {





				try {
					ce = agentb.getContentManager().extractContent(msg);
				} catch (UngroundedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CodecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OntologyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// We expect an action inside the message
				if (ce instanceof Action){
					Action agAction = (Action) ce;
					Concept conc = agAction.getAction();

					if (conc instanceof AccountStatusQueryRef){


						System.out.println(myAgent.getLocalName()+": received AccountStatusQueryRef Request from   "+(msg.getSender()).getLocalName());    


						AccountStatusQueryRef re = (AccountStatusQueryRef)conc;

						int id_account = re.getId_account();

						
						Account ac =  agentb.getStatusForHotel(id_account);
						AccountStatus as = new AccountStatus();
											                  
		                 as.setAccount(ac);
		                     
						reply.setPerformative(ACLMessage.INFORM);


						reply.setProtocol(agent.ACCOUNT_INFO);
						try {
							myAgent.getContentManager().fillContent(reply,as);
						} catch (CodecException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (OntologyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						myAgent.send(reply);
						System.out.println(myAgent.getLocalName()+":  answer sent -> INFORM");



					}else{
						reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
						myAgent.send(reply);

						
					}



				}else{
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
