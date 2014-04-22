/*****************************************************************
Agent Client demanding a painting service: AgClient.java

 *****************************************************************/

package hotelmania.group3;

import jade.core.Agent;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.content.lang.Codec;
import jade.content.lang.Codec.*;
import jade.content.lang.sl.*;
import jade.content.*;
import jade.content.onto.*;
import jade.content.onto.basic.*;
//import javax.swing.Action;
import hotelmania.onto.*;

import java.util.ArrayList;
import java.util.Date;




//import searchPainter.paintServOntology.PaintServOntology;


/**
 * This agent has the following functionality: 
 * <ul>
 * <li> It registers itself in the DF as HOTELMANIA
 * <li> Waits for requests of its Hotel Registration service
 * <li> If any registration request arrives, it register Hotel in their platform 
 * 		and send the answer to the Hotel
 * <li> Finally, it continues waiting for a new register requests.
 * </ul>
 * @author Lohana Lema Moreta, EMSE
 * @version $Date: 2014/04/19 19:28 $ $Revision: 1.0 $
 **/


public class AgHotelmania3 extends Agent {
	// Codec for the SL language used and instance of the ontology
	// PaintServOntology that we have created
    private Codec codec = new SLCodec();
    private Ontology ontology = SharedAgentsOntology.getInstance();
    
    private ArrayList<Hotel> RegisteredHotels = new ArrayList<Hotel>(); 
    public final static String HOTELMANIA = "Hotelmania";
    
    
    protected void setup(){
		System.out.println(getLocalName()+": has entered into the system");
//      Register of the codec and the ontology to be used in the ContentManager
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);		
        
		try{
			// Creates its own description
			DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setName("Registration"); //I am not sure if the name should be "Registration"
			sd.setType(HOTELMANIA);
			dfd.addServices(sd);
			
			// Registers its description in the DF
			DFService.register(this, dfd);
			System.out.println(getLocalName()+": registered in the DF");
			dfd = null;
			sd = null;
			doWait(10000);
			
		} catch (FIPAException e){
			e.printStackTrace();
		}
		
		
		
		// Adds a behavior to answer the estimation requests
		// Waits for a request and, when it arrives, answers with			  
		// the ESTIMATION and waits again.
		// If arrives a DECISION, it takes it (at this point, the painter would begin painting
		// if it is accepted...)
		
		addBehaviour(new CyclicBehaviour(this){
		
			public void action()
			{
				// Waits for estimation requests
				ACLMessage msg = receive(MessageTemplate.and(MessageTemplate.MatchLanguage(codec.getName()), 
						MessageTemplate.MatchOntology(ontology.getName())));
				if(msg!=null){
					try{
						ContentElement ce = null;
						int AclMessage = msg.getPerformative();
						ACLMessage reply = msg.createReply();
						
						if (AclMessage == ACLMessage.REQUEST){
							// If an REGISTRATION request arrives (type REQUEST)
							// it answers with the acceptance o deny
							
							// The ContentManager transforms the message content (string)
							// in java objects
							ce = getContentManager().extractContent(msg);
							// We expect an action inside the message
							if (ce instanceof Action){
								Action agAction = (Action) ce;
								Concept conc = agAction.getAction();
								// If the action is RegistrationRequest...
								if (conc instanceof RegistrationRequest){
									System.out.println(myAgent.getLocalName()+": received Registration request from "+(msg.getSender()).getLocalName());    
																						
									RegistrationRequest re = (RegistrationRequest)conc;
									Hotel newHotel = re.getHotel();
									boolean repeated = false;
									boolean blank_name = false;
									boolean wrong_name = false;
									
									
									/*Hotel testHotel = new Hotel();
									testHotel.setHotel_name("Hotel3");
									RegisteredHotels.add(testHotel);*/
									
									if (!RegisteredHotels.isEmpty()){
										for (int i=0; i<RegisteredHotels.size(); i++){
											Hotel currentHotel = (Hotel)RegisteredHotels.get(i);
											if (currentHotel.getHotel_name().toLowerCase().compareTo(newHotel.getHotel_name().toLowerCase()) == 0)
												repeated = true;
										}
									}
									
									if (newHotel.getHotel_name().compareTo("")==0)
										blank_name = true;
									
									if (!newHotel.getHotel_name().toLowerCase().contains("hotel"))
										wrong_name = true;
									
									
									if ( blank_name || wrong_name || repeated  ){
										reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
										System.out.println(myAgent.getLocalName()+ ": Registration request of "+ newHotel.getHotel_name() + " is deny");
									} 
									else {
										RegisteredHotels.add(newHotel);
										reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
										System.out.println(myAgent.getLocalName()+ ": "+ newHotel.getHotel_name() + " is resgistered in Hotelmania");	
									}
								} 
								else {
									reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
									System.out.println(myAgent.getLocalName()+ ": Registration request doesn't understood");
								}
							}
						} 
						else{
							reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
							System.out.println(myAgent.getLocalName()+ ": Registration request doesn't understood");
						}
						
						myAgent.send(reply);
						System.out.println(myAgent.getLocalName()+": answer sent");
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
					// If no message arrives
					block();
				}
			}
		});
    }

}
