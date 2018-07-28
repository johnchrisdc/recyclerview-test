package xyz.jcdc.chipz.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onegravity.contactpicker.contact.Contact;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.jcdc.chipz.R;

/**
 * Created by jcdc on 7/28/18.
 */

public class SelectedContactsAdapter extends RecyclerView.Adapter<SelectedContactsAdapter.ViewHolder>{

    private List<Contact> contacts = new ArrayList<>();

    private SelectedContactClickedListener selectedContactClickedListener;

    public SelectedContactsAdapter(SelectedContactClickedListener selectedContactClickedListener) {
        this.selectedContactClickedListener = selectedContactClickedListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.button)
        AppCompatButton button;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedContactClickedListener.onContactClicked(getAdapterPosition(), getContacts().get(getAdapterPosition()));
                }
            });

        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_contact, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = getContacts().get(position);

        holder.button.setText(contact.getDisplayName());
    }

    @Override
    public int getItemCount() {
        return getContacts().size();
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public interface SelectedContactClickedListener {
        void onContactClicked(int position, Contact contact);
    }

}
