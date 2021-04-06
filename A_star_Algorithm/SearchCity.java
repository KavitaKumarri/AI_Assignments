import java.util.*;
import java.lang.*;

public class SearchCity {
	String cityName;
	double longitude, latitude;
	HashMap<SearchCity, Double> cityRoads;
	HashMap<SearchCity, CityRoad> paths;
	public SearchCity(String cityname, double longitude, double latitude) {
		this.cityName = new String(cityName);
		this.latitude = latitude;
		this.longitude = longitude;
		this.cityRoads = new HashMap<SearchCity, Double>();
	}
	
	public boolean compareCities(SearchCity city) {
		return this.cityName.equals(city.cityName) && this.longitude == city.longitude && this.latitude == city.latitude;
	}
	
	
	public double heuristicFunction(SearchCity city) {
		double h_value;
		
		h_value = Math.sqrt(Math.pow((69.5*(this.longitude-city.longitude)),2)+(69.5 * Math.cos((this.latitude + city.latitude)/360 * Math.PI))*Math.pow((this.longitude-city.longitude),2));
		;

		return h_value * 100;
	}
	
	public void addRoad(SearchCity city, int distance) {
		@SuppressWarnings("deprecation")
		Double dist = new Double(distance);
		cityRoads.put(city, dist);
	}
	
	public ArrayList<SearchCity> getCities(){
		return new ArrayList<SearchCity>()
	}
}
