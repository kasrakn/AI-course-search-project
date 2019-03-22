package searchProject;

public class State {
    int row_count = 5, col_count = 5;
    String[][] board;
    State parent;
    int depth;
    int cost = 0;

    State(String[][] board, int depth) {
        this.board = board;
        this.depth = depth;
    }

    boolean equals(State other_state) {
        /**
         * check if passed State is equal to this State
         */
        for (int i = 0; i < row_count; i++) {
            for (int j = 0; j < col_count; j++) {
                if (!this.board[i][j].equals(other_state.board[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }
}
