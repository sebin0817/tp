package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

public class SaveCommand extends Command {

  public static final String COMMAND_WORD = "save";

  public static final String MESSAGE_USAGE = COMMAND_WORD
      + ": Saves the current state of the patient book to the file. An id will be generated for the saved file. "
      + "Example: " + COMMAND_WORD;

  @Override
  public CommandResult execute(Model model) throws CommandException {
    // TODO: Implement saving to a file
    // 1. Copy the current state of addressbook.json into a new file
    // 2. Generate an id for the new file
    int id = 0;
    return new CommandResult("Saved to file: " + id);
  }

  @Override
  public String getCommandWord() {
    return COMMAND_WORD;
  }

  @Override
  public String getMessageUsage() {
    return MESSAGE_USAGE;
  }

}
