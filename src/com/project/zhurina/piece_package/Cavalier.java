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

public class Cavalier extends Piece{
    //les deplacements possible étainent basés sur l'image de wiki, où cavalier est sur la position D4
    private final int[] DEPLACEMENT_POTENTIEL = {-17, -15, -10, -6, 6, 10, 15, 17};
    public Cavalier(final Color color, final int position) {
        super(Type.CAVALIER,color, position, true);
    }
    public Cavalier(Color color, int position, boolean premierPas){
        super(Type.TOUR,color, position, premierPas);
    }
    @Override
    public Collection<Deplacer> possibleDeplacer(final Board board) {
        //liste avec les deplacementes possibles
        final List<Deplacer> deplacePossible = new ArrayList<>();

        for (final int positionCur : DEPLACEMENT_POTENTIEL){
           final int coordonneeDestination = this.position + positionCur;

            //si c'est coordonne qui on peut utiliser(entre 0 et 64)
            if(Utilitaires.coordonneePossible(coordonneeDestination)){
                //si nous somme les 1er, 2eme, 7eme ou 8eme colonne, on ne peut pas utilliser nos deplacements
                if(premierColonneEx(this.position, positionCur)|| deuxiemeColonneEx(this.position, positionCur) || septiemeColonneEx(this.position, positionCur)  || huitiemeColonneEx(this.position, positionCur) ){
                    continue;
                }

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
    public Cavalier deplacePiece(Deplacer deplacer) {
        return new Cavalier(deplacer.getPieceDeplacee().getColor(), deplacer.getCoordonnee());
    }

    //pour affichage sur terminal
    @Override
    public String toString(){
        return Type.CAVALIER.toString();
    }
    //car on a les positions differentes qui peut na pas fonctionner avec nos deplacements, on doit créer les cases particuliers
    private static boolean premierColonneEx(final int positionCur, final int coordonnee){
       return Utilitaires.PREMIER_COLONNE[positionCur] && (coordonnee == -17 || coordonnee == -10 || coordonnee == 6 || coordonnee == 15);

    }
    private static boolean deuxiemeColonneEx(final int positionCur, final int coordonnee){
       return Utilitaires.DEUXIEME_COLONNE[positionCur] && (coordonnee == -10 || coordonnee == 6);

    }
    private static boolean septiemeColonneEx(final int positionCur, final int coordonnee){
        return Utilitaires.SEPTIEME_COLONNE[positionCur] && (coordonnee == -6 || coordonnee == 10);

    }
    private static boolean huitiemeColonneEx(final int positionCur, final int coordonnee){
        return Utilitaires.HUITIEME_COLONNE[positionCur] && (coordonnee == -15 || coordonnee == -6 || coordonnee == 10 || coordonnee == 17);

    }

}
