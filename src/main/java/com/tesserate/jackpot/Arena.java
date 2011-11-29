package com.tesserate.jackpot;

import static com.tesserate.game.api.sound.SoundManager.play;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.SwingUtilities;

import com.tesserate.game.api.GameCore;
import com.tesserate.game.api.Sprite;
import com.tesserate.game.api.fs.ResourceManager;
import com.tesserate.game.api.math.Vector2D;
import com.tesserate.game.api.ui.FullScreenDevice;
import com.tesserate.game.api.ui.GraphicButton;
import com.tesserate.game.api.ui.GraphicText;
import com.tesserate.game.api.ui.GraphicsObjects;
import com.tesserate.game.api.ui.SceneGraph;
import com.tesserate.game.api.util.Util;
import com.tesserate.jackpot.Ball.Status;

public class Arena extends GraphicsObjects {
	private static final long serialVersionUID = -9146228304753001488L;

	private static final int TOP = 79;
	private static final int LEFT = 209;
	private int H;
	private int W;
	private int BOTTOM;
	private int RIGHT;
	
	private static Arena instance;

	private static List<Color> colors = new ArrayList<Color>();
	private List<Player> listPlayer = Collections.synchronizedList(new ArrayList<Player>());
	
	private GraphicText players = new GraphicText();
	private GraphicText remaining = new GraphicText();
	private GraphicText cueBall = new GraphicText();
	private GraphicText nextBall = new GraphicText();
	private GraphicText prizes = new GraphicText();
	private List<GraphicButton> buttons = new ArrayList<GraphicButton>();
	private MouseAdapter mouseAdapter;
	
	private GraphicText screem;
	private Sprite ball;
	private boolean showBallLabels = false;
	
	private Arena(){ 
		super();
	};
	
	public void init() {
		BOTTOM = H - 109;
		RIGHT = W - 29;
		players.setColor(Color.WHITE);
		Font f = new Font("Impact", Font.PLAIN, 30);
		players.setFont(f);
		players.setPosition(30, H-30);
		players.setMsg(StructJackpot.getPlayers());
		remaining.setFont(f);
		remaining.setPosition(130, H-30);
		remaining.setMsg(StructJackpot.remaining);
		nextBall.setMsg("5 s");
		nextBall.setFont(f);
		nextBall.setPosition(230, H-30);
		cueBall.setFont(f);
		cueBall.setPosition(340, H-30);
		prizes.setMsg("0");
		prizes.setFont(f);
		prizes.setPosition(440, H-30);
		prizes.setMsg(StructJackpot.getPrizes());
		screem = new GraphicText(String.format("Resolução %d x %d", W, H), RIGHT-200, HEIGHT - 20 );
		screem.setColor(Color.DARK_GRAY);
		initColors();
		ball = new Sprite(ResourceManager.getImageResource("ball"), 4, 4, 23, 26, 0, 0);
		
		Sprite start = new Sprite(ResourceManager.getImageResource("start"), 2, 1, 74, 74, 0, 0);
		
		MouseAdapter mouseAdapater = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				GameCore.pause();
				((GraphicButton)e.getSource()).onClick();
			}
		};
		GraphicButton btStart = new GraphicButton(start.getFrame(0), start.getFrame(1), mouseAdapater, 575, H-90, 0);
		buttons.add(btStart);
		
		Container contentPane = FullScreenDevice.getInstance().getMainFrame().getContentPane();
		contentPane.add(btStart);
		
		Sprite butttons = new Sprite(ResourceManager.getImageResource("buttons"), 2, 7, 34, 34, 0, 0);
		
		mouseAdapater = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				initPlayerPosition();
				((GraphicButton)e.getSource()).onClick();
			}
		};

		int buttonX=535;
		GraphicButton bt = new GraphicButton(butttons.getFrame(6), butttons.getFrame(7), mouseAdapater, buttonX, H-70, 0);
		buttons.add(bt);
		contentPane.add(bt);
		
		mouseAdapater = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(!GameCore.isPaused())
					GameCore.pause();
				((GraphicButton)e.getSource()).onClick();
			}
		};
		buttonX = 660;
		bt = new GraphicButton(butttons.getFrame(0), butttons.getFrame(1), mouseAdapater, buttonX, H-70, 0);
		buttons.add(bt);
		contentPane.add(bt);
		
		mouseAdapater = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(!GameCore.isPaused())
					launchKillBall();
				((GraphicButton)e.getSource()).onClick();
			}
		};
		buttonX+=40;
		bt = new GraphicButton(butttons.getFrame(2), butttons.getFrame(3), mouseAdapater, buttonX, H-70, 0);
		buttons.add(bt);
		contentPane.add(bt);
		
		mouseAdapater = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				showBallLabels = !showBallLabels;
				showLabel( showBallLabels);
				((GraphicButton)e.getSource()).onClick();
			}
		};
		buttonX+=40;
		bt = new GraphicButton(butttons.getFrame(8), butttons.getFrame(9), mouseAdapater, buttonX, H-70, 0);
		buttons.add(bt);
		contentPane.add(bt);
		
		mouseAdapater = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				StructJackpot.prizes++;
				((GraphicButton)e.getSource()).onClick();
			}
		};
		buttonX=500;
		bt = new GraphicButton(butttons.getFrame(10), butttons.getFrame(11), mouseAdapater, buttonX, H-90, 0);
		buttons.add(bt);
		contentPane.add(bt);
		
		mouseAdapater = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				StructJackpot.prizes--;
				((GraphicButton)e.getSource()).onClick();
			}
		};

		bt = new GraphicButton(butttons.getFrame(12), butttons.getFrame(13), mouseAdapater, buttonX, H-50, 0);
		buttons.add(bt);
		contentPane.add(bt);

	}

	private void initColors() {
		colors.add(new Color(229,196,223)); //rosa c
		colors.add(new Color(228,33,218)); //rosa e
		colors.add(new Color(226,72,72)); //vermelho c
		colors.add(new Color(140,27,27)); //vermelho e
		colors.add(new Color(27,32,201)); //azul e
		colors.add(new Color(140,122,196)); //roxo
		colors.add(new Color(31,125,247)); // azul
		colors.add(new Color(151,210,243)); // azul c
		colors.add(new Color(255,201,25)); //amarelo e
		colors.add(new Color(110,72,33)); //marrom
		colors.add(new Color(242,160,30)); //laranja
		colors.add(new Color(225,208,26)); //amarelo c
		colors.add(new Color(33,131,22)); //verde e
		colors.add(new Color(27,223,50)); //verde c
		colors.add(new Color(44,205,205)); // ciano c
		colors.add(new Color(47,167,155)); //ciano e
	}

	public static Arena getInstance(){
		if(instance == null){
			instance = new Arena();
		}
		return instance;
	}
	
	@Override
	public void render(Graphics2D g) {
		super.paint(g);
		
		if(this.isVisible()){
			g.drawImage(ResourceManager.getImageResource("lobby").getImage(), getX(), getY(), null);
		}
		
		this.updateTextValues();
		players.render(g);
		remaining.render(g);
		nextBall.render(g);
		cueBall.render(g);
		prizes.render(g);
		screem.render(g);
		renderPlayer(g);
		
		for (GraphicButton b : buttons) {
			b.render(g);
		} 
//		Component[] comp = FullScreenDevice.getInstance().getMainFrame().getContentPane().getComponents();
//		for (Component c : comp) {
//			Rectangle r = c.getBounds();
//			g.draw(r);
//		}
	}

	private void renderPlayer(Graphics2D g) {
		int heightLine = 5;
		for (int i=0; i<listPlayer.size(); i++) {
			Player player = listPlayer.get(i);
			player.ball.render(g);
			
			if( ((TOP + heightLine) < BOTTOM) && !player.isKiller() && (player.ball.getStatus() == Ball.Status.RUNNING || player.ball.getStatus() == Ball.Status.READY)){
				g.setColor(player.getColor());
				g.fillOval(25, TOP-10+heightLine, 10, 10);
				player.name.setPosition(40, TOP+heightLine);
				player.name.render(g);
				heightLine += 15;
			}
		}
	}

	public void update(long elapsedTime) {
		/* TODO refatorar */
		if(StructJackpot.delay > 0)
			StructJackpot.delay -= elapsedTime;
		else{
			StructJackpot.delay += 10000;
			StructJackpot.nextBall = 10;
			this.launchKillBall();
		}
		StructJackpot.countdown += elapsedTime;
		if(StructJackpot.countdown > 1000){
			StructJackpot.countdown -= 1000; 
			--StructJackpot.nextBall;
		}

		synchronized (listPlayer) {
			Iterator<Player> playerIterator = listPlayer.iterator();
			while (playerIterator.hasNext()){
				Player player = playerIterator.next();
				Ball next = player.ball;
				next.update(elapsedTime);
				
				if(player.ball.getStatus() == Status.READY) {
					if((next.getPosition().getX() > RIGHT) || (next.getPosition().getX() < LEFT)){
						next.getVelocity().setX(-next.getVelocity().getX());
						if(next.getPosition().getX() > RIGHT)
							next.getPosition().setX(RIGHT);
						if(next.getPosition().getX() < LEFT)
							next.getPosition().setX(LEFT);
					}
					if((next.getPosition().getY() > BOTTOM) || (next.getPosition().getY() < TOP)){
						next.getVelocity().setY(-next.getVelocity().getY());
						if(next.getPosition().getY() > BOTTOM)
							next.getPosition().setY(BOTTOM);
						if(next.getPosition().getY() < TOP)
							next.getPosition().setY(TOP);
					}
				}
			}
			for(int i = 0; i<listPlayer.size(); i++){
				for(int j=i; j<listPlayer.size(); j++){
					if(listPlayer.get(i).ball.isCollide(listPlayer.get(j).ball)){
						this.collitionEffect(listPlayer.get(i), listPlayer.get(j));
					}
				}
			}
			if(StructJackpot.players >0 && StructJackpot.prizes == StructJackpot.remaining){
				showBallLabels = true;
				showLabel(showBallLabels);
				GameCore.setPaused(true);
			}
		}
	}
	
	private void collitionEffect(Player p1, Player p2){
		Ball b1 = p1.ball;
		Ball b2 = p2.ball;
		
		Vector2D direction = b2.getPosition().subtract(b1.getPosition());
		direction = direction.normalize();
		//2 corpos nao ocupam o mesmo lugar no espaco.
		Vector2D positionB2 = b1.getPosition().add( direction.multiply(b1.getRaio()+b2.getRaio()));
		b2.setPosition(positionB2);
		
		//conservacao de energia
		double f1 = b1.getVelocity().dot(direction);
		double f2 = b2.getVelocity().dot(direction);
		
		Vector2D velo = b1.getVelocity().subtract(direction.multiply(f1-f2));
		b1.setVelocity(velo.getX(), velo.getY());

		velo = b2.getVelocity().add(direction.multiply((f1-f2)));
		b2.setVelocity(velo.getX(), velo.getY());
		
		if( (b1.isCueBall() || b2.isCueBall()) ){
			if(!b1.isCueBall()){
				b1.kill();
				play("kill");
				--StructJackpot.remaining;
			}
			if(!b2.isCueBall()){
				b2.kill();
				play("kill");
				--StructJackpot.remaining;
			}
		}
	}
	
	private void launchKillBall(){
		play("go");
		Ball bw = new Ball("killball-a");
		bw.setPosition((RIGHT-LEFT)/2+LEFT, (BOTTOM-TOP)/2+TOP);
		bw.setVelocity(Util.rnd(-7d, 7d),Util.rnd(-7d, 7d));
		bw.setCueBall(true);
		
		listPlayer.add(new Player(Player.KILLER, bw, Color.WHITE));
		StructJackpot.cueBall++;
	}
	
	private void updateTextValues() {
		players.setMsg(StructJackpot.players);
		remaining.setMsg(StructJackpot.remaining);
		nextBall.setMsg(StructJackpot.nextBall);
		cueBall.setMsg(StructJackpot.cueBall);
		prizes.setMsg(StructJackpot.prizes);	
	}
	
	public KeyAdapter keyMapper(){
		return new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				}
				if(e.getKeyCode() == KeyEvent.VK_P || e.getKeyCode() == KeyEvent.VK_SPACE) {
					GameCore.pause();
				}
				if(e.getKeyCode() == KeyEvent.VK_J || e.getKeyCode() == KeyEvent.VK_A) {
						initPlayerPosition();
				}
				
				if(e.getKeyCode() == KeyEvent.VK_L){
					showBallLabels = !showBallLabels;
					showLabel( showBallLabels);
				}
				
				if(e.getKeyCode() == KeyEvent.VK_R){
					reset();
				}
				if(e.getKeyCode() == KeyEvent.VK_K){
					if(!GameCore.isPaused())
						launchKillBall();
				}
				if(e.getKeyCode() == KeyEvent.VK_S){
					StructJackpot.prizes++;
				}
				if(e.getKeyCode() == KeyEvent.VK_X){
					StructJackpot.prizes--;
				}
				
				if(e.getKeyCode() == KeyEvent.VK_RIGHT){
					//eBall.getPosition().x = RIGHT;
					SceneGraph.getInstance().add( new GraphicText(String.format("Resolução: %d x %d",W,H), RIGHT-150, H-20) );
				}
				if(e.getKeyCode() == KeyEvent.VK_LEFT){
				}
				if(e.getKeyCode() == KeyEvent.VK_UP){
					//eBall.getPosition().y = TOP;
				}
				if(e.getKeyCode() == KeyEvent.VK_DOWN){
					//eBall.getPosition().y = BOTTOM;
				}
			}
		};
	}

	private void showLabel(boolean show) {
		Iterator<Player> playerIterator = listPlayer.iterator();
		while (playerIterator.hasNext()){
			playerIterator.next().ball.showLabel(show);
		}
	}
	
	private void initPlayerPosition() {
		//if(ResourceManager.getTextFiles("players").size() > StructJackpot.players){
		reset();
		for(int i = 0; i<ResourceManager.getTextFiles("players").size(); i++){
			List<String> names = ResourceManager.getTextFiles("players");
//			String name = names.get(StructJackpot.players%names.size());
			String name = names.get(i);
			BufferedImage image = ball.getFrame(StructJackpot.players%ball.size());
			Ball bw = new Ball(image);
			//Ball bw = new Ball("ball_"+StructJackpot.players%16);
			bw.setPosition(Util.rnd(LEFT*1d, RIGHT*1d),Util.rnd(TOP*1d, BOTTOM*1d));
			bw.setVelocity(Util.rnd(-7d, 7d), Util.rnd(-7d, 7d));
			bw.setLabel(name);
			Player player = new Player(name, bw, colors.get(listPlayer.size()%16));
			
			listPlayer.add(player);
			++StructJackpot.players;
			++StructJackpot.remaining;
		}
	}
	
	private void reset() {
		listPlayer = Collections.synchronizedList(new ArrayList<Player>());
		StructJackpot.reset();
		GameCore.setPaused(true);
	}
	
	public void setH(int h) {
		H = h;
	}

	public void setW(int w) {
		W = w;
	}

	public MouseAdapter getMouseAdapter() {
		return mouseAdapter;
	}

	public void setMouseAdapter(MouseAdapter mouseAdapter) {
		this.mouseAdapter = mouseAdapter;
	}

}
