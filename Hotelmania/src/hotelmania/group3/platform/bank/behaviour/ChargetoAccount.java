package hotelmania.group3.platform.bank.behaviour;

import hotelmania.group3.hotel.AgHotel3;
import hotelmania.group3.platform.AgBank3;
import hotelmania.ontology.Account;
import hotelmania.ontology.AccountStatus;
import hotelmania.ontology.AccountStatusQueryRef;
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
public class ChargetoAccount extends CyclicBehaviour {

	public ChargetoAccount(Agent agente) {
		super(agente);
	}

	@SuppressWarnings("static-access")
	@Override
	public void action() {
		// TODO Auto-generated method stub

		AgHotel3 agent = new AgHotel3();

		AgBank3 agentb = (AgBank3)this.myAgent;
		
		
 		ACLMessage msg = agentb.receive(MessageTemplate.and(MessageTemplate.MatchLanguage(agentb.codec.getName()), MessageTemplate.and(
						MessageTemplate.MatchOntology(agentb.ontology.getName()),MessageTemplate.MatchProtocol(agentb.CHARGE_ACCOUNT_SERVICE))));

		if (msg != null) {

			ContentElement ce = null;
			int AclMessage = msg.getPerformative();
			ACLMessage reply = msg.createReply();
			reply.setProtocol(agentb.CHARGE_ACCOUNT_SERVICE);

			if (AclMessage == ACLMessage.REQUEST) {





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

					if (conc instanceof ChargeAccount){


						System.out.println(myAgent.getLocalName()+": received  ChargetoAccount REQUEST from   "+(msg.getSender()).getLocalName());    


						ChargeAccount re = (ChargeAccount)conc;

						
						Float amount = re.getAmount();
						Hotel h = re.getHotel();
 						
						if( agentb.chargetoaccount(h,amount) )
						{
							reply.setPerformative(ACLMessage.AGREE);
							reply.setProtocol(agentb.ACCOUNTSTATUS_SERVICE);					 
							myAgent.send(reply);
							System.out.println(myAgent.getLocalName()+":  answer sent -> AGREE");
							
							
						}else{
							reply.setPerformative(ACLMessage.REFUSE);
							reply.setProtocol(agentb.ACCOUNTSTATUS_SERVICE);					 
							myAgent.send(reply);
							System.out.println(myAgent.getLocalName()+":  answer sent -> REFUSE");

							
							
						}		

			

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