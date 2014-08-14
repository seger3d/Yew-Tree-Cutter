package YewChopper;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;

import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.methods.widget.DepositBox;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;

public class Deposit extends Node {
	Area smallBankArea = new Area(new Tile[] { new Tile(3042, 3239, 0),
			new Tile(3042, 3230, 0), new Tile(3054, 3230, 0),
			new Tile(3054, 3239, 0) });
	public static Tile bankTile = new Tile(3047, 3236, 0);

	@Override
	public boolean activate() {
		return (Inventory.isFull() && Calculations.distanceTo(bankTile.getLocation()) < 5);

	}

	@Override
	public void execute() {
		SceneObject box = SceneEntities.getNearest(36788);

		if (box != null)
			if (box.isOnScreen()) {
				box.interact("Deposit");
				Timer x = new Timer(5000);
				while (x.isRunning() && !DepositBox.isOpen()) {
					Task.sleep(100);
				}

				if (DepositBox.isOpen()) {
					DepositBox.deposit(1515, 28);
					DepositBox.close();
				}

			} else {
				Camera.turnTo(box);
			}

	}

}
