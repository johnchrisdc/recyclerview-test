package xyz.jcdc.chipz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.onegravity.contactpicker.contact.Contact;
import com.onegravity.contactpicker.contact.ContactDescription;
import com.onegravity.contactpicker.contact.ContactSortOrder;
import com.onegravity.contactpicker.core.ContactPickerActivity;
import com.onegravity.contactpicker.group.Group;
import com.onegravity.contactpicker.picture.ContactPictureType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.jcdc.chipz.adapter.SelectedContactsAdapter;
import xyz.jcdc.chipz.model.Radio;
import xyz.jcdc.chipz.model.RadioGroup;

public class MainActivity extends AppCompatActivity implements SelectedContactsAdapter.SelectedContactClickedListener {

    public static final int REQUEST_CONTACT = 666;

    private Context context;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.container_radios)
    LinearLayout container_radios;

    private SelectedContactsAdapter selectedContactsAdapter;

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

        selectedContactsAdapter = new SelectedContactsAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(selectedContactsAdapter);


        setUpRadio();
    }

    @Override
    public void onContactClicked(final int position, final Contact contact) {
        new MaterialDialog.Builder(this)
                .title(contact.getDisplayName())
                .items("Edit", "Remove")
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                showEditDialog(position, contact);
                                break;

                            case 1:
                                removeContact(position);
                                break;
                        }
                    }
                })
                .show();

    }

    private void showEditDialog(final int position, final Contact contact) {
        new MaterialDialog.Builder(this)
                .title("Edit")
                .inputType(InputType.TYPE_CLASS_TEXT )
                .input(null, contact.getDisplayName(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        selectedContactsAdapter.getContacts().set(position, contact);
                        selectedContactsAdapter.notifyDataSetChanged();
                    }
                }).show();
    }

    private void removeContact(int position) {
        selectedContactsAdapter.getContacts().remove(position);
        selectedContactsAdapter.notifyDataSetChanged();
    }

    private void showChooser() {
        Intent intent = new Intent(this, ContactPickerActivity.class)
                .putExtra(ContactPickerActivity.EXTRA_CONTACT_BADGE_TYPE, ContactPictureType.ROUND.name())
                .putExtra(ContactPickerActivity.EXTRA_SHOW_CHECK_ALL, true)
                .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION, ContactDescription.ADDRESS.name())
                .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .putExtra(ContactPickerActivity.EXTRA_CONTACT_SORT_ORDER, ContactSortOrder.AUTOMATIC.name());
        startActivityForResult(intent, REQUEST_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CONTACT && resultCode == Activity.RESULT_OK &&
                data != null && data.hasExtra(ContactPickerActivity.RESULT_CONTACT_DATA)) {

            // we got a result from the contact picker

            // process contacts
            List<Contact> contacts = (List<Contact>) data.getSerializableExtra(ContactPickerActivity.RESULT_CONTACT_DATA);
            selectedContactsAdapter.setContacts(contacts);
            selectedContactsAdapter.notifyDataSetChanged();
        }
    }

    private void setUpRadio() {
        for (int x=0; x<50; x++) {
            RadioGroup group = new RadioGroup();

            Radio radio = new Radio("Sample 1", 1);
            Radio radio2 = new Radio("Sample 2", 2);
            Radio radio3 = new Radio("Sample 3", 3);

            group.getRadios().add(radio);
            group.getRadios().add(radio2);
            group.getRadios().add(radio3);

            android.widget.RadioGroup radioGroup = new android.widget.RadioGroup(context);
            radioGroup.setOrientation(LinearLayout.HORIZONTAL);

            for (Radio rad : group.getRadios()) {
                AppCompatRadioButton appCompatRadioButton = new AppCompatRadioButton(context);
                appCompatRadioButton.setText(rad.getName());
                appCompatRadioButton.setId(rad.getId());

                radioGroup.addView(appCompatRadioButton);
            }

            container_radios.addView(radioGroup);
        }


    }
}
