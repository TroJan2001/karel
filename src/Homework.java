import stanford.karel.SuperKarel;


public class Homework extends SuperKarel {

    /* You fill the code here */
    private int numOfVerticalMoves = 0, numOfHorizontalMoves = 0, numOfMoves = 0,verticalDimension = 1000 ,horizontalDimension=0;
    private int maxNumOfChambers;
    private int numOfLines;
    private int padding;
    private int hop;

    Boolean putBeepers;

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

    private void otherPathIsDone() {
        turnRight();
        move();
        turnRight();
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
        verticalDimension = 1000;
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
            if(verticalDimension==2 && horizontalDimension<=6) {
                zigzagHorizontal();
            }
            else
                divideHorizontal();
        else if (horizontalDimension <= 2) {
            divideVertical();
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
                divideBig();
        }
        numOfMoves = numOfHorizontalMoves + numOfVerticalMoves + numOfMoves;
    }

    private void divideBig() {
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

    private void divideVertical() {
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

    private void divideHorizontal() {
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
    private void zigzagHorizontal() {
        padding = 0;
        boolean zigzag = true;
        if(horizontalDimension==2)
            maxNumOfChambers=2;
        else if (horizontalDimension==3)
            maxNumOfChambers = 3;
        else {
            maxNumOfChambers = 4;
            padding = (horizontalDimension % maxNumOfChambers) * 2;
        }
        for (int i = 0; i < maxNumOfChambers; i++) {
            putBeeper();
            if(i!=maxNumOfChambers-1){
                if(zigzag)
                    currentPathIsDone();
                else
                    otherPathIsDone();
                move();
                numOfMoves++;
            }
            zigzag=!zigzag;
        }
        if (padding>0) {
            for (int i = 0; i < padding/2; i++) {
                if(!zigzag)
                    currentPathIsDone();
                else
                    otherPathIsDone();
                zigzag=!zigzag;
                moveVertical(true);
            }
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
