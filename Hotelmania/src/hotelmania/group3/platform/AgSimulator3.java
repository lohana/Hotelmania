/**
 * This agent has the following functionality: 
 * <ul>
 * <li> It registers itself in the DF
 * <li> Waits for agents to subscribes for NotificationDayEvent;
 * <li> Send notification for day event;
 * <li> Generates clients.
 * </ul>
 * @author Eleonora Adova, EMSE
 * @version $Date: 2014/05/03 19:28 $ $Revision: 1.0 $
 **/

package hotelmania.group3.platform;

import hotelmania.group3.ontology.Ontology3;
import hotelmania.group3.platform.simulator.behaviour.SubscribeEndSimulation;
import hotelmania.group3.platform.simulator.behaviour.SendDayChange;
import hotelmania.group3.platform.simulator.behaviour.SubscribeAgents;
import hotelmania.ontology.SharedAgentsOntology;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class AgSimulator3 extends Agent {

	public Codec codec = new SLCodec();
	public Ontology ontology = SharedAgentsOntology.getInstance();
	public Ontology innerOntology = Ontology3.getInstance();
	public static final String TIMECHANGE_SERVICE = "SubscribeToDayEvent";
	public static final String END_SIMULATION = "EndSimulation";

	// Agents, subscribed for NotificationDayEvent 
	private ArrayList<AID> registeredAgents = new ArrayList<AID>();
	// Agents, subscribed for NotificationDayEvent 
	private ArrayList<AID> registeredAgents_EndSimulation = new ArrayList<AID>();
	
	// Settings
	int interval = 5000;
	float clientBudget = 50.0f;
	float clientBudgetVariance = 25.0f;
	int lastDay = 30;
	int clientsPerDay = 10;
	boolean isover = false;

	private int day = 0;
	private int nextClientID = 0;

	protected void setup() {
		System.out.println(getLocalName() + ": has entered.");

		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);		
		getContentManager().registerOntology(innerOntology);		

		try{
			// Creates its own description
			DFAgentDescription dfd = new DFAgentDescription();

			// Add registration service
			ServiceDescription sd = new ServiceDescription();
			sd.setName(this.getName()); 
			sd.setType(TIMECHANGE_SERVICE);
			
			ServiceDescription sdes = new ServiceDescription();
			sdes.setName(this.getName()); 
			sdes.setType(END_SIMULATION);
			
			dfd.addServices(sdes);
			dfd.addServices(sd);

			// Registers its description in the DF
			DFService.register(this, dfd);
			
			System.out.println(getLocalName() + ": is registered in the DF");
			System.out.println(getLocalName() + ":(EndSimulation) is registered in the DF");
			dfd = null;
			sd = null;
			sdes = null;

			System.out.println(getLocalName() + ": Waiting subscriptions ...");
			System.out.println(getLocalName() + ":(EndSimulation) Waiting subscriptions ...");

		} catch (FIPAException e){
			e.printStackTrace();
		}

		// Adds a behavior to answer the estimation requests
		addBehaviour(new SubscribeAgents(this));
		
		// Adds a behavior to answer the estimation requests
		addBehaviour(new SubscribeEndSimulation(this));
		
		interval = 5000;
		try {
			Configuration configuration = Configuration.getInstance();
			interval = Integer.parseInt(configuration.getProperty(Configuration.DATE_LENGTH)) * 1000;
			clientBudget = Float.parseFloat(configuration.getProperty(Configuration.CLIENT_BUDGET));
			clientBudgetVariance = Float.parseFloat(configuration.getProperty(Configuration.BUDGET_VARIANCE));
			lastDay = Integer.parseInt(configuration.getProperty(Configuration.MAX_DAYS));
			clientsPerDay = Integer.parseInt(configuration.getProperty(Configuration.CLIENTS_PER_DAY));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("EXCEPTION: Unable to read configuration file. Default values are used.");
		}

		// Adds a behavior to receive evaluation from clients
		addBehaviour(new SendDayChange(this, interval));
	}

	public void changeDay()
	{
		day++;
		if (day <= lastDay) {
			generateClients();
		} else {
			// End of the simulation
		}
	}

	public int getCurrentDay()
	{
		return day;
	}
	
	public int getLastDay()
	{
		return lastDay;
	}
public void setisover(boolean b)
{
isover = b;	
}

public boolean get_isover(){
	
	return isover; 
}
	public boolean isRegistered(AID agent)
	{
		for (AID ag : registeredAgents)
		{
			if (ag.getName().equals(agent.getName()))
			{
				return true;
			}
		}

		return false;
	}

	public void registerAgent(AID agent)
	{
		if (!isRegistered(agent))
		{
			registeredAgents.add(agent);
		}
	}

	public ArrayList<AID> getRegisteredAgents()
	{
		return new ArrayList<AID>(registeredAgents);
	}

	public boolean isRegistered_EndSimulation(AID agent){

		for (AID ag : registeredAgents_EndSimulation)
		{
			if (ag.getName().equals(agent.getName()))
			{
				return true;
			}
		}
		return false;
	}

	public void registerAgent_EndSimulation(AID agent)
	{
		if (!isRegistered_EndSimulation(agent))
		{
			registeredAgents_EndSimulation.add(agent);
		}
	}

	public ArrayList<AID> getRegisteredAgents_EndSimulation()
	{
		return new ArrayList<AID>(registeredAgents_EndSimulation);
	}

	private void generateClients() {
		for (int i = 0; i < clientsPerDay; i++) {
			nextClientID++;
			String name = "Client" + nextClientID;
			
			float budget = clientBudget + randomValue(0 - (int)clientBudgetVariance, (int)clientBudgetVariance);
			int arrivalDay = randomValue(day + 1, lastDay);
			int nightsToStay = randomValue(1, lastDay - arrivalDay);
			try {
				startNewAgent("hotelmania.group3.platform.AgClient3", "Client" + nextClientID, new Object[]{ name, budget, arrivalDay, nightsToStay });
			} catch (StaleProxyException e) {
				e.printStackTrace();
				System.out.println("Unable to launch agent Client.");
			}
		}
	}
	
	private void startNewAgent(String className,String agentName,Object[] arguments) throws StaleProxyException {
    	((AgentController)getContainerController().createNewAgent(agentName,className,arguments)).start();
    }

	private int randomValue(int min, int max) {
		return min + (int)(Math.random() * ((max - min) + 1));
	}


}
