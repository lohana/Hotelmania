/**
 * This agent launches all the agents that are inner for the platform.
 * @author Eleonora Adova, EMSE
 * @version $Date: 2014/04/27 12:28 $ $Revision: 1.0 $
 **/

package hotelmania.group3.platform;

import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

@SuppressWarnings("serial")
public class AgPlatform3 extends Agent {

	protected void setup(){
		try {
			startNewAgent("hotelmania.group3.platform.AgHotelmania3", "Hotelmania", new Object[]{});		
		} catch (StaleProxyException e) {
			e.printStackTrace();
			System.out.println("Unable to launch Agent Hotelmania.");
		}
		
		try {
			startNewAgent("hotelmania.group3.platform.AgBank3", "Bank", new Object[]{});	
		} catch (StaleProxyException e) {
			e.printStackTrace();
			System.out.println("Unable to launch Agent Bank.");
		}
		
		try {
			startNewAgent("hotelmania.group3.platform.AgAgency3", "Agency", new Object[]{});
		} catch (StaleProxyException e) {
			e.printStackTrace();
			System.out.println("Unable to launch Agent Agency.");
		}
		
		try {
			startNewAgent("hotelmania.group3.platform.AgSimulator3", "Simulator", new Object[]{});
		} catch (StaleProxyException e) {
			e.printStackTrace();
			System.out.println("Unable to launch Agent Simulator.");
		}
		
		try {
			startNewAgent("hotelmania.group3.platform.AgClient3", "Client0", new Object[]{ "Client0", 50f, 3, 2 });
		} catch (StaleProxyException e) {
			e.printStackTrace();
			System.out.println("Unable to launch agent Client.");
		}
		
		this.doDelete();
    }
    
    private void startNewAgent(String className,String agentName,Object[] arguments) throws StaleProxyException {
    	((AgentController)getContainerController().createNewAgent(agentName,className,arguments)).start();
    }
}
