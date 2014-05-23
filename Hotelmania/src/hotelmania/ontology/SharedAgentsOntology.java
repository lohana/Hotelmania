// file: SharedAgentsOntologyOntology.java generated by ontology bean generator.  DO NOT EDIT, UNLESS YOU ARE REALLY SURE WHAT YOU ARE DOING!
package hotelmania.ontology;

import jade.content.onto.*;
import jade.content.schema.*;
import jade.util.leap.HashMap;
import jade.content.lang.Codec;
import jade.core.CaseInsensitiveString;

/** file: SharedAgentsOntologyOntology.java
 * @author ontology bean generator
 * @version 2014/05/23, 21:12:41
 */
public class SharedAgentsOntology extends jade.content.onto.Ontology  {
  //NAME
  public static final String ONTOLOGY_NAME = "SharedAgentsOntology";
  // The singleton instance of this ontology
  private static ReflectiveIntrospector introspect = new ReflectiveIntrospector();
  private static Ontology theInstance = new SharedAgentsOntology();
  public static Ontology getInstance() {
     return theInstance;
  }


   // VOCABULARY
    public static final String BOOKINGOFFER_ROOMPRICE="roomPrice";
    public static final String BOOKINGOFFER="BookingOffer";
    public static final String NUMBEROFCLIENTS_NUM_CLIENTS="num_clients";
    public static final String NUMBEROFCLIENTS="NumberOfClients";
    public static final String ACCOUNTSTATUS_ACCOUNT="account";
    public static final String ACCOUNTSTATUS="AccountStatus";
    public static final String HOTELINFORMATION_HOTEL="hotel";
    public static final String HOTELINFORMATION_RATING="rating";
    public static final String HOTELINFORMATION="HotelInformation";
    public static final String NOTIFICATIONENDSIMULATION="NotificationEndSimulation";
    public static final String STAYQUERYREF_STAY="stay";
    public static final String STAYQUERYREF="StayQueryRef";
    public static final String NOTIFICATIONDAYEVENT_DAYEVENT="dayEvent";
    public static final String NOTIFICATIONDAYEVENT="NotificationDayEvent";
    public static final String ACCOUNTSTATUSQUERYREF_ID_ACCOUNT="id_account";
    public static final String ACCOUNTSTATUSQUERYREF="AccountStatusQueryRef";
    public static final String CREATEACCOUNTREQUEST_HOTEL="hotel";
    public static final String CREATEACCOUNTREQUEST="CreateAccountRequest";
    public static final String SIGNCONTRACT_CONTRACT="contract";
    public static final String SIGNCONTRACT_HOTEL="hotel";
    public static final String SIGNCONTRACT="SignContract";
    public static final String BOOKROOM_PRICE="price";
    public static final String BOOKROOM_STAY="stay";
    public static final String BOOKROOM="BookRoom";
    public static final String CHARGEACCOUNT_HOTEL="hotel";
    public static final String CHARGEACCOUNT_AMOUNT="amount";
    public static final String CHARGEACCOUNT_DAY="day";
    public static final String CHARGEACCOUNT="ChargeAccount";
    public static final String REGISTRATIONREQUEST_HOTEL="hotel";
    public static final String REGISTRATIONREQUEST="RegistrationRequest";
    public static final String SUBSCRIBETODAYEVENT="SubscribeToDayEvent";
    public static final String NUMBEROFCLIENTSQUERYREF_DAY="day";
    public static final String NUMBEROFCLIENTSQUERYREF="NumberOfClientsQueryRef";
    public static final String ENDSIMULATION="EndSimulation";
    public static final String DAYEVENT_DAY="day";
    public static final String DAYEVENT="DayEvent";
    public static final String ACCOUNT_ID_ACCOUNT="id_account";
    public static final String ACCOUNT_HOTEL="hotel";
    public static final String ACCOUNT_BALANCE="balance";
    public static final String ACCOUNT="Account";
    public static final String PRICE_AMOUNT="amount";
    public static final String PRICE="Price";
    public static final String STAY_CHECKIN="checkIn";
    public static final String STAY_CHECKOUT="checkOut";
    public static final String STAY="Stay";
    public static final String CONTRACT_RECEPCIONIST_NOVICE="recepcionist_novice";
    public static final String CONTRACT_CHEF_1STARS="chef_1stars";
    public static final String CONTRACT_CHEF_2STARS="chef_2stars";
    public static final String CONTRACT_ROOM_SERVICE_STAFF="room_service_staff";
    public static final String CONTRACT_DAY="day";
    public static final String CONTRACT_RECEPCIONIST_EXPERIENCED="recepcionist_experienced";
    public static final String CONTRACT_CHEF_3STARS="chef_3stars";
    public static final String CONTRACT="Contract";
    public static final String HOTEL_HOTEL_NAME="hotel_name";
    public static final String HOTEL_HOTELAGENT="hotelAgent";
    public static final String HOTEL="Hotel";

  /**
   * Constructor
  */
  private SharedAgentsOntology(){ 
    super(ONTOLOGY_NAME, BasicOntology.getInstance());
    try { 

    // adding Concept(s)
    ConceptSchema hotelSchema = new ConceptSchema(HOTEL);
    add(hotelSchema, hotelmania.ontology.Hotel.class);
    ConceptSchema contractSchema = new ConceptSchema(CONTRACT);
    add(contractSchema, hotelmania.ontology.Contract.class);
    ConceptSchema staySchema = new ConceptSchema(STAY);
    add(staySchema, hotelmania.ontology.Stay.class);
    ConceptSchema priceSchema = new ConceptSchema(PRICE);
    add(priceSchema, hotelmania.ontology.Price.class);
    ConceptSchema accountSchema = new ConceptSchema(ACCOUNT);
    add(accountSchema, hotelmania.ontology.Account.class);
    ConceptSchema dayEventSchema = new ConceptSchema(DAYEVENT);
    add(dayEventSchema, hotelmania.ontology.DayEvent.class);

    // adding AgentAction(s)
    AgentActionSchema endSimulationSchema = new AgentActionSchema(ENDSIMULATION);
    add(endSimulationSchema, hotelmania.ontology.EndSimulation.class);
    AgentActionSchema numberOfClientsQueryRefSchema = new AgentActionSchema(NUMBEROFCLIENTSQUERYREF);
    add(numberOfClientsQueryRefSchema, hotelmania.ontology.NumberOfClientsQueryRef.class);
    AgentActionSchema subscribeToDayEventSchema = new AgentActionSchema(SUBSCRIBETODAYEVENT);
    add(subscribeToDayEventSchema, hotelmania.ontology.SubscribeToDayEvent.class);
    AgentActionSchema registrationRequestSchema = new AgentActionSchema(REGISTRATIONREQUEST);
    add(registrationRequestSchema, hotelmania.ontology.RegistrationRequest.class);
    AgentActionSchema chargeAccountSchema = new AgentActionSchema(CHARGEACCOUNT);
    add(chargeAccountSchema, hotelmania.ontology.ChargeAccount.class);
    AgentActionSchema bookRoomSchema = new AgentActionSchema(BOOKROOM);
    add(bookRoomSchema, hotelmania.ontology.BookRoom.class);
    AgentActionSchema signContractSchema = new AgentActionSchema(SIGNCONTRACT);
    add(signContractSchema, hotelmania.ontology.SignContract.class);
    AgentActionSchema createAccountRequestSchema = new AgentActionSchema(CREATEACCOUNTREQUEST);
    add(createAccountRequestSchema, hotelmania.ontology.CreateAccountRequest.class);
    AgentActionSchema accountStatusQueryRefSchema = new AgentActionSchema(ACCOUNTSTATUSQUERYREF);
    add(accountStatusQueryRefSchema, hotelmania.ontology.AccountStatusQueryRef.class);

    // adding AID(s)

    // adding Predicate(s)
    PredicateSchema notificationDayEventSchema = new PredicateSchema(NOTIFICATIONDAYEVENT);
    add(notificationDayEventSchema, hotelmania.ontology.NotificationDayEvent.class);
    PredicateSchema stayQueryRefSchema = new PredicateSchema(STAYQUERYREF);
    add(stayQueryRefSchema, hotelmania.ontology.StayQueryRef.class);
    PredicateSchema notificationEndSimulationSchema = new PredicateSchema(NOTIFICATIONENDSIMULATION);
    add(notificationEndSimulationSchema, hotelmania.ontology.NotificationEndSimulation.class);
    PredicateSchema hotelInformationSchema = new PredicateSchema(HOTELINFORMATION);
    add(hotelInformationSchema, hotelmania.ontology.HotelInformation.class);
    PredicateSchema accountStatusSchema = new PredicateSchema(ACCOUNTSTATUS);
    add(accountStatusSchema, hotelmania.ontology.AccountStatus.class);
    PredicateSchema numberOfClientsSchema = new PredicateSchema(NUMBEROFCLIENTS);
    add(numberOfClientsSchema, hotelmania.ontology.NumberOfClients.class);
    PredicateSchema bookingOfferSchema = new PredicateSchema(BOOKINGOFFER);
    add(bookingOfferSchema, hotelmania.ontology.BookingOffer.class);


    
    // adding fields
    hotelSchema.add(HOTEL_HOTELAGENT, (ConceptSchema)getSchema(BasicOntology.AID), ObjectSchema.OPTIONAL);
    hotelSchema.add(HOTEL_HOTEL_NAME, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
    contractSchema.add(CONTRACT_CHEF_3STARS, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    contractSchema.add(CONTRACT_RECEPCIONIST_EXPERIENCED, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    contractSchema.add(CONTRACT_DAY, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    contractSchema.add(CONTRACT_ROOM_SERVICE_STAFF, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    contractSchema.add(CONTRACT_CHEF_2STARS, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    contractSchema.add(CONTRACT_CHEF_1STARS, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    contractSchema.add(CONTRACT_RECEPCIONIST_NOVICE, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    staySchema.add(STAY_CHECKOUT, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    staySchema.add(STAY_CHECKIN, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    priceSchema.add(PRICE_AMOUNT, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.MANDATORY);
    accountSchema.add(ACCOUNT_BALANCE, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.MANDATORY);
    accountSchema.add(ACCOUNT_HOTEL, hotelSchema, ObjectSchema.OPTIONAL);
    accountSchema.add(ACCOUNT_ID_ACCOUNT, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    dayEventSchema.add(DAYEVENT_DAY, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    numberOfClientsQueryRefSchema.add(NUMBEROFCLIENTSQUERYREF_DAY, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    registrationRequestSchema.add(REGISTRATIONREQUEST_HOTEL, hotelSchema, ObjectSchema.OPTIONAL);
    chargeAccountSchema.add(CHARGEACCOUNT_DAY, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    chargeAccountSchema.add(CHARGEACCOUNT_AMOUNT, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.MANDATORY);
    chargeAccountSchema.add(CHARGEACCOUNT_HOTEL, hotelSchema, ObjectSchema.OPTIONAL);
    bookRoomSchema.add(BOOKROOM_STAY, staySchema, ObjectSchema.OPTIONAL);
    bookRoomSchema.add(BOOKROOM_PRICE, priceSchema, ObjectSchema.MANDATORY);
    signContractSchema.add(SIGNCONTRACT_HOTEL, hotelSchema, ObjectSchema.OPTIONAL);
    signContractSchema.add(SIGNCONTRACT_CONTRACT, contractSchema, ObjectSchema.MANDATORY);
    createAccountRequestSchema.add(CREATEACCOUNTREQUEST_HOTEL, hotelSchema, ObjectSchema.OPTIONAL);
    accountStatusQueryRefSchema.add(ACCOUNTSTATUSQUERYREF_ID_ACCOUNT, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    notificationDayEventSchema.add(NOTIFICATIONDAYEVENT_DAYEVENT, dayEventSchema, ObjectSchema.MANDATORY);
    stayQueryRefSchema.add(STAYQUERYREF_STAY, staySchema, ObjectSchema.OPTIONAL);
    hotelInformationSchema.add(HOTELINFORMATION_RATING, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.MANDATORY);
    hotelInformationSchema.add(HOTELINFORMATION_HOTEL, hotelSchema, ObjectSchema.OPTIONAL);
    accountStatusSchema.add(ACCOUNTSTATUS_ACCOUNT, accountSchema, ObjectSchema.MANDATORY);
    numberOfClientsSchema.add(NUMBEROFCLIENTS_NUM_CLIENTS, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    bookingOfferSchema.add(BOOKINGOFFER_ROOMPRICE, priceSchema, ObjectSchema.OPTIONAL);

    // adding name mappings

    // adding inheritance

   }catch (java.lang.Exception e) {e.printStackTrace();}
  }
  }
