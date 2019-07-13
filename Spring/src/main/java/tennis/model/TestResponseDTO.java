package tennis.model;

public class TestResponseDTO {
    private String message;
    private double punctuality;

    public TestResponseDTO() {
    }

    public TestResponseDTO(String message, double punctuality) {
        this.message = message;
        this.punctuality = punctuality;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getPunctuality() {
        return punctuality;
    }

    public void setPunctuality(double punctuality) {
        this.punctuality = punctuality;
    }
}
