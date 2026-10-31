package org.hosp1.facility.model;

import java.math.BigDecimal;
import java.util.Objects;

/** POJO: a room or ward in the hospital. */
public class Room {

    private String id;
    private String number;
    private RoomType type;
    private int capacity;           // number of beds
    private BigDecimal dailyRate;   // charge per patient per day

    public Room(String id, String number, RoomType type, int capacity, BigDecimal dailyRate) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.capacity = capacity;
        this.dailyRate = dailyRate;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public RoomType getType() { return type; }
    public void setType(RoomType type) { this.type = type; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public BigDecimal getDailyRate() { return dailyRate; }
    public void setDailyRate(BigDecimal dailyRate) { this.dailyRate = dailyRate; }

    @Override
    public boolean equals(Object o) { return o instanceof Room r && Objects.equals(id, r.id); }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Room{number=" + number + ", type=" + type + ", capacity=" + capacity + "}";
    }
}