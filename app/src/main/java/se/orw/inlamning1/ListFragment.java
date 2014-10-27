package se.orw.inlamning1;


import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Marcus Orwén on 2014-09-18.
 *
 * A fragment that displays income and outcome in a list.
 */
public class ListFragment extends Fragment {
    private DatePickerDialog datePickerDialogStart;
    private DatePickerDialog datePickerDialogEnd;
    private ListView lvIncomeOutcome;
    private ListAdapter adapter;
    private EditText etStartDate;
    private EditText etEndDate;
    private Button btnStartDateSelect;
    private Button btnEndDateSelect;
    private Button btnFilter;
    private Button btnReset;
    private Controller controller;

    /**
     * Unused Constructor
     */
    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Set the adapter for the list
     *
     * @param adapter The ListIncomeOutcomeAdapter object
     */
    public void setAdapter(ListAdapter adapter) {
        this.adapter = adapter;
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
     * @param savedInstanceState -
     * @return View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        if(controller != null) {
            init(view);
            registerListeners();
        }
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

        controller.updateList(null, null);
        adapter.notifyDataSetChanged();
        lvIncomeOutcome = (ListView)view.findViewById(R.id.lvIncomeOutcome);
        lvIncomeOutcome.setAdapter(adapter);
        etStartDate = (EditText) view.findViewById(R.id.etStartDate);
        etEndDate = (EditText) view.findViewById(R.id.etEndDate);
        btnStartDateSelect = (Button) view.findViewById(R.id.btnStartDateSelect);
        btnEndDateSelect = (Button) view.findViewById(R.id.btnEndDateSelect);
        btnFilter = (Button) view.findViewById(R.id.btnFilter);
        btnReset = (Button) view.findViewById(R.id.btnReset);
        datePickerDialogStart = new DatePickerDialog(getActivity(), new DatePickerDialogStartListener(), year, month, day);
        datePickerDialogEnd = new DatePickerDialog(getActivity(), new DatePickerDialogEndListener(), year, month, day);
    }

    /**
     * Register the listeners for the different input types.
     */
    private void registerListeners() {
        lvIncomeOutcome.setOnItemClickListener(new IncomeOutcomeListListener());
        btnStartDateSelect.setOnClickListener(new SelectStartDateListener());
        btnEndDateSelect.setOnClickListener(new SelectEndDateListener());
        btnFilter.setOnClickListener(new FilterListListener());
        btnReset.setOnClickListener(new ResetListListener());
    }

    /**
     * Handles the list so that it is clickable.
     */
    private class IncomeOutcomeListListener implements android.widget.AdapterView.OnItemClickListener {
        /**
         * Called once an item is clicked
         *
         * @param parent -
         * @param view -
         * @param position The position in the list
         * @param id -
         */
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            controller.listItemClicked(position);
        }
    }

    /**
     * Handles the start filter date picker button
     */
    private class SelectStartDateListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            datePickerDialogStart.show();
        }
    }

    /**
     * Handles the end filter date picker button
     */
    private class SelectEndDateListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            datePickerDialogEnd.show();
        }
    }

    /**
     * Handles the filter button
     */
    private class FilterListListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = null;
            Date endDate = null;
            try {
                startDate = format.parse(etStartDate.getText().toString());
                endDate = format.parse(etEndDate.getText().toString());
            } catch (ParseException e) {
                Toast.makeText(getActivity(), "Error invalid date entered", Toast.LENGTH_SHORT).show();
            }
            controller.updateList(startDate, endDate);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Handles the reset filter button
     */
    private class ResetListListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            etStartDate.setText(null);
            etEndDate.setText(null);
            controller.updateList(null, null);
            adapter.notifyDataSetChanged();
            Toast.makeText(getActivity(), "Reset list", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Handles the start date picker
     */
    private class DatePickerDialogStartListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            etStartDate.setText(year + "-" + ++month + "-" + day);
        }
    }

    /**
     * Handles the end date picker
     */
    private class DatePickerDialogEndListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            etEndDate.setText(year + "-" + ++month + "-" + day);
        }
    }
}
