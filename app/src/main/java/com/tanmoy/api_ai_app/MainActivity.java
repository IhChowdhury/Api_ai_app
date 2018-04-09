package com.tanmoy.api_ai_app;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.os.Bundle;

import ai.api.AIListener;
import ai.api.RequestExtras;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

import com.google.gson.JsonElement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;



public class MainActivity extends AppCompatActivity implements AIListener {
    private AIService aiService;
    private String newmsg = "";

    private EditText editmessagetext;
    private Button sendButton;
    private RecyclerView messageDisplay;
    private MessageAdapter messageAdapter;
    private List<NewMessage> messageList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        config("fcbb3891ff1d42b592092355d5082d7d");

        messageDisplay = findViewById(R.id.message_display);
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(getApplicationContext(),messageList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),1);
        messageDisplay.setLayoutManager(layoutManager);
        messageDisplay.setItemAnimator(new DefaultItemAnimator());
        messageDisplay.setAdapter(messageAdapter);

        String welcome = "Welcome to ECE Information Center. Please send us any questions you may have regarding ECE Department. For any guidline type \"Help\"";
        NewMessage welcomemsg = new NewMessage("E-Care",welcome,getTime(),R.drawable.nsulogo);
        messageList.add(welcomemsg);
        messageAdapter.notifyDataSetChanged();



        editmessagetext = findViewById(R.id.edit_message_text);
        sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editmessagetext.getText().toString().trim().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Enter Your Question...",Toast.LENGTH_SHORT).show();
                    return;
                }
                String msg = editmessagetext.getText().toString();
                NewMessage newMessage = new NewMessage("You",msg,getTime(),R.drawable.profile_pic);
                messageList.add(newMessage);
                messageAdapter.notifyDataSetChanged();
                messageDisplay.smoothScrollToPosition(messageList.size()-1);
                sendButton.setClickable(false);

                editmessagetext.setText("");

                new MainActivity.DoTextRequestTask().execute(msg);

            }
        });

    }

    public void config(String token) {
        final AIConfiguration config = new AIConfiguration(token,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiService = AIService.getService(MainActivity.this, config);
        aiService.setListener(MainActivity.this);

        Toast toast = Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void onResult(final AIResponse response) {
        Result result = response.getResult();

        String parameterString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
            }
        }

        // Show results in TextView.
//        resultTextView.setText("Query:" + result.getResolvedQuery() +
//                "\nAction: " + result.getAction() +
//                "\nParameters: " + parameterString);

        Log.e("param","Query:" + result.getResolvedQuery() +
                "\nAction: " + result.getAction() +
                "\nParameters: " + parameterString);
        Toast.makeText(getApplicationContext(),"Query:" + result.getResolvedQuery() +
                "\nAction: " + result.getAction() +
                "\nParameters: " + parameterString,Toast.LENGTH_LONG).show();
    }

    private String getTime(){
        Calendar calander = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");

        String time = simpleDateFormat.format(calander.getTime());
        return time;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /*if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enablebutton();
            } else {
                permission();
            }
        }*/
    }

    @Override
    public void onError(final AIError error) {
        //resultTextView.setText(error.toString());
    }

    @Override
    public void onListeningStarted() {
    }

    @Override
    public void onListeningCanceled() {
    }

    @Override
    public void onListeningFinished() {
    }

    @Override
    public void onAudioLevel(final float level) {
    }


    class DoTextRequestTask extends AsyncTask<String, Void, AIResponse> {
        private Exception exception = null;

        protected AIResponse doInBackground(String... text) {
            AIResponse resp = null;
            try {
                resp = aiService.textRequest(text[0], new RequestExtras());


            } catch (Exception e) {
                this.exception = e;
            }
            return resp;
        }

        protected void onPostExecute(AIResponse response) {
            if (this.exception == null) {

            }
            Result result = response.getResult();

            //result.getFulfillment().getSpeech()


            newmsg = result.getFulfillment().getSpeech();
            NewMessage newMessage = new NewMessage("E-Care",newmsg,getTime(),R.drawable.nsulogo);
            messageList.add(newMessage);
            messageAdapter.notifyDataSetChanged();
            messageDisplay.smoothScrollToPosition(messageList.size()-1);
            sendButton.setClickable(true);
            // Show results in TextView.

        }
    }
}