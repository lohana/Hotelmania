
package hotelmania.group3.ontology;

import jade.content.*;

@SuppressWarnings("serial")
public class Evaluation implements AgentAction {

   private Client client;
   
   public void setClient(Client value) { 
    this.client=value;
   }
   
   public Client getClient() {
     return this.client;
   }
}