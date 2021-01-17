
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class StringConstant {
    private static final File file=new File("src/language/English");
    static final HashMap<NameConstant, String> dictionary;
    static {
        dictionary=new HashMap<>();
        try(BufferedReader bufferedReader=new BufferedReader(new FileReader(file))){
            String readIn;
            int i=0;
            for (;(readIn=bufferedReader.readLine())!=null;i++){
                if(i>= NameConstant.values().length){
                    System.err.println("==String Constant fail to load on file"+file.getName());
                    System.exit(1);
                }
                dictionary.put(NameConstant.values()[i],readIn);
            }
            if(i!=NameConstant.values().length){
                System.err.println("==String Constant fail to load on file"+file.getName());
                System.exit(1);
            }
        }catch (IOException e){
            System.err.println("==File not Found!");
            e.printStackTrace();
            System.exit(1);
        }
    }

}
enum NameConstant{
    Defend,Gain
}

