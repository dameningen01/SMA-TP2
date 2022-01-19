import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.*;
public class ProducerAgent extends Agent{
	HashMap<String,Double> products = new HashMap<>();
	public ProducerAgent() {
		products.put("PHONE", 5600.0);
		products.put("CAMERA", 2500.0);
		products.put("LAPTOP", 6590.0);
		products.put("PRINTER", 2648.0);
	}
	protected void setup() {
		System.out.println("Agent cree : "+ getLocalName());
        
		addBehaviour(new CyclicBehaviour() {
			@Override
			public void action() {
				ACLMessage message = receive();
				if(message != null) {
					System.out.println(message.getSender().getLocalName() +" => " +myAgent.getAID().getLocalName()+
					": "+message.getContent());
					//Replay with a PROPOSITION
					ACLMessage reply = message.createReply();
					reply.setPerformative(ACLMessage.PROPOSE);
					String productName = message.getContent();
					Double productPrice = products.get(productName);
					reply.setContent(productName+"\t Price : "+productPrice+" DHS");
					send(reply);
					}else { block();}
			}
		});
		
	}
	
}
