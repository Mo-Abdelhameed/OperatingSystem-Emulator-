import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


public class Kernel {
	static Word[] memory = new Word[1024];
	static Queue<PCB> ready = new LinkedList<>();
	static PCB runningPCB;

	public static boolean isPcbAttribute(PCB pcb, int index) {
		return  index >= pcb.lowerBound && index <= pcb.lowerBound + 4;	
	}
	
	public static boolean isInstruction(PCB pcb) {
		return memory[pcb.pc].name.equals("instruction");	
	}
	
	public static boolean isVariable(PCB pcb, int index) {
		return  !isPcbAttribute(pcb, index) && !memory[index].name.equals("instruction");
	}
	
	public static boolean withinBounds(PCB pcb) {
		return pcb.pc <= pcb.upperBound && pcb.pc >= pcb.lowerBound;
	}
	
	public static void scheduler() throws Exception {
		while(!ready.isEmpty()) {
			PCB pcb = ready.poll();
			boolean finished = false;
			runningPCB = pcb;
			System.out.println("ID of running Program: " + runningPCB.id);
			Word quanta = memory[pcb.lowerBound+5];
			Word word = null;
			for(int i = 0 ; i < 2 ; i++) {
				if (withinBounds(pcb) && isInstruction(pcb)) {
					pcb.state = processState.RUNNING;
					word = memory[pcb.pc++];
					String lineOfCode = (String) word.value;
					MS1.interpreter(lineOfCode);
				} else {
					pcb.state = processState.FINISHED;
					System.out.println("ID of finished Program: " + runningPCB.id + " finished at quanta: " +  memory[pcb.lowerBound+5].value);
					runningPCB = null;
					break;
				}
			}

			if(pcb.state == processState.RUNNING && isInstruction(pcb) && withinBounds(pcb)){
				quanta.value = ((int)quanta.value)+1;
				pcb.state = processState.NOTRUNNING;
				runningPCB = null;
				ready.add(pcb);
			}
		}
	}
	public static void writeFile(String x, String y) throws Exception {
		PrintWriter pw = new PrintWriter(new File((String)readMemory(x)));
		Object s = readMemory(y);
		if(s==null)
			throw new Exception("There does not exist a variable " + y);
		pw.print(s);
		pw.flush();
		print("Data in " + y + " has been written to file (" + readMemory(x) + ") successfully!");
	}
	
	public static String readFile(String filePath) throws IOException{	
		
		BufferedReader br = new BufferedReader(new FileReader("./src/"+(String)readMemory(filePath)));
		String res = "";
		while(br.ready()) {
			res += br.readLine();
			if(br.ready())
				res += "\n";
		}
		br.close();
		return res;
	}
	
	public static String input() {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.println("Value:");
		String inp = sc.nextLine();
		return inp;		
	}

	public static void print(Object x) {
		if (x.equals("input"))
			System.out.println(input());
		else{
			Object s = readMemory((String) x);
			if(s != null)
				System.out.println(s);
			else
				System.out.println(x);
		}
	}

	public static void writeMemory(String name, Object value, int pc) {
		if(pc!=-1) {
			Word newWord = new Word (name, value);
			memory[pc]= newWord;
			System.out.println("Word: "+newWord+" is being written into index: "+ pc);
		}
		else {
			int index = runningPCB.lowerBound+6;
			while(memory[index]!=null && index <= runningPCB.upperBound) {
				if((memory[index].name).equals(name)) {
					System.out.println("Value of Word: "+memory[index].toString()+" is being changed to: "+value+" in index: "+ index);
					memory[index].value=value;
					return;
				}
				else
					index++;
			}
			memory[index] = new Word(name,value);
			System.out.println("Word: "+ memory[index].toString() + " is being written into index: "+ index);
		}
		
	}
	
	public static Object readMemory(String x) {
		for(int i=runningPCB.lowerBound+6; i <= runningPCB.upperBound && memory[i] != null ; i++) {
			if((memory[i].name).equals(x)) {
				System.out.println("Word: " + memory[i].toString()+ " is being read from index: " + i);
				return memory[i].value;
			}
		}
		return null;
	}
	
	
	public static void main(String[] args) {
		
		
		
	}
}




