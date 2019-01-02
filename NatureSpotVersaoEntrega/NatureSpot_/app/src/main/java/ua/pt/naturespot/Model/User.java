package ua.pt.naturespot.Model;

public class User {

    private String userName;
    private String userEmail;
    private String userImage;
    private String userDistrict;
    private String userDescription;
    private String userVerifier;

    // DEFAULT CONSTRUCTOR:
    public User() {
    }


    // CONSTRUCTOR:
    public User(String name, String email, String userImage, String userDistrict, String userDescription, String verifier) {
        this.userName = name;
        this.userEmail = email;
        this.userImage = userImage;
        this.userDistrict = userDistrict;
        this.userDescription = userDescription;
        this.userVerifier = verifier;
    }


    // GETTERS:
    public String getUserName() {
        return userName;
    }
    public String getUserEmail() {
        return userEmail;
    }
    public String getUserImage() {
        return userImage;
    }
    public String getUserDistrict() {
        return userDistrict;
    }
    public String getUserDescription() {
        return userDescription;
    }
    public String getUserVerifier() {
        return userVerifier;
    }


    //SETTERS:
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
    public void setUserDistrict(String userDistrict) {
        this.userDistrict = userDistrict;
    }
    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }
    public void setUserVerifier(String verifier) {
        this.userVerifier = verifier;
    }
}