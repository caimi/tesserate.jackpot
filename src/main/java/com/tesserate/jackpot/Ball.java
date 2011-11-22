package com.tesserate.jackpot;

import java.awt.Graphics2D;

import com.tesserate.game.api.fs.ResourceManager;
import com.tesserate.game.api.math.Vector2D;
import com.tesserate.game.api.ui.GraphicText;
import com.tesserate.game.api.ui.GraphicsObjects;

public class Ball extends GraphicsObjects {
	private static final long serialVersionUID = -4877593359509470106L;

	private String resourceId;
	private int RAIO = 10;
	private Vector2D velocity = new Vector2D();
	private GraphicText label = new GraphicText();
	

	public Ball(String resourceId) {
		this.resourceId = resourceId;
	}
	
	@Override
	public void render(Graphics2D g) {
		super.paint(g);
		g.drawImage(ResourceManager.getImageResource(resourceId).getImage(), this.getLocation().x-RAIO, this.getLocation().y-RAIO, null);
		//g.drawOval( this.getLocation().x-RAIO, this.getLocation().y-RAIO, 2*RAIO,2*RAIO);
		label.setPosition(this.getLocation().x, this.getLocation().y-10);
		//label.setMsg(String.format("(%d,%d)", label.getLocation().x, label.getLocation().y));
		label.render(g);
	}

	public int getRaio() {
		return RAIO;
	}

	public void update(long elapsedTime) {
		position = this.position.add(velocity);
		
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
		int somaDosRaios = this.RAIO + b.getRaio();
		if(this == b) return false;
		if(this.getVelocity().getX() == 0 && this.getVelocity().getY() ==0) return false;

		if(this.position.subtract(b.getPosition()).squareLength() > (somaDosRaios*somaDosRaios))
			return false;
		
		return true;
	}

}
