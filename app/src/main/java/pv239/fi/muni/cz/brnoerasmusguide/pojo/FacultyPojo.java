package pv239.fi.muni.cz.brnoerasmusguide.pojo;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FacultyPojo {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("web")
    @Expose
    private String web;
    @SerializedName("buildings")
    @Expose
    private List<BuildingPojo> buildings = new ArrayList<BuildingPojo>();

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The web
     */
    public String getWeb() {
        return web;
    }

    /**
     * 
     * @param web
     *     The web
     */
    public void setWeb(String web) {
        this.web = web;
    }

    /**
     * 
     * @return
     *     The buildings
     */
    public List<BuildingPojo> getBuildings() {
        return buildings;
    }

    /**
     * 
     * @param buildings
     *     The buildings
     */
    public void setBuildings(List<BuildingPojo> buildings) {
        this.buildings = buildings;
    }

}
