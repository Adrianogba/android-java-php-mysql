package io.github.adrianogba.crud_java;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AddVeiculo extends AppCompatActivity {

    EditText marca;
    EditText modelo;
    EditText cor;
    EditText ano;
    EditText preco;
    EditText descricao;
    TextView txtehnovo;
    String ehnovo="-1";

    RelativeLayout rlehnovo;

    ProgressDialog progressDialog;
    RequestQueue queue;
    Bundle bundle;

    //variaveis a guardar no modo edição
    String dt_cadastro;
    String id;
    boolean editar=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_veiculo);

        marca = findViewById(R.id.etmarca);
        modelo = findViewById(R.id.etmodelo);
        cor = findViewById(R.id.etcor);
        ano = findViewById(R.id.etano);
        preco = findViewById(R.id.etpreco);
        descricao = findViewById(R.id.etdescricao);
        rlehnovo = findViewById(R.id.rlEhnovo);
        txtehnovo = findViewById(R.id.txtehnovo);

        queue = Volley.newRequestQueue(this);


        rlehnovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] lista = new String[2];
                lista[0]="Novo";
                lista[1]="Usado";
                AlertDialog.Builder builder = new AlertDialog.Builder(AddVeiculo.this);
                builder.setTitle("O veículo é Novo ou Usado?");
                builder.setItems(lista, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        if (which==0){
                            txtehnovo.setText("Novo");
                            ehnovo="1";
                        }else{
                            txtehnovo.setText("Usado");
                            ehnovo="0";
                        }

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


        findViewById(R.id.btncadastrar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(testForm()){

                    progressDialog = new ProgressDialog(AddVeiculo.this);
                    progressDialog.setMessage("Carregando...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    String url = getString(R.string.webservice)+"addVeiculo.php";
                    if (editar){
                        url = getString(R.string.webservice)+"updateVeiculo.php";
                    }

                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                            url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response
                            // string.
                            try{
                                progressDialog.cancel();
                                Toast.makeText(AddVeiculo.this, response, Toast.LENGTH_LONG).show();
                                Intent i = new Intent(AddVeiculo.this, MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);

                            }catch (Exception e){
                                Toast.makeText(AddVeiculo.this, "Problemas na comuncação com o servidor.", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                                progressDialog.cancel();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.cancel();

                            Toast.makeText(AddVeiculo.this,
                                    "Problema na comunicação com o servidor!",
                                    Toast.LENGTH_LONG).show();
                        }

                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            if (editar){params.put("PATH", "updateVeiculo");}else{params.put("PATH", "addVeiculo");}
                            params.put("MARCA", marca.getText().toString().trim());
                            params.put("MODELO", modelo.getText().toString().trim());
                            params.put("PRECO", preco.getText().toString().trim());
                            params.put("COR", cor.getText().toString().trim());
                            params.put("ANO", ano.getText().toString().trim());
                            params.put("DESCRICAO", descricao.getText().toString().trim());
                            params.put("EHNOVO", ehnovo);
                            if(editar){
                                params.put("ID", id);
                                //params.put("DT_CADASTRO", dt_cadastro);
                            }

                            return params;
                        }
                    };

                    queue.add(stringRequest);
                }


            }
        });


        try{
            bundle = getIntent().getExtras();
            if (bundle.getString("editar").equalsIgnoreCase("editar")){

                editar=true;


                marca.setText(bundle.getString("marca"), TextView.BufferType.EDITABLE);
                modelo.setText(bundle.getString("modelo"), TextView.BufferType.EDITABLE);
                cor.setText(bundle.getString("cor"), TextView.BufferType.EDITABLE);
                ano.setText(bundle.getString("ano"), TextView.BufferType.EDITABLE);
                preco.setText(bundle.getString("preco"), TextView.BufferType.EDITABLE);
                descricao.setText(bundle.getString("descricao"), TextView.BufferType.EDITABLE);

                id=bundle.getString("id");
                dt_cadastro=bundle.getString("dt_cadastro");

                ehnovo=bundle.getString("ehnovo");
                if(ehnovo=="1"){
                    txtehnovo.setText("Novo");

                }else{
                    txtehnovo.setText("Usado");
                }
            }

        }catch (Exception e){}

    }

    public Boolean testForm(){
        if(marca.getText().toString().trim().equalsIgnoreCase("")
                || modelo.getText().toString().trim().equalsIgnoreCase("")
                || cor.getText().toString().trim().equalsIgnoreCase("")
                || ano.getText().toString().trim().equalsIgnoreCase("")
                || preco.getText().toString().trim().equalsIgnoreCase("")
                || descricao.getText().toString().trim().equalsIgnoreCase("")
                || ehnovo=="-1"
                ){
            Toast.makeText(this, "Preencha todo o formulário.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
