package pv239.fi.muni.cz.brnoerasmusguide.dataClasses;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jakubfiser on 28/04/16.
 * Abstract for a single building. It implements Parcelable interface in order to pass this class in Intent.
 */
public class Building implements Parcelable {

    public String name;
    public String address;
    public String web;
    public String openHours;
    public String mhdInfo;

    /**
     * Constructor for Parcelable interface.
     * @param in Parcel object.
     */
    protected Building(Parcel in) {
        name = in.readString();
        address = in.readString();
        web = in.readString();
        openHours = in.readString();
        mhdInfo = in.readString();
    }

    /**
     * Custom constructor for testing purpose. Will be removed probably.
     * @param name Name of a building.
     * @param address Address of a building.
     * @param web Link to web page of a building.
     * @param opening String containing opening hours.
     * @param mhd String containing information about bus stop nearby.
     */
    public Building(String name, String address, String web, String opening, String mhd) {
        this.name = name;
        this.address = address;
        this.web = web;
        this.openHours = opening;
        this.mhdInfo = mhd;
    }

    public static final Creator<Building> CREATOR = new Creator<Building>() {
        @Override
        public Building createFromParcel(Parcel in) {
            return new Building(in);
        }

        @Override
        public Building[] newArray(int size) {
            return new Building[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(web);
        dest.writeString(openHours);
        dest.writeString(mhdInfo);
    }
}
