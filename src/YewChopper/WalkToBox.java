package YewChopper;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;

public class WalkToBox extends Node {
	public static Tile bankTile = new Tile(3047, 3236, 0);
	Tile[] myTiles = new Tile[] { new Tile(3010, 3215, 0),
			new Tile(3012, 3215, 0), new Tile(3015, 3216, 0),
			new Tile(3019, 3217, 0), new Tile(3022, 3218, 0),
			new Tile(3025, 3218, 0), new Tile(3027, 3219, 0),
			new Tile(3026, 3223, 0), new Tile(3027, 3225, 0),
			new Tile(3027, 3228, 0), new Tile(3027, 3230, 0),
			new Tile(3027, 3233, 0), new Tile(3027, 3234, 0),
			new Tile(3029, 3235, 0), new Tile(3032, 3235, 0),
			new Tile(3034, 3235, 0), new Tile(3037, 3235, 0),
			new Tile(3041, 3235, 0), new Tile(3045, 3235, 0),
			new Tile(3048, 3235, 0), new Tile(3048, 3236, 0) };

	Area myArea = new Area(new Tile[] { new Tile(3053, 3252, 0),
			new Tile(3054, 3209, 0), new Tile(3004, 3210, 0),
			new Tile(3014, 3243, 0) });

	@Override
	public boolean activate() {
		return Inventory.isFull() && Calculations.distanceTo(bankTile) < 45;
	}

	@Override
	public void execute() {
		Walking.newTilePath(myTiles).traverse();
	}

}
