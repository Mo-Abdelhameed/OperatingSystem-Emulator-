import java.io.*;
import java.util.*;

public class MS1 {

	static ArrayList<String> ins = new ArrayList<>();

	@SuppressWarnings("resource")
	public static void interpreter(String instruction) throws Exception {
		StringTokenizer st = new StringTokenizer(instruction);

		while (st.hasMoreTokens())
			ins.add(st.nextToken());			

		if (ins.size() == 1)
			throw new Exception("Invalid instruction");
		if(ins.isEmpty())
			return;

		switch (ins.get(0)) {
		case "print":
			switch (ins.get(1)) {
			case "readFile":
				if (ins.size() > 2)
					Kernel.print(Kernel.readFile(ins.get(2)));
				else
					throw new Exception("Invalid instruction. Missing parameters!");
				break;
			case "add":
				if (ins.size() > 3) {
					add(ins.get(2), ins.get(3));
					Kernel.print((Kernel.readFile(ins.get(2))).toString());
				} else
					throw new Exception("Invalid instruction. Missing parameters!");
				break;
			default:
				Kernel.print(ins.get(1));
			}
			break;

		case "assign":
			if (ins.size() < 3)
				throw new Exception("Invalid instruction. Missing parameters!");

			if (ins.get(1).equals("readFile")) {
				if (ins.size() > 4 && ins.get(3).equals("readFile"))
					assign(Kernel.readFile(ins.get(2)), Kernel.readFile(ins.get(4)));
				else if (ins.size() == 4)
					assign(Kernel.readFile(ins.get(2)), ins.get(3));
				else
					throw new Exception("Invalid instruction. Missing parameters!");
			} else {
				if (ins.size() > 3 && ins.get(2).equals("readFile"))
					assign(ins.get(1), Kernel.readFile(ins.get(3)));
				else if (ins.size() == 3)
					assign(ins.get(1), ins.get(2));
				else
					throw new Exception("Invalid instruction. Missing parameters!");
			}
			break;

		case "add":
			String first;
			String second;
			try {
				first = ins.get(1);
				second = ins.get(2);
			} catch (Exception e) {
				throw new Exception("Invalid instruction. Missing parameters!");

			}
			add(first, second);
			break;

		case "readFile":
			if (ins.get(1).equals("readFile")) {
				if (ins.size() > 2)
					Kernel.readFile(Kernel.readFile(ins.get(2)));
				else
					throw new Exception("Invalid instruction. Missing parameters!");
			} else
				Kernel.readFile(ins.get(1));
			break;

		case "writeFile":
			if (ins.size() < 3) {
				throw new Exception("Invalid instruction. Missing parameters!");
			}
			if (ins.size() > 3 && ins.get(1).equals("readFile")) {
				if (ins.get(3).equals("readFile")) {
					if (ins.size() > 4)
						Kernel.writeFile(Kernel.readFile(ins.get(2)), Kernel.readFile(ins.get(4)));
					else
						throw new Exception("Invalid instruction. Missing parameters!");
				} else if (ins.get(3).equals("add")) {
					if (ins.size() > 5) {
						add(ins.get(4), ins.get(5));
						Kernel.writeFile(Kernel.readFile(ins.get(2)), (Kernel.readMemory(ins.get(4))).toString());
					} else
						throw new Exception("Invalid instruction. Missing parameters!");
				} else
					Kernel.writeFile(Kernel.readFile(ins.get(2)), ins.get(3));
			} else {
				if (ins.get(2).equals("readFile")) {
					if (ins.size() > 3)
						Kernel.writeFile(ins.get(1), Kernel.readFile(ins.get(3)));
					else
						throw new Exception("Invalid instruction. Missing parameters!");
				}

				else if (ins.get(2).equals("add")) {
					if (ins.size() > 4) {
						add(ins.get(3), ins.get(4));
						Kernel.writeFile(ins.get(1), (Kernel.readMemory(ins.get(3))).toString());
					} else
						throw new Exception("Invalid instruction. Missing parameters!");
				} else
					Kernel.writeFile(ins.get(1), ins.get(2));
			}
			break;
		default:
			throw new Exception("Invalid instruction. Missing parameters!");
		}
		ins.clear();

	}

	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			Integer.parseInt(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static void assign(String x, String y) throws Exception {

		if (y == null)
			throw new Exception("Cannot assign variable to null");

		if (y.equals("input"))
			y = Kernel.input();
		if (x.equals("input"))
			x = Kernel.input();
		int valueInt;
		boolean isInt = isNumeric(y);

		if (isInt) {
			valueInt = Integer.parseInt(y);
		    Kernel.writeMemory(x, valueInt, -1);
		} else {
			Kernel.writeMemory(x, y,-1);
		}

	}

	public static void add(String first, String second) throws Exception {

		if (Kernel.readMemory(first) == null)
			throw new Exception("There does not exist a variable " + first);

		if (Kernel.readMemory(second) == null)
			throw new Exception("There does not exist a variable " + second);

		double firstNum = 0;
		double secondNum = 0;

		boolean isInteger = Kernel.readMemory(first) instanceof Integer;
		boolean isDouble = Kernel.readMemory(first) instanceof Double;

		if (isInteger)
			firstNum = (int) Kernel.readMemory(first);

		else if (isDouble)
			firstNum = (double) Kernel.readMemory(first);

		else
			throw new Exception("The first parameter is not an integer!");

		isInteger = Kernel.readMemory(second) instanceof Integer;
		isDouble = Kernel.readMemory(second) instanceof Double;

		if (isInteger)
			secondNum = (int) Kernel.readMemory(second);

		else if (isDouble)
			secondNum = (double) Kernel.readMemory(second);

		else
			throw new Exception("The second parameter is not an integer!");

		double result = firstNum + secondNum;

		if (result == (int) result)
			Kernel.writeMemory(first, (int) result,-1);

		else
	    	Kernel.writeMemory(first, result,-1);
	}

	public static void main(String[] args) throws Exception {
		
	}

}