package pv239.fi.muni.cz.brnoerasmusguide.pojo;

import com.google.gson.annotations.SerializedName;



import com.google.gson.annotations.Expose;

public class BuildingPojo {

    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("openHours")
    @Expose
    private String openHours;
    @SerializedName("mhdInfo")
    @Expose
    private String mhdInfo;

    /**
     * 
     * @return
     *     The address
     */
    public String getAddress() {
        return address;
    }

    /**
     * 
     * @param address
     *     The address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 
     * @return
     *     The openHours
     */
    public String getOpenHours() {
        return openHours;
    }

    /**
     * 
     * @param openHours
     *     The openHours
     */
    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }

    /**
     * 
     * @return
     *     The mhdInfo
     */
    public String getMhdInfo() {
        return mhdInfo;
    }

    /**
     * 
     * @param mhdInfo
     *     The mhdInfo
     */
    public void setMhdInfo(String mhdInfo) {
        this.mhdInfo = mhdInfo;
    }

}
