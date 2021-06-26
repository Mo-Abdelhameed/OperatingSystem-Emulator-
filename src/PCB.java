import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

enum processState {RUNNING, NOTRUNNING, FINISHED}

public class PCB {

	int id, pc, lowerBound, upperBound;
	processState state;
	
	public PCB (int lowerBound, int upperBound) {
		 this.pc = lowerBound+6;
		 this.lowerBound = lowerBound;
		 this.upperBound = upperBound;
			 
	 }
	
	public static void createPCBs() throws IOException {
		
		for(int i = 1; i<=3; i++) {
			String fileName = "Program "+ i+".txt";
			try {
				BufferedReader br = new BufferedReader(new FileReader("./src/"+fileName));
				//
				PCB pcb = new PCB ((i-1)*50, (i-1)*50+49); 
				//
				pcb.id = i;
				pcb.state = processState.NOTRUNNING;
				
				addPCBToMemory(pcb); 
				
				int pcCounter = 6;
				while(br.ready()) {
					String instruction = br.readLine();
					Kernel.writeMemory("instruction", instruction, pcb.lowerBound + pcCounter++);	
				}	
				br.close();	
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	public static void addPCBToMemory(PCB pcb) {
		Kernel.writeMemory("id", pcb.id, pcb.lowerBound);
		Kernel.writeMemory("pc", pcb.pc, pcb.lowerBound+1);
		Kernel.writeMemory("state", pcb.state, pcb.lowerBound+2);
		Kernel.writeMemory("lowerBound", pcb.lowerBound, pcb.lowerBound+3);
		Kernel.writeMemory("upperBound", pcb.upperBound, pcb.lowerBound+4);
		Kernel.writeMemory("quanta", 1, pcb.lowerBound+5);

		Kernel.ready.add(pcb);
	}
	
	
	
	
	public static void main(String[] args) throws Exception {
		createPCBs();
		Kernel.scheduler();
		System.out.println(Arrays.toString(Kernel.memory));
	}
	
}

