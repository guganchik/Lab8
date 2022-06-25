package data;

import java.io.Serializable;
import java.util.Date;

public class CollectionInfo implements Serializable {
    
    Class<?> type;
    Date date;
    int size;

    public CollectionInfo(Class<?> type, Date date, int size) {
        this.type = type;
        this.date = date;
        this.size = size;
    }

    public Class<?> getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

    public int getSize() {
        return size;
    }
    
    
    
}
