package tests;

import java.lang.reflect.Method;
import annotations.Post;
import annotations.Pre;
import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.interfaces.RequiredI;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class Testing {

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
	
	public static void op2(Class<?> IR, Class<?> outboundPort, ClassPool pool) throws Exception {
		
		Method[] methodsIR = IR.getDeclaredMethods();

		for (Method mIR : methodsIR) {

			//Partie annotation
			if (mIR.getAnnotation(Pre.class) == null) continue;
			Pre annotation = mIR.getAnnotation(Pre.class);
			Method mOutboundPort = outboundPort.getMethod(mIR.getName(), mIR.getParameterTypes());

			String expression = annotation.expression();
			String[] args = annotation.args();

			for (int i = 0; i < args.length; i++) {
				expression = expression.replaceAll("\\b" + args[i] + "\\b", "\\$" + (i + 1));
			}

			// Partie javassist

			CtClass cc = pool.get(outboundPort.getCanonicalName());
			cc.defrost();

			Class<?>[] paramTypes = mOutboundPort.getParameterTypes();
			CtClass[] params = new CtClass[paramTypes.length];

			for (int i = 0; i < params.length; i++) {
				params[i] = pool.get(paramTypes[i].getName());
			}

			CtMethod cm = cc.getDeclaredMethod(mOutboundPort.getName(), params);
			
			// insérer le code au debut de la methode
			cm.insertBefore("if (!(" + expression + "))" + "throw new IllegalArgumentException();");
			cc.writeFile();
			
			//byte[] classFile = cc.toBytecode();
			//HotSwapper hs = new HotSwapper(8000);
			//hs.reload(cc.getName(), classFile);
		}
	}
}
