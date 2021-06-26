
public class Word {
	String name;
	Object value;

	public Word(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	public String toString() {
		return name + ": " + value;
	}
}
