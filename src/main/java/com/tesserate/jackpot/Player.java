package com.tesserate.jackpot;

import java.awt.Color;
import java.awt.Font;

import com.tesserate.game.api.ui.GraphicText;

public class Player {
	public static final String KILLER = "killer";
	
	public GraphicText name;
	public Ball ball;
	private boolean killer = true;
	private Font font = new Font("tahoma", Font.BOLD, 10);
	private Color color;
	
	public Player(String name, Ball ball, Color color) {
		if(!name.equals(KILLER)){
			this.killer = false;
			this.name = new GraphicText(name, 0, 0);
			this.color = color;
			this.name.setColor(new Color(215,215,215));
			this.name.setFont(font);
			
		}
		this.ball = ball;
	}
	
	public boolean isKiller() {
		return killer;
	}

	public Color getColor() {
		return color;
	}

}
