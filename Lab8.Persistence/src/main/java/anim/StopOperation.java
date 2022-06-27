package anim;

import java.awt.geom.Point2D;
import java.io.Serializable;

public class StopOperation implements Serializable {
    
    private int vehicleId;
    private Point2D targetPoint;

    public StopOperation() {
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public Point2D getTargetPoint() {
        return targetPoint;
    }
    
    public static StopOperation of(int vehicleId, Point2D targetPoint) {
        StopOperation operation = new StopOperation();
        operation.vehicleId = vehicleId;
        operation.targetPoint = targetPoint;
        return operation;
    }
    
    
}
