package com.ictis.sheduler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ictis.sheduler.components.SelectorListAdapter;
import com.ictis.sheduler.components.TimelineListAdapter;
import com.ictis.sheduler.model.Choice;
import com.ictis.sheduler.model.IctisSelectorResponse;
import com.ictis.sheduler.model.SheduleData;
import com.ictis.sheduler.model.SheduledDataStorage;
import com.ictis.sheduler.model.task.FormattedItemRow;
import com.ictis.sheduler.model.task.GetTableAsyncTask;
import com.ictis.sheduler.model.task.RequestData;
import com.ictis.sheduler.storage.DataHandler;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static SheduleData CURRENT_DATA = null;

    public static void fillScheduler(SheduleData sheduleData, Activity context) {
        ListView list = (ListView) context.findViewById(R.id.list);
        list.setAdapter(null);
        List<List<String>> listOfContent = sheduleData.getTable().getTable();
        if (listOfContent != null && listOfContent.size() > 0) {
            List<String> timesRorw = listOfContent.get(1); // 2nd row
            List<FormattedItemRow> items = new ArrayList<>();
            for (int i = 2; i < listOfContent.size(); i++) {
                String date = listOfContent.get(i).get(0);
                FormattedItemRow rowDate = new FormattedItemRow();
                rowDate.setDate(date);
                boolean isAdded = false;
                for (int j = 1; j < listOfContent.get(i).size(); j++) {
                    if (listOfContent.get(i).get(j) != null && listOfContent.get(i).get(j).trim().length() > 0) {
                        FormattedItemRow row = new FormattedItemRow();
                        row.setTime(timesRorw.get(j));
                        //row.setDate(date);
                        row.setData(listOfContent.get(i).get(j));
                        if (!isAdded) {
                            items.add(rowDate);
                            isAdded = true;
                        }
                        items.add(row);
                    }
                }
            }

            if (items.size() > 0) {
                FormattedItemRow[] rowsArray = new FormattedItemRow[items.size()];
                TimelineListAdapter adapter = new TimelineListAdapter(context, R.id.list, items.toArray(rowsArray));
                list.setAdapter(adapter);

            }
        }
        TextView listTitle = context.findViewById(R.id.listTitle);
        listTitle.setText(sheduleData.getTable().getName());
        context.findViewById(R.id.favoriteButton).setVisibility(View.VISIBLE);


    }

    public static void fillSelector(final IctisSelectorResponse selectorResponse, Activity context) {

        if (selectorResponse.getChoices() != null) {
            Choice[] choices = new Choice[selectorResponse.getChoices().size()];
            SelectorListAdapter adapter = new SelectorListAdapter(context, R.id.list, selectorResponse.getChoices().toArray(choices));
            ListView list = context.findViewById(R.id.list);
            list.setAdapter(adapter);
            TextView listTitle = context.findViewById(R.id.listTitle);
            listTitle.setText(R.string.please_select);
            context.findViewById(R.id.favoriteButton).setVisibility(View.INVISIBLE);
        }
    }

    public static void fillList(String searchQuery, Activity context) {
        RequestData[] requests = new RequestData[1];
        RequestData requestData = new RequestData();
        requests[0] = requestData;
        try {
            requestData.setContentType("text/html; charset=utf-8");
            requestData.setUrl("http://ictis.sfedu.ru/schedule-api/?" + searchQuery);
            requestData.setRequestMethod("GET");
            new GetTableAsyncTask((data) -> {
                if (data.getScheduledData() != null) {
                    fillScheduler(data.getScheduledData(), context);
                    SheduleData storedData = DataHandler.getServerCodeDTOS(context).getByGroup(data.getScheduledData().getTable().getGroup());
                    favoriteStatus(storedData != null, context);
                    if (storedData != null) {
                        DataHandler.getServerCodeDTOS(context).update(data.getScheduledData());
                        DataHandler.updateData(context);
                    }
                    CURRENT_DATA = data.getScheduledData();
                } else if (data.getIctisSelectorResponse() != null) {
                    fillSelector(data.getIctisSelectorResponse(), context);
                    CURRENT_DATA = null;
                } else {
                    data.setScheduledData(new SheduleData());
                }
                return true;
            }).execute(requests);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setIcon(R.drawable.sfedu_logo);
        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.search_bar, null);
        actionBar.setCustomView(v);
    }

    private static void favoriteStatus(boolean checked, Activity context) {
        if (checked) {
            ((ImageButton) context.findViewById(R.id.favoriteButton)).setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            ((ImageButton) context.findViewById(R.id.favoriteButton)).setImageResource(android.R.drawable.btn_star_big_off);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        processActionBar();
        setContentView(R.layout.activity_main);
        findViewById(R.id.favoriteButton).setVisibility(View.INVISIBLE);
        final InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        EditText textInput = findViewById(R.id.searchData);
        final ImageButton button = findViewById(R.id.searchButton);
        button.setOnClickListener(view -> {
            if (textInput.getText() != null && !textInput.getText().toString().trim().isEmpty()) {
                fillList("query=" + ((EditText) findViewById(R.id.searchData)).getText().toString(), this);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        findViewById(R.id.ictisLink).setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://ictis.sfedu.ru"));
            startActivity(browserIntent);
        });


        findViewById(R.id.sfeduLink).setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sfedu.ru"));
            startActivity(browserIntent);
        });

        findViewById(R.id.favoriteButton).setOnClickListener(view -> {
            if (CURRENT_DATA == null) {
                return;
            }
            SheduleData getByGroup = DataHandler.getServerCodeDTOS(this).getByGroup(CURRENT_DATA.getTable().getGroup());
            if (getByGroup == null) {
                DataHandler.getServerCodeDTOS(this).getStorage().add(CURRENT_DATA);
                favoriteStatus(true, this);
            } else {
                DataHandler.getServerCodeDTOS(this).getStorage().removeIf(it -> it.getTable().getGroup().equals(CURRENT_DATA.getTable().getGroup()));
                favoriteStatus(false, this);
            }
            DataHandler.updateData(this);
        });

        textInput.setOnEditorActionListener((textView, actionId, keyEvent) -> {
                    boolean handled = false;
                    if (actionId == EditorInfo.IME_ACTION_DONE
                            || actionId == EditorInfo.IME_ACTION_NEXT
                            || actionId == EditorInfo.IME_ACTION_SEARCH
                            || actionId == EditorInfo.IME_ACTION_SEND) {
                        handled = button.callOnClick();
                    }
                    return handled;
                }
        );
        //prefil from storage if exists
        if (CURRENT_DATA == null && DataHandler.getServerCodeDTOS(this).getStorage() != null && DataHandler.getServerCodeDTOS(this).getStorage().size() > 0) {
            SheduleData firstValue = DataHandler.getServerCodeDTOS(this).getStorage().get(0);
            if (firstValue != null) {
                CURRENT_DATA = firstValue;
                favoriteStatus(true, this);
                fillScheduler(CURRENT_DATA, this);
                // attempt to get new data
                fillList("group=" + CURRENT_DATA.getTable().getGroup(), this);
            }
            DataHandler.updateData(this);
        }
        /*
        List<View> buttons = new ArrayList<>();
        buttons.add(new Button())
        findViewById(R.id.weeks).addChildrenForAccessibility();*/
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorite_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final SheduledDataStorage dataStorage = DataHandler.getServerCodeDTOS(this);
        menu.clear();
        if (dataStorage != null && dataStorage.getStorage() != null && !dataStorage.getStorage().isEmpty()) {
            for (final SheduleData storage : dataStorage.getStorage()) {
                final MenuItem newMenuItem = menu.add(String.format("%s", storage.getTable().getName()));
                newMenuItem.setOnMenuItemClickListener(item -> {
                    CURRENT_DATA = storage;
                    fillScheduler(CURRENT_DATA, this);
                    ((ImageButton) findViewById(R.id.favoriteButton)).setImageResource(android.R.drawable.btn_star_big_on);
                    return true;
                });
            }
        } else {
            final MenuItem newMenuItem = menu.add("Пусто");
            newMenuItem.setEnabled(false);

        }
        return super.onPrepareOptionsMenu(menu);
    }

}
