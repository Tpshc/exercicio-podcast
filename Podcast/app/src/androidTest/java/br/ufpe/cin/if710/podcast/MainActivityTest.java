package br.ufpe.cin.if710.podcast;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.runner.RunWith;

import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import junit.framework.Assert;
import org.junit.Rule;
import org.junit.Test;

import br.ufpe.cin.if710.podcast.ui.MainActivity;

import static android.support.test.espresso.Espresso.onData;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static android.support.test.espresso.action.ViewActions.click;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public final ActivityTestRule<MainActivity> main = new ActivityTestRule(MainActivity.class, true);

    @Test
    public void eventosDeTecla() {
        onView(withId(R.id.items)).perform(
                pressKey(KeyEvent.KEYCODE_DPAD_DOWN),
                pressKey(KeyEvent.KEYCODE_DPAD_DOWN),
                pressKey(KeyEvent.KEYCODE_DPAD_DOWN),
                pressKey(KeyEvent.KEYCODE_DPAD_DOWN)
        )
                .check(new ListSelectionAssertion(3));
    }

    @Test
    public void checarPrimeiroElemento(){
        onData(anything())
                .inAdapterView(withId(R.id.items))
                .atPosition(0)
                .onChildView(withId(R.id.item_title))
                .check(matches(withText("Ciência e Pseudociência")));
    }

    @Test
    public void testClickPodcastInfo(){
        //assumindo que a informação sejá estática como dito
        onData(anything())
                .inAdapterView(withId(R.id.items))
                .atPosition(0)
                .perform(click());

        onView(withId(R.id.item_title)).check(matches(withText("Ciência e Pseudociência")));
        onView(withId(R.id.item_description)).check(matches(withText("Programa 1")));
        onView(withId(R.id.item_pubdate)).check(matches(withText("Sun, 20 Jun 2010 10:40:05 GMT")));
        onView(withId(R.id.item_link)).check(matches(withText("http://frontdaciencia.ufrgs.br/#1")));
        onView(withId(R.id.item_downloadlink)).check(matches(withText("http://dstats.net/download/http://www6.ufrgs.br/frontdaciencia/arquivos/Fronteiras_da_Ciencia-E001-Ciencia-e-Pseudociencia-07.06.2010.mp3")));
    }

    static class ListSelectionAssertion implements ViewAssertion {
        private final int position;

        ListSelectionAssertion(int position) {
            this.position=position;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            Assert.assertTrue(view instanceof ListView);
            Assert.assertEquals(position, ((ListView)view).getSelectedItemPosition());
        }
    }

}
