package com.project.zhurina.piece_package;

import com.google.common.collect.ImmutableList;
import com.project.zhurina.Color;
import com.project.zhurina.board_package.Board;
import com.project.zhurina.board_package.Cellule;
import com.project.zhurina.board_package.Deplacer;
import com.project.zhurina.board_package.Deplacer.PieceMajeure;
import com.project.zhurina.board_package.Deplacer.DeplaceAttaque;
import com.project.zhurina.board_package.Utilitaires;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Dame extends Piece{//queen
    //Dame peut alle comme Tour et comme Fou, donc pour les deplacement on a l'union de deplacement de Tour et Fou
    private final int[] DEPLACEMENT_POTENTIEL = {-9, -8, -7, -1, 1, 7, 8, 9};
    public Dame(Color color, int position) {
        super(Type.DAME, color,position, true);
    }
    public Dame(Color color, int position, boolean premierPas){
        super(Type.DAME,color, position, premierPas);
    }
    @Override
    public Collection<Deplacer> possibleDeplacer(final Board board) {
        //liste avec les deplacementes possibles
        final List<Deplacer> deplacePossible = new ArrayList<>();

        for (final int positionCur : DEPLACEMENT_POTENTIEL) {
            int coordonneeDestination = this.position;

            while (Utilitaires.coordonneePossible(coordonneeDestination)){
                //pour 1er et 8eme colonne on ne peut pas utiliser nos deplacements
                if(premierColonneEx(coordonneeDestination, positionCur)||huitiemeColonneEx(coordonneeDestination, positionCur)){
                    continue;
                }
                coordonneeDestination += positionCur;

                if(Utilitaires.coordonneePossible(coordonneeDestination)){
                    final Cellule destinationCellule = board.getCellule(coordonneeDestination);

                    //si la cellule où on veut deplacer fou n'est pas occupée
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
                    break;
                }

            }
        }

        return ImmutableList.copyOf(deplacePossible);
    }
    @Override
    public Dame deplacePiece(Deplacer deplacer) {
        return new Dame(deplacer.getPieceDeplacee().getColor(), deplacer.getCoordonnee());
    }
    //pour affichage sur terminal
    @Override
    public String toString(){
        return Type.DAME.toString();
    }
    //car on a les positions differentes qui peut na pas fonctionner avec nos deplacements, on doit créer les cases particuliers
    private static boolean premierColonneEx(final int positionCur, final int coordonnee){
        return Utilitaires.PREMIER_COLONNE[positionCur] && (coordonnee == -9 || coordonnee == -1 || coordonnee == 7);

    }
    private static boolean huitiemeColonneEx(final int positionCur, final int coordonnee){
        return Utilitaires.HUITIEME_COLONNE[positionCur] && (coordonnee == -7 || coordonnee == 1 || coordonnee == 9);

    }
}
