import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


/**
 * First comes first served scheduler.
 */
public class FCFSScheduler implements Scheduler {

  @Override
  public void schedule(String inputFile, String outputFile) {

	  List<Integer> numbers = new ArrayList<>();
	  int lowestArrivalTime = 0;
	  int toProcessIndex = 0;
	  int totalTime = 0;
	  int waitTime = 0;
	  int averageWaitTime = 0;
	  int averageTurnaroundTime = 0;
	  int jobStart = 0;
	  int jobFinish = 0;
	  
	  // Here we add the numbers into a list in order to track process info
	  try {
		for(String line : Files.readAllLines(Paths.get(inputFile))) {
			  for(String part : line.split("\\s+")) {
				  Integer i = Integer.valueOf(part);
				  numbers.add(i);
			  }
		  }
	} catch (NumberFormatException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
	  
	  // Initialize lowest with the first PID

	  toProcessIndex = numbers.get(0);
	  
	  for(int j = 0; j < numbers.size()/3; j++) {
		  // Here we will begin the logic of choosing which process to finish
		  lowestArrivalTime = 1000;
		  for(int i = 1; i < numbers.size(); i = i + 3) {
			  if(numbers.get(i) < lowestArrivalTime && numbers.get(i) != -1) {
				  lowestArrivalTime = numbers.get(i);
				  toProcessIndex = i;
			  }
		  }
		  
		  // If the earliest arrival time is not zero then set the totalTime to arrivalTime
		  if(totalTime == 0 && numbers.get(toProcessIndex)+ 1 != 0) {
			  totalTime = numbers.get(toProcessIndex);
			  jobStart = totalTime;
			  jobFinish = totalTime +  numbers.get(toProcessIndex + 1);
		  }
		  // We now have the index of the Process with lowest Arrival time
		  else {
			  jobStart = totalTime;
			  jobFinish = totalTime + numbers.get(toProcessIndex + 1);
			  waitTime = totalTime - numbers.get(toProcessIndex);
			  totalTime += numbers.get(toProcessIndex + 1);
		  }
		  
		  
		  
		  if(waitTime < 0)
			  waitTime = 0;
		  totalTime = jobFinish;
		  System.out.println(numbers.get(toProcessIndex - 1) +" "+ jobFinish + " "
				  + waitTime + " " + (jobFinish - numbers.get(toProcessIndex)));
		  
		  averageWaitTime += jobStart - numbers.get(toProcessIndex);
		  averageTurnaroundTime += jobFinish - numbers.get(toProcessIndex);
		  
		  numbers.set(toProcessIndex, -1);
	  }
	  
	  System.out.println(averageWaitTime / (numbers.size()/3) + " " 
			  + averageTurnaroundTime / (numbers.size()/3));
	  
  }
}
