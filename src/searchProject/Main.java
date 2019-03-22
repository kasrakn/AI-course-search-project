package searchProject;

import java.util.*;

public class Main {

    static int row_count, col_count;
    static String[][] input_elements, output_elements;
    static State goal_state;
    static int depth;
    // data structure objects
    static Queue<State> fringe = new LinkedList<State>();
    static Set<State> visited = new HashSet<State>();


    public static void main(String[] args) {
        /**
         *  - get board sizes and then get start & goal board
         *  - create initial & goal state objects and
         *  - add initial_state object to fringe queue
         *  - call bfs method
         */
        Scanner scanner = new Scanner(System.in);
        String size = scanner.nextLine();
        String[] temp1;
        temp1 = size.split(" ");

        row_count = Integer.valueOf(temp1[0]);
        col_count = Integer.valueOf(temp1[1]);
        input_elements = new String[row_count][col_count];
        output_elements = new String[row_count][col_count];

        // input board
        for (int i = 0; i < row_count; i++) {
            String temp = scanner.nextLine();
            input_elements[i] = temp.split(" ");
        }
        // output board
        for (int i = 0; i < row_count; i++) {
            String temp = scanner.nextLine();
            output_elements[i] = temp.split(" ");
        }

        // Create goal state object
        goal_state = new State(output_elements, depth);
        // Create initial state object
        State initial_state = new State(input_elements, 0);
        // add initial state to fringe queue
        fringe.add(initial_state);
        bfs();

    }

    private static void bfs() {
        /**
         * start search in tree with BFS algorithm which starts at head of fringe queue
         */

        boolean continuee = true;
        while (!fringe.isEmpty() && continuee) {
            State current = fringe.poll();
            depth = current.depth;
            System.out.println("\n polled from fringe : ");
            print_state(current);
            if (goal_test(current)) {
                System.out.println("GOoOoOl");
                System.out.println("moves = " + current.depth);
                continuee = false;

            } else { // if current state is not goal
                if (visited_not_contains(current)) {
                    visited.add(current);
                    System.out.println("visited size = " + visited.size());
                    expand(current);
                }
            }

        }

    }

    private static void expand(State current) {
        /**
         * It is looking for 'Knights' and call successor to get each children.
         */
        ArrayList<State> sucList;
        for (int i = 0; i < row_count; i++) {
            for (int j = 0; j < col_count; j++) {
                if (current.board[i][j].equals("#")) {
                    System.out.println(" , successor row = "+ i + ", column = " + j);
                    sucList = successor(current, i, j);
                    fringe.addAll(sucList);
                }
            }
        }
    }

    static private ArrayList<State> successor(State current, int knight_row, int knight_col) {
        /**
         * call born_child_object after checking the state
         */
        ArrayList<State> sucList = new ArrayList<State>();

        // 1
        if (check_states(current, knight_row, knight_col, 2, 1)) {
            born_child_object(current,sucList, knight_row, knight_col, 2, 1);
        }

        // 2
        if (check_states(current, knight_row, knight_col, 2, -1)) {
            born_child_object(current,sucList, knight_row, knight_col, 2, -1);
        }

        // 3
        if (check_states(current, knight_row, knight_col, -2, 1)) {
            born_child_object(current,sucList, knight_row, knight_col, -2, 1);
        }

        // 4
        if (check_states(current, knight_row, knight_col, -2, -1)) {
            born_child_object(current,sucList, knight_row, knight_col, -2, -1);
        }

        // 5
        if (check_states(current, knight_row, knight_col, 1, 2)) {
            born_child_object(current,sucList, knight_row, knight_col, 1, 2);
        }

        // 6
        if (check_states(current, knight_row, knight_col, 1, -2)) {
            born_child_object(current,sucList, knight_row, knight_col, 1, -2);
        }

        // 7
        if (check_states(current, knight_row, knight_col, -1, 2)) {
            born_child_object(current,sucList, knight_row, knight_col, -1, 2);
        }

        // 8
        if (check_states(current, knight_row, knight_col, -1, -2)) {
            born_child_object(current,sucList, knight_row, knight_col, -1, -2);
        }
        return sucList;
    }

    private static void born_child_object(State current, ArrayList<State> list, int knight_row, int knight_col, int i, int i2) {
        /**
         * create the new state object and then after checking the visited list for not to contain the
         * state add it to fringe list
         * @param:
         *      current : to set the newBoard equal to it
         *      knight_row : current knight row
         *      knight_col : current knight column
         *      i : for checking one of the around square row
         *      i2 : for checking one of the around square column
         */
        String[][] newBoard = new String[row_count][col_count];
        set_two_arrays_equals(current.board, newBoard);
        swap_values(newBoard, knight_row, knight_col, i, i2);  // move knight to destination square on board
        State newState = new State(newBoard, current.depth + 1);
        if (visited_not_contains(newState)) {
            list.add(newState);
        }
    }

    private static boolean check_states(State parent, int knight_row, int knight_col, int add_row, int add_col) {
        /**
         * This method checks for boundary limitations and calls isDanger method to check if next step is a dangerous state or not
         * output:
         *      if there is no error --> true
         *      else --> false
         */
        int wantToMove_row = knight_row + add_row;
        int wantToMove_col = knight_col + add_col;
        if (
                wantToMove_row < row_count
                && wantToMove_row >= 0
                && wantToMove_col < col_count
                && wantToMove_col >= 0) {
            return !isDanger(parent.board, knight_row, knight_col, wantToMove_row, wantToMove_col);
        }else {
            return false;
        }

    }

    private static boolean isDanger(String[][] board, int knight_row, int knight_col, int wantToMove_row, int wantToMove_col) {
        /**
         * Check all possible movements and find states that going through a danger state
         * output:
         *      dangerous -> true
         *      not dangerous -> false
         */

        // 1
        if (check_isNot_current(knight_row, knight_col, wantToMove_row, wantToMove_col, 1, 2)) {
            if (check_for_boundary(wantToMove_row, wantToMove_col, 1, 2)) {
                if (board[wantToMove_row + 1][wantToMove_col + 2].equals("#")) return true;
            }
        }

        // 2
        if (check_isNot_current(knight_row, knight_col, wantToMove_row, wantToMove_col, 1, -2)) {
            if (check_for_boundary(wantToMove_row, wantToMove_col, 1, -2)) {
                if (board[wantToMove_row + 1][wantToMove_col - 2].equals("#")) return true;
            }
        }

        // 3
        if (check_isNot_current(knight_row, knight_col, wantToMove_row, wantToMove_col, -1, 2)) {
            if (check_for_boundary(wantToMove_row, wantToMove_col, -1, +2)) {
                if (board[wantToMove_row - 1][wantToMove_col + 2].equals("#")) return true;
            }
        }

        // 4
        if (check_isNot_current(knight_row, knight_col, wantToMove_row, wantToMove_col, -1, -2)) {
            if (check_for_boundary(wantToMove_row, wantToMove_col, -1, -2)) {
                if (board[wantToMove_row - 1][wantToMove_col - 2].equals("#")) return true;
            }
        }

        // 5
        if (check_isNot_current(knight_row, knight_col, wantToMove_row, wantToMove_col, 2, 1)) {
            if (check_for_boundary(wantToMove_row, wantToMove_col, 2, 1)) {
                if (board[wantToMove_row + 2][wantToMove_col + 1].equals("#")) return true;
            }
        }

        // 6
        if (check_isNot_current(knight_row, knight_col, wantToMove_row, wantToMove_col, 2, -1)) {
            if (check_for_boundary(wantToMove_row, wantToMove_col, 2, -1)) {
                if (board[wantToMove_row + 2][wantToMove_col - 1].equals("#")) return true;
            }
        }

        // 7
        if (check_isNot_current(knight_row, knight_col, wantToMove_row, wantToMove_col, -2, 1)) {
            if (check_for_boundary(wantToMove_row, wantToMove_col, -2, 1)) {
                if (board[wantToMove_row - 2][wantToMove_col + 1].equals("#")) return true;
            }
        }
        // 8
        if (check_isNot_current(knight_row, knight_col, wantToMove_row, wantToMove_col, -2, -1)) {
            if (check_for_boundary(wantToMove_row, wantToMove_col, -2, -1)) {
                if (board[wantToMove_row - 2][wantToMove_col - 1].equals("#")) return true;
            }
        }
        return false;
    }

    private static boolean check_isNot_current(int knight_row, int knight_col, int check_row, int check_col, int row_move, int col_move) {
        /**
         * This method is for checking knight location with movement location
         */
        return check_row + row_move != knight_row || check_col + col_move != knight_col;
    }

    private static void print_state(State state) {
        /**
         * print state board
         */
        System.out.println("\n");
        for (int i = 0; i < row_count; i++) {
            for (int j = 0; j < col_count; j++) {
                System.out.print(state.board[i][j] + "  ");

            }
            System.out.println();
        }
        System.out.println("\n");
    }

    private static boolean goal_test(State state) {
        /**
         * This method checks if the passed state is goal or not
         * output:
         *      if is goal --> true
         *      else --> false
         */
        return state.equals(goal_state);
    }

    private static void swap_values(String[][] board, int knight_row, int knight_col, int add_row, int add_col) {
        /**
         * move a knight from one square to other square
         */
        board[knight_row][knight_col] = ".";
        board[knight_row + add_row][knight_col + add_col] = "#";
    }

    private static boolean check_for_boundary(int wantToMove_row, int wantToMove_col, int checkDanger_row, int checkDanger_col) {
        /**
         * This method checks for not to get into array out of bound exeption
         */
        return wantToMove_row + checkDanger_row < row_count
                && wantToMove_row + checkDanger_row >= 0
                && wantToMove_col + checkDanger_col < row_count
                && wantToMove_col + checkDanger_col >= 0;
    }

    private static void set_two_arrays_equals(String[][] host, String[][] guest) {
        /**
         * set guest 2D array equal to host 2D array
         */
        for (int i = 0; i < row_count; i++) {
            for (int j = 0; j < col_count; j++) {
                guest[i][j] = host[i][j];
            }
        }
    }

    private static boolean visited_not_contains(State current) {
        for (State aVisited : visited) {
            if (current.equals(aVisited)) {
                return false;
            }
        }
        return true;
    }
}