/*****************************************************************
Agent Client demanding a painting service: AgClient.java

 *****************************************************************/

package hotelmania.group3;

import jade.core.Agent;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.DFService;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import jade.content.lang.Codec;
import jade.content.lang.Codec.*;
import jade.content.lang.sl.*;

import jade.content.*;
import jade.content.onto.*;
import jade.content.onto.basic.*;
//import javax.swing.Action;
import hotelmania.onto.*;

import java.util.Date;


/**
 * This agent has the following functionality: 
 * <ul>
 * <li> It register itself in the DF
 * <li> Asks the DF the name of new "painter" agents
 * <li> If there is any, it ask this agent for a service estimation;
 *      if not, waits and try it again later
 * <li> Waits for an answer to the request
 * <li> If the estimation provided is the best one among the received in a minute time,
 *      it sends an acceptation message to the painter; if not, sends one of rejection
 * </ul>
 * @author Ricardo Imbert, UPM
 * @version $Date: 2008/04/09 12:36:23 $ $Revision: 1.0 $
 **/


public class AgPlatfform3 extends Agent {



	}
