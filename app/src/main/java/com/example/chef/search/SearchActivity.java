package com.example.chef.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chef.R;
import com.example.chef.user.UserInfoActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    private String mQueryIngredients = "";
    private List<String> mIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Spinner recipeCountSpinner = findViewById(R.id.recipeCount_spinner);
        Spinner preparationTimeSpinner = findViewById(R.id.preparationTime_spinner);
        ArrayAdapter<CharSequence> recipeCountSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.recipeCount_spinner_data, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> preparationTimeSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.preparationTime_spinner_data, android.R.layout.simple_spinner_item);
        recipeCountSpinner.setAdapter(recipeCountSpinnerAdapter);
        preparationTimeSpinner.setAdapter(preparationTimeSpinnerAdapter);

        EditText ingredientsInputField = findViewById(R.id.ingredients_input_field);
        ingredientsInputField.setOnEditorActionListener(this);

        //Test for ingredients recycler view
        mIngredients = new ArrayList<>();
        mIngredients.add("Tomato");
        mIngredients.add("Onion");
        mIngredients.add("Pickle");

        RecyclerView ingredientsRecyclerView = findViewById(R.id.ingredients_list);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(this, mIngredients);
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_user_info) {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if(currentUser == null) {
                displayToast("You are not logged in!");
                return true;
            }
            else{
                Intent intent = new Intent(this, UserInfoActivity.class);
                startActivity(intent);
            }

        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_DONE) {
            mIngredients.add(v.getText().toString());
            v.setText(null);
        }
        return false;
    }

    public void searchForRecipes(View view) {
        new RecipesSearchTask(getApplicationContext()).execute(new RecipesSearch(mIngredients,5,60));
    }

    public void displayToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

}
