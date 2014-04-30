/**
 * Client agent
 * @author Eleonora Adova, EMSE
 * @version $Date: 2014/04/27 20:28 $ $Revision: 1.0 $
 **/

package hotelmania.group3.platform;

import jade.core.Agent;
import jade.content.lang.Codec;
import jade.content.lang.sl.*;
import jade.content.onto.*;
import hotelmania.group3.ontology.Ontology3;
import hotelmania.group3.platform.client.behaviour.SendRate;

@SuppressWarnings("serial")
public class AgClient extends Agent {
	
	public static final String EVALUATION_SERVICE = "Evaluation";
	
	public Codec codec = new SLCodec();
	public Ontology ontology = Ontology3.getInstance();
	
	public String id;
	
	// Information about the hotel where the client stayed
	private String hotel = "";
	private int rate = 0;
	
	protected void setup(){
		Object[] pr = this.getArguments();
		this.id = (String)pr[0];
		
		System.out.println(getLocalName()+": New client entered");

        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);		
        
        // TOREMOVE
        // Random generation of rate, should be assign when going to the hotel 
        rate = randomValue();
        hotel = "Hotel3";
        
        addBehaviour(new SendRate(this));
    }
	
	public int getRate()
	{
		return rate;
	}
	
	public String getHotel()
	{
		return hotel;
	}

	private int randomValue() {
		return (int)(Math.random() * 11);
	}
}
