package se.orw.inlamning1;

import android.os.Bundle;

/**
 * Created by Marcus Orwén on 2014-09-18.
 *
 * The main activity of the application
 */
public class MainActivity extends android.app.Activity {
    private Controller controller;

    /**
     * Called once the activity is being created
     *
     * @param savedInstanceState -
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controller = new Controller(this);
    }

    /**
     * Called once the application is paused, (not fully closed).
     */
    @Override
    protected void onPause() {
        super.onPause();
        controller.onPause();
    }

    /**
     * Called once the application resumes from paused state.
     */
    @Override
    protected void onResume() {
        super.onResume();
        controller.onResume();
    }
}
