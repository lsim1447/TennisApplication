package tennis.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
@Entity
public class Player {
    @Id
    private String player_id;

    private String playerSlug;

    public String getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(String player_id) {
        this.player_id = player_id;
    }

    public String getPlayerSlug() {
        return playerSlug;
    }

    public void setPlayerSlug(String playerSlug) {
        this.playerSlug = playerSlug;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPlayerUrl() {
        return playerUrl;
    }

    public void setPlayerUrl(String playerUrl) {
        this.playerUrl = playerUrl;
    }

    public String getFlagCode() {
        return flagCode;
    }

    public void setFlagCode(String flagCode) {
        this.flagCode = flagCode;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public int getBirthMonth() {
        return birthMonth;
    }

    public void setBirthMonth(int birthMonth) {
        this.birthMonth = birthMonth;
    }

    public int getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(int birthDay) {
        this.birthDay = birthDay;
    }

    public int getTurnedPro() {
        return turnedPro;
    }

    public void setTurnedPro(int turnedPro) {
        this.turnedPro = turnedPro;
    }

    public int getWeightLbs() {
        return weightLbs;
    }

    public void setWeightLbs(int weightLbs) {
        this.weightLbs = weightLbs;
    }

    public int getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(int weightKg) {
        this.weightKg = weightKg;
    }

    public String getHeightFt() {
        return heightFt;
    }

    public void setHeightFt(String heightFt) {
        this.heightFt = heightFt;
    }

    public String getHeightInches() {
        return heightInches;
    }

    public void setHeightInches(String heightInches) {
        this.heightInches = heightInches;
    }

    public String getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(String heightCm) {
        this.heightCm = heightCm;
    }

    public String getHandedness() {
        return handedness;
    }

    public void setHandedness(String handedness) {
        this.handedness = handedness;
    }

    public String getBackhand() {
        return backhand;
    }

    public void setBackhand(String backhand) {
        this.backhand = backhand;
    }

    private String firstName;

    private String lastName;

    private String playerUrl;

    private String flagCode;

    private String birthdate;

    private int birthYear;

    private int birthMonth;

    private int birthDay;

    private int turnedPro;

    private int weightLbs;

    private int weightKg;

    private String heightFt;

    private String heightInches;

    private String heightCm;

    private String handedness;

    private String backhand;

    public Player(String player_id, String player_slug, String first_name, String last_name, String player_url, String flag_code, String birthdate, int birth_year, int birth_month, int birth_day, int turned_pro, int weight_lbs, int weight_kg, String height_ft, String height_inches, String height_cm, String handedness, String backhand) {
        this.player_id = player_id;
        this.playerSlug = player_slug;
        this.firstName = first_name;
        this.lastName = last_name;
        this.playerUrl = player_url;
        this.flagCode = flag_code;
        this.birthdate = birthdate;
        this.birthYear = birth_year;
        this.birthMonth = birth_month;
        this.birthDay = birth_day;
        this.turnedPro = turned_pro;
        this.weightLbs = weight_lbs;
        this.weightKg = weight_kg;
        this.heightFt = height_ft;
        this.heightInches = height_inches;
        this.heightCm = height_cm;
        this.handedness = handedness;
        this.backhand = backhand;
    }

    public Player(){

    }
}
