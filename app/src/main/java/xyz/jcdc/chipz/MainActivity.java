package xyz.jcdc.chipz;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.jcdc.chipz.adapter.SelectedContactsAdapter;

public class MainActivity extends AppCompatActivity implements SelectedContactsAdapter.SelectedContactClickedListener {

    private Context context;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private SelectedContactsAdapter selectedContactsAdapter;

    private Integer[] last;

    private List<String> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChooser();
            }
        });


        contacts.add("John");
        contacts.add("Paul");
        contacts.add("Ringo");
        contacts.add("George");


        selectedContactsAdapter = new SelectedContactsAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(selectedContactsAdapter);

    }

    @Override
    public void onContactClicked(final int position, final String who) {
        new MaterialDialog.Builder(this)
                .title(who)
                .items("Edit", "Remove")
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                showEditDialog(position, who);
                                break;

                            case 1:
                                removeContact(position, who);
                                break;
                        }
                    }
                })
                .show();

    }

    private void showEditDialog(final int position, final String who) {
        new MaterialDialog.Builder(this)
                .title("Edit")
                .inputType(InputType.TYPE_CLASS_TEXT )
                .input(null, who, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        selectedContactsAdapter.getContacts().set(position, input.toString());
                        selectedContactsAdapter.notifyDataSetChanged();
                    }
                }).show();
    }

    private void removeContact(int position, final String who) {
        last = null;

        selectedContactsAdapter.getContacts().remove(position);
        selectedContactsAdapter.notifyDataSetChanged();
    }

    private void showChooser() {

        new MaterialDialog.Builder(this)
                .title("Contacts")
                .items(contacts)
                .itemsCallbackMultiChoice(last, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                        return false;
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        last = dialog.getSelectedIndices();

                        selectedContactsAdapter.getContacts().clear();
                        selectedContactsAdapter.notifyDataSetChanged();

                        for (int i : dialog.getSelectedIndices()) {
                            selectedContactsAdapter.getContacts().add(contacts.get(i));
                        }

                        selectedContactsAdapter.notifyDataSetChanged();
                    }
                })
                .positiveText("Done")
                .negativeText("Dismiss")
                .show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
