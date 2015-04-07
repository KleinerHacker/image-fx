package org.pcsoft.tools.image_fx.ui.utils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pfeifchr on 18.06.2014.
 */
public class HistoryStack {

    public static interface Action {
        void undo();

        void redo();

        String getActionName();
    }

    private final List<Action> actionStack = new ArrayList<>();
    private int currentIndex = 0;

    private final BooleanProperty canUndo = new ReadOnlyBooleanWrapper(false);
    private final BooleanProperty canRedo = new ReadOnlyBooleanWrapper(false);
    private final StringProperty undoMessage = new ReadOnlyStringWrapper("Undo");
    private final StringProperty redoMessage = new ReadOnlyStringWrapper("Redo");

    public void addToStack(Action obj) {
        //Clear redo list
        for (int i = 0; i < currentIndex; i++) {
            actionStack.remove(0);
        }

        actionStack.add(0, obj);
        currentIndex = 0;
        updateState();
    }

    public void undo() {
        if (currentIndex + 1 >= actionStack.size())
            return;

        actionStack.get(currentIndex).undo();

        currentIndex++;
        updateState();
    }

    public void redo() {
        if (currentIndex - 1 < 0)
            return;

        currentIndex--;
        updateState();

        actionStack.get(currentIndex).redo();
    }

    public boolean canUndo() {
        return canUndo.get();
    }

    public BooleanProperty canUndoProperty() {
        return canUndo;
    }

    public boolean canRedo() {
        return canRedo.get();
    }

    public BooleanProperty canRedoProperty() {
        return canRedo;
    }

    public String getUndoMessage() {
        return undoMessage.get();
    }

    public StringProperty undoMessageProperty() {
        return undoMessage;
    }

    public String getRedoMessage() {
        return redoMessage.get();
    }

    public StringProperty redoMessageProperty() {
        return redoMessage;
    }

    public void clearStack() {
        actionStack.clear();
        currentIndex = 0;
        updateState();
    }

    private void updateState() {
        canUndo.set(currentIndex + 1 < actionStack.size());
        canRedo.set(currentIndex - 1 >= 0);

        if (canUndo.get()) {
            undoMessage.set("Undo: " + actionStack.get(currentIndex).getActionName());
        } else {
            undoMessage.set("Undo");
        }
        if (canRedo.get()) {
            redoMessage.set("Redo: " + actionStack.get(currentIndex - 1).getActionName());
        } else {
            redoMessage.set("Redo");
        }
    }

}
