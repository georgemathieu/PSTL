package Translator;

import javassist.ClassPool;
import javassist.Loader;
import javassist.Translator;

public class Main {

	public static void main(String[] args) throws Throwable {
		 Translator t = new MyTranslator();
	     ClassPool pool = ClassPool.getDefault();
	     Loader cl = new Loader();
	     cl.addTranslator(pool, t);
	     cl.run("CVM", args);
	}

}
