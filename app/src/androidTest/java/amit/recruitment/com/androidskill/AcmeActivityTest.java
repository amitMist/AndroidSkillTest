package amit.recruitment.com.androidskill;

/**
 * Created by amit on 3/13/18.
 */

import android.support.design.widget.FloatingActionButton;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class AcmeActivityTest extends ActivityInstrumentationTestCase2<AcmeActivity> {

    private AcmeActivity mTestActivity;
    private TextView mTestFortuneTextview;
    private FloatingActionButton mFab;

    public AcmeActivityTest() {
        super(AcmeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Starts the activity under test using
        // the default Intent with:
        // action = {@link Intent#ACTION_MAIN}
        // flags = {@link Intent#FLAG_ACTIVITY_NEW_TASK}
        // All other fields are null or empty.
        mTestActivity = getActivity();
        mTestFortuneTextview = (TextView) mTestActivity
                .findViewById(R.id.tv_acme_fortune);
        mFab = (FloatingActionButton) mTestActivity
                .findViewById(R.id.fab);
    }

    /**
     * If this tests fails all other tests are
     * likely to fail as well.
     */
    public void testPreconditions() {
        // Try to add a message to add context to your assertions.
        // These messages will be shown if
        // a tests fails and make it easy to
        // understand why a test failed
        assertNotNull("mTestActivity is null", mTestActivity);
        assertNotNull("mTestFortuneTextview is null", mTestFortuneTextview);
        assertNotNull("mFab is null", mFab);
    }


    public void  testDataFetched(){

        getActivity();
        Espresso.onView(ViewMatchers.withId(R.id.fab))
                .perform(ViewActions.click());

        assertNotNull("mTestFortuneTextview content is null after fetching",mTestFortuneTextview.getText());

    }


    public void  testResponseDisplayed(){

        getActivity();
        Espresso.onView(ViewMatchers.withId(R.id.fab))
                .perform(ViewActions.click());


        assertFalse("Does not get data from api", mTestFortuneTextview.getText().toString().contains("Response:"));

    }


}