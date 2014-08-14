package YewChopper;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.Tile;

public class WalkToTree extends Node {
	public static Tile bankTile = new Tile(2937, 3232, 0);
	Tile[] myTiles = new Tile[] { new Tile(3010, 3215, 0),
			new Tile(3006, 3216, 0), new Tile(3002, 3217, 0),
			new Tile(2997, 3218, 0), new Tile(2992, 3220, 0),
			new Tile(2989, 3220, 0), new Tile(2986, 3221, 0),
			new Tile(2982, 3222, 0), new Tile(2978, 3222, 0),
			new Tile(2974, 3223, 0), new Tile(2970, 3223, 0),
			new Tile(2966, 3224, 0), new Tile(2962, 3226, 0),
			new Tile(2959, 3227, 0), new Tile(2956, 3229, 0),
			new Tile(2952, 3230, 0), new Tile(2948, 3231, 0),
			new Tile(2944, 3232, 0), new Tile(2940, 3232, 0),
			new Tile(2937, 3232, 0) };

	@Override
	public boolean activate() {
		return (Inventory.getCount() <= 1 && Calculations.distanceTo(bankTile) > 30);
	}

	@Override
	public void execute() {
		Walking.newTilePath(myTiles).traverse();

	}

}
