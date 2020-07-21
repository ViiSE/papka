package ru.viise.papka.entity;

import org.testng.annotations.Test;
import utils.PrintTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.*;

public class FolderRawTextTestNG {

    @Test(priority = 1)
    public void ctorArray() {
        new FolderRawText(
                "/root1.png",
                "/music/a.mp3",
                 "/music/opus/b.mp3",
                 "/music/opus/var/var.mp3",
                 "/music/opus/var/varsAr.mp3",
                 "/music/opus/bar/bar.mp3",
                 "/music/opus/dar/dar.flac",
                 "/opes/b.png",
                 "/music/opus/ar.mp3",
                 "/root2.png",
                 "/root3.png");
    }

    @Test(priority = 2)
    public void ctorList() {
        new FolderRawText(
                new ArrayList<String>() {{
                add("/root1.png");
                add("/music/a.mp3");
                add("/music/opus/b.mp3");
                add("/music/opus/var/var.mp3");
                add("/music/opus/var/varsAr.mp3");
                add("/music/opus/bar/bar.mp3");
                add("/music/opus/dar/dar.flac");
                add("/opes/b.png");
                add("/music/opus/ar.mp3");
                add("/root2.png");
                add("/root3.png"); }});
    }

    @Test(priority = 3)
    public void ctorNameAndArray() {
        new FolderRawText(
                new NameFolderRoot(),
                "/root1.png",
                "/music/a.mp3",
                "/music/opus/b.mp3",
                "/music/opus/var/var.mp3",
                "/music/opus/var/varsAr.mp3",
                "/music/opus/bar/bar.mp3",
                "/music/opus/dar/dar.flac",
                "/opes/b.png",
                "/music/opus/ar.mp3",
                "/root2.png",
                "/root3.png");
    }

    @Test(priority = 4)
    public void ctorNameAndList() {
        new FolderRawText(
                new NameFolderRoot(),
                new ArrayList<String>() {{
                    add("/root1.png");
                    add("/music/a.mp3");
                    add("/music/opus/b.mp3");
                    add("/music/opus/var/var.mp3");
                    add("/music/opus/var/varsAr.mp3");
                    add("/music/opus/bar/bar.mp3");
                    add("/music/opus/dar/dar.flac");
                    add("/opes/b.png");
                    add("/music/opus/ar.mp3");
                    add("/root2.png");
                    add("/root3.png"); }});
    }

    @Test(priority = 5)
    public void children() {
        Folder<String> folders = testFolder();

        List<Folder<String>> children = folders.children();
        List<Folder<String>> expected = new ArrayList<>();
        assertNotNull(children);
        assertEquals(children, expected);
    }

    @Test(priority = 6)
    public void files() {
        Folder<String> folders = new FolderRawText(
                "/root1.png",
                "/root2.png",
                "/root3.png");

        List<String> files = folders.files();
        assertNotNull(files);
        assertEquals(
                files,
                new ArrayList<String>() {{
                    add("root1.png");
                    add("root2.png");
                    add("root3.png");}});
    }

    @Test(priority = 7)
    public void fullName() {
        String fullName = testFolder().fullName();

        assertNotNull(fullName);
        assertEquals(fullName, "/music");
    }

    @Test(priority = 8)
    public void travel() {
        List<String> rootFiles = new ArrayList<String>() {{
            add("root1.png");
            add("root2.png");
            add("root3.png");
        }};

        List<String> musicFiles = new ArrayList<String>() {{
            add("a.mp3");
        }};

        List<String> musicOpusFiles = new ArrayList<String>() {{
            add("b.mp3");
            add("ar.mp3");
        }};

        List<String> musicOpusVarFiles = new ArrayList<String>() {{
            add("var.mp3");
        }};

        List<String> musicOpusBarFiles = new ArrayList<String>() {{
            add("bar.mp3");
        }};

        List<String> musicOpesFiles = new ArrayList<String>() {{
            add("b.png");
        }};

        List<List<String>> files = new ArrayList<>();
        files.add(rootFiles);
        files.add(musicFiles);
        files.add(musicOpusFiles);
        files.add(musicOpusBarFiles);
        files.add(musicOpusVarFiles);
        files.add(musicOpesFiles);

        AtomicInteger i = new AtomicInteger();
        Folder<String> root = new FolderRawText(
                "/root1.png",
                "/music/a.mp3",
                "/music/opus/b.mp3",
                "/music/opus/var/var.mp3",
                "/music/opus/bar/bar.mp3",
                "/opes/b.png",
                "/music/opus/ar.mp3",
                "/root2.png",
                "/root3.png");

        root.travel(folder -> {
            PrintTest.printFolder(folder);
            List<String> _files = folder.files();
            assertEquals(_files, files.get(i.getAndIncrement()));
        });
    }

    @Test(priority = 9)
    public void shortName() {
        String shortName = testFolder().shortName();
        assertNotNull(shortName);
        assertEquals(shortName, "music");
    }

    @Test(priority = 10)
    public void eq() {
        Folder<String> f1 = testFolder();
        Folder<String> f2 = new FolderPure<>("/music");

        f1.travel(folder -> assertEquals(folder, f2));
    }

    @Test(priority = 11)
    public void eq_wrongType() {
        Folder<String> f2 = new FolderPure<>("/music");
        String fileFake = "/music";

        assertNotEquals(f2, fileFake);
    }

    private Folder<String> testFolder() {
        Folder<String> folder = new FolderPure<>(
                "/",
                new ArrayList<>(),
                new ArrayList<Folder<String>>() {{
                    add(new FolderPure<>("/music", new ArrayList<>(), new ArrayList<>()));
                }});
        List<Folder<String>> children = folder.children();
        return children.get(0);
    }
}
