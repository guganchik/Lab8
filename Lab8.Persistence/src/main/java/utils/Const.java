package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *  Константы
 */
public class Const {
    
    public static final int         SUCCESS                         = 0;
    public static final int         ERROR                           = 1;
    
    public static DateFormat        timeFormat                      = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final String      DB_URL                          = "jdbc:postgresql://127.0.0.1:5432/studs";
    //public static final String      DB_URL                          = "jdbc:postgresql://pg:5432/studs";
    
    public static final String      DB_USER                         = "s335083";

    public static final int         ERROR_101                       = 101;
    public static final int         ERROR_102                       = 102;
    public static final int         ERROR_103                       = 103;
    public static final int         ERROR_104                       = 104;
    public static final int         ERROR_105                       = 105;
    public static final int         ERROR_106                       = 106;
    public static final int         ERROR_107                       = 107;
    public static final int         ERROR_108                       = 108;
    
    public static final int         ERROR_201                       = 201;
    public static final int         ERROR_202                       = 202;
    public static final int         ERROR_203                       = 203;
    public static final int         ERROR_204                       = 204;
    
    public static final int         ERROR_301                       = 301;

    
}
