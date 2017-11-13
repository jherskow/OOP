import java.io.File;

/**
 * Created by d on 5/24/17.
 */
public class Command {

    File[] applyFilter(String filter, File[] files){
        File[] endList = new File[files.length];
        int i = 0;
        for(File file : files){
            if(applyOneFilter(filter, file)){
                endList[i] = file;
                i++;
            }
        }
        return endList;
    }

    boolean applyOneFilter(String filter, File file){
        String[] parts = filter.split("#");
        String operation = parts[0];
        if(operation.equals("greater_than")) {
            return file.getFreeSpace() > Double.parseDouble(parts[1]);
        }
        if(operation.equals("smaller_than")){
            return file.getFreeSpace() < Double.parseDouble(parts[1]);
        }
        if(operation.equals("between")){
            return file.getFreeSpace() < Double.parseDouble(parts[1]) &&
                   file.getFreeSpace() > Double.parseDouble(parts[2]);
        }
        return false;
    }
}
