package tennis.model;

public class PredictionResponseDTO {

    private Double first_percentage;
    private Double second_percentage;

    public PredictionResponseDTO() { }

    public PredictionResponseDTO(Double first_percentage, Double second_percentage) {
        this.first_percentage = first_percentage;
        this.second_percentage = second_percentage;
    }

    public Double getFirst_percentage() {
        return first_percentage;
    }

    public void setFirst_percentage(Double first_percentage) {
        this.first_percentage = first_percentage;
    }

    public Double getSecond_percentage() {
        return second_percentage;
    }

    public void setSecond_percentage(Double second_percentage) {
        this.second_percentage = second_percentage;
    }

    @Override
    public String toString(){
        return "first_percentage = " + this.first_percentage + "\n" + "second_percentage = " + this.second_percentage;
    }
}
