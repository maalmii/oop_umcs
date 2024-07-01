package music;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

public class PlaylistTests {
    @Test
    //За допомогою методу assertTrue перевіряється, чи є плейлист порожнім за допомогою методу isEmpty() у класі Playlist.
    public void testNewPlaylistIsEmpty() {
        Playlist playlist = new Playlist();
        assertTrue("New playlist should be empty", playlist.isEmpty());
    }

    @Test
    //За допомогою методу assertEquals перевіряється, чи розмір плейлиста дорівнює 1 після додавання однієї пісні.
    public void testPlaylistSizeAfterAddingSong() {
        Playlist playlist = new Playlist();
        Song song = new Song("Artist", "Title", 180);

        playlist.add(song);

        assertEquals(String.valueOf(1), playlist.size(), "Playlist size should be 1 after adding one song");
    }
//За допомогою методу assertTrue перевіряється, чи міститься додана пісня у плейлисті за допомогою методу contains().
    @Test
    public void testSongExistsInPlaylistAfterAdding() {
        Playlist playlist = new Playlist();
        Song songToAdd = new Song("Artist", "Title", 180);

        playlist.add(songToAdd);

        assertTrue("Playlist should contain the added song", playlist.contains(songToAdd));
    }

    @Test
    public void testAtSecond() {
        Playlist playlist = new Playlist();
        Song song1 = new Song("Artist1", "Song1", 120); // Song1 - 120 seconds
        Song song2 = new Song("Artist2", "Song2", 180); // Song2 - 180 seconds
        Song song3 = new Song("Artist3", "Song3", 150); // Song3 - 150 seconds

        playlist.add(song1);
        playlist.add(song2);
        playlist.add(song3);

        // Test case 1: After 100 seconds, should return song1
        assertEquals(song1, playlist.atSecond(100));

        // Test case 2: After 200 seconds, should return song2
        assertEquals(song2, playlist.atSecond(200));

        // Test case 3: After 400 seconds (exceeding total duration), should return song3 (last song)
        assertEquals(song3, playlist.atSecond(400));

        // Test case 4: At exactly 450 seconds (total duration), should return song3
        assertEquals(song3, playlist.atSecond(450));

        // Test case 5: Before any songs are played (0 seconds), should return song1 (first song)
        assertEquals(song1, playlist.atSecond(0));

        // Test case 6: After 600 seconds (exceeding total duration), should return null
        assertEquals(null, playlist.atSecond(600));
    }

    
}
