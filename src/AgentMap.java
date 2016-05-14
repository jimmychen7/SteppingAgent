
public class AgentMap {
	
	public AgentMap() {
		agentMap = new AgentElement[159][159];
		for(int i = 0; i < 159; i++)
			for(int j = 0; j < 159; j++)
				agentMap[j][i] = new AgentElement("unknown");
	}
	
	
	
	
	
	
	private AgentElement[][] agentMap;
}
