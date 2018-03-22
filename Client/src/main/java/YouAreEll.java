


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.*;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import org.apache.http.client.fluent.Request;

import java.io.IOException;

public class YouAreEll {

    YouAreEll() {
    }

    public static void main(String[] args) {
        YouAreEll urlhandler = new YouAreEll();
        System.out.println(urlhandler.MakeURLCall("/ids", "GET", ""));
        System.out.println(urlhandler.MakeURLCall("/messages", "GET", ""));

    }

    public String get_ids() {

        return MakeURLCall("/ids", "GET", "");
    }

    public String get_messages() {

        return MakeURLCall("/messages", "GET", "");
    }

    public String MakeURLCall(String mainurl, String method, String jpayload) {
        String magicURL = "http://zipcode.rocks:8085" + mainurl;
        GetRequest request = Unirest.get(magicURL);
        Id idTest = new Id("JoeyBiscuits", "JoeHendricks415");

        if (method.equals("GET")) {
            try {
                return request.asJson().getBody().toString();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        }
        else if (method.equals("POST")){
            try {
                return Unirest.post(magicURL).body(jpayload).asJson().getBody().toString();
            }
            catch(UnirestException e){
                e.printStackTrace();
            }
        }
        return "nada";

    }

}

