// Assignment 9
// Matuszak Caitlin
// cmatusza
// Radwan Ameen
//

import java.awt.Color;
import javalib.colors.Black;
import javalib.worldimages.*;
import tester.*;

/* EXTRA FEATURES IMPLEMENTED:
 * Press "p" to pause/unpause game
 * Multiplayer - move second player with a, s, d, w keys
 * Score - score in top corner counts number of times the player moves, lowers scores are better
 * (multiplayer scores are cumulative for both players, as they are working together to escape)
 */

// Examples and tests for the forbidden island game
class ExamplesForbiddenIsland {

    // Examples
    IList<Integer> mtloi = new Empty<Integer>();
    IList<Integer> loi1 = new Cons<Integer>(1, this.mtloi);
    IList<Integer> loi2 = new Cons<Integer>(3, new Cons<Integer>(2, this.loi1));

    Cell c1 = new Cell(3, 3, 3);
    Cell c2 = new Cell(-2, 2, 2);
    Cell c3 = new Cell(0, 1, 1);
    OceanCell ocean = new OceanCell(5, 5);
    OceanCell ocean2 = new OceanCell(4, 4);
    Player player1 = new Player(new Posn(10, 10));
    Player player2 = new Player(new Posn(2, 2));
    Part p1 = new Part(new Posn(1, 1));
    Part p2 = new Part(new Posn(3, 3));
    Helicopter h1 = new Helicopter(new Posn(3, 3));

    IPred<Cell> posnequal = new PosnEqual(new Posn(1, 1));
    IList<Cell> mtcell = new Empty<Cell>();
    IList<Cell> loc1 = new Cons<Cell>(this.c1, this.mtcell);
    IList<Cell> loc2 = new Cons<Cell>(this.c3, new Cons<Cell>(this.c2,
            this.loc1));

    WorldImage background = new FrameImage(new Posn(
            (int) ForbiddenIslandWorld.HALF_ISLAND_SIZE
                    * ForbiddenIslandWorld.PIXEL_SIZE,
            (int) ForbiddenIslandWorld.HALF_ISLAND_SIZE
                    * ForbiddenIslandWorld.PIXEL_SIZE),
            ForbiddenIslandWorld.ISLAND_SIZE * ForbiddenIslandWorld.PIXEL_SIZE,
            ForbiddenIslandWorld.ISLAND_SIZE * ForbiddenIslandWorld.PIXEL_SIZE,
            new Color(255, 255, 255));

    ForbiddenIslandWorld world1 = new ForbiddenIslandWorld();

    // EFFECT: initializes the data
    void initData() {
        c1 = new Cell(3, 3, 3);
        c2 = new Cell(-2, 2, 2);
        c3 = new Cell(0, 1, 1);
        ocean = new OceanCell(5, 5);
        ocean2 = new OceanCell(4, 4);
        player1 = new Player(new Posn(10, 10));
        player2 = new Player(new Posn(2, 2));

        posnequal = new PosnEqual(new Posn(1, 1));
        mtcell = new Empty<Cell>();
        loc1 = new Cons<Cell>(this.c1, this.mtcell);
        loc2 = new Cons<Cell>(this.c3, new Cons<Cell>(this.c2, this.loc1));
        
        world1 = new ForbiddenIslandWorld();
    }

    // tests for IList functions
    // test length
    void testLength(Tester t) {
        t.checkExpect(this.mtloi.length(), 0);
        t.checkExpect(this.loi1.length(), 1);
        t.checkExpect(this.loi2.length(), 3);
    }

    // test asCons
    void testAsCons(Tester t) {
        t.checkException(new ClassCastException("empty lists can't be cons"),
                this.mtloi, "asCons");
        t.checkExpect(this.loi1.asCons(), this.loi1);
        t.checkExpect(this.loi2.asCons(), this.loi2);
    }

    // test find
    void testFind(Tester t) {
        t.checkException(new RuntimeException("Object not in list"),
                this.mtcell, "find", this.posnequal);
        t.checkException(new RuntimeException("Object not in list"), this.loc1,
                "find", this.posnequal);
        t.checkExpect(this.loc2.find(this.posnequal), this.c3);
    }

    // test remove
    void testRemove(Tester t) {
        t.checkExpect(this.mtcell.remove(this.c1), this.mtloi);
        t.checkExpect(this.loc1.remove(this.c1), this.mtloi);
        t.checkExpect(this.loc1.remove(this.c2), this.loc1);
        t.checkExpect(this.loc2.remove(this.c3), new Cons<Cell>(this.c2,
                this.loc1));
    }

    // test iterator
    void testIterator(Tester t) {
        t.checkExpect(this.mtcell.iterator(), new ListIterator<Cell>(
                this.mtcell));
        t.checkExpect(this.loc1.iterator(), new ListIterator<Cell>(this.loc1));
        t.checkExpect(this.loi2.iterator(),
                new ListIterator<Integer>(this.loi2));
    }

    // test hasNext
    void testHasNext(Tester t) {
        t.checkExpect(new ListIterator<Cell>(this.mtcell).hasNext(), false);
        t.checkExpect(new ListIterator<Cell>(this.loc1).hasNext(), true);
        t.checkExpect(new ListIterator<Cell>(this.loc2).hasNext(), true);
    }

    // test next
    void testNext(Tester t) {
        t.checkExpect(new ListIterator<Cell>(this.loc1).next(), this.c1);
        t.checkExpect(new ListIterator<Cell>(this.loc2).next(), this.c3);
        t.checkExpect(new ListIterator<Integer>(this.loi1).next(), 1);
        t.checkExpect(new ListIterator<Integer>(this.loi2).next(), 3);
    }

    // test remove
    void testRemoveIterator(Tester t) {
        t.checkException(new UnsupportedOperationException(
                "Method not supported"), new ListIterator<Cell>(this.mtcell),
                "remove");
        t.checkException(new UnsupportedOperationException(
                "Method not supported"), new ListIterator<Cell>(this.loc1),
                "remove");
        t.checkException(new UnsupportedOperationException(
                "Method not supported"), new ListIterator<Cell>(this.loc2),
                "remove");
    }

    // test apply
    void testApply(Tester t) {
        t.checkExpect(this.posnequal.apply(this.c1), false);
        t.checkExpect(this.posnequal.apply(this.c2), false);
        t.checkExpect(this.posnequal.apply(this.c3), true);
    }

    // tests for cells

    // test updateFloodStatus
    void testUpdateFloodStatus(Tester t) {
        this.initData();

        this.world1.board = this.world1.array2IList(this.world1
                .double2Cell(this.world1.constructMountain()));

        Cell cell1 = this.world1.board.find(new PosnEqual(new Posn(32, 32)));
        t.checkExpect(cell1.isFlooded, false);
        cell1.updateFloodStatus(1);
        t.checkExpect(cell1.isFlooded, true);

        Cell cell2 = this.world1.board.find(new PosnEqual(new Posn(16, 16)));
        Cell cell3 = this.world1.board.find(new PosnEqual(new Posn(16, 17)));
        t.checkExpect(cell2.isFlooded, false);
        t.checkExpect(cell3.isFlooded, false);
        cell2.updateFloodStatus(3);
        t.checkExpect(cell2.isFlooded, true);
        t.checkExpect(cell3.isFlooded, true);

    }

    // test updateLeft
    void testUpdateLeft(Tester t) {
        this.initData();
        t.checkExpect(this.c1.left, null);
        this.c1.updateLeft(this.c2);
        t.checkExpect(this.c1.left, this.c2);
        t.checkExpect(this.c2.right, this.c1);

        t.checkExpect(this.c3.left, null);
        this.c3.updateLeft(this.c1);
        t.checkExpect(this.c3.left, this.c1);
        t.checkExpect(this.c1.right, this.c3);
    }

    // test updateTop
    void testUpdateTop(Tester t) {
        this.initData();
        t.checkExpect(this.c1.top, null);
        this.c1.updateTop(this.c2);
        t.checkExpect(this.c1.top, this.c2);
        t.checkExpect(this.c2.bottom, this.c1);

        t.checkExpect(this.c3.top, null);
        this.c3.updateTop(this.c1);
        t.checkExpect(this.c3.top, this.c1);
        t.checkExpect(this.c1.bottom, this.c3);
    }

    // test updateOnlyLeft
    void testUpdateOnlyLeft(Tester t) {
        this.initData();
        t.checkExpect(this.c1.left, null);
        this.c1.updateOnlyLeft(this.c2);
        t.checkExpect(this.c1.left, this.c2);
        t.checkExpect(this.c2.right, null);

        t.checkExpect(this.c3.left, null);
        this.c3.updateOnlyLeft(this.c1);
        t.checkExpect(this.c3.left, this.c1);
        t.checkExpect(this.c3.right, null);
    }

    // test updateRight
    void testUpdateRight(Tester t) {
        this.initData();
        t.checkExpect(this.c1.right, null);
        this.c1.updateRight(this.c2);
        t.checkExpect(this.c1.right, this.c2);

        t.checkExpect(this.c2.right, null);
        this.c2.updateRight(this.c3);
        t.checkExpect(this.c2.right, this.c3);
    }

    // test updateBottom
    void testUpdateBottom(Tester t) {
        this.initData();
        t.checkExpect(this.c1.bottom, null);
        this.c1.updateBottom(this.c2);
        t.checkExpect(this.c1.bottom, this.c2);

        t.checkExpect(this.c2.bottom, null);
        this.c2.updateBottom(this.c3);
        t.checkExpect(this.c2.bottom, this.c3);
    }

    // test drawCell
    void testDrawCell(Tester t) {
        this.initData();
        t.checkExpect(this.c1.drawCell(this.background, 0),
                this.background
                        .overlayImages(new RectangleImage(new Posn(3
                                * ForbiddenIslandWorld.PIXEL_SIZE
                                + ForbiddenIslandWorld.PIXEL_SIZE / 2, 3
                                * ForbiddenIslandWorld.PIXEL_SIZE
                                + ForbiddenIslandWorld.PIXEL_SIZE / 2),
                                ForbiddenIslandWorld.PIXEL_SIZE,
                                ForbiddenIslandWorld.PIXEL_SIZE, new Color(48,
                                        180, 48))));
        t.checkExpect(this.c2.drawCell(this.background, 0),
                this.background
                        .overlayImages(new RectangleImage(new Posn(2
                                * ForbiddenIslandWorld.PIXEL_SIZE
                                + ForbiddenIslandWorld.PIXEL_SIZE / 2, 2
                                * ForbiddenIslandWorld.PIXEL_SIZE
                                + ForbiddenIslandWorld.PIXEL_SIZE / 2),
                                ForbiddenIslandWorld.PIXEL_SIZE,
                                ForbiddenIslandWorld.PIXEL_SIZE, new Color(30,
                                        30, 167))));
        t.checkExpect(this.c3.drawCell(this.background, 0),
                this.background
                        .overlayImages(new RectangleImage(new Posn(1
                                * ForbiddenIslandWorld.PIXEL_SIZE
                                + ForbiddenIslandWorld.PIXEL_SIZE / 2, 1
                                * ForbiddenIslandWorld.PIXEL_SIZE
                                + ForbiddenIslandWorld.PIXEL_SIZE / 2),
                                ForbiddenIslandWorld.PIXEL_SIZE,
                                ForbiddenIslandWorld.PIXEL_SIZE, new Color(30,
                                        30, 200))));
    }

    // test surroundedByLand
    void testSurroundedByLand(Tester t) {
        this.initData();
        this.c1.updateBottom(this.c1);
        this.c1.updateOnlyLeft(this.c1);
        this.c1.updateOnlyTop(this.c1);
        this.c1.updateRight(this.c1);
        t.checkExpect(this.c1.surroundedByLand(), true);

        this.c1.updateBottom(this.c1);
        this.c1.updateOnlyLeft(this.c2);
        this.c1.updateOnlyTop(this.c1);
        this.c1.updateRight(this.c1);
        t.checkExpect(this.c1.surroundedByLand(), false);

        this.c3.updateBottom(this.c1);
        this.c3.updateOnlyLeft(this.c1);
        this.c3.updateOnlyTop(this.c1);
        this.c3.updateRight(this.c1);
        t.checkExpect(this.c3.surroundedByLand(), true);
    }

    // test drawOceanCell
    void testDrawOceanCell(Tester t) {
        t.checkExpect(this.ocean.drawCell(this.background, 0), background
                .overlayImages(new RectangleImage(new Posn(5
                        * ForbiddenIslandWorld.PIXEL_SIZE
                        + ForbiddenIslandWorld.PIXEL_SIZE / 2, 5
                        * ForbiddenIslandWorld.PIXEL_SIZE
                        + ForbiddenIslandWorld.PIXEL_SIZE / 2),
                        ForbiddenIslandWorld.PIXEL_SIZE,
                        ForbiddenIslandWorld.PIXEL_SIZE, new Color(0, 0, 200))));
        t.checkExpect(this.ocean2.drawCell(this.background, 0), background
                .overlayImages(new RectangleImage(new Posn(4
                        * ForbiddenIslandWorld.PIXEL_SIZE
                        + ForbiddenIslandWorld.PIXEL_SIZE / 2, 4
                        * ForbiddenIslandWorld.PIXEL_SIZE
                        + ForbiddenIslandWorld.PIXEL_SIZE / 2),
                        ForbiddenIslandWorld.PIXEL_SIZE,
                        ForbiddenIslandWorld.PIXEL_SIZE, new Color(0, 0, 200))));
    }

    // test drawPlayer
    void testDrawPlayer(Tester t) {
        this.initData();
        t.checkExpect(this.player1.drawPlayer(this.background, "pilot-icon.png"), background
                .overlayImages(new FromFileImage(new Posn(10
                        * ForbiddenIslandWorld.PIXEL_SIZE
                        + ForbiddenIslandWorld.PIXEL_SIZE / 2, 10
                        * ForbiddenIslandWorld.PIXEL_SIZE
                        + ForbiddenIslandWorld.PIXEL_SIZE / 2),
                        "pilot-icon.png")));
        t.checkExpect(this.player2.drawPlayer(this.background, "pilot-icon.png"), background
                .overlayImages(new FromFileImage(new Posn(2
                        * ForbiddenIslandWorld.PIXEL_SIZE
                        + ForbiddenIslandWorld.PIXEL_SIZE / 2, 2
                        * ForbiddenIslandWorld.PIXEL_SIZE
                        + ForbiddenIslandWorld.PIXEL_SIZE / 2),
                        "pilot-icon.png")));
    }

    // test move
    void testMove(Tester t) {
        this.initData();
        IList<Cell> board = this.world1.array2IList(this.world1
                .double2Cell(this.world1.constructMountain()));
        Player p = new Player(new Posn(32, 32));
        Player pl2 = new Player(new Posn(1, 1));

        t.checkExpect(p.move("left", board), new Player(new Posn(31, 32)));
        t.checkExpect(p.move("right", board), new Player(new Posn(33, 32)));
        t.checkExpect(p.move("up", board), new Player(new Posn(32, 31)));
        t.checkExpect(p.move("down", board), new Player(new Posn(32, 33)));
        t.checkExpect(pl2.move("left", board), pl2);
        t.checkExpect(pl2.move("right", board), pl2);
        t.checkExpect(pl2.move("up", board), pl2);
        t.checkExpect(pl2.move("down", board), pl2);

    }

    // test samePosition
    void testSamePosition(Tester t) {
        this.initData();
        t.checkExpect(this.player1.samePosition(new Posn(10, 10)), true);
        t.checkExpect(this.player1.samePosition(new Posn(1, 1)), false);
        t.checkExpect(this.player2.samePosition(new Posn(10, 10)), false);
        t.checkExpect(this.player2.samePosition(new Posn(2, 2)), true);

    }

    // test drawPart
    void testDrawPart(Tester t) {
        this.initData();
        t.checkExpect(this.p1.drawPart(this.background), background
                .overlayImages(new DiskImage(new Posn(1
                        * ForbiddenIslandWorld.PIXEL_SIZE
                        + ForbiddenIslandWorld.PIXEL_SIZE / 2, 1
                        * ForbiddenIslandWorld.PIXEL_SIZE
                        + ForbiddenIslandWorld.PIXEL_SIZE / 2),
                        ForbiddenIslandWorld.PIXEL_SIZE / 2, new Color(255, 0,
                                0))));
        t.checkExpect(this.p2.drawPart(this.background), background
                .overlayImages(new DiskImage(new Posn(3
                        * ForbiddenIslandWorld.PIXEL_SIZE
                        + ForbiddenIslandWorld.PIXEL_SIZE / 2, 3
                        * ForbiddenIslandWorld.PIXEL_SIZE
                        + ForbiddenIslandWorld.PIXEL_SIZE / 2),
                        ForbiddenIslandWorld.PIXEL_SIZE / 2, new Color(255, 0,
                                0))));

    }

    // test drawHelicopter
    void testDrawHelicopter(Tester t) {
        this.initData();
        t.checkExpect(this.h1.drawHelicopter(this.background), background
                .overlayImages(new FromFileImage(new Posn(3
                        * ForbiddenIslandWorld.PIXEL_SIZE
                        + ForbiddenIslandWorld.PIXEL_SIZE / 2, 3
                        * ForbiddenIslandWorld.PIXEL_SIZE
                        + ForbiddenIslandWorld.PIXEL_SIZE / 2),
                        "helicopter.png")));
    }

    // test construct mountain
    void testConstructMountainLength(Tester t) {
        t.checkExpect(world1.constructMountain().size(), 64);
        t.checkExpect(world1.constructRandomMountain().size(), 64);
        t.checkExpect(world1.double2Cell(world1.constructMountain()).size(), 64);
    }

    // test generateN
    void testGenerateN(Tester t) {
        this.initData();
        IList<Cell> board = this.world1.array2IList(this.world1
                .double2Cell(this.world1.constructMountain()));
        world1.board = board;

        t.checkExpect(world1.generateN(0), new Empty<Part>());
        t.checkExpect(world1.generateN(1).length(), 1);
        t.checkExpect(world1.generateN(3).length(), 3);
        t.checkExpect(world1.generateN(5).length(), 5);
        t.checkExpect(world1.generateN(7).length(), 7);
        t.checkExpect(world1.generateN(20).length(), 20);
    }

    // test onKeyEvent

    void testOnKeyEvent(Tester t) {
        this.initData();
        this.world1.player = new Player(new Posn(32, 32));
        this.world1.onKeyEvent("m");
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(0, 0)))
                .isOceanCell(), true);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(63, 0)))
                .isOceanCell(), true);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(0, 63)))
                .isOceanCell(), true);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(63, 63)))
                .isOceanCell(), true);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(31, 31)))
                .isOceanCell(), false);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(16, 16)))
                .isOceanCell(), false);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(16, 15)))
                .isOceanCell(), true);
        t.checkInexact(
                this.world1.board.find(new PosnEqual(new Posn(31, 31))).height,
                ForbiddenIslandWorld.HALF_ISLAND_SIZE - 1, 0.001);
        t.checkInexact(
                this.world1.board.find(new PosnEqual(new Posn(16, 16))).height,
                ForbiddenIslandWorld.HALF_ISLAND_SIZE - 31, 0.001);

        this.world1.onKeyEvent("r");
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(0, 0)))
                .isOceanCell(), true);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(63, 0)))
                .isOceanCell(), true);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(0, 63)))
                .isOceanCell(), true);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(63, 63)))
                .isOceanCell(), true);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(31, 31)))
                .isOceanCell(), false);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(16, 16)))
                .isOceanCell(), false);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(16, 15)))
                .isOceanCell(), true);
        t.checkRange(
                this.world1.board.find(new PosnEqual(new Posn(31, 31))).height,
                0.5, ForbiddenIslandWorld.HALF_ISLAND_SIZE - 1);
        t.checkRange(
                this.world1.board.find(new PosnEqual(new Posn(16, 16))).height,
                0.5, ForbiddenIslandWorld.HALF_ISLAND_SIZE - 1);

        this.world1.onKeyEvent("t");
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(0, 0)))
                .isOceanCell(), true);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(63, 0)))
                .isOceanCell(), true);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(0, 63)))
                .isOceanCell(), true);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(63, 63)))
                .isOceanCell(), true);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(31, 31)))
                .isOceanCell(), false);
        t.checkRange(
                this.world1.board.find(new PosnEqual(new Posn(31, 31))).height,
                0.5, ForbiddenIslandWorld.HALF_ISLAND_SIZE - 1);
        t.checkRange(
                this.world1.board.find(new PosnEqual(new Posn(31, 26))).height,
                0.5, ForbiddenIslandWorld.HALF_ISLAND_SIZE - 1);

        this.initData();
        this.world1.onKeyEvent("m");
        Player curplayer = this.world1.player;
        t.checkExpect(this.world1.score, 0);
        this.world1.onKeyEvent("left");
        t.checkExpect(this.world1.player, curplayer.move("left", world1.board));
        t.checkExpect(this.world1.score, 1);


        this.initData();
        this.world1.onKeyEvent("m");
        Player curplayer2 = this.world1.player;
        t.checkExpect(this.world1.score, 0);
        this.world1.onKeyEvent("right");
        t.checkExpect(this.world1.player,
                curplayer2.move("right", world1.board));
        t.checkExpect(this.world1.score, 1);

        this.initData();
        this.world1.onKeyEvent("m");
        Player curplayer3 = this.world1.player;
        t.checkExpect(this.world1.score, 0);
        this.world1.onKeyEvent("up");
        t.checkExpect(this.world1.player, curplayer3.move("up", world1.board));
        t.checkExpect(this.world1.score, 1);

        this.initData();
        this.world1.onKeyEvent("m");
        Player curplayer4 = this.world1.player;
        t.checkExpect(this.world1.score, 0);
        this.world1.onKeyEvent("down");
        t.checkExpect(this.world1.player, curplayer4.move("down", world1.board));
        t.checkExpect(this.world1.score, 1);
        
        this.initData();
        this.world1.onKeyEvent("m");
        Player curplayer5 = this.world1.player2;
        t.checkExpect(this.world1.score, 0);
        this.world1.onKeyEvent("s");
        t.checkExpect(this.world1.player2, curplayer5.move("down", world1.board));
        t.checkExpect(this.world1.score, 1);
        
        this.initData();
        this.world1.onKeyEvent("m");
        Player curplayer6 = this.world1.player2;
        t.checkExpect(this.world1.score, 0);
        this.world1.onKeyEvent("w");
        t.checkExpect(this.world1.player2, curplayer6.move("up", world1.board));
        t.checkExpect(this.world1.score, 1);
        
        this.initData();
        this.world1.onKeyEvent("m");
        Player curplayer7 = this.world1.player2;
        t.checkExpect(this.world1.score, 0);
        this.world1.onKeyEvent("a");
        t.checkExpect(this.world1.player2, curplayer7.move("left", world1.board));
        t.checkExpect(this.world1.score, 1);
        
        this.initData();
        this.world1.onKeyEvent("m");
        Player curplayer8 = this.world1.player2;
        t.checkExpect(this.world1.score, 0);
        this.world1.onKeyEvent("d");
        t.checkExpect(this.world1.player2, curplayer8.move("right", world1.board));
        t.checkExpect(this.world1.score, 1);
        
        t.checkExpect(this.world1.paused, false);
        this.world1.onKeyEvent("p");
        t.checkExpect(this.world1.paused, true);
    }

    // test onTick
    void testOnTick(Tester t) {
        this.initData();
        this.world1.paused = false;
        this.world1.onTick();
        t.checkExpect(this.world1.counter, 1);
        t.checkExpect(this.world1.waterHeight, 0);
        this.world1.counter = 5;
        this.world1.onTick();
        t.checkExpect(this.world1.counter, 6);
        t.checkExpect(this.world1.waterHeight, 0);
        this.world1.counter = 8;
        this.world1.onTick();
        t.checkExpect(this.world1.counter, 9);
        t.checkExpect(this.world1.waterHeight, 1);

        this.world1.board = this.world1.array2IList(this.world1
                .double2Cell(this.world1.constructMountain()));
        this.world1.counter = 8;
        this.world1.waterHeight = 0;
        t.checkExpect(
                this.world1.board.find(new PosnEqual(new Posn(16, 16))).isFlooded,
                false);
        this.world1.onTick();
        t.checkExpect(
                this.world1.board.find(new PosnEqual(new Posn(16, 16))).isFlooded,
                true);

    }

    // test worldEnds
    void testWorldEnds(Tester t) {
        this.initData();
        this.world1.board = this.world1.array2IList(this.world1
                .double2Cell(this.world1.constructMountain()));
        this.world1.player = new Player(new Posn(32, 32));
        this.world1.player2 = new Player(new Posn(32, 32));
        this.world1.parts = this.world1.generateN(4);
        t.checkExpect(this.world1.worldEnds(), new WorldEnd(false,
                new FrameImage(new Posn(
                        (int) ForbiddenIslandWorld.HALF_ISLAND_SIZE
                                * ForbiddenIslandWorld.PIXEL_SIZE,
                        (int) ForbiddenIslandWorld.HALF_ISLAND_SIZE
                                * ForbiddenIslandWorld.PIXEL_SIZE),
                        ForbiddenIslandWorld.ISLAND_SIZE
                                * ForbiddenIslandWorld.PIXEL_SIZE,
                        ForbiddenIslandWorld.ISLAND_SIZE
                                * ForbiddenIslandWorld.PIXEL_SIZE, new Color(
                                255, 255, 255))));
        this.world1.parts = new Empty<Part>();
        this.world1.helicopter = new Helicopter(new Posn(32, 32));
        t.checkExpect(
                this.world1.worldEnds(),
                new WorldEnd(true, new FrameImage(new Posn(
                        (int) ForbiddenIslandWorld.HALF_ISLAND_SIZE
                                * ForbiddenIslandWorld.PIXEL_SIZE,
                        (int) ForbiddenIslandWorld.HALF_ISLAND_SIZE
                                * ForbiddenIslandWorld.PIXEL_SIZE),
                        ForbiddenIslandWorld.ISLAND_SIZE
                                * ForbiddenIslandWorld.PIXEL_SIZE,
                        ForbiddenIslandWorld.ISLAND_SIZE
                                * ForbiddenIslandWorld.PIXEL_SIZE, new Color(
                                255, 255, 255)).overlayImages(new TextImage(
                        new Posn((int) ForbiddenIslandWorld.HALF_ISLAND_SIZE
                                * ForbiddenIslandWorld.PIXEL_SIZE,
                                (int) ForbiddenIslandWorld.HALF_ISLAND_SIZE
                                        * ForbiddenIslandWorld.PIXEL_SIZE),
                        "Congrats. You won!",
                        (int) ForbiddenIslandWorld.HALF_ISLAND_SIZE,
                        new Black()))));
        this.world1.player = new Player(new Posn(0, 0));
        t.checkExpect(
                this.world1.worldEnds(),
                new WorldEnd(true, new FrameImage(new Posn(
                        (int) ForbiddenIslandWorld.HALF_ISLAND_SIZE
                                * ForbiddenIslandWorld.PIXEL_SIZE,
                        (int) ForbiddenIslandWorld.HALF_ISLAND_SIZE
                                * ForbiddenIslandWorld.PIXEL_SIZE),
                        ForbiddenIslandWorld.ISLAND_SIZE
                                * ForbiddenIslandWorld.PIXEL_SIZE,
                        ForbiddenIslandWorld.ISLAND_SIZE
                                * ForbiddenIslandWorld.PIXEL_SIZE, new Color(
                                255, 255, 255)).overlayImages(new TextImage(
                        new Posn((int) ForbiddenIslandWorld.HALF_ISLAND_SIZE
                                * ForbiddenIslandWorld.PIXEL_SIZE,
                                (int) ForbiddenIslandWorld.HALF_ISLAND_SIZE
                                        * ForbiddenIslandWorld.PIXEL_SIZE),
                        "Oh no... You drowned!",
                        (int) ForbiddenIslandWorld.HALF_ISLAND_SIZE,
                        new Black()))));
    }

    // test makeImage
    void testMakeImage(Tester t) {
        this.initData();

        Cell cell1 = new Cell(-1, 2, 2);
        Cell cell2 = new Cell(0, 3, 3);
        Cell cell3 = new Cell(1, 4, 4);
        Cell cell4 = new Cell(2, 5, 5);

        this.world1.board = new Cons<Cell>(cell1,
                new Cons<Cell>(cell2, new Cons<Cell>(cell3, new Cons<Cell>(
                        cell4, new Empty<Cell>()))));
        this.world1.waterHeight = 0;

        WorldImage b1 = cell1.drawCell(this.background, 0);
        WorldImage b2 = cell2.drawCell(b1, 0);
        WorldImage b3 = cell3.drawCell(b2, 0);
        WorldImage b4 = cell4.drawCell(b3, 0);
        WorldImage b5 = this.world1.player.drawPlayer(b4, "pilot-icon.png");
        WorldImage b6 = this.world1.player2.drawPlayer(b5, "betterpilot.png");
        WorldImage b7 = this.world1.helicopter.drawHelicopter(b6);
        WorldImage b8 = b7.overlayImages(new TextImage(new Posn(ForbiddenIslandWorld.PIXEL_SIZE * 2, ForbiddenIslandWorld.PIXEL_SIZE * 2), 
                new Integer(this.world1.score).toString(), ForbiddenIslandWorld.PIXEL_SIZE * 2, new Color(255, 255, 255)));

        t.checkExpect(this.world1.makeImage(), b8);
    }

    // test constructMountain
    void testConstructMountain(Tester t) {
        this.initData();
        this.world1.onKeyEvent("m");
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(0, 0)))
                .isOceanCell(), true);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(63, 0)))
                .isOceanCell(), true);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(0, 63)))
                .isOceanCell(), true);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(63, 63)))
                .isOceanCell(), true);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(31, 31)))
                .isOceanCell(), false);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(16, 16)))
                .isOceanCell(), false);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(16, 15)))
                .isOceanCell(), true);
    }

    // test constructRandomMountain
    void testConstructRandomMountain(Tester t) {
        this.initData();
        this.world1.onKeyEvent("r");
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(0, 0)))
                .isOceanCell(), true);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(63, 0)))
                .isOceanCell(), true);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(0, 63)))
                .isOceanCell(), true);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(63, 63)))
                .isOceanCell(), true);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(31, 31)))
                .isOceanCell(), false);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(16, 16)))
                .isOceanCell(), false);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(16, 15)))
                .isOceanCell(), true);
        t.checkRange(
                this.world1.board.find(new PosnEqual(new Posn(31, 31))).height,
                0.5, ForbiddenIslandWorld.HALF_ISLAND_SIZE - 1);
        t.checkRange(
                this.world1.board.find(new PosnEqual(new Posn(16, 16))).height,
                0.5, ForbiddenIslandWorld.HALF_ISLAND_SIZE - 1);
    }

    // test constructRandomTerrain
    // this also sufficiently tests process terrain, as it is a helper function
    // with the majority of the functionality
    void testConstructRandomTerrain(Tester t) {
        this.initData();
        this.world1.onKeyEvent("t");
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(0, 0)))
                .isOceanCell(), true);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(63, 0)))
                .isOceanCell(), true);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(0, 63)))
                .isOceanCell(), true);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(63, 63)))
                .isOceanCell(), true);
        t.checkExpect(this.world1.board.find(new PosnEqual(new Posn(31, 31)))
                .isOceanCell(), false);
        t.checkRange(
                this.world1.board.find(new PosnEqual(new Posn(31, 31))).height,
                0.5, ForbiddenIslandWorld.HALF_ISLAND_SIZE - 1);
        t.checkRange(
                this.world1.board.find(new PosnEqual(new Posn(31, 26))).height,
                0.5, ForbiddenIslandWorld.HALF_ISLAND_SIZE - 1);

    }

    // test double2cell
    void testDouble2cell(Tester t) {
        this.initData();

        this.world1.board = this.world1.array2IList(this.world1
                .double2Cell(this.world1.constructMountain()));

        t.checkExpect(
                this.world1.board.find(new PosnEqual(new Posn(0, 0))).left.height,
                this.world1.board.find(new PosnEqual(new Posn(0, 0))).height);
        t.checkExpect(
                this.world1.board.find(new PosnEqual(new Posn(2, 0))).right.height,
                this.world1.board.find(new PosnEqual(new Posn(2, 0))).height);
        t.checkExpect(
                this.world1.board.find(new PosnEqual(new Posn(2, 0))).top.height,
                this.world1.board.find(new PosnEqual(new Posn(2, 0))).height);
        t.checkExpect(
                this.world1.board.find(new PosnEqual(new Posn(0, 2))).left.height,
                this.world1.board.find(new PosnEqual(new Posn(0, 2))).height);
        t.checkExpect(
                this.world1.board.find(new PosnEqual(new Posn(0, 2))).bottom.height,
                this.world1.board.find(new PosnEqual(new Posn(0, 2))).height);
        t.checkExpect(
                this.world1.board.find(new PosnEqual(new Posn(2, 2))).right.height,
                this.world1.board.find(new PosnEqual(new Posn(2, 2))).height);
        t.checkExpect(
                this.world1.board.find(new PosnEqual(new Posn(2, 2))).bottom.height,
                this.world1.board.find(new PosnEqual(new Posn(2, 2))).height);
        t.checkExpect(
                this.world1.board.find(new PosnEqual(new Posn(1, 1))).left.height,
                this.world1.board.find(new PosnEqual(new Posn(0, 1))).height);
        t.checkExpect(
                this.world1.board.find(new PosnEqual(new Posn(1, 1))).right.height,
                this.world1.board.find(new PosnEqual(new Posn(2, 1))).height);
        t.checkExpect(
                this.world1.board.find(new PosnEqual(new Posn(1, 1))).top.height,
                this.world1.board.find(new PosnEqual(new Posn(1, 0))).height);
        t.checkExpect(
                this.world1.board.find(new PosnEqual(new Posn(1, 1))).bottom.height,
                this.world1.board.find(new PosnEqual(new Posn(1, 2))).height);

    }

    // test array2IList
    void testArray2IList(Tester t) {
        this.initData();

        this.world1.board = this.world1.array2IList(this.world1
                .double2Cell(this.world1.constructMountain()));

        t.checkExpect(this.world1.board.length(), 64 * 64);
    }

    // runs the game
    void testGame(Tester t) {
        ForbiddenIslandWorld world2 = new ForbiddenIslandWorld();
        world2.onKeyEvent("m");
        world2.bigBang(ForbiddenIslandWorld.ISLAND_SIZE
                * ForbiddenIslandWorld.PIXEL_SIZE,
                ForbiddenIslandWorld.ISLAND_SIZE
                        * ForbiddenIslandWorld.PIXEL_SIZE, .1);

    }
    
 

}