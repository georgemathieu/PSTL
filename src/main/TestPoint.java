package main;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Loader;
import javassist.NotFoundException;
import javassist.util.HotSwapper;

public class TestPoint {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, 
	IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NotFoundException, 
	CannotCompileException, IOException, IllegalConnectorArgumentsException {
		
		ClassPool pool = ClassPool.getDefault();
		Loader cl = new Loader(pool);
		
		TestCreation tc = new TestCreation();
		tc.creation(pool);
		
	    CtClass[] param = new CtClass[1];
		param[0] = pool.get("java.lang.Integer");
		
	    CtClass cc = pool.get("bin.main.Point");
	    cc.defrost(); // le writeFile dans testCreation lock la classe, defrost unlock
	    CtMethod cm = cc.getDeclaredMethod("testAffichage", param);
	    cm.insertBefore("System.out.println(\"TEST INSERT BEFORE\");");
	    
	    /*byte[] classFile = cc.toBytecode();
	    HotSwapper hs = new HotSwapper(8001);
	    hs.reload("bin.main.Point", classFile);*/
	    cc.writeFile(); // update de la methode testAffichage
	    
	    Class c = cl.loadClass("bin.main.Point");
		Object obj = c.newInstance();
	    Method methode = c.getDeclaredMethod("testAffichage", Integer.class);
	    
	    methode.invoke(obj, new Integer(10));
	}

}
