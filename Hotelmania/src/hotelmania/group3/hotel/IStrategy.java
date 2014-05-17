/**
 * Interface for hotel price strategy.
 * @author Eleonora Adova, EMSE
 * @version $Date: 2014/05/17 22:36:23 $ $Revision: 1.0 $
 **/
package hotelmania.group3.hotel;

import hotelmania.ontology.Contract;

public interface IStrategy {

	// Calculates the price for staying in the hotel based on the current state of the hotel.
	public float getCurrentPrice();
	
	// Returns proposal for contract for the next day.
	// The day of the contract is not set!
	public Contract getNextContract();
}
