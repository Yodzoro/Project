package com.project.gui;

import com.project.zhurina.board_package.Board;
import com.project.zhurina.board_package.Cellule;
import com.project.zhurina.board_package.Deplacer;
import com.project.zhurina.board_package.Deplacer.*;
import com.project.zhurina.board_package.Utilitaires;
import com.project.zhurina.joueur_package.DeplaceTrans;
import com.project.zhurina.piece_package.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class BoardSwing {//Table
    private Board jeudechecBoard;
    private Cellule sourceCell;
    private Cellule coorCell;
    private Piece pieceJoueur;
    private final DeplaceEnvelop deplaceEnvelop;


    //private static final BoardSwing EXEMPLE = new BoardSwing();

    private static final Dimension DIM_EXTER = new Dimension(800, 800);
    private static final Dimension DIM_BOARD = new Dimension(400, 350);
    private static final Dimension DIM_CELLULE = new Dimension(10,10);

    private final Color blanchBack = Color.decode("#E282A0");
    private final Color noirBack = Color.decode("#BD305C");
    private final Color cheminBack = Color.decode("#FAC1D3");

    public BoardSwing(){
        //fenetre de jeu avec menu
        //если прислугаться к проге, то ничего не поменяется
        JFrame jeuFrame = new JFrame("Jeu d\'échecs");
        final JMenuBar menu = posMenuBar();
        jeuFrame.setJMenuBar(menu);
        jeuFrame.setSize(DIM_EXTER);

        //board de jeu
        this.jeudechecBoard = Board.boardDefaut();
        this.deplaceEnvelop = new DeplaceEnvelop();
        jeuFrame.setLayout(new BorderLayout());
        BoardPanel boardPanel = new BoardPanel();
        jeuFrame.add(boardPanel, BorderLayout.CENTER);
        boolean deplaceCheminPos = false;

        jeuFrame.setVisible(true);
    }
    /*private static BoardSwing get() {
        return EXEMPLE;
    }
    private boolean getDeplaceCheminPos(){
        return this.deplaceCheminPos;
    }
    */

    private JMenuBar posMenuBar() {
        final JMenuBar menu = new JMenuBar();
        menu.add(creerFichierMenu());
        return menu;
    }

    private JMenu creerFichierMenu() {
        final JMenu fmenu = new JMenu("menu");
        final JMenuItem fermerMenu = new JMenuItem("Fermer jeu");
        fermerMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fmenu.add(fermerMenu);
        return fmenu;
    }
    /////////////////////////////////////////////////////////////////////////
    // Board panel
    ////////////////////////////////////////////////////////////////////////
    private class BoardPanel extends JPanel{
        final List<CellulePanel> boardCellules;
        public BoardPanel(){
            //board 8x8
            super(new GridLayout(8,8));
            //on va mettre des cellules
            this.boardCellules= new ArrayList<>();
            for (int i = 0; i< Utilitaires.NUM_CELLULES; i++){
                final CellulePanel cellulePanel = new CellulePanel(this, i);
                this.boardCellules.add(cellulePanel);
                add(cellulePanel);
            }
            //board vas pas prendre tout la surface de fenetre, donc on indique sa taille
            setPreferredSize(DIM_BOARD);
            validate();
        }

        public void dessinerBoard(final Board board) {
            removeAll();
            for(final CellulePanel cellulePanel : boardCellules){
                cellulePanel.dessinerCellule(board);
                add(cellulePanel);
            }
            validate();
            repaint();
        }
    }
    /////////////////////////////////////////////////////////////////////////
    // Envelopper tous les deplacementes
    ////////////////////////////////////////////////////////////////////////
    public static class DeplaceEnvelop{
        private final List<Deplacer> deplacers;
        public DeplaceEnvelop(){
            this.deplacers = new ArrayList<>();
        }
        public List<Deplacer> getDeplacers(){
            return this.deplacers;
        }
        public void ajdDeplace(final Deplacer deplacer){
            this.deplacers.add(deplacer);
        }
        public int taille(){
            return this.deplacers.size();
        }
        public void sup(){
            this.deplacers.clear();
        }
        public Deplacer supDeplace(int id){
            return this.deplacers.remove(id);
        }
        public boolean supDeplace(Deplacer deplacer){
            return this.deplacers.remove(deplacer);
        }
    }
    /////////////////////////////////////////////////////////////////////////
    // Cellule panel
    ////////////////////////////////////////////////////////////////////////
    private class CellulePanel extends JPanel{

        private final int idCell;
        public CellulePanel(final BoardPanel boardPanel, final int idCell){
            super(new GridLayout());
            this.idCell=idCell;
            setPreferredSize(DIM_CELLULE);
            donerColor();
            celluleImage(jeudechecBoard);
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    //annuller les pieces selectionées
                    if(isRightMouseButton(e)){
                        sourceCell = null;
                        coorCell = null;
                        pieceJoueur = null;

                    }else if(isLeftMouseButton(e)){
                        //premier click
                        if(sourceCell==null){
                            sourceCell = jeudechecBoard.getCellule(idCell);
                            pieceJoueur = sourceCell.getPiece();
                            if(pieceJoueur ==null){
                                sourceCell = null;
                            }
                        }else{
                            //deuxieme click
                            coorCell = jeudechecBoard.getCellule(idCell);
                            final Deplacer deplacer = DeplaceUsine.creerDeplace(jeudechecBoard, sourceCell.getCelluleCoor(), coorCell.getCelluleCoor());
                            final DeplaceTrans deplaceTrans = jeudechecBoard.joueurCur().faireDeplace(deplacer);
                            if(deplaceTrans.getDeplaceEtat().estFait()){
                                jeudechecBoard = deplaceTrans.getTransboard();
                                deplaceEnvelop.ajdDeplace(deplacer);
                            }
                            sourceCell = null;
                            coorCell = null;
                            pieceJoueur = null;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                boardPanel.dessinerBoard(jeudechecBoard);
                            }
                        });
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            validate();
        }


        public void dessinerCellule(Board board){
            donerColor();
            celluleImage(board);
            montrerChemin(board);
            validate();
            repaint();
        }

        private void celluleImage(final Board board){
            this.removeAll();
            if(board.getCellule(this.idCell).celluleOccupee()){
                try{
                    //pur charger image et bien le posser on doit dire où sont les images et
                    // pour chaque image on doit utiliser 1 lettre comme la couler et 2 letter comme le type de piece,
                    // à la fin on dit le formate d'image
                    String imagePack = "piecesImages/chess/";
                    final BufferedImage image = ImageIO.read(new File(imagePack + board.getCellule(this.idCell).getPiece().getColor().toString().charAt(0)+board.getCellule(this.idCell).getPiece().toString()+".png"));
                    add(new JLabel(new ImageIcon(image)));
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        private void montrerChemin(Board board){
            if(true){
                for(Deplacer deplacer: deplacePossible(board)){
                    if(deplacer.getCoordonnee()==this.idCell){
                        setBackground(cheminBack);
                    }
                }
            }
        }
        private Collection<Deplacer> deplacePossible(Board board){
            if(pieceJoueur != null && pieceJoueur.getColor() == board.joueurCur().getColor()){
                return pieceJoueur.possibleDeplacer(board);
            }
            return Collections.emptyList();
        }

        private void donerColor() {
            if(Utilitaires.PREMIER_LIGNE[this.idCell] ||Utilitaires.TROISIEME_LIGNE[this.idCell] ||Utilitaires.CINQUIEME_LIGNE[this.idCell] ||Utilitaires.SEPTIEME_LIGNE[this.idCell]){

                setBackground(this.idCell%2==0? blanchBack : noirBack);
            }else if(Utilitaires.DEUXIEME_LIGNE[this.idCell] ||Utilitaires.QUATRIEME_LIGNE[this.idCell] ||Utilitaires.SIXIEME_LIGNE[this.idCell] ||Utilitaires.HUITIEME_LIGNE[this.idCell]){
                setBackground(this.idCell%2!=0? blanchBack : noirBack);
            }
        }
    }
}
