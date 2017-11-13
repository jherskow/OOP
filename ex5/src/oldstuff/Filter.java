import java.io.File;

/**
 * Created by d on 5/24/17.
 */
public class Filter{
    Filter1 myFilter;
    public void temp(){
        Filter blah = new Filter();
        Filter1 greater_than = (file, val1, val2, val3, val4) -> file.getFreeSpace() > val1;
        Filter1 between = (file, val1, val2, val3, val4) -> file.getFreeSpace() > val1
                                                                && file.getFreeSpace() < val2;
        Filter1 name = (file, val1, val2, val3, val4) -> file.getName().equals(val3);
        Filter1 writeable = (file, val1, val2, val3, val4) -> file.canWrite();

        this.myFilter = name;

    }
    public boolean isIn(File file){
        boolean blah = true;
        return operate(file, 1.0,1.0,"1",true, myFilter);
    }
    private boolean operate(File file, double val1, double val2, String val3, boolean val4, Filter1 filter1){
        return filter1.operation(file, val1, val2, val3, val4);
    }

}
