package com.example.studentappmessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;

import com.example.studentappmessenger.fragments.ChatListFragment;
import com.example.studentappmessenger.fragments.ForumFragment;
import com.example.studentappmessenger.fragments.GroupChatsFragment;
import com.example.studentappmessenger.fragments.HomeFragment;
import com.example.studentappmessenger.fragments.NotificationsFragment;
import com.example.studentappmessenger.fragments.ProfileFragment;
import com.example.studentappmessenger.fragments.UsersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import com.example.studentappmessenger.notifications.Token;

public class DashboardActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    ActionBar actionBar;
private  BottomNavigationView navigationView;
    String mUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        actionBar=getSupportActionBar();

        firebaseAuth = FirebaseAuth.getInstance();
        navigationView= findViewById(R.id.navigation_btm );
        // mProfileTv = findViewById(R.id.profileTv);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);
        actionBar.setTitle("Home");
        HomeFragment fragment1=new HomeFragment();
        FragmentTransaction ft1= getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content,fragment1,"");
        ft1.commit();

        checkUserStatus();



    }

    @Override
    protected void onResume() {
        checkUserStatus();
        super.onResume();
    }

    public void updateToken(String token){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tokens");
        Token mToken = new Token(token);
        ref.child(mUID).setValue(mToken);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                   switch (menuItem.getItemId()){
                       case R.id.nav_home:
                           //Fragment Transaction Process
                           actionBar.setTitle("Home");
                         HomeFragment fragment1=new HomeFragment();
                         FragmentTransaction ft1= getSupportFragmentManager().beginTransaction();
                         ft1.replace(R.id.content,fragment1,"");
                         ft1.commit();
                           return true;

                       case R.id.nav_profile:
                           //Fragment Transaction Process
                         actionBar.setTitle("Profile");
                           ProfileFragment fragment2= new ProfileFragment();
                           FragmentTransaction ft2= getSupportFragmentManager().beginTransaction();
                           ft2.replace(R.id.content,fragment2,"");
                           ft2.commit();
                           return true;

                       case R.id.nav_users:
                           //Fragment Transaction Process
                           actionBar.setTitle("Users");
                           UsersFragment fragment3 = new UsersFragment();
                           FragmentTransaction ft3= getSupportFragmentManager().beginTransaction();
                           ft3.replace(R.id.content,fragment3,"");
                           ft3.commit();
                           return true;
                       case R.id.nav_chat:
                           //Fragment Transaction Process
                           actionBar.setTitle("Chats");
                           ChatListFragment fragment4 = new ChatListFragment();
                           FragmentTransaction ft4= getSupportFragmentManager().beginTransaction();
                           ft4.replace(R.id.content,fragment4,"");
                           ft4.commit();
                           return true;
                       case R.id.nav_more:
                           //Fragment Transaction Process
                        showMoreOptions();
                   }
                    return false;
                }
            } ;

    private void showMoreOptions() {
        PopupMenu popupMenu=new PopupMenu(this,navigationView, Gravity.END);
        popupMenu.getMenu().add(Menu.NONE,0,0,"Notifications");
        popupMenu.getMenu().add(Menu.NONE,1,0,"Group Chats");
        popupMenu.getMenu().add(Menu.NONE,2,0,"Forum");
        popupMenu.getMenu().add(Menu.NONE,3,0,"Sos");


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id= item.getItemId();
                if(id==0){
                    actionBar.setTitle("Notifications");
                    NotificationsFragment fragment5 = new NotificationsFragment();
                    FragmentTransaction ft5= getSupportFragmentManager().beginTransaction();
                    ft5.replace(R.id.content,fragment5,"");
                    ft5.commit();
                }
                else if (id==1){
                    actionBar.setTitle("Group Chats");
                    GroupChatsFragment fragment6 = new GroupChatsFragment();
                    FragmentTransaction ft6= getSupportFragmentManager().beginTransaction();
                    ft6.replace(R.id.content,fragment6,"");
                    ft6.commit();
                }
                else if (id==2){
                    actionBar.setTitle("Forum");
                   ForumFragment fragment7 = new ForumFragment();
                    FragmentTransaction ft7= getSupportFragmentManager().beginTransaction();
                    ft7.replace(R.id.content,fragment7,"");
                    ft7.commit();

                }
                else if(id==3){
                   openSos();
                }
                return false;
            }
        });
popupMenu.show();
    }

    private void openSos() {
        Intent intent = new Intent(this,MainActivity2.class);
        startActivity(intent);
    }

    private void checkUserStatus(){

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null){
            // mProfileTv.setText(user.getEmail());
            mUID = user.getUid();

            SharedPreferences sp = getSharedPreferences("SP_USER", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("Current_USERID", mUID);
            editor.apply();

            updateToken(FirebaseInstanceId.getInstance().getToken());

        }else{
            startActivity(new Intent(DashboardActivity.this,MainActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart(){
        checkUserStatus();
        super.onStart();
    }


}