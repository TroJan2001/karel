import stanford.karel.SuperKarel;


public class Homework extends SuperKarel {

    /* You fill the code here */
    private int numOfVerticalMoves, numOfHorizontalMoves, numOfMoves, verticalDimension, horizontalDimension;
    private int maxNumOfChambers, numOfLines, padding, hop;
    boolean putBeepers, zigzag, turn;

    private void calculateParameters(int dimension) {
        maxNumOfChambers = maxChambers(dimension);
        numOfLines = maxNumOfChambers - 1;
        padding = (dimension + 1) % maxNumOfChambers;
        hop = (dimension - (padding + numOfLines)) / maxNumOfChambers;
    }

    private void initializeKarel() {
        numOfVerticalMoves = 0;
        numOfHorizontalMoves = 0;
        numOfMoves = 0;
        verticalDimension = 0;
        horizontalDimension = 0;
        padding = 0;
        zigzag = true;
    }

    private void moveOneStep() {
        move();
        numOfMoves++;
    }

    private void turnKarel(boolean turn) {
        if (!turn)
            turnRight();
        else
            turnLeft();
    }

    private int maxChambers(int dimension) {
        if (dimension >= 7)
            return 4;
        return (dimension+1)/2;
    }

    private void currentPathIsDone() {
        turnLeft();
        moveOneStep();
        turnLeft();
    }

    private void otherPathIsDone() {
        turnRight();
        moveOneStep();
        turnRight();
    }

    private void addLines(boolean vertical) {
        turn = !vertical;
        for (int i = 0; i < numOfLines; i++) {
            if (i == 0) {
                if (!vertical)
                    moveHorizontal(hop);
                else
                    moveVertical(hop);
            } else {
                if (!vertical)
                    moveHorizontal(hop + 1);
                else
                    moveVertical(hop + 1);
            }
            turnKarel(turn);
            moveForward(true, false);
            turnKarel(!turn);
            turn = !turn;
        }
    }

    private void addPadding(boolean vertical) {
        turn = !vertical;
        for (int i = 0; i < padding; i++) {
            if (i == 0) {
                if (!vertical)
                    moveHorizontal(hop);
                else
                    moveVertical(hop);
            }
            if (!vertical)
                moveHorizontal(1);
            else
                moveVertical(1);
            turnKarel(turn);
            moveForward(true, false);
            turnKarel(!turn);
            turn = !turn;
        }
    }

    private void checkVerticalDimension() {
        if (frontIsClear()) {
            moveOneStep();
            if (frontIsBlocked())
                verticalDimension = 2;
        } else
            verticalDimension = 1;
    }

    private void moveForward(boolean beepers, boolean vertical) {
        while (frontIsClear()) {
            if (beepers && noBeepersPresent())
                putBeeper();
            move();
            if (!vertical)
                numOfHorizontalMoves++;
            else
                numOfVerticalMoves++;
        }
        if (beepers && noBeepersPresent())
            putBeeper();
    }

    private void moveForward(int moves, boolean beepers, boolean vertical) {
        for (int i = 0; i < moves; i++) {
            if (beepers && noBeepersPresent())
                putBeeper();
            move();
            if (!vertical)
                numOfHorizontalMoves++;
            else
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


    private void pickBeepers(int moves) {
        for (int i = 0; i < moves; i++) {
            if (beepersPresent())
                pickBeeper();
            move();
            numOfVerticalMoves++;
        }
        if (beepersPresent())
            pickBeeper();
    }


    private void divideMap() {
        moveForward(false, false);
        horizontalDimension = numOfHorizontalMoves + 1;
        turnLeft();
        checkVerticalDimension();
        if (verticalDimension == 2 || verticalDimension == 1) {
            if (verticalDimension == 2 && horizontalDimension <= 6 && horizontalDimension != 1) {
                zigzagHorizontal();
            } else {
                divideHorizontal();
            }
        } else if (horizontalDimension <= 2) {
            divideVertical();
        } else {
            turnAround();
            moveOneStep();
            turnRight();
            moveHorizontal(horizontalDimension / 2 - ((horizontalDimension + 1) % 2));
            turnRight();
            putBeepers = true;
            moveForward(true, true);
            verticalDimension = numOfVerticalMoves + 1;
            divideBig();
        }
        numOfMoves = numOfHorizontalMoves + numOfVerticalMoves + numOfMoves;
    }

    private void fan() {
        int hop = 1 + (verticalDimension - 4) / 2;
        moveForward(hop, false,true);
        moveForward(true, true);
        currentPathIsDone();
        pickBeepers(hop-1);
        moveOneStep();
        turnRight();
        moveForward(true,false);
        currentPathIsDone();
        moveForward(hop,false,false);
        moveForward(hop+1,true,false);
    }

    private void divideBig() {
        if (horizontalDimension % 2 == 0) {
            currentPathIsDone();
            if (horizontalDimension == verticalDimension) {
                fan();
                return;
            }
            moveForward(putBeepers, true);
        }
        putBeepers = true;
        turnAround();
        moveVertical(verticalDimension / 2);
        turnLeft();
        moveForward(putBeepers, false);
        if (verticalDimension % 2 == 0) {
            currentPathIsDone();
            moveForward(putBeepers, false);
            currentPathIsDone();
            moveForward(horizontalDimension / 2 - 1, putBeepers,false);
        } else {
            turnAround();
            moveForward(putBeepers, false);
        }
    }

    private void divideVertical() {
        moveForward(false, true);
        verticalDimension = numOfVerticalMoves + 2;
        if (verticalDimension <= 6 && horizontalDimension == 2) {
            zigzagVertical();
            return;
        }
        calculateParameters(verticalDimension);
        turnAround();
        addLines(true);
        addPadding(false);
    }

    private void divideHorizontal() {
        calculateParameters(horizontalDimension);
        turnLeft();
        addLines(false);
        addPadding(true);
    }

    private void zigzagInit(int dimension) {
        padding = 0;
        if (dimension == 2)
            maxNumOfChambers = 2;
        else if (dimension == 3)
            maxNumOfChambers = 3;
        else {
            maxNumOfChambers = 4;
            padding = (dimension % maxNumOfChambers) * 2;
        }

    }

    private void zigzagHorizontal() {
        zigzagInit(horizontalDimension);
        for (int i = 0; i < maxNumOfChambers; i++) {
            putBeeper();
            if (i != maxNumOfChambers - 1) {
                zigzagHorizontalTurn();
                moveOneStep();
            }
            zigzag = !zigzag;
        }
        zigzag = !zigzag;
        if (padding > 0) {
            for (int i = 0; i < padding / 2; i++) {
                zigzagHorizontalTurn();
                zigzag = !zigzag;
                moveForward(true, true);
            }
        }
    }

    private void zigzagVertical() {
        zigzagInit(verticalDimension);
        turnAround();
        for (int i = 0; i < maxNumOfChambers; i++) {
            putBeeper();
            if (i != maxNumOfChambers - 1) {
                zigzagVerticalTurn(false);
            }
        }
        if (padding > 0) {
            for (int i = 0; i < padding / 2; i++) {
                zigzagVerticalTurn(true);
            }
        }
    }

    private void zigzagHorizontalTurn() {
        if (zigzag)
            currentPathIsDone();
        else
            otherPathIsDone();
    }

    private void zigzagVerticalTurn(boolean isPadding) {
        moveOneStep();
        turnKarel(!zigzag);
        moveForward(isPadding, false);
        turnKarel(zigzag);
        zigzag = !zigzag;
    }

    private void returnToOrigin() {
        if (facingEast())
            turnAround();
        else if (facingNorth())
            turnLeft();
        else if (facingSouth())
            turnRight();
        while (frontIsClear()) {
            moveOneStep();
        }
        turnLeft();
        while (frontIsClear()) {
            moveOneStep();
        }
        turnLeft();
    }

    public void run() {
        setBeepersInBag(1000);
        initializeKarel();
        divideMap();
        returnToOrigin();
        System.out.println(numOfMoves);
    }
}
