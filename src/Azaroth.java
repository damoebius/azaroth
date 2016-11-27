import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Grab Snaffles and try to throw them through the opponent's goal!
 * Move towards a Snaffle and use your team id to determine where you need to throw it.
 **/
class Player {

    private static final Point LEFT_GOAL = new Point(0,3750);
    private static final Point RIGHT_GOAL = new Point(16000,3750);

    private static Point WIZARD_GOAL;
    private static Point OPPONENT_GOAL;

    public static void main( String args[] ) {
        Scanner in = new Scanner(System.in);

        int myTeamId = in.nextInt(); // if 0 you need to score on the right of the map, if 1 you need to score on the left

        List<Entity> wizards;
        List<Entity> snaffles;
        List<Entity> opponents;

        if(myTeamId == 0){
            WIZARD_GOAL = LEFT_GOAL;
            OPPONENT_GOAL = RIGHT_GOAL;
        } else {
            WIZARD_GOAL = RIGHT_GOAL;
            OPPONENT_GOAL = LEFT_GOAL;
        }

        // game loop
        while ( true ) {
            int entities = in.nextInt(); // number of entities still in game
            wizards = new ArrayList<Entity>();
            opponents = new ArrayList<Entity>();
            snaffles = new ArrayList<Entity>();
            for ( int i = 0; i < entities; i++ ) {
                int entityId = in.nextInt(); // entity identifier
                String entityType = in.next(); // "WIZARD", "OPPONENT_WIZARD" or "SNAFFLE" (or "BLUDGER" after first league)
                int x = in.nextInt(); // position
                int y = in.nextInt(); // position
                int vx = in.nextInt(); // velocity
                int vy = in.nextInt(); // velocity
                int state = in.nextInt(); // 1 if the wizard is holding a Snaffle, 0 otherwise

                Entity entity = new Entity(entityId, entityType, x, y, vx, vy, state);
                if ( entity.type.equals("WIZARD") ) {
                    wizards.add(entity);
                } else if ( entity.type.equals( "OPPONENT_WIZARD") ) {
                    opponents.add(entity);
                } else if ( entity.type.equals("SNAFFLE") ) {
                    snaffles.add( entity );
                }

            }
            System.err.println(wizards.size());
            for ( int i = 0; i < 2; i++ ) {

                Entity wizard = wizards.get(i);
                if ( wizard.state == 0 ) {
                    if(snaffles.size() > 0) {
                        Entity targetSnaffle = Utils.getSnaffle(wizard, snaffles);
                        snaffles.remove(targetSnaffle);
                        System.out.println("MOVE " + targetSnaffle.x + " " + targetSnaffle.y + " " + 150);
                    } else {
                        System.out.println("MOVE " + opponents.get(0).x + " " + opponents.get(0).y + " " + 150);
                    }
                } else {
                    System.out.println("THROW " + OPPONENT_GOAL.x + " " + OPPONENT_GOAL.y + " " + 500);
                }
            }
        }
    }
}

class Utils {
    public static Entity getSnaffle(Entity wizard, List<Entity> snaffles){
        Entity result = snaffles.get(0);
        if(snaffles.size() > 1) {
            double currentDist = getDist(new Point(wizard.x, wizard.y), new Point(result.x, result.y));
            for ( int i = 1; i < snaffles.size(); i++ ) {
                Entity snaffle = snaffles.get(i);
                double newDist = getDist(new Point(wizard.x, wizard.y), new Point(snaffle.x, snaffle.y));
                if ( newDist < currentDist ) {
                    result = snaffle;
                }
            }
        }
        return result;
    }

    public static double getDist(Point a, Point b){
        return Math.sqrt( Math.pow(b.x - a.x,2.0) + Math.pow(b.y - a.y,2.0));
    }
}

class Entity {

    int id;
    String type;
    int x;
    int y;
    int vx;
    int vy;
    int state;

    public Entity(int id, String type, int x, int y, int vx, int vy, int state){
        this.id = id;
        this.type = type;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.state = state;
    }

}

class Point {
    int x;
    int y;

    public Point( int x, int y ) {
        this.x = x;
        this.y = y;
    }
}