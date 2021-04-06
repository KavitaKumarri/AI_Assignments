import java.util.*;


public class CityRoad {
	double f_value;
	double h_value;
	LinkedList<SearchCity> paths;
	
	
	public CityRoad() 
	{
		this.f_value = 0;
		this.h_value = 0;
		paths = new LinkedList<SearchCity>();
		
	}
	
	public CityRoad(CityRoad road) 
	{
		this.f_value = road.f_value;
		this.h_value = road.h_value;
		this.paths = new LinkedList<SearchCity>(road.paths);
	}
	
	public CityRoad(LinkedList<SearchCity> city, double f_value, double h_value) {
		this.f_value = 0;
		this.h_value = 0;
		this.paths = new LinkedList<SearchCity>(city);
		
	}
	
	public SearchCity returnSource() {
		return this.paths.get(0);
	}
	
	public SearchCity returnDestination() {
		return this.paths.get(this.paths.size()-1);
	}
	
	public void addCity(SearchCity city) {
		this.paths.add(city);
	}
	
	
	
	
}
