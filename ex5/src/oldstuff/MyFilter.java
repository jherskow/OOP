import java.io.File;

/**
 * Created by d on 5/24/17.
 */
public class MyFilter{
    String type;
    double num1;
    double num2;
    String name;
    boolean bool;
    boolean reverse;

    public MyFilter(String filter){
        String[] parts = type.split("#");
        this.type = parts[0];
    }

    public boolean apply(File file){
        if(type.equals("between")){
            return
        }
    }

    private boolean between(int num1, int num2){
        return
    }
}
