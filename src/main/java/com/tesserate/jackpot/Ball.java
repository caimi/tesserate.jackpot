package com.tesserate.jackpot;

import java.awt.Graphics2D;

import com.tesserate.game.api.fs.ResourceManager;
import com.tesserate.game.api.ui.GraphicsObjects;
import com.tesserate.game.api.util.Util;

public class Ball extends GraphicsObjects {
	private static final long serialVersionUID = -4877593359509470106L;

	private String resourceId;
	
	public Ball(String resourceId) {
		this.resourceId = resourceId;
	}
	
	@Override
	public void render(Graphics2D g) {
		super.paint(g);
		g.drawImage(ResourceManager.getImageResource(resourceId).getImage(), getX(), getY(), null);
	}

	public void update(long elapsedTime) {
		this.addXY(Util.rnd(-1,1), Util.rnd(-1,1));
	}

}
