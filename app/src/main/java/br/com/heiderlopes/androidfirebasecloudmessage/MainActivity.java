package br.com.heiderlopes.androidfirebasecloudmessage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

import br.com.heiderlopes.androidfirebasecloudmessage.model.Message;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private DatabaseReference mFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciarConexaoComFirebase();

        View buttonSend = findViewById(R.id.buttonSend);
        final EditText editText = (EditText) findViewById(R.id.myEditText);

        buttonSend.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String message = editText.getText().toString();
                sendMessage(message);
                editText.getText().clear();
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void iniciarConexaoComFirebase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mFirebaseRef = database.getReference("chatroom");
    }


    private void sendMessage(String message){

        String key = mFirebaseRef.child("room").push().getKey();
        Message message1 = new Message(message, "Ramon Silva");

        Map<String, Object> postValues = message1.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/messages/" + key, postValues);

        mFirebaseRef.updateChildren(childUpdates);

    }
}