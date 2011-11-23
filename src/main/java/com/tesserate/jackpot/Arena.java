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
import com.tesserate.game.api.util.Util;

public class Arena extends GraphicsObjects {
	private static final int TOP = 79;
	private static final int LEFT = 209;
	private static final int H = 600;
	private static final int W = 800;
	private static final int BOTTOM = H - 109;
	private static final int RIGHT = W - 29;
	
	private int delay = 5000;
	private static final long serialVersionUID = -9146228304753001488L;
	private int killBall = 0;
	private static Arena instance;
	private List<Ball> balls = Collections.synchronizedList(new ArrayList<Ball>());
	private GraphicText ballSize = new GraphicText();
	private GraphicText killBallSize = new GraphicText();
	
	private Arena(){ 
		super();
		this.init();
	};
	
	private void init() {
		ballSize.setColor(Color.WHITE);
		Font f = new Font("Impact", Font.PLAIN, 30);
		ballSize.setFont(f);
		ballSize.setPosition(30, H-40);
		killBallSize.setFont(f);
		killBallSize.setPosition(270, H-40);
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
		ballSize.render(g);
		killBallSize.setMsg(killBall+"");
		killBallSize.render(g);
		
		synchronized (balls) {
			Iterator<Ball> ball = balls.iterator();
			while (ball.hasNext())
				ball.next().render(g);
		}
	}

	public void update(long elapsedTime) {
		if(delay > 0)
			delay -= elapsedTime;
		else{
			delay = 10000;
			this.launchKillBall();
		}
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
			}
			if(!b2.isKillBall()){
				balls.remove(b2);
				play("kill");
			}
			ballSize.setMsg(balls.size()-killBall +"");
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
		killBall++;
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
					ballSize.setMsg(balls.size()+"");
				}
				if(e.getKeyCode() == KeyEvent.VK_R){
					balls = Collections.synchronizedList(new ArrayList<Ball>());
					killBall = 0;
					ballSize.setMsg(balls.size()+"");
				}
				if(e.getKeyCode() == KeyEvent.VK_K){
					launchKillBall();
				}
				
				if(e.getKeyCode() == KeyEvent.VK_RIGHT){
					//eBall.getPosition().x = RIGHT;
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
