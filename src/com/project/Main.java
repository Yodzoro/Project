package com.project;

import com.project.gui.BoardSwing;
import com.project.zhurina.board_package.Board;

public class Main {
    public static void main(String[] args) {
        Board board = Board.boardDefaut();
        System.out.println(board);

        BoardSwing boardSwing = new BoardSwing();
    }
}