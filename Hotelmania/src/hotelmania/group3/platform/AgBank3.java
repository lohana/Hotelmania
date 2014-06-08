
package hotelmania.group3.platform;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import jade.domain.FIPAAgentManagement.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.content.lang.Codec;
import jade.content.lang.sl.*;
import jade.content.onto.*;
import hotelmania.ontology.*;
import hotelmania.group3.platform.bank.behaviour.*;

@SuppressWarnings("serial")
public class AgBank3 extends DayDependentAgent{

	public Codec codec = new SLCodec();
	public Ontology ontology = SharedAgentsOntology.getInstance();
	public static final String CREATEACCOUNT_SERVICE = "CreateAccount";

	public static final String ACCOUNTSTATUS_SERVICE = "AccountStatus";
	public static final String CHARGE_ACCOUNT_SERVICE = "ChargeAcount";
	public static final String PAYMENTS = "ClientPayment";

	private HashMap<Integer, Account> accounts = new HashMap<Integer, Account>();
	private HashMap<String, Integer> id_accounts = new HashMap<String, Integer>();
	public ArrayList<Hotel> hotelsWithAccount = new ArrayList<Hotel>();
	private int nextId = 1;
	
	// Information about the report
	private ArrayList<HotelInformation> hotelsInformation = new ArrayList<HotelInformation>();
	private HashMap<String, ArrayList<String>> hotelsClients = new HashMap<String, ArrayList<String>>();

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
			ServiceDescription sdp = new ServiceDescription();
			sd.setName(this.getName()); 
			sd.setType(CREATEACCOUNT_SERVICE);
			dfd.addServices(sd);

			sdas.setName(this.getName()); 
			sdas.setType(ACCOUNTSTATUS_SERVICE);
			dfd.addServices(sdas);

			sdca.setName(this.getName()); 
			sdca.setType(CHARGE_ACCOUNT_SERVICE);
			dfd.addServices(sdca);
			
			sdp.setName(this.getName()); 
			sdp.setType(PAYMENTS);
			dfd.addServices(sdp);

			// Registers its description in the DF
			DFService.register(this, dfd);
			System.out.println(getLocalName()+": Bank CREATE ACCOUNT SERVICE is registered in the DF");
			System.out.println(getLocalName()+": Bank CREATE ACCOUNT STATUS SERVICE is registered in the DF");
			System.out.println(getLocalName()+": Bank CHARGE TO ACCOUNT SERVICE is registered in the DF");

			dfd = null;
			sd = null;
			sdas= null;
			sdca= null;
			
			Thread.sleep(5000);

		}catch (FIPAException | InterruptedException e){
			e.printStackTrace();
		}

		addBehaviour(new BankAccount_CreateAccountForHotel(this));
		addBehaviour(new BankAccount_SendAccountStatus(this));
		addBehaviour(new ChargeAccount_ChargeAccount(this));
		
		addDayBehaviour();
		
		addBehaviour(new HotelsInformation_ExpectHotelsInformation(this));
		
		addBehaviour(new MoneyTransfer_TransferMoneyFromClientToHotel(this));
		
		// EndSimulation Behaviors 
    	addBehaviour (new SimulationEnd_SubscribeForSimulationEnd(this));
    	addBehaviour (new SimulationEnd_ExpectSimulationEnd(this));
	}

	public Account getStatusForHotel(int account)
	{
		return accounts.get(account);
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
		hotelsClients.put(hotel.getHotel_name(), new ArrayList<String>());
		return result;
	}

	public boolean chargetoaccount(Hotel h, float amount, String payer){

		int id_account = id_accounts.get(h.getHotel_name());

		if(getStatusForHotel(id_account)  != null && amount>0){
			
			Account act = getStatusForHotel(id_account);
			act.setBalance(act.getBalance() - amount);		
			accounts.put(id_account, act);
			
			if (payer.contains("Client")) {
				hotelsClients.get(h.getHotel_name()).add(payer);
			}
			
			return true;
		}else {

			return false;
		}
	}
	
	public void addHotelInformation(HotelInformation info) {
		for (int i = 0; i < hotelsInformation.size(); i++) {
			if (hotelsInformation.get(i).getHotel().getHotel_name().equals(info.getHotel().getHotel_name())) {
				hotelsInformation.get(i).setRating(info.getRating());
				return;
			}
		}
		HotelInformation hi = new HotelInformation();
		hi.setRating(info.getRating());
		Hotel hotel = new Hotel();
		hotel.setHotel_name(info.getHotel().getHotel_name());
		hotel.setHotelAgent(info.getHotel().getHotelAgent());
		hi.setHotel(hotel);
		hotelsInformation.add(hi);
	}
	
	public void writeReport() {
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		Date today = Calendar.getInstance().getTime();        
		String reportDate = df.format(today);
		
		// Read settings
		int interval = 5;
		float clientBudget = 50.0f;
		float clientBudgetVariance = 25.0f;
		int lastDay = 30;
		int clientsPerDay = 10;
		try {
			Configuration configuration = Configuration.getInstance();
			interval = Integer.parseInt(configuration
					.getProperty(Configuration.DATE_LENGTH));
			clientBudget = Float.parseFloat(configuration
					.getProperty(Configuration.CLIENT_BUDGET));
			clientBudgetVariance = Float.parseFloat(configuration
					.getProperty(Configuration.BUDGET_VARIANCE));
			lastDay = Integer.parseInt(configuration
					.getProperty(Configuration.MAX_DAYS));
			clientsPerDay = Integer.parseInt(configuration
					.getProperty(Configuration.CLIENTS_PER_DAY));
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("EXCEPTION: Unable to read configuration file. Default values are used.");
		}

		try {
			PrintWriter writer = new PrintWriter("FinalReportPlatform3" + reportDate + ".txt");
			writer.println("Platform: Group 3");
			writer.println("Simulation period: " + lastDay);
			writer.println("Number of clients generated: " + (lastDay * clientsPerDay));
			writer.println("Client's budget range: " + (clientBudget - clientBudgetVariance) + " - " + (clientBudget + clientBudgetVariance) + " EUR");
			writer.println("Initial money for each hotel: 0 EUR");
			writer.println("Rooms in each hotel: 6");
			writer.println("Day duration: " + interval + " seconds");
			writer.println("Participants: " + this.hotelsWithAccount.size());
			writer.println("Status of the participants after the simulation:");
			for (Hotel hotel : this.hotelsWithAccount) {
				String hotelName = hotel.getHotel_name();
				writer.println();
				writer.println("\t" + hotelName);
				int id = id_accounts.get(hotelName);
				writer.println("\t\tMoney in the account: " + accounts.get(id).getBalance() + " EUR");
				writer.println("\t\tClients visited the hotel: " + hotelsClients.get(hotelName).size());
				writer.println("\t\tRate in Hotelmania: " + findRate(hotelName));
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private float findRate(String hotelName) {
		for (HotelInformation info : hotelsInformation) {
			if (info.getHotel().getHotel_name().equals(hotelName)) {
				return info.getRating();
			}
		}
		return 5;
	}

	@Override
	public void ChangesOnDayChange() {
		addBehaviour(new HotelsInformation_RequestHotelsInformation(this));
	}
}
