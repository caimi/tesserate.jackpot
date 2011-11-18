package com.tesserate.jackpot;

import com.tesserate.game.api.ControllerDevice;
import com.tesserate.game.api.GameCore;
import com.tesserate.game.api.fs.ImageResource;
import com.tesserate.game.api.fs.ResourceManager;
import com.tesserate.game.api.ui.SceneGraph;

public class Jackpot extends GameCore{

	@Override
	public void init() {
		Arena arena = Arena.getInstance();
		ControllerDevice.getInstance().setKeyListener(arena.keyMapper());
		SceneGraph.getInstance().add( arena );
		super.init();
	}

	@Override
	public void update(long elapsedTime) {
		// TODO Auto-generated method stub
	}

	@Override
	public void loadResources() {
		ImageResource image = new ImageResource("lobby", "images/jackpot.png");
		ResourceManager.addResource(image);

		image = new ImageResource("ball_white", "images/b_branca.png");
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
