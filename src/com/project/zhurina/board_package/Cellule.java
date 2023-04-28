package com.project.zhurina.board_package;
import com.project.zhurina.piece_package.Piece;

import java.util.HashMap;
import java.util.Map;
import com.google.common.collect.ImmutableMap;

public abstract class Cellule {
    //il y a 64 cellules(8x8) qui sont vide et pas vide
    protected final int coordonnee;
    //on doit creer l'espace avec les cellules vide
    private static final Map<Integer, CelluleVide> CELLULE_VIDE = creationCelluleVide();

    private static Map<Integer, CelluleVide> creationCelluleVide() {
        final Map<Integer, CelluleVide> mapVide= new HashMap<>();
        for(int i = 0; i<Utilitaires.NUM_CELLULES; i++){
            //on mettre dans notre map les cellules vide(64 cellules)
            mapVide.put(i, new CelluleVide(i));
        }
        //mapVide is mutable, donc pour le fait IMmutable on peut utiliser cette biblioteque
        return ImmutableMap.copyOf(mapVide);
    }
    //on doit creer la cellule, mais pour cela on doit regarder si piece non vide ou vide.
    public static Cellule craetionCellule(final int coordonnee, final Piece p){
        return p != null ? new CelluleNonVide(coordonnee, p) : CELLULE_VIDE.get(coordonnee);
    }

    private Cellule(final int coordonnee) {
        this.coordonnee = coordonnee;
    }
    //cellule est occupée?
    public abstract boolean celluleOccupee();

    public abstract Piece getPiece();

    public int getCelluleCoor() {
        return this.coordonnee;
    }

    /////////////////////////////////////////////////////////////////////
    public final static class CelluleVide extends Cellule{
        //car cellule est vide on ne doit pas mettre qqchose dedans
        private CelluleVide(final int coordonnee) {
            super(coordonnee);
        }

        //pour afficher sur le terminale les cas vide
        @Override
        public String toString(){
            return " ";
        }

        @Override
        public boolean celluleOccupee() {
            return false;
        }

        @Override
        public Piece getPiece() {
            return null;
        }
    }
///////////////////////////////////////////////////////////////////
    public final static class CelluleNonVide extends Cellule{
        //car cellule n'est pas vide on doit mettre le piece dedans
        private final Piece p;
        private CelluleNonVide(int coordonnee, final Piece p) {
            super(coordonnee);
            this.p = p;
        }

        @Override
        public String toString(){
            //si la piece est noir - A..Z, sinon a..z
            return getPiece().getColor().estNoir() ? getPiece().toString().toLowerCase() : getPiece().toString();
        }

        @Override
        public boolean celluleOccupee() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return this.p;
        }
    }
}
