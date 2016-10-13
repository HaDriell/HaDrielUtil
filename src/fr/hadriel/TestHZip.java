package fr.hadriel;

import fr.hadriel.serialization.hzip.HZipFile;
import fr.hadriel.serialization.hzip.Index;

import java.io.IOException;

/**
 * Created by HaDriel on 12/10/2016.
 */
public class TestHZip {
    public static void main(String[] args) throws IOException {
        HZipFile archive = new HZipFile();
        archive.open("archive.hzip");

        Index toto = archive.get("toto.txt");
        if(toto == null) toto = archive.create("toto.txt", 0);

        Index titi = archive.get("titi.txt");
        if(titi == null) titi = archive.create("titi.txt", 0);

        archive.write(titi, 0, "HelloWorld".getBytes());
        archive.write(toto, 0, "GoodByeWorld".getBytes());
        archive.close();
    }
}