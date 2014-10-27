package se.orw.inlamning1;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Marcus Orwén on 2014-09-18.
 *
 * The main controller of the application
 */
public class Controller {
    private ArrayList<AdapterData> listItems = new ArrayList<AdapterData>();
    private DatabaseController databaseController;
    private SharedPreferences preferences;
    private Activity activity;
    // Fragments
    private FragmentManager fragmentManager;
    private LoginFragment loginFragment;
    private WelcomeFragment welcomeFragment;
    private ListFragment listFragment;
    private IncomeFormFragment incomeFormFragment;
    private OutcomeFormFragment outcomeFormFragment;
    private ResultFragment resultFragment;
    // Variables
    private int userID = 0;

    /**
     * The constructor of the controller
     *
     * @param activity The main activity
     */
    public Controller(Activity activity) {
        this.activity = activity;
        init();
        switchTo(""); // default fragment to load on startup
    }

    /**
     * Initialize the controllers variables.
     */
    private void init() {
        fragmentManager = activity.getFragmentManager();
        preferences = activity.getSharedPreferences("userPref", Context.MODE_PRIVATE);

        databaseController = new DatabaseController(activity);

        loginFragment = new LoginFragment();
        loginFragment.setController(this);

        welcomeFragment = new WelcomeFragment();
        welcomeFragment.setController(this);

        incomeFormFragment = new IncomeFormFragment();
        incomeFormFragment.setController(this);

        outcomeFormFragment = new OutcomeFormFragment();
        outcomeFormFragment.setController(this);

        listFragment = new ListFragment();
        listFragment.setController(this);
        listFragment.setAdapter(new ListAdapter(activity, listItems));

        resultFragment = new ResultFragment();
        resultFragment.setController(this);
    }

    /**
     * Called once the application is paused, (not fully closed).
     */
    public void onPause() {
        String firstName = loginFragment.getFirstNameText();
        String lastName = loginFragment.getLastNameText();
        SharedPreferences.Editor editor = preferences.edit();
        if(firstName != null && firstName.length() > 0 && lastName != null && firstName.length() > 0) {
            editor.putString("firstName", firstName);
            editor.putString("lastName", lastName);
        } else {
            editor.remove("firstName");
            editor.remove("lastName");
        }
    }

    /**
     * Called once the application resumes from paused state.
     */
    public void onResume() {
        loginFragment.setTexts(getFromPreferences("firstName"), getFromPreferences("lastName"));
    }

    /**
     * Set the fragment on the main activity
     *
     * @param fragment The fragment to display
     * @param backstack If we should allow the back button to move back to previous fragment.
     */
    private void setFragment(Fragment fragment, boolean backstack) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_economy, fragment);
        if(backstack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    /**
     * Switch to different fragments
     *
     * @param fragment The fragment's name (case sensitive).
     */
    public void switchTo(String fragment) {
        if (fragment.equals("IncomeFormFragment")) {
            setFragment(incomeFormFragment, true);
        } else if (fragment.equals("OutcomeFormFragment")) {
            setFragment(outcomeFormFragment, true);
        } else if (fragment.equals("ListInOutcomeFragment")) {
            setFragment(listFragment, true);
        } else if (fragment.equals("WelcomeFragment")) {
            setFragment(welcomeFragment, true);
        } else if(fragment.equals("ResultFragment")) {
            setFragment(resultFragment, true);
        } else {
            setFragment(loginFragment, false);
        }
    }

    /**
     * Login the user with the specified first and last name
     *
     * @param firstName The users first name.
     * @param lastName The users last name.
     */
    public void loginUser(String firstName, String lastName) {
        boolean empty = true;
        SharedPreferences.Editor editor = preferences.edit();
        if(!firstName.isEmpty() && !lastName.isEmpty()) {
            editor.putString("firstName", firstName);
            editor.putString("lastName", lastName);
            empty = false;
        } else {
            editor.remove("firstName");
            editor.remove("lastName");
        }
        editor.commit();

        if(!empty) {
            databaseController.open();
            userID = databaseController.getUserID(firstName, lastName);
            if(userID < 1) {
                databaseController.addUser(firstName, lastName);
                userID = databaseController.getUserID(firstName, lastName);
            }
            databaseController.close();
            switchTo("WelcomeFragment");
        } else {
            Toast.makeText(activity, R.string.emptyField, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Update the welcome screen's information
     */
    public void updateWelcome() {
        databaseController.open();
        Cursor incomeCursor = databaseController.getAllIncome(userID);
        Cursor outcomeCursor = databaseController.getAllOutcome(userID);

        int totalIncome = 0;
        int totalOutcome = 0;
        int excessDeficit;

        // update total income
        if(incomeCursor.moveToFirst()) {
            do {
                totalIncome += incomeCursor.getInt(4);
            } while(incomeCursor.moveToNext());
        }

        // update total outcome
        if(outcomeCursor.moveToFirst()) {
            do {
                totalOutcome += outcomeCursor.getInt(4);
            } while(outcomeCursor.moveToNext());
        }

        // update excess / deficit
        excessDeficit = totalIncome - totalOutcome;

        welcomeFragment.setTotalIncome(totalIncome);
        welcomeFragment.setTotalOutcome(totalOutcome);
        welcomeFragment.setExcessDeficit(excessDeficit);

        incomeCursor.close();
        outcomeCursor.close();
        databaseController.close();
    }

    /**
     * Get the preference value by a key.
     *
     * @return String the preference value
     */
    public String getFromPreferences(String key) {
        String value = "";
        if(preferences.contains(key)) {
            value = preferences.getString(key, "");
        }

        return value;
    }

    /**
     * Convert the data to a 360 circle.
     *
     * @param data The data to insert to the circle diagram.
     * @return ArrayList<Float>
     */
    public ArrayList<Float> calculateData(ArrayList<Float> data) {
        float total = 0;
        for(int i = 0; i < data.size(); i++) { // calculate total
            total += data.get(i);
        }

        for(int i = 0; i < data.size(); i++) { // add the 360 degrese algo.
            data.set(i, (360 * data.get(i) / total));
        }
        return data;
    }

    /**
     * Add income or outcome to database.
     *
     * @param title The income title.
     * @param date The date of the income.
     * @param amount The amount of money/price.
     * @param category The category of the income.
     * @param type The type (income / outcome).
     */
    public void addToDatabase(String title, String date, String amount, String category, String type) {
        int iAmount = 0;
        if(!amount.isEmpty()) {
            iAmount = Integer.parseInt(amount);
        }

        if(!title.isEmpty() && !date.isEmpty() && iAmount > 0 && !category.isEmpty()) {
            if(type == "Income") {
                databaseController.open();
                databaseController.addIncomeEntry(title, date, iAmount, category, userID);
                databaseController.close();
                Toast.makeText(activity, R.string.added, Toast.LENGTH_SHORT).show();
            } else if (type == "Outcome") {
                databaseController.open();
                databaseController.addOutcomeEntry(title, date,iAmount, category, userID);
                databaseController.close();
                Toast.makeText(activity, R.string.added, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "Error did not specify type", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, R.string.emptyField, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Updates the list of income/outcome.
     *
     * @param startDate - The start date to search from (leave "" to disable date search)
     * @param endDate - The end date to search to (leave "" to disable date search
     */
    public void updateList(Date startDate, Date endDate) {
        AdapterData adapterData;
        listItems.clear();
        databaseController.open();
        Cursor allIncomeOutcome;
        allIncomeOutcome = databaseController.getAllIncomeOutcome(userID);
        if(allIncomeOutcome.moveToFirst()) {
            do {
                adapterData = new AdapterData();
                adapterData.setText("Title: " + allIncomeOutcome.getString(2) + "\nDate: " + allIncomeOutcome.getString(1) + "\nAmount: " + allIncomeOutcome.getString(4) + "\nCategory: " + allIncomeOutcome.getString(3) + "\nType: " + allIncomeOutcome.getString(5));
                if(allIncomeOutcome.getString(5).equals("Income")) {
                    adapterData.setImageID(getCategoryImage(""));
                } else {
                    adapterData.setImageID(getCategoryImage(allIncomeOutcome.getString(3)));
                }

                if(startDate != null && endDate != null) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date databaseDate = format.parse(allIncomeOutcome.getString(1));
                        if((databaseDate.after(startDate) || databaseDate.equals(startDate)) && (databaseDate.before(endDate) || databaseDate.equals(endDate))) {
                            listItems.add(adapterData);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    listItems.add(adapterData);
                }
            } while(allIncomeOutcome.moveToNext());
        }
        allIncomeOutcome.close();
        databaseController.close();
    }

    /**
     * Called once an list item is clicked, to show a more detailed page.
     *
     * @param position The position of the item clicked
     */
    public void listItemClicked(int position) {
        databaseController.open();
        Cursor allIncomeOutcome = databaseController.getAllIncomeOutcome(userID);

        if(allIncomeOutcome.moveToFirst()) {
            do {
                if(allIncomeOutcome.getPosition() == position) {
                    resultFragment.setDataResult(allIncomeOutcome.getString(2), allIncomeOutcome.getString(1), allIncomeOutcome.getInt(4), allIncomeOutcome.getString(3), allIncomeOutcome.getString(5), allIncomeOutcome.getInt(0));
                    break;
                }
            } while(allIncomeOutcome.moveToNext());
        }

        if(allIncomeOutcome.getString(5).equals("Income")) {
            resultFragment.setImage(getCategoryImage(""));
        } else {
            resultFragment.setImage(getCategoryImage(allIncomeOutcome.getString(3)));
        }

        allIncomeOutcome.close();
        databaseController.close();
        switchTo("ResultFragment");
    }

    /**
     * Show image based on the category.
     *
     * @param category The category of the income/outcome.
     */
    private int getCategoryImage(String category) {
        if(category.equals("Food")) {
            return R.drawable.food;
        } else if(category.equals("Free-time")) {
            return R.drawable.freetime;
        } else if(category.equals("Travel")) {
            return R.drawable.traveling;
        } else if(category.equals("Living")) {
            return R.drawable.living;
        } else if(category.equals("Other")) {
            return R.drawable.other;
        } else {
            return R.drawable.blank;
        }
    }

    /**
     * Removes a result from the database
     *
     * @param resultID - The id of the income/outcome result.
     * @param type - The type of the result (Income or Outcome)
     */
    public void removeResult(int resultID, String type) {
        databaseController.open();
        if(type.equals("Income")) {
            databaseController.removeIncomeEntry(resultID);
            Toast.makeText(activity, "Income removed", Toast.LENGTH_SHORT).show();
        } else if(type.equals("Outcome")) {
            databaseController.removeOutcomeEntry(resultID);
            Toast.makeText(activity, "Outcome removed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
        databaseController.close();
    }
}
