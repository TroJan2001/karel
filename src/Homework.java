import stanford.karel.SuperKarel;


public class Homework extends SuperKarel {

    /* You fill the code here */
    private int numOfVerticalMoves, numOfHorizontalMoves, numOfMoves, verticalDimension , horizontalDimension;
    private int maxNumOfChambers, numOfLines, padding, hop;
    Boolean putBeepers, zigzag, turn;

    private void calculateParameters(int dimension) {
        maxNumOfChambers = maxChambers(dimension);
        numOfLines = maxNumOfChambers - 1;
        padding = (dimension + 1) % maxNumOfChambers;
        hop = (dimension - (padding + numOfLines)) / maxNumOfChambers;
    }

    private void initializeKarel() {
        numOfVerticalMoves = 0; numOfHorizontalMoves = 0; numOfMoves = 0; verticalDimension = 1000; horizontalDimension = 0; padding = 0;
        zigzag = true;
    }

    private void moveOneStep() {
        move();
        numOfMoves++;
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
            moveForward(true,false);
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
            moveForward(true,false);
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
    private void moveForward(boolean beepers, boolean vertical){
        while (frontIsClear()) {
            if (beepers && noBeepersPresent())
                putBeeper();
            move();
            if(!vertical)
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

    private void moveVertical(int moves, Boolean beepers) {
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
        moveForward(false,false);
        horizontalDimension = numOfHorizontalMoves + 1;
        turnLeft();
        checkVerticalDimension();
        if (verticalDimension <= 2) {
            if (verticalDimension == 2 && horizontalDimension <= 6 && horizontalDimension != 1) {
                zigzagHorizontal();
            } else
                divideHorizontal();
        } else if (horizontalDimension <= 2) {
            divideVertical();
        } else if (horizontalDimension % 2 == 1) {
            turnAround();
            moveOneStep();
            turnRight();
            moveHorizontal(horizontalDimension / 2 - ((horizontalDimension + 1) % 2));
            turnRight();
            putBeepers = true;
            moveForward(true,true);
            verticalDimension = numOfVerticalMoves + 1;
            divideBig();
        } else {
            moveForward(false,true);
            verticalDimension = numOfVerticalMoves + 2;
            turnLeft();
            moveHorizontal(horizontalDimension / 2 - ((horizontalDimension + 1) % 2));
            turnLeft();
            int hop = verticalDimension / 4;
            for (int i = 0; i < verticalDimension - 1; i++) {
                if (i + 1 <= hop || i + hop >= verticalDimension || i == verticalDimension / 2 || i == verticalDimension / 2 + 1) {
                    putBeeper();
                }
                moveOneStep();
            }
            turnRight();
            moveOneStep();
            turnRight();
        }
        numOfMoves = numOfHorizontalMoves + numOfVerticalMoves + numOfMoves;
    }

    private void divideBig() {
        if (horizontalDimension % 2 == 0) {
            currentPathIsDone();
            moveForward(putBeepers,true);
        }
        putBeepers = true;
        turnAround();
        moveVertical(verticalDimension / 2);
        turnLeft();
        moveForward(putBeepers,false);
        if (verticalDimension % 2 == 0) {
            currentPathIsDone();
            moveForward(putBeepers,false);
            currentPathIsDone();
            moveHorizontal(horizontalDimension / 2 - 1, putBeepers);
        } else {
            turnAround();
            moveForward(putBeepers,false);
        }
    }

    private void divideVertical() {
        moveForward(false,true);
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
                moveForward(true,true);
            }
        }
    }

    private void zigzagVertical() {
        zigzagInit(verticalDimension);
        turnAround();
        for (int i = 0; i < maxNumOfChambers; i++) {
            putBeeper();
            if (i != maxNumOfChambers - 1) {
                zigzagVerticalTurn();
            }
        }
        if (padding > 0) {
            for (int i = 0; i < padding / 2; i++) {
                zigzagVerticalTurn();
            }
        }
    }

    private void zigzagHorizontalTurn() {
        if (zigzag)
            currentPathIsDone();
        else
            otherPathIsDone();
    }

    private void zigzagVerticalTurn() {
        moveOneStep();
        turnKarel(!zigzag);
        moveForward(false,false);
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
