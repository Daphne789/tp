package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

public class CloneCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToClone = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        CloneCommand cloneCommand = new CloneCommand(INDEX_FIRST_PERSON);

        // Creating the expected cloned person
        Person expectedClonedPerson = createClonedPerson(personToClone);

        String expectedMessage = String.format(CloneCommand.MESSAGE_CLONE_PERSON_SUCCESS,
                Messages.format(expectedClonedPerson));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(expectedClonedPerson);

        assertCommandSuccess(cloneCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        CloneCommand cloneCommand = new CloneCommand(outOfBoundIndex);

        assertCommandFailure(cloneCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToClone = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        CloneCommand cloneCommand = new CloneCommand(INDEX_FIRST_PERSON);

        // Creating the expected cloned person
        Person expectedClonedPerson = createClonedPerson(personToClone);

        String expectedMessage = String.format(CloneCommand.MESSAGE_CLONE_PERSON_SUCCESS,
                Messages.format(expectedClonedPerson));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(expectedClonedPerson);
        showNoPerson(expectedModel);

        assertCommandSuccess(cloneCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_FIRST_PERSON; // Try to clone the first person in the filtered list
        CloneCommand cloneCommand = new CloneCommand(outOfBoundIndex);

        assertCommandFailure(cloneCommand, model, CloneCommand.MESSAGE_CLONE_PERSON_DUPLICATE_FAILURE);
    }

    @Test
    public void equals() {
        CloneCommand cloneFirstCommand = new CloneCommand(INDEX_FIRST_PERSON);
        CloneCommand cloneSecondCommand = new CloneCommand(INDEX_FIRST_PERSON);

        // same object -> returns true
        assertTrue(cloneFirstCommand.equals(cloneFirstCommand));

        // same values -> returns true
        CloneCommand cloneFirstCommandCopy = new CloneCommand(INDEX_FIRST_PERSON);
        assertTrue(cloneFirstCommand.equals(cloneFirstCommandCopy));

        // different types -> returns false
        assertFalse(cloneFirstCommand.equals(1));

        // null -> returns false
        assertFalse(cloneFirstCommand.equals(null));

        // different indexes -> returns false
        assertFalse(cloneFirstCommand.equals(cloneSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        CloneCommand cloneCommand = new CloneCommand(targetIndex);
        String expected = CloneCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, cloneCommand.toString());
    }

    /**
     * Creates a cloned person based on the given person.
     */
    private Person createClonedPerson(Person personToClone) {
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
        return new Person(cloneName, personToClone.getPhone(), personToClone.getEmail(),
                personToClone.getOccupation(), personToClone.getAddress(), personToClone.getApptDate(),
                personToClone.getTags());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}