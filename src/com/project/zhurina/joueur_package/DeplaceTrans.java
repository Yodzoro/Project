package com.project.zhurina.joueur_package;

import com.project.zhurina.board_package.Board;
import com.project.zhurina.board_package.Deplacer;

public class DeplaceTrans {


    //transition de deplacement
    private final Board transboard;
    private final Deplacer deplacer;
    //status va nous indique si c'est possible pour nous de faire des deplacement ou non
    private final DeplaceEtat deplaceEtat;

    public DeplaceTrans(Board transboard, Deplacer deplacer, DeplaceEtat deplaceEtat) {
        this.transboard = transboard;
        this.deplacer = deplacer;
        this.deplaceEtat = deplaceEtat;

    }
    public Board getTransboard() {
        return this.transboard;
    }


    public DeplaceEtat getDeplaceEtat() {
        return this.deplaceEtat;
    }
}
