package com.application.itunes;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.text.TextUtils;

import com.application.itunes.util.DateUtils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

import static android.support.test.espresso.action.ViewActions.click;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest extends BaseEspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void checkIfBasicViewsExists() {
        onView(R.id.album_recycler_view).check(isDisplayed());
        onView(R.id.album_content_parent).check(isDisplayed());
        onView(R.id.album_loading_parent).check(isNotDisplayed());
        onView(R.id.album_error_parent).check(isNotDisplayed());
    }

    @Test
    public void testRecyclerView() {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        RecyclerViewMatcher recycler = withRecyclerView(R.id.album_recycler_view);
        List<Album> albums = activityRule.getActivity().getDataSet();

        for (int i = 0; i < albums.size(); ++i) {
            Date time = DateUtils.parseDate(albums.get(i).getReleaseDate());
            String diffDate = DateUtils.getDateDifference(time.getTime());

            onView(R.id.album_recycler_view).perform(RecyclerViewActions.<AlbumAdapter.AlbumAdapterViewHolder>scrollToPosition(i));
            Espresso.onView(recycler.atPositionOnView(i, R.id.album_cover)).check(isDisplayed());
            Espresso.onView(recycler.atPositionOnView(i, R.id.album)).check(withText(albums.get(i).getName()));
            Espresso.onView(recycler.atPositionOnView(i, R.id.artist)).check(withText(albums.get(i).getArtistName()));
            Espresso.onView(recycler.atPositionOnView(i, R.id.release_date)).check(withText(diffDate));

            Espresso.onView(recycler.atPositionOnView(i, R.id.album_cover)).perform(click());

            onView(R.id.album_info_cover).check(isDisplayed());
            onView(R.id.divider_1).check(isDisplayed());
            onView(R.id.album_label).check(isDisplayed()).check(withText(R.string.album_label));
            onView(R.id.album_value).check(isDisplayed()).check(withText(albums.get(i).getName()));
            onView(R.id.divider_2).check(isDisplayed());
            onView(R.id.artist_label).check(isDisplayed()).check(withText(R.string.artist_label));
            onView(R.id.artist_value).check(isDisplayed()).check(withText(albums.get(i).getArtistName()));
            onView(R.id.divider_3).check(isDisplayed());
            onView(R.id.genre_label).check(isDisplayed()).check(withText(R.string.genre_label));
            onView(R.id.genre_value).check(isDisplayed()).check(withText(TextUtils.join(", ", albums.get(i).getGenresNames())));
            onView(R.id.divider_4).check(isDisplayed());
            onView(R.id.date_label).check(isDisplayed()).check(withText(R.string.date_label));
            onView(R.id.date_value).check(isDisplayed()).check(withText(diffDate));
            onView(R.id.divider_5).check(isDisplayed());
            onView(R.id.open_album).check(isDisplayed()).check(withText(R.string.view_in_itunes));
            onView(R.id.copyright).check(isDisplayed()).check(withText(albums.get(i).getCopyright()));
            Espresso.pressBack();
        }
    }
}