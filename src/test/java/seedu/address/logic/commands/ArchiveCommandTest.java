package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;

public class ArchiveCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        // Initialize your model mock here
        model = mock(Model.class);
    }

    @Test
    public void execute_archivedFile_success() throws Exception {
        // Create the command to test
        ArchiveCommand archiveCommand = new ArchiveCommand();

        // Execute the command with your model mock
        CommandResult commandResult = archiveCommand.execute(model);

        // Assert that the command execution returns the expected CommandResult
        assertEquals(ArchiveCommand.MESSAGE_SUCCESS, commandResult.getFeedbackToUser());
    }

    @Test
    public void getCommandWord() {
        Command command = new ArchiveCommand();
        assertEquals("archive", command.getCommandWord());
    }

    @Test
    public void getMessageUsage() {
        Command command = new ArchiveCommand();
        assertEquals("archive"
            + ": Archives the current state of the patient book to the file. "
            + "The current file accompanied with a date will be generated for the archived file. "
            + "Example: " + "archive", command.getMessageUsage());
    }
}
