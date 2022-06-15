# QUML - UML Editor
### UML editor developed in Java with Swing

## About
The UML-editor was created back in 2018 and was developed in IntelliJ IDEA Community Edition 2018.3.1. I thought it would be of more use in my University era projects here on GitHub than collecting bit-dust on some harddrive of mine.

## Screenshot
![Screenshot of the editor in action](images/quml.png?raw=true "Screenshot of the editor in action")

## Functionality
The editor has actually got some advanced features. 

- It is possible to work in multiple tabs on separate diagrams. 
- It is also possible to copy paste between the tabs. 
- Custom undo / redo functionality using stacks to push / pop undoable items from. 
- Serialize and save the diagram to a file. It re-attaches all the listeners when the file is opened again.
- The lines are drawn using three type of modes, direct-, semi-, and bent mode. The bent mode will find a way between two points and adapt when the boxes are moved.
- Drag to select objects.
- Resize objects.
- Custom designed icons using Illustrator.

Why i haven't finished the project is because the end goal was always pushed further away. I thought that the program must be as good as commercial editors, a rather naive approach. A rough estimate is that the project might be 95% done with the existing functionality that has been added.

## Java version
The Java version i used to compile this editor was.
```
$ javac -version
javac 9.0.4

$ java -version
java 9.0.4
```

## Compiling
Both of the commands must be run from the src directory.

Compile the program. 
```
$ javac -d ../out ./main/StartScreen.java
```

Copy the icons directory to the out directory.
```
$ cp ./icons/ ../out/ -r
```

## Running
Run the program. Must be ran from the out directory.
```
$ java -Dfile.encoding=UTF-8 -classpath . main.StartScreen
```

![Screenshot of the terminal](images/terminal.png?raw=true "Screenshot of the terminal")

## More images

### Startscreen
![Screenshot of the startscreen](images/startscreen.png?raw=true "Screenshot of the startscreen")

### Editor
![Screenshot of the editor](images/editor.png?raw=true "Screenshot of the editor")
Shows window for editing lines and the red helper-line to show to what anchor point the line-text is connected to when the text is moved.

## Author
[Qulle](https://github.com/qulle/)