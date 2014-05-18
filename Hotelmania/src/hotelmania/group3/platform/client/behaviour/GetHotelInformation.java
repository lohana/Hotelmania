
package hotelmania.group3.platform.client.behaviour;

import java.util.Date;

import jade.lang.acl.ACLMessage;
import hotelmania.group3.platform.AgClient3;
import hotelmania.group3.platform.AgHotelmania3;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

@SuppressWarnings("serial")
public class GetHotelInformation extends CyclicBehaviour {

	boolean end = false;
	AID[] simulator = new AID[20];
	AID ag;
	int last = 0;
	boolean ignore = false;


	public GetHotelInformation(Agent agent) {

		super(agent);
	}

	public void action() {

		AgClient3 agent = (AgClient3) this.myAgent;
		ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);



		final Date registerTime = new Date();


		// Creates the description for the type of agent to be searched
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(AgHotelmania3.HOTEL_INFORMATION_SERVICE);
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
					msg.setProtocol(AgHotelmania3.HOTEL_INFORMATION);


					agent.send(msg);
					System.out.println(agent.getLocalName()
							+ ": QUERY_REF  HotelInformation   "
							+ ag.getName());


				}ignore = false;
			}

			//agent.doWait(5);
		} else {
			// If no new Simulator has been found, it waits 5 seconds
			//agent.doWait(5000);
			//block();
		}	


	}
}