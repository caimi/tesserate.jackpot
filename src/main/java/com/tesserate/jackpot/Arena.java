package com.tesserate.jackpot;

import static com.tesserate.game.api.sound.SoundManager.play;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.tesserate.game.api.GameCore;
import com.tesserate.game.api.fs.ResourceManager;
import com.tesserate.game.api.math.Vector2D;
import com.tesserate.game.api.ui.GraphicText;
import com.tesserate.game.api.ui.GraphicsObjects;
import com.tesserate.game.api.ui.SceneGraph;
import com.tesserate.game.api.util.Util;

public class Arena extends GraphicsObjects {
	private static final long serialVersionUID = -9146228304753001488L;

	private static final int TOP = 79;
	private static final int LEFT = 209;
	private static final int H = 1024;
	private static final int W = 1280;
	private static final int BOTTOM = H - 109;
	private static final int RIGHT = W - 29;
	
	private static Arena instance;

//	private int delay = 5000;
//	private int countdown = 5;
//	private long countTime = 0;
//	private int killBall = 0;
	
	private List<Ball> balls = Collections.synchronizedList(new ArrayList<Ball>());
	private GraphicText players = new GraphicText();
	private GraphicText remaining = new GraphicText();
	private GraphicText cueBall = new GraphicText();
	private GraphicText nextBall = new GraphicText();
	private GraphicText prizes = new GraphicText();
	
	private Arena(){ 
		super();
		this.init();
	};
	
	private void init() {
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
		
		synchronized (balls) {
			Iterator<Ball> ball = balls.iterator();
			while (ball.hasNext())
				ball.next().render(g);
		}
	}

	public void update(long elapsedTime) {
		/*refatorar */
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
			//nextBall.setMsg(String.format("%d s", --StructJackpot.nextBall));
		}
		/* */
		synchronized (balls) {
			Iterator<Ball> ball = balls.iterator();
			while (ball.hasNext()){
				Ball next = ball.next();
				next.update(elapsedTime);
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
			for(int i = 0; i<balls.size(); i++){
				for(int j=i; j<balls.size(); j++){
					if(balls.get(i).isCollide(balls.get(j))){
						this.collitionEffect(balls.get(i), balls.get(j));
					}
				}
			}
			if(StructJackpot.players >0 && StructJackpot.prizes == StructJackpot.remaining){
				GameCore.setPaused(true);
			}
		}
	}
	
	private void collitionEffect(Ball b1, Ball b2){
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
		
		if( (b1.isKillBall() || b2.isKillBall()) ){
			if(!b1.isKillBall()){
				balls.remove(b1);
				play("kill");
				--StructJackpot.remaining;
			}
			if(!b2.isKillBall()){
				balls.remove(b2);
				play("kill");
				--StructJackpot.remaining;
			}
		}
	}
	
	private void launchKillBall(){
		play("go");
		Ball bw = new Ball("killball-a");
		//bw.setPosition(Util.rnd(LEFT, RIGHT),Util.rnd(TOP, BOTTOM));
		//bw.setVelocity(Util.rnd(-7, 7), Util.rnd(-7, 7));
		bw.setPosition((RIGHT-LEFT)/2+LEFT, (BOTTOM-TOP)/2+TOP);
		bw.setVelocity(Util.rnd(-7, 7),Util.rnd(-7, 7));
		bw.setKillBall(true); 
		balls.add(bw);
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
				if(e.getKeyCode() == KeyEvent.VK_A) {
					Ball bw = new Ball("ball_"+balls.size()%16);
					balls.add(bw);
					bw.setPosition(Util.rnd(LEFT, RIGHT),Util.rnd(TOP, BOTTOM));
					bw.setVelocity(Util.rnd(-7, 7), Util.rnd(-7, 7));
					
					++StructJackpot.players;
					++StructJackpot.remaining;
				}
				if(e.getKeyCode() == KeyEvent.VK_R){
					balls = Collections.synchronizedList(new ArrayList<Ball>());
					StructJackpot.reset();
					GameCore.setPaused(true);
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
					SceneGraph.getInstance().add( new GraphicText("aqui", 20, TOP+20) );
				}
				if(e.getKeyCode() == KeyEvent.VK_LEFT){
					//eBall.getPosition().x = LEFT;
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
}
