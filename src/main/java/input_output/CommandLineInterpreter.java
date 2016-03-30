package input_output;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CommandLineInterpreter {

    public static List<String> sendCommand(String command) {
        List<String> resultList = new ArrayList<>();
        Runtime rt = Runtime.getRuntime();
        try {
            Process process = rt.exec(command);
            String s;
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((s = reader.readLine()) != null) {
                if (s.length() > 0) {
                    resultList.add(s.trim());
                }
            }
            reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((s = reader.readLine()) != null) {
                System.out.print(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
