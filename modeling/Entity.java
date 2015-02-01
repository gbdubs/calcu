import java.util.HashMap;
import java.util.Map;


public class Entity {

	private Map<String, Long> mapping;
	private String uuid;
	
	public Entity(String uuid){
		this.uuid = uuid;
		mapping = new HashMap<String, Long>();
	}
	
	public void setProperty(String key, long value){
		mapping.put(key, value);
	}
	
	public long getProperty(String key){
		return mapping.get(key);
	}
}
