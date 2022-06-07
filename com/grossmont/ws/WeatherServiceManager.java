package com.grossmont.ws;

// Classes for reading web service.
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

// Classes for JSON conversion to java objects using Google's gson.
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class WeatherServiceManager{

    private Weather m_oWeather = null;

    private String m_sWeatherJson;

    // Gets the overall weather JSON string from the 3rd party web service.
    public void callWeatherWebService(String sCity){

    	String sServiceReturnJson = "";

    	try {

            // Call weather API.
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" +
                    sCity + "&appid=1868f2463a960613c0a78b66a99b5e5f&units=imperial");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String strTemp = "";
            while (null != (strTemp = br.readLine())) {
                    sServiceReturnJson += strTemp;
            }

            // sServiceReturnJson now looks something like this:
            /*
            {"coord":{"lon":-116.96,"lat":32.79},
            "weather":[{"id":802,"main":"Clouds","description":"scattered clouds","icon":"03n"}],
            "base":"cmc stations",
            "main":{"temp":62.65,"pressure":1007.4,"humidity":93,"temp_min":62.65,"temp_max":62.65,"sea_level":1028.19,"grnd_level":1007.4},
            "wind":{"speed":7.29,"deg":310.501},"clouds":{"all":32},"dt":1463026609,
            "sys":{"message":0.0078,"country":"US","sunrise":1463057430,"sunset":1463107097},
            "id":5345529,"name":"El Cajon","cod":200}
            */

            // *****************
            // UNCOMMENT THIS if you wish to print out raw json that came back from web service during testing.
            //System.out.println("Returned json:");
            //System.out.println(sServiceReturnJson);
            // *****************


        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("An error occurred in callWeatherWebService() in WeatherServiceManager: " + ex.toString());
        }

        m_sWeatherJson = sServiceReturnJson;

        // Turn raw json into java object heirarchy using Google's gson.
        convertJsonToJavaObject();
    }


	// Uses Google's gson library to convert json into filled java objects
	// using the java object hierarchy that you already created.
    private void convertJsonToJavaObject(){

        Gson gson = new GsonBuilder().create();

        m_oWeather = gson.fromJson(m_sWeatherJson, Weather.class);
    }


    // This uses Google's gson library for parsing json.
    public float getCurrentTemp(){

        return m_oWeather.main.temp;
    }


    public String getCityName(){

        return m_oWeather.name;
    }
    public float getHighTemp(){

        return m_oWeather.main.temp_max;
    }
    public float getLowTemp(){

        return m_oWeather.main.temp_min;
    }


    public static void main(String[] args) {

        WeatherServiceManager city1 = new WeatherServiceManager();
        WeatherServiceManager city2 = new WeatherServiceManager();

        Scanner input = new Scanner(System.in);
        System.out.print("Please enter a city's name: ");
        String nameCity1 = input.nextLine().replaceAll(" ","%20");
        System.out.print("Please enter another city's name: ");
        String nameCity2 = input.nextLine().replaceAll(" ","%20");

        city1.callWeatherWebService(nameCity1);
        city2.callWeatherWebService(nameCity2);

        if (city1.getCurrentTemp() > city2.getCurrentTemp()){
            System.out.println(city1.getCityName() + " has the highest current temperature.");
        }
        else{
            System.out.println(city2.getCityName() + " has the highest current temperature.");
        }

        float tempRange1 = city1.getHighTemp() - city1.getLowTemp();
        float tempRange2 = city2.getHighTemp() - city2.getLowTemp();
        if(tempRange1 > tempRange2){
            System.out.println(city1.getCityName() + " has the greatest range between low and high.");
        }
        else{
            System.out.println(city2.getCityName() + " has the greatest range between low and high.");
        }

    }

    
