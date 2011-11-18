package com.tesserate.jackpot;

import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import com.tesserate.game.api.GameCore;
import com.tesserate.game.api.fs.ResourceManager;
import com.tesserate.game.api.ui.GraphicsObjects;

public class Arena extends GraphicsObjects {
	private static final long serialVersionUID = -9146228304753001488L;

	private static Arena instance;
	private List<Ball> balls = new ArrayList<Ball>();
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
		g.drawImage(ResourceManager.getImageResource("lobby").getImage(), getX(), getY(), null);
		for (Ball b : balls) {
			b.render(g);
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
					Ball bw = new Ball("ball_white");
					balls.add(bw);
					bw.setX((int)Math.round(Math.random()*800));
					bw.setY((int)Math.round(Math.random()*600));
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
					Arena.getInstance().setY(Arena.getInstance().getY()+10);
				}
				if(e.getKeyCode() == KeyEvent.VK_DOWN){
					Arena.getInstance().setY(Arena.getInstance().getY()-10);
				}
			}
		};
	}
}
