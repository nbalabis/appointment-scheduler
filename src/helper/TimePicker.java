package helper;

import javafx.scene.control.SpinnerValueFactory;

/**
 * Helper to set TimePickers for appointment time selections.
 *
 * @author Nicholas Balabis
 */
public class TimePicker {
    /**
     * Sets Hour spinner.
     *
     * @return SpinnerValueFactory for setting hour spinner.
     */
    public static SpinnerValueFactory<Integer> setHours() {
        SpinnerValueFactory<Integer> hourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23);
        hourFactory.setValue(0);
        return hourFactory;
    }

    /**
     * Sets Minute Spinner
     *
     * @return SpinnerValueFactory for setting minute spinner.
     */
    public static SpinnerValueFactory<Integer> setMinutes() {
        return new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59);
    }
}
