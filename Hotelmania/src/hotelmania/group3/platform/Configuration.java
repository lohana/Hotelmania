/**
 * Class for reading the configuration file. 
 * @author Eleonora Adova, EMSE
 * @version $Date: 2014/05/03 19:28 $ $Revision: 1.0 $
 **/

package hotelmania.group3.platform;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Configuration {
 
    Properties properties = null;
 
    /** Configuration file name */
    public final static String CONFIG_FILE_NAME = "resources/settings.properties";
 
    /** Date length in seconds */
    public final static String DATE_LENGTH = "day.length";
    
    /** Number of days to simulate */
    public final static String MAX_DAYS = "simulation.days";
    
    /** The initial budget of a client */
    public final static String CLIENT_BUDGET = "client_budget";
    
    /** The variance that a client's budget may have */
    public final static String BUDGET_VARIANCE = "budget_variance";
    
    /** The number of clients to be generated every day */
    public final static String CLIENTS_PER_DAY = "clients_per_day";
 
    private Configuration() {
        this.properties = new Properties();
        try {
        	InputStream stream = Configuration.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
            properties.load(stream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
 
    public static Configuration getInstance() {
        return ConfigurationHolder.INSTANCE;
    }
 
    private static class ConfigurationHolder {

        private static final Configuration INSTANCE = new Configuration();
    }
 
    public String getProperty(String key) {
        return this.properties.getProperty(key);
    }
}
