
public class AgentMap {
	public static final int mapSize = 160;
	
	public AgentMap() {
		agentMap = new AgentElement[mapSize][mapSize];
		for(int i = 0; i < mapSize; i++)
			for(int j = 0; j < mapSize; j++)
				agentMap[j][i] = new AgentElement("unknown");
	}
	
	
	
	
	
	
	private AgentElement[][] agentMap;
}
