/**
 * Simple strategy with fixed values
 * @author Eleonora Adova, EMSE
 * @version $Date: 2014/05/17 22:36:23 $ $Revision: 1.0 $
 * 
 **/

package hotelmania.group3.hotel;

import hotelmania.ontology.Contract;

public class SimpleFixedStrategy implements IStrategy {

	@Override
	public float getCurrentPrice() {
		return 10;
	}

	@Override
	public Contract getNextContract() {
		Contract contract = new Contract();
		contract.setChef_3stars(1);
		contract.setRoom_service_staff(6);
		contract.setRecepcionist_novice(0);
		contract.setRecepcionist_experienced(2);
		
		return contract;
	}
}
