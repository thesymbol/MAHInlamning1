package se.orw.inlamning1;


import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Calendar;


/**
 * Created by Marcus Orwén on 2014-09-18.
 *
 * A form fragment that handles outcome.
 */
public class OutcomeFormFragment extends Fragment {
    private DatePickerDialog datePickerDialog;
    private Controller controller;
    private EditText etTitle;
    private EditText etDate;
    private EditText etAmount;
    private RadioGroup rgCategory;
    private Button btnAdd;
    private Button btnDatePicker;

    /**
     * Unused constructor
     */
    public OutcomeFormFragment() {
        // Required empty public constructor
    }

    /**
     * Sets the controller class for this fragment.
     *
     * @param controller The Controller object
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Called once the view is being created
     *
     * @param inflater -
     * @param container -
     * @param savedInstanceState-
     * @return View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outcome_form, container, false);
        init(view);
        registerListeners();
        return view;
    }

    /**
     * Initialize the fragments variables.
     *
     * @param view The view of the fragment.
     */
    private void init(View view) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        etTitle = (EditText)view.findViewById(R.id.etTitle);
        etDate = (EditText)view.findViewById(R.id.etDate);
        etAmount = (EditText)view.findViewById(R.id.etAmount);
        rgCategory = (RadioGroup)view.findViewById(R.id.rgCategory);
        btnAdd = (Button)view.findViewById(R.id.btnAdd);
        btnDatePicker = (Button)view.findViewById(R.id.btnDatePicker);
        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialogListener(), year, month, day);
    }

    /**
     * Register the listeners for the different input types.
     */
    private void registerListeners() {
        btnAdd.setOnClickListener(new OutcomeFormListener());
        btnDatePicker.setOnClickListener(new DatePickerListener());
    }

    /**
     * Handles the input's so that they do things.
     */
    private class OutcomeFormListener implements View.OnClickListener {
        /**
         * Called once the user presses something
         *
         * @param view -
         */
        @Override
        public void onClick(View view) {
            String title = etTitle.getText().toString();
            String date = etDate.getText().toString();
            String amount = etAmount.getText().toString();
            String category = "Other";
            if(rgCategory.getCheckedRadioButtonId() != -1) {
                int rgID = rgCategory.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton)rgCategory.findViewById(rgID);
                int rbID = rgCategory.indexOfChild(radioButton);
                RadioButton btn = (RadioButton) rgCategory.getChildAt(rbID);
                category = btn.getText().toString();
            }
            controller.addToDatabase(title, date, amount, category, "Outcome");
        }
    }

    /**
     * Handles the DatePicker button
     */
    private class DatePickerListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            datePickerDialog.show();
        }
    }

    /**
     * Handles the date picker
     */
    private class DatePickerDialogListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            etDate.setText(year + "-" + ++month + "-" + day);
        }
    }
}
