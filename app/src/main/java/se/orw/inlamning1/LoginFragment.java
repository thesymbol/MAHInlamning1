package se.orw.inlamning1;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 * Created by Marcus Orwén on 2014-09-18.
 *
 * A Login fragment that displays a login UI.
 */
public class LoginFragment extends Fragment {
    private EditText etFirstName;
    private EditText etLastName;
    private LoginButton btnLogin;
    private Controller controller;

    /**
     * Unused constructor
     */
    public LoginFragment() {
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
     * Get the first name of the input field.
     *
     * @return String first name
     */
    public String getFirstNameText() {
        return etFirstName.getText().toString();
    }

    /**
     * Get the last name of the input field.
     *
     * @return String last name
     */
    public String getLastNameText() {
        return etLastName.getText().toString();
    }

    /**
     * Sets the first and last name of the input fields.
     *
     * @param firstName The first name
     * @param lastName The last name
     */
    public void setTexts(String firstName, String lastName) {
        etFirstName.setText(firstName);
        etLastName.setText(lastName);
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
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
        etFirstName = (EditText)view.findViewById(R.id.etFirstName);
        etLastName = (EditText)view.findViewById(R.id.etLastName);
        btnLogin = (LoginButton)view.findViewById(R.id.btnLogin);
    }

    /**
     * Register the listeners for the different input types.
     */
    private void registerListeners() {
        btnLogin.setOnClickListener(new LoginListener());
    }

    /**
     * Handles the input's so that they do things.
     */
    private class LoginListener implements View.OnClickListener {
        /**
         * Called once the user presses something
         *
         * @param view -
         */
        @Override
        public void onClick(View view) {
            controller.loginUser(etFirstName.getText().toString(), etLastName.getText().toString());
        }
    }
}
