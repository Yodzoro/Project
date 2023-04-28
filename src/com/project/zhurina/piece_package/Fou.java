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

public class Fou extends Piece{//BISHOP
    //les deplacements possible étainent basés sur l'image de wiki, où fou est sur la position E3
    //ici on peut deplacer pour chaque -9, -7, 7, 9 cases (deplacement vectiorialle)
    private final int[] DEPLACEMENT_POTENTIEL = {-9, -7, 7, 9};
    public Fou(final Color color, final int position) {
        super(Type.FOU,color,position,true);
    }//

    public Fou(Color color, int position, boolean premierPas){
        super(Type.TOUR,color, position, premierPas);
    }
    @Override
    public Collection<Deplacer> possibleDeplacer(final Board board) {
        //liste avec les deplacementes possibles
        final List<Deplacer> deplacePossible = new ArrayList<>();

        for (final int positionCur : DEPLACEMENT_POTENTIEL) {
            int coordonneeDestination = this.position;

            while (Utilitaires.coordonneePossible(coordonneeDestination)){
                //pour 1er et 8eme colonne on ne peut pas utiliser nos deplacements
                if(premierColonneEx(coordonneeDestination, positionCur)|| huitiemeColonneEx(coordonneeDestination, positionCur)){
                    break;
                }
                coordonneeDestination+=positionCur;
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
    public Fou deplacePiece(Deplacer deplacer) {
        return new Fou(deplacer.getPieceDeplacee().getColor(), deplacer.getCoordonnee());
    }

    //pour affichage sur terminal
    @Override
    public String toString(){
        return Type.FOU.toString();
    }
    //car on a les positions differentes qui peut na pas fonctionner avec nos deplacements, on doit créer les cases particuliers
    private static boolean premierColonneEx(final int positionCur, final int coordonnee){
        return Utilitaires.PREMIER_COLONNE[positionCur] && (coordonnee == -9 || coordonnee == 7);

    }
    private static boolean huitiemeColonneEx(final int positionCur, final int coordonnee){
        return Utilitaires.HUITIEME_COLONNE[positionCur] && (coordonnee == -7 || coordonnee == 9);

    }

}
