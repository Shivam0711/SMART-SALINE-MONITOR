package lifesaver.codeutsava.hospitalone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class Add_contacts extends AppCompatActivity implements SearchView.OnQueryTextListener {
    ListView lv_sms;
    ArrayList<String> smsList;
    ArrayList<String> numList;
    ArrayAdapter<String> adapter;
    Cursor c;
    String allnum;
    private static final int READ_CONTACTS=1;
    Button btnAdd,btnView;
    EditText editText;
    DatabaseHelper mDatabaseHelper;
    SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);
        lv_sms=(ListView)findViewById(R.id.l1);
        search=findViewById(R.id.s2);
        mDatabaseHelper = new DatabaseHelper(this);
        int permissioncheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if (permissioncheck == PackageManager.PERMISSION_GRANTED) {
            readContacts();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS);
        }
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,smsList);
        lv_sms.setAdapter(adapter);

        search.setOnQueryTextListener(this);


        lv_sms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                String  newEntry = smsList.get(position);

                if (newEntry.length() != 0) {
                    AddData(newEntry);
                    newEntry = " ";
                } else {
                    Toast.makeText(Add_contacts.this,"not added" ,Toast.LENGTH_SHORT).show();

                }










            }
        });



    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == READ_CONTACTS){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                readContacts();
            }else {
                Toast.makeText(this,"Permission Required",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void readContacts(){
        c=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,ContactsContract.Contacts.DISPLAY_NAME+" ASC ");
        smsList=new ArrayList<String>();

        while (c.moveToNext()){
            String Number=c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String Body=c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            smsList.add(""+Number+"\n"+Body);


        }

        c.close();
    }




    public void AddData(String newEntry) {
        boolean insertData = mDatabaseHelper.addData(newEntry);

        if (insertData) {
            Toast.makeText(this,"ADDED" ,Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this,"Permission Required",Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String txt = newText;
        adapter.getFilter().filter(newText);

        return false;
    }
}