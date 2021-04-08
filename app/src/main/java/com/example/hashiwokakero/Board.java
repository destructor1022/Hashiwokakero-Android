package com.example.hashiwokakero;

import java.util.List;

public class Board {
    private List<List<Piece>> board;

    public Board(List<List<Piece>> javaGame) {
        this.board = javaGame;
    }

    public List<List<Piece>> getBoard() {
        return board;
    }
}
