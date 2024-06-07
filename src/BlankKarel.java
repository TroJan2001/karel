/*
 * File: BlankKarel.java
 * ---------------------
 * This class is a blank one that you can change at will.
 */

import stanford.karel.*;

public class BlankKarel extends SuperKarel {
    private int numOfVerticalMoves = 0, numOfHorizontalMoves = 0, numOfMoves = 0;
    Boolean putBeepers;

    private int maxChambers(int dimension) {
        if(dimension>=7)
            return 4;
        return (dimension+1)/2;
    }

    private void turnKarel(boolean turn) {
        if (!turn)
            turnRight();
        else
            turnLeft();
    }

    private void currentPathIsDone() {
        turnLeft();
        move();
        turnLeft();
        numOfMoves++;
    }

    private void moveHorizontal(Boolean beepers) {
        while (frontIsClear()) {
            if (beepers && noBeepersPresent())
                putBeeper();
            move();
            numOfHorizontalMoves++;
        }
        if (beepers && noBeepersPresent())
            putBeeper();
    }

    private void moveVertical(Boolean beepers) {
        while (frontIsClear()) {
            if (beepers && noBeepersPresent())
                putBeeper();
            move();
            numOfVerticalMoves++;
        }
        if (beepers && noBeepersPresent())
            putBeeper();
    }

    private void moveVertical(int moves) {
        for (int i = 0; i < moves; i++) {
            move();
            numOfVerticalMoves++;
        }
    }

    private void moveHorizontal(int moves) {
        for (int i = 0; i < moves; i++) {
            move();
            numOfHorizontalMoves++;
        }
    }

    private void moveHorizontal(int moves, Boolean beepers) {
        for (int i = 0; i < moves; i++) {
            if (beepers && noBeepersPresent())
                putBeeper();
            move();
            numOfHorizontalMoves++;
        }
        if (beepers && noBeepersPresent())
            putBeeper();
    }

    private void divideMap() {
        int verticalDimension=1000;
        int horizontalDimension;
        putBeepers = false;
        moveHorizontal(putBeepers);
        horizontalDimension = numOfHorizontalMoves + 1;
        turnLeft();
        if(frontIsClear()){
            move();
            if(frontIsBlocked())
                verticalDimension=2;
        }
        else
            verticalDimension=1;
        if(verticalDimension<=2)
            divideVertical(horizontalDimension,verticalDimension);
        else {
            turnAround();
            move();
            turnRight();
            moveHorizontal(horizontalDimension / 2 - ((horizontalDimension + 1) % 2));
            turnRight();
            if (horizontalDimension > 2)
                putBeepers = true;
            moveVertical(putBeepers);
            verticalDimension = numOfVerticalMoves + 1;
            System.out.println(horizontalDimension);
            System.out.println(verticalDimension);
            if (verticalDimension > 2 && horizontalDimension > 2)
                divideBig(horizontalDimension, verticalDimension);
            else if (horizontalDimension <= 2)
                divideVertical(horizontalDimension,verticalDimension);
            else {
                System.out.println("To do");
            }
        }
        numOfMoves = numOfHorizontalMoves + numOfVerticalMoves;
        System.out.println(numOfMoves);
    }

    private void divideBig(int horizontalDimension, int verticalDimension) {
        System.out.println(horizontalDimension);
        System.out.println(verticalDimension);
        if (horizontalDimension % 2 == 0) {
            currentPathIsDone();
            moveVertical(putBeepers);
        }
        putBeepers = true;
        turnAround();
        moveVertical(verticalDimension / 2);
        turnLeft();
        moveHorizontal(putBeepers);
        if (verticalDimension % 2 == 0) {
            currentPathIsDone();
            moveHorizontal(putBeepers);
            currentPathIsDone();
            moveHorizontal(horizontalDimension / 2 - 2, putBeepers);
        } else {
            turnAround();
            moveHorizontal(putBeepers);
        }
    }

    private void divideVertical(int horizontalDimension, int verticalDimension) {

        int maxNumOfChambers = maxChambers(verticalDimension);
        int numOfLines = maxNumOfChambers - 1;
        int padding = (verticalDimension+1) % maxNumOfChambers;
        int hop = (verticalDimension-(padding + numOfLines))/maxNumOfChambers;
        System.out.println("maxNumOfChambers = " + maxNumOfChambers);
        System.out.println("Number of needed lines = " + numOfLines);
        System.out.println("padding = " + padding);
        turnAround();
        boolean turn = false;
        for (int i = 0; i < numOfLines; i++) {
            if (i == 0)
                moveVertical(hop);
            else
                moveVertical(hop + 1);
            turnKarel(turn);
            moveHorizontal(true);
            turnKarel(!turn);
            turn = !turn;
        }
        for (int i = 0; i < padding; i++) {
            if (i==0)
                moveVertical(hop);
            moveVertical(1);
            turnKarel(turn);
            moveHorizontal(true);
            turnKarel(!turn);
            turn = !turn;
        }

    }

    public void run() {
        divideMap();
    }
}


