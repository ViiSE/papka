# Papka
Object-Oriented library for working with file tree.

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
Search is carried out through the <code>Find</code> interface.
<br>
<br>
<b>Find files</b>
```java
 Find<List<String>, String> find = new FindFilesByRegex(root);
 List<String> files = find.answer("a.mp3") // {a.mp3}
```
<b>Find files by extension</b>
```java
 Find<List<String>, String> find = new FindByExt<>(
                                    new FindFilesByRegex(root));
 List<String> files = find.answer(".flac") // {13.flac}
```
<b>Find files by start with</b>
```java
 Find<List<String>, String> find = new FindByStartWith<>(
                                    new FindFilesByRegex(root));
 List<String> files = find.answer("1") // {1.mp3, 111.mp3, 13.flac}
```

<b>Find folders</b>
```java
 FindFolders<String, String> find = new FindFoldersByRegex<>(root);
 List<Folder<String>> folders = find.answer("bch") // {bch}
```
<b>Find folder by full name</b>
```java
 FindFolder<String, String> find = new FindFoldersByFullName<>(root);
 Folder<String> folder = find.answer("/music/opus/mzt") // mzt
```
<b>Find folder by short name</b>
```java
 FindFolder<String, String> find = new FindFoldersByShortName<>(root);
 Folder<String> folder = find.answer("doc") // doc
```
### Filter
The interface <code>Filter</code> provides filtering. It differs from <code>Find</code> in that it does not throws 
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
If using <code>Find</code>:
```java
Find<List<String>, List<String>> find = new FindUniqueByList();
List<String> uqFiles = find.answer(files); // throw NotFoundException
```
If using <code>Filter</code>:
```java
Filter<List<String>> filter = new FilterFilenamesRaw(
                new FindUniqueByList<>(),
                root);
List<String> uqFiles = filter.apply(); // return empty List
```
Also <code>Filter</code> can be used for folders: 
```java
Filter<List<Folder<String>>> filter = new FilterFolders<>(
                new FindByStartWith<>(
                        new FindFoldersByRegex<>(
                                root)),
                "b");
List<Folder<String>> folders = filter.apply(); // {bch, bth}
```
For more information, read the docs [wiki](https://github.com/ViiSE/papka/wiki).
