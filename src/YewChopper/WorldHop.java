package YewChopper;

import java.util.Random;

import org.powerbot.core.randoms.AntiRandom;
import org.powerbot.core.randoms.Login;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.methods.Environment;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.widget.Lobby;
import org.powerbot.game.api.methods.widget.Lobby.World;
import org.powerbot.game.api.wrappers.interactive.Player;

public class WorldHop extends Node {
	Area CuttingArea = new Area(new Tile[] { new Tile(2920, 3240, 0),
			new Tile(2921, 3225, 0), new Tile(2930, 3221, 0),
			new Tile(2939, 3223, 0), new Tile(2946, 3230, 0),
			new Tile(2944, 3237, 0), new Tile(2935, 3242, 0),
			new Tile(2926, 3241, 0) });

	int[] f2pWorlds = { 3, 7, 8, 11, 13, 17, 19, 20, 29, 33, 34, 38, 41, 43,
			57, 61, 80, 81, 108, 120, 135, 136 };

	@Override
	public boolean activate() {
		return (Players.getLoaded(playerFilter).length > 2);
	}

	private final Filter<Player> playerFilter = new Filter<Player>() {
		public boolean accept(final Player player) {
			return player != null && CuttingArea.contains(player.getLocation());
		}
	};
	
	

	@Override
	public void execute() {
		Random generator = new Random(); 
		int world = (int) generator.nextInt(f2pWorlds.length);
		Environment.enableRandom(org.powerbot.core.randoms.Login.class, false);
		Game.logout(true);
		Lobby.enterGame((Lobby.getWorld(f2pWorlds[world])));
		//Environment.enableRandom(org.powerbot.core.randoms.Login.class, false);
	}

}
