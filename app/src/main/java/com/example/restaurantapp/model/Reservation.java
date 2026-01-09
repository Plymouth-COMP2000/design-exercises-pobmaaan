package com.example.restaurantapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "reservations")
@TypeConverters(DateConverter.class)
public class Reservation implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String time;
    private int partySize;
    private int tableId;
    private Date date;
    private String status;

    public Reservation(String name, String time, int partySize, int tableId, Date date, String status) {
        this.name = name;
        this.time = time;
        this.partySize = partySize;
        this.tableId = tableId;
        this.date = date;
        this.status = status;
    }

    protected Reservation(Parcel in) {
        id = in.readInt();
        name = in.readString();
        time = in.readString();
        partySize = in.readInt();
        tableId = in.readInt();
        long tmpDate = in.readLong();
        date = tmpDate == -1 ? null : new Date(tmpDate);
        status = in.readString();
    }

    public static final Creator<Reservation> CREATOR = new Creator<Reservation>() {
        @Override
        public Reservation createFromParcel(Parcel in) {
            return new Reservation(in);
        }

        @Override
        public Reservation[] newArray(int size) {
            return new Reservation[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getCustomerName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public String getReservationTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPartySize() {
        return partySize;
    }

    public int getCovers() {
        return partySize;
    }

    public void setPartySize(int partySize) {
        this.partySize = partySize;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(time);
        dest.writeInt(partySize);
        dest.writeInt(tableId);
        dest.writeLong(date != null ? date.getTime() : -1);
        dest.writeString(status);
    }
}
