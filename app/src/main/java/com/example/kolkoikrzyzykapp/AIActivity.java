package com.example.kolkoikrzyzykapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AIActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[][] buttons = new Button[3][3];
    private boolean playerTurn = true;
    private int roundCount = 0;
    private TextView statusText;
    boolean finished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);

        statusText = findViewById(R.id.status_text);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }

        Button resetButton = findViewById(R.id.button_reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBoard();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;

        if (button.getText().toString().equals("") && finished == false) {

                button.setText("X");

            roundCount++;
            if (checkForWin()) {
                if (playerTurn) {
                    statusText.setText("Gracz z X wygrywa!");
                    finished = true;

                } else {
                    statusText.setText("Gracz z 0 wygrywa!");
                    finished = true;

                }
            } else if (roundCount == 9) {
                statusText.setText("REMIS");
                finished = true;

            } else {
                playerTurn = !playerTurn;
                statusText.setText
                        ("KOLEJ GRACZA " + (playerTurn ? "X" : "O") + " ");
            }
        }else if(finished==true){
            statusText.setText("ZRESETUJ GRĘ, ABY GRAĆ DALEJ");

        }
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


    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        playerTurn = true;
        roundCount = 0;
        finished = false;
        statusText.setText("KOLEJ GRACZA Z X");
    }
}