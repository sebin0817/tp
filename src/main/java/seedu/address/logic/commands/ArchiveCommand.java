package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

public class ArchiveCommand extends Command {

  public static final String COMMAND_WORD = "archive";

  public static final String MESSAGE_USAGE = COMMAND_WORD
      + ": Archives the current state of the patient book to the file. "
      + "The current file accompanied with a date will be generated for the archived file. "
      + "Example: " + COMMAND_WORD;

  @Override
  public CommandResult execute(Model model) throws CommandException {
    return new CommandResult("File archived", false, true, false);
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
