package com.gmail.bisrihanafi.savedata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    EditText nama, telepon;
    Button tombolInput;
    ListView listView;
    ArrayList<String> listnama=new ArrayList<String>();
    ArrayAdapter<String> listadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //penghubung antara objek dalam java dengan layout
        nama = (EditText) findViewById(R.id.editNama);
        telepon = (EditText) findViewById(R.id.editTelepon);
        //dataTelepon = (TextView) findViewById(R.id.textDataTelp);
        tombolInput = (Button) findViewById(R.id.buttonInput);
        listView=(ListView)findViewById(R.id.listView);
        listadapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listnama);
        listView.setOnItemClickListener(this);
        //event pada tombol
        tombolInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //menyiapkan buffer
                byte[] bufferNama = new byte[30];
                byte[] bufferTelepon = new byte[15];
                //menyalin data ke buffer
                salinData(bufferNama, nama.getText().toString());
                salinData(bufferTelepon, telepon.getText().toString());
                //proses menyimpan fileke internal memori
                try{
                    //menyiapkan file di memori internal
                    FileOutputStream dataFile = openFileOutput("telepon.dat", MODE_APPEND);
                    DataOutputStream output = new DataOutputStream(dataFile);
                    //menyimpan data
                    output.write(bufferNama);

                    output.write(bufferTelepon);
                    //menutup file
                    dataFile.close();
                    //menampilkan pesan jika data tersimpan
                    Toast.makeText(getBaseContext(), "Data telah disimpan",
                            Toast.LENGTH_LONG).show();
                }
                catch (IOException e){
                    Toast.makeText(getBaseContext(), "Kesalahan: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                tampilkanData();
            }
        });
        tampilkanData();
    }
    public void salinData(byte[] buffer, String data) {
        //mengosongkan buffer
        for (int i = 0; i < buffer.length; i++)
            buffer[i] = 0;
        //menyalin data ke buffer
        for (int i = 0; i < data.length(); i++)
            buffer[i] = (byte) data.charAt(i);
    }
    public void tampilkanData() {
        try {
            listnama.clear();
            // menyiapkan file untuk dibaca
            FileInputStream dataFile = openFileInput("telepon.dat");
            DataInputStream input = new DataInputStream(dataFile);
            //menyiapkan buffer
            byte[] bufNama = new byte[30];
            byte[] bufTelepon = new byte[15];
            String infoData = "";
            //proses membaca data
            int countadd=0;
            while (input.available() > 0) {
                countadd++;
                input.read(bufNama);
                input.read(bufTelepon);
                String dataNama = "";
                for (int i = 0; i < bufNama.length; i++)
                    dataNama = dataNama + (char) bufNama[i];
                String dataTelepon = "";
                for (int i = 0; i < bufTelepon.length; i++)
                    dataTelepon = dataTelepon + (char) bufTelepon[i];
                    //format menampilkan data
                infoData =countadd+". " + dataNama + "-" + dataTelepon + "\n";
                listnama.add(infoData);

            }
            //menampilkan data ke teks view
            //dataTelepon.setText(infoData);
            listView.setAdapter(listadapter);
            dataFile.close();
        }
        catch (IOException e) {
            Toast.makeText(getBaseContext(), "Kesalahan: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, listadapter.getItem(position), Toast.LENGTH_SHORT).show();
    }
}
