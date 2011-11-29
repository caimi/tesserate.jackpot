package com.tesserate.jackpot;

import com.tesserate.game.api.ControllerDevice;
import com.tesserate.game.api.GameCore;
import com.tesserate.game.api.fs.ImageResource;
import com.tesserate.game.api.fs.Resource;
import com.tesserate.game.api.fs.ResourceManager;
import com.tesserate.game.api.fs.TextFileResource;
import com.tesserate.game.api.sound.SoundManager;
import com.tesserate.game.api.ui.FullScreenDevice;
import com.tesserate.game.api.ui.NullRepaintManager;
import com.tesserate.game.api.ui.SceneGraph;

public class Jackpot extends GameCore{

	private Arena arena;
	private static String file = "";

	@Override
	public void init() {
		GameCore.pause();
		arena = Arena.getInstance();
		ControllerDevice.getInstance().setKeyListener(arena.keyMapper());
		SceneGraph.getInstance().add( arena );
		super.init();
		arena.setW(FullScreenDevice.getWidth());
		arena.setH(FullScreenDevice.getHeight());
		arena.init();
		NullRepaintManager.install(); //nao sei porque eu coloquei isso no PMT
	}

	@Override
	public void update(final long elapsedTime) {
		arena.update(elapsedTime);
	}

	@Override
	public void loadResources() {
		loadImageResources();
		loadAudioResources();
		loadStringFilesResourcers();
	}

	private void loadStringFilesResourcers() {
		addFilePlayers();		
	}

	private void loadAudioResources() {
		final SoundManager soundManager = SoundManager.getInstance(); 
		soundManager.getSound("go", Resource.loadFile("go.wav"));
		soundManager.getSound("time", Resource.loadFile("time.wav"));
		soundManager.getSound("hit", Resource.loadFile("hit.wav"));
		soundManager.getSound("kill", Resource.loadFile("kill2.wav"));
	}

	private void loadImageResources() {
		addBackgroundResource();
		addMainBallsResource();
		addColoredBallsResource();
	}

	private void addBackgroundResource() {
		ImageResource image;
		final String backgroundImageName = String.format("images/jackpot_%dx%d.png", FullScreenDevice.getWidth(), FullScreenDevice.getHeight());
		try{
			image = new ImageResource("lobby", backgroundImageName);
		}catch (final IllegalArgumentException e) {
			throw new RuntimeException("Image not available: "+backgroundImageName,e);
		}
		ResourceManager.addResource(image);
	}

	private void addMainBallsResource() {
		addResource("killball-a", "b_branca.png");
		addResource("killball-i", "b_preta.png");
	}

	private void addColoredBallsResource() {
		addResource("explosion", "explosions.png");
		addResource("buttons", "buttons.png");
		addResource("start", "start.png");
		addResource("spaceship", "spaceships32x32.png");
		addResource("ball", "ball.png");
	}
	
	private void addFilePlayers(){
		Resource playerFile;
		if(file == null)
			playerFile = new TextFileResource("players", "./src/main/resources/concorrentes.txt");
		else{
			playerFile = new TextFileResource("players", file);
		}
		ResourceManager.addResource(playerFile);
	}

	private void addResource(final String resourceId, final String resourceFile) {
		ImageResource image;
		image = new ImageResource(resourceId, "images/"+resourceFile);
		ResourceManager.addResource(image);
	}

	public static void main(final String[] args) {
		try{
			new Jackpot();
			 for (String s: args) {
				 if(!s.isEmpty())
					 file = s;
			 }
		}catch (final Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

}
