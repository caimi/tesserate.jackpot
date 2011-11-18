package com.tesserate.jackpot;

import com.tesserate.game.api.ControllerDevice;
import com.tesserate.game.api.GameCore;
import com.tesserate.game.api.fs.ImageResource;
import com.tesserate.game.api.fs.ResourceManager;
import com.tesserate.game.api.ui.SceneGraph;

public class Jackpot extends GameCore{

	private Arena arena;

	@Override
	public void init() {
		GameCore.pause();
		arena = Arena.getInstance();
		ControllerDevice.getInstance().setKeyListener(arena.keyMapper());
		SceneGraph.getInstance().add( arena );
		super.init();
	}

	@Override
	public void update(long elapsedTime) {
		arena.update(elapsedTime);
	}

	@Override
	public void loadResources() {
		ImageResource image = new ImageResource("lobby", "images/jackpot.png");
		ResourceManager.addResource(image);

		//TODO usar enum para nomes das imagens
		image = new ImageResource("ball_0", "images/b_branca.png");
		ResourceManager.addResource(image);
		
		image = new ImageResource("ball_1", "images/b_preta.png");
		ResourceManager.addResource(image);

		image = new ImageResource("ball_2", "images/b_amarela_c.png");
		ResourceManager.addResource(image);

		image = new ImageResource("ball_3", "images/b_amarela_e.png");
		ResourceManager.addResource(image);

		image = new ImageResource("ball_4", "images/b_azul_c.png");
		ResourceManager.addResource(image);
		
		image = new ImageResource("ball_5", "images/b_azul_e.png");
		ResourceManager.addResource(image);

		image = new ImageResource("ball_6", "images/b_azul.png");
		ResourceManager.addResource(image);
		
		image = new ImageResource("ball_7", "images/b_ciano_c.png");
		ResourceManager.addResource(image);
		
		image = new ImageResource("ball_8", "images/b_ciano_e.png");
		ResourceManager.addResource(image);
		
		image = new ImageResource("ball_9", "images/b_laranja.png");
		ResourceManager.addResource(image);
		
		image = new ImageResource("ball_10", "images/b_marrom.png");
		ResourceManager.addResource(image);
		
		image = new ImageResource("ball_11", "images/b_rosa_c.png");
		ResourceManager.addResource(image);
		
		image = new ImageResource("ball_12", "images/b_rosa_e.png");
		ResourceManager.addResource(image);
		
		image = new ImageResource("ball_13", "images/b_roxa.png");
		ResourceManager.addResource(image);
		
		image = new ImageResource("ball_14", "images/b_verde_c.png");
		ResourceManager.addResource(image);
		
		image = new ImageResource("ball_15", "images/b_verde_e.png");
		ResourceManager.addResource(image);
		
		image = new ImageResource("ball_16", "images/b_vermelho_c.png");
		ResourceManager.addResource(image);
		
		image = new ImageResource("ball_17", "images/b_vermelho_e.png");
		ResourceManager.addResource(image);
	}

	public static void main(String[] args) {
		try{
			new Jackpot();
		}catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

}
