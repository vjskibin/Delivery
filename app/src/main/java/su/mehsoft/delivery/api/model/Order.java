package su.mehsoft.delivery.api.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Date;

public class Order {

    @SerializedName("id")
    //@Expose
    private Integer id;

    @SerializedName("creator_id")
    //@Expose
    private int creatorId;

    @SerializedName("name")
    //@Expose
    private String name;

    @SerializedName("description")
    //@Expose
    private String description;

    @SerializedName("location")
    //@Expose
    private String location;

    @SerializedName("salary")
    //@Expose
    private Float salary;

    @SerializedName("date_created")
    //@Expose
    private String dateCreated;

    public Order(int id, int creatorId, String name, String description, String location, float salary, String dateCreated) {
        this.id = id;
        this.creatorId = creatorId;
        this.name = name;
        this.description = description;
        this.location = location;
        this.salary = salary;
        this.dateCreated = dateCreated;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Float getSalary() {
        return salary;
    }

    public void setSalary(Float salary) {
        this.salary = salary;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
