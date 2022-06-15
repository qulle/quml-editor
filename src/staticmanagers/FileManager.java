package staticmanagers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystemException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import main.Editor;
import main.Model;

/**
 * Handles save and open of the model and export of PNG image
 */
public final class FileManager {
    private FileManager() {}
    private static final FileFilter modelFilter = new FileNameExtensionFilter("QUML file format (.quml)", "quml");
    private static final FileFilter imageFilter = new FileNameExtensionFilter("Portable Network Graphics (.png)", "png");

    // Handle if can be saved to existing path or save as new file
    public static void save(Model model) {
        String filePath = model.getFilePath();

        if(filePath == null) {
            saveAs(model);
        }else {
            save(model, filePath);
        }
    }

    // Force save as new file
    public static void saveAs(Model model) {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.addChoosableFileFilter(modelFilter);

        if(jfc.showSaveDialog(Editor.window) == JFileChooser.APPROVE_OPTION) {
            String path = jfc.getSelectedFile().getAbsolutePath();

            // Check if path has correct file-extension
            if(!path.endsWith(".quml")) {
                path += ".quml";
            }

            // Save file
            save(model, path);
        }
    }

    // Help-method to save file
    private static void save(Model model, String path) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(path)));

            oos.writeObject(model);
            oos.close();

            model.setFilePath(path);
            model.getDrawingSurface().updateTabLabel();

            JOptionPane.showMessageDialog(Editor.window, "Model was saved to file");
        }catch (IOException e) {
            JOptionPane.showMessageDialog(Editor.window, "The file could not be saved [ " + e.getMessage() + " ]");
        }
    }

    // Load serialised model from file
    public static Model loadFromFile() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.addChoosableFileFilter(modelFilter);

        if(jfc.showOpenDialog(Editor.window) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();

            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(selectedFile.getAbsolutePath())));
                Model model = (Model)(ois.readObject());

                model.setFilePath(selectedFile.getAbsolutePath());
                ois.close();
                return model;
            }catch (FileSystemException | ClassNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e) {
                JOptionPane.showMessageDialog(Editor.window, "The file could not be opened [ " + e.getMessage() + " ]");
            }catch (ClassCastException e) {
                JOptionPane.showMessageDialog(Editor.window, "This file was created with an older version");
            }catch(Exception e) {
                JOptionPane.showMessageDialog(Editor.window, "Unexpected error [ " + e.getMessage() + " ]");
            }
        }

        return null;
    }

    // Save drawing as a image
    public static void saveImage(BufferedImage image) {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.addChoosableFileFilter(imageFilter);

        if(jfc.showSaveDialog(Editor.window) == JFileChooser.APPROVE_OPTION) {
            String path = jfc.getSelectedFile().getAbsolutePath();

            // Check if path has correct file-extension
            if(!path.endsWith(".png")) {
                path += ".png";
            }

            try {
                ImageIO.write(image, "png", new File(path));
                JOptionPane.showMessageDialog(Editor.window, "Image was exported to file");
            }catch (IOException e) {
                JOptionPane.showMessageDialog(Editor.window, "The image could not be exported [ " + e.getMessage() + " ]");
            }
        }
    }
}