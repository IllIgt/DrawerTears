package DrawerTears;

import javafx.scene.input.MouseButton;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.security.PrivateKey;

public class GameWindow extends JFrame {

    private static GameWindow game_window;
    private static long last_frame_time;
    private static Image Background;
    private static Image GO;
    private static Image drop;
    private static Image dead_kaplya;
    private static Image cooldrop;
    private static Image evildrop;
    private static Image goodbadandugly;
    private static boolean dklifecircle;
    private static boolean iteration_circle = true;
    private static float dkleft;
    private static float dktop;
    private static float drop_left = 200;
    private static float drop_top = -100;
    private static float drop_v = 150;
    private static int score = 0;

    public static void main(String[] args) throws IOException {

        Background = ImageIO.read(GameWindow.class.getResourceAsStream("Background.jpg"));
        GO = ImageIO.read(GameWindow.class.getResourceAsStream("GO.png"));
        drop = ImageIO.read(GameWindow.class.getResourceAsStream("kaplya.png"));
        cooldrop = ImageIO.read(GameWindow.class.getResourceAsStream("cooldrop.png"));
        evildrop = ImageIO.read(GameWindow.class.getResourceAsStream("evildrop.png"));
        dead_kaplya = ImageIO.read(GameWindow.class.getResourceAsStream("dead_kaplya.png"));
        game_window = new GameWindow();
        game_window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game_window.setLocation(200, 100);
        game_window.setSize(906, 478);
        game_window.setResizable(false);
        last_frame_time = System.nanoTime();
        GameField game_field = new GameField();
        game_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                float drop_right = drop_left + kaplya().getWidth(null);
                float drop_bottom = drop_top + kaplya().getHeight(null);
                boolean is_drop = x >= drop_left && x <= drop_right && y >= drop_top && y <= drop_bottom;
                if (is_drop) {
                    if (kaplya() == cooldrop){
                        drop_v = drop_v - 10;
                        score = score + 9;
                    }
                    if (kaplya() == evildrop){
                        drop_v = drop_v + 25;
                        score = score -11;
                    }
                    dkleft = drop_left;
                    dktop = drop_top;
                    iteration_circle = true;
                    dklifecircle = false;
                    drop_top = -100;
                    drop_left = (int) (Math.random() * (game_field.getWidth() - kaplya().getWidth(null)));
                    if (drop_v <= 0) drop_v = 5;
                    drop_v = drop_v + 5;
                    score++;
                    if (score <= 0) score = 0;
                    game_window.setTitle("Score: " + score);

                    dklifecircle = true;
                    iteration_circle = false;
                }
            }
        });

        game_window.add(game_field);
        game_window.setVisible(true);
    }

    public static Image kaplya() {


        while (iteration_circle == true ) {
            goodbadandugly = drop;
            if (score > 10) {
                if (Math.random() * 10 <= 2) {
                    goodbadandugly = evildrop;

                } else if(Math.random() * 10 >= 8) {
                    goodbadandugly = cooldrop;
                }else goodbadandugly = drop;


            }return goodbadandugly;

        } return goodbadandugly;
    }


    private static void onRepaint (Graphics g){

        long current_time = System.nanoTime();
        float delta_time = (current_time - last_frame_time) * 0.000000001f;
        last_frame_time = current_time;
        drop_top = drop_top + drop_v * delta_time;
        g.drawImage (Background,0,0,null);
        g.drawImage(kaplya(), (int) drop_left,(int)drop_top,null);
        if (drop_top > game_window.getHeight() && (kaplya() != evildrop || kaplya() == cooldrop)) g.drawImage (GO, 280, 100, null);
        if (drop_top > game_window.getHeight() && (kaplya() == evildrop || kaplya() == cooldrop)) {

            drop_top = -100;

            drop_left = (int) (Math.random() * (game_window.getWidth() - kaplya().getWidth(null)));
            iteration_circle = false;
            if (Math.random() * 10 <= 2) {
                goodbadandugly = evildrop;

            } else if(Math.random() * 10 >= 8) {
                goodbadandugly = cooldrop;
            }else goodbadandugly = drop;
        }
//    	if (dklifecircle == true)
//            g.drawImage(dead_kaplya,(int) dkleft, (int) dktop, null );



    }

    private static class GameField extends JPanel{

        @Override
        protected void paintComponent (Graphics g) {
            super.paintComponent(g);
            onRepaint(g);
            repaint ();
        }
    }
}
