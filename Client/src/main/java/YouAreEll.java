import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;


public class YouAreEll {

    YouAreEll() {
    }

    public static void main(String[] args) {
        ObjectMapper objMapper = new ObjectMapper();
        YouAreEll urlhandler = new YouAreEll();
        System.out.println(urlhandler.MakeURLCall("/ids", "GET", ""));
        System.out.println(urlhandler.MakeURLCall("/messages", "GET", ""));
//
//        Id joeyB = new Id("JoeyBiscuitsTest", "JoeHendricks415");
//        Message messageTest = new Message("JoeHendricks415", "wulawrence", "You are a good person and I hope that you win $50 dollars one day.");
//        try {
//            String message = objMapper.writeValueAsString(messageTest);
//            urlhandler.MakeURLCall("/ids/JoeHendricks415/messages", "POST", message);
//        }
//        catch(JsonProcessingException jpe){
//            System.out.println(jpe.getMessage());
//        }

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
        else if (method.equals("PUT")){
            try {
                return Unirest.put(magicURL).body(jpayload).asJson().getBody().toString();
            } catch (UnirestException e){
                e.printStackTrace();
            }
        }
        return "nada";
    }
}

