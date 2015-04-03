// Assignment 4
// Radwan Ameen
// aradwan
// Zhou FeiJie
// zhoufeij

import tester.*;                // The tester library
import javalib.worldimages.*;   // images, like RectangleImage or OverlayImages
import javalib.worldcanvas.*;   // the abstract World class and the big-bang library
import javalib.colors.*;        // Predefined colors (Red, Green, Yellow, Blue, Black, White)
import javalib.funworld.World;

import java.awt.Color;          // general colors (as triples of red,green,blue values)

// An Cell is one of InertCell or ARuleCell
interface ICell {
    // returns either 0 or 1 based on state of cell
    int getState();

    // draw cell onto image
    WorldImage renderInto(CartPt topLeft, CartPt botRight);

    // calculate new ICell
    ICell childCell(ICell left, ICell right);
}

// It is the cartesian coordinate with an x and a y value.
class CartPt extends Posn {
    CartPt(int x, int y) {
        super(x, y);
    }

    // return midpoint of this cartesian point and the other 
    // cartesian point
    CartPt midPoint(CartPt other) {
        return new CartPt((this.x + other.x) / 2, (this.y + other.y) / 2);
    }

    // return the x-distance between this cartesian point and the other
    // cartesian point
    int xDistance(CartPt other) {
        return Math.abs(this.x - other.x);
    }

    // return the y-distance between this cartesian point and the other
    // cartesian point
    int yDistance(CartPt other) {
        return Math.abs(this.y - other.y);
    }

    // return distance between this cartesian point and the other
    // cartesian point
    double distance(CartPt other) {
        return Math.sqrt(this.xDistance(other) * this.xDistance(other) +
                this.yDistance(other) * this.yDistance(other));
    }
}

// A cell that is always off
class InertCell implements ICell {
    InertCell() {
        // it is always off
    }

    // it is off, thus return 0
    public int getState() {
        return 0;
    }

    // draw a white rectangle
    public WorldImage renderInto(CartPt topLeft, CartPt botRight) {
        return new RectangleImage(topLeft.midPoint(botRight),
                topLeft.xDistance(botRight), topLeft.yDistance(botRight),
                new White());
    }

    // ignore its neighbors and produce another cell
    public ICell childCell(ICell left, ICell right) {
        return new InertCell();
    }
}

// A cell that has state and follows rules for child generation
abstract class ARuleCell implements ICell {
    int state;

    ARuleCell(int state) {
        this.state = state;
    }

    // based on state, return 1 or 0
    public int getState() {
        return this.state;
    }

    // based on state, draw a black or white rectangle
    public WorldImage renderInto(CartPt topLeft, CartPt botRight) {
        if (this.state == 1) {
            return new RectangleImage(topLeft.midPoint(botRight),
                    topLeft.xDistance(botRight), topLeft.yDistance(botRight),
                    new Black());
        }
        else {
            return new RectangleImage(topLeft.midPoint(botRight),
                    topLeft.xDistance(botRight), topLeft.yDistance(botRight),
                    new White());
        }
    }

    // will calculate child
    abstract public ICell childCell(ICell left, ICell right);

}


// A rule cell with rule 60
class Rule60 extends ARuleCell {
    Rule60(int state) {
        super(state);
    }

    // applies rule 60 to make child
    public ICell childCell(ICell left, ICell right) {
        if (left.getState() + this.getState() == 1) {
            return new Rule60(1);
        }
        else {
            return new Rule60(0);
        }
    }
}

// A rule cell with rule 30
class Rule30 extends ARuleCell {
    Rule30(int state) {
        super(state);
    }

    // applies rule 30 to make child
    public ICell childCell(ICell left, ICell right) {
        if ((left.getState() == 0 && this.getState() == 0 && right.getState() == 1) ||
                (left.getState() == 0 && this.getState() == 1 && right.getState() == 0) ||
                (left.getState() == 0 && this.getState() == 1 && right.getState() == 1) ||
                (left.getState() == 1 && this.getState() == 0 && right.getState() == 0)) {
            return new Rule30(1);
        }
        else {
            return new Rule30(0);
        }
    }
}

// A rule cell with rule 13
class Rule13 extends ARuleCell {
    Rule13(int state) {
        super(state);
    }

    // applies rule 13 to make child
    public ICell childCell(ICell left, ICell right) {
        if ((left.getState() == 0 && right.getState() == 0)) {
            return new Rule13(1);
        }
        else {
            return new Rule13(0);
        }
    }
}

// List of Cells
interface ILoCell {
    // create the next generation of the List of cells
    ILoCell nextGen();

    // a helper for nextGen
    ILoCell nextGenHelp(ICell previous);

    // create a child cell given neighbors
    ICell nextGenHelp2(ICell previous, ICell current);

    // append other list to end of this list
    ILoCell append(ILoCell other);

    // draw the list of cells onto given image at given cartesian point
    WorldImage drawOnto(WorldImage image, CartPt position);

}

class MtLoCell implements ILoCell {
    MtLoCell() {
        // empty case
    }

    // create the next generation of the List of cells
    public ILoCell nextGen() {
        return new MtLoCell();
    }

    // a helper for nextGen
    public ILoCell nextGenHelp(ICell previous) {
        return new MtLoCell();
    }

    // create a child cell given neighbors
    public ICell nextGenHelp2(ICell previous, ICell current) {
        return current.childCell(previous, new InertCell());
    }

    // append other list to end of this list
    public ILoCell append(ILoCell other) {
        return other;
    }

    // draw the list of cells onto given image at given cartesian point
    public WorldImage drawOnto(WorldImage image, CartPt position) {
        return image;
    }

}

class ConsLoCell implements ILoCell {
    ICell first;
    ILoCell rest;

    ConsLoCell(ICell first, ILoCell rest) {
        this.first = first;
        this.rest = rest;
    }

    // create the next generation of the List of cells    
    public ILoCell nextGen() {
        return nextGenHelp(new InertCell());
    }

    // a helper for nextGen
    public ILoCell nextGenHelp(ICell previous) {
        return new ConsLoCell(this.rest.nextGenHelp2(previous, this.first),
                this.rest.nextGenHelp(this.first));
    }

    // create a child cell given neighbors
    public ICell nextGenHelp2(ICell previous, ICell current) {
        return current.childCell(previous, this.first);
    }

    // append other list to end of this list
    public ILoCell append(ILoCell other) {
        return new ConsLoCell(this.first, this.rest.append(other));
    }

    // draw the list of cells onto given image at given cartesian point
    public WorldImage drawOnto(WorldImage image, CartPt position) {
        return this.rest.drawOnto(image.overlayImages(this.first.renderInto(
                        new CartPt(position.x, position.y - 10),
                        new CartPt(position.x + 10, position.y))),
                new CartPt(position.x + 10, position.y));
    }
}

// Examples
class ExamplesCells {
    ICell rule60t = new Rule60(1);
    ICell rule60f = new Rule60(0);
    ICell rule30t = new Rule30(1);
    ICell rule30f = new Rule30(0);
    ILoCell rule60s = new ConsLoCell(rule60t,
            new ConsLoCell(rule60f, new ConsLoCell(rule60t, new MtLoCell())));
    ILoCell rule30s = new ConsLoCell(rule30t, new ConsLoCell(rule30f, new ConsLoCell(rule30t,
            new MtLoCell())));

    boolean testchildCell(Tester t) {
        return t.checkExpect(this.rule30f.childCell(this.rule30f, this.rule30t), new Rule30(1))
                && t.checkExpect(this.rule30f.childCell(this.rule30t, this.rule30t), new Rule30(0));
    }

    boolean testnextGen(Tester t) {
        return
                t.checkExpect(this.rule60s.nextGen(), new ConsLoCell(new Rule60(1),
                        new ConsLoCell(new Rule60(1), new ConsLoCell(new Rule60(1),
                                new MtLoCell())))) &&
                        t.checkExpect(this.rule30s.nextGen(), new ConsLoCell(new Rule30(1),
                                new ConsLoCell(new Rule30(0), new ConsLoCell(new Rule30(1),
                                        new MtLoCell()))));
    }

    boolean testAppend(Tester t) {
        return
                t.checkExpect(this.rule60s.append(this.rule60s), new ConsLoCell(rule60t,
                        new ConsLoCell(rule60f,
                                new ConsLoCell(rule60t, new ConsLoCell(rule60t,
                                        new ConsLoCell(rule60f, new ConsLoCell(rule60t,
                                                new MtLoCell())))))));
    }

    boolean testdrawOnto(Tester t) {
        CAWorld world = new CAWorld(new Rule30(0), new Rule30(1), 30);
        return world.bigBang(600, 600, .1);
    }
}


// List of Lists of Cells
interface ILoLoCell {
    // draw the list of lists of cells onto given image at given cartesian point
    WorldImage drawOnto(WorldImage image, CartPt position);
}

class MtLoLoCell implements ILoLoCell {
    MtLoLoCell() {
        //empty case
    }

    // draw the list of lists of cells onto given image at given cartesian point
    public WorldImage drawOnto(WorldImage image, CartPt position) {
        return image;
    }
}

class ConsLoLoCell implements ILoLoCell {
    ILoCell first;
    ILoLoCell rest;

    ConsLoLoCell(ILoCell first, ILoLoCell rest) {
        this.first = first;
        this.rest = rest;
    }

    // draw the list of lists of cells onto given image at given cartesian point
    public WorldImage drawOnto(WorldImage image, CartPt position) {
        return this.rest.drawOnto(this.first.drawOnto(image, position),
                new CartPt(position.x, position.y - 10));
    }
}

class CAWorld extends World {
    // the current generation of cells
    ILoCell curGen;
    // the history of previous generations
    ILoLoCell history;

    // Constructs a CAWorld with the given width of off cells on the left,
    // then one on cell, then the given width of off cells on the right
    CAWorld(ICell off, ICell on, int width) {
        this.curGen = this.repeat(off, width).append(new ConsLoCell(on, this.repeat(off, width)));
        this.history = new MtLoLoCell();
    }

    // You may need to define additional constructors here
    CAWorld(ILoCell curGen, ILoLoCell history) {
        this.curGen = curGen;
        this.history = history;
    }

    // produces a list of the given item, repeated for the given count
    ILoCell repeat(ICell item, int count) {
        if (count > 0) {
            return new ConsLoCell(item, this.repeat(item, count - 1));
        }
        else {
            return new MtLoCell();
        }
    }

    // Produce a new CAWorld, with its current generation produced from 
    // this world's current generation,
    // and with a history produced from this world's current generation and history
    public World onTick() {
        return new CAWorld(this.curGen.nextGen(),
                new ConsLoLoCell(this.curGen, this.history));
    }

    // Draws the current world, ``scrolling up'' from the bottom of the image
    public WorldImage makeImage() {
        // make a light-gray background image big enough to hold 41 generations of 41 cells each
        WorldImage bg = new RectangleImage(new Posn(205, 205), 410, 410, new Color(240, 240, 240));
        // draw the current generation onto the background,
        // starting just above the bottom-left corner
        WorldImage curImg = this.curGen.drawOnto(bg, new CartPt(0, 410 - 10));
        // draw the history onto the current image, starting just above the current generation
        return this.history.drawOnto(curImg, new CartPt(0, 410 - 20));
    }
}

