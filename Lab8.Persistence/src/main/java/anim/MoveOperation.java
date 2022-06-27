package anim;

import java.awt.geom.Point2D;
import java.io.Serializable;

public class MoveOperation implements Serializable {
    
    private int vehicleId;
    private Point2D startPoint;
    private Point2D targetPoint;

    public MoveOperation() {
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public Point2D getStartPoint() {
        return startPoint;
    }

    public Point2D getTargetPoint() {
        return targetPoint;
    }
    
    public static MoveOperation of(int vehicleId, Point2D startPoint, Point2D targetPoint) {
        MoveOperation operation = new MoveOperation();
        operation.vehicleId = vehicleId;
        operation.startPoint = startPoint;
        operation.targetPoint = targetPoint;
        return operation;
    }
    
    
}
