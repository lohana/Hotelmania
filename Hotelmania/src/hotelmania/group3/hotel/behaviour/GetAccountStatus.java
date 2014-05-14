package hotelmania.group3.hotel.behaviour;

import java.util.Date;

import jade.lang.acl.ACLMessage;
import hotelmania.group3.hotel.AgHotel3;
import hotelmania.group3.platform.AgBank3;
import hotelmania.ontology.AccountStatusQueryRef;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

@SuppressWarnings("serial")
public class GetAccountStatus extends CyclicBehaviour {

	boolean end = false;
	AID[] simulator = new AID[20];
	AID ag;
	int last = 0;
	boolean ignore = false;


	public GetAccountStatus(Agent agent) {

		super(agent);
	}

	public void action() {

		AgHotel3 agent = (AgHotel3) this.myAgent;
		ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);

		if(agent.id_account!=0) {

			final Date registerTime = new Date();


			ACLMessage reply = msg.createReply();
			reply.setProtocol(agent.ACCOUNT_INFO);



			// Creates the description for the type of agent to be searched
			DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType(AgBank3.ACCOUNTSTATUS_SERVICE);
			dfd.addServices(sd);

			if ((new Date()).getTime() - registerTime.getTime() >= 60000){
				end = true;
			}

			// It finds agents of the required type
			DFAgentDescription[] res = new DFAgentDescription[20];
			try {
				res = DFService.search(myAgent, dfd);
			} catch (FIPAException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// Gets the first occurrence, if there was success
			if (res.length > 0){
				for (int i = 0; i < res.length; i++){
					ag = (AID)res[i].getName();

					for (int j = 0; j < last; j++){
						if (simulator[j].compareTo(ag) == 0){
							ignore = true;
						}
					}
					// If we have not contacted yet with this simulator
					if (!ignore){
						simulator[last++] = ag;




						msg.addReceiver(ag);
						msg.setLanguage(agent.codec.getName());
						msg.setOntology(agent.ontology.getName());
						msg.setProtocol(agent.ACCOUNT_INFO);

						// Create Action AccountStatusQueryRef

						AccountStatusQueryRef acsqf = new AccountStatusQueryRef();


						acsqf.setId_account(agent.id_account);

						Action agAction = new Action(ag,acsqf);


						try {
							agent.getContentManager().fillContent(msg, agAction);
						} catch (CodecException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (OntologyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						agent.send(msg);
						System.out.println(agent.getLocalName()
								+ ": QUERY_REF AccountStatusQueryRef  "
								+ ag.getName());


					}ignore = false;
				}
				
				agent.doWait(5);
			} else {
				// If no new Simulator has been found, it waits 5 seconds
				agent.doWait(5);
			}	
		}
		
	}
}