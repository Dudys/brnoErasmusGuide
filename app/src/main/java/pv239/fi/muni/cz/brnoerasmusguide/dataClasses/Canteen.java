package pv239.fi.muni.cz.brnoerasmusguide.dataClasses;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jan Duda on 5/20/2016.
 * Abstract for a single canteen. It implements Parcelable interface in order to pass this class in Intent.
 */
public class Canteen implements Parcelable {

    public String name;
    public String web;
    public String address;
    public List<OpenHours> openHours;
    public String mhdInfo;

    /**
     * Constructor for Parcelable interface.
     * @param in Parcel object.
     */
    protected Canteen(Parcel in) {
        name = in.readString();
        web = in.readString();
        address = in.readString();
        openHours = new ArrayList<>();
        in.readTypedList(openHours, OpenHours.CREATOR);
        mhdInfo = in.readString();
    }

    /**
     * Custom constructor for testing purpose. Will be removed probably.
     * @param name Name of a building.
     * @param web Link to web page of a building.
     * @param address Address of canteen.
     * @param openHours List of open hours of canteen.
     * @param mhdInfo Bus/Tram stop near to Canteen.
     */
    public Canteen(String name, String web, String address, List<OpenHours> openHours, String mhdInfo) {
        this.name = name;
        this.web = web;
        this.address = address;
        this.openHours = openHours;
        this.mhdInfo = mhdInfo;
    }

    public static final Creator<Canteen> CREATOR = new Creator<Canteen>() {
        @Override
        public Canteen createFromParcel(Parcel in) {
            return new Canteen(in);
        }

        @Override
        public Canteen[] newArray(int size) {
            return new Canteen[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(web);
        dest.writeString(address);
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
