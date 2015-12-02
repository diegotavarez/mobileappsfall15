package edu.uco.dtavarespereira.wanderlust.util;
import android.widget.EditText;
import android.content.Context;

import java.util.regex.Pattern;

import edu.uco.dtavarespereira.wanderlust.R;

/**
 * Created by diegotavarez on 11/17/15.
 */
public class FieldValidation {
    private final Context context;

    /** The max number of digits in value field. */
    private static final int MAXDIGITS = 8;

    //Regular Expression
    /** The currency regex. */
    private static final String CURRENCYREGEX =
            "(0|[1-9]+[0-9]*)?((\\.[0-9]*)|(\\[.|,][0-9]*))?";

    /**
     * Instantiates a new field validation.
     *
     * @param c the c
     */
    public FieldValidation(final Context c) {
        context = c;
    }

    public final boolean isValid(final EditText editText, final String regex,
        final String errMsg, final boolean required) {
        editText.setError(null);
        if (required && !hasText(editText)) {
            return false;
        }
        if (required && !Pattern.matches(
                regex, editText.getText().toString())) {
            editText.setError(errMsg);
            return false;
        }
        return true;
    }

    /**
     * check the input field has any text or not.
     *
     * @param editText to be verified
     * @return true if it contains text otherwise false
     */
    public final boolean hasText(final EditText editText) {
        final String text = editText.getText().toString().trim();
        editText.setError(null);
        if (text.length() == 0) {
            editText.setError("Required");
            return false;
        }
        return true;
    }

}
