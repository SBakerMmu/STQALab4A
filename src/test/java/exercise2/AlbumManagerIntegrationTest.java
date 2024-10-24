package exercise2;

import org.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assumptions.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AlbumManagerIntegrationTest {

    final String RELATIVE_FILE_PATH = "src/test/java/resources/albums.json";
    final String INVALID_FILE_PATH = "src/test/java/resources/notalbums.json";
    final String COPY_FILE_PATH = "src/test/java/resources/copyOfAlbums.json";

    //Titles matching the test data file
    final String[] TITLES_CS = new String[]{
          "Abbey Road", "Thriller","The Wall","Back in Black","Purple Rain","Rumours","Born to Run","Hotel California"
        };

    final String[] TITLES_CI = new String[]{
            "ABBEY ROAD", "THRILLER", "THE WALL","BACK IN BLACK","PURPLE RAIN","RUMOURS","BORN TO RUN","HOTEL CALIFORNIA"
    };



    @BeforeEach
    void setUp() throws IOException {
    }

    // Exercise 2.1 - Test that getting an album that doesn't exist returns null as expected
    @Test
    @DisplayName("Test retrieval of non-existent album")
    void testRetrievalOfNonExistentAlbumShouldReturnNull() {
        final String NON_EXISTENT = "Non Existent Album Name";

        //arrange
        var albumManager = new AlbumManager(RELATIVE_FILE_PATH);
        //assume that our NON_EXISTENT name isn't one of the titles in the test data
        assumeFalse(Arrays.stream(TITLES_CS).anyMatch(title -> title.equalsIgnoreCase(NON_EXISTENT)));

        //act and assert when name does not match
        assertNull(albumManager.getAlbum(NON_EXISTENT));
    }

    // Exercise 2.2 - Test that getting an album by name is case-insensitive using the CI list of titles
    @ParameterizedTest
    @MethodSource("streamAlbumNames")
    @DisplayName("Test getAlbum method is case insensitive")
    void testGetAlbumIsCaseInsensitiveShouldReturnCorrectAlbum(String ciAlbumName, String csAlbumName) {
        //arrange
        var albumManager = new AlbumManager(RELATIVE_FILE_PATH);
        //act
        Album album = albumManager.getAlbum(ciAlbumName);
        //Assert we get a result
        assertNotNull(album);
        //Assert result is correct (not null is insufficient, we need to confirm that the title is correct also)
        assertEquals(csAlbumName, album.getTitle());
    }

    Stream<Arguments> streamAlbumNames()
    {
        List<Arguments> names = new ArrayList<>();
        assumeTrue(TITLES_CI.length == TITLES_CS.length);
        for (int i = 0; i < TITLES_CI.length; i++) {
            names.add(Arguments.arguments(TITLES_CI[i], TITLES_CS[i]));
        }
        return names.stream();

    }


    // Exercise 2.3 - Test that the constructor loads albums
    // Note: There was a mismatch between the worksheet and the code in the provided zip.
    // Note: This is the solution for the task detailed on the worksheet.
    @ParameterizedTest
    @MethodSource("streamAlbumNames")
    @DisplayName("Test AlbumManager's constructor correctly loads albums")
    void testConstructor_ShouldLoadAlbums(String ciAlbumName, String csAlbumName) {
        //arrange
        var albumManager = new AlbumManager(RELATIVE_FILE_PATH);
        //act
        Album album = albumManager.getAlbum(csAlbumName);
        //assert we get a result
        assertNotNull(album);
        //assert result is correct
        assertEquals(csAlbumName, album.getTitle());
    }

    // Exercise 2.4 - Test that giving the loadAlbums method a file that doesn't exist gives us the correct exception
    @Test
    @DisplayName("Test Constructor with invalid file path/name")
    void testConstructorWithInvalidFilePathShouldThrowRuntimeException() {
        //assert throws the expected exception class
        RuntimeException exception = assertThrows(RuntimeException.class, () ->  new AlbumManager(INVALID_FILE_PATH));
    }

    // Exercise 2.5 - Test that successive calls to the loadAlbumsFromJSON method do not keep adding the albums to the ArrayList
    // Note: This last one is a bit more complex than you might have expected. It has been written like this to avoid altering the methods under test.
    // Note: Often, you will not want to, or not have the ability to alter the methods under test.
    @Test
    @DisplayName("testLoadAlbumsFromJSONShouldClearPreviousAlbums")
    void testLoadAlbumsFromJSONShouldClearPreviousAlbums() {
        //arrange
        var albumManager = new AlbumManager(RELATIVE_FILE_PATH);
        //Reload
        assertDoesNotThrow(() ->  albumManager.loadAlbumsFromJSON(RELATIVE_FILE_PATH));
        //Write copy - this is actually testing two methods, an error could be in either, but this is the only way provided by the API to count albums
        assertDoesNotThrow(() ->albumManager.saveAlbumsToJSON(COPY_FILE_PATH));

        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(COPY_FILE_PATH)));
        } catch (IOException e) {
            //use fail so reports in as a testing error
            fail(e);
        }
        JSONArray jsonArray = new JSONArray(content);
        assertEquals(TITLES_CI.length, jsonArray.length());
    }


}
