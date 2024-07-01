package music;

public class Song {
    private  String artist;
    private  String title;
    private int durationInSeconds;

    public Song(String artist, String title, int durationInSeconds) {
        this.artist = artist;
        this.title = title;
        this.durationInSeconds = durationInSeconds;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }
}
