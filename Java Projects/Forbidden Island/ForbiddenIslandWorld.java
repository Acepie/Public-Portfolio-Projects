// Assignment 9
// Matuszak Caitlin
// cmatusza
// Radwan Ameen
// 

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import javalib.colors.Black;
import javalib.impworld.*;
import javalib.worldimages.FrameImage;
import javalib.worldimages.Posn;
import javalib.worldimages.TextImage;
import javalib.worldimages.WorldEnd;
import javalib.worldimages.WorldImage;

// Represents the world state of the ForbiddenIsland game
class ForbiddenIslandWorld extends World {
    // All the cells of the game, including the ocean
    IList<Cell> board;
    // the current height of the ocean
    int waterHeight;
    Player player;
    Player player2;
    IList<Part> parts;
    Helicopter helicopter;
    int counter;
    int score;
    boolean paused;

    // empty world constructor
    // initializes board to an empty list, waterHeight to 0,
    // player to arbitrary position, empty parts list, helicopter
    // at arbitrary position, time counter to 0
    ForbiddenIslandWorld() {
        this.board = new Empty<Cell>();
        this.waterHeight = 0;
        this.player = new Player(new Posn(0, 0));
        this.player2 = new Player(new Posn(3, 3));
        this.parts = new Empty<Part>();
        this.helicopter = new Helicopter(new Posn(1, 1));
        this.counter = 0;
        this.score = 0;
        this.paused = false;
    }

    // Normal world constructor given all fields
    ForbiddenIslandWorld(IList<Cell> board, int waterHeight, Player player,
            Player player2, IList<Part> parts, Helicopter heli, int counter,
            int score, boolean paused) {
        this.board = board;
        this.waterHeight = waterHeight;
        this.player = player;
        this.player2 = player2;
        this.parts = parts;
        this.helicopter = heli;
        this.counter = counter;
        this.score = score;
        this.paused = paused;

    }

    // World constructor given board, waterHeight, and counter
    // initializes the player and helicopter to a valid random position
    // on the board, generates a list of size NUM_PARTS of parts at random
    // valid positions on the board
    ForbiddenIslandWorld(IList<Cell> board, int waterHeight, int counter) {
        this(board, waterHeight, new Player(board), new Player(board), null,
                new Helicopter(board), counter, 0, false);
        this.parts = this.generateN(NUM_PARTS);
    }

    // the number of tiles on one side of the island
    static final int ISLAND_SIZE = 64;

    // half of the number of tiles on one side of the island
    static final double HALF_ISLAND_SIZE = ((double) ISLAND_SIZE) / 2 - .5;

    // size of the side of a single cell on the board in pixels
    static final int PIXEL_SIZE = 10;

    // number of parts to be generated in the beginning of the game
    static final int NUM_PARTS = 4;

    // Generates a list of parts of size n at random valid positions on the
    // board
    // of this world
    IList<Part> generateN(int n) {
        IList<Part> list = new Empty<Part>();
        for (int i = 0; i < n; i += 1) {
            list = new Cons<Part>(new Part(this.board), list);
        }
        return list;
    }

    // Key handler for this world
    // make a new world based on the key pressed and the state of the current
    // world
    public void onKeyEvent(String ke) {
        IList<Cell> cells = this.board;

        // Generates new mountain board
        if (ke.equals("m")) {
            ArrayList<ArrayList<Double>> heights = this.constructMountain();
            cells = this.array2IList(this.double2Cell(heights));
            this.board = cells;
            this.waterHeight = 0;
            this.player = new Player(cells);
            this.player2 = new Player(cells);
            this.helicopter = new Helicopter(cells);
            this.parts = this.generateN(NUM_PARTS);
        }
        // Generates new random mountain board
        else if (ke.equals("r")) {
            ArrayList<ArrayList<Double>> heights = this
                    .constructRandomMountain();
            cells = this.array2IList(this.double2Cell(heights));
            this.board = cells;
            this.waterHeight = 0;
            this.player = new Player(cells);
            this.player2 = new Player(cells);
            this.helicopter = new Helicopter(cells);
            this.parts = this.generateN(NUM_PARTS);
        }
        // Generates new random terrain board
        else if (ke.equals("t")) {
            ArrayList<ArrayList<Double>> heights = this
                    .constructRandomTerrain();
            cells = this.array2IList(this.double2Cell(heights));
            this.board = cells;
            this.waterHeight = 0;
            this.player = new Player(cells);
            this.player2 = new Player(cells);
            this.helicopter = new Helicopter(cells);
            this.parts = this.generateN(NUM_PARTS);
        }
        // Moves player left by one cell
        else if (ke.equals("left")) {
            if (!this.paused) {
                this.player = this.player.move("left", this.board);
                this.score = this.score + 1;
            }
        }
        // Moves player right by one cell
        else if (ke.equals("right")) {
            if (!this.paused) {
                this.player = this.player.move("right", this.board);
                this.score = this.score + 1;
            }
        }
        // Moves player up by one cell
        else if (ke.equals("up")) {
            if (!this.paused) {
                this.player = this.player.move("up", this.board);
                this.score = this.score + 1;
            }
        }
        // Moves player down by one cell
        else if (ke.equals("down")) {
            if (!this.paused) {
                this.player = this.player.move("down", this.board);
                this.score = this.score + 1;
            }
        }
        // Moves player2 down by one cell
        else if (ke.equals("s")) {
            if (!this.paused) {
                this.player2 = this.player2.move("down", this.board);
                this.score = this.score + 1;
            }
        }
        // Moves player2 up by one cell
        else if (ke.equals("w")) {
            if (!this.paused) {
                this.player2 = this.player2.move("up", this.board);
                this.score = this.score + 1;
            }
        }
        // Moves player2 left by one cell
        else if (ke.equals("a")) {
            if (!this.paused) {
                this.player2 = this.player2.move("left", this.board);
                this.score = this.score + 1;
            }
        }
        // Moves player2 right by one cell
        else if (ke.equals("d")) {
            if (!this.paused) {
                this.player2 = this.player2.move("right", this.board);
                this.score = this.score + 1;
            }
        }
        // Pauses/unpauses the game
        else if (ke.equals("p")) {
            this.paused = !this.paused;
        }
    }

    // Returns the new updated ForbiddenIslandWorld on each tick
    public void onTick() {
        IList<Part> curParts = parts;

        if (!this.paused) {
            // current time +1 mod 10 (as flooding only happens every 10 ticks)
            int time = (this.counter + 1) % 10;

            // If the player is at the same position as any of the parts in
            // the parts list, removes that part from the list
            for (Part p : parts) {
                if (this.player.samePosition(p.posn)
                        || this.player2.samePosition(p.posn)) {
                    curParts = curParts.remove(p);
                }
            }
            this.parts = curParts;

            // Every 10 ticks increase the waterHeight by 1 and flood any
            // appropriate cells
            if (time == 9) {
                int height = this.waterHeight + 1;

                // If the cell is below sea level and is completely surrounded
                // by flooded cells, update that cell's flood status to true
                for (Cell c : this.board) {
                    if (c.height <= height && !c.surroundedByLand()) {
                        c.updateFloodStatus(height);
                    }
                }

                this.waterHeight = height;
                this.counter = time;
            }
            else {

                this.counter = time;
            }
        }
    }

    // Returns the appropriate end screen when the game is over
    public WorldEnd worldEnds() {
        // win condition: player is in the same position as the helicopter and
        // the parts list is empty
        boolean win = this.player.samePosition(this.helicopter.posn)
                && this.player2.samePosition(this.helicopter.posn)
                && this.parts.length() == 0;
        // lose condition: the cell that the player is on is flooded
        boolean lose = this.board.find(new PosnEqual(this.player.posn)).isFlooded
                || this.board.find(new PosnEqual(this.player2.posn)).isFlooded;

        WorldImage background = new FrameImage(new Posn((int) HALF_ISLAND_SIZE
                * PIXEL_SIZE, (int) HALF_ISLAND_SIZE * PIXEL_SIZE), ISLAND_SIZE
                * PIXEL_SIZE, ISLAND_SIZE * PIXEL_SIZE,
                new Color(255, 255, 255));

        // Display win screen if win condition
        if (win) {
            return new WorldEnd(true, background.overlayImages(new TextImage(
                    new Posn((int) HALF_ISLAND_SIZE * PIXEL_SIZE,
                            (int) HALF_ISLAND_SIZE * PIXEL_SIZE),
                    "Congrats. You won!", (int) HALF_ISLAND_SIZE, new Black())));
        }

        // Display lose screen if lose condition
        else if (lose) {
            return new WorldEnd(true, background.overlayImages(new TextImage(
                    new Posn((int) HALF_ISLAND_SIZE * PIXEL_SIZE,
                            (int) HALF_ISLAND_SIZE * PIXEL_SIZE),
                    "Oh no... You drowned!", (int) HALF_ISLAND_SIZE,
                    new Black())));
        }

        // Else game has not ended
        else {
            return new WorldEnd(false, background);
        }
    }

    // Renders the image of the current world
    public WorldImage makeImage() {
        // Background image
        WorldImage image = new FrameImage(new Posn((int) HALF_ISLAND_SIZE
                * PIXEL_SIZE, (int) HALF_ISLAND_SIZE * PIXEL_SIZE), ISLAND_SIZE
                * PIXEL_SIZE, ISLAND_SIZE * PIXEL_SIZE,
                new Color(255, 255, 255));

        // Draw all cells in the board list on top of the image so far
        for (Cell c : this.board) {
            image = c.drawCell(image, this.waterHeight);
        }

        // Draw the player on top of the image so far
        image = this.player.drawPlayer(image, "pilot-icon.png");

        // Draw the second player on top of the image so far
        image = this.player2.drawPlayer(image, "betterpilot.png");

        // Draw the helicopter on top of the image so far
        image = this.helicopter.drawHelicopter(image);

        // Draw all parts in the parts list on top of the image so far
        for (Part p : this.parts) {
            image = p.drawPart(image);
        }

        // Draw the score in the top left corner
        image = image.overlayImages(new TextImage(new Posn(PIXEL_SIZE * 2,
                PIXEL_SIZE * 2), new Integer(this.score).toString(),
                PIXEL_SIZE * 2, new Color(255, 255, 255)));

        return image;
    }

    // Constructs a new mountain board as an ArrayList of Array Lists of heights
    // (doubles)
    // Creates a matrix of heights for a uniform mountain
    ArrayList<ArrayList<Double>> constructMountain() {
        ArrayList<ArrayList<Double>> heights = new ArrayList<ArrayList<Double>>(
                ISLAND_SIZE);

        for (int i = 0; i < ISLAND_SIZE; i += 1) {
            ArrayList<Double> row = new ArrayList<Double>(ISLAND_SIZE);

            for (int j = 0; j < ISLAND_SIZE; j += 1) {
                double height = HALF_ISLAND_SIZE
                        - Math.abs(HALF_ISLAND_SIZE - i)
                        - Math.abs(HALF_ISLAND_SIZE - j);
                row.add(height);
            }

            heights.add(row);
        }
        return heights;
    }

    // Constructs a new random mountain board as an ArrayList of Array Lists of
    // heights(doubles)
    // Create a matrix of heights for a random mountain
    ArrayList<ArrayList<Double>> constructRandomMountain() {
        ArrayList<ArrayList<Double>> mountain = new ArrayList<ArrayList<Double>>(
                ISLAND_SIZE);

        for (int i = 0; i < ISLAND_SIZE; i += 1) {
            ArrayList<Double> row = new ArrayList<Double>(ISLAND_SIZE);

            for (int j = 0; j < ISLAND_SIZE; j += 1) {
                double height = HALF_ISLAND_SIZE
                        - Math.abs(HALF_ISLAND_SIZE - i)
                        - Math.abs(HALF_ISLAND_SIZE - j);

                if (height > 0) {
                    height = new Random().nextInt((int) (HALF_ISLAND_SIZE - 1)) + 1;
                }

                row.add(height);
            }
            mountain.add(row);
        }
        return mountain;
    }

    // Constructs a new random terrain board as an ArrayList of ArrayLists of
    // heights(doubles)
    // Creates a matrix of heights for a random terrain using the given
    // algorithm
    ArrayList<ArrayList<Double>> constructRandomTerrain() {
        ArrayList<ArrayList<Double>> mountain = new ArrayList<ArrayList<Double>>(
                ISLAND_SIZE);
        for (int i = 0; i < ISLAND_SIZE; i += 1) {
            ArrayList<Double> row = new ArrayList<Double>(ISLAND_SIZE);
            for (int j = 0; j < ISLAND_SIZE; j += 1) {
                if (Math.abs(i - HALF_ISLAND_SIZE) <= .5
                        && Math.abs(j - HALF_ISLAND_SIZE) <= .5) {
                    row.add(HALF_ISLAND_SIZE);
                }
                else if ((Math.abs(i - HALF_ISLAND_SIZE) <= .5 && Math.abs(j
                        - HALF_ISLAND_SIZE) >= HALF_ISLAND_SIZE)
                        || (Math.abs(i - HALF_ISLAND_SIZE) >= HALF_ISLAND_SIZE && Math
                                .abs(j - HALF_ISLAND_SIZE) <= .5)) {
                    row.add(0.0);
                }
                else {
                    row.add(0.0);
                }
            }
            mountain.add(row);
        }
        int lowerbound = (int) Math.floor(HALF_ISLAND_SIZE);
        int upperbound = (int) Math.ceil(HALF_ISLAND_SIZE);
        this.processTerrain(mountain, 0, 0, upperbound, ISLAND_SIZE / 2);
        this.processTerrain(mountain, 0, lowerbound, upperbound,
                ISLAND_SIZE - 1);
        this.processTerrain(mountain, lowerbound, 0, ISLAND_SIZE - 1,
                upperbound);
        this.processTerrain(mountain, lowerbound, lowerbound, ISLAND_SIZE - 1,
                ISLAND_SIZE - 1);
        return mountain;
    }

    // Helper method for constructRandomTerrain that recursively updates a
    // matrix
    // using the coordinates of the topleft and bottom right of the current
    // quadrant
    // EFFECT: recursively updates the height matrix of the terrain
    void processTerrain(ArrayList<ArrayList<Double>> mountain, int topleftX,
            int topleftY, int botrightX, int botrightY) {
        if (((botrightX - topleftX) >= 2) || ((botrightY - topleftY) >= 2)) {
            Random rand = new Random();
            double scalar = .15 * Math.sqrt(botrightX - topleftX)
                    * (botrightY - topleftY);
            double tl = mountain.get(topleftY).get(topleftX);
            double tr = mountain.get(topleftY).get(botrightX);
            double bl = mountain.get(botrightY).get(topleftX);
            double br = mountain.get(botrightY).get(botrightX);
            int midX = (topleftX + botrightX) / 2;
            int midY = (topleftY + botrightY) / 2;
            double t = (tl + tr) / 2 + (rand.nextDouble() - .8) * scalar;
            double b = (bl + br) / 2 + (rand.nextDouble() - .8) * scalar;
            double l = (tl + bl) / 2 + (rand.nextDouble() - .8) * scalar;
            double r = (tr + br) / 2 + (rand.nextDouble() - .8) * scalar;
            double m = (tl + tr + bl + br) / 4 + (rand.nextDouble() - .8)
                    * scalar;
            ArrayList<Double> top = mountain.get(topleftY);
            ArrayList<Double> mid = mountain.get(midY);
            ArrayList<Double> bot = mountain.get(botrightY);
            if (top.get(midX) != 0) {
                top.set(midX, (t + top.get(midX)) / 2);
            }
            else {
                top.set(midX, t);
            }
            if (mid.get(topleftX) != 0) {
                mid.set(topleftX, (l + mid.get(topleftX)) / 2);
            }
            else {
                mid.set(topleftX, l);
            }
            if (mid.get(midX) != 0) {
                mid.set(midX, (m + mid.get(midX)) / 2);
            }
            else {
                mid.set(midX, m);
            }
            if (mid.get(botrightX) != 0) {
                mid.set(botrightX, (r + mid.get(botrightX)) / 2);
            }
            else {
                mid.set(botrightX, r);
            }
            if (bot.get(midX) != 0) {
                bot.set(midX, (b + bot.get(midX)) / 2);
            }
            else {
                bot.set(midX, b);
            }
            mountain.set(topleftY, top);
            mountain.set(midY, mid);
            mountain.set(botrightY, bot);
            this.processTerrain(mountain, topleftX, topleftY, midX, midY);
            this.processTerrain(mountain, topleftX, midY, midX, botrightY);
            this.processTerrain(mountain, midX, topleftY, botrightX, midY);
            this.processTerrain(mountain, midX, midY, botrightX, botrightY);
        }
    }

    // Turn a matrix of heights into a matrix of cells and properly connect them
    ArrayList<ArrayList<Cell>> double2Cell(ArrayList<ArrayList<Double>> arr) {
        ArrayList<ArrayList<Cell>> cells = new ArrayList<ArrayList<Cell>>(
                ISLAND_SIZE);
        for (int i = 0; i < ISLAND_SIZE; i += 1) {
            ArrayList<Cell> row = new ArrayList<Cell>(ISLAND_SIZE);
            for (int j = 0; j < ISLAND_SIZE; j += 1) {
                Cell c;
                if (arr.get(i).get(j) <= 0) {
                    c = new OceanCell(i, j);
                }
                else {
                    c = new Cell(arr.get(i).get(j), i, j);
                }
                if (i != 0) {
                    c.updateTop(cells.get(i - 1).get(j));
                }
                else {
                    c.updateOnlyTop(c);
                }
                if (j != 0) {
                    c.updateLeft(row.get(j - 1));
                }
                else {
                    c.updateOnlyLeft(c);
                }
                if (i >= ISLAND_SIZE - 1) {
                    c.updateBottom(c);
                }
                if (j >= ISLAND_SIZE - 1) {
                    c.updateRight(c);
                }
                row.add(c);
            }
            cells.add(row);
        }

        return cells;

    }

    // Convert a matrix into a single list of cells
    IList<Cell> array2IList(ArrayList<ArrayList<Cell>> arr) {
        IList<Cell> cells = new Empty<Cell>();
        for (ArrayList<Cell> row : arr) {
            for (Cell c : row) {
                cells = new Cons<Cell>(c, cells);
            }
        }
        return cells;
    }
}