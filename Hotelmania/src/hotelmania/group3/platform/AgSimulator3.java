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
import hotelmania.group3.platform.simulator.behaviour.SendDayChange;
import hotelmania.group3.platform.simulator.behaviour.SubscribeAgents;
import hotelmania.ontology.SharedAgentsOntology;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
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
    // Agents, subscribed for NotificationDayEvent 
    private ArrayList<AID> registeredAgents = new ArrayList<AID>();
    
    private int day = 0;
    
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
			dfd.addServices(sd);
			
			// Registers its description in the DF
			DFService.register(this, dfd);
			System.out.println(getLocalName() + ": is registered in the DF");
			dfd = null;
			sd = null;
			
			System.out.println(getLocalName() + ": Waiting subscriptions ...");
			
			doWait(10000);
			
		} catch (FIPAException e){
			e.printStackTrace();
		}
		
		// Adds a behavior to answer the estimation requests
		addBehaviour(new SubscribeAgents(this));

		int interval = 5000;
		//interval = Integer.parseInt(Configuration.getInstance().getProperty(Configuration.DATE_LENGTH)) * 1000;
		interval = 10 * 1000;

		// Adds a behavior to receive evaluation from clients
		addBehaviour(new SendDayChange(this, interval));
    }
	
	public void changeDay()
	{
		day++;
	}

	public int getCurrentDay()
	{
		return day;
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
}
