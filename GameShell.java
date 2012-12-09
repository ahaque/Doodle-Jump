
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.sound.sampled.*;
import java.io.*;
import javax.swing.*;

public class GameShell extends Applet implements KeyListener, MouseListener, MouseMotionListener, Runnable {
    // #### MASTER SETTINGS #########################
    // DOODLE BOUNCE VELOCITY (SMALLER (negative) number for higher jumps)

    private int SDV = -9;
    // PLATFORM SCROLL DOWN SPEED (higher number to fall faster)
    private int BSDS = 5;
    // DOODLE SCROLL DOWN SPEED (higher number to fall faster)
    private int DSDS = 1;
    private int level = 0;
    // ################################################
    private int score = 0;
    // ArrayLists to store characters and images
    private ArrayList<Character> myGuys;
    private ArrayList<Image> myImages;
    private ArrayList<Character> myPlatforms;
    private ArrayList<Character> myBullets;
    private ArrayList<Character> myMonsters;
    private ArrayList<Person> people;
    // Objects for buffering
    private Graphics offScreenBuffer;
    private Image offScreenImage;
    private MediaTracker mt;
    private Image gridImg, topbar, bulletImg;
    private Image doodleRImg, doodleLImg, doodleSImg;
    private Image greenP, blueP, whiteP, dblueP, greenS0, greenS1;
    private Image brownP1, brownP2, brownP3, brownP4, brownP5, brownP6;
    private Image batM1, batM2, batM3;
    private Image intro0, intro1, intro3, scores0, scores1, gameover0, gameover1;
    private Image gridF, borderF, dblueF, greenF, whiteF, greenS0F, greenS1F;
    private Image brownF1, brownF2, brownF3, brownF4, brownF5, brownF6;
    private Character doodle;
    private boolean gameOver2 = false;
    private boolean nameEntered = false;
    private boolean FOREST_MODE = false;
    private int springCount = 0;
    // used in doodle left(-1) center(0) right(1) facing
    private int hFacing = 0;
    private int menuHoverOver = 0;
    private int scoresHoverOver = 0;
    private int gameOverHover = 0;
    private int creationCounter = 0;
    private Platform lastHitPlatform = new Platform(0, 0, 0, 0, 0);
    boolean shiftDown;
    boolean samePlatform;
    private Thread gameloop;
    private boolean menuOn = true;
    private boolean gameOver = false;
    private boolean gameOn = false;
    private boolean scoresOn = false;
    // AudioClip for midi files
    private AudioClip ac, acFall;
    private Clip clip;

    // called by Applet before beginning - 
    // give all attributes starting values here.
    public void init() {
        ac = getAudioClip(getDocumentBase(), "sounds/mystery.wav");
        acFall = getAudioClip(getDocumentBase(), "sounds/fall.wav");

        score = 0;

        // add keyboard listener
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        // prepares the double buffered image
        offScreenImage = createImage(450, 600);
        offScreenBuffer = offScreenImage.getGraphics();

        // set Font and Color in the offScreenBuffer


        // prepare images
        mt = new MediaTracker(this);
        gridImg = Toolkit.getDefaultToolkit().getImage("images/bg-grid.png");
        topbar = Toolkit.getDefaultToolkit().getImage("images/topbar.png");
        bulletImg = Toolkit.getDefaultToolkit().getImage("images/bullet.png");

        intro0 = Toolkit.getDefaultToolkit().getImage("images/menu/intro0.png");
        intro1 = Toolkit.getDefaultToolkit().getImage("images/menu/intro1.png");
        intro3 = Toolkit.getDefaultToolkit().getImage("images/menu/intro3.png");
        scores0 = Toolkit.getDefaultToolkit().getImage("images/menu/scores0.png");
        scores1 = Toolkit.getDefaultToolkit().getImage("images/menu/scores1.png");
        gameover0 = Toolkit.getDefaultToolkit().getImage("images/menu/gameover0.png");
        gameover1 = Toolkit.getDefaultToolkit().getImage("images/menu/gameover1.png");

        doodleRImg = Toolkit.getDefaultToolkit().getImage("images/doodleR.png");
        doodleLImg = Toolkit.getDefaultToolkit().getImage("images/doodleL.png");
        doodleSImg = Toolkit.getDefaultToolkit().getImage("images/doodleS.png");

        batM1 = Toolkit.getDefaultToolkit().getImage("images/monsters/bat1.png");
        batM2 = Toolkit.getDefaultToolkit().getImage("images/monsters/bat2.png");
        batM3 = Toolkit.getDefaultToolkit().getImage("images/monsters/bat3.png");

        greenS0 = Toolkit.getDefaultToolkit().getImage("images/p-green-s0.png");
        greenS1 = Toolkit.getDefaultToolkit().getImage("images/p-green-s1.png");
        greenP = Toolkit.getDefaultToolkit().getImage("images/p-green.png");
        blueP = Toolkit.getDefaultToolkit().getImage("images/p-blue.png");
        whiteP = Toolkit.getDefaultToolkit().getImage("images/p-white.png");
        dblueP = Toolkit.getDefaultToolkit().getImage("images/p-dblue.png");
        dblueP = Toolkit.getDefaultToolkit().getImage("images/p-dblue.png");

        // brown block animation
        brownP1 = Toolkit.getDefaultToolkit().getImage("images/brown/p-brown-1.png");
        brownP2 = Toolkit.getDefaultToolkit().getImage("images/brown/p-brown-2.png");
        brownP3 = Toolkit.getDefaultToolkit().getImage("images/brown/p-brown-3.png");
        brownP4 = Toolkit.getDefaultToolkit().getImage("images/brown/p-brown-4.png");
        brownP5 = Toolkit.getDefaultToolkit().getImage("images/brown/p-brown-5.png");
        brownP6 = Toolkit.getDefaultToolkit().getImage("images/brown/p-brown-6.png");

        gridF = Toolkit.getDefaultToolkit().getImage("images/forest/gridbg-f.png");
        borderF = Toolkit.getDefaultToolkit().getImage("images/forest/border-f.png");
        dblueF = Toolkit.getDefaultToolkit().getImage("images/forest/dblueF.png");
        greenF = Toolkit.getDefaultToolkit().getImage("images/forest/greenF.png");
        whiteF = Toolkit.getDefaultToolkit().getImage("images/forest/whiteF.png");
        greenS0F = Toolkit.getDefaultToolkit().getImage("images/forest/greenS0F.png");
        greenS1F = Toolkit.getDefaultToolkit().getImage("images/forest/greenS1F.png");

        brownF1 = Toolkit.getDefaultToolkit().getImage("images/forest/brownF1.png");
        brownF2 = Toolkit.getDefaultToolkit().getImage("images/forest/brownF2.png");
        brownF3 = Toolkit.getDefaultToolkit().getImage("images/forest/brownF3.png");
        brownF4 = Toolkit.getDefaultToolkit().getImage("images/forest/brownF4.png");
        brownF5 = Toolkit.getDefaultToolkit().getImage("images/forest/brownF5.png");
        brownF6 = Toolkit.getDefaultToolkit().getImage("images/forest/brownF6.png");


        myImages = new ArrayList();
        // add all images
        myImages.add(doodleRImg);// 0
        myImages.add(greenP);	// 1
        myImages.add(blueP);	// 2
        myImages.add(brownP1);	// 3
        myImages.add(brownP2);	// 4
        myImages.add(brownP3);	// 5
        myImages.add(brownP4);	// 6
        myImages.add(brownP5);	// 7
        myImages.add(brownP6);	// 8
        myImages.add(whiteP);	// 9
        myImages.add(dblueP);	// 10
        myImages.add(batM1);	// 11
        myImages.add(batM2);	// 12
        myImages.add(batM3);	// 13
        myImages.add(greenS0);	// 14
        myImages.add(greenS1);	// 15
        myImages.add(borderF);	// 16

        // load images to Media Tracker
        for (Image i : myImages) {
            mt.addImage(i, 0);
        }
        try {
            mt.waitForAll();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        // create main characters
        doodle = new Doodle(1, 190, 540, 60, 59);

        myGuys = new ArrayList<Character>();
        myGuys.add(doodle);

        myBullets = new ArrayList<Character>();

        myMonsters = new ArrayList<Character>();

        myPlatforms = new ArrayList<Character>();
        // generates first 12 random platforms
        for (int i = 0; i < 12; i++) {
            Platform plat1 = randomPlatform();
            myPlatforms.add(plat1);
        }

        int yp = 500;
        int xp = (int) (Math.random() * 400);

        myPlatforms.add(new Platform(1, xp, 500, 58, 15));
    }

    public Platform randomPlatform() {
        int yp = (int) (Math.random() * 450);
        int xp = (int) (Math.random() * 400);

        Platform plat1 = new Platform(1, xp, yp, 58, 15);
        return plat1;
    }

    public void start() {
        gameloop = new Thread(this);
        gameloop.start();
    }

    public void run() {
        // keep going as long as the thread is alive
        while (!gameOver2) {
            try {
                // speed of game - larger number makes game slower
                Thread.sleep(19);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            repaint();
        }
    }

    public void updatePlat2(int k, Character plat) {
        // manages horizontal moving platforms
        Platform tempPlat2 = (Platform) plat;

        int tempx1 = tempPlat2.getX();
        int tempv = tempPlat2.getHV();

        if (tempv == 2) {
            tempPlat2.changeX(tempv);

            if (tempx1 > 400) {
                tempPlat2.setHV(-2);
            }
        }

        if (tempv == -2) {
            tempPlat2.changeX(tempv);

            if (tempx1 < 0) {
                tempPlat2.setHV(2);
            }
        }

        myPlatforms.set(k, tempPlat2);
        offScreenBuffer.drawImage(myImages.get(tempPlat2.getId()), tempPlat2.getX(), tempPlat2.getY(), this);
    }

    public void updatePlat3(int k, Character plat) {
        // manages brown platforms, continues the animation by changing images
        Platform brownPlat = (Platform) plat;

        if (brownPlat.getBrownAnimation() == true) {
            if (brownPlat.getId() == 8) {
                myPlatforms.remove(k);
            }

            if (brownPlat.getId() < 8) {
                offScreenBuffer.drawImage(myImages.get(brownPlat.getId()), brownPlat.getX(), brownPlat.getY(), this);
                brownPlat.setId(plat.getId() + 1);
                myPlatforms.set(k, brownPlat);
            }
        }

        if (brownPlat.getBrownAnimation() == false) {
            offScreenBuffer.drawImage(myImages.get(brownPlat.getId()), brownPlat.getX(), brownPlat.getY(), this);
        }
    }

    public void updatePlat10(int k, Character plat) {
        // manages vertical moving platforms
        Platform tempPlat10 = (Platform) plat;

        int tempy1 = tempPlat10.getY();
        int tempv = tempPlat10.getVV();
        int vcount = tempPlat10.getVcount();

        if (tempv == -1) {
            if (vcount >= 100) {
                tempPlat10.setVV(1);
            }

            if (vcount < 100) {
                tempPlat10.changeY(tempv);
                tempPlat10.setVcount(tempPlat10.getVcount() + 1);
            }
        }

        if (tempv == 1) {
            if (vcount <= -100) {
                tempPlat10.setVV(-1);
            }

            if (vcount > -100) {
                tempPlat10.changeY(tempv);
                tempPlat10.setVcount(tempPlat10.getVcount() - 1);
            }
        }

        myPlatforms.set(k, tempPlat10);
        offScreenBuffer.drawImage(myImages.get(tempPlat10.getId()), tempPlat10.getX(), tempPlat10.getY(), this);
    }

    public void updateMonster(int w, Character mon) {
        Monster tempMonster = (Monster) mon;

        int tempy = tempMonster.getY();
        int tempx = tempMonster.getX();

        int temph = tempMonster.getHV();
        int tempv = tempMonster.getVV();
        int vcount = tempMonster.getVcount();
        int hcount = tempMonster.getHcount();

        int mid = tempMonster.getId();

        switch (mid) {
            case 11:
                tempMonster.setId(12);
                break;
            case 12:
                tempMonster.setId(13);
                break;
            case 13:
                tempMonster.setId(11);
                break;
        }

        // VERTICAL MOVEMENT			
        if (tempv == -1) {
            if (vcount >= 25) {
                tempMonster.setVV(1);
            }

            if (vcount < 25) {
                tempMonster.changeY(tempv);
                tempMonster.setVcount(tempMonster.getVcount() + 1);
            }
        }

        if (tempv == 1) {
            if (vcount <= -25) {
                tempMonster.setVV(-1);
            }

            if (vcount > -25) {
                tempMonster.changeY(tempv);
                tempMonster.setVcount(tempMonster.getVcount() - 1);
            }
        }

        // HORIZONTAL MOVEMENT
        if (temph == -1) {
            if (hcount >= 60) {
                tempMonster.setHV(1);
            }

            if (hcount < 60) {
                tempMonster.changeX(temph);
                tempMonster.setHcount(tempMonster.getHcount() + 1);
            }
        }

        if (temph == 1) {
            if (hcount <= -60) {
                tempMonster.setHV(-1);
            }

            if (hcount > -60) {
                tempMonster.changeX(temph);
                tempMonster.setHcount(tempMonster.getHcount() - 1);
            }
        }

        if ((shiftDown == true) && (samePlatform == false)) {
            tempMonster.changeY(BSDS);
        }

        myMonsters.set(w, tempMonster);
        offScreenBuffer.drawImage(myImages.get(tempMonster.getId()), tempMonster.getX(), tempMonster.getY(), this);

        if (tempMonster.getY() > 610) {
            myMonsters.remove(w);
        }
    }

    public void readScores() {
        people = new ArrayList<Person>();

        try {
            FileReader fr = new FileReader("scores.dat");
            BufferedReader br = new BufferedReader(fr);
            String s;
            while ((s = br.readLine()) != null) {
                int num = Integer.parseInt(br.readLine());
                Person per = new Person(s, num);
                people.add(per);
            }
            fr.close();
        } catch (IOException e) {
        }

        // sort scores

        ArrayList sorted = new ArrayList<Person>();

        Person highest;
        Person current;

        while (people.size() > 0) {
            int lowidx = 0;
            highest = people.get(0);

            for (int i = 1; i < people.size(); i++) {
                current = people.get(i);

                if (current.getScore() > highest.getScore()) {
                    highest = current;
                    lowidx = i;
                }
            }

            sorted.add(highest);
            people.remove(lowidx);
        }

        people = sorted;

    }

    public void drawScores() {
        int yi = 45;

        // read from arraylist and draw on screen
        for (int i = 0; i < people.size(); i++) {
            Person temp = people.get(i);

            int newY = 166 + (yi * i);

            offScreenBuffer.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
            offScreenBuffer.drawString(temp.getName(), 85, newY);
            offScreenBuffer.drawString("" + temp.getScore(), 350, newY);
        }

    }

    public void calculateScore() {
        readScores();

        if (score > people.get(5).getScore()) {

            String name = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

            name = JOptionPane.showInputDialog(null, "You have gotten a new high score!\nYour Score: " + score + "\nPlease enter your name below:", "High Score", JOptionPane.INFORMATION_MESSAGE);

            if (name == null) {
                name = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
            }

            while ((name.length() > 20) || (name.length() == 0)) {
                name = JOptionPane.showInputDialog(null, "You have gotten a new high score!\nYour Score: " + score + "\nPlease enter your name below:\n\nMust be less than 20 characters.", "High Score", JOptionPane.INFORMATION_MESSAGE);
                if (name == null) {
                    name = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
                }
            }

            // write score to file
            Person per = new Person(name, score);

            people.add(per);

            //add new hiscore to end
            //keep moving up the hiscore table until it belongs

            for (int k = people.size() - 1; k > 0; k--) {
                Person current = people.get(k);

                if (per.getScore() > current.getScore()) {
                    // swap 2
                    Person temp = current;
                    people.set(k, per);
                    people.set(k - 1, temp);
                }
            }
            // keep array size at 6
            people.remove(6);

        }

    }
    private int fCount = 0;

    // one step of game - draw to buffer and displays all at the end
    public void update(Graphics g) {
        if ((gameOver == true) && (fCount == 0)) {
            //plays "fall" sound effect - game over
            acFall.play();
            fCount = 1;
        }

        // if on main menu
        if (menuOn == true) {
            if (menuHoverOver == 0) {
                offScreenBuffer.drawImage(intro0, 0, 0, this);
            }
            if (menuHoverOver == 1) {
                offScreenBuffer.drawImage(intro1, 0, 0, this);
            }
            if (menuHoverOver == 3) {
                offScreenBuffer.drawImage(intro3, 0, 0, this);
            }
        }

        // game over screen
        if (gameOver == true) {
            if (gameOverHover == 0) {
                offScreenBuffer.drawImage(gameover0, 0, 0, this);
            }
            if (gameOverHover == 1) {
                offScreenBuffer.drawImage(gameover1, 0, 0, this);
            }

            offScreenBuffer.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
            offScreenBuffer.drawString("" + score, 246, 412);

            if (nameEntered == false) {
                nameEntered = true;
                calculateScore();
            }
        }

        // scores
        if (scoresOn == true) {
            if (scoresHoverOver == 0) {
                offScreenBuffer.drawImage(scores0, 0, 0, this);
            }
            if (scoresHoverOver == 1) {
                offScreenBuffer.drawImage(scores1, 0, 0, this);
            }

            drawScores();
        }

        if (gameOn == true) {
            //draw background
            if (FOREST_MODE == false) {
                offScreenBuffer.drawImage(gridImg, 0, 0, this);
            }

            if (FOREST_MODE == true) {
                offScreenBuffer.drawImage(gridF, 0, 0, this);
            }

            Doodle tempDoodle = (Doodle) myGuys.get(0);

            if (springCount < 1) {
                SDV = -9;
                BSDS = 5;
                DSDS = 1;
            }

            if (springCount > 0) {
                springCount--;
            }

            // if doodle is moving up
            shiftDown = false;

            if (tempDoodle.getVelocity() < 0) {
                shiftDown = true;
            }

            for (int k = 0; k < myPlatforms.size(); k++) {
                // cycle through platforms and draw
                Character tempPlatform = (Platform) myPlatforms.get(k);

                // #############################################################
                // performs action for different platforms
                // light blue - horizontal scroll
                if (tempPlatform.getId() == 2) {
                    updatePlat2(k, tempPlatform);
                }

                // brown
                if ((tempPlatform.getId() >= 3) && (tempPlatform.getId() <= 9)) {
                    updatePlat3(k, tempPlatform);
                }

                // dark blue - vertical scroll
                if (tempPlatform.getId() == 10) {
                    updatePlat10(k, tempPlatform);
                } // if its a normal platform
                else {
                    offScreenBuffer.drawImage(myImages.get(tempPlatform.getId()), tempPlatform.getX(), tempPlatform.getY(), this);
                }

                // move platform down if doodle is moving up
                if ((shiftDown == true) && (samePlatform == false)) {
                    tempPlatform.changeY(BSDS);
                    score = score + 1;
                }

                // if platform moves off bottom of screen, create new platform
                if (tempPlatform.getY() > 400) {
                    //interval for every Y to create new platform
                    if (creationCounter > ((int) (Math.random() * 7) + 5)) {
                        generateLiveRandomPlatform();
                        creationCounter = 0;
                    }
                }

                if (tempPlatform.getY() > 650) {
                    myPlatforms.remove(k);
                    //	generateLiveRandomPlatform();
                }
            }

            if (myPlatforms.size() < 13) {
                generateLiveRandomPlatform();
            }

            for (int w = 0; w < myMonsters.size(); w++) {
                updateMonster(w, myMonsters.get(w));
            }

            for (int h = 0; h < myBullets.size(); h++) {
                Bullet tempBullet = (Bullet) myBullets.get(h);
                tempBullet.move();

                if ((shiftDown == true) && (samePlatform == false)) {
                    tempBullet.changeY(BSDS);
                }

                myBullets.set(h, tempBullet);
                offScreenBuffer.drawImage(bulletImg, tempBullet.getX(), tempBullet.getY(), this);

                if ((tempBullet.getX() > 450) || (tempBullet.getX() < 0) || (tempBullet.getY() < 0) || (tempBullet.getY() > 600)) {
                    myBullets.remove(h);
                }

            }

            // draw doodle, last character
            offScreenBuffer.drawImage(myImages.get(tempDoodle.show()), tempDoodle.getX(), tempDoodle.getY(), this);
            tempDoodle.move();
            if ((shiftDown == true) && (samePlatform == false)) {
                tempDoodle.changeY(DSDS + 2);
                creationCounter++;
            }

            //draw top bar then score on top
            offScreenBuffer.drawImage(topbar, 0, 0, this);

            if (FOREST_MODE == true) {
                offScreenBuffer.drawImage(borderF, 0, 0, this);
            }

            offScreenBuffer.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
            offScreenBuffer.drawString("" + score, 20, 28);
            offScreenBuffer.drawString("Level: " + level, 350, 28);

            // draw the buffer LAST LAST
            checkPlatformHit();
            checkBulletHit();
            checkDoodleGameOver();

        }
        g.drawImage(offScreenImage, 0, 0, this);
    }

    public void checkDoodleGameOver() {
        Doodle dod = (Doodle) myGuys.get(0);

        // checks if below screen
        if (dod.getY() > 620) {
            gameOver = true;
            gameOn = false;
        }

        // checks if hits a monster
        for (int k = 0; k < myMonsters.size(); k++) {
            Character mon = myMonsters.get(k);

            if (dod.getVelocity() < 0) {
                if (mon.equals(dod)) {
                    gameOver = true;
                    gameOn = false;
                }
            }

        }

    }

    public void generateLiveRandomPlatform() {
        int color = 1;

        // processes levels
        if (score >= 40000) {
            level = 5;
        }

        if ((score > 30000) && (score <= 40000)) {
            level = 4;
        }

        if ((score > 20000) && (score <= 30000)) {
            level = 3;
        }

        if ((score > 10000) && (score <= 20000)) {
            level = 2;
        }

        if ((score > 5000) && (score <= 10000)) {
            level = 1;
        }

        if (score <= 5000) {
            level = 0;
        }

        color = (int) (Math.random() * 110) + 1;

        if (level == 0) {
            color = 1;
        }

        int xp = (int) (Math.random() * 400);
        int yp = (int) (Math.random() * 10);
        yp = yp * -1;

        // green
        if ((color > 0) && (color <= 50)) {
            // makes the game harder, less green platforms
            if (level == 2) {
                color = (int) (Math.random() * 99) + 1;
            } else if (level == 3) {
                color = (int) (Math.random() * 70) + 30;
            } else if (level == 4) {
                color = (int) (Math.random() * 63) + 45;
            } else if (level == 5) {
                color = (int) (Math.random() * 63) + 45;
            }

            if ((color > 0) && (color <= 50)) {
                Platform plat1 = new Platform(1, xp, yp, 58, 15);
                myPlatforms.add(plat1);
            }
        }

        // light blue LR
        if ((color > 50) && (color <= 60) && (level > 1)) {
            Platform plat2 = new Platform(2, xp, yp, 56, 16);
            myPlatforms.add(plat2);
        }

        if ((color > 50) && (color <= 60) && (level < 2)) {
            color = 62;
        }

        //brown
        if ((color > 60) && (color <= 70)) {
            // makes sure there are not 2 brown in a row
            if (myPlatforms.get(myPlatforms.size() - 1).getId() == 3) {
                Platform plat1 = new Platform(1, xp, yp, 58, 15);
                myPlatforms.add(plat1);
            } else {
                Platform plat3 = new Platform(3, xp, yp, 68, 20);
                myPlatforms.add(plat3);
            }
        }

        //white
        if ((color > 70) && (color <= 80)) {
            if (level == 5) {
                color = (int) (Math.random() * 63) + 45;
            }

            if ((color > 70) && (color <= 80)) {
                Platform plat9 = new Platform(9, xp, yp, 58, 15);
                myPlatforms.add(plat9);
            }

        }

        // dark blue - vertical scroll
        if ((color > 90) && (color <= 100)) {
            Platform plat10 = new Platform(10, xp, yp, 57, 15);
            myPlatforms.add(plat10);
        }

        if ((color > 100) && (color <= 105)) {
            Doodle dod = (Doodle) myGuys.get(0);

            if (level >= 2) {
                if (springCount < 1) {
                    if (dod.getY() > 200) {
                        if (myMonsters.size() < 1) {
                            generateMonster();
                        }
                    }
                }
            }

            Platform plat1 = new Platform(1, xp, yp, 58, 15);
            myPlatforms.add(plat1);
        }


        if ((color > 105) && (color <= 108)) {
            Platform plat1 = new Platform(14, xp, yp, 57, 23);
            myPlatforms.add(plat1);
        }

    }

    public void generateMonster() {
        int monX = (int) (Math.random() * 300);
        int monY = (int) (Math.random() * 20);

        Monster mon = new Monster(11, monX, monY, 120, 67);
        myMonsters.add(mon);
    }

    public void checkBulletHit() {
    try{
        //checks if each bullet hits a monster
        for (int a = 0; a < myMonsters.size(); a++) {
            if (myMonsters.size() > 0) {
                for (int k = 0; k < myBullets.size(); k++) {
                    if (myBullets.get(k).equals(myMonsters.get(a))) {
                        myMonsters.remove(a);
                        myBullets.remove(k);
                        score = score + 500;
                    }
                }
            }
        }
    }catch(IndexOutOfBoundsException e){}
    }

    public void checkPlatformHit() {
        Doodle doodle1 = (Doodle) myGuys.get(0);

        for (int a = 0; a < myPlatforms.size(); a++) {
            // only compare if doodle is falling, ignore if bouncing up
            if (doodle1.getVelocity() > 0) {
                // if a doodle hits a platform
                if (doodle1.checkHitPlatform(myPlatforms.get(a))) {
                    Platform hitPlat = (Platform) myPlatforms.get(a);

                    // if doodle hits a brown platform, play animation and skip the "Hit jump"
                    if ((hitPlat.getId() >= 3) && (hitPlat.getId() < 9)) {
                        Platform newBrown = (Platform) myPlatforms.get(a);
                        newBrown.setBrownAnimation(true);
                        myPlatforms.set(a, newBrown);
                    } // if doodle hits a spring platform
                    else if (hitPlat.getId() == 14) {
                        Platform launch = (Platform) myPlatforms.get(a);
                        launch.setId(15);
                        myPlatforms.set(a, launch);

                        SDV = -20;
                        BSDS = 18;
                        DSDS = 3;
                        springCount = 50;
                    } // if it is any other platform
                    else {
                        Platform t2 = (Platform) myPlatforms.get(a);

                        Doodle temp = (Doodle) myGuys.get(0);
                        temp.setVelocity(SDV);
                        myGuys.set(0, temp);

                        if (hitPlat.getId() == 9) {
                            myPlatforms.remove(a);
                        }

                        // if doodle stays on same platform, dont move platforms down					
                        if ((lastHitPlatform.getX() != t2.getX()) && (temp.getY() < lastHitPlatform.getY())) {
                            score = score + 100;
                            samePlatform = false;
                        } else {
                            samePlatform = true;
                        }

                        //System.out.println("oldx: "+lastHitPlatform.getX()+" thisx: "+t2.getX()+" sameplatform: "+samePlatform);
                        lastHitPlatform = (Platform) myPlatforms.get(a);
                    }

                }
            }
        }

        for (int r = 0; r < myMonsters.size(); r++) {
            if (doodle1.getVelocity() > 0) {
                if (doodle1.equals(myMonsters.get(r))) {
                    Doodle temp = (Doodle) myGuys.get(0);
                    temp.setVelocity(SDV);
                    myGuys.set(0, temp);
                    score = score + 100;
                    myMonsters.remove(r);
                }
            }
        }

    }

    public void wavRunner(String s) {
        try {
            File file = new File(s);
            AudioInputStream audiosource = AudioSystem.getAudioInputStream(file);
            DataLine.Info info = new DataLine.Info(Clip.class, audiosource.getFormat());
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(audiosource);
        } catch (UnsupportedAudioFileException e) {
        } catch (LineUnavailableException e) {
        } catch (IOException e) {
        }
        clip.loop(0);
    }

    public void calculateLR(int x) {
        Doodle doodle2 = (Doodle) myGuys.get(0);
        int doodleX = doodle2.getX();

        // if already facing left, dont do anything, same with right
        if ((x < doodleX)) {
            myImages.set(0, doodleLImg);
            doodle2.setX(doodle2.getX());
            hFacing = -1;
            doodle2.setHFacing(-1);
        } else if ((x > doodleX + doodle2.getWidth()) && (hFacing < 1)) {
            myImages.set(0, doodleRImg);
            doodle2.setX(doodle2.getX());
            hFacing = 1;
            doodle2.setHFacing(1);
        } else if (hFacing != 0) {
            hFacing = 0;
            doodle2.setHFacing(0);
        }

        myGuys.set(0, doodle2);
    }

    public void createBullet(int mx, int my) {
        // #############################################################
        // THIS IS WHERE ALL THE MATH MAGIC HAPPENS FOR BULLETS
        Doodle doodle = (Doodle) myGuys.get(0);

        int dx = doodle.getX() + 16;
        int dy = doodle.getY() - 15;
        int dw = doodle.getWidth() / 2;

        Bullet bnew = new Bullet(11, dx, dy, 10, 11);

        /* make a triangle, find base leg and height, find hypotnouse
        reduce the base and height to smaller numbers for less velocity
        move the bullet using base leg and height to travel at angle
         */

        int triangleLeg = Math.abs(mx) - Math.abs(dx);
        int triangleHeight = Math.abs(my) - Math.abs(dy);

        int hypo = (int) Math.sqrt(Math.pow(triangleLeg, 2) + Math.pow(triangleHeight, 2));

        int numMoves = (int) hypo / 10;

        int legStep = (int) (mx - dx) / 10;
        int heightStep = (int) (my - dy) / 10;

        // minimum bullet speed is 6
        if ((legStep > -6) && (legStep < 0)) {
            legStep = -6;
        }
        if ((legStep < 6) && (legStep > 0)) {
            legStep = 6;
        }

        if ((heightStep > -6) && (heightStep < 0)) {
            heightStep = -6;
        }
        if ((heightStep < 6) && (heightStep > 0)) {
            heightStep = 6;
        }

        // tries to keep speed consistent
        while (Math.abs(legStep) > 10) {
            if (heightStep != 0) {
                heightStep = heightStep / 2;
            }
            if (legStep != 0) {
                legStep = legStep / 2;
            }
        }

        while (Math.abs(heightStep) > 10) {
            if (heightStep != 0) {
                heightStep = heightStep / 2;
            }
            if (legStep != 0) {
                legStep = legStep / 2;
            }
        }

        //	System.out.println("Legstep: "+legStep+ " Heightstep: "+heightStep);

        bnew.setLegStep(legStep);
        bnew.setHeightStep(heightStep);

        myBullets.add(bnew);

    }

    public void resetGame() {
        // resets all variables to start new game
        Doodle doodle1 = new Doodle(1, 190, 540, 60, 59);
        myGuys.set(0, doodle1);
        score = 0;
        level = 0;
        nameEntered = false;

        myBullets = new ArrayList<Character>();
        myMonsters = new ArrayList<Character>();
        myPlatforms = new ArrayList<Character>();

        spaceCount = 0;
        fCount = 0;

        for (int i = 0; i < 12; i++) {
            Platform plat1 = randomPlatform();
            myPlatforms.add(plat1);
        }

        int yp = 500;
        int xp = (int) (Math.random() * 400);

        myPlatforms.add(new Platform(1, xp, 500, 58, 15));
    }

    public void overrideLR(int f) {
        // for keyboard input
        Doodle doodle2 = (Doodle) myGuys.get(0);
        doodle2.setHFacing(f);

        // set image L and R
        if (f == -1) {
            myImages.set(0, doodleLImg);
            doodle2.setX(doodle2.getX());
        } else if (f == 1) {
            myImages.set(0, doodleRImg);
            doodle2.setX(doodle2.getX());
        }

        myGuys.set(0, doodle2);
    }

    public void paint(Graphics g) {
        update(g);
    }

    // key listeners
    public void mousePressed(MouseEvent me) {
        //	System.out.println("X: "+me.getX()+ " | Y: "+me.getY());
        if (gameOn == true) {
            myImages.set(0, doodleSImg);
            if (myBullets.size() < 5) {
                createBullet(me.getX(), me.getY());
            }
        }

        if (menuOn == true) {
            if ((me.getX() >= 70) && (me.getX() <= 230)) {
                if ((me.getY() >= 160) && (me.getY() <= 210)) {
                    menuOn = false;
                    gameOn = true;
                    gameOver = false;
                    resetGame();
                }
            }

            if ((me.getX() >= 280) && (me.getX() <= 400)) {
                if ((me.getY() >= 400) && (me.getY() <= 450)) {
                    scoresOn = true;
                    menuOn = false;
                    gameOver = false;
                    readScores();
                }
            }
        }

        if (scoresOn == true) {
            if ((me.getX() >= 250) && (me.getX() <= 400)) {
                if ((me.getY() >= 520) && (me.getY() <= 570)) {
                    scoresOn = false;
                    menuOn = true;
                    gameOver = false;
                    scoresHoverOver = 0;
                    menuHoverOver = 0;
                }
            }
        }

        if (gameOver == true) {
            if ((me.getX() >= 68) && (me.getX() <= 225)) {
                if ((me.getY() >= 155) && (me.getY() <= 208)) {
                    gameOver = false;
                    menuOn = true;
                    scoresOn = false;
                    scoresHoverOver = 0;
                    menuHoverOver = 0;
                }
            }
        }
    }

    public void mouseReleased(MouseEvent me) {
        if (gameOn == true) {
            myImages.set(0, doodleRImg);
        }
    }

    public void mouseEntered(MouseEvent me) {
    }

    public void mouseExited(MouseEvent me) {
    }

    public void mouseClicked(MouseEvent me) {
    }

    // mouse motion listeners
    public void mouseMoved(MouseEvent me) {
        if (menuOn == true) {
            if ((me.getX() >= 70) && (me.getX() <= 230)) {
                if ((me.getY() >= 160) && (me.getY() <= 210)) {
                    menuHoverOver = 1;
                }
            } else if ((me.getX() >= 280) && (me.getX() <= 400)) {
                if ((me.getY() >= 400) && (me.getY() <= 450)) {
                    menuHoverOver = 3;
                }
            } else {
                menuHoverOver = 0;
            }
        } else if (scoresOn == true) {
            if ((me.getX() >= 250) && (me.getX() <= 400)) {
                if ((me.getY() >= 520) && (me.getY() <= 570)) {
                    scoresHoverOver = 1;
                }
            } else {
                scoresHoverOver = 0;
            }
        } else if (gameOver == true) {
            if ((me.getX() >= 68) && (me.getX() <= 225)) {
                if ((me.getY() >= 155) && (me.getY() <= 208)) {
                    gameOverHover = 1;
                }
            } else {
                gameOverHover = 0;
            }
        }
    }

    public void mouseDragged(MouseEvent me) {
        //	calculateLR(me.getX());
    }

    // keyboard listeners
    public void keyReleased(KeyEvent e) {
        // set back to 0
        if (gameOn == true) {
            if ((e.getKeyCode() == 37) || (e.getKeyCode() == (39))) {
                overrideLR(0);
            }
        }
    }

    public void keyTyped(KeyEvent e) {
    }
    private int spaceCount;

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case 32: {
                //space key
                if (spaceCount == 0) {
                    samePlatform = true;
                    Doodle temp = (Doodle) myGuys.get(0);
                    temp.setVelocity(SDV);
                    myGuys.set(0, temp);
                    spaceCount = 1;
                }
                break;
            }

            case 49: {
                //1 key
                score = 5000;
                break;
            }

            case 50: {
                //2 key
                score = 10000;
                break;
            }

            case 51: {
                //3 key
                score = 20000;
                break;
            }

            case 52: {
                //4 key
                score = 30000;
                break;
            }

            case 53: {
                //5 key
                score = 40000;
                break;
            }

            case 70: {
                //F key

                Doodle temp = (Doodle) myGuys.get(0);
                temp.setVelocity(SDV);
                myGuys.set(0, temp);
            }

            break;
            case 37:
                // left arrow
                overrideLR(-1);

                break;
            case 38:
                /*   //up arrow 
                {
                Doodle temp = (Doodle) myGuys.get(0);
                temp.setVelocity(SDV);
                myGuys.set(0,temp);
                } */


                break;
            case 39:
                //right arrow
                overrideLR(1);

                break;
            case 40:
                //down arrow 

                break;

            // escape key, return to main menu
            case 27: {
                gameOn = false;
                menuOn = true;
                scoresOn = false;
                gameOver = false;
                break;
            }


            case 112: { // F1
                if (menuOn == true) {
                    JOptionPane.showMessageDialog(null, "Controls\n+ Arrow keys to move\n+ F2 to toggle music\n+ F3 to toggle JUNGLE MODE\n+ ESC to return to menu\n+ Click to shoot\n+ Press SPACE to start jumping\n\n"
                            + "Hints\n+ Don't fall off the screen or hit a monster\n+ You can jump off the side of the\n    screen and appear on the other side\n+ You can bounce on top of monsters\n+ Springs make you jump really high\n+ Try to get the highest score\n\n", "Doodle Jump", JOptionPane.INFORMATION_MESSAGE);
                }
                break;
            }

            case 113: {
                // F2
                ac.play();
                break;
            }

            case 114: {
                // F3
                if (FOREST_MODE == false) {
                    FOREST_MODE = true;
                    myImages.set(10, dblueF);
                    myImages.set(1, greenF);
                    myImages.set(2, dblueF);
                    myImages.set(9, whiteF);
                    myImages.set(14, greenS0F);
                    myImages.set(15, greenS1F);
                    myImages.set(3, brownF1);
                    myImages.set(4, brownF2);
                    myImages.set(5, brownF3);
                    myImages.set(6, brownF4);
                    myImages.set(7, brownF5);
                    myImages.set(8, brownF6);
                } // make images original
                else if (FOREST_MODE == true) {
                    myImages.set(10, dblueP);
                    myImages.set(1, greenP);
                    myImages.set(2, blueP);
                    myImages.set(9, whiteP);
                    myImages.set(14, greenS0);
                    myImages.set(15, greenS1);
                    myImages.set(3, brownP1);
                    myImages.set(4, brownP2);
                    myImages.set(5, brownP3);
                    myImages.set(6, brownP4);
                    myImages.set(7, brownP5);
                    myImages.set(8, brownP6);
                    FOREST_MODE = false;
                }

                break;
            }
        }
    }
}
