package com.rbarrientos.tresenraya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.security.PrivilegedAction;

public class TresEnRayaActivity extends AppCompatActivity {

    private ImageView esquinaIzqSupImageView;
    private ImageView superiorImageView;
    private ImageView esquinaDerSupImageView;
    private ImageView izquierdaImageView;
    private ImageView centroImageView;
    private ImageView derechaImageView;
    private ImageView esquinaIzqInfImageView;
    private ImageView inferiorImageView;
    private ImageView esquinaDerInfImageView;

    private TextView resultadoTextView;

    private enum Turno {JUG1, JUG2};

    private  int[][] juego = {{-1, -1, -1},
                              {-1, -1, -1},
                              {-1, -1, -1}};

    private ImageView[][] casillasImageView  = new ImageView[juego.length][juego[0].length];

    private Turno turno;

    private Animation animacionCasilla;

    private Button reiniciarButton;
    private Button menuButton;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tres_en_raya);

        esquinaIzqSupImageView = findViewById(R.id.esquinaIzqSupImageView);
        superiorImageView = findViewById(R.id.superiorImageView);
        esquinaDerSupImageView = findViewById(R.id.esquinaDerSupImageView);
        izquierdaImageView = findViewById(R.id.izquierdaImageView);
        centroImageView = findViewById(R.id.centroImageView);
        derechaImageView = findViewById(R.id.derechaImageView);
        esquinaIzqInfImageView = findViewById(R.id.esquinaIzqInfImageView);
        inferiorImageView = findViewById(R.id.inferiorImageView);
        esquinaDerInfImageView = findViewById(R.id.esquinaDerInfImageView);

        resultadoTextView = findViewById(R.id.resultadoTextView);

        ViewGroup fichasGridLayout = findViewById(R.id.tableroGridLayout);
        //getChildCount() obtiene el numero de hijos de una vista
        for(int i=0; i<fichasGridLayout.getChildCount(); i++){
            //getChildAt() obtiene una referencia a la vistahija con el indice especificado
            casillasImageView[i / juego.length][i % juego[0].length] = (ImageView) fichasGridLayout.getChildAt(i);
        }
        for (int f=0; f<casillasImageView.length; f++){
            for(int c=0; c<casillasImageView[0].length; c++){
                casillasImageView[f][c].setOnClickListener(oyenteImageView);
            }
        }

        turno = Turno.JUG1;

        animacionCasilla = AnimationUtils.loadAnimation(this, R.anim.animacioncasilla);
        animacionCasilla.setAnimationListener(oyenteAnimacion);

        reiniciarButton = findViewById(R.id.reiniciarButton);
        reiniciarButton.setOnClickListener(oyenteReiniciarButton);
        menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(oyenteMenuButton);
    }//Create

    private final View.OnClickListener oyenteReiniciarButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            reiniciarJuego();

        }
    };//ReiniciarButton

    public void reiniciarJuego(){
        resultadoTextView.setText("");
        for(int f = 0; f < juego.length; f++){
            for(int c = 0; c < juego[0].length; c++){
                juego[f][c] = -1;
                casillasImageView[f][c].setImageResource(R.drawable.vacio);
                casillasImageView[f][c].setEnabled(true);
            }
        }
    }

    public void deshabilitarCasillas(){
        for(int f = 0; f < juego.length; f++){
            for(int c = 0; c < juego[0].length; c++){
                casillasImageView[f][c].setEnabled(false);
            }
        }
    }

    private final View.OnClickListener oyenteMenuButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(TresEnRayaActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    };//MenuButton


    private void actualizarGui(){
        for(int f = 0; f < juego.length; f++){
            for(int c = 0; c < juego[0].length; c++){
                if(juego[f][c] == 0){
                    casillasImageView[f][c].setImageResource(R.drawable.circulo);
                    //casillasImageView[f][c].setEnabled(false);
                }
                if(juego[f][c] == 1){
                    casillasImageView[f][c].setImageResource(R.drawable.equis);
                    //casillasImageView[f][c].setEnabled(false);
                }
            }
        }

    }//actualizarGui()

    private final View.OnClickListener oyenteImageView = new View.OnClickListener() {
        @NonNull
        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override
        public void onClick(View v) {
            int fila = 0;
            int columna = 0;
            //Determinar la ficha sobre la que se hizo click
            for(int f = 0; f < casillasImageView.length; f++){
                for(int c = 0; c < casillasImageView.length; c++){
                    //v es la vista sobre la que se hizo click
                    if(casillasImageView[f][c] == v){
                        fila = f;
                        columna = c;
                        casillasImageView[f][c].setEnabled(false);
                        break;
                    }
                }
            }
            if(turno == Turno.JUG1){
                juego[fila][columna] = 0;
                actualizarGui();
                if(verificacion(fila, columna) == true){
                    resultadoTextView.setText(getString(R.string.ganadorJug1));
                    deshabilitarCasillas();
                }else{
                    if(hayVacios() == false){
                        resultadoTextView.setText(getString(R.string.empate));
                        deshabilitarCasillas();
                    }
                }
                turno = Turno.JUG2;
            }else{
                juego[fila][columna] = 1;
                actualizarGui();
                if(verificacion(fila, columna) == true){
                    resultadoTextView.setText(getString(R.string.ganadorJug2));
                    deshabilitarCasillas();
                }else{
                    if(hayVacios() == false){
                        resultadoTextView.setText(getString(R.string.empate));
                        deshabilitarCasillas();
                    }
                }
                turno = Turno.JUG1;
            }

        }//onClick()
    };//oyenteImageView()

    private boolean hayVacios(){
        boolean ver = false;
        for(int f = 0; f < juego.length; f++){
            for(int c = 0; c < juego.length; c++){
                if(juego[f][c] == -1){
                    ver = true;
                    break;
                }
            }
            if(ver == true){
                break;
            }
        }
        return ver;
    };//hayVacios

    private final Animation.AnimationListener oyenteAnimacion = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            reiniciarButton.setEnabled(false);
            menuButton.setEnabled(false);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            reiniciarButton.setEnabled(true);
            menuButton.setEnabled(true);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private void animarCasillas(int f, int c, int f1, int c1, int f2, int c2){
        casillasImageView[f][c].startAnimation(animacionCasilla);
        casillasImageView[f1][c1].startAnimation(animacionCasilla);
        casillasImageView[f2][c2].startAnimation(animacionCasilla);
    }

    private boolean verificacion(int f, int c){
        boolean ver = false;
        if(casillasImageView[f][c] == esquinaIzqSupImageView){
            if(((juego[f][c]) == (juego[f+1][c])) && ((juego[f][c]) == (juego[f+2][c]))){
                ver = true;
                animarCasillas(f, c, f+1, c, f+2, c);
            }else{
                if(((juego[f][c]) == (juego[f+1][c+1])) && ((juego[f][c]) == (juego[f+2][c+2]))){
                    ver = true;
                    animarCasillas(f, c, f+1, c+1, f+2, c+2);
                }else{
                    if(((juego[f][c]) == (juego[f][c+1])) && ((juego[f][c]) == (juego[f][c+2]))){
                        ver = true;
                        animarCasillas(f, c, f, c+1, f, c+2);
                    }else{
                        ver = false;
                    }
                }
            }
        }
        if(casillasImageView[f][c] == superiorImageView){
            if(((juego[f][c]) == (juego[f+1][c])) && ((juego[f][c]) == (juego[f+2][c]))){
                ver = true;
                animarCasillas(f, c, f+1, c, f+2, c);
            }else{
                if(((juego[f][c]) == (juego[f][c-1])) && ((juego[f][c]) == (juego[f][c+1]))){
                    ver = true;
                    animarCasillas(f, c, f, c-1, f, c+1);
                }else{
                    ver = false;
                }
            }
        }
        if(casillasImageView[f][c] == esquinaDerSupImageView){
            if(((juego[f][c]) == (juego[f+1][c])) && ((juego[f][c]) == (juego[f+2][c]))){
                ver = true;
                animarCasillas(f, c, f+1, c, f+2, c);
            }else{
                if(((juego[f][c]) == (juego[f+1][c-1])) && ((juego[f][c]) == (juego[f+2][c-2]))){
                    ver = true;
                    animarCasillas(f, c, f+1, c-1, f+2, c-2);
                }else{
                    if(((juego[f][c]) == (juego[f][c-1])) && ((juego[f][c]) == (juego[f][c-2]))){
                        ver = true;
                        animarCasillas(f, c, f, c-1, f, c-2);
                    }else{
                        ver = false;
                    }
                }
            }
        }
        if(casillasImageView[f][c] == izquierdaImageView){
            if(((juego[f][c]) == (juego[f-1][c])) && ((juego[f][c]) == (juego[f+1][c]))){
                ver = true;
                animarCasillas(f, c, f-1, c, f+1, c);
            }else{
                if(((juego[f][c]) == (juego[f][c+1])) && ((juego[f][c]) == (juego[f][c+2]))){
                    ver = true;
                    animarCasillas(f, c, f, c+1, f, c+2);
                }else{
                    ver = false;
                }
            }
        }
        if(casillasImageView[f][c] == centroImageView){
            if(((juego[f][c]) == (juego[f-1][c-1])) && ((juego[f][c]) == (juego[f+1][c+1]))){
                ver = true;
                animarCasillas(f, c, f-1, c-1, f+1, c+1);
            }else{
                if(((juego[f][c]) == (juego[f-1][c])) && ((juego[f][c]) == (juego[f+1][c]))){
                    ver = true;
                    animarCasillas(f, c, f-1, c, f+1, c);
                }else{
                    if(((juego[f][c]) == (juego[f-1][c+1])) && ((juego[f][c]) == (juego[f+1][c-1]))){
                        ver = true;
                        animarCasillas(f, c, f-1, c+1, f+1, c-1);
                    }else{
                        if(((juego[f][c]) == (juego[f][c-1])) && ((juego[f][c]) == (juego[f][c+1]))){
                            ver = true;
                            animarCasillas(f, c, f, c-1, f, c+1);
                        }else{
                            ver = false;
                        }
                    }
                }
            }
        }
        if(casillasImageView[f][c] == derechaImageView){
            if(((juego[f][c]) == (juego[f-1][c])) && ((juego[f][c]) == (juego[f+1][c]))){
                ver = true;
                animarCasillas(f, c, f-1, c, f+1, c);
            }else{
                if(((juego[f][c]) == (juego[f][c-1])) && ((juego[f][c]) == (juego[f][c-2]))){
                    ver = true;
                    animarCasillas(f, c, f, c-1, f, c-2);
                }else{
                    ver = false;
                }
            }
        }
        if(casillasImageView[f][c] == esquinaIzqInfImageView){
            if(((juego[f][c]) == (juego[f-1][c])) && ((juego[f][c]) == (juego[f-2][c]))){
                ver = true;
                animarCasillas(f, c, f-1, c, f-2, c);
            }else{
                if(((juego[f][c]) == (juego[f-1][c+1])) && ((juego[f][c]) == (juego[f-2][c+2]))){
                    ver = true;
                    animarCasillas(f, c, f-1, c+1, f-2, c+2);
                }else{
                    if(((juego[f][c]) == (juego[f][c+1])) && ((juego[f][c]) == (juego[f][c+2]))){
                        ver = true;
                        animarCasillas(f, c, f, c+1, f, c+2);
                    }else{
                        ver = false;
                    }
                }
            }
        }
        if(casillasImageView[f][c] == inferiorImageView){
            if(((juego[f][c]) == (juego[f-1][c])) && ((juego[f][c]) == (juego[f-2][c]))){
                ver = true;
                animarCasillas(f, c, f-1, c, f-2, c);
            }else{
                if(((juego[f][c]) == (juego[f][c-1])) && ((juego[f][c]) == (juego[f][c+1]))){
                    ver = true;
                    animarCasillas(f, c, f, c-1, f, c+1);
                }else{
                    ver = false;
                }
            }
        }
        if(casillasImageView[f][c] == esquinaDerInfImageView){
            if(((juego[f][c]) == (juego[f-1][c])) && ((juego[f][c]) == (juego[f-2][c]))){
                ver = true;
                animarCasillas(f, c, f-1, c, f-2, c);
            }else{
                if(((juego[f][c]) == (juego[f-1][c-1])) && ((juego[f][c]) == (juego[f-2][c-2]))){
                    ver = true;
                    animarCasillas(f, c, f-1, c-1, f-2, c-2);
                }else{
                    if(((juego[f][c]) == (juego[f][c-1])) && ((juego[f][c]) == (juego[f][c-2]))){
                        ver = true;
                        animarCasillas(f, c, f, c-1, f, c-2);
                    }else{
                        ver = false;
                    }
                }
            }
        }
        return ver;
    };//Verificar
}