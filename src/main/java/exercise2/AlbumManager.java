package exercise2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class AlbumManager {
    private ArrayList<Album> albums;

    public AlbumManager(String filePath) {
        this.albums = new ArrayList<>();
        try {
            loadAlbumsFromJSON(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadAlbumsFromJSON(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        JSONArray jsonArray = new JSONArray(content);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Album album = new Album();
            album.setTitle(jsonObject.getString("title"));
            album.setArtist(jsonObject.getString("artist"));
            album.setYear(jsonObject.getInt("year"));
            album.setGenre(jsonObject.getString("genre"));
            albums.add(album);
        }
    }

    public Album getAlbum(String title) {
        for (Album album : albums) {
            if (album.getTitle().equals(title)) {
                return album;
            }
        }
        return albums.get(0);
    }

    public void addAlbum(Album album) {
        albums.add(album);
    }

    public void saveAlbumsToJSON(String filePath) throws IOException {
        JSONArray jsonArray = new JSONArray();

        for (Album album : albums) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", album.getTitle());
            jsonObject.put("artist", album.getArtist());
            jsonObject.put("year", album.getYear());
            jsonObject.put("genre", album.getGenre());
            jsonArray.put(jsonObject);
        }

        String jsonContent = jsonArray.toString();
        Files.write(Paths.get(filePath), jsonContent.getBytes());
    }

    // Additional methods to manage albums
}
