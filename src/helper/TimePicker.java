package helper;

import javafx.scene.control.SpinnerValueFactory;

public class TimePicker {
    public static SpinnerValueFactory<Integer> setHours() {
        SpinnerValueFactory<Integer> hourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23);
        hourFactory.setValue(0);
        return hourFactory;
    }

    public static SpinnerValueFactory<Integer> setMinutes() {
        return new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59);
    }
}
