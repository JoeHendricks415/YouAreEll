import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SimpleShell {



    public static void prettyPrint(String output) {
        ObjectMapper pretty = new ObjectMapper();
        try {
            Object json = pretty.readValue(output, Object.class);
            String prettyString = pretty.writerWithDefaultPrettyPrinter().writeValueAsString(json);
            // yep, make an effort to format things nicely, eh?
            System.out.println(prettyString);
        } catch(IOException io){
            io.printStackTrace();
        }
    }
    public static void main(String[] args) throws java.io.IOException {

        YouAreEll webber = new YouAreEll();
        String commandLine;
        BufferedReader console = new BufferedReader
                (new InputStreamReader(System.in));

        ObjectMapper objMap = new ObjectMapper();
        ProcessBuilder pb = new ProcessBuilder();
        List<String> history = new ArrayList<String>();
        int index = 0;
        //we break out with <ctrl c>
        while (true) {
            //read what the user enters
            System.out.println("cmd? ");
            commandLine = console.readLine();

            //input parsed into array of strings(command and arguments)
            String[] commands = commandLine.split(" ");
            List<String> list = new ArrayList<String>();

            //if the user entered a return, just loop again
            if (commandLine.equals(""))
                continue;
            if (commandLine.equals("exit")) {
                System.out.println("bye!");
                break;
            }

            //loop through to see if parsing worked
            for (int i = 0; i < commands.length; i++) {
                //System.out.println(commands[i]); //***check to see if parsing/split worked***
                list.add(commands[i]);

            }
            System.out.print(list); //***check to see if list was added correctly***
            history.addAll(list);
            try {
                //display history of shell with index
                if (list.get(list.size() - 1).equals("history")) {
                    for (String s : history)
                        System.out.println((index++) + " " + s);
                    continue;
                }

                // Specific Commands.

                // ids
                if (list.contains("ids") && list.size() == 1) {
                    String results = webber.get_ids();
                    SimpleShell.prettyPrint(results);
                    continue;
                }

                // messages
                if (list.contains("messages") && list.size() == 1) {
                    String results = webber.get_messages();
                    SimpleShell.prettyPrint(results);
                    continue;
                }
                // you need to add a bunch more.
                if(list.contains("ids") && list.size() == 3){
                    String name = list.get(1);
                    String githubId = list.get(2);
                    Id newId = new Id(name, githubId);
                    String idInfo = objMap.writeValueAsString(newId);
                    webber.MakeURLCall("/ids", "POST", idInfo);
                    continue;
                }


                if(list.contains("messages") && list.size() == 2){
                    String result = webber.MakeURLCall("/ids/" + list.get(1) + "/messages", "GET", "");
                    SimpleShell.prettyPrint(result);
                    continue;
                }


                if(list.contains("send")){
                    ObjectMapper objMapper = new ObjectMapper();
                    YouAreEll urlhandler = new YouAreEll();

                    String groupedMessage = "";

                    if(list.get(list.size() - 2).equals("to")){

                        for(int i = 2; i < list.size() - 2; i++){
                            groupedMessage += list.get(i) + " ";
                        }

                        String resultWithTo = groupedMessage.trim();
                        Message messageTest = new Message(list.get(1), list.get(list.size() - 1), resultWithTo);

                        try {
                            String message = objMapper.writeValueAsString(messageTest);
                            urlhandler.MakeURLCall("/ids/" + list.get(1) + "/messages", "POST", message);
                        } catch(JsonProcessingException jpe){
                            System.out.println(jpe.getMessage());
                        }
                    }
                    else
                    {

                        for(int i = 2; i < list.size(); i++){
                            groupedMessage += list.get(i) + " ";
                        }

                        String resultWithoutTo = groupedMessage.trim();
                        Message messageTest = new Message(list.get(1),resultWithoutTo);

                        try {
                            String message = objMapper.writeValueAsString(messageTest);
                            urlhandler.MakeURLCall("/ids/" + list.get(1) + "/messages", "POST", message);
                        } catch(JsonProcessingException jpe){
                            System.out.println(jpe.getMessage());
                        }
                    }
                    String showResults = webber.get_messages();
                    SimpleShell.prettyPrint(showResults);
                    continue;
                }

                //!! command returns the last command in history
                if (list.get(list.size() - 1).equals("!!")) {
                    pb.command(history.get(history.size() - 2));

                }//!<integer value i> command
                else if (list.get(list.size() - 1).charAt(0) == '!') {
                    int b = Character.getNumericValue(list.get(list.size() - 1).charAt(1));
                    if (b <= history.size())//check if integer entered isn't bigger than history size
                        pb.command(history.get(b));
                } else {
                    pb.command(list);
                }

                // wait, wait, what curiousness is this?
                Process process = pb.start();

                //obtain the input stream
                InputStream is = process.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                //read output of the process
                String line;
                while ((line = br.readLine()) != null)
                    System.out.println(line);
                br.close();


            }

            //catch ioexception, output appropriate message, resume waiting for input
            catch (IOException e) {
                System.out.println("Input Error, Please try again!");
            }
            // So what, do you suppose, is the meaning of this comment?
            /** The steps are:
             * 1. parse the input to obtain the command and any parameters
             * 2. create a ProcessBuilder object
             * 3. start the process
             * 4. obtain the output stream
             * 5. output the contents returned by the command
             */

        }


    }

}