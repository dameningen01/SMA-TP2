import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Consumer {
	
	/** - create an agent container: ConsumerContainer
	* - deploy an assistant agent "ConsumerAgent" on the ConsumerContainer
	**/
	public Consumer() {
		
		// set jade platform
		JadePlateform myPlatform = new JadePlateform();
		
		// create container
		
		ContainerController producerContainer = myPlatform.createAgentContainer("ProducerContainer","localhost");
		ContainerController consumerContainer = myPlatform.createAgentContainer("ConsumerContainer","localhost");
		
		try {
			
			//deploy a consumer agent on the consumer container
			
			AgentController producerAgentController =
					producerContainer.createNewAgent("ProducerAgent",
							ProducerAgent.class.getName(),new Object []{}) ; // pass what the consumer wants in Object[]
					producerAgentController.start();
			
			AgentController consumerAgentController =
					consumerContainer.createNewAgent("ConsumerAgent",
							ConsumerAgent.class.getName(),new Object []{"LAPTOP"}) ; // pass what the consumer wants in Object[]
			consumerAgentController.start();
			
		} catch (StaleProxyException e) { e.printStackTrace();}
	}
	
	public static void main(String[] args) { new Consumer(); }

}
