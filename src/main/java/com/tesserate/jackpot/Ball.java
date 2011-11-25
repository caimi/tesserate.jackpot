package com.tesserate.jackpot;

import java.awt.Color;
import java.awt.Font;
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
	private String msgLabel = "";
	private boolean killBall = false;
	
	public Ball(String resourceId) {
		this.resourceId = resourceId;
		label.setVisible(false);
		label.setFont(new Font("Arial", Font.PLAIN, 10));
		label.setColor(new Color(138,144,149));
		label.setAlignment(CENTER_ALIGNMENT);
	}
	
	@Override
	public void render(Graphics2D g) {
		super.paint(g);
		g.drawImage(ResourceManager.getImageResource(resourceId).getImage(), this.getLocation().x-RAIO, this.getLocation().y-RAIO, null);
		//g.drawOval( this.getLocation().x-RAIO, this.getLocation().y-RAIO, 2*RAIO,2*RAIO);
		label.setPosition(this.getLocation().x, this.getLocation().y-13);
		label.setMsg(msgLabel);
		label.render(g);
	}

	private String removeLowerCase(String msg) {
		String upperCase = "ABCDEFGHIJKLMNOPKRSTUVXWYZ";
		String result = "";
		
		for (int i = 0; i < msg.length(); i++)
			for (int j = 0; j < upperCase.length(); j++){
				if(msg.charAt(i)==upperCase.charAt(j)){
					result += msg.charAt(i);
				}
			}
		return result;
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

	public boolean isKillBall() {
		return killBall;
	}

	public void setKillBall(boolean killBall) {
		this.killBall = killBall;
	}

	public void setLabel(String msg){
		this.msgLabel = msg;
	}
	public void showLabel(boolean show){
		label.setVisible(show);
	}
}
