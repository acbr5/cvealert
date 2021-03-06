package com.v1.opencve.CVE;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONTokener;
import org.springframework.core.io.ClassPathResource;

import org.springframework.core.io.Resource;


public class GetCVE {
  /*  private double getWeatherOnJSON(){
        String API_KEY = "25e585073d9232f10fa66afa76d62fda";
        String LOCATION = "istanbul";
        String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + LOCATION + "&appid=" + API_KEY + "&units =imperial";

        double celcius = 0;
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            rd.close();

            JSONObject jsonObject = new JSONObject(result.toString());
            //System.out.println(jsonObject);
            JSONObject main = jsonObject.getJSONObject("main");
            double temp = main.getDouble("temp");

        } catch (IOException | JSONException e) {
            System.out.println(e.getMessage());
        }
        return celcius;
    }

    public static void main(String[] args) {
        GetCVE nesne = new GetCVE();

        nesne.getCVEfromJsons();

    }
    public void getCVEfromJsons() {
        try {
            JSONParser parser = new JSONParser();
            Resource resource = new ClassPathResource("/cve_jsons/nvdcve-1.1-2020.json");
            InputStream inputStream = resource.getInputStream();
            StringBuilder result = new StringBuilder();
            JSONTokener tokener = new JSONTokener(inputStream);
            JSONObject object = new JSONObject(tokener);
            System.out.println(object.getString("CVE_Items[0]"));
            System.out.println("cve  : " + object.getString("cve"));
            System.out.println("CVE_data_meta: " + object.getString("CVE_data_meta"));
            System.out.println("ID : " + object.getString("ID"));

            System.out.println("CVE: ");
            JSONArray courses = object.getJSONArray("cve");
            for (int i = 0; i < courses.length(); i++) {
                System.out.println("  - " + courses.get(i));
            }

           /* BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            rd.close();

            JSONObject jsonObject = new JSONObject(result.toString());
            System.out.println(jsonObject);
            String main = jsonObject.getString("CVE_Items");
            String cve = jsonObject.getString("cve");
            String temp = jsonObject.getString("CVE_data_meta");
            String id = jsonObject.getString("ID");
            System.out.println(id);
            */
       /* } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void readJsonFromFile() throws IOException, JSONException {
       /* JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader("C:/My Workspace/JSON Test/file.json"));

            JSONArray array = (JSONArray) obj;
            JSONObject jsonObject = (JSONObject) array.get(0);

            String name = (String) jsonObject.get("cve");
            System.out.println(name);

            String cve = (String) jsonObject.get("CVE_data_meta");
            System.out.println(cve);

            String id = (String) jsonObject.get("ID");
            System.out.println(id);

            // loop array
            JSONArray cves = (JSONArray) jsonObject.get("cve");
            Iterator<String> iterator = cves.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }*/
}

