package anim;

import java.awt.geom.Point2D;
import java.io.Serializable;

public class EatOperation implements Serializable {
    
    private int vehicle1Id;
    private long vehicle1Capacity;
    
    private int vehicle2Id;
    private long vehicle2Capacity;
    

    public EatOperation() {
    }

    public int getVehicle1Id() {
        return vehicle1Id;
    }

    public long getVehicle1Capacity() {
        return vehicle1Capacity;
    }

    public int getVehicle2Id() {
        return vehicle2Id;
    }

    public long getVehicle2Capacity() {
        return vehicle2Capacity;
    }

    public void setVehicle1Capacity(long vehicle1Capacity) {
        this.vehicle1Capacity = vehicle1Capacity;
    }

    public void setVehicle2Capacity(long vehicle2Capacity) {
        this.vehicle2Capacity = vehicle2Capacity;
    }
    
    public static EatOperation of(int vehicle1Id, long vehicle1Capacity, int vehicle2Id, long vehicle2Capacity) {
        EatOperation operation = new EatOperation();
        operation.vehicle1Id = vehicle1Id;
        operation.vehicle1Capacity = vehicle1Capacity;
        operation.vehicle2Id = vehicle2Id;
        operation.vehicle2Capacity = vehicle2Capacity;
        return operation;
    }
}
