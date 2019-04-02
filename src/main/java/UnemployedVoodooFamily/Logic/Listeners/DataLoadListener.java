package UnemployedVoodooFamily.Logic.Listeners;

import UnemployedVoodooFamily.Data.Enums.Data;

/**
 * Listener to notify when a new piece of data has been succesfully loaded
 * Uses the Data enum to specify which piece of data was loaded.
 */
public interface DataLoadListener {
    void dataLoaded(Data d);
}
