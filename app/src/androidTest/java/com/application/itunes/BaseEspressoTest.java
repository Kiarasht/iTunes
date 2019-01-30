package com.application.itunes;

import android.content.res.Resources;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsNot.not;

class BaseEspressoTest {
    public ViewAssertion isDisplayed() {
        return matches(ViewMatchers.isDisplayed());
    }

    public ViewAssertion isNotDisplayed() {
        return matches(not(ViewMatchers.isDisplayed()));
    }

    public ViewAssertion isChecked() {
        return matches(ViewMatchers.isChecked());
    }

    public ViewAssertion isNotChecked() {
        return matches(not(ViewMatchers.isChecked()));
    }

    public ViewAssertion withText(@StringRes int id) {
        return matches(ViewMatchers.withText(id));
    }

    public ViewAssertion withText(String text) {
        return matches(ViewMatchers.withText(text));
    }

    public ViewAssertion withSpinnerText(String text) {
        return matches(ViewMatchers.withSpinnerText(containsString(text)));
    }

    public ViewAssertion withContentDescription(String text) {
        return matches(ViewMatchers.withContentDescription(text));
    }

    public ViewAssertion isEnabled() {
        return matches(ViewMatchers.isEnabled());
    }

    public ViewAssertion isNotEnabled() {
        return matches(not(ViewMatchers.isEnabled()));
    }

    public ViewInteraction onView(@IdRes int id) {
        return Espresso.onView(ViewMatchers.withId(id));
    }

    public ViewAssertion isVisible() {
        return matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE));
    }

    public ViewAssertion isGone() {
        return matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE));
    }

    public ViewAssertion isInvisible() {
        return matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE));
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    /**
     * Fork of the RecyclerViewMatcher from https://github.com/dannyroa/espresso-samples
     * More at: https://spin.atomicobject.com/2016/04/15/espresso-testing-recyclerviews/
     */
    public static class RecyclerViewMatcher {

        private final int recyclerViewId;

        public RecyclerViewMatcher(int recyclerViewId) {
            this.recyclerViewId = recyclerViewId;
        }

        public Matcher<View> atPosition(final int position) {
            return atPositionOnView(position, -1);
        }

        public Matcher<View> atPositionOnView(final int position, final int targetViewId) {

            return new TypeSafeMatcher<View>() {
                Resources resources = null;
                View childView;

                public void describeTo(Description description) {
                    String idDescription = Integer.toString(recyclerViewId);
                    if (this.resources != null) {
                        try {
                            idDescription = this.resources.getResourceName(recyclerViewId);
                        } catch (Resources.NotFoundException var4) {
                            idDescription = String.format("%s (resource name not found)", recyclerViewId);
                        }
                    }

                    description.appendText("RecyclerView with id: " + idDescription + " at position: " + position);
                }

                public boolean matchesSafely(View view) {

                    this.resources = view.getResources();

                    if (childView == null) {
                        RecyclerView recyclerView = view.getRootView().findViewById(recyclerViewId);
                        if (recyclerView != null && recyclerView.getId() == recyclerViewId) {
                            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
                            if (viewHolder != null) {
                                childView = viewHolder.itemView;
                            }
                        } else {
                            return false;
                        }
                    }

                    if (targetViewId == -1) {
                        return view == childView;
                    } else {
                        View targetView = childView.findViewById(targetViewId);
                        return view == targetView;
                    }
                }
            };
        }
    }
}
