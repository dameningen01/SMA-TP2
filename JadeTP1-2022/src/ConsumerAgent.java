import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.*;
import jade.wrapper.ControllerException;

public class ConsumerAgent extends Agent{
	
	String productName = null;
	private ParallelBehaviour parBehav = new ParallelBehaviour();
	
	//cycle de vie de l’agent
    protected void setup() {
        
    	
        System.out.println("Agent cree : "+ getLocalName());
        
        Object[] args = this.getArguments(); // list of products 
        
        if(args.length == 1) { productName = (String) args[0]; }
        
        System.out.println("Argument retrieved : "+productName);
        this.addBehaviour(this.parBehav);
        this.doMission();
        this.lookForSeller();
        this.finish();
    }

    protected void takeDown() {
        System.out.println("Agent detruit : "+ getLocalName());
    }

    protected void beforeMove() { 
        //au niveau du container de départ
        try {
            System.out.println("Agent (" + getLocalName()+") part de : "+ getContainerController().getContainerName());
        } catch (ControllerException e) {
            e.printStackTrace();
        }

    }

    protected void afterMove() {
        // au niveau du container d’arrivée
        try {
        System.out.println("Agent (" + getLocalName()+") arrive à : "+ getContainerController().getContainerName());
        } catch (ControllerException e) {
            e.printStackTrace();
        } 
    }
    
    public void doDelete() {
    	System.out.println("On Essaye de supprimer l'Agent: "+ getLocalName());
    	super.doDelete();
    }
    
    public void lookForSeller() {
    	var searchBehaviour = new TickerBehaviour(this,2000) {
    		//keeps on ticking till the timeout is over no matter what
    		//cyclic on the other hand stops once a certain condition is met using block();
			@Override
			protected void onTick() {
				System.out.println("I'm looking for a Seller. tick="+this.getTickCount());
				ACLMessage message = receive();
				if(message != null) {
					System.out.println(message.getSender().getLocalName() +" => " +myAgent.getAID().getLocalName()+
					": "+message.getContent());
				}
			}
    	};
    	this.parBehav.addSubBehaviour(searchBehaviour);
    }
    public void doMission() {
    	var mission = new OneShotBehaviour() {
			@Override
			public void action() {
				System.out.println(myAgent.getAID().getLocalName() + ": my mission => Buying a "
	    				+ productName +" for the consumer.");
				
				ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
				AID receiver = new AID("ProducerAgent",AID.ISLOCALNAME);
				request.addReceiver(receiver);
				request.setContent(productName);
				send(request);
			}
    	};
    	this.parBehav.addSubBehaviour(mission);
    }
    public void finish() {
    	var finish = new WakerBehaviour(this,10000) {
    		protected void onWake() {
    			System.out.println(myAgent.getAID().getLocalName() + ": Bye...");
    			myAgent.doDelete();
    		}
    	};
    	this.parBehav .addSubBehaviour(finish);
    }

    
}
