package common;

import collections.VehicleType;
import utils.Const;

public class InputCheck {

    public static int checkLogin(String input) {
        if (input.trim().length() > 0) {
            return Const.SUCCESS;
        } else {
            return Const.ERROR_201;
        }
    }

    public static int checkPassword(String input) {
        if (input.trim().length() > 0) {
            return Const.SUCCESS;
        } else {
            return Const.ERROR_202;
        }
    }
    
    public static int checkName(String input) {
        if (input.trim().length() > 0) {
            return Const.SUCCESS;
        } else {
            return Const.ERROR_101;
        }
    }

    public static int checkX(String input) {
        try {
            Float.parseFloat(input);
            return Const.SUCCESS;
        } catch (Exception e) {
            return Const.ERROR_102;
        }
    }

    public static int checkY(String input) {
        try {
            Float.parseFloat(input);
            return Const.SUCCESS;
        } catch (Exception e) {
            return Const.ERROR_103;
        }
    }
    

    public static int checkEnginePower(String input) {
        try {
            float param = Float.parseFloat(input);
            if (param <= 0) throw new Exception();
            return Const.SUCCESS;
        } catch (Exception e) {
            return Const.ERROR_104;
        }
    }

    public static int checkCapacity(String input) {
        try {
            long param = Long.parseLong(input);
            if (param <= 0) throw new Exception();
            return Const.SUCCESS;
        } catch (Exception e) {
            return Const.ERROR_105;
        }
    }

    public static int checkDistanceTravelled(String input) {
        try {
            double param = Double.parseDouble(input);
            if (param <= 0) throw new Exception();
            return Const.SUCCESS;
        } catch (Exception e) {
            return Const.ERROR_106;
        }
    }
    
    public static int checkVehicleType(String input) {
        try {
            VehicleType.valueOf(input);
            return Const.SUCCESS;
        } catch (Exception e) {
            return Const.ERROR_107;
        }
    }


    public static int checkCredentials(String login, String password) {
        int result;
        
        result = InputCheck.checkLogin(login);
        if (result != Const.SUCCESS) {
            return result;
        }

        result = InputCheck.checkPassword(password);
        if (result != Const.SUCCESS) {
            return result;
        }
        
        return Const.SUCCESS;
    }
    
    public static int checkVehicle(String name, String x, String y, String engine, String capacity, String distance, String type) {
        int result;
        
        result = InputCheck.checkName(name);
        if (result != Const.SUCCESS) {
            return result;
        }
        
        result = InputCheck.checkX(x);
        if (result != Const.SUCCESS) {
            return result;
        }

        result = InputCheck.checkY(y);
        if (result != Const.SUCCESS) {
            return result;
        }

        result = InputCheck.checkEnginePower(engine);
        if (result != Const.SUCCESS) {
            return result;
        }

        result = InputCheck.checkCapacity(capacity);
        if (result != Const.SUCCESS) {
            return result;
        }

        result = InputCheck.checkDistanceTravelled(distance);
        if (result != Const.SUCCESS) {
            return result;
        }

        result = InputCheck.checkVehicleType(type);
        if (result != Const.SUCCESS) {
            return result;
        }
        
        return Const.SUCCESS;
        
    }
}
