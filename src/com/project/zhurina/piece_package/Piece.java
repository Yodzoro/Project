package com.project.zhurina.piece_package;

import com.project.zhurina.Color;
import com.project.zhurina.board_package.Board;
import com.project.zhurina.board_package.Deplacer;

import java.util.Collection;

public abstract class Piece {
    //pour les positions on va utiliser les :
    protected final Type pieceType;
    protected final int position;
    protected final Color color;
    protected final boolean premierPas;
    private final int hashcodeCachee;

    public Piece(final Type pieceType, final Color color, final int position, boolean premierPas) {//
        this.pieceType = pieceType;
        this.color = color;
        this.position = position;
        this.premierPas = premierPas;
        this.hashcodeCachee = trouveHashCode();
    }
    public enum Type{
        ROI("R"){
            @Override
            public boolean estRoi() {
                return true;
            }

            @Override
            public boolean estTour() {
                return false;
            }
        },
        DAME("D"){
            @Override
            public boolean estRoi() {
                return false;
            }

            @Override
            public boolean estTour() {
                return false;
            }
        },
        FOU("F"){
            @Override
            public boolean estRoi() {
                return false;
            }

            @Override
            public boolean estTour() {
                return false;
            }
        },
        CAVALIER("C"){
            @Override
            public boolean estRoi() {
                return false;
            }

            @Override
            public boolean estTour() {
                return false;
            }
        },
        TOUR("T") {
            @Override
            public boolean estRoi() {
                return false;
            }

            @Override
            public boolean estTour() {
                return true;
            }
        },
        PION("P"){
            @Override
            public boolean estRoi() {
                return false;
            }

            @Override
            public boolean estTour() {
                return false;
            }
        };
        private final String name;
        Type(String name) {
            this.name = name;
        }
        @Override
        public String toString(){
            return this.name;
        }
        public abstract boolean estRoi();

        public abstract boolean estTour();
    }


    private int trouveHashCode() {//V
        int result = pieceType.hashCode();
        result = 31*result+color.hashCode();
        result = 31*result+position;
        result = 31*result+(premierPas ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(final Object other){
        if(this==other){return true;}
        if(!(other instanceof Piece)){return false;}
        final Piece pAuther = (Piece) other;
        return this.position == pAuther.position && this.pieceType == pAuther.pieceType && this.color == pAuther.color && this.premierPas == pAuther.premierPas;
    }

    @Override
    public int hashCode(){
        return this.hashcodeCachee;
    }

    public Type getPieceType() {
        return this.pieceType;
    }

    //on ne peut pas deplacer n'importe quelle piece où on veut, donc on doit proposer pour la piece les déplacements possibles
    public abstract Collection<Deplacer> possibleDeplacer(final Board board);

    public abstract Piece deplacePiece(Deplacer deplacer);//movePiece

    public Color getColor() {
        return this.color;
    }

    public boolean premierPas(){
        return this.premierPas;
    }
    public int getPosition() {
        return this.position;
    }


}
