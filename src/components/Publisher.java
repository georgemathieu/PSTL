package components;

import java.awt.event.FocusAdapter;
import java.lang.annotation.Annotation;

import com.sun.org.apache.bcel.internal.classfile.Method;

import annotations.Pre;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.interfaces.RequiredI;
import interfaces.MessageI;
import interfaces.PublicationCI;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.util.HotSwapper;
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
		ClassPool pool = ClassPool.getDefault();
		try {
			String pathInterface = ppop.getImplementedInterface().getCanonicalName();
			CtClass classWithAnnotations = pool.get(pathInterface);
			classWithAnnotations.defrost();
			
			String pathOutboundPort = ppop.getClass().getCanonicalName();
			CtClass classOutboundPort = pool.get(pathOutboundPort);
			classOutboundPort.defrost();
			
			CtClass[] param = new CtClass[2];
			param[0] = pool.get("interfaces.MessageI");
			param[1] = pool.get("java.lang.String");
			
			CtMethod methodOutboundPort = classOutboundPort.getDeclaredMethod("publish", param);
			CtMethod methodWithAnnotations = classWithAnnotations.getDeclaredMethod("publish", param);
			
			methodOutboundPort.insertBefore("System.out.println(" + ((Pre)methodWithAnnotations.getAnnotation(Pre.class)).expression() + ");");
			
			classOutboundPort.writeFile();
			
			byte[] classFile = classOutboundPort.toBytecode();
		    HotSwapper hs = new HotSwapper(8000);
		    hs.reload(ppop.getClass().getCanonicalName(), classFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void execute() throws Exception{
		publish(new Message("Hello bro"), "nothing");
	}
	
	public void publish(MessageI m, String topic) throws Exception {
		logMessage("Publishing message "+m);

		ppop.publish(m, topic);

	}

}
