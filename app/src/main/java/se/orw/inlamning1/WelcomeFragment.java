package se.orw.inlamning1;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Marcus Orwén on 2014-09-18.
 *
 * The welcome fragment that is displayed after user has logged in.
 */
public class WelcomeFragment extends Fragment {
    private Controller controller;
    private TextView tvWelcome;
    private Button btnAddIncome;
    private Button btnAddOutcome;
    private Button btnViewList;
    private TextView tvTotalOutcome;
    private TextView tvTotalIncome;
    private TextView tvExcessDeficit;
    private LinearLayout llGraph;
    private int income = 0;
    private int outcome = 0;
    private int excessDeficit = 0;

    /**
     * Unused constructor
     */
    public WelcomeFragment() {
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
     * Sets the total income.
     *
     * @param income int income
     */
    public void setTotalIncome(int income) {
        this.income = income;
    }

    /**
     * Sets the total outcome.
     *
     * @param outcome int outcome
     */
    public void setTotalOutcome(int outcome) {
        this.outcome = outcome;
    }

    /**
     * Sets the Excess/Deficit.
     *
     * @param excessDeficit int excessDeficit
     */
    public void setExcessDeficit(int excessDeficit) {
        this.excessDeficit = excessDeficit;
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
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);
        if(controller != null) {
            init(view);
            registerListeners();
            controller.updateWelcome();
            tvTotalIncome.setText("Total income: " + income);
            tvTotalOutcome.setText("Total outcome: " + outcome);
            tvExcessDeficit.setText("Excess/Deficit: " + excessDeficit);
            tvWelcome.setText("Welcome " + controller.getFromPreferences("firstName") + " " + controller.getFromPreferences("lastName"));
            ArrayList<Float> data = new ArrayList<Float>();
            data.add((float) income);
            data.add((float) outcome);
            llGraph.addView(new GraphView(getActivity(), controller.calculateData(data)));
        }
        return view;
    }

    /**
     * Initialize the fragments variables.
     *
     * @param view The view of the fragment.
     */
    private void init(View view) {
        tvWelcome = (TextView)view.findViewById(R.id.tvWelcome);
        btnAddIncome = (Button)view.findViewById(R.id.btnAddIncome);
        btnAddOutcome = (Button)view.findViewById(R.id.btnAddOutcome);
        btnViewList = (Button)view.findViewById(R.id.btnViewList);
        tvExcessDeficit = (TextView)view.findViewById(R.id.tvExcessDeficit);
        tvTotalIncome = (TextView)view.findViewById(R.id.tvTotalIncome);
        tvTotalOutcome = (TextView)view.findViewById(R.id.tvTotalOutcome);
        llGraph = (LinearLayout) view.findViewById(R.id.graphLayout);
    }

    /**
     * Register the listeners for the different input types.
     */
    private void registerListeners() {
        btnAddIncome.setOnClickListener(new AddIncomeListener());
        btnAddOutcome.setOnClickListener(new AddOutcomeListener());
        btnViewList.setOnClickListener(new ViewListListener());
    }

    /**
     * The AddIncome button listener
     */
    private class AddIncomeListener implements View.OnClickListener {
        /**
         * Called once the user presses something
         *
         * @param view -
         */
        @Override
        public void onClick(View view) {
            controller.switchTo("IncomeFormFragment");
        }
    }

    /**
     * The AddOutcome button listener
     */
    private class AddOutcomeListener implements View.OnClickListener {
        /**
         * Called once the user presses something
         *
         * @param view -
         */
        @Override
        public void onClick(View view) {
            controller.switchTo("OutcomeFormFragment");
        }
    }

    /**
     * The ViewList button listener
     */
    private class ViewListListener implements View.OnClickListener {
        /**
         * Called once the user presses something
         *
         * @param view -
         */
        @Override
        public void onClick(View view) {
            controller.switchTo("ListInOutcomeFragment");
        }
    }
}
