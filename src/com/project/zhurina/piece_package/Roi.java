package com.project.zhurina.piece_package;

import com.google.common.collect.ImmutableList;
import com.project.zhurina.Color;
import com.project.zhurina.board_package.Board;
import com.project.zhurina.board_package.Cellule;
import com.project.zhurina.board_package.Deplacer;
import com.project.zhurina.board_package.Deplacer.*;
import com.project.zhurina.board_package.Utilitaires;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Roi extends Piece{//KING
    private final int[] DEPLACEMENT_POTENTIEL = {-9, -8, -7, -1, 1, 7, 8, 9};
    public Roi(final Color color, final int position) {
        super(Type.ROI, color, position, true);
    }
    public Roi(Color color, int position, boolean premierPas){
        super(Type.TOUR,color, position, premierPas);
    }

    @Override
    public Collection<Deplacer> possibleDeplacer(final Board board) {
        //liste avec les deplacementes possibles
        final List<Deplacer> deplacePossible = new ArrayList<>();

        for (final int positionCur : DEPLACEMENT_POTENTIEL){
            int coordonneeDestination = this.position+positionCur;

            if(premierColonneEx(this.position,positionCur)||huitiemeColonneEx(this.position,positionCur)){
                continue;
            }

            if(Utilitaires.coordonneePossible(coordonneeDestination)){
                //celllule sur board
                final Cellule destinationCellule = board.getCellule(coordonneeDestination);

                //si la cellule où on veut deplacer cavalier n'est pas occupée
                if(!destinationCellule.celluleOccupee()){
                    deplacePossible.add(new PieceMajeure(board, this, coordonneeDestination));
                }else{//si elle est occupée
                    //par quelle piece elle est occupée et quelle sa couleur?
                    final Piece pDestination = destinationCellule.getPiece();
                    final Color color = pDestination.getColor();
                    //si les couleurs sonr pas mêmes
                    if(this.color != color){
                        deplacePossible.add(new DeplaceAttaque(board, this, pDestination, coordonneeDestination));
                    }
                }
            }
        }

        return ImmutableList.copyOf(deplacePossible);
    }
    @Override
    public Roi deplacePiece(Deplacer deplacer) {
        return new Roi(deplacer.getPieceDeplacee().getColor(), deplacer.getCoordonnee());
    }
    //pour affichage sur terminal
    @Override
    public String toString(){
        return Type.ROI.toString();
    }
    private static boolean premierColonneEx(final int positionCur, final int coordonnee){
        return Utilitaires.PREMIER_COLONNE[positionCur] && (coordonnee == -9 || coordonnee == -1 || coordonnee == 7);

    }
    private static boolean huitiemeColonneEx(final int positionCur, final int coordonnee){
        return Utilitaires.HUITIEME_COLONNE[positionCur] && (coordonnee == -7 || coordonnee == 1 || coordonnee == 9);

    }
}
