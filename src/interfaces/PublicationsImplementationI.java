package interfaces;

import annotations.Pre;

public interface PublicationsImplementationI {
	
	@Pre(expression = "m != null",args = {"x"})
	void publish(MessageI m, String topic) throws Exception;
	
	void publish(MessageI m, String[] topics) throws Exception;
	void publish(MessageI[] ms, String topic) throws Exception;
	void publish(MessageI[] ms, String[] topics) throws Exception;

}
