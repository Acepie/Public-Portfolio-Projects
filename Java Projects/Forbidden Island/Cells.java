// Assignment 9
// Matuszak Caitlin
// cmatusza
// Radwan Ameen
// 

import javalib.worldimages.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

// Represents a single square of the game area
class Cell {
    // represents absolute height of this cell, in feet
    double height;
    // In logical coordinates, with the origin at the top-left corner of the
    // screen
    int x, y;
    // the four adjacent cells to this one
    Cell left, top, right, bottom;
    // reports whether this cell is flooded or not
    boolean isFlooded;

    // Cell constructor, given height, x, and y
    // initializes all adjacent cell links to null
    Cell(double height, int x, int y) {
        this.height = height;
        this.x = x;
        this.y = y;
        this.left = null;
        this.top = null;
        this.right = null;
        this.bottom = null;
        // sets isFlooded to the correct value based on height of the cell
        if (height <= 0) {
            this.isFlooded = true;
        }
        else {
            this.isFlooded = false;
        }
    }

    // Cell constructor, given height, x, y, and 4 adjacent cells
    Cell(double height, int x, int y, Cell left, Cell top, Cell right,
            Cell bottom, boolean isFlooded) {
        this.height = height;
        this.x = x;
        this.y = y;
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.isFlooded = isFlooded;
    }

    // EFFECT: updates isFlooded field of this cell to true
    // also may modify the isFlooded status of the adjacent cells if appropriate
    void updateFloodStatus(double height) {
        this.isFlooded = true;
        if (this.top.height <= height && !this.top.surroundedByLand()
                && !this.top.isFlooded) {
            this.top.updateFloodStatus(height);
        }
        if (this.left.height <= height && !this.left.surroundedByLand()
                && !this.left.isFlooded) {
            this.left.updateFloodStatus(height);
        }
        if (this.right.height <= height && !this.right.surroundedByLand()
                && !this.right.isFlooded) {
            this.right.updateFloodStatus(height);
        }
        if (this.bottom.height <= height && !this.bottom.surroundedByLand()
                && !this.bottom.isFlooded) {
            this.bottom.updateFloodStatus(height);
        }

    }

    // EFFECT: updates left cell field of this cell to be the given cell
    // also modifies the right field of the given cell to be this cell
    void updateLeft(Cell left) {
        this.left = left;
        left.updateRight(this);
    }

    // EFFECT: updates top cell field of this cell to be the given cell
    // also modifies the bottom field of the given cell to be this cell
    void updateTop(Cell top) {
        this.top = top;
        top.updateBottom(this);
    }

    // EFFECT: updates left cell field of this cell to be the given cell
    // note: does not modify any links of the given cell
    void updateOnlyLeft(Cell left) {
        this.left = left;
    }

    // EFFECT: updates top cell field of this cell to be the given cell
    // note: does not modify any links of the given cell
    void updateOnlyTop(Cell top) {
        this.top = top;
    }

    // EFFECT: updates right cell field of this cell to be the given cell
    void updateRight(Cell right) {
        this.right = right;
    }

    // EFFECT: updates bottom cell field of this cell to be the given cell
    void updateBottom(Cell bottom) {
        this.bottom = bottom;
    }

    // Renders the image of this cell on top of the given background image
    // with the appropriate color depending on the given waterHeight
    WorldImage drawCell(WorldImage background, double waterHeight) {
        // height above sea level
        double distfromsea = Math.abs(waterHeight - this.height);
        int depth = (int) (180 * (1 - Math.exp(-1 * distfromsea
                / (.15 * (ForbiddenIslandWorld.ISLAND_SIZE)))));

        // for rendering flooded cells
        if (this.isFlooded) {
            return background.overlayImages(new RectangleImage(new Posn(this.x
                    * ForbiddenIslandWorld.PIXEL_SIZE
                    + ForbiddenIslandWorld.PIXEL_SIZE / 2, this.y
                    * ForbiddenIslandWorld.PIXEL_SIZE
                    + ForbiddenIslandWorld.PIXEL_SIZE / 2),
                    ForbiddenIslandWorld.PIXEL_SIZE,
                    ForbiddenIslandWorld.PIXEL_SIZE, new Color(30, 30,
                            200 - depth)));
        }
        // for cells that are not flooded but are below sea level
        else if (waterHeight > this.height) {
            return background.overlayImages(new RectangleImage(new Posn(this.x
                    * ForbiddenIslandWorld.PIXEL_SIZE
                    + ForbiddenIslandWorld.PIXEL_SIZE / 2, this.y
                    * ForbiddenIslandWorld.PIXEL_SIZE
                    + ForbiddenIslandWorld.PIXEL_SIZE / 2),
                    ForbiddenIslandWorld.PIXEL_SIZE,
                    ForbiddenIslandWorld.PIXEL_SIZE, new Color(depth,
                            180 - depth, 0)));
        }
        // for normal non-flooded cells above sea level
        else {
            return background.overlayImages(new RectangleImage(new Posn(this.x
                    * ForbiddenIslandWorld.PIXEL_SIZE
                    + ForbiddenIslandWorld.PIXEL_SIZE / 2, this.y
                    * ForbiddenIslandWorld.PIXEL_SIZE
                    + ForbiddenIslandWorld.PIXEL_SIZE / 2),
                    ForbiddenIslandWorld.PIXEL_SIZE,
                    ForbiddenIslandWorld.PIXEL_SIZE, new Color(depth, 180,
                            depth)));
        }

    }

    // Determines if this cell is completely surrounded by land
    // by way of checking the isFlooded statuses of the adjacent cells
    boolean surroundedByLand() {
        return !this.top.isFlooded && !this.bottom.isFlooded
                && !this.left.isFlooded && !this.right.isFlooded;
    }

    // Determines if this cell is an OceanCell
    boolean isOceanCell() {
        return false;
    }
}

// Represents a single ocean cell in the game area
class OceanCell extends Cell {

    // Ocean cell constructor, given x and y
    // ocean cells are always flooded
    OceanCell(int x, int y) {
        super(-1, x, y);
        this.isFlooded = true;
    }

    // Ocean cell constructor, given x, y, and 4 adjacent cells
    // ocean cells are always flooded
    OceanCell(int x, int y, Cell left, Cell top, Cell right, Cell bottom) {
        super(-1, x, y, left, top, right, bottom, true);
    }

    // Renders the image of this OceanCell on the given background image
    // OceanCells are always blue and never change color
    WorldImage drawCell(WorldImage background, double waterHeight) {
        return background.overlayImages(new RectangleImage(new Posn(this.x
                * ForbiddenIslandWorld.PIXEL_SIZE
                + ForbiddenIslandWorld.PIXEL_SIZE / 2, this.y
                * ForbiddenIslandWorld.PIXEL_SIZE
                + ForbiddenIslandWorld.PIXEL_SIZE / 2),
                ForbiddenIslandWorld.PIXEL_SIZE,
                ForbiddenIslandWorld.PIXEL_SIZE, new Color(0, 0, 200)));
    }

    // Determines if this cell is an OceanCell
    boolean isOceanCell() {
        return true;
    }
}

// A Player represents the user and their current position
class Player {
    Posn posn;

    // Player constructor given a position
    Player(Posn posn) {
        this.posn = posn;
    }

    // Player constructor given the board, constructs player in random valid
    // position on the board
    Player(IList<Cell> board) {
        Posn posn = new Posn(
                new Random().nextInt(ForbiddenIslandWorld.ISLAND_SIZE),
                (new Random().nextInt(ForbiddenIslandWorld.ISLAND_SIZE)));
        Cell c = board.find(new PosnEqual(posn));
        while (c.isFlooded || c.height < ForbiddenIslandWorld.ISLAND_SIZE / 8) {
            posn = new Posn(
                    new Random().nextInt(ForbiddenIslandWorld.ISLAND_SIZE),
                    (new Random().nextInt(ForbiddenIslandWorld.ISLAND_SIZE)));
            c = board.find(new PosnEqual(posn));
        }
        this.posn = posn;
    }

    // Draws the player image on top of the given image
    WorldImage drawPlayer(WorldImage background, String file) {
        return background.overlayImages(new FromFileImage(new Posn(this.posn.x
                * ForbiddenIslandWorld.PIXEL_SIZE
                + ForbiddenIslandWorld.PIXEL_SIZE / 2, this.posn.y
                * ForbiddenIslandWorld.PIXEL_SIZE
                + ForbiddenIslandWorld.PIXEL_SIZE / 2), file));
    }

    // Moves the player in the given direction if possible
    Player move(String key, IList<Cell> board) {
        Posn posn = this.posn;
        if (key.equals("left")) {
            posn = new Posn(this.posn.x - 1, this.posn.y);
            if (!board.find(new PosnEqual(posn)).isFlooded) {
                return new Player(posn);
            }
        }
        else if (key.equals("right")) {
            posn = new Posn(this.posn.x + 1, this.posn.y);
            if (!board.find(new PosnEqual(posn)).isFlooded) {
                return new Player(posn);
            }
        }
        else if (key.equals("up")) {
            posn = new Posn(this.posn.x, this.posn.y - 1);
            if (!board.find(new PosnEqual(posn)).isFlooded) {
                return new Player(posn);
            }
        }
        else if (key.equals("down")) {
            posn = new Posn(this.posn.x, this.posn.y + 1);
            if (!board.find(new PosnEqual(posn)).isFlooded) {
                return new Player(posn);
            }
        }
        return this;
    }

    // Determines if this player is in the same position as the given Posn
    boolean samePosition(Posn other) {
        return this.posn.x == other.x && this.posn.y == other.y;
    }
}

// Represents a helicopter part and its position
class Part {
    Posn posn;

    // Part constructor, constructs part at given Posn
    Part(Posn posn) {
        this.posn = posn;
    }

    // Part constructor given a board, constructs the part in a random valid
    // position on the board
    Part(IList<Cell> board) {
        Posn posn = new Posn(
                new Random().nextInt(ForbiddenIslandWorld.ISLAND_SIZE),
                new Random().nextInt(ForbiddenIslandWorld.ISLAND_SIZE));
        Cell c = board.find(new PosnEqual(posn));
        while (c.isFlooded || c.height < ForbiddenIslandWorld.ISLAND_SIZE / 8) {
            posn = new Posn(
                    new Random().nextInt(ForbiddenIslandWorld.ISLAND_SIZE),
                    new Random().nextInt(ForbiddenIslandWorld.ISLAND_SIZE));
            c = board.find(new PosnEqual(posn));
        }
        this.posn = posn;
    }

    // Draw this part on the given image
    WorldImage drawPart(WorldImage background) {
        return background.overlayImages(new DiskImage(new Posn(this.posn.x
                * ForbiddenIslandWorld.PIXEL_SIZE
                + ForbiddenIslandWorld.PIXEL_SIZE / 2, this.posn.y
                * ForbiddenIslandWorld.PIXEL_SIZE
                + ForbiddenIslandWorld.PIXEL_SIZE / 2),
                ForbiddenIslandWorld.PIXEL_SIZE / 2, new Color(255, 0, 0)));
    }

}

// Represents the helicopter and its position
class Helicopter {
    Posn posn;

    // Constructs a helicopter at the given position
    Helicopter(Posn posn) {
        this.posn = posn;
    }

    // Constructs a helicopter at the highest point on the given board
    Helicopter(IList<Cell> board) {
        double maxHeight = 0;
        ArrayList<Cell> peaks = new ArrayList<Cell>();
        for (Cell c : board) {
            if (c.height > maxHeight) {
                peaks.clear();
                peaks.add(c);
                maxHeight = c.height;
            }
            else if (c.height == maxHeight) {
                peaks.add(c);
            }
        }
        Cell peak = peaks.get(peaks.size() / 2);
        this.posn = new Posn(peak.x, peak.y);
    }

    // Draws this helicopter on top of the given image
    WorldImage drawHelicopter(WorldImage background) {
        return background.overlayImages(new FromFileImage(new Posn(this.posn.x
                * ForbiddenIslandWorld.PIXEL_SIZE
                + ForbiddenIslandWorld.PIXEL_SIZE / 2, this.posn.y
                * ForbiddenIslandWorld.PIXEL_SIZE
                + ForbiddenIslandWorld.PIXEL_SIZE / 2), "helicopter.png"));
    }
}

// Predicate that determines if this object's posn is the same as the posn of
// the given cell
class PosnEqual implements IPred<Cell> {
    Posn posn;

    PosnEqual(Posn posn) {
        this.posn = posn;
    }

    // Determines if this's posn is the same as the posn of the given cell
    public boolean apply(Cell c) {
        return c.x == posn.x && c.y == posn.y;
    }
}