package br.com.heiderlopes.androidfirebasecloudmessage;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.Locale;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import br.com.heiderlopes.androidfirebasecloudmessage.api.HomeAutomationAPI;
import br.com.heiderlopes.androidfirebasecloudmessage.model.Message;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ComandosActivity extends AppCompatActivity {

    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private Button btVoz;

    private final int REQ_CODE_SPEECH_INPUT = 100;

    private TextToSpeech t1;


    final RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("http://172.16.51.172:3000")
            .build();

    HomeAutomationAPI api = restAdapter.create(HomeAutomationAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comandos);

        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        btVoz = (Button)findViewById(R.id.btVoz);

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.getDefault());
                }
            }
        });

        t1.setSpeechRate(0.7f);

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        btVoz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String toSpeak = ed1.getText().toString();
                String toSpeak = "Sua casa esta sendo invadida";
                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

    }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Diga alguma coisa");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Speech nao suportado",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));

                    if(result.get(0).toUpperCase().contains("DESLIGAR LED")) {
                        desligarLed();
                    } else if(result.get(0).toUpperCase().contains("LIGAR LED")) {
                        ligarLed();
                    } else if(result.get(0).toUpperCase().contains("QUAL A TEMPERATURA")) {
                        getTemperatura();
                    }  else if(result.get(0).toUpperCase().contains("DESATIVAR ALARME")) {
                        desativarAlarme();
                    }  else if(result.get(0).toUpperCase().contains("DISPARAR ALARME")) {
                        dispararAlarme();
                    }
                }
                break;
            }

        }
    }

    private void dispararAlarme() {
        api.dispararAlarme(new Callback<Message>() {
            @Override
            public void success(Message message, Response response) {
                t1.speak(message.text, TextToSpeech.QUEUE_FLUSH, null);
            }

            @Override
            public void failure(RetrofitError error) {
                t1.speak("Nao foi possivel executar o comando", TextToSpeech.QUEUE_FLUSH, null);

            }
        });
    }

    private void desativarAlarme() {
        api.desativarAlarme(new Callback<Message>() {
            @Override
            public void success(Message message, Response response) {
                t1.speak(message.text, TextToSpeech.QUEUE_FLUSH, null);
            }

            @Override
            public void failure(RetrofitError error) {
                t1.speak("Nao foi possivel executar o comando", TextToSpeech.QUEUE_FLUSH, null);

            }
        });
    }

    private void ligarLed() {
        api.ligarLed(new Callback<Message>() {
            @Override
            public void success(Message s, Response response) {
                //Toast.makeText(getApplicationContext(), s,Toast.LENGTH_SHORT).show();
                t1.speak(s.text, TextToSpeech.QUEUE_FLUSH, null);
            }

            @Override
            public void failure(RetrofitError error) {
                t1.speak("Nao foi possivel executar o comando", TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    private void desligarLed() {

        api.desligarLed(new Callback<Message>() {
            @Override
            public void success(Message s, Response response) {
                t1.speak(s.text, TextToSpeech.QUEUE_FLUSH, null);
            }

            @Override
            public void failure(RetrofitError error) {
                t1.speak("Nao foi possivel executar o comando", TextToSpeech.QUEUE_FLUSH, null);
            }
        });

    }

    private void getTemperatura() {
        api.getTemperatura(new Callback<Message>() {
            @Override
            public void success(Message s, Response response) {
                t1.speak("A temperatura da sala é de " + s.text + " graus celsius", TextToSpeech.QUEUE_FLUSH, null);

            }

            @Override
            public void failure(RetrofitError error) {
                t1.speak("Nao foi possivel executar o comando", TextToSpeech.QUEUE_FLUSH, null);
            }
        });

    }
}
