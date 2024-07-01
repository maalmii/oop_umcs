package music;

import java.util.ArrayList;

public class Playlist extends ArrayList<Song> {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Playlist:\n");
        for (int i = 0; i < this.size(); i++) {
            sb.append(i + 1).append(". ").append(this.get(i)).append("\n");
        }
        return sb.toString();
    }

    public Song atSecond(int seconds) {
        int totalDuration = 0;
        for (Song song : this) {
            totalDuration += song.getDurationInSeconds();
            if (totalDuration >= seconds) {
                return song;
            }
        }
        return null;
    }
}
