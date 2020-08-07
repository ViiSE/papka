[![Build Status](https://travis-ci.com/viise/papka.svg?branch=master)](https://travis-ci.com/viise/papka)
[![Code Coverage](https://codecov.io/github/viise/papka/coverage.svg)](https://codecov.io/gh/viise/papka)
# Papka
Object-Oriented library for working with file tree.

## Get
```xml
<dependency>
    <groupId>com.github.viise.papka</groupId>
    <artifactId>papka</artifactId>
    <version>1.0.2</version>
</dependency>
```

## Usage

You have a raw filename list:
<br>
```
/root1.png
/root2.png
/root3.png
/music/a.mp3
/music/opus/oA.mp3
/music/opus/oB.mp3
/music/opus/bth/1.mp3
/music/opus/bth/111.mp3
/music/opus/mzt/21.mp3
/music/opus/bch/13.flac
/doc/table.docx
```

You can use <code>Folder</code> interface:
```java
 Folder<String> root = new FolderRawText(
    "/root1.png",
    "/root2.png",
    "/root3.png",
    "/music/a.mp3",
    "/music/opus/oA.mp3",
    "/music/opus/oB.mp3",
    "/music/opus/bth/1.mp3",
    "/music/opus/bth/111.mp3",
    "/music/opus/mzt/21.mp3",
    "/music/opus/bch/13.flac",
    "/doc/table.docx");
```

Now, you can work with this object.

### Working with Folder

#### Files
```java
 List<String> files = root.files(); 
// |
// |-> root1.png
// |-> root2.png
// +-> root3.png
```

#### Children
```java
 List<Folder<String>> children = root.children(); 
// |
// |-> Folder("/music")
// +-> Folder("/doc")
```

#### FullName
```java
 List<Folder<String>> children = root.children(); 
 Folder<String> child = children.get(0);
 child.fullName(); // --> /music
```

#### ShortName
```java
 List<Folder<String>> children = root.children(); 
 Folder<String> child = children.get(0);
 child.shortName(); // --> music
```

#### Travel
```java
 root.travel(folder -> {
    String shortName = folder.shortName();
    String fullName = folder.fullName();
    List<String> files = folder.files();
    List<Folder<String>> children = folder.children();
 });
```

Papka creates the following file structure:
```
                          /-------------
                          |            |
                          |            |
                          |            +--> root1.png
                          |            +--> root2.png
                          |            +--> root3.png
                          \/
               +-------------------+
               |                   | 
               \/                  \/
              music                doc
              |   |                |
              \/  |                | 
            opus  +--> a.mp3       +--> table.docx
            |  |
            |  |
            |  +--> oA.mp3
            |  +--> oB.mp3
            |
            \/
 +-----------------------------+
 |             |               |
 \/            \/              \/
 bch           bth             mzt
 |             |               |
 |             |               |
 +--> 13.flac  +--> 1.mp3      +--> 21.mp3
               +--> 111.mp3 
   
```

Method <code>travel()</code> allows you to do a complete traversal of the file tree, starting at the root.
<br>
In our case, <code>travel()</code> starts in folder <code>/</code> (root). Then it moves to <code>doc</code>,
<code>music</code>, <code>opus</code>, <code>bch</code>, <code>bth</code>, and the end is <code>mzt</code>.
<br>
Thus, method <code>travel()</code> traverses the entire file tree.

### Search
Search is carried out through the <code>Search</code> interface.
<br>
<br>
<b>Search files</b>
```java
 Find<List<String>, String> search = new SearchFilesByRegex(root);
 List<String> files = search.answer("a.mp3") // {a.mp3}
```
<b>Search files by extension</b>
```java
 Search<List<String>, String> search = new SearchByExt<>(
                                    new SearchFilesByRegex(root));
 List<String> files = search.answer(".flac") // {13.flac}
```
<b>Search files by start with</b>
```java
 Search<List<String>, String> search = new SearchByStartWith<>(
                                    new SearchFilesByRegex(root));
 List<String> files = search.answer("1") // {1.mp3, 111.mp3, 13.flac}
```

<b>Search folders</b>
```java
 SearchFolders<String, String> search = new SearchFoldersByRegex<>(root);
 List<Folder<String>> folders = search.answer("^b.*$) // {bch, bth}
```
<b>Search folder by full name</b>
```java
 SearchFolder<String, String> search = new SearchFolderByFullName<>(root);
 Folder<String> folder = search.answer("/music/opus/mzt") // mzt
```
<b>Search folder by short name</b>
```java
 SearchFolder<String, String> search = new SearchFolderByShortName<>(root);
 Folder<String> folder = search.answer("doc") // doc
```
### Filter
The interface <code>Filter</code> provides filtering. It differs from <code>Search</code> in that it does not throws 
exceptions.
<br>
<br>
Suppose we have the following raw list of <code>files</code>:
```
/music/sound1.mp3
/music/sound1.mp3
/music/sound2.mp3
/music/sound2.mp3
```
If using <code>Search</code>:
```java
Search<List<String>, List<String>> search = new SearchUniqueByList();
List<String> uqFiles = search.answer(files); // throw NotFoundException
```
If using <code>Filter</code>:
```java
Filter<List<String>> filter = new FilterFilesRaw(
                new SearchUniqueByList<>(),
                files);
List<String> uqFiles = filter.apply(); // return empty List
```
Also <code>Filter</code> can be used for folders: 
```java
Filter<List<Folder<String>>> filter = new FilterFolders<>(
                new SearchByStartWith<>(
                        new SearchFoldersByRegex<>(
                                root)),
                "b");
List<Folder<String>> folders = filter.apply(); // {bch, bth}
```

#### Coping with duplicates
Suppose we have the following raw list of <code>files</code>:
```
/music/sound1.mp3
/music/sound1.mp3
/music/sound2.mp3
/music/sound2.mp3
```
We want to keep one value for each duplicate. We can use `FilterFilesUniqueNormalize`:
Also <code>Filter</code> can be used for folders: 
```java
Filter<List<String>> filter = new FilterFilesUniqueNormalize<>(files);
List<String> normFiles = filter.apply(); // {/music/sound1.mp3, /music/sound2.mp3}
```

### Working with filesystem
Papka supports working with filesystem. 
<br>
For unix like system:
```java
 Folder<File> folder = new FolderFile(
    "/home/papka/file1.txt",
    "/home/papka/file2.txt",
    "/home/papka/file3.txt"
 );
```
For Windows:
```java
 Folder<File> folder = new FolderFileWin(
    "C:\\papka\\file1.txt",
    "C:\\papka\\file2.txt",
    "C:\\papka\\file3.txt"
 );
```
In Windows implementation, the root folder is the same as in Unix implementation, because such a case is possible:
```java
 Folder<File> folder = new FolderFileWin(
    "C:\\papka\\file1.txt",
    "C:\\papka\\file2.txt",
    "C:\\papka\\file3.txt",
    "D:\\papka\\dFile1.txt",
    "D:\\papka\\dFile2.txt"
 );
```

Then `folder.fullName()` is `/`, and `folder.children()` is folder `/\\C:` and `/\\D:`. 
<br>
You can remove all non existing files in filesystem use `excludeNonExisting` parameter:
```java
 boolean excludeNonExisting = true;
 Folder<File> folder = new FolderFileWin(
    excludeNonExisting,
    "C:\\papka\\file1.txt",
    "C:\\papka\\file2.txt",
    "C:\\papka\\file3.txt",
    "D:\\papka\\dFile1.txt",
    "D:\\papka\\dFile2.txt"
 );
```
In Windows implementation you can use unix like files:
```java
 boolean excludeNonExisting = true;
 boolean isUnixLike = true;
 Folder<File> folder = new FolderFileWin(
    excludeNonExisting,
    isUnixLike,
    "/papka/file1.txt",
    "/papka/file2.txt",
    "/C:/papka/file3.txt",
    "D:/papka/dFile1.txt",
    "D:/papka/dFile2.txt"
 );
```
If the drive is not specified in the file name, then system drive will be used by default.

### Searching files in filesystem
You can use `Search` interface to do this:
#### Search files in system
```java
 String beginWith = "/home"; // Files will be searched from here
 Search<List<File>, String> search = new SearchFilesInSystem(beginWith);
 List<File> files = new SearchByExt<>(search).answer(".txt"); // Find all files in folder '/home' with extension '.txt'
```
#### Search folder with files in system
```java
 String beginWith = "/home"; 
 Search<Folder<File>, String> search = new SearchFolderInSystem(name);
 Folder<File> folder = search.answer("documents"); // Find folder with name documents 
```

#### Search files by folder name
```java
 String beginWith = "/home";
 Search<List<File>, String> search = new SearchFilesByFolderNameInSystem(beginWith);
 List<File> files = search.answer("documents"); // Find all files in folder 'documents' 
```

For more information, read the docs [wiki](https://github.com/ViiSE/papka/wiki).
