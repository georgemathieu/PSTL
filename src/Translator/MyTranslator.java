package Translator;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.NotFoundException;
import javassist.Translator;
import ports.PublisherPublicationOutboundPort;
import tests.Testing;

public class MyTranslator implements Translator {
	
	//The method onLoad() is called before javassist.Loader loads a class, onLoad() can modify the definition of the loaded class
	@Override
	public void onLoad(ClassPool pool, String classname) throws NotFoundException, CannotCompileException {
		if(PublisherPublicationOutboundPort.class.getCanonicalName().equals(classname)){
			try {
				Class<?> IR = PublisherPublicationOutboundPort.class.getInterfaces()[0];
				Class<?> outboundPort = PublisherPublicationOutboundPort.class; 
				Testing.op2(IR,outboundPort,pool);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//The method start() is called when this event listener is added to a javassist.Loader object by addTranslator()
	@Override
	public void start(ClassPool arg0) throws NotFoundException, CannotCompileException {
		// TODO Auto-generated method stub
	}
	
}
