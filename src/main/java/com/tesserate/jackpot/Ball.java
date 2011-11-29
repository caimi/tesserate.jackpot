package com.tesserate.jackpot;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.tesserate.game.api.AnimatedSprite;
import com.tesserate.game.api.fs.ResourceManager;
import com.tesserate.game.api.math.Vector2D;
import com.tesserate.game.api.ui.GraphicText;
import com.tesserate.game.api.ui.GraphicsObjects;
import com.tesserate.game.api.util.Util;

public class Ball extends GraphicsObjects {
	private static final long serialVersionUID = -4877593359509470106L;

	public enum Status {READY, RUNNING, DYING, ENDED}
	
	private int RAIO = 10;
	private Vector2D velocity = new Vector2D();
	private GraphicText label = new GraphicText();
	private String msgLabel = "";
	private boolean cueBall = false;
	private Status status = Status.READY;
	private AnimatedSprite sprite;
	private BufferedImage image;
	
	public Ball(BufferedImage image) {
		this.image = image;
		init();
	}
	
	public Ball(String resourceId) {
		this.image = ResourceManager.getImageResource(resourceId).getImage();
		init();
	}

	private void init() {
		label.setVisible(false);
		label.setFont(new Font("Arial", Font.PLAIN, 10));
		label.setColor(new Color(138,144,149));
		label.setAlignment(CENTER_ALIGNMENT);
		sprite = new AnimatedSprite(ResourceManager.getImageResource("explosion"), 16, 16, 1, 64, 64, 0, Util.rnd(0,9)*64);
	}
	
	@Override
	public void render(Graphics2D g) {
		if(status == Status.READY || status == Status.RUNNING){
			super.paint(g);
			g.drawImage(image, this.getLocation().x-RAIO, this.getLocation().y-RAIO, null);
			label.setPosition(this.getLocation().x, this.getLocation().y-13);
			label.setMsg(msgLabel);
			label.render(g);
		}

		if(status == Status.DYING){
			g.drawImage(sprite.getFrame(), this.getLocation().x-sprite.getWidth()/2, this.getLocation().y-sprite.getHeight()/2, null);
		}
	}

	public int getRaio() {
		return RAIO;
	}

	public void update(long elapsedTime) {
		position = this.position.add(velocity);
		sprite.update(elapsedTime);
		
		if(sprite.getStatus()==AnimatedSprite.Status.ENDED){
			this.status = Status.ENDED;
		}
	}

	public Vector2D getVelocity() {
		return this.velocity;
	}

	public void setVelocity(Vector2D v) {
		this.setVelocity(v.getX(), v.getY());
	}
	
	public void setVelocity(double x, double y) {
		this.velocity.setX(x);
		this.velocity.setY(y);
	}
	
	public boolean isCollide(Ball b){
		if( b.status == Status.DYING || 
			b.status == Status.ENDED || 
			this.status == Status.DYING || 
			this.status == Status.ENDED ) return false;
		
		int somaDosRaios = this.RAIO + b.getRaio();
		if(this == b) return false;
		if(this.getVelocity().getX() == 0 && this.getVelocity().getY() ==0) return false;

		if(this.position.subtract(b.getPosition()).squareLength() > (somaDosRaios*somaDosRaios))
			return false;
		
		return true;
	}

	public boolean isCueBall() {
		return cueBall;
	}

	public void setCueBall(boolean cueBall) {
		this.cueBall = cueBall;
	}

	public void setLabel(String msg){
		this.msgLabel = msg;
	}
	public void showLabel(boolean show){
		label.setVisible(show);
	}
	
	public void kill(){
		status = Status.DYING;
		sprite.start();
	}

	public Status getStatus() {
		return status;
	}

}
