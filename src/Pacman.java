import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

public class Pacman extends JFrame implements ActionListener {

	public static boolean isDead = false;
	
	private final int WIDTH = 750;
	private final int HEIGHT = 470;
	private final int BORDER_THICKNESS = 10;
	private final Color BACKGROUND_COLOR = Color.BLACK;
	private final Color BORDER_COLOR = Color.RED;
	private final Color OBSTACLE_COLOR = Color.BLUE;
	private final Color BAIT_COLOR = Color.YELLOW;
	private final int squareheads=0;//If you make this something between 5-20 head of ghosts will be round
	private JButton button;
	private JButton againButton;

	private Timer timer = new Timer();
	
	private ArrayList<GObject> borders;
	private ArrayList<GObject> obstacles;
	private ArrayList<GObject> baits;
	private ArrayList<GObject> ghosts;

	private int point = 0; 
	private int remainingTime = 500;
	private JLabel remainingTimeLabel;
	private JLabel pointLabel;
	
	JPanel gui = new JPanel(new BorderLayout(1, 1));
	
	public Pacman() {
		borders = new ArrayList<>();
		obstacles = new ArrayList<>();
		baits = new ArrayList<>();
		ghosts= new ArrayList<>();

		setBorders();
		setObstacles();
		setBaits();
		setGhosts(4);

		//JPanel gui = new JPanel(new BorderLayout(1, 1));
		button=new JButton("Next Level");
		button.setBackground(Color.YELLOW);
		button.setEnabled(true);
		button.setSize(100,100);
		button.setVisible(true);
		button.setBounds(300,50,100,100);
		button.addActionListener(this);
		
		againButton=new JButton("Try Again");
		againButton.setBackground(Color.YELLOW);
		againButton.setEnabled(true);
		againButton.setSize(100,100);
		againButton.setVisible(true);
		againButton.setBounds(300,330,100,100);
		againButton.addActionListener(this);
		
		JToolBar tools = new JToolBar();
		tools.setFloatable(false);
		
		remainingTimeLabel = new JLabel("Remaining time : " + remainingTime/100);
		tools.add(remainingTimeLabel);
		tools.addSeparator();
		pointLabel = new JLabel("Your point : " + point);
		tools.add(pointLabel);
		
		gui.add(tools, BorderLayout.PAGE_START);
		gui.add(new GamePanel());

		this.setTitle("Pacman");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(new Dimension(WIDTH, HEIGHT + 50));
		this.setLocationRelativeTo(null);
		// this.getContentPane().setLayout();

		this.getContentPane().add(gui);
		this.setVisible(true);
		setResizable(false);
		
		
        timer.schedule(new TimerTask() {
            @Override
    		public void run() {
				remainingTime--;
				repaint();
				if (remainingTime == 0) {
					

						gui.removeAll();
						JPanel border = new JPanel(new BorderLayout());
						JLabel label = new JLabel("<html>Level 1 is finished! Your point is : " + point + "<br/> Are you ready for level 2 ? </html>", SwingConstants.CENTER);
						label.setFont(new Font("", 0, 24));
						border.add(button);
						border.add(againButton);

						
						border.add(label, BorderLayout.CENTER);
						gui.add(border);	
						
						timer.cancel();

				}
				remainingTimeLabel.setText("Remaining time : " + remainingTime/25);
				revalidate();

			}
        }, 100, 50);
	}

	private void setBorders() {
		borders.add(new GObject(0, 0, WIDTH, BORDER_THICKNESS)); // Upper
		borders.add(new GObject(0, 0, BORDER_THICKNESS, HEIGHT)); // Left
		borders.add(new GObject(WIDTH - BORDER_THICKNESS, 0, BORDER_THICKNESS, HEIGHT)); // Right
		borders.add(new GObject(0, HEIGHT - BORDER_THICKNESS, WIDTH, BORDER_THICKNESS)); // Bottom
	}

	private void setObstacles() {
		int obstacleThickness = 20;

		// Üst yatay
		//		newObstacles.add(new GObject(BORDER_THICKNESS, 60, 150, obstacleThickness));
		//		newObstacles.add(new GObject(300, 60, 150, obstacleThickness));
		//		newObstacles.add(new GObject(600, 60, 150 - BORDER_THICKNESS, obstacleThickness));

		// üst dikeyler
		obstacles.add(new GObject(200, BORDER_THICKNESS, obstacleThickness, 120 - BORDER_THICKNESS));
		obstacles.add(new GObject(520, BORDER_THICKNESS, obstacleThickness, 120 - BORDER_THICKNESS));
		//		newObstacles.add(new GObject(520, BORDER_THICKNESS, obstacleThickness, 80 - BORDER_THICKNESS));
		//
		//		// Ortadaki |_| ve içindeki |
		obstacles.add(new GObject(365, 50, obstacleThickness, 100));
		obstacles.add(new GObject(290, 130, obstacleThickness, 120));
		obstacles.add(new GObject(290, 130, 150 + obstacleThickness, obstacleThickness));
		obstacles.add(new GObject(440, 130, obstacleThickness, 120));
		//
		obstacles.add(new GObject(65, 200, 120, obstacleThickness));
		obstacles.add(new GObject(65, 80, obstacleThickness, 120));

		obstacles.add(new GObject(65, 280, 120, obstacleThickness)); // L
		obstacles.add(new GObject(65, 280, obstacleThickness, 120)); // L

		obstacles.add(new GObject(290, 320, 150 + obstacleThickness, obstacleThickness));
		obstacles.add(new GObject(365, 320, obstacleThickness, 100)); // T

		obstacles.add(new GObject(150, 400, 120, obstacleThickness)); // ters T
		obstacles.add(new GObject(200, 330, obstacleThickness, 80 - BORDER_THICKNESS));

		obstacles.add(new GObject(470, 400, 120, obstacleThickness)); // ters T
		obstacles.add(new GObject(520, 330, obstacleThickness, 80 - BORDER_THICKNESS));

		obstacles.add(new GObject(550, 280, 120, obstacleThickness)); // L
		obstacles.add(new GObject(670, 280, obstacleThickness, 120)); // L

		obstacles.add(new GObject(550, 200, 140, obstacleThickness)); // L
		obstacles.add(new GObject(670, 80, obstacleThickness, 120)); // L
	}

	private void setBaits() {
		int baitWidth = 8;
		int baitHeight = 8;
		int powerWidth = 20;
		int powerHeight = 20;
		ArrayList<GObject> allObjects = new ArrayList<>();
		allObjects.addAll(obstacles);
		allObjects.addAll(borders);

		for (int i = 0; i * 5 < HEIGHT; i++) {
			for (int j = 0; j * 5 < WIDTH; j++) {
				if (i <= 10 && j <= 10) {
					continue;
				}
				// %5 ihtimal
				if (Math.random() < 0.06) {
					GObject bait = new GObject(j * 5 - 5, i * 5 - 5, baitWidth + 10, baitHeight + 10);
					boolean intersects = false;
					for (GObject gObject : allObjects) {
						intersects = intersects(gObject, bait);
						if (intersects) {
							break;
						}
					}
					if (intersects == false) {
						bait = new GObject(j * 5, i * 5, baitWidth, baitHeight);
						allObjects.add(bait);
						baits.add(bait);
					}
				}
			}
		}
		for (int i = 0; i * 5 < HEIGHT; i++) {
			for (int j = 0; j * 5 < WIDTH; j++) {
				if (i <= 10 && j <= 10) {
					continue;
				}
				// %5 ihtimal
				if (Math.random() < 0.008) {
					GObject bait = new GObject(j * 5 - 5, i * 5 - 5, powerWidth + 10, powerHeight + 10);
					boolean intersects = false;
					for (GObject gObject : allObjects) {
						intersects = intersects(gObject, bait);
						if (intersects) {
							break;
						}
					}
					if (intersects == false) {
						bait = new GObject(j * 5, i * 5, powerWidth, powerHeight);
						allObjects.add(bait);
						baits.add(bait);
					
					}
				
					
				}
			}
		}

	}
	
	private void setGhosts(int number) {
		ghosts.add(new GObject(WIDTH/2,  HEIGHT-250, 20, 20));
		ghosts.add(new GObject(HEIGHT/10,  WIDTH/2, 20, 20));
		ghosts.add(new GObject(WIDTH-50,  HEIGHT-50, 20, 20));
		ghosts.add(new GObject(WIDTH/4,  HEIGHT/2, 20, 20));
	}


	private boolean intersects(GObject first, GObject second) {
		// checks X axis
		if (first.getX() > second.getX() - first.getWidth() && first.getX() < second.getX() + second.getWidth()) {
			// checks Y axis
			if (first.getY() > second.getY() - first.getHeight() && first.getY() < second.getY() + second.getHeight()) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		new Pacman();
		playPacmanMusic();
		
	}

	private enum Direction {
		RIGHT, LEFT, UP, DOWN, RELEASE;
	}

	public class GamePanel extends JPanel {
		private Image pmImage;
		private int imageX = 20;
		private int imageY = 20;
		private final int IMAGE_WIDTH = 30;
		private final int IMAGE_HEIGHT = 30;

		private Direction direction = Direction.RELEASE;

		public GamePanel() {
			loadImage("right");
			this.setFocusable(true);
			addKeyListener(new GameInput()); // Add it to the JPanel
		}

		//
		public void loadImage(String url) {
			pmImage = Toolkit.getDefaultToolkit().getImage(url + ".gif");		    
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			setBackground(BACKGROUND_COLOR);

			Graphics2D g2d = (Graphics2D) g;

			g2d.setColor(BORDER_COLOR);
			for (GObject border : borders) {
				g2d.fillRect(border.getX(), border.getY(), border.getWidth(), border.getHeight());
			}

			g2d.setColor(OBSTACLE_COLOR);
			for (GObject obstacle : obstacles) {
				g2d.fillRect(obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight());
			}

			g2d.setColor(BAIT_COLOR);
			for (GObject bait : baits) {
				g2d.fillOval(bait.getX(), bait.getY(), bait.getWidth(), bait.getHeight());
			}
			
			
			for (GObject ghost : ghosts) {
				
				Random rand = new Random();
				float red = rand.nextFloat();
				float green = rand.nextFloat();
				float blue = rand.nextFloat();
				g2d.setColor(new Color(red, green, blue));	
				g2d.fillRoundRect(ghost.getX(), ghost.getY(), ghost.getWidth(), ghost.getHeight(),squareheads,squareheads);
				g2d.setColor(Color.WHITE);
				g2d.fillRect(ghost.getX()+5, ghost.getY()+5, 2, 2);//Left Eye
				g2d.fillRect(ghost.getX()+15, ghost.getY()+5, 2, 2);//Right Eye
				g2d.setColor(BACKGROUND_COLOR);//Ghost legs or whatever :P				
				g2d.fillOval(ghost.getX(), ghost.getY()+15, 4, 5);
				g2d.fillOval(ghost.getX()+5, ghost.getY()+15, 4, 5);
				g2d.fillOval(ghost.getX()+10, ghost.getY()+15, 4, 5);
				g2d.fillOval(ghost.getX()+15, ghost.getY()+15, 4, 5);				
			}
			

			switch (direction) {
			case RIGHT:
				loadImage("right");
				if (hits(new GObject(imageX + 2, imageY, IMAGE_WIDTH, IMAGE_HEIGHT)) == false) {
					imageX += 2;
				}
				if (remainingTime%2==0) {
	            	loadImage("full");            	
	            }
				break;
			case LEFT:
				loadImage("left");
				if (hits(new GObject(imageX - 2, imageY, IMAGE_WIDTH, IMAGE_HEIGHT)) == false) {
					imageX -= 2;
				}
				if (remainingTime%2==0) {
	            	loadImage("full");            	
	            }
				break;
			case UP:
				loadImage("up");
				if (hits(new GObject(imageX, imageY - 2, IMAGE_WIDTH, IMAGE_HEIGHT)) == false) {
					imageY -= 2;
				}
				if (remainingTime%2==0) {
	            	loadImage("full");            	
	            }
				break;
			case DOWN:
				loadImage("down");
				if (hits(new GObject(imageX, imageY + 2, IMAGE_WIDTH, IMAGE_HEIGHT)) == false) {
					imageY += 2;
				}
				if (remainingTime%2==0) {
	            	loadImage("full");            	
	            }
				break;
			default:
				break;
			}
			
			
			g.drawImage(pmImage, imageX, imageY, this);

			Iterator<GObject> iter = baits.iterator();
			while (iter.hasNext()) {
				GObject bait = iter.next();
				if (intersects(bait, new GObject(imageX - 2, imageY - 2, IMAGE_WIDTH - 4, IMAGE_HEIGHT - 4))) {
					iter.remove(); // Removes the 'current' item
					Chomp();
					if(bait.height==20) {
						point +=2;
					}
					else {
						point ++;
					}
				}
				pointLabel.setText("Your point : " + point);  
			}
			
		}

		private boolean hits(GObject imgObj) {
			for (GObject border : borders) {
				if (intersects(border, imgObj)) {
					return true;
				}
			}
			for (GObject obstacle : obstacles) {
				if (intersects(obstacle, imgObj)) {
					return true;
				}
			}
			return false;
		}

		private class GameInput implements KeyListener {
			boolean pressed = false;
			long delay = 10;
			
			public GameInput() {
				Thread thread = new Thread() {
					public void run() {
						while (true) {
							if (pressed) {
								repaint();
							}							
							try {
								Thread.sleep(delay);								
							} catch (Exception e2) {
								e2.getStackTrace();
							}
													
							
							for (GObject ghost : ghosts) {
								Random rnd=new Random();								
								int tempx=rnd.nextInt(6)-3;								
								int tempy=rnd.nextInt(6)-3;	
								
								
								if(imageX-ghost.x>0) {
									tempx++;
								}else {
									tempx--;
								}
								
								if(imageY-ghost.y>0) {
									tempy++;
								}else {
									tempy--;
								}
								
								ghost.x+=tempx;
								ghost.y+=tempy;
								
								//if there is a wall change back the movement of ghost
								if(hits(ghost)){
									ghost.x-=tempx;
									ghost.y-=tempy;
								}
								
								//Checks if any ghost hits pacman
								
								if(intersects(ghost,new GObject(imageX, imageY, IMAGE_WIDTH, IMAGE_HEIGHT))) {
									remainingTime=1;
									//die();
									//isDead = true;

								
								
								}								
								
							}
							
							
							
						}
					}
				};
				thread.start();
			}

			public void keyTyped(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
				
				switch (e.getKeyCode()) {
				case KeyEvent.VK_DOWN:
					direction = Direction.DOWN;
					break;
				case KeyEvent.VK_UP:
					direction = Direction.UP;
					break;
				case KeyEvent.VK_RIGHT:
					direction = Direction.RIGHT;
					break;
				case KeyEvent.VK_LEFT:
					direction = Direction.LEFT;
					break;
				default:
					direction = Direction.RELEASE;
					break;
				}

				pressed = true;
			}

			public void keyReleased(KeyEvent e) {
				direction = Direction.RELEASE;
				pressed = false;
			}
		}
	}

	private class GObject {
		private int x, y, width, height;

		public GObject(int x, int y, int width, int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}

		@Override
		public String toString() {
			return "GObject [x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + "]";
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==button) {
			Level2 newlevel=new Level2();

			playPacmanMusic();

		}
		if(e.getSource()==againButton) {
			new Pacman();

		}
}
	public static void playPacmanMusic() {
	    try {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("C:/ibrahim/JAVA/newPacman/pacman_beginning.wav").getAbsoluteFile());
	        Clip pacmanclip = AudioSystem.getClip();
	        pacmanclip.open(audioInputStream);
	        pacmanclip.start();
	    } catch(Exception ex) {
	        System.out.println("Error with the sound.");
	        ex.printStackTrace();
	    }
	}	
	public static void Chomp() {
	    try {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("C:/ibrahim/JAVA/newPacman/pacman_chomp.wav").getAbsoluteFile());
	        Clip chompclip = AudioSystem.getClip();
	        chompclip.open(audioInputStream);
	        chompclip.start();
	    } catch(Exception e) {
	        System.out.println("Error with the sound.");
	        e.printStackTrace();
	    }
	}
	public void die() {
		
		gui.removeAll();
		JPanel border = new JPanel(new BorderLayout(3,1));
		JLabel label = new JLabel("<html>Level 1 is completed. Your score is " + point + "<br/> ********Congratulations!!******** </html>", SwingConstants.CENTER);
		label.setFont(new Font("", 0, 24));
		border.add(label);
		border.add(new Button("Quit"));
		border.add(againButton);
		
		border.add(label, BorderLayout.CENTER);
		gui.removeAll();
		gui.add(border);
		border.setVisible(true);
		
		border.setBackground(Color.black);
		//repaint();
		timer.cancel();
	}
}