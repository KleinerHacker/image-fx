package org.pcsoft.tools.image_fx.ui.utils.actions;

import org.pcsoft.tools.image_fx.ui.utils.HistoryStack;

/**
 * Created by pfeifchr on 21.08.2014.
 */
public final class MultiHistoryAction implements HistoryStack.Action {

    private final HistoryStack.Action[] actions;

    public MultiHistoryAction(HistoryStack.Action... actions) {
        if (actions == null || actions.length <= 0)
            throw new IllegalArgumentException("Actions parameter is empty or null!");

        this.actions = actions;
    }

    @Override
    public void undo() {
        for (final HistoryStack.Action action : actions) {
            action.undo();
        }
    }

    @Override
    public void redo() {
        for (final HistoryStack.Action action : actions) {
            action.redo();
        }
    }

    @Override
    public String getActionName() {
        final StringBuilder sb = new StringBuilder();
        for (final HistoryStack.Action action : actions) {
            sb.append(action.getActionName()).append(" / ");
        }
        sb.delete(sb.length() - 3, sb.length());

        return sb.toString();
    }
}
