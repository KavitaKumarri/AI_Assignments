import java.util.*;

public class Output {

	
	public static void main(String args[]) 
	{
		String search = "DFS"; //args[0];
		String sourceCity = "vancouver"; //args[1];
		String destinationCity = "Medford"; //args[2];
		
		DFS dfs = new DFS();
		HashMap<String, ArrayList<String>> route = new HashMap<String, ArrayList<String>>();
		
		if(search.equals("DFS")) 
        {
			route = dfs.dfsSearch(sourceCity, destinationCity);
			dfs.displayRoute(route);
		}
		System.out.println("i just finished");
		
	}
}
