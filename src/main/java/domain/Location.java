package domain;

public class Location {
    private final int file;
    private final int rank;

    public Location(int file, int rank) {
        this.file = file;
        this.rank = rank;
    }

    public int getFile() {
        return file;
    }

    public int getRank() {
        return rank;
    }
}
