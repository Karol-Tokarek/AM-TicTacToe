package com.example.kolkoikrzyzykapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AIActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[][] buttons = new Button[3][3];
    private boolean playerTurn = true; // Gracz X zaczyna
    private int roundCount = 0;
    private boolean gameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aiactivity);
        Button resetButton = findViewById(R.id.button_reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBoard();
            }
        });
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (gameOver) {
            return;
        }
        Button button = (Button) v;
        if (button.getText().toString().equals("")) {
            if (playerTurn) {
                button.setText("X");
            }
            roundCount++;
            if (checkForWin()) {
                gameOver = true;
                updateStatus("GRACZ X wygrywa");
            } else if (roundCount == 9) {
                gameOver = true;
                updateStatus("REMIS !");
            } else {
                playerTurn = !playerTurn;
                if (!playerTurn) {
                    makeAIMove();
                }
            }
        }
    }
    private void makeAIMove() {
        int[] bestMove = minimax();
        int bestMoveX = bestMove[0];
        int bestMoveY = bestMove[1];

        buttons[bestMoveX][bestMoveY].setText("O");
        roundCount++;
        if (checkForWin()) {
            gameOver = true;
            updateStatus("GRACZ Z O WYGRYWA");
        } else if (roundCount == 9) {
            gameOver = true;
            updateStatus("REMIS");
        } else {
            playerTurn = true;
        }
    }

    private int[] minimax() {
        int[] bestMove = null;
        int bestScore = Integer.MIN_VALUE;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().toString().equals("")) {
                    buttons[i][j].setText("O");
                    int score = minimax(false);
                    buttons[i][j].setText("");
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new int[]{i, j};
                    }
                }
            }
        }

        return bestMove;
    }

    private int minimax(boolean isMaximizing) {
        int result = evaluate();

        if (result != 0) {
            return result;
        }

        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().toString().equals("")) {
                    buttons[i][j].setText(isMaximizing ? "O" : "X");
                    int score = minimax(!isMaximizing);
                    buttons[i][j].setText("");
                    bestScore = isMaximizing ? Math.max(score, bestScore) : Math.min(score, bestScore);
                }
            }
        }

        return bestScore;
    }

    private int evaluate() {
        // Sprawdzamy, czy gra została wygrana, przegrała lub jest remis
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().toString().equals(buttons[i][1].getText().toString()) &&
                    buttons[i][0].getText().toString().equals(buttons[i][2].getText().toString()) &&
                    !buttons[i][0].getText().toString().equals("")) {
                if (buttons[i][0].getText().toString().equals("X")) {
                    return -1;
                } else {
                    return 1;
                }
            }

            if (buttons[0][i].getText().toString().equals(buttons[1][i].getText().toString()) &&
                    buttons[0][i].getText().toString().equals(buttons[2][i].getText().toString()) &&
                    !buttons[0][i].getText().toString().equals("")) {
                if (buttons[0][i].getText().toString().equals("X")) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }

        if (buttons[0][0].getText().toString().equals(buttons[1][1].getText().toString()) &&
                buttons[0][0].getText().toString().equals(buttons[2][2].getText().toString()) &&
                !buttons[0][0].getText().toString().equals("")) {
            if (buttons[0][0].getText().toString().equals("X")) {
                return -1;
            } else {
                return 1;
            }
        }

        if (buttons[0][2].getText().toString().equals(buttons[1][1].getText().toString()) &&
                buttons[0][2].getText().toString().equals(buttons[2][0].getText().toString()) &&
                !buttons[0][2].getText().toString().equals("")) {
            if (buttons[0][2].getText().toString().equals("X")) {
                return -1;
            } else {
                return 1;
            }
        }

        return 0;
    }

    private boolean checkForWin() {
        String[][] board = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = buttons[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++) {
            if (board[i][0].equals(board[i][1]) && board[i][0].equals(board[i][2]) && !board[i][0].equals("")) {
                return true;
            }
            if (board[0][i].equals(board[1][i]) && board[0][i].equals(board[2][i]) && !board[0][i].equals("")) {
                return true;
            }
        }

        if (board[0][0].equals(board[1][1]) && board[0][0].equals(board[2][2]) && !board[0][0].equals("")) {
            return true;
        }
        if (board[0][2].equals(board[1][1]) && board[0][2].equals(board[2][0]) && !board[0][2].equals("")) {
            return true;
        }

        return false;
    }

    private void updateStatus(String message) {
        TextView statusText = findViewById(R.id.status_text);
        statusText.setText(message);
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }

        playerTurn = true;
        roundCount = 0;
        gameOver = false;
        updateStatus("KOLEJ GRACZA Z X");
    }
}