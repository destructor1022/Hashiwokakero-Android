package com.example.hashiwokakero;

public class Piece {
    String struct;
    int val;

    public Piece(String struct, int val) {
        this.struct = struct;
        this.val = val;
    }

    public String toString() {
        return struct + Integer.toString(val);
    }
}
