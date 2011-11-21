package com.tesserate.jackpot;

import java.awt.Graphics2D;

import com.tesserate.game.api.fs.ResourceManager;
import com.tesserate.game.api.ui.GraphicsObjects;

public class Ball extends GraphicsObjects {
	private static final long serialVersionUID = -4877593359509470106L;

	private String resourceId;
	private final int raio = 9;
	

	public Ball(String resourceId) {
		this.resourceId = resourceId;
	}
	
	@Override
	public void render(Graphics2D g) {
		super.paint(g);
		g.drawImage(ResourceManager.getImageResource(resourceId).getImage(), getX()-raio, getY()-raio, null);
	}

	public int getRaio() {
		return raio;
	}

	public void update(long elapsedTime) {
		//this.addXY(Util.rnd(-2,2), Util.rnd(-2,2));
		this.posicao.translate(velocidade.x, velocidade.y);
	}

}
