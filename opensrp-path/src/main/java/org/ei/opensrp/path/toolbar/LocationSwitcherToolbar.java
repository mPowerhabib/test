package org.ei.opensrp.path.toolbar;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

import org.ei.opensrp.path.R;
import org.ei.opensrp.path.activity.BaseActivity;
import org.ei.opensrp.path.fragment.LocationPickerDialogFragment;
import org.ei.opensrp.path.view.LocationActionView;

import java.util.ArrayList;

/**
 * To use this toolbar in your activity, include the following line as the first child in your
 * activity's main {@link android.support.design.widget.CoordinatorLayout}
 * <p>
 * <include layout="@layout/toolbar_location_switcher" />
 * <p>
 * Created by Jason Rogena - jrogena@ona.io on 17/02/2017.
 */

public class LocationSwitcherToolbar extends BaseToolbar {
    private static final String TAG = "LocationSwitcherToolbar";
    public static final int TOOLBAR_ID = R.id.location_switching_toolbar;
    private BaseActivity baseActivity;
    private OnLocationChangeListener onLocationChangeListener;
    private LocationPickerDialogFragment locationPickerDialogFragment;
    private static final String LOCATION_DIALOG_TAG = "locationDialogTAG";
    int separatorResource;

    public LocationSwitcherToolbar(Context context) {
        super(context);
    }

    public LocationSwitcherToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LocationSwitcherToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(BaseActivity baseActivity, int separatorResource) {
        this.baseActivity = baseActivity;
        this.separatorResource = separatorResource;
    }

    public ArrayList<String> getCurrentLocation() {
        if (locationPickerDialogFragment != null) {
            return locationPickerDialogFragment.getValue();
        }

        return null;
    }

    private void updateMenu(ArrayList<String> selectedLocation) {
        if (baseActivity != null) {
            String name = baseActivity.getString(R.string.select_location);
            if (selectedLocation != null && selectedLocation.size() > 0) {
                name = selectedLocation.get(selectedLocation.size() - 1);
            }

            ((LocationActionView) baseActivity.getMenu().findItem(R.id.location_switcher)
                    .getActionView()).setItemText(name);
        }
    }

    public void setOnLocationChangeListener(OnLocationChangeListener onLocationChangeListener) {
        this.onLocationChangeListener = onLocationChangeListener;
    }

    @Override
    public int getSupportedMenu() {
        return R.menu.menu_location_switcher;
    }

    @Override
    public void prepareMenu() {
        if (baseActivity != null) {
            locationPickerDialogFragment = new LocationPickerDialogFragment(baseActivity,
                    baseActivity.getOpenSRPContext(),
                    baseActivity.getOpenSRPContext().anmLocationController().get());
            locationPickerDialogFragment.setOnLocationChangeListener(new OnLocationChangeListener() {
                @Override
                public void onLocationChanged(ArrayList<String> newLocation) {
                    updateMenu(newLocation);
                    if (onLocationChangeListener != null) {
                        onLocationChangeListener.onLocationChanged(newLocation);
                    }
                }
            });

            LocationActionView locationActionView = new LocationActionView(baseActivity);
            locationActionView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLocationPickerDialog();
                }
            });
            locationActionView.updateSeparatorView(separatorResource);
            baseActivity.getMenu().findItem(R.id.location_switcher).setActionView(locationActionView);
        }
        updateMenu(getCurrentLocation());
    }

    @Override
    public MenuItem onMenuItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.location_switcher) {
            showLocationPickerDialog();
        }
        return menuItem;
    }

    public void updateSeparatorView(int newView) {
        if (baseActivity.getMenu() != null
                && baseActivity.getMenu().getItem(R.id.location_switcher) != null) {
            ((LocationActionView)baseActivity.getMenu().getItem(R.id.location_switcher)
                    .getActionView()).updateSeparatorView(newView);
        }
    }

    private void showLocationPickerDialog() {
        if (locationPickerDialogFragment != null && baseActivity != null) {
            FragmentTransaction ft = baseActivity.getFragmentManager().beginTransaction();
            Fragment prev = baseActivity.getFragmentManager()
                    .findFragmentByTag(LOCATION_DIALOG_TAG);

            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            locationPickerDialogFragment.show(ft, LOCATION_DIALOG_TAG);
        }
    }

    public static interface OnLocationChangeListener {
        void onLocationChanged(final ArrayList<String> newLocation);
    }
}
