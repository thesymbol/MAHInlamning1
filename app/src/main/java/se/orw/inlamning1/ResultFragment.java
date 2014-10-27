package se.orw.inlamning1;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by Marcus Orwén on 2014-09-18.
 *
 * A result fragment that displays the result of a income/outcome.
 */
public class ResultFragment extends Fragment {
    private Controller controller;
    private TextView tvTitle;
    private TextView tvDate;
    private TextView tvAmount;
    private TextView tvCategory;
    private TextView tvType;
    private ImageView ivCategory;
    private Button btnRemove;
    private int imageID = R.drawable.other;
    private String title = "-";
    private String date = "-";
    private int amount = 0;
    private String category = "-";
    private String type = "-";
    private int resultID = -1;

    /**
     * Unused constructor
     */
    public ResultFragment() {
        // Required empty public constructor
    }

    /**
     * Sets the text fields
     *
     * @param title The title of the income/outcome
     * @param date The date of the income/outcome
     * @param amount The money/price of the income/outcome
     * @param category The category of the income/outcome
     * @param type The type if its an income or outcome.
     */
    public void setDataResult(String title, String date, int amount, String category, String type, int resultID) {
        this.title = title;
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.type = type;
        this.resultID = resultID;
    }

    /**
     * Set's the image of the result.
     *
     * @param imageID The ID of an image resource.
     */
    public void setImage(int imageID) {
        this.imageID = imageID;
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
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        init(view);
        registerListeners();
        tvTitle.setText("Title: " + title);
        tvDate.setText("Date: " + date);
        tvAmount.setText("Amount: " + amount);
        tvCategory.setText("Category: " + category);
        tvType.setText("Type: " + type);
        ivCategory.setBackground(getResources().getDrawable(imageID));
        return view;
    }

    /**
     * Initialize the fragments variables.
     *
     * @param view The view of the fragment.
     */
    private void init(View view) {
        tvTitle = (TextView)view.findViewById(R.id.tvTitle);
        tvDate = (TextView)view.findViewById(R.id.tvDate);
        tvAmount = (TextView)view.findViewById(R.id.tvAmount);
        tvCategory = (TextView)view.findViewById(R.id.tvCategory);
        tvType = (TextView)view.findViewById(R.id.tvType);
        ivCategory = (ImageView)view.findViewById(R.id.ivCategory);
        btnRemove = (Button)view.findViewById(R.id.btnRemove);
    }

    /**
     * Register the listeners for the different input types.
     */
    private void registerListeners() {
        btnRemove.setOnClickListener(new RemoveListener());
    }

    /**
     * Removes an result from the database
     */
    private class RemoveListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            controller.removeResult(resultID, type);
        }
    }
}
