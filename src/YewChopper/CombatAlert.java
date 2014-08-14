package YewChopper;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.methods.Players;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.wrappers.Tile;

public class CombatAlert extends Node {
	Tile safe = new Tile(2914, 3234, 0) ;
	@Override
	public boolean activate() {
		return Players.getLocal().isInCombat();
	}

	@Override
	public void execute() {
		while(Players.getLocal().isInCombat()){
			Walking.walk(safe);
			Task.sleep(200,300);	
			
		}

	}

}
