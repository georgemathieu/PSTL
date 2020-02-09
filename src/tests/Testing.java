package tests;

import java.lang.reflect.Method;
import annotations.Post;
import annotations.Pre;
import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.interfaces.RequiredI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Loader;

public class Testing {

	/*public static void main(String[] args) throws Exception {
		//op(AdditionClientI.class,AdditionFournisseurI.class);
		op2(AdditionClientI.class,AdditionOutboundPort.class);
	}*/
	
	public static void op(Class<? extends RequiredI> IR, Class<? extends OfferedI> IF) throws NoSuchMethodException, SecurityException {

		Method[] methodsIR = IR.getDeclaredMethods();

		for (Method mIR : methodsIR) {
			Method mIF = IF.getMethod(mIR.getName(), mIR.getParameterTypes());
			
			Pre annotation = mIR.getAnnotation(Pre.class);
			Pre annotation2 = mIF.getAnnotation(Pre.class);
			
			if(annotation != null && annotation2 != null)
			System.out.println("Il faut verifier cette implication : ("+annotation.expression()
			+") => ("+annotation2.expression()+")");
			
			Post anno = mIR.getAnnotation(Post.class);
			Post anno2 = mIF.getAnnotation(Post.class);
			if(anno != null && anno2 != null)
			System.out.println("Il faut verifier cette implication : ("+anno2.value()
			+") => ("+anno.value()+")");
	
		}	
	}
	
	public static void op2(Class<? extends RequiredI> IR, Class<? extends AbstractOutboundPort> outboundPort) throws Exception {
		
		//verify that the outboundPort implements the required interface IR
		assert IR.isAssignableFrom(outboundPort);
		
		//get all methods of the required interface IR
		Method[] methodsIR = IR.getDeclaredMethods();

		//for each method of IR add contract verification code in the corresponding method of the OutboundPort
		for (Method mIR : methodsIR) {
			
			Pre annotation = mIR.getAnnotation(Pre.class);
			Method mOutboundPort = outboundPort.getMethod(mIR.getName(), mIR.getParameterTypes());

			String expression = annotation.expression();
			String[] args = annotation.args();
			
			for(int i = 0; i< args.length; i++)
			{
				expression = expression.replaceAll("\\b"+args[i]+"\\b", "\\$"+(i+1));
			}
			
			System.out.println("if !("+ expression +")"+" throw new Exception();");
			
			// Partie javassist
			
			ClassPool pool = ClassPool.getDefault();
			Loader cl = new Loader(pool);
			CtClass cc = pool.getCtClass(outboundPort.getCanonicalName());
			
			//CtClass[] param = new CtClass[2];
			//param[0] = pool.get("java.lang.Integer");
			//param[1] = pool.get("java.lang.Integer");
			
			CtMethod cm = cc.getDeclaredMethod(mOutboundPort.getName());
			cm.insertBefore("if !("+ expression +")"+" throw new Exception();");
			cc.writeFile();
			Class<?> c = cl.loadClass(outboundPort.getName());
			Object obj = c.newInstance();
			Method methode = c.getDeclaredMethod(mOutboundPort.getName(), Integer.class,Integer.class);
			methode.invoke(obj,new Integer(-5),new Integer(11));
			 
		}
	}
}
