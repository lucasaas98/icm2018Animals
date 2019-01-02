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
    private String species_fancy;
    private String verified;
    private String identifier;
    private List<String> comments;
    private String id_;
    private String uuidUser;


    // CONSTRUCTOR:
    public SightingsData(){}


    // CONSTRUCTOR:
    public SightingsData(String name, String description, String location, String image, String date, String id_, String uuidUser, String species, String species_fancy, String verified, String identifier ) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.image = image;
        this.date = date;
        this.id_ = id_;
        this.uuidUser = uuidUser;
        this.species = species;
        this.species_fancy = species_fancy;
        this.verified = verified;
        this.identifier = identifier;
    }


    // GETTERS:
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getLocation() {
        return location;
    }
    public String getImage() {
        return image;
    }
    public String getDate() {
        return date;
    }
    public String getId_() {
        return id_;
    }
    public String getSpecies() {
        return species;
    }
    public List<String> getComments() {
        return comments;
    }
    public String isVerified() {
        return verified;
    }
    public String getUuidUser() {
        return uuidUser;
    }
    public String getSpecies_fancy() { return species_fancy; }
    public String getIdentifier() {return identifier;}

    // SETTERS:
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setId_(String id_) {
        this.id_ = id_;
    }
    public void setSpecies(String species) {
        this.species = species;
    }
    public void setVerified(String verified) {
        this.verified = verified;
    }
    public void setComments(List<String> comments) {
        this.comments = comments;
    }
    public void setUuidUser(String uuidUser) {
        this.uuidUser = uuidUser;
    }
    public void setSpecies_fancy(String species_fancy) { this.species_fancy = species_fancy; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }

    @Override
    public String toString() {
        return "SightingsData{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", location='" + location + '\'' +
                ", image='" + image + '\'' +
                ", species='" + species + '\'' +
                ", species_fancy='" + species_fancy + '\'' +
                ", verified='" + verified + '\'' +
                ", comments=" + comments +
                ", id_='" + id_ + '\'' +
                ", uuidUser='" + uuidUser + '\'' +
                '}';
    }
}
