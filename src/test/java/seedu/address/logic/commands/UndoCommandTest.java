package seedu.address.logic.commands;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;

import org.junit.jupiter.api.Test;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

public class UndoCommandTest {

    @Test
    public void execute_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new UndoCommand(), model, UndoCommand.MESSAGE_SUCCESS_UNDO, expectedModel);
    }
}
