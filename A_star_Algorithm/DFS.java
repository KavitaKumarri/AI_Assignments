import java.util.*;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;

public class DFS {
	public Map<String, ArrayList<String>> cityRoads;
	
	public DFS() {
		cityRoads = new HashMap<String, ArrayList<String>>();
		readCitiesData();
	}
	
	public void readCitiesData() {
		
		File file = new File("./roadsdata.txt");
		String inputLine ="";
		BufferedReader buffer = null;
		try {
			buffer = new BufferedReader(new FileReader("/Users/kavitakumari/eclipse-workspace/Assignment2/src/roadsdata.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			while((inputLine = buffer.readLine()) != null) {
				if(inputLine.length() == 0) {
					continue;
				}
				String[] split = inputLine.split(",");
				
				addRoads(split[0].trim(),split[1].trim());
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void addRoads(String sourceCity, String destinationCity) {
		
		if(!cityRoads.containsKey(sourceCity)) {
			ArrayList<String> cities = new ArrayList<String>();
			cityRoads.put(sourceCity, cities);
		}
		
		if(!cityRoads.containsKey((destinationCity))){
			ArrayList<String> cities = new ArrayList<String>();
			cityRoads.put(destinationCity,cities);
			
		}
		cityRoads.get(sourceCity).add(destinationCity);
		cityRoads.get(destinationCity).add(sourceCity);
		
	}
	
	
	public ArrayList<String> getNextNodes(String city){
		ArrayList<String> nextNodes = cityRoads.get(city);
		Collections.sort(nextNodes);
		return (ArrayList<String>) nextNodes.clone();
	}
	
	
	public HashMap<String, ArrayList<String>> dfsSearch(String sourceCity, String destinationCity){
		ArrayList<String> visitedNodes = new ArrayList<String>();
		ArrayList<String> path = new ArrayList<String>();
		
		HashMap<String, ArrayList<String>> temp = new HashMap<String, ArrayList<String>>();
		Stack<List<String>> stack = new Stack<List<String>>();
		
		path.add(sourceCity);
		stack.push(path);
		
		while(true) {
		//	System.out.println("I am creating issue");
			System.out.println(path);
			if(stack.isEmpty()) {
				temp.put("Visited Nodes", visitedNodes);
				temp.put("path", null);
				return temp;
			}
			path = (ArrayList)stack.pop();
			String end = path.get(path.size()-1);
			
			if(end.equals(destinationCity)) {
				temp.put("Visited Nodes", visitedNodes);
				temp.put("path", path);
				return temp;
			}
			if(!visitedNodes.contains(end)) {
				visitedNodes.add(end);
			}
			ArrayList<String> nextNodes = getNextNodes(end);
			for(int i = nextNodes.size()-1; i >= 0 ;i--) {
				String nextNode = nextNodes.get(i);
				if(visitedNodes.indexOf(nextNode) == -1) {
					List<String> list = (ArrayList) path.clone();
					list.add(nextNode);
					stack.push(list);
				}
			}
			
			
		}
		//return temp;
	}
	
	public void displayRoute(HashMap<String, ArrayList<String>> route) {
		
		ArrayList<String> temp = route.get("path");
		//System.out.println(temp.size());
		if(temp == null)
			return;
		System.out.println("Total no. of cities expanded  = " + route.get("Visited Nodes").size());
		
		System.out.println("Order of cities expanded by DFS : " + route.get("Visited Nodes"));
		
		System.out.println("Path from " + temp.get(0) + " from " + temp.get(temp.size()-1)+" :" );
		System.out.println();
		
		for(int i=0;i<temp.size();i++) {
			System.out.print(temp.get(i));
			if(i < temp.size()) {
				System.out.print(" -> ");
			}
		}
		
	
	}
	
	
	
}
