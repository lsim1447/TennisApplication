package tennis.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Tourney {
    @Id
    private String tourney_id;
    private String name;
    private String slug;
    private String location;

    public Tourney() {

    }

    public Tourney(String tourney_id, String name, String slug, String location) {
        this.tourney_id = tourney_id;
        this.name = name.replace("\"", "");
        this.slug = slug;
        this.location = location.replace("\"", "");
    }

    public String getTourney_id() {
        return tourney_id;
    }

    public void setTourney_id(String tourney_id) {
        this.tourney_id = tourney_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Tourney)) {
            return false;
        }
        Tourney tourney = (Tourney) o;
        return tourney.getTourney_id().equals(this.tourney_id);
    }

    @Override
    public int hashCode(){
        return this.tourney_id.length();
    }
}
