package hello.world;

public class Demo {
	public boolean method(String arg, boolean isCompatible) {
		if (isCompatible) {
			if (arg.equals("toto")) {
				return false;
			} else if (arg.equals("titi"))
				return true;
		}
		return false;

	}

}
