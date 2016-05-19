package pv239.fi.muni.cz.brnoerasmusguide.dataClasses;

import android.os.Parcel;
import android.os.Parcelable;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jakubfiser on 28/04/16.
 * Abstract for a single building. It implements Parcelable interface in order to pass this class in Intent.
 */
public class Faculty implements Parcelable, ParentListItem {

    public String name;
    public String web;
    public List<Building> buildings;

    /**
     * Constructor for Parcelable interface.
     * @param in Parcel object.
     */
    protected Faculty(Parcel in) {
        name = in.readString();
        web = in.readString();
        buildings = new ArrayList<>();
        in.readTypedList(buildings, Building.CREATOR);
    }

    /**
     * Custom constructor for testing purpose. Will be removed probably.
     * @param name Name of a building.
     * @param web Link to web page of a building.
     * @param buildings List of buildings of faculty
     */
    public Faculty(String name, String web, List<Building> buildings) {
        this.name = name;
        this.web = web;
        this.buildings = buildings;
    }

    public static final Creator<Faculty> CREATOR = new Creator<Faculty>() {
        @Override
        public Faculty createFromParcel(Parcel in) {
            return new Faculty(in);
        }

        @Override
        public Faculty[] newArray(int size) {
            return new Faculty[size];
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
        dest.writeTypedList(buildings);
    }

    @Override
    public List<Building> getChildItemList() {
        return buildings;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

//    public static class BuildingOfFac implements Parcelable {
//        public String name;
//        public String address;
//        public String openingHours;
//        public String mhdInfo;
//
//        /**
//         * Constructor for Parcelable interface.
//         * @param in Parcel object.
//         */
//        protected BuildingOfFac(Parcel in) {
//            address = in.readString();
//            openingHours = in.readString();
//            mhdInfo = in.readString();
//        }
//
//        public static final Creator<BuildingOfFac> CREATOR = new Creator<BuildingOfFac>() {
//            @Override
//            public BuildingOfFac createFromParcel(Parcel in) {
//                return new BuildingOfFac(in);
//            }
//
//            @Override
//            public BuildingOfFac[] newArray(int size) {
//                return new BuildingOfFac[size];
//            }
//        };
//
//        @Override
//        public int describeContents() {
//            return 0;
//        }
//
//        @Override
//        public void writeToParcel(Parcel dest, int flags) {
//            dest.writeString(address);
//            dest.writeString(openingHours);
//            dest.writeString(mhdInfo);
//        }
//    }
}
