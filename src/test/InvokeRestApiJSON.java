package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;

import pojo.MyPOJO;

public class InvokeRestApiJSON {
	public static void main(String[] args) {

		try {
			//ENTER ACCESS KEY
			String open_weather_access_key = "";
			String location = "London";

			String urlString = "https://openweathermap.org/data/2.5/weather?q=" + location + "&appid=" + open_weather_access_key;

			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setRequestProperty("Accept-Language", "en");

			conn.connect();

			System.out.println("API Response recevied : " + conn.getResponseCode());

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : http error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			StringBuilder sb = new StringBuilder();
			String s = null;
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}

			String sb_to_string = sb.toString();
			System.out.println("######## API Response \n" + sb);

			ObjectMapper mapper = new ObjectMapper();
			//ReadFile takes input in String or File as JSON
			MyPOJO mypojo = mapper.readValue(sb_to_string, MyPOJO.class);
			
			// Formatted JSON
			System.out.println(" ###### Formatted JSON response ####### ");
			System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mypojo));
			
			// Output Weather Info
			System.out.println("###### Weather Information for : " + mypojo.getName() + " ############");
			System.out.println("Current Temprature : " + mypojo.getMain().getTemp());
			System.out.println("Weather : " + mypojo.getWeather()[0].getMain());
			System.out.println("Wind Speed : " + mypojo.getWind().getSpeed());

			conn.disconnect();

		} catch (MalformedURLException e) {

		} catch (IOException e) {
		}
	}
}
