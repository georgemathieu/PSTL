package interfaces;

import annotations.Pre;
import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.interfaces.RequiredI;

public interface PublicationCI 
extends PublicationsImplementationI, RequiredI, OfferedI{
	
	@Pre(expression = "m != null",args = {"x"})
	void publish(MessageI m, String topic) throws Exception;
	
	void publish(MessageI m, String[] topics) throws Exception;
	void publish(MessageI[] ms, String topic) throws Exception;
	void publish(MessageI[] ms, String[] topics) throws Exception;


}
