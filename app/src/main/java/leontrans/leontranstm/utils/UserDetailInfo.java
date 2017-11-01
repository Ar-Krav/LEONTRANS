package leontrans.leontranstm.utils;


public class UserDetailInfo {
    private String userName;
    private String userCity;
    private String userEmail;
    private String userPhone;
    private String userSkype;
    private String userICQ;
    private String userWebSite;
    private String userOccupation;
    private String userOccupationType;
    private String userOccupationDescription;
    private String userRegistrationDate;
    private String userLastOnline;
    private String userAvatarCode;

    public UserDetailInfo(String userName, String userCity, String userEmail, String userPhone,
                          String userSkype, String userICQ, String userWebSite,
                          String userOccupation, String userOccupationType, String userOccupationDescription,
                          String userRegistrationDate, String userLastOnline, String userAvatarCode) {
        this.userName = userName;
        this.userCity = userCity;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userSkype = userSkype;
        this.userICQ = userICQ;
        this.userWebSite = userWebSite;
        this.userOccupation = userOccupation;
        this.userOccupationType = userOccupationType;
        this.userOccupationDescription = userOccupationDescription;
        this.userRegistrationDate = userRegistrationDate;
        this.userLastOnline = userLastOnline;
        this.userAvatarCode = userAvatarCode;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserCity() {
        return userCity;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserSkype() {
        return userSkype;
    }

    public String getUserICQ() {
        return userICQ;
    }

    public String getUserWebSite() {
        return userWebSite;
    }

    public String getUserOccupation() {
        return userOccupation;
    }

    public String getUserOccupationType() {
        return userOccupationType;
    }

    public String getUserOccupationDescription() {
        return userOccupationDescription;
    }

    public String getUserRegistrationDate() {
        return userRegistrationDate;
    }

    public String getUserLastOnline() {
        return userLastOnline;
    }

    public String getUserAvatarCode() {
        return userAvatarCode;
    }
}
