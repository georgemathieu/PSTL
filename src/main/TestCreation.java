package main;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Loader;
import javassist.NotFoundException;

public class TestCreation {
	
	public void creation(ClassPool pool) 
			throws NotFoundException, CannotCompileException, IOException,
			ClassNotFoundException, InstantiationException, IllegalAccessException, 
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		CtClass cc = pool.makeClass("bin.main.Point");
		
		/*CtClass[] param = new CtClass[1];
		param[0] = pool.get("java.lang.Integer");
		
		CtClass testVoid = pool.get("java.lang.Void");*/
		
		/*CtClass e = pool.get("java.lang.Exception");
		CtClass[] ex = new CtClass[1];
		ex[0] = e;*/
		
		// returnType, methodName, parameters, exceptions, body, declaring
		//CtMethod gMethod = CtNewMethod.make(null, "testAffichage", param, null, "{System.out.println(\"test\"); return new Integer(1);}",cc);
		//CtMethod gMethod = CtNewMethod.make(testVoid, "testAffichage", param, null, "{System.out.println(\"test\");}",cc);
		
		CtMethod gMethod = CtNewMethod.make("public void testAffichage(Integer i){System.out.println(\"test \" + i.toString());}",cc);
		cc.addMethod(gMethod);
		
		cc.writeFile(); // update
		System.out.println("end of creation");
	}
}
