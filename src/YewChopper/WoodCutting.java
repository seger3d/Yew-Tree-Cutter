package YewChopper;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.methods.Players;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.wrappers.Locatable;
import org.powerbot.game.api.wrappers.node.GroundItem;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.client.RSObject;

public class WoodCutting extends Node {
	public static Tile[] yewTiles = { new Tile(2926, 3229, 0),
			new Tile(2934, 3234, 0), new Tile(2935, 3226, 0),
			new Tile(2941, 3233, 0) };
	public static Tile waitTiles[] = { new Tile(2928, 3229, 0),
			new Tile(2934, 3232, 0), new Tile(2935, 3227, 0),
			new Tile(2941, 3231, 0) };

	String status = "Starting up";

	long startTime = System.currentTimeMillis();

	int hatchetID[] = { 1351, 1349, 1361, 1355, 1357, 1359, 1353, 6739 };
	int yewTreeID = 38755;
	int yewTreeStumpID = 38759;

	int currentTree;
	boolean noHatchet = false;
	boolean kill = false;

	boolean treeSwitch = false;

	static long treeTimer[] = new long[yewTiles.length];

	enum treeStatus {
		GREEN, BROWN, RED
	}

	public static treeStatus yewPositions[] = { treeStatus.GREEN,
			treeStatus.GREEN, treeStatus.GREEN, treeStatus.GREEN };

	@Override
	public boolean activate() {
		if (Main.SAVECPU){
			return (Players.getLocal().getAnimation() == -1);
		}
		return true;
	}

	public void dropOnlyThis(int...dropThis) {
			 for (Item i : Inventory.getItems()) {
				 for (int id : dropThis) {
						 if ((i.getId() == id)) {
								 i.getWidgetChild().interact("drop");
						 }
				 }
		}
		}
	
	@Override
	public void execute() {
		
		
		if (Inventory.getCount(1511) > 0){
			System.out.println("h");
			dropOnlyThis(1511);
		}
		
		getTreeStatus();
		int newTree = getCurrentTree();
		if (currentTree != newTree) {
			treeSwitch = true;
			currentTree = newTree;
		}
		if (Players.getLocal().getAnimation() == -1) {
			chopYews();
		}

	}

	public void chopYews() {
		if (Calculations.distanceTo(yewTiles[currentTree].getLocation()) < 5) {
			if (yewPositions[currentTree] == treeStatus.GREEN) {
				if (Players.getLocal().getAnimation() == -1
						|| treeSwitch == true) {

					status = "Clicking tree";
					if (SceneEntities
							.getAt(yewTiles[currentTree].getLocation()) != null)
						if (SceneEntities.getAt(
								yewTiles[currentTree].getLocation()).interact(
								"Chop")) {
							Timer x = new Timer(5000);
							while (x.isRunning()
									&& Players.getLocal().getAnimation() == -1) {
								Task.sleep(200);
							}
							if (Players.getLocal().getAnimation() != -1) {
								treeSwitch = false;
							}

						}

				} else {
					status = "Chopping";
					// return runControl();
				}
			} else {
				// wait
				status = "Waiting for spawn";
			}
		} else {
			status = "Moving to tree";
			Walking.walk(waitTiles[currentTree]);
		}

	}

	private void getTreeStatus() {
		for (int i = 0; i < yewTiles.length; i++) {

			if (Calculations.distanceTo((yewTiles[i].getLocation())) < 24) {
				SceneObject obj = SceneEntities
						.getAt(yewTiles[i].getLocation());
				// System.out.println("Tree #" + i + " " + obj.getId());

				if (obj != null) {
					if (obj.getId() == yewTreeID) {
						yewPositions[i] = treeStatus.GREEN;
					} else if (obj.getId() == yewTreeStumpID) {
						if (yewPositions[i] == treeStatus.GREEN) {
							treeTimer[i] = System.currentTimeMillis();
						}
						yewPositions[i] = treeStatus.BROWN;

					}
				}
			}
		}
	}

	int getCurrentTree() {
		// don't change if current tree is fine
		if (yewPositions[currentTree] == treeStatus.GREEN) {
			return currentTree;
		}

		// find a green tree first
		int lowestDist = -1;
		for (int i = 0; i < yewTiles.length; i++) {
			if (yewPositions[i] == treeStatus.GREEN) {
				lowestDist = i;
			}
		}

		if (lowestDist != -1) {
			// use GREEN ones first
			for (int i = 0; i < yewTiles.length; i++) {
				if (yewPositions[i] == treeStatus.GREEN) {
					if (Calculations.distanceTo(yewTiles[i]) < Calculations
							.distanceTo(yewTiles[lowestDist])) {

						lowestDist = i;
					}
				}
			}

			return lowestDist;
		}

		// then check for highest timer.NO GREEN TREES SHOULD BE LEFT
		int highestTimer = 0;
		for (int i = 0; i < yewTiles.length; i++) {
			long time = System.currentTimeMillis() - treeTimer[i];
			long htime = System.currentTimeMillis() - treeTimer[highestTimer];
			if (time > htime) {
				highestTimer = i;
			}
		}

		return highestTimer;
	}

}
