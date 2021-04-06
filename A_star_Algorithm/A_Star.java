import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileNotFoundException;

public class A_Star {
	
	public Map<String,SearchCity> map;
	
	public A_Star() {
		map = new HashMap<String, SearchCity>();
		readCitiesData();
	}
	
	public void readCitiesData() {
		String roadFile = "";
		String citiesFile = "";
		BufferedReader buffer = null;
		String inputLine = "";
		
		//read cities data and insert into map
		try {
			buffer = new BufferedReader(new FileReader(citiesFile));
			while((inputLine = buffer.readLine()) != null) {
				if(inputLine.length() == 0)
					continue;
				String[] split = inputLine.split(",");
				SearchCity city = new SearchCity(split[0].trim(),Double.parseDouble(split[1].trim()),Double.parseDouble(split[2].trim()));
				map.put(split[0].trim(), city);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		// read roads data - 
		
		try {
			buffer = new BufferedReader(new FileReader(roadFile));
			while((inputLine = buffer.readLine())!=null) {
				if(inputLine.length() == 0) {
					continue;
				}
				String[] split = inputLine.split(",");
				int distance = Integer.valueOf(split[2].trim());
				
				SearchCity source = map.get(split[0].trim());
				SearchCity destination = map.get(split[1].trim());
				map.put(split[0].trim(), source);
				map.put(split[1].trim(), destination);
				source.addRoad(destination, distance);
				destination.addRoad(source, distance);
				
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	public double getRoadCost(CityRoad road) {
		if(road == null)
			return 0;
		return road.h_value + road.f_value;
	}
	
	
	public Map<String, Object> a_star_algorithm(String s, String d){
		
		SearchCity source = map.get(s);
		SearchCity destination = map.get(d);
		
		ArrayList<String> visited = new ArrayList<String>();
		Map<String, Object> result = new HashMap<String, Object>();
		
		Comparator<CityRoad> comp = new Comparator<CityRoad>() {
			@Override
			public int compare(CityRoad r1, CityRoad r2) {
				double a = 0, b= 0;
				a = getRoadCost(r1);
				b = getRoadCost(r2);
				
				return (a>b) ? 1 : (a==b) ? 0 : -1;
			}
		};
		
		PriorityQueue<CityRoad> queue = new PriorityQueue<CityRoad>(map.size(),comp);
		
		CityRoad road = new CityRoad();
		road.paths.add(source);
		road.f_value = 0;
		road.h_value = source.heuristicFunction(destination);
		
		source.paths.put(destination, road);
		
		queue.add(road);
		
		
		while(!queue.isEmpty()) {
			road = queue.poll();
			SearchCity prev = road.paths.get(road.paths.size() - 1);
			if(prev.equals(destination)) {
				result.put("Expanded nodes", visited.clone());
				result.put("final path", road);
				break;
			}
			
			if(!visited.contains(prev.cityName)) {
				visited.add(prev.cityName);
			}
			else {
				SearchCity temp = road.paths.get(0);
				CityRoad oldPath = temp.paths.get(destination);
				
				if(getRoadCost(road) < getRoadCost(oldPath)) {
					queue.remove(oldPath);
					SearchCity city = road.paths.get(0);
					city.paths.put(destination, road);
				}
				else
					continue;
			}
			
			ArrayList<SearchCity> nextNodes = prev.getCities();
			
			for(int i=0;i<nextNodes.size();i++) {
				CityRoad tempRoad = new CityRoad(road);
				
			}
			
		}
	}
	
}









