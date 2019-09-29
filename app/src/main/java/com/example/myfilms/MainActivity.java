package com.example.myfilms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button botaoCadastrar, botaoAssistidos, botaoAssistir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //recuperar ids dos botoes
        botaoCadastrar = findViewById(R.id.botaoCadastrarID);
        botaoAssistidos = findViewById(R.id.botaoAssistidosID);
        botaoAssistir = findViewById(R.id.botaoAssistirID);

        //tratar ações que acontece ao clicar neles

        //botao de cadastro leva para activity cadastro
        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telaCadastro = new Intent(MainActivity.this,CadastroActivity.class);
                startActivity(telaCadastro);
            }
        });

        //botao assisitir leva para a activity filmes com uma string assistir indicando o que deve ser feito
        botaoAssistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telaAssistir = new Intent(MainActivity.this,FIlmesActivity.class);
                telaAssistir.putExtra("assistido",false);
                startActivity(telaAssistir);
            }
        });

        //botao assisitidos leva para a activity filmes com uma string assistidos indicando o que deve ser feito
        botaoAssistidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telaAssistido = new Intent(MainActivity.this,FIlmesActivity.class);
                telaAssistido.putExtra("assistido",true);
                startActivity(telaAssistido);
            }
        });
    }
}
