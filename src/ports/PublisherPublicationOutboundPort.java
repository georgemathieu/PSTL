package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.MessageI;
import interfaces.PublicationCI;

public class PublisherPublicationOutboundPort 
extends AbstractOutboundPort 
implements PublicationCI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PublisherPublicationOutboundPort(ComponentI owner) throws Exception {
		super(PublicationCI.class, owner);
	}
	
	public PublisherPublicationOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, PublicationCI.class, owner);
	}

	@Override
	public void publish(MessageI m, String topic) throws Exception {
		((PublicationCI)this.connector).publish(m, topic);
	}

	@Override
	public void publish(MessageI m, String[] topics) throws Exception {
		((PublicationCI)this.connector).publish(m, topics);
	}

	@Override
	public void publish(MessageI[] ms, String topic) throws Exception {
		((PublicationCI)this.connector).publish(ms, topic);
	}

	@Override
	public void publish(MessageI[] ms, String[] topics) throws Exception {
		((PublicationCI)this.connector).publish(ms, topics);
	}
	
	

}
