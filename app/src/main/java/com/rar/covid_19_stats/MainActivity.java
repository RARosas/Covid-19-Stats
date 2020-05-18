package com.rar.covid_19_stats;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ArrayList<Pais> paises = new ArrayList<>();
    ArrayList<String> nombrePaises = new ArrayList<>();
    private static final String HTTP_ALL = "https://corona.lmao.ninja/v2/countries?yesterday&sort";
    Spinner spinner;
    TextView updated, country, cases, todaycases, deaths, todaydeaths, recovered, active, critical, tests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Start();
    }

    public void Start() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setSubtitle(R.string.app_subname);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setSubtitleTextColor(getResources().getColor(android.R.color.white));

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, HTTP_ALL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        desSerializeJSON(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error al procesar la Base", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        queue.add(request);

        updated = findViewById(R.id.updated_description);
        country = findViewById(R.id.country_description);
        cases = findViewById(R.id.cases_description);
        todaycases = findViewById(R.id.todaycases_description);
        deaths = findViewById(R.id.deaths_description);
        todaydeaths = findViewById(R.id.todaydeaths_description);
        recovered = findViewById(R.id.recovered_description);
        active = findViewById(R.id.active_description);
        critical = findViewById(R.id.critical_description);
        tests = findViewById(R.id.tests_description);
    }

    public void desSerializeJSON (JSONArray object) {
        for (int i=0; i<object.length(); i++)   {
            try {
                JSONObject pais = object.getJSONObject(i);
                Pais actual = new Pais();
                actual.setUpdated(pais.getLong("updated"));
                actual.setCountry(pais.getString("country"));
                actual.setCases(pais.getInt("cases"));
                actual.setTodayCases(pais.getInt("todayCases"));
                actual.setDeaths(pais.getInt("deaths"));
                actual.setTodayDeaths(pais.getInt("todayDeaths"));
                actual.setRecovered(pais.getInt("recovered"));
                actual.setActive(pais.getInt("active"));
                actual.setCritical(pais.getInt("critical"));
                actual.setTests(pais.getInt("tests"));
                paises.add(actual);
                nombrePaises.add(actual.getCountry());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, nombrePaises);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.notifyDataSetChanged();
        spinner.setPrompt("Select your country");
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Date date = new Date(paises.get(position).getUpdated());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = dateFormat.format(date);
        updated.setText(fecha);
        country.setText(paises.get(position).getCountry());
        cases.setText(String.valueOf(paises.get(position).getCases()));
        todaycases.setText(String.valueOf(paises.get(position).getTodayCases()));
        deaths.setText(String.valueOf(paises.get(position).getDeaths()));
        todaydeaths.setText(String.valueOf(paises.get(position).getTodayCases()));
        recovered.setText(String.valueOf(paises.get(position).getRecovered()));
        active.setText(String.valueOf(paises.get(position).getActive()));
        critical.setText(String.valueOf(paises.get(position).getCritical()));
        tests.setText(String.valueOf(paises.get(position).getTests()));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
