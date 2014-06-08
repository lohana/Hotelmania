package hotelmania.group3.platform.hotelmania.behaviuor;

import hotelmania.group3.ontology.*;
import hotelmania.group3.platform.AgHotelmania3;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class Rating_ReceiveEvaluation extends CyclicBehaviour {

	public Rating_ReceiveEvaluation(Agent agent)
	{
		super(agent);
	}
	
	public void action()
	{
		// Waits for evaluation
		AgHotelmania3 agent = (AgHotelmania3)this.myAgent;
		
		// Waits for estimation requests
		ACLMessage msg = agent.receive(MessageTemplate.and(MessageTemplate.MatchLanguage(agent.codec.getName()), 
				MessageTemplate.and(MessageTemplate.MatchOntology(agent.innerOntology.getName()), 
						MessageTemplate.MatchProtocol(AgHotelmania3.EVALUATION_SERVICE))));
		
		if(msg!=null)
		{
			try
			{
				ContentElement content = null;
				int message = msg.getPerformative();

				if (message == ACLMessage.INFORM)
				{
					content = agent.getContentManager().extractContent(msg);
					if (content instanceof Action)
					{
						Action agAction = (Action)content;
						Concept concept = agAction.getAction();

						if (concept instanceof Evaluation)
						{
							System.out.println(myAgent.getLocalName()+": received Evaluation from "+(msg.getSender()).getLocalName());    
																				
							Evaluation e = (Evaluation)concept;
							Client client = e.getClient();
							agent.addOpinion(client.getClientId(), client.getHotel(), client.getRate());
							System.out.println(client.getInformation());
							
							System.out.println(String.format("Rate:%d", agent.getOpinionForHotel("Hotel3")));
						}
					}
				}
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
			block();
		}
	}

}