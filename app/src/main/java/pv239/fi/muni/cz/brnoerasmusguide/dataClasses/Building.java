package pv239.fi.muni.cz.brnoerasmusguide.dataClasses;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jakubfiser on 28/04/16.
 * Abstract for a single building. It implements Parcelable interface in order to pass this class in Intent.
 */
public class Building implements Parcelable {

    public String name;
    public String address;
    public String web;
    public String image;
    public List<OpenHours> openHours;
    public String mhdInfo;

    /**
     * Constructor for Parcelable interface.
     * @param in Parcel object.
     */
    protected Building(Parcel in) {
        name = in.readString();
        address = in.readString();
        web = in.readString();
        image = in.readString();
        openHours = new ArrayList<>();
        in.readTypedList(openHours, OpenHours.CREATOR);
        mhdInfo = in.readString();
    }

    /**
     * Custom constructor for testing purpose. Will be removed probably.
     * @param name Name of a building.
     * @param address Address of a building.
     * @param web Link to web page of a building.
     * @param openHours List containing opening hours.
     * @param mhd String containing information about bus stop nearby.
     */
    public Building(String name, String address, String web, String image, List<OpenHours> openHours, String mhd) {
        this.name = name;
        this.address = address;
        this.web = web;
        this.image = image;
        this.openHours = openHours;
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
        dest.writeString(image);
        dest.writeTypedList(openHours);
        dest.writeString(mhdInfo);
    }

    public static class OpenHours implements Parcelable {
        public String weekdays;
        public String hours;

        /**
         * Constructor for Parcelable interface.
         * @param in Parcel object.
         */
        protected OpenHours(Parcel in) {
            weekdays = in.readString();
            hours = in.readString();
        }

        /**
         * Custom constructor for testing purpose. Will be removed probably.
         * @param weekdays Days when is canteen open.
         * @param hours Hours when is canteen open.
         */
        public OpenHours(String weekdays, String hours) {
            this.weekdays = weekdays;
            this.hours = hours;
        }

        public static final Creator<OpenHours> CREATOR = new Creator<OpenHours>() {
            @Override
            public OpenHours createFromParcel(Parcel in) {
                return new OpenHours(in);
            }

            @Override
            public OpenHours[] newArray(int size) {
                return new OpenHours[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(weekdays);
            dest.writeString(hours);
        }
    }
}
