import stanford.karel.SuperKarel;


public class Homework extends SuperKarel {

    /* You fill the code here */
    private int numOfVerticalMoves, numOfHorizontalMoves, numOfMoves, verticalDimension, horizontalDimension;
    private int maxNumOfChambers, numOfLines, padding, hop;
    boolean putBeepers, zigzag, turn;

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

    private void moveOneStepAndTurnRight() {
        moveOneStep();
        turnRight();
    }

    private void moveForward(int moves, boolean beepers, boolean vertical) {
        for (int i = 0; i < moves; i++) {
            if (beepers && noBeepersPresent())
                putBeeper();
            if (frontIsClear()) {
                move();
                if (!vertical)
                    numOfHorizontalMoves++;
                else
                    numOfVerticalMoves++;
            } else
                break;
        }
        if (beepers && noBeepersPresent())
            putBeeper();
    }

    private void calculateParameters(int dimension) {
        maxNumOfChambers = maxChambers(dimension);
        numOfLines = maxNumOfChambers - 1;
        padding = (dimension + 1) % maxNumOfChambers;
        hop = (dimension - (padding + numOfLines)) / maxNumOfChambers;
    }

    private void checkVerticalDimension() {
        if (frontIsClear()) {
            moveOneStep();
            if (frontIsBlocked())
                verticalDimension = 2;
        } else
            verticalDimension = 1;
    }

    private int maxChambers(int dimension) {
        if (dimension >= 7)
            return 4;
        return (dimension + 1) / 2;
    }

    private void turnKarel(boolean turn) {
        if (!turn)
            turnRight();
        else
            turnLeft();
    }

    private void currentPathIsDone() {
        turnLeft();
        moveOneStep();
        turnLeft();
    }

    private void otherPathIsDone() {
        turnRight();
        moveOneStepAndTurnRight();
    }

    private void turnAndPutBeepers() {
        turnKarel(turn);
        moveForward(Integer.MAX_VALUE, true, false);
        turnKarel(!turn);
        turn = !turn;
    }

    private void addLines(boolean vertical) {
        turn = !vertical;
        for (int i = 0; i < numOfLines; i++) {
            if (i == 0) {
                moveForward(hop, false, vertical);
            } else {
                moveForward(hop + 1, false, vertical);
            }
            turnAndPutBeepers();
        }
    }

    private void addPadding(boolean vertical) {
        turn = !vertical;
        for (int i = 0; i < padding; i++) {
            if (i == 0) {
                moveForward(hop, false, vertical);
            }
            moveForward(1, false, vertical);
            turnAndPutBeepers();
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
        moveForward(Integer.MAX_VALUE, false, false);
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
            moveOneStepAndTurnRight();
            moveForward(horizontalDimension / 2 - ((horizontalDimension + 1) % 2), false, false);
            turnRight();
            putBeepers = true;
            moveForward(Integer.MAX_VALUE, true, true);
            verticalDimension = numOfVerticalMoves + 1;
            divideBigWorlds();
        }
        numOfMoves = numOfHorizontalMoves + numOfVerticalMoves + numOfMoves;
    }

    private void drawFan() {
        int hop = 1 + (verticalDimension - 4) / 2;
        moveForward(hop, false, true);
        moveForward(Integer.MAX_VALUE, true, true);
        currentPathIsDone();
        pickBeepers(hop - 1);
        moveOneStep();
        turnRight();
        moveForward(Integer.MAX_VALUE, true, false);
        currentPathIsDone();
        moveForward(hop, false, false);
        moveForward(hop + 1, true, false);
    }

    private void divideBigWorlds() {
        if (horizontalDimension % 2 == 0) {
            currentPathIsDone();
            if (horizontalDimension == verticalDimension) {
                drawFan();
                return;
            }
            moveForward(Integer.MAX_VALUE, putBeepers, true);
        }
        putBeepers = true;
        turnAround();
        moveForward((verticalDimension - (horizontalDimension + 1) % 2) / 2, false, true);
        turnKarel(horizontalDimension % 2 == 1);
        moveForward(Integer.MAX_VALUE, putBeepers, false);
        if (verticalDimension % 2 == 0) {
            currentPathIsDone();
            moveForward(Integer.MAX_VALUE, putBeepers, false);
            currentPathIsDone();
            moveForward(horizontalDimension / 2 - ((horizontalDimension+1)%2) - 1, putBeepers, false);
        } else {
            turnAround();
            moveForward(Integer.MAX_VALUE, putBeepers, false);
        }
    }

    private void divideVertical() {
        moveForward(Integer.MAX_VALUE, false, true);
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
        if (dimension >= 2 && dimension <= 4)
            maxNumOfChambers = dimension;
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
                moveForward(Integer.MAX_VALUE, true, true);
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
        moveForward(Integer.MAX_VALUE, isPadding, false);
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
