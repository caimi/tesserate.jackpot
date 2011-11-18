package com.tesserate.jackpot;

import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.tesserate.game.api.GameCore;
import com.tesserate.game.api.fs.ResourceManager;
import com.tesserate.game.api.ui.GraphicsObjects;
import com.tesserate.game.api.util.Util;

public class Arena extends GraphicsObjects {
	private static final long serialVersionUID = -9146228304753001488L;

	private static Arena instance;
	private List<Ball> balls = Collections.synchronizedList(new ArrayList<Ball>());
	
	private Arena(){ super(); };
	
	public static Arena getInstance(){
		if(instance == null){
			instance = new Arena();
		}
		return instance;
	}
	
	@Override
	public void render(Graphics2D g) {
		super.paint(g);
		
		if(this.isVisivel()){
			g.drawImage(ResourceManager.getImageResource("lobby").getImage(), getX(), getY(), null);
		}
		
		synchronized (balls) {
			Iterator<Ball> ball = balls.iterator();
			while (ball.hasNext())
				ball.next().render(g);
		}
	}

	public void update(long elapsedTime) {
		synchronized (balls) {
			Iterator<Ball> ball = balls.iterator();
			while (ball.hasNext())
				ball.next().update(elapsedTime);
		}
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
					Ball bw = new Ball("ball_"+(int)Math.round(Math.random()*17));
					balls.add(bw);
					//bw.setX((int)Math.round(Math.random()*560) + 200);
					//bw.setY((int)Math.round(Math.random()*400) + 80);
					bw.setX(Util.rnd(210, 760));
					bw.setY(Util.rnd(80, 480));
				}
				if(e.getKeyCode() == KeyEvent.VK_I){
					Arena.getInstance().setVisible(false);
				}
				if(e.getKeyCode() == KeyEvent.VK_V){
					Arena.getInstance().setVisible(true);
				}
				if(e.getKeyCode() == KeyEvent.VK_RIGHT){
					Arena.getInstance().setX(Arena.getInstance().getX()+10);
				}
				if(e.getKeyCode() == KeyEvent.VK_LEFT){
					Arena.getInstance().setX(Arena.getInstance().getX()-10);
				}
				if(e.getKeyCode() == KeyEvent.VK_UP){
					Arena.getInstance().setY(Arena.getInstance().getY()-10);
				}
				if(e.getKeyCode() == KeyEvent.VK_DOWN){
					Arena.getInstance().setY(Arena.getInstance().getY()+10);
				}
			}
		};
	}
}
