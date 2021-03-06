/**
 * Subscribes the agent for end of the simulation
 * 
 **/

package hotelmania.group3.platform.agency.behaviuor;

import hotelmania.group3.platform.AgAgency3;
import hotelmania.group3.platform.AgSimulator3;
import hotelmania.ontology.EndSimulation;

import java.util.Date;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class SimulationEnd_SubscribeForSimulationEnd extends SimpleBehaviour {
	
	boolean end = false;
	AID[] simulator = new AID[20];
	AID ag;
	int last = 0;
	boolean ignore = false;

	public SimulationEnd_SubscribeForSimulationEnd(Agent agent) {
		super(agent);
	}
	
	public void action(){
		AgAgency3 agent =  (AgAgency3)this.myAgent;
		
	     // From this moment, it will be searching for the simulator for a minute		
     	final Date registerTime = new Date();
		
		// Creates the description for the type of agent to be searched
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(AgSimulator3.END_SIMULATION);
		dfd.addServices(sd);
		
		try{
			// If has been searching for the simulator for more than a minute, it does not search any more
			if ((new Date()).getTime() - registerTime.getTime() >= 60000){
				end = true;
			}
			
			// It finds agents of the required type
			DFAgentDescription[] res = new DFAgentDescription[20];
			res = DFService.search(myAgent, dfd);
			
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
						
						// Subscribe
						ACLMessage msg = new ACLMessage(ACLMessage.SUBSCRIBE);
						msg.setProtocol(AgSimulator3.END_SIMULATION);
						msg.addReceiver(ag);
						msg.setLanguage(agent.codec.getName());
						msg.setOntology(agent.ontology.getName());
						EndSimulation s = new EndSimulation();
						Action agAction = new Action(ag, s);
						try{
							agent.getContentManager().fillContent(msg, agAction);
							agent.send(msg);
							System.out.println(agent.getLocalName()+": SUSCRIBE END_SIMULATION");
							end = true;
						}
						catch (CodecException ce){
							ce.printStackTrace();
						}
						catch (OntologyException oe){
							oe.printStackTrace();
						}
					}
					ignore = false;
				}
				Thread.sleep(1000);
			} else {
				// If no new Simulator has been found, it waits 1 second
				Thread.sleep(1000);
			}	
		}catch (Exception e){
			e.printStackTrace();
		}
	} 
	
	public boolean done (){
     	return end;
    }

}
