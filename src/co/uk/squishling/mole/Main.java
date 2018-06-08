package co.uk.squishling.mole;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import co.uk.squishling.mole.objects.MoleHill;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Main extends Application {

	private Pane root;
	private Scene scene;
	private Canvas canvas;
	
	private double WIDTH;
	private double HEIGHT;
	
	private static float score = 600;
	private static float survived = 0;
	private static int moles = 0;
	private int bestMoles = 0;
	
	private boolean menu = true;
	private boolean drawn = false;
	private boolean updated = false;
	
	private ArrayList<MoleHill> moleHills = new ArrayList<MoleHill>();
	
	private static double round (double value, int precision) {
	    int scale = (int) Math.pow(10, precision);
	    return (double) Math.round(value * scale) / scale;
	}
	
	public void start(Stage stage) {
		try {
			root = new Pane();
			
			scene = new Scene(root, 1280, 720);
			
			stage.setScene(scene);
			stage.setTitle("Holey Moley!");
			stage.setFullScreen(true);
			stage.setResizable(false);
			stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
			
			stage.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent e) -> {
		        if (KeyCode.ESCAPE == e.getCode()) {
		            stage.close();
		        }
		    });
			
			stage.getIcons().add(new Image("/icon.png"));
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		try {
			Scanner scanner = new Scanner(new File("hiscore.txt"));
			bestMoles = Integer.parseInt(scanner.useDelimiter("\\Z").next());
			scanner.close();
		} catch(Exception e) {
			
		}
		
		WIDTH = root.getWidth();
		HEIGHT = root.getHeight();
		
		moleHills.add(new MoleHill(WIDTH / 6, (HEIGHT / 4) * 3, WIDTH / 25.6));
		moleHills.add(new MoleHill((WIDTH / 6) * 2, (HEIGHT / 4) * 3, WIDTH / 25.6));
		moleHills.add(new MoleHill((WIDTH / 6) * 3, (HEIGHT / 4) * 3, WIDTH / 25.6));
		moleHills.add(new MoleHill((WIDTH / 6) * 4, (HEIGHT / 4) * 3, WIDTH / 25.6));
		moleHills.add(new MoleHill((WIDTH / 6) * 5, (HEIGHT / 4) * 3, WIDTH / 25.6));
		
		for (int i = 0; i < moleHills.size(); i++) {
			moleHills.get(i).setIsMole(true);
		}
		
		canvas = new Canvas(scene.getWidth(), scene.getHeight());
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		final long startNanoTime = System.nanoTime();
		 
	    new AnimationTimer()
	    {
	        public void handle(long currentNanoTime)
	        {
	            double t = (currentNanoTime - startNanoTime) / 1000000000.0;
	            
	            update();
	            draw(gc);
	        }
	    }.start();
		
		canvas.setOnMouseClicked((MouseEvent e) -> {
			if (menu) {
				menu = false;
				survived = 0;
				score = 600;
				moles = 0;
				drawn = false;
				
				for (int i = 0; i < moleHills.size(); i++) {
					moleHills.get(i).setIsMole(false);
	    		}
			} else {
				if (score > 0) {
					for (int i = 0; i < moleHills.size(); i++) {
						if (moleHills.get(i).clicked(e.getX(), e.getY())) {
							moleHills.get(i).setIsMole(false);
							score += 60;
							moles++;
						}
		    		}
				} else {
					drawn = false;
					updated = false;
					menu = true;

					for (int i = 0; i < moleHills.size(); i++) {
						moleHills.get(i).setIsMole(true);
		    		}
				}
			}
		});
		
		root.getChildren().add(canvas);
	}
	
	public void draw(GraphicsContext gc) {
		if (menu && !drawn) {
			background(gc);
			
			gc.setFill(Color.BLACK);
        	gc.setGlobalAlpha(0.9);
        	gc.fillRect(0, 0, WIDTH, HEIGHT);
        	gc.setGlobalAlpha(1);
        	
        	gc.setFill(Color.WHITE);
        	gc.setFont(new Font("Arial", 200));
        	gc.setTextAlign(TextAlignment.CENTER);
        	gc.setTextBaseline(VPos.CENTER);
    		gc.fillText("Holey Moley!", WIDTH / 2, (HEIGHT / 2) - (HEIGHT / 12));
    		
    		gc.setFont(new Font("Arial", 100));
    		gc.fillText("HIGH SCORE: " + String.valueOf(bestMoles), WIDTH / 2, (HEIGHT / 2) + (HEIGHT / 12));
    		
    		gc.setFont(new Font("Arial", 50));
    		gc.setTextBaseline(VPos.BASELINE);
    		gc.fillText("Click to continue", WIDTH / 2, HEIGHT - 50);
    		
    		gc.setFont(new Font("Arial", 20));
    		gc.setTextAlign(TextAlignment.LEFT);
    		gc.fillText("© Squishling 2018            https://squishling.co.uk/", 10, HEIGHT - 10);
    		
    		gc.setTextAlign(TextAlignment.RIGHT);
    		gc.fillText("Press ESC to exit", WIDTH - 10, HEIGHT - 10);
    		
    		drawn = true;
		} else if (!menu && score > 0) {
			background(gc);
    		
    		gc.setFill(Color.RED);
    		gc.setFont(new Font("Arial", HEIGHT / 5));
    		gc.setTextAlign(TextAlignment.CENTER);
    		gc.fillText(round((score / 60), 1) + "s", WIDTH / 2, (HEIGHT / 2) - (HEIGHT / 30));
    		
    		gc.setFill(Color.WHITE);
    		gc.setFont(new Font("Arial", HEIGHT / 8));
    		gc.fillText(round((survived / 60), 1) + "s", WIDTH / 2, (HEIGHT / 2) - (HEIGHT / 5));
    		
    		gc.setFill(Color.BLACK);
    		gc.setFont(new Font("Arial", HEIGHT / 16));
    		gc.fillText(moles + " moles hit", WIDTH / 2, (HEIGHT / 2) - ((HEIGHT / 5) * 1.5));
    		
    		gc.setFont(new Font("Arial", 20));
    		gc.setTextAlign(TextAlignment.LEFT);
    		gc.fillText("© Squishling 2018            https://squishling.co.uk/", 10, HEIGHT - 10);
    		
    		gc.setTextAlign(TextAlignment.RIGHT);
    		gc.fillText("Press ESC to exit", WIDTH - 10, HEIGHT - 10);
		} else if (!menu && !drawn) {
			background(gc);
			
			gc.setFill(Color.BLACK);
        	gc.setGlobalAlpha(0.9);
        	gc.fillRect(0, 0, WIDTH, HEIGHT);
        	gc.setGlobalAlpha(1);
        	
        	gc.setFill(Color.WHITE);
        	gc.setFont(new Font("Arial", 200));
        	gc.setTextAlign(TextAlignment.CENTER);
    		gc.fillText("You Lost!", WIDTH / 2, HEIGHT / 2);
    		
    		gc.setFont(new Font("Arial", 75));
    		if (moles > bestMoles) {
        		gc.fillText("NEW HIGH SCORE: " + String.valueOf(moles), WIDTH / 2, (HEIGHT / 2) + 75);
    		} else {
    			gc.fillText("HIGH SCORE: " + String.valueOf(bestMoles), WIDTH / 2, (HEIGHT / 2) + 75);
    		}
    		
    		gc.setFont(new Font("Arial", 50));
    		gc.fillText("You survived " + (int) round((survived / 60), 1) + "s, hitting " + moles + " moles", WIDTH / 2, (HEIGHT / 2) + 125);
    		
    		gc.fillText("Click to continue", WIDTH / 2, HEIGHT - 50);
    		
    		gc.setFont(new Font("Arial", 20));
    		gc.setTextAlign(TextAlignment.LEFT);
    		gc.fillText("© Squishling 2018            https://squishling.co.uk/", 10, HEIGHT - 10);
    		
    		gc.setTextAlign(TextAlignment.RIGHT);
    		gc.fillText("Press ESC to exit", WIDTH - 10, HEIGHT - 10);
    		
    		drawn = true;
		}
	}
	
	public void background(GraphicsContext gc) {
		gc.setFill(Color.AQUA);
		gc.fillRect(0, 0, WIDTH, HEIGHT);
		
		gc.setFill(Color.GREENYELLOW);
		gc.fillRect(0, HEIGHT - ((HEIGHT) / 2), WIDTH, HEIGHT / 2);
		
		for (int i = 0; i < moleHills.size(); i++) {
			moleHills.get(i).draw(gc);
		}
	}
	
	public void update() {
		if (!menu && score > 0) {
			score--;
	    	survived++;
	    	
	    	for (int i = 0; i < moleHills.size(); i++) {
	    		if (ThreadLocalRandom.current().nextInt(1, 181) == 5) {
	    			moleHills.get(i).setIsMole(!moleHills.get(i).getIsMole());
	    		}
			}
		} else if(!updated) {
			if (moles > bestMoles) {
				bestMoles = moles;
				
				Writer wr;
				try {
					wr = new FileWriter("hiscore.txt");
					wr.write(String.valueOf(moles));
					wr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			updated = true;
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
