import stanford.karel.SuperKarel;


public class Homework extends SuperKarel {

    /* You fill the code here */
    private int numOfVerticalMoves = 0, numOfHorizontalMoves = 0, numOfMoves = 0;
    Boolean putBeepers;
    private int maxNumOfChambers;
    private int numOfLines;
    private int padding;
    private int hop;

    private void returnToOrigin() {
        if(facingEast())
            turnAround();
        else if(facingNorth())
            turnLeft();
        else if(facingSouth())
            turnRight();
        while (frontIsClear()) {
            move();
            numOfMoves++;
        }
            turnLeft();
        while (frontIsClear()) {
            move();
            numOfMoves++;
        }
        turnLeft();
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
        int verticalDimension = 1000;
        int horizontalDimension;
        putBeepers = false;
        moveHorizontal(putBeepers);
        horizontalDimension = numOfHorizontalMoves + 1;
        turnLeft();
        if (frontIsClear()) {
            move();
            numOfMoves++;
            if (frontIsBlocked())
                verticalDimension = 2;
        }
        else
            verticalDimension = 1;
        if (verticalDimension <= 2)
            if(verticalDimension==2&&horizontalDimension==2) {
                putBeeper();
                turnLeft();
                move();
                currentPathIsDone();
                numOfMoves++;
                putBeeper();
            }
            else
                divideHorizontal(horizontalDimension);
        else if (horizontalDimension <= 2) {
            divideVertical(verticalDimension);
        } else {
            turnAround();
            move();
            numOfMoves++;
            turnRight();
            moveHorizontal(horizontalDimension / 2 - ((horizontalDimension + 1) % 2));
            turnRight();
            putBeepers = true;
            moveVertical(putBeepers);
            verticalDimension = numOfVerticalMoves + 1;
            if (verticalDimension > 2)
                divideBig(horizontalDimension, verticalDimension);
        }
        numOfMoves = numOfHorizontalMoves + numOfVerticalMoves + numOfMoves;
    }

    private void divideBig(int horizontalDimension, int verticalDimension) {
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
            moveHorizontal(horizontalDimension / 2 - 1, putBeepers);
        } else {
            turnAround();
            moveHorizontal(putBeepers);
        }
    }

    private void divideVertical(int verticalDimension) {
        moveVertical(false);
        verticalDimension = numOfVerticalMoves + 2;
        maxNumOfChambers = maxChambers(verticalDimension);
        numOfLines = maxNumOfChambers - 1;
        padding = (verticalDimension + 1) % maxNumOfChambers;
        hop = (verticalDimension - (padding + numOfLines)) / maxNumOfChambers;
        boolean turn = false;

        turnAround();
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
            if (i == 0)
                moveVertical(hop);
            moveVertical(1);
            turnKarel(turn);
            moveHorizontal(true);
            turnKarel(!turn);
            turn = !turn;
        }

    }

    private void divideHorizontal(int horizontalDimension) {
        maxNumOfChambers = maxChambers(horizontalDimension);
        numOfLines = maxNumOfChambers - 1;
        padding = (horizontalDimension + 1) % maxNumOfChambers;
        hop = (horizontalDimension - (padding + numOfLines)) / maxNumOfChambers;
        turnLeft();
        boolean turn = true;
        for (int i = 0; i < numOfLines; i++) {
            if (i == 0)
                moveHorizontal(hop);
            else
                moveHorizontal(hop + 1);
            turnKarel(turn);
            moveHorizontal(true);
            turnKarel(!turn);
            turn = !turn;
        }
        for (int i = 0; i < padding; i++) {
            if (i == 0)
                moveHorizontal(hop);
            moveHorizontal(1);
            turnKarel(turn);
            moveHorizontal(true);
            turnKarel(!turn);
            turn = !turn;
        }

    }

    public void run() {
        setBeepersInBag(1000);
        divideMap();
        putBeepers = false;
        returnToOrigin();
        System.out.println(numOfMoves);
    }
}
