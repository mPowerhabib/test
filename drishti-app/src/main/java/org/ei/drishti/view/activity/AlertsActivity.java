package org.ei.drishti.view.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.PullToRefreshListView;
import org.ei.drishti.R;
import org.ei.drishti.controller.AlertController;
import org.ei.drishti.domain.Alert;
import org.ei.drishti.domain.Displayable;
import org.ei.drishti.domain.FetchStatus;
import org.ei.drishti.view.*;
import org.ei.drishti.view.adapter.ListAdapter;
import org.ei.drishti.view.matcher.MatchAlertByBeneficiaryOrThaayiCard;
import org.ei.drishti.view.matcher.MatchAlertByTime;
import org.ei.drishti.view.matcher.MatchAlertByVisitCode;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;
import static org.ei.drishti.domain.AlertFilterCriterionForTime.*;
import static org.ei.drishti.domain.AlertFilterCriterionForType.*;
import static org.ei.drishti.domain.FetchStatus.fetched;
import static org.ei.drishti.util.DateUtil.formattedDueDate;
import static org.ei.drishti.util.Log.logVerbose;

public class AlertsActivity extends Activity {
    private UpdateActionsTask updateAlerts;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;
    private Context context;
    private AlertController controller;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logVerbose("Initializing ...");
        setContentView(R.layout.main);

        context = Context.getInstance().updateApplicationContext(this.getApplicationContext());

        findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EligibleCoupleActivity.class));
            }
        });

        final ListAdapter<Alert> alertAdapter = new ListAdapter<Alert>(this, R.layout.list_item, new ArrayList<Alert>()) {
            @Override
            protected void populateListItem(View view, Alert item) {
                setTextView(view, R.id.leftTop, item.beneficiaryName());
                setTextView(view, R.id.rightTop, formattedDueDate(item.dueDate()));
                setTextView(view, R.id.leftBottom, item.thaayiCardNo());
                setTextView(view, R.id.rightBottom, item.visitCode());
            }

            private void setTextView(View v, int viewId, String text) {
                ((TextView) v.findViewById(viewId)).setText(text);
            }
        };
        controller = context.alertController(alertAdapter);
        updateAlerts = new UpdateActionsTask(this, context.actionService(), (ProgressBar) findViewById(R.id.progressBar));

        ItemFilter<Alert> filter = new ItemFilter<Alert>(alertAdapter);
        filter.addFilter(new MatchAlertByVisitCode(this, createDialog(R.drawable.ic_tab_type, "Filter By Type", All, BCG, HEP, OPV)));
        EditText searchBox = (EditText) findViewById(R.id.searchEditText);
        searchBox.setHint("Search Reminders");
        filter.addFilter(new MatchAlertByBeneficiaryOrThaayiCard(searchBox));
        filter.addFilter(new MatchAlertByTime(this, createDialog(R.drawable.ic_tab_clock, "Filter By Time", ShowAll, PastDue, Upcoming)));

        final PullToRefreshListView alertsList = (PullToRefreshListView) findViewById(R.id.listView);
        alertsList.setAdapter(alertAdapter);
        alertsList.setTextFilterEnabled(true);

        alertsList.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            public void onRefresh() {
                updateAlerts.updateFromServer(new AfterFetchListener() {
                    public void afterChangeHappened(FetchStatus status) {
                        if (fetched.equals(status)) {
                            controller.refreshAlertsFromDB();
                        }
                        alertsList.onRefreshComplete();
                    }
                });
            }
        });

        alertsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent("android.intent.action.MAIN");
                intent.setComponent(ComponentName.unflattenFromString("org.commcare.android/.activities.CommCareHomeActivity"));
                intent.addCategory("android.intent.category.Launcher");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "CommCare ODK is not installed.", LENGTH_SHORT).show();
                }
            }
        });

        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                context.userService().changeUser();
                Toast.makeText(getApplicationContext(), "Changes saved.", LENGTH_SHORT).show();
            }
        };
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        controller.refreshAlertsFromDB();
        updateAlerts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.updateMenuItem:
                updateAlerts();
                return true;
            case R.id.settingsMenuItem:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private <T extends Displayable> DialogAction<T> createDialog(int icon, String title, T... options) {
        DialogAction<T> filterDialog = new DialogAction<T>(this, icon, title, options);
        ((ActionBar) findViewById(R.id.actionbar)).addAction(filterDialog);
        return filterDialog;
    }

    private void updateAlerts() {
        updateAlerts.updateFromServer(new AfterFetchListener() {
            public void afterChangeHappened(FetchStatus status) {
                if (fetched.equals(status)) {
                    controller.refreshAlertsFromDB();
                }
            }
        });
    }
}