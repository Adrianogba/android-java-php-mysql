package io.github.adrianogba.crud_java.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.github.adrianogba.crud_java.R;
import io.github.adrianogba.crud_java.VeiculoDetalheActivity;
import io.github.adrianogba.crud_java.model.Veiculo;

/**
 * Created by Adrianogba on 12/26/2017.
 */

public class VeiculoListAdapter extends BaseAdapter {

    Context context;
    ArrayList<Veiculo> veiculos;
    LayoutInflater inflater = null;

    TextView title;


    public VeiculoListAdapter(Context context, ArrayList<Veiculo> veiculos){
        this.context = context;
        this.veiculos = veiculos;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return veiculos.size();
    }

    @Override
    public Object getItem(int position) {
        return veiculos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // important method

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final Veiculo veiculo = veiculos.get(position);

        view = inflater.inflate(R.layout.item_veiculo, null);

        ///customList is the inflated view
        /// components in inflated view

        title = view.findViewById(R.id.textModelo);


        title.setText(veiculo.getModelo());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent i = new Intent(v.getContext(), VeiculoDetalheActivity.class);
                i.putExtra("id", veiculo.getId());
                v.getContext().startActivity(i);

            }
        });

        return view;
    }
}