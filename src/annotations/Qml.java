package annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Qml {
	String dim();
	String code();
	String constraint();
}

/* exemple d'utilisation sur une méthode : 
 * @Qml(dim = "DureeExecution"
		code = "..."
		constraint = "DureeExecution < 10 "
	int add (int x,int y) throws Exception
*/