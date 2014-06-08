package hotelmania.group3.hotel;

import hotelmania.ontology.Contract;

public class FinalStrategy implements IStrategy {
	
	private AgHotel3 hotel;
	
	private float currentPrice;
	private Contract currentContract;	
	
	public FinalStrategy(AgHotel3 hotel) {
		this.hotel = hotel;
		this.currentPrice = 60f;
		this.currentContract = new Contract();
		this.currentContract.setChef_1stars(1);
		this.currentContract.setChef_2stars(0);
		this.currentContract.setChef_3stars(0);
		this.currentContract.setRoom_service_staff(1);
		this.currentContract.setRecepcionist_novice(1);
		this.currentContract.setRecepcionist_experienced(0);
	}

	@Override
	public float getCurrentPrice() {
		if (hotel.currentDay < 2) {
			return currentPrice;
		}
		
		// See the booked rooms for the last three days
		float occupied = 0;
		for (int i = 1; i < 4; i++) {
			int day = hotel.currentDay - i;
			if (day < 1) {
				break;
			}
			int client = hotel.getNumberOfClients(day);
			occupied += ((float)client) / 6;
		}
		occupied = occupied / 3;
		if (occupied > 0.6f) {
			// Increase price
			currentPrice += 15f;
			System.out.println("Price increased to " + currentPrice);
		} else if (occupied < 0.3f) {
			// Decrease price
			currentPrice -= 15f;
			if (currentPrice < 30f) {
				currentPrice = 30f;
			}
			System.out.println("Price decreased to " + currentPrice);
		}
		
		return currentPrice;
	}

	@Override
	public Contract getNextContract() {
		int day = hotel.currentDay + 1;
		if (day >= 3) {
			int clients = hotel.getNumberOfClients(day);
			Contract newContract = new Contract();
			newContract.setChef_1stars(0);
			newContract.setChef_2stars(0);
			newContract.setChef_3stars(1);
			newContract.setRoom_service_staff(clients < 6 ? clients + 1 : clients);
			if (clients < 3) {
				newContract.setRecepcionist_novice(1);
				newContract.setRecepcionist_experienced(0);
			} else if (clients == 3) {
				newContract.setRecepcionist_novice(0);
				newContract.setRecepcionist_experienced(1);
			} else if (clients < 6) {
				newContract.setRecepcionist_novice(1);
				newContract.setRecepcionist_experienced(1);
			} else {
				newContract.setRecepcionist_novice(0);
				newContract.setRecepcionist_experienced(2);
			}
			
			if (clients > 0) {
				float avaragePrice = calculateContractPrice(newContract) / clients;
				if (avaragePrice > currentPrice) {
					currentPrice = avaragePrice;
				}
			}
		}
		
		Contract contract = new Contract();
		contract.setChef_1stars(this.currentContract.getChef_1stars());
		contract.setChef_2stars(this.currentContract.getChef_2stars());
		contract.setChef_3stars(this.currentContract.getChef_3stars());
		contract.setRoom_service_staff(this.currentContract.getRoom_service_staff());
		contract.setRecepcionist_novice(this.currentContract.getRecepcionist_novice());
		contract.setRecepcionist_experienced(this.currentContract.getRecepcionist_experienced());
		
		return contract;
	}

	private float calculateContractPrice(Contract contract) {
		float amount = contract.getChef_1stars() * 45 +
				   contract.getChef_2stars() * 58 +
				   contract.getChef_3stars() * 77 +
				   contract.getRecepcionist_novice() * 34 +
				   contract.getRecepcionist_experienced() * 44 +
				   contract.getRoom_service_staff() * 28;
		return amount;
	}
}
