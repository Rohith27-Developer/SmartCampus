package com.example.smartservices;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class ChatBot extends AppCompatActivity implements TextToSpeech.OnInitListener {
    String[] questions = {"Canteen", "Events", "Cleanliness", "Lost and Found", "Others"};
    ArrayList<String> al = new ArrayList<>();
    ArrayAdapter<String> adapter;
    MessageAdapter messageAdapter;
    ListView lv, messageView;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    Dialog lovelyCustomDialog;
    GoogleSignInAccount acct;
    TextToSpeech TTS;
    private EditText issue, reg_no, mobile_no;
    GoogleSignInClient mGoogleSignInClient;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getSupportActionBar().hide();
        if (InitApplication.getInstance().isNightModeEnabled()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor("#000000"));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.WHITE);
            }
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
            setContentView(R.layout.activity_chat_bot);

        TTS = new TextToSpeech(this, this);
            lv = findViewById(R.id.lv);
            messageView = findViewById(R.id.messages_view);
            messageAdapter = new MessageAdapter(this);
            messageView = findViewById(R.id.messages_view);
            messageView.setAdapter(messageAdapter);
            tv = findViewById(R.id.ended);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        acct = GoogleSignIn.getLastSignedInAccount(this);
        final Message m = new Message("Hello "+acct.getDisplayName()+", I am Support Assistant", false);
     //   TTS.speak("Hello "+acct.getDisplayName()+", I am Support Assistant", TextToSpeech.QUEUE_FLUSH, null, null);
        TTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = TTS.setLanguage(Locale.UK);
                    TTS.setPitch((float) 1
                    );
                    TTS.setSpeechRate((float) 1);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language Not Supported");
                    }
                    else {
                        speak("Hello "+acct.getDisplayName()+", I am Support Assistant");
                    }
                } else {
                    Log.e("TTS", "Error initializing TTS Engine");

                }
            }
        });
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                messageAdapter.add(m);
                                messageView.setSelection(messageView.getCount() - 1);
                                tv.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            ;
        };
        thread.start();

            for (int i = 0; i < questions.length; i++) {
                al.add(questions[i]);
            }
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, al) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView tv = (TextView) view.findViewById(android.R.id.text1);
                    tv.setTextColor(getResources().getColor(R.color.textColor));
                    tv.setGravity(Gravity.CENTER);
                    return view;
                }
            };
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if (lv.getItemAtPosition(position).toString().equalsIgnoreCase("canteen")) {
                        al.removeAll(al);
                        al.add("My order instructions were not followed");
                        al.add("I have food taste, quality or quantity issue with my order");
                        al.add("Go back");
                        al.add("Others");
                        adapter = new ArrayAdapter<String>(ChatBot.this,
                                android.R.layout.simple_list_item_1, al) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view = super.getView(position, convertView, parent);
                                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                                tv.setTextColor(getResources().getColor(R.color.textColor));
                                tv.setGravity(Gravity.CENTER);
                                return view;
                            }
                        };
                        lv.setAdapter(adapter);
                    } else if (lv.getItemAtPosition(position).toString().equalsIgnoreCase("cleanliness")) {
                        al.removeAll(al);
                        al.add("My request is not done yet");
                        al.add("Image is not getting uploaded");
                        al.add("Go back");
                        al.add("Others");
                        adapter = new ArrayAdapter<String>(ChatBot.this,
                                android.R.layout.simple_list_item_1, al) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view = super.getView(position, convertView, parent);
                                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                                tv.setTextColor(getResources().getColor(R.color.textColor));
                                tv.setGravity(Gravity.CENTER);
                                return view;
                            }
                        };
                        lv.setAdapter(adapter);
                    } else if (lv.getItemAtPosition(position).toString().equalsIgnoreCase("Events")) {
                        al.removeAll(al);
                        al.add("The link is not working");
                        al.add("The matter is irrelevant");
                        al.add("The date for the event is wrong");
                        al.add("Go back");
                        al.add("Others");
                        adapter = new ArrayAdapter<String>(ChatBot.this,
                                android.R.layout.simple_list_item_1, al) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view = super.getView(position, convertView, parent);
                                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                                tv.setTextColor(getResources().getColor(R.color.textColor));
                                tv.setGravity(Gravity.CENTER);
                                return view;
                            }
                        };
                        lv.setAdapter(adapter);
                    } else if (lv.getItemAtPosition(position).toString().equalsIgnoreCase("lost and found")) {
                        al.removeAll(al);
                        al.add("Image is not getting uploaded");
                        al.add("Go back");
                        al.add("Others");
                        adapter = new ArrayAdapter<String>(ChatBot.this,
                                android.R.layout.simple_list_item_1, al) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view = super.getView(position, convertView, parent);
                                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                                tv.setTextColor(getResources().getColor(R.color.textColor));
                                tv.setGravity(Gravity.CENTER);
                                return view;
                            }
                        };
                        lv.setAdapter(adapter);
                    } else if (lv.getItemAtPosition(position).toString().equalsIgnoreCase("Go back")) {
                        al.removeAll(al);
                        for (int i = 0; i < questions.length; i++) {
                            al.add(questions[i]);
                        }
                        adapter = new ArrayAdapter<String>(ChatBot.this,
                                android.R.layout.simple_list_item_1, al) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view = super.getView(position, convertView, parent);
                                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                                tv.setTextColor(getResources().getColor(R.color.textColor));
                                tv.setGravity(Gravity.CENTER);
                                return view;
                            }
                        };
                        lv.setAdapter(adapter);
                    } else if (lv.getItemAtPosition(position).toString().equalsIgnoreCase("Others")) {
                        adapter = new ArrayAdapter<String>(ChatBot.this,
                                android.R.layout.simple_list_item_1, al) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view = super.getView(position, convertView, parent);
                                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                                tv.setTextColor(getResources().getColor(R.color.textColor));
                                tv.setGravity(Gravity.CENTER);
                                return view;
                            }
                        };
                        lv.setAdapter(adapter);
                        LayoutInflater inflater = LayoutInflater.from(ChatBot.this);
                        final View view1 = inflater.inflate(R.layout.popup_chatbotrequest, null);
                        issue = view1.findViewById(R.id.name_student_popup);
                        reg_no = view1.findViewById(R.id.regNo_student_popup);
                        mobile_no = view1.findViewById(R.id.mobileNo_student_popup);
                        lovelyCustomDialog = new LovelyCustomDialog(ChatBot.this)
                                .setView(view1)
                                .setTopColorRes(R.color.dashname)
                                .setTitle("Your Issue")
                                .setIcon(R.drawable.ic_baseline_chat_bubble_outline_24)
                                .setCancelable(false)
                                .setListener(R.id.add_btn_popup, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        String name = issue.getText().toString();
                                        String regNo = reg_no.getText().toString();
                                        String mobNo = mobile_no.getText().toString();

                                        if (mobNo.length()==10 && regNo.length()==10 && (mobNo.startsWith("9") || mobNo.startsWith("8") || mobNo.startsWith("7") || mobNo.startsWith("6"))){
                                            HashMap<String,String> hm=new HashMap<>();
                                            hm.put("issue",name);
                                            hm.put("regno",regNo);
                                            hm.put("mobno",mobNo);
                                            hm.put("email",acct.getEmail());
                                            hm.put("name",acct.getDisplayName());
                                            db.collection("chatbotreq").add(hm).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    lovelyCustomDialog.dismiss();
                                                    Toast.makeText(ChatBot.this, "Sent to admin Successfully!!!", Toast.LENGTH_SHORT).show();
                                                    final String str = "Your chat has been ended...";
                                                    lv.setOnItemClickListener(null);
                                                    lv.setSelector(new StateListDrawable());
                                                    Thread thread = new Thread() {
                                                        @Override
                                                        public void run() {
                                                            try {
                                                                synchronized (this) {
                                                                    wait(2500);
                                                                    runOnUiThread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            messageView.setSelection(messageView.getCount() - 1);
                                                                            TTS = new TextToSpeech(ChatBot.this, new TextToSpeech.OnInitListener() {
                                                                                @Override
                                                                                public void onInit(int status) {
                                                                                    if (status == TextToSpeech.SUCCESS) {
                                                                                        int result = TTS.setLanguage(Locale.UK);
                                                                                        TTS.setPitch((float) 1
                                                                                        );
                                                                                        TTS.setSpeechRate((float) 1);
                                                                                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                                                                            Log.e("TTS", "Language Not Supported");
                                                                                        }
                                                                                        else {

                                                                                            speak("The admin will contact you at the earliest...Thankyou");
                                                                                        }
                                                                                    } else {
                                                                                        Log.e("TTS", "Error initializing TTS Engine");

                                                                                    }
                                                                                }
                                                                            });
                                                                            tv.setVisibility(View.VISIBLE);
                                                                        }
                                                                    });
                                                                }
                                                            } catch (InterruptedException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }

                                                        ;
                                                    };
                                                    thread.start();
                                                    Thread thread1 = new Thread() {
                                                        @Override
                                                        public void run() {
                                                            try {
                                                                synchronized (this) {
                                                                    wait(3000);
                                                                    runOnUiThread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            tv.setVisibility(View.VISIBLE);
                                                                            tv.setText(str);
                                                                        }
                                                                    });
                                                                }
                                                            } catch (InterruptedException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }

                                                        ;
                                                    };
                                                    thread1.start();
                                                }
                                            });
                                        }else{
                                            Toast.makeText(ChatBot.this, "Please fill correct details..", Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                })
                                .setListener(R.id.cancel_btn_popup, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        lovelyCustomDialog.dismiss();
                                    }
                                })
                                .show();

                    } else {
                        final Message m = new Message(lv.getItemAtPosition(position).toString(), true);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                messageAdapter.add(m);
                                messageView.setSelection(messageView.getCount() - 1);
                            }
                        });
                        if (lv.getItemAtPosition(position).toString().equalsIgnoreCase("My order instructions were not followed")) {
                            final Message bot = new Message("Sorry for the inconvenience caused to you!!! Your issue has been reported to admin.", false);
                            final Message bot1 = new Message("The admin will contact you at the earliest...", false);
                            //  Message last=new Message("Your chat has been ended...",false);
                            HashMap<String,String> hm=new HashMap<>();
                            hm.put("issue","My order instructions were not followed");
                            hm.put("regno","");
                            hm.put("mobno","");
                            hm.put("email",acct.getEmail());
                            hm.put("name",acct.getDisplayName());
                            db.collection("chatbotreq").add(hm);
                            TTS = new TextToSpeech(ChatBot.this, new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    if (status == TextToSpeech.SUCCESS) {
                                        int result = TTS.setLanguage(Locale.UK);
                                        TTS.setPitch((float) 1
                                        );
                                        TTS.setSpeechRate((float) 1);
                                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                            Log.e("TTS", "Language Not Supported");
                                        }
                                        else {
                                            speak("Sorry for the inconvenience caused to you!!! Your issue has been reported to admin.");

                                        }
                                    } else {
                                        Log.e("TTS", "Error initializing TTS Engine");

                                    }
                                }
                            });

                            final String str = "Your chat has been ended...";
                            lv.setOnItemClickListener(null);
                            lv.setSelector(new StateListDrawable());
                            Thread thread = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        synchronized (this) {
                                            wait(2500);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    messageAdapter.add(bot);
                                                    messageView.setSelection(messageView.getCount() - 1);
                                                    TTS = new TextToSpeech(ChatBot.this, new TextToSpeech.OnInitListener() {
                                                        @Override
                                                        public void onInit(int status) {
                                                            if (status == TextToSpeech.SUCCESS) {
                                                                int result = TTS.setLanguage(Locale.UK);
                                                                TTS.setPitch((float) 1
                                                                );
                                                                TTS.setSpeechRate((float) 1);
                                                                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                                                    Log.e("TTS", "Language Not Supported");
                                                                }
                                                                else {

                                                                    speak("The admin will contact you at the earliest...Thankyou");
                                                                }
                                                            } else {
                                                                Log.e("TTS", "Error initializing TTS Engine");

                                                            }
                                                        }
                                                    });
                                                    tv.setVisibility(View.VISIBLE);
                                                }
                                            });
                                        }
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                ;
                            };
                            thread.start();
                            Thread thread1 = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        synchronized (this) {
                                            wait(3000);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    messageAdapter.add(bot1);
                                                    messageView.setSelection(messageView.getCount() - 1);
                                                    tv.setVisibility(View.VISIBLE);
                                                    tv.setText(str);
                                                }
                                            });
                                        }
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                ;
                            };
                            thread1.start();
                        }
                       else if (lv.getItemAtPosition(position).toString().equalsIgnoreCase("I have food taste, quality or quantity issue with my order")) {
                            final Message bot = new Message("Sorry for the inconvenience caused to you!!! Your issue has been reported to admin.", false);
                            final Message bot1 = new Message("The admin will contact you at the earliest...", false);
                            //  Message last=new Message("Your chat has been ended...",false);
                            HashMap<String,String> hm=new HashMap<>();
                            hm.put("issue","I have food taste, quality or quantity issue with my order");
                            hm.put("regno","");
                            hm.put("mobno","");
                            hm.put("email",acct.getEmail());
                            hm.put("name",acct.getDisplayName());
                            db.collection("chatbotreq").add(hm);
                            TTS = new TextToSpeech(ChatBot.this, new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    if (status == TextToSpeech.SUCCESS) {
                                        int result = TTS.setLanguage(Locale.UK);
                                        TTS.setPitch((float) 1
                                        );
                                        TTS.setSpeechRate((float) 1);
                                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                            Log.e("TTS", "Language Not Supported");
                                        }
                                        else {
                                            speak("Sorry for the inconvenience caused to you!!! Your issue has been reported to admin.");

                                        }
                                    } else {
                                        Log.e("TTS", "Error initializing TTS Engine");

                                    }
                                }
                            });

                            final String str = "Your chat has been ended...";
                            lv.setOnItemClickListener(null);
                            lv.setSelector(new StateListDrawable());
                            Thread thread = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        synchronized (this) {
                                            wait(2500);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    messageAdapter.add(bot);
                                                    messageView.setSelection(messageView.getCount() - 1);
                                                    TTS = new TextToSpeech(ChatBot.this, new TextToSpeech.OnInitListener() {
                                                        @Override
                                                        public void onInit(int status) {
                                                            if (status == TextToSpeech.SUCCESS) {
                                                                int result = TTS.setLanguage(Locale.UK);
                                                                TTS.setPitch((float) 1
                                                                );
                                                                TTS.setSpeechRate((float) 1);
                                                                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                                                    Log.e("TTS", "Language Not Supported");
                                                                }
                                                                else {

                                                                    speak("The admin will contact you at the earliest...Thankyou");
                                                                }
                                                            } else {
                                                                Log.e("TTS", "Error initializing TTS Engine");

                                                            }
                                                        }
                                                    });
                                                    tv.setVisibility(View.VISIBLE);
                                                }
                                            });
                                        }
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                ;
                            };
                            thread.start();
                            Thread thread1 = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        synchronized (this) {
                                            wait(3000);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    messageAdapter.add(bot1);
                                                    messageView.setSelection(messageView.getCount() - 1);
                                                    tv.setVisibility(View.VISIBLE);
                                                    tv.setText(str);
                                                }
                                            });
                                        }
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                ;
                            };
                            thread1.start();
                        }
                        else if (lv.getItemAtPosition(position).toString().equalsIgnoreCase("My request is not done yet")) {
                            HashMap<String,String> hm=new HashMap<>();
                            hm.put("issue","My request is not done yet");
                            hm.put("regno","");
                            hm.put("mobno","");
                            hm.put("email",acct.getEmail());
                            hm.put("name",acct.getDisplayName());
                            db.collection("chatbotreq").add(hm);
                            final Message bot = new Message("Your request is processed again and also sent to admin...", false);
                            TTS = new TextToSpeech(ChatBot.this, new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    if (status == TextToSpeech.SUCCESS) {
                                        int result = TTS.setLanguage(Locale.UK);
                                        TTS.setPitch((float) 1
                                        );
                                        TTS.setSpeechRate((float) 1);
                                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                            Log.e("TTS", "Language Not Supported");
                                        }
                                        else {
                                            speak("Your request is processed again and also sent to admin...Thankyou");

                                        }
                                    } else {
                                        Log.e("TTS", "Error initializing TTS Engine");

                                    }
                                }
                            });

                            final String str = "Your chat has been ended...";
                            lv.setOnItemClickListener(null);
                            lv.setSelector(new StateListDrawable());
                            Thread thread = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        synchronized (this) {
                                            wait(2500);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    messageAdapter.add(bot);
                                                    messageView.setSelection(messageView.getCount() - 1);
                                                    tv.setText(str);
                                                    tv.setVisibility(View.VISIBLE);
                                                }
                                            });
                                        }
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                ;
                            };
                            thread.start();

                        }
                        else if (lv.getItemAtPosition(position).toString().equalsIgnoreCase("Image is not getting uploaded")) {
                            HashMap<String,String> hm=new HashMap<>();
                            hm.put("issue","Image is not getting uploaded");
                            hm.put("regno","");
                            hm.put("mobno","");
                            hm.put("email",acct.getEmail());
                            hm.put("name",acct.getDisplayName());
                            db.collection("chatbotreq").add(hm);
                            final Message bot = new Message("Don't worry, We have a strong technical team... It will be resolved in next 1hour", false);
                            TTS = new TextToSpeech(ChatBot.this, new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    if (status == TextToSpeech.SUCCESS) {
                                        int result = TTS.setLanguage(Locale.UK);
                                        TTS.setPitch((float) 1
                                        );
                                        TTS.setSpeechRate((float) 1);
                                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                            Log.e("TTS", "Language Not Supported");
                                        }
                                        else {
                                            speak("Don't worry, We have a strong technical team... It will be resolved in next 1hour...Thankyou");

                                        }
                                    } else {
                                        Log.e("TTS", "Error initializing TTS Engine");

                                    }
                                }
                            });

                            final String str = "Your chat has been ended...";
                            lv.setOnItemClickListener(null);
                            lv.setSelector(new StateListDrawable());
                            Thread thread = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        synchronized (this) {
                                            wait(2500);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    messageAdapter.add(bot);
                                                    messageView.setSelection(messageView.getCount() - 1);
                                                    tv.setText(str);
                                                    tv.setVisibility(View.VISIBLE);
                                                }
                                            });
                                        }
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                ;
                            };
                            thread.start();

                        }
                        else if (lv.getItemAtPosition(position).toString().equalsIgnoreCase("The link is not working")) {
                            HashMap<String,String> hm=new HashMap<>();
                            hm.put("issue","The link is not working");
                            hm.put("regno","");
                            hm.put("mobno","");
                            hm.put("email",acct.getEmail());
                            hm.put("name",acct.getDisplayName());
                            db.collection("chatbotreq").add(hm);
                            final Message bot = new Message("It might be some technical glitch... Admin has been reported the situation!!! It will be resolved in 30minutes", false);
                            TTS = new TextToSpeech(ChatBot.this, new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    if (status == TextToSpeech.SUCCESS) {
                                        int result = TTS.setLanguage(Locale.UK);
                                        TTS.setPitch((float) 1
                                        );
                                        TTS.setSpeechRate((float) 1);
                                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                            Log.e("TTS", "Language Not Supported");
                                        }
                                        else {
                                            speak("It might be some technical glitch... Admin has been reported the situation!!! It will be resolved in 30minutes...Thankyou");

                                        }
                                    } else {
                                        Log.e("TTS", "Error initializing TTS Engine");

                                    }
                                }
                            });

                            final String str = "Your chat has been ended...";
                            lv.setOnItemClickListener(null);
                            lv.setSelector(new StateListDrawable());
                            Thread thread = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        synchronized (this) {
                                            wait(2500);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    messageAdapter.add(bot);
                                                    messageView.setSelection(messageView.getCount() - 1);
                                                    tv.setText(str);
                                                    tv.setVisibility(View.VISIBLE);
                                                }
                                            });
                                        }
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                ;
                            };
                            thread.start();

                        }
                        else if (lv.getItemAtPosition(position).toString().equalsIgnoreCase("The date for the event is wrong")) {
                            HashMap<String,String> hm=new HashMap<>();
                            hm.put("issue","The date for the event is wrong");
                            hm.put("regno","");
                            hm.put("mobno","");
                            hm.put("email",acct.getEmail());
                            hm.put("name",acct.getDisplayName());
                            db.collection("chatbotreq").add(hm);
                            final Message bot = new Message("Sorry for the inconvinience caused... Admin has been reported the situation!!! It will be resolved in 30minutes", false);
                            TTS = new TextToSpeech(ChatBot.this, new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    if (status == TextToSpeech.SUCCESS) {
                                        int result = TTS.setLanguage(Locale.UK);
                                        TTS.setPitch((float) 1
                                        );
                                        TTS.setSpeechRate((float) 1);
                                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                            Log.e("TTS", "Language Not Supported");
                                        }
                                        else {
                                            speak("Sorry for the inconvinience caused... Admin has been reported the situation!!! It will be resolved in 30minutes...Thankyou");

                                        }
                                    } else {
                                        Log.e("TTS", "Error initializing TTS Engine");

                                    }
                                }
                            });

                            final String str = "Your chat has been ended...";
                            lv.setOnItemClickListener(null);
                            lv.setSelector(new StateListDrawable());
                            Thread thread = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        synchronized (this) {
                                            wait(2500);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    messageAdapter.add(bot);
                                                    messageView.setSelection(messageView.getCount() - 1);
                                                    tv.setText(str);
                                                    tv.setVisibility(View.VISIBLE);
                                                }
                                            });
                                        }
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                ;
                            };
                            thread.start();

                        }
                        else if (lv.getItemAtPosition(position).toString().equalsIgnoreCase("The matter is irrelevant")) {
                            HashMap<String,String> hm=new HashMap<>();
                            hm.put("issue","The matter is irrelevant");
                            hm.put("regno","");
                            hm.put("mobno","");
                            hm.put("email",acct.getEmail());
                            hm.put("name",acct.getDisplayName());
                            db.collection("chatbotreq").add(hm);
                            final Message bot = new Message("Thankyou for the intimation.."+getEmojiByUnicode(0x1F607), false);
                            TTS = new TextToSpeech(ChatBot.this, new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    if (status == TextToSpeech.SUCCESS) {
                                        int result = TTS.setLanguage(Locale.UK);
                                        TTS.setPitch((float) 1
                                        );
                                        TTS.setSpeechRate((float) 1);
                                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                            Log.e("TTS", "Language Not Supported");
                                        }
                                        else {
                                            speak("It might be some technical glitch... Admin has been reported the situation!!! It will be resolved in 30minutes...Thankyou");

                                        }
                                    } else {
                                        Log.e("TTS", "Error initializing TTS Engine");

                                    }
                                }
                            });

                            final String str = "Your chat has been ended...";
                            lv.setOnItemClickListener(null);
                            lv.setSelector(new StateListDrawable());
                            Thread thread = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        synchronized (this) {
                                            wait(2500);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    messageAdapter.add(bot);
                                                    messageView.setSelection(messageView.getCount() - 1);
                                                    tv.setText(str);
                                                    tv.setVisibility(View.VISIBLE);
                                                }
                                            });
                                        }
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                ;
                            };
                            thread.start();

                        }
                    }
                }
            });

        }
    public String getEmojiByUnicode(int unicode){

        return new String(Character.toChars(unicode));
    }
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = TTS.setLanguage(Locale.UK);
            TTS.setPitch((float) 1
            );
            TTS.setSpeechRate((float) 1);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language Not Supported");
            }
            else {

            }
        } else {
            Log.e("TTS", "Error initializing TTS Engine");

        }
    }
    public void speak(String str) {
        TTS.speak(str, TextToSpeech.QUEUE_FLUSH, null, null);
    }
}
