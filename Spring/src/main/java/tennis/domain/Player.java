package tennis.domain;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
@Entity
public class Player {
    @Id
    private String player_id;

    private String player_slug;

    public String getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(String player_id) {
        this.player_id = player_id;
    }

    public String getPlayer_slug() {
        return player_slug;
    }

    public void setPlayer_slug(String player_slug) {
        this.player_slug = player_slug;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPlayer_url() {
        return player_url;
    }

    public void setPlayer_url(String player_url) {
        this.player_url = player_url;
    }

    public String getFlag_code() {
        return flag_code;
    }

    public void setFlag_code(String flag_code) {
        this.flag_code = flag_code;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public int getBirth_year() {
        return birth_year;
    }

    public void setBirth_year(int birth_year) {
        this.birth_year = birth_year;
    }

    public int getBirth_month() {
        return birth_month;
    }

    public void setBirth_month(int birth_month) {
        this.birth_month = birth_month;
    }

    public int getBirth_day() {
        return birth_day;
    }

    public void setBirth_day(int birth_day) {
        this.birth_day = birth_day;
    }

    public int getTurned_pro() {
        return turned_pro;
    }

    public void setTurned_pro(int turned_pro) {
        this.turned_pro = turned_pro;
    }

    public int getWeight_lbs() {
        return weight_lbs;
    }

    public void setWeight_lbs(int weight_lbs) {
        this.weight_lbs = weight_lbs;
    }

    public int getWeight_kg() {
        return weight_kg;
    }

    public void setWeight_kg(int weight_kg) {
        this.weight_kg = weight_kg;
    }

    public String getHeight_ft() {
        return height_ft;
    }

    public void setHeight_ft(String height_ft) {
        this.height_ft = height_ft;
    }

    public String getHeight_inches() {
        return height_inches;
    }

    public void setHeight_inches(String height_inches) {
        this.height_inches = height_inches;
    }

    public String getHeight_cm() {
        return height_cm;
    }

    public void setHeight_cm(String height_cm) {
        this.height_cm = height_cm;
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

    private String first_name;

    private String last_name;

    private String player_url;

    private String flag_code;

    private String birthdate;

    private int birth_year;

    private int birth_month;

    private int birth_day;

    private int turned_pro;

    private int weight_lbs;

    private int weight_kg;

    private String height_ft;

    private String height_inches;

    private String height_cm;

    private String handedness;

    private String backhand;

    public Player(String player_id, String player_slug, String first_name, String last_name, String player_url, String flag_code, String birthdate, int birth_year, int birth_month, int birth_day, int turned_pro, int weight_lbs, int weight_kg, String height_ft, String height_inches, String height_cm, String handedness, String backhand) {
        this.player_id = player_id;
        this.player_slug = player_slug;
        this.first_name = first_name;
        this.last_name = last_name;
        this.player_url = player_url;
        this.flag_code = flag_code;
        this.birthdate = birthdate;
        this.birth_year = birth_year;
        this.birth_month = birth_month;
        this.birth_day = birth_day;
        this.turned_pro = turned_pro;
        this.weight_lbs = weight_lbs;
        this.weight_kg = weight_kg;
        this.height_ft = height_ft;
        this.height_inches = height_inches;
        this.height_cm = height_cm;
        this.handedness = handedness;
        this.backhand = backhand;
    }

    public Player(){

    }
}
