package hotelmania.group3.HotelBehaviours;

import hotelmania.ontology.RegistrationRequest;

import java.util.Date;



import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

/**
 * This class has the following functionality: 
 * <ul>
 * <li> Adds a behavior to search a hotelmania agent
		IF it is found, asks it a registration request
		ELSE, waits 5 seconds and tries again
 * </ul>
 * @author Lohana Lema Moreta, EMSE
 * @version $Date: 2014/04/19 17:19 $ $Revision: 1.0 $
 **/

public class SearchHotelmaniaAgent extends SimpleBehaviour{

	
	
	
	public void action(){
		
		
	}
	
	 public boolean done ()
     {
		return false;
     	
     }
	
	
}
