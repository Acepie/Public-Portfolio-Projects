// Assignment 5h
// Radwan Ameen
// aradwan
// Zhou FeiJie
// zhoufeij

import javalib.funworld.World;
import tester.*;
import javalib.worldimages.*;
import javalib.colors.*;

import java.awt.*;
import java.util.Random;

// An AFish represents a fish with a size, color, position, and a speed in the x direction
abstract class AFish {
    int size;
    IColor color;
    CartPt position;
    int xspeed;

    AFish(int size, IColor color, CartPt position, int xspeed) {
        if (size <= 0) {
            throw new IllegalArgumentException("size of fish must be greater than 0");
        }
        else {
            this.size = size;
            this.color = color;
            this.position = position;
            this.xspeed = xspeed;
        }
    }

    // move this AFish to new position based on speed
    abstract AFish move();

    // check if this AFish is colliding with another fish
    boolean collision(AFish other) {
        return this.position.distance(other.position) < (this.size * 3 + other.size * 3);
    }

    // draw this fish facing left
    WorldImage drawLeft(WorldImage background) {
        return background.overlayImages(new EllipseImage(
                this.position, this.size * 10, this.size * 5, this.color), new TriangleImage(
                    new Posn(this.position.x + this.size * 5, this.position.y),
                    new Posn(this.position.x + this.size * 7, this.position.y + this.size * 2),
                    new Posn(this.position.x + this.size * 7, this.position.y - this.size * 2),
                    this.color));
    }

    // draw this fish facing right
    WorldImage drawRight(WorldImage background) {
        return background.overlayImages(new EllipseImage(
                this.position, this.size * 10, this.size * 5, this.color), new TriangleImage(
                    new Posn(this.position.x - this.size * 5, this.position.y),
                    new Posn(this.position.x - this.size * 7, this.position.y + this.size * 2),
                    new Posn(this.position.x - this.size * 7, this.position.y - this.size * 2),
                    this.color));
    }

    //draw this fish onto a scene facing the appropriate direction
    abstract WorldImage drawFish(WorldImage background);

    //is this fish moving off the left side of the screen
    boolean offLeft() {
        return this.xspeed < 0 && (this.position.x + this.xspeed) < 0;
    }

    //is this fish moving off the right side of the screen
    boolean offRight() {
        return this.xspeed > 0 && (this.position.x + this.xspeed) > 800;
    }

}

// A Fish is an AFish that is not a player and moves freely
class Fish extends AFish {
    Fish(int size, CartPt position, int xspeed) {
        super(size, new Blue(), position, xspeed);
    }

    Fish(CartPt position, int xspeed) {
        this(new Random().nextInt(24) + 1, position, xspeed);
    }

    Fish(int size, int xspeed) {
        this(size, new CartPt(5, new Random().nextInt(590) + 5), xspeed);
        if (xspeed < 0) {
            this.position = new CartPt(795, new Random().nextInt(590) + 5);
        }
    }

    Fish(int xspeed) {
        this(new Random().nextInt(24) + 1, new CartPt(5, new Random().nextInt(590) + 5), xspeed);
        if (xspeed < 0) {
            this.position = new CartPt(795, new Random().nextInt(590) + 5);
        }
    }


    // move this fish to new position based on speed
    Fish move() {
        if (this.offLeft() || this.offRight()) {
            return new Fish(this.xspeed);
        }
        else {
            return new Fish(this.size,
                    new CartPt(this.position.x + xspeed, this.position.y),
                    this.xspeed);
        }
    }

    //draw this fish onto a scene facing the appropriate direction
    WorldImage drawFish(WorldImage background) {
        if (this.xspeed > 0) {
            return this.drawRight(background);
        }
        else {
            return this.drawLeft(background);
        }
    }
}

// A Player if an AFish that is controlled by the player
class Player extends AFish {
    int yspeed;
    boolean idleleft;

    Player(int size, CartPt position, int xspeed, int yspeed, boolean idleleft) {
        super(size, new Red(), position, xspeed);
        this.yspeed = yspeed;
        this.idleleft = idleleft;
    }

    Player(int size, CartPt position, int xspeed, int yspeed) {
        this(size, position, xspeed, yspeed, true);
        if (this.xspeed > 0) {
            this.idleleft = false;
        }
        else if (this.xspeed < 0) {
            this.idleleft = true;
        }
        else {
            throw new IllegalArgumentException("direction can not be automatically determined");
        }
    }

    // move this player to new position based on speed
    // players can not move above or below the screen but they will loop around the sides
    Player move() {
        if (this.position.y + this.size + this.yspeed > 600
                || this.position.y - this.size + this.yspeed < 0) {
            return new Player(this.size,
                    new CartPt((this.position.x + this.xspeed + 800) % 800, this.position.y),
                    0, 0, this.idleleft);
        }
        else {
            return new Player(this.size,
                    new CartPt(((this.position.x + this.xspeed + 800) % 800),
                            this.position.y + this.yspeed),
                    0, 0, this.idleleft);
        }
    }

    // check if this player can eat another fish
    boolean edible(AFish other) {
        return this.size > other.size;
    }

    // change this player's speed and direction based on a key event
    Player moveOnKey(String ke) {
        if (ke.equals("left")) {
            return this.updateFishXSpeed(-20);
        }
        else if (ke.equals("right")) {
            return this.updateFishXSpeed(20);
        }
        else if (ke.equals("up")) {
            return this.updateFishYSpeed(-10);
        }
        else if (ke.equals("down")) {
            return this.updateFishYSpeed(10);
        }
        else {
            return this.updateFishXSpeed(0).updateFishYSpeed(0);
        }
    }

    // construct a new fish with a new xspeed
    Player updateFishXSpeed(int xspeed) {
        if (xspeed != 0) {
            return new Player(this.size, this.position, xspeed, this.yspeed);
        }
        else {
            return new Player(this.size, this.position, xspeed, this.yspeed, this.idleleft);
        }
    }

    // construct a new fish with a new yspeed
    Player updateFishYSpeed(int yspeed) {
        return new Player(this.size, this.position, this.xspeed, yspeed, this.idleleft);
    }

    //draw this fish onto a scene facing the appropriate direction
    WorldImage drawFish(WorldImage background) {
        if (this.idleleft) {
            return this.drawLeft(background);
        }
        else {
            return this.drawRight(background);
        }
    }

    // grow this player based on the size of the food
    Player grow(Fish fish) {
        return new Player(this.size + fish.size, this.position, this.xspeed,
                    this.yspeed, this.idleleft);
    }
}

// An ILoFish is a list of fishes represented by either an empty list or a cons list
interface ILoFish {
    // move all the fishes in this list
    ILoFish moveAll();

    // add n new random fishes to this list
    ILoFish generateFish(int n);

    // compute the length of this list
    int length();

    // remove a fish from this list if it is in the list
    ILoFish removeFish(Fish fish);

    // check if a player collides with any of the fish in this list
    boolean collision(Player player);

    // find the fish that collides with this player
    // this function should only be called on lists that have collisions with this player
    Fish collider(Player player);

    //draw all the fishes in this list onto a background
    WorldImage drawAll(WorldImage background);

    //get the first fish in a nonempty list
    //only used for testing purposes
    Fish getFirst();
}

// A ConsLoFish is a list of fishes with a first fish and a list of the rest of the fishes
class ConsLoFish implements ILoFish {
    Fish first;
    ILoFish rest;

    ConsLoFish(Fish first, ILoFish rest) {
        this.first = first;
        this.rest = rest;
    }

    // move all the fishes in this list
    public ILoFish moveAll() {
        return new ConsLoFish(this.first.move(), this.rest.moveAll());
    }

    // add n new random fishes to this list
    public ILoFish generateFish(int n) {
        if (n > 0) {
            int speed = new Random().nextInt(10) - 5;
            if (speed == 0) {
                speed = 5;
            }
            if (speed > 0) {
                return new ConsLoFish(
                        new Fish(speed), this.generateFish(n - 1));
            }
            else {
                return new ConsLoFish(
                        new Fish(speed), this.generateFish(n - 1));
            }
        }
        else {
            return this;
        }
    }

    // compute the length of this list
    public int length() {
        return 1 + this.rest.length();
    }

    // remove a fish from this list if it is in the list
    public ILoFish removeFish(Fish fish) {
        if (this.first.equals(fish)) {
            return this.rest;
        }
        else {
            return new ConsLoFish(this.first, this.rest.removeFish(fish));
        }
    }

    // check if a player collides with any of the fish in this list
    public boolean collision(Player player) {
        return this.first.collision(player)
                || this.rest.collision(player);
    }

    // find the fish that collides with this player
    // this function should only be called on lists that have collisions with this player
    public Fish collider(Player player) {
        if (this.first.collision(player)) {
            return this.first;
        }
        else {
            return this.rest.collider(player);
        }
    }

    //draw all the fishes in this list onto a background
    public WorldImage drawAll(WorldImage background) {
        return this.first.drawFish(this.rest.drawAll(background));
    }

    //get the first fish in a nonempty list
    //only used for testing purposes
    public Fish getFirst() {
        return this.first;
    }
}

// A MtLoFish represents an empty list of fishes
class MtLoFish implements ILoFish {
    // move all the fishes in this list
    public ILoFish moveAll() {
        return this;
    }

    // add n new random fishes to this list
    public ILoFish generateFish(int n) {
        if (n > 0) {
            int speed = new Random().nextInt(10) - 5;
            if (speed == 0) {
                return new ConsLoFish(
                        new Fish(5), this).generateFish(n - 1);
            }
            else if (speed > 0) {
                return new ConsLoFish(
                        new Fish(speed), this).generateFish(n - 1);
            }
            else {
                return new ConsLoFish(
                        new Fish(speed), this).generateFish(n - 1);
            }
        }
        else {
            return this;
        }
    }

    // calculate the length of this list
    public int length() {
        return 0;
    }

    // remove a fish from this list if it is in this list
    public ILoFish removeFish(Fish fish) {
        return this;
    }

    // check if a player collides with a fish in this list
    public boolean collision(Player player) {
        return false;
    }

    // return the fish in this list that collides with a player
    public Fish collider(Player player) {
        throw new IllegalStateException("no colliders in list");
    }

    //draw all the fishes in this list onto a background
    public WorldImage drawAll(WorldImage background) {
        return background;
    }

    //get the first fish in a nonempty list
    //only used for testing purposes
    public Fish getFirst() {
        throw new IllegalArgumentException("can't get the first of empty");
    }
}

// A CartPt represents Cartesian point
class CartPt extends Posn {
    CartPt(int x, int y) {
        super(x, y);
    }

    // calculate the distance between this posn and another posn
    double distance(CartPt other) {
        return Math.sqrt((this.x - other.x) * (this.x - other.x) +
                (this.y - other.y) * (this.y - other.y));
    }
}

// A FishGame represents the world of the game
class FishGame extends World {
    ILoFish fishes;
    Player player;

    FishGame(ILoFish fishes, Player player) {
        this.fishes = fishes;
        this.player = player;
    }

    FishGame() {
        this.fishes = new MtLoFish().generateFish(5);
        this.player = new Player(3, new CartPt(400, 300), 0, 0, true);
    }

    // draw the player and the list of fishes onto the background
    public WorldImage makeImage() {
        WorldImage bg = new RectangleImage(new Posn(400, 300), 800, 600, new Color(240, 240, 240));
        return this.player.drawFish(this.fishes.drawAll(bg));
    }

    // update the world based on every tick of the clock
    // move the list of fish and player after every tick
    public FishGame onTick() {
        if (this.fishes.collision(this.player)
                && this.player.edible(this.fishes.collider(this.player))) {
            return new FishGame(this.fishes.removeFish(this.fishes.collider(player))
                    .moveAll().generateFish(2),
                    this.player.grow(this.fishes.collider(player)).move());
        }
        else {
            return new FishGame(this.fishes.moveAll(), this.player.move());
        }
    }

    // create a new FishGame with update down based on the key event
    public FishGame onKeyEvent(String ke) {
        return new FishGame(this.fishes, this.player.moveOnKey(ke));
    }

    // determine when the game ends
    // when player is the bigger than all fishes in the list
    // or when player is eaten by a fish bigger than it
    public WorldEnd worldEnds() {
        WorldImage end = new RectangleImage(new Posn(400, 300), 800, 600, new Color(240, 240, 240))
                .overlayImages(new TextImage(new Posn(400, 300), "Game Over. You scored "
                        .concat(Integer.toString(this.player.size))
                        .concat(" points!"), 30, new Black()));
        if ((this.fishes.collision(this.player)
                && !(this.player.edible(this.fishes.collider(this.player))))
                || (this.player.size > 40)) {
            return new WorldEnd(true, end);
        }
        else {
            return new WorldEnd(false, end);
        }
    }
}

class ExamplesFishGame {
    // Fish Game
    Fish basefish = new Fish(1, new CartPt(5, 400), 3);
    ILoFish fishes = new ConsLoFish(basefish, new MtLoFish()).generateFish(4);
    Player player = new Player(5, new CartPt(400, 300), 0, 0, true);
    FishGame game = new FishGame(fishes, player);

    boolean donttestGame(Tester t) {
        return game.bigBang(800, 600, .1);
    }

    // AFish, Fish and Player
    Player player1 = new Player(5, new CartPt(400, 300), 5, 5, true);
    Player player2 = new Player(9, new CartPt(540, 265), 0, 0, false);
    Fish fish1 = new Fish(2, new CartPt(14, 240), 5);
    Fish fish2 = new Fish(4, new CartPt(7, 388), 5);
    Fish fish3 = new Fish(5, new CartPt(2, 400), -3);
    Fish fish4 = new Fish(10, new CartPt(794, 200), 9);
    Fish fish5 = new Fish(3, new CartPt(356, 90), -15);
    Fish fish6 = new Fish(4, new CartPt(405, 295), -5);

    Fish fish8 = new Fish(new CartPt(500, 130), 5);
    Fish fish9 = new Fish(8, 4);
    Fish fish10 = new Fish(7);

    boolean testCollision(Tester t) {
        return t.checkExpect(player.collision(player), true)
                && t.checkExpect(basefish.collision(fish2), true)
                && t.checkExpect(basefish.collision(fish1), false)
                && t.checkExpect(player.collision(basefish), false);
    }

    boolean testOffScreen(Tester t) {
        return t.checkExpect(fish3.offLeft(), true)
                && t.checkExpect(fish4.offRight(), true)
                && t.checkExpect(fish1.offLeft(), false)
                && t.checkExpect(fish1.offRight(), false);
    }

    boolean testMove(Tester t) {
        return t.checkExpect(basefish.move(), new Fish(1, new CartPt(8, 400), 3))
                && t.checkExpect(player.move(), new Player(5, new CartPt(400, 300), 0, 0, true))
                && t.checkExpect(player1.move(), new Player(5, new CartPt(405, 305), 0, 0, true))
                && t.checkExpect(new Player(5, new CartPt(2, 300), -5, 0, true).move(),
                    new Player(5, new CartPt(797, 300), 0, 0, true));
    }

    boolean testEdible(Tester t) {
        return t.checkExpect(player.edible(fish1), true)
                && t.checkExpect(player.edible(fish3), false)
                && t.checkExpect(player.edible(fish4), false);
    }

    boolean testMoveOnKey(Tester t) {
        return t.checkExpect(player.moveOnKey("left"),
                    new Player(5, new CartPt(400, 300), -20, 0, true))
                && t.checkExpect(player.moveOnKey("right"),
                    new Player(5, new CartPt(400, 300), 20, 0, false))
                && t.checkExpect(player.moveOnKey("up"),
                    new Player(5, new CartPt(400, 300), 0, -10, true))
                && t.checkExpect(player.moveOnKey("down"),
                    new Player(5, new CartPt(400, 300), 0, 10, true))
                && t.checkExpect(player.moveOnKey("space"),
                    new Player(5, new CartPt(400, 300), 0, 0, true));
    }

    boolean testUpdateFishSpeed(Tester t) {
        return t.checkExpect(player.updateFishXSpeed(0),
                    new Player(5, new CartPt(400, 300), 0, 0, true))
                && t.checkExpect(player.updateFishXSpeed(15),
                    new Player(5, new CartPt(400, 300), 15, 0, false))
                && t.checkExpect(player.updateFishXSpeed(-25),
                    new Player(5, new CartPt(400, 300), -25, 0, true))
                && t.checkExpect(player.updateFishYSpeed(10),
                    new Player(5, new CartPt(400, 300), 0, 10, true))
                && t.checkExpect(player.updateFishYSpeed(0),
                    new Player(5, new CartPt(400, 300), 0, 0, true))
                && t.checkExpect(player.updateFishYSpeed(-15),
                    new Player(5, new CartPt(400, 300), 0, -15, true));
    }

    boolean testGrow(Tester t) {
        return t.checkExpect(player.grow(fish1),
                    new Player(7, new CartPt(400, 300), 0, 0, true))
                && t.checkExpect(player2.grow(fish1),
                    new Player(11, new CartPt(540, 265), 0, 0, false));
    }

    // CartPt
    CartPt cp1 = new CartPt(6, 15);
    CartPt cp2 = new CartPt(9, 11);
    CartPt cp3 = new CartPt(1, 27);

    boolean testDistance(Tester t) {
        return t.checkInexact(cp1.distance(cp2), 5.0, 0.001)
                && t.checkInexact(cp1.distance(cp3), 13.0, 0.001);
    }

    // List of Fish
    ILoFish emptyfish = new MtLoFish();
    ILoFish listfish1 = new ConsLoFish(fish1, emptyfish);
    ILoFish listfish2 = new ConsLoFish(fish2, new ConsLoFish(fish1, emptyfish));
    ILoFish listfish3 = new ConsLoFish(fish5, new ConsLoFish(fish2, emptyfish));
    ILoFish listfish4 = new ConsLoFish(fish1, new ConsLoFish(fish6,
            new ConsLoFish(fish2, emptyfish)));
    ILoFish listfish5 = new ConsLoFish(fish6, new ConsLoFish(fish5, emptyfish));

    boolean testMoveAll(Tester t) {
        return t.checkExpect(emptyfish.moveAll(), emptyfish)
                && t.checkExpect(listfish1.moveAll(),
                    new ConsLoFish(new Fish(2, new CartPt(19, 240), 5), emptyfish))
                && t.checkExpect(listfish2.moveAll(),
                    new ConsLoFish(new Fish(4, new CartPt(12, 388), 5),
                        new ConsLoFish(new Fish(2, new CartPt(19, 240), 5),
                                emptyfish)))
                && t.checkExpect(listfish3.moveAll(),
                    new ConsLoFish(new Fish(3, new CartPt(341, 90), -15),
                        new ConsLoFish(new Fish(4, new CartPt(12, 388), 5),
                                emptyfish)));
    }

    boolean testLength(Tester t) {
        return t.checkExpect(emptyfish.length(), 0)
                && t.checkExpect(listfish1.length(), 1)
                && t.checkExpect(listfish2.length(), 2)
                && t.checkExpect(listfish3.length(), 2)
                && t.checkExpect(listfish4.length(), 3);
    }

    boolean testGenerateFishes(Tester t) {
        return t.checkExpect(this.fishes.length(), 5)
                && t.checkRange(this.fish8.size, 0, 25)
                && t.checkRange(this.fish9.position.x, 0, 800)
                && t.checkRange(this.fish9.position.y, 0, 600)
                && t.checkRange(this.fish10.size, 0, 25)
                && t.checkRange(this.fish10.position.x, 0, 800)
                && t.checkRange(this.fish10.position.y, 0, 600)
                && t.checkRange(this.fishes.getFirst().size, 0, 25);
    }

    boolean testRemoveFish(Tester t) {
        return t.checkExpect(emptyfish.removeFish(fish1), emptyfish)
                && t.checkExpect(listfish1.removeFish(fish1), emptyfish)
                && t.checkExpect(listfish1.removeFish(fish2), listfish1)
                && t.checkExpect(listfish2.removeFish(fish2), listfish1)
                && t.checkExpect(listfish2.removeFish(fish1), new ConsLoFish(fish2, emptyfish))
                && t.checkExpect(listfish2.removeFish(fish5), listfish2)
                && t.checkExpect(listfish4.removeFish(fish6),
                    new ConsLoFish(fish1, new ConsLoFish(fish2, emptyfish)));
    }

    boolean testCollisionInList(Tester t) {
        return t.checkExpect(emptyfish.collision(player), false)
                && t.checkExpect(listfish1.collision(player), false)
                && t.checkExpect(listfish2.collision(player), false)
                && t.checkExpect(listfish3.collision(player), false)
                && t.checkExpect(listfish4.collision(player), true)
                && t.checkExpect(listfish5.collision(player1), true);
    }

    boolean testCollider(Tester t) {
        return t.checkException(new IllegalStateException("no colliders in list"),
                    emptyfish, "collider", player)
                && t.checkException(new IllegalStateException("no colliders in list"),
                    listfish1, "collider", player)
                && t.checkException(new IllegalStateException("no colliders in list"),
                    listfish2, "collider", player)
                && t.checkExpect(listfish4.collider(player), fish6)
                && t.checkExpect(listfish5.collider(player), fish6)
                && t.checkExpect(listfish4.collider(player1), fish6)
                && t.checkExpect(listfish5.collider(player1), fish6);
    }
}