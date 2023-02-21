package com.example.testdbentity.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.testdbentity.InformationEntity;
import com.example.testdbentity.MainPageActivity;
import com.example.testdbentity.R;
import com.example.testdbentity.homefragment.bookinghotel.database.BookingHotelViewModel;
import com.example.testdbentity.login.database.LoginEntity;
import com.example.testdbentity.login.database.LoginViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.ArrayList;
import java.util.List;

public class LoginGoogleActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;
    private BookingHotelViewModel bookingHotelViewModel;

    private boolean checkAdd = false;


    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int checkLogin = getIntent().getIntExtra("startByGoogle", 0);
        checkLoginWithGoogle(checkLogin);

    }
    private void checkLoginWithGoogle(int checkLogin) {
        if (checkLogin == 5) {
            //check da co tai khoan chua
            loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
            bookingHotelViewModel = new ViewModelProvider(this).get(BookingHotelViewModel.class);

            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
            gsc = GoogleSignIn.getClient(this, gso);
            GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(this);
            if (gsa != null) {
                String emailS = gsa.getEmail();
                String firstnameS = gsa.getFamilyName();
                String lastnameS = gsa.getGivenName();

                loginViewModel.getAccountSameEmail(emailS).observe(this, new Observer<LoginEntity>() {
                    @Override
                    public void onChanged(LoginEntity loginEntity) {
                        if(loginEntity != null && !checkAdd)
                        {
                            //neu co roi
                            bookingHotelViewModel.getInformationOneUser(loginEntity.idAccount).observe(LoginGoogleActivity.this, new Observer<InformationEntity>() {
                                @Override
                                public void onChanged(InformationEntity informationEntity) {
                                    Intent intent = new Intent(LoginGoogleActivity.this, MainPageActivity.class);
                                    if(informationEntity != null)
                                    {
                                        intent.putExtra("idUserInformation", informationEntity.idInformationUser);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), "Dang ky khong thanh cong", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });                        }
                        else if(loginEntity != null && checkAdd)
                        {
                            List<InformationEntity> informationEntities = new ArrayList<>();
                            //add thong tin nguoi dung moi
                            InformationEntity informationEntity = new InformationEntity(firstnameS, lastnameS, "", 1, emailS);
                            informationEntity.foreignKeyInformationUser = loginEntity.idAccount;
                            informationEntities.add(informationEntity);
                            bookingHotelViewModel.insertInformation(informationEntities);
                            Toast.makeText(getApplicationContext(), "Dang ky thanh cong:" + String.valueOf(loginEntity.idAccount), Toast.LENGTH_SHORT).show();

                            //check null
                            bookingHotelViewModel.getInformationOneUser(loginEntity.idAccount).observe(LoginGoogleActivity.this, new Observer<InformationEntity>() {
                                @Override
                                public void onChanged(InformationEntity informationEntity) {
                                    Intent intent = new Intent(LoginGoogleActivity.this, MainPageActivity.class);
                                    if(informationEntity != null)
                                    {
                                        intent.putExtra("idUserInformation", informationEntity.idInformationUser);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), "Dang ky khong thanh cong", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else
                        {
                            //neu email chua ton tai va pass hop le
                            loginEntity = new LoginEntity(firstnameS, lastnameS, emailS, "", "");
                            loginViewModel.createAccount(loginEntity);
                            checkAdd = true;
                            //loginEntity nay la cai de add. Sau khi add se co idAccount khac. Nen sau khi add no se quay lai ham observe
                            //boi vi no da add nen coi nhu thay doi

                        }
                    }
                });
            }
        }
    }
}