package ua.pt.naturespot.Model;

import android.widget.ImageView;

import java.util.List;

public class SightingsData {

    private String name;
    private String description;
    private String date;
    private String location;
    private String image;
    private String species;
    private boolean verified;
    private List<String> comments;

    public SightingsData(){}

    public SightingsData(String name, String description, String location, String image, String date) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.image = image;
        this.date = date;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "SightingsData{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
