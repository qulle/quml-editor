package staticmanagers;

import chartobjects.ChartObject;
import chartobjects.Connector;
import lines.ChartLine;
import main.Model;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public final class CopyPasteManager {
    private CopyPasteManager() {}
    private static transient Set<ChartObject> copyPaste = new HashSet<>();

    public static void copy(Model model) {
        // Clear prev copied objects, iff there is selected objects
        if(!model.getSelectedObjects().isEmpty()) {
            copyPaste.clear();

            // Sets to remember what lines that needs to be copied
            Set<ChartLine> linesToCopyHelp = new HashSet<>();
            Set<ChartLine> linesToCopy = new HashSet<>();
            List<ChartLine> copiedLines = new ArrayList<>();

            // Keeps track of the original chartObject and the copy
            // Makes its easier to copy the lines and attach them to the copied objects connector
            Map<ChartObject, ChartObject> originalAndCopyPaired = new HashMap<>();

            // Copy all selected objects
            for(ChartObject co : model.getSelectedObjects()) {
                ChartObject copy = co.copy();

                // Store original and copied as a mapped pair
                originalAndCopyPaired.put(co, copy);

                // Store the new copy so that is can be pasted later
                copyPaste.add(copy);

                // Find what lines to copy, only lines explicitly between two chartObjects that both are copied
                for(ChartLine line : co.getLines()) {
                    if(line.getStartChartObject().equals(line.getEndChartObject())) {
                        linesToCopy.add(line);
                    }

                    if(linesToCopyHelp.contains(line)) {
                        linesToCopy.add(line);
                        linesToCopyHelp.remove(line);
                    }else {
                        linesToCopyHelp.add(line);
                    }
                }
            }

            // Copy the lines
            for(ChartLine line : linesToCopy) {
                copiedLines.add(line.copy());
            }

            // Add lines to model and Connect the copied line with its copied chartObject
            for(ChartLine line : copiedLines) {
                // Prepare values
                ChartObject ob1 = originalAndCopyPaired.get(line.getStartChartObject());
                ChartObject ob2 = originalAndCopyPaired.get(line.getEndChartObject());
                Connector c1 = ob1.getConnector(line.getStartConnector().getPosition());
                Connector c2 = ob2.getConnector(line.getEndConnector().getPosition());

                // Set the copied lines new connectors
                line.setStartConnector(c1);
                line.setEndConnector(c2);

                // Add line to both chartObjects
                ob1.addLine(line);
                ob2.addLine(line);
            }
        }
    }

    public static void paste(Model model) {
        model.deSelectAll();

        for(ChartObject co : copyPaste) {
            // Offset pasted objects
            co.setLocation(co.getX() + 200, co.getY() + 100);

            // Update so that the object has the right model
            // It can be a new instance from which it was copied
            co.setModel(model);

            model.add(co);

            for(ChartLine line : co.getLines()) {
                line.setModel(model);
                model.addLine(line);
            }

            // Select the object so it easily can be moved, after the paste
            // And to that it can be copied again
            model.setSelected(co, true);
        }

        // Update all lines to make sure they are correctly drawn
        model.updateLines();

        // Copy objects that was pasted, enabling multiple paste
        CopyPasteManager.copy(model);
    }
}