// Inner ontology for group 3

package hotelmania.group3.ontology;

import jade.content.onto.*;
import jade.content.schema.*;

@SuppressWarnings("serial")
public class Ontology3 extends jade.content.onto.Ontology  {
	
  //NAME
  public static final String ONTOLOGY_NAME = "Ontology3";
  // The singleton instance of this ontology
  @SuppressWarnings("unused")
private static ReflectiveIntrospector introspect = new ReflectiveIntrospector();
  private static Ontology theInstance = new Ontology3();
  public static Ontology getInstance() {
     return theInstance;
  }


   // VOCABULARY
  public static final String EVALUATION_CLIENT = "client";
    public static final String EVALUATION = "Evaluation";
    public static final String CLIENT_ID = "clientId";
    public static final String CLIENT = "clientId";
    public static final String HOTEL = "hotel";
    public static final String RATE = "rate";
    public static final String BUDGET = "budget";

  /**
   * Constructor
  */
  private Ontology3(){ 
    super(ONTOLOGY_NAME, BasicOntology.getInstance());
    try { 

    // adding Concept(s)
    ConceptSchema clientSchema = new ConceptSchema(CLIENT);
    add(clientSchema, hotelmania.group3.ontology.Client.class);

    // adding AgentAction(s)
    AgentActionSchema evaluationSchema = new AgentActionSchema(EVALUATION);
    add(evaluationSchema, hotelmania.group3.ontology.Evaluation.class);

    // adding fields
    clientSchema.add(CLIENT_ID, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
    clientSchema.add(HOTEL, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
    clientSchema.add(RATE, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    clientSchema.add(BUDGET, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.MANDATORY);
    evaluationSchema.add(EVALUATION_CLIENT, clientSchema, ObjectSchema.MANDATORY);

   }catch (java.lang.Exception e) {
	   e.printStackTrace();}
  }
  }
