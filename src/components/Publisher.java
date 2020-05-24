package components;


import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.MessageI;
import ports.PublisherPublicationOutboundPort;

public class Publisher extends AbstractComponent{
	
	protected PublisherPublicationOutboundPort ppop;
	
	//protected String publisherPublicationOutboundPortURI;
	
	public Publisher(int nbThreads, int nbSchedulableThreads) {
		super(nbThreads, nbSchedulableThreads);
		// TODO Auto-generated constructor stub
	}
	
	public Publisher(String uri, 
			String publicationOutboundPortURI) throws Exception
	{
		super(uri, 0, 1) ;
		
		//Publish the reception port (an outbound port is always local)
		this.ppop = new PublisherPublicationOutboundPort(publicationOutboundPortURI, this);
		this.ppop.localPublishPort();
		
		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}
		
		this.tracer.setTitle("publisher") ;
		this.tracer.setRelativePosition(1, 0) ;
	}
	
	@Override
	public void start() throws ComponentStartException
	{
		try {
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	
	@Override
	public void execute() throws Exception{
		publish(null, "nothing");
	}
	
	public void publish(MessageI m, String topic) throws Exception {
		logMessage("Publishing message "+m);
		try {
			ppop.publish(m, topic);
		} catch (IllegalArgumentException e1) {
			System.out.print("exception généré");
		}
	}

}