package hotelmania.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: StayQueryRef
* @author ontology bean generator
* @version 2014/05/27, 17:55:03
*/
public class StayQueryRef implements Predicate {

   /**
* Protege name: stay
   */
   private Stay stay;
   public void setStay(Stay value) { 
    this.stay=value;
   }
   public Stay getStay() {
     return this.stay;
   }

}
