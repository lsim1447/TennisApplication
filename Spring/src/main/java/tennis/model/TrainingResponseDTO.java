package tennis.model;

public class TrainingResponseDTO {
    private String kecske;
    private String beka;
    private int malac;

    public TrainingResponseDTO() { }

    public TrainingResponseDTO(String kecske, String beka, int malac) {
        this.kecske = kecske;
        this.beka = beka;
        this.malac = malac;
    }

    public String getKecske() {
        return kecske;
    }

    public void setKecske(String kecske) {
        this.kecske = kecske;
    }

    public String getBeka() {
        return beka;
    }

    public void setBeka(String beka) {
        this.beka = beka;
    }

    public int getMalac() {
        return malac;
    }

    public void setMalac(int malac) {
        this.malac = malac;
    }

    @Override
    public String toString() {
        return String.format(this.kecske + " + " + this.beka + " " + this.malac);
    }
}
