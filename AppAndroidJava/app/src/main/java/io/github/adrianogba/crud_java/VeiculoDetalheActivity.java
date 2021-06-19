package io.github.adrianogba.crud_java;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.github.adrianogba.crud_java.model.Veiculo;

public class VeiculoDetalheActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    private JsonParser jsonParser;
    private Gson gson;
    RequestQueue queue;
    Bundle bundle;
    Veiculo veiculo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veiculo_detalhe);

        bundle = getIntent().getExtras();

        progressDialog = new ProgressDialog(VeiculoDetalheActivity.this);
        progressDialog.setMessage("Carregando...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        jsonParser = new JsonParser();
        gson = new Gson();

        queue = Volley.newRequestQueue(this);

        findViewById(R.id.btnvoltar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        findViewById(R.id.btneditar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), AddVeiculo.class);
                i.putExtra("editar", "editar");
                i.putExtra("marca", veiculo.getMarca());
                i.putExtra("modelo", veiculo.getModelo());
                i.putExtra("cor", veiculo.getCor());
                i.putExtra("ano", veiculo.getAno());
                i.putExtra("preco", veiculo.getPreco());
                i.putExtra("ehnovo", veiculo.getEhnovo());
                i.putExtra("descricao", veiculo.getDescricao());
                i.putExtra("id", veiculo.getId());
                i.putExtra("dt_cadastro", veiculo.getDt_cadastro());
                v.getContext().startActivity(i);


            }
        });
        findViewById(R.id.btnremover).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] lista = new String[2];
                lista[0]="Sim";
                lista[1]="Não";

                AlertDialog.Builder builder = new AlertDialog.Builder(VeiculoDetalheActivity.this);
                builder.setTitle("Tem certeza que deseja remover este veículo?");
                builder.setItems(lista, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        if (which==0){
                            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                                    getString(R.string.webservice)+"deleteVeiculo.php", new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    // Display the first 500 characters of the response
                                    // string.
                                    try{

                                        progressDialog.cancel();
                                        Toast.makeText(VeiculoDetalheActivity.this, response, Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(VeiculoDetalheActivity.this, MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);


                                    }catch (Exception e){
                                        Toast.makeText(VeiculoDetalheActivity.this, "Problemas na comuncação com o servidor.", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                        progressDialog.cancel();

                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.cancel();

                                    Toast.makeText(VeiculoDetalheActivity.this,
                                            "Problema na comunicação com o servidor!",
                                            Toast.LENGTH_LONG).show();

                                }

                            }) {
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("PATH", "deleteVeiculo");
                                    params.put("ID", bundle.getString("id"));


                                    return params;
                                };
                            };
                            // Add the request to the RequestQueue.

                            queue.add(stringRequest);

                        }

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();




            }
        });




        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                getString(R.string.webservice)+"getVeiculo.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response
                // string.


                try{

                    JsonArray mJson = (JsonArray) jsonParser
                            .parse(response);

                    veiculo = gson.fromJson(mJson.get(0), Veiculo.class);

                    TextView marca = findViewById(R.id.txtmarca);
                    TextView modelo = findViewById(R.id.txtmodelo);
                    TextView cor = findViewById(R.id.txtcor);
                    TextView ano = findViewById(R.id.txtano);
                    TextView preco = findViewById(R.id.txtpreco);
                    TextView descricao = findViewById(R.id.txtdescricao);
                    TextView ehnovo = findViewById(R.id.txtehnovo);
                    TextView dt_cadastro = findViewById(R.id.txtdt_cadastro);
                    TextView dt_atualizacao = findViewById(R.id.txtdt_atualizacao);

                    marca.setText(veiculo.getMarca());
                    modelo.setText(veiculo.getModelo());
                    cor.setText(veiculo.getCor());
                    ano.setText(veiculo.getAno());
                    preco.setText(veiculo.getPreco());
                    descricao.setText(veiculo.getDescricao());

                    if(veiculo.getEhnovo().equalsIgnoreCase("1")){
                        ehnovo.setText("Novo");
                    }else {
                        ehnovo.setText("Usado");
                    }

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    SimpleDateFormat formatterbr = new SimpleDateFormat("dd/MM/yyyy 'às' hh:mm");
                    Date result = formatter.parse (veiculo.getDt_cadastro());
                    dt_cadastro.setText(formatterbr.format(result));

                    try{
                        if (!veiculo.getDt_atualizacao().trim().equalsIgnoreCase("")){
                            result = formatter.parse (veiculo.getDt_atualizacao());
                            dt_atualizacao.setText(formatterbr.format(result));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        dt_atualizacao.setText(formatterbr.format(result));
                    }

                    progressDialog.cancel();


                }catch (Exception e){
                    Toast.makeText(VeiculoDetalheActivity.this, "Problemas na comuncação com o servidor.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    progressDialog.cancel();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();

                Toast.makeText(VeiculoDetalheActivity.this,
                        "Problema na comunicação com o servidor!",
                        Toast.LENGTH_LONG).show();

            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("PATH", "getVeiculoDetalhe");
                params.put("ID", bundle.getString("id"));


                return params;
            };
        };
        // Add the request to the RequestQueue.

        queue.add(stringRequest);


    }



}
