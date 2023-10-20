package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;

/**
 * CLones a person identified using it's displayed index from the address book.
 */
public class CloneCommand extends Command {

    public static final String COMMAND_WORD = "clone";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Clones the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_CLONE_PERSON_SUCCESS = "Cloned Person: %1$s";

    public static final String MESSAGE_CLONE_PERSON_DUPLICATE_FAILURE = "A clone of this person already exists. To "
        + "clone again, please edit the previous clone first or alternatively, clone the previous clone.";

    private final Index targetIndex;

    public CloneCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToClone = lastShownList.get(targetIndex.getZeroBased());

        Person clonedPerson = clonedPerson(personToClone);
        try {
            model.addPerson(clonedPerson);
            return new CommandResult(String.format(MESSAGE_CLONE_PERSON_SUCCESS, Messages.format(personToClone)));
        } catch (DuplicatePersonException e) {
            System.out.println("");
            return new CommandResult(String.format(MESSAGE_CLONE_PERSON_DUPLICATE_FAILURE));
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CloneCommand)) {
            return false;
        }

        CloneCommand otherCloneCommand = (CloneCommand) other;
        return targetIndex.equals(otherCloneCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }

    private Person clonedPerson(Person personToClone) {
        int intValue;
        String intValueS = personToClone.getName().toString().replaceAll("[^0-9]", "");
        String bareName = personToClone.getName().toString().replaceAll("[0-9]", "");
        if (intValueS.isEmpty()) {
            intValue = 0;
        } else {
            intValue = Integer.parseInt(intValueS);
        }
        intValue++;
        String intValueSNew = String.valueOf(intValue);
        Name cloneName = new Name(bareName + " " + intValueSNew);
        Person clonedPerson = new Person(cloneName, personToClone.getPhone(), personToClone.getEmail(),
                personToClone.getOccupation(), personToClone.getAddress(), personToClone.getApptDate(),
                personToClone.getTags());
        return clonedPerson;
    }
}
