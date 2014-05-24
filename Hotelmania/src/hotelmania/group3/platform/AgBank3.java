
package hotelmania.group3.platform;

import java.util.ArrayList;

import jade.domain.FIPAAgentManagement.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.util.leap.HashMap;
import jade.util.leap.Map;
import jade.content.lang.Codec;
import jade.content.lang.sl.*;
import jade.content.onto.*;
import jade.core.Agent;
import hotelmania.ontology.*;
 
import hotelmania.group3.platform.bank.behaviour.*;

@SuppressWarnings("serial")
public class AgBank3 extends Agent{

	public Codec codec = new SLCodec();
	public Ontology ontology = SharedAgentsOntology.getInstance();
	public static final String CREATEACCOUNT_SERVICE = "CreateAccount";

	public static final String ACCOUNTSTATUS_SERVICE = "AccountStatus";
	public static final String CHARGE_ACCOUNT_SERVICE = "ChargeAcount";


	private Map accounts = new HashMap();
	private Map id_accounts = new HashMap();
	public ArrayList<Hotel> hotelsWithAccount = new ArrayList<Hotel>();
	private int nextId = 1;

	protected void setup() {
		System.out.println(getLocalName() + ": Bank3 has entered into the system");
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);

		try{
			// Creates its own description
			DFAgentDescription dfd = new DFAgentDescription();
			// Add registration service
			ServiceDescription sd = new ServiceDescription();
			ServiceDescription sdas = new ServiceDescription();
			ServiceDescription sdca = new ServiceDescription();
			sd.setName(this.getName()); 
			sd.setType(CREATEACCOUNT_SERVICE);
			dfd.addServices(sd);




			sdas.setName(this.getName()); 
			sdas.setType(ACCOUNTSTATUS_SERVICE);
			dfd.addServices(sdas);

			sdca.setName(this.getName()); 
			sdca.setType(CHARGE_ACCOUNT_SERVICE);
			dfd.addServices(sdca);


			// Registers its description in the DF
			DFService.register(this, dfd);
			System.out.println(getLocalName()+": Bank CREATE ACCOUNT SERVICE is registered in the DF");
			System.out.println(getLocalName()+": Bank CREATE ACCOUNT STATUS SERVICE is registered in the DF");
			System.out.println(getLocalName()+": Bank CHARGE TO ACCOUNT SERVICE is registered in the DF");



			dfd = null;
			sd = null;
			sdas= null;
			sdca= null;
			//doWait(10000);

		}catch (FIPAException e){
			e.printStackTrace();
		}

		addBehaviour(new CreateAccountForHotel(this));
		addBehaviour(new SendAccountStatus(this));
		addBehaviour(new ChargetoAccount(this));
		
		// EndSimulation Behaviors 
    	addBehaviour (new SubscribeForEndSimulation(this));
    	addBehaviour (new SubscribeFrEndSimulation_ExpectforMessages(this));
		
	}
 
    

	public Account getStatusForHotel(int account)
	{
		return (Account)(accounts.get(account));

	}



	public int createAccount(Hotel hotel)
	{
		hotelsWithAccount.add(hotel);
		int result = nextId;
		nextId++;
		Account newAccount = new Account();
		newAccount.setBalance(0);
		newAccount.setHotel(hotel);
		newAccount.setId_account(result);
		accounts.put(result, newAccount);
		id_accounts.put(hotel.getHotel_name(), result);
		return result;
	}

	public boolean chargetoaccount(Hotel h,float amount){
		
 		
		
		int id_account = (int)id_accounts.get(h.getHotel_name());

		if(getStatusForHotel(id_account)  != null && amount>0){
			
			Account act = getStatusForHotel(id_account);
			act.setBalance(act.getBalance()-amount);		
			accounts.put(id_account, act);

			
			return true;
		}else {

			return false;
		}

	}
	
	
	 
}
