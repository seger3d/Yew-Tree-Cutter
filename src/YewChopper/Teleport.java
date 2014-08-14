package YewChopper;

import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;

import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.DepositBox;


public class Teleport extends Node {

	public static Tile x = new Tile(3011, 3215, 0);
	public static Tile bankTile = new Tile(3047, 3236, 0);
	Area bankArea = new Area(new Tile[] { new Tile(3015, 3213, 0),
			new Tile(3016, 3242, 0), new Tile(3055, 3241, 0),
			new Tile(3054, 3212, 0) });

	Area treeArea = new Area(new Tile[] { new Tile(3006, 3204, 0),
			new Tile(2914, 3205, 0), new Tile(2914, 3244, 0),
			new Tile(3006, 3245, 0) });

	@Override
	public boolean activate() {
		return ((bankArea.contains(Players.getLocal().getLocation()) && Inventory
				.getCount() <= 1) || treeArea.contains(Players.getLocal()
				.getLocation()) && Inventory.isFull());
	}

	@Override
	public void execute() {

		if (DepositBox.isOpen()) {
			DepositBox.close();
		} else {

			if (Widgets.get(548).getChild(123).isOnScreen()
					&& !Widgets.get(548).getChild(123).equals(null)) {
				if (Widgets.get(548).getChild(123).click(true)) {

					if (Widgets.get(275).getChild(40).isOnScreen()
							&& !Widgets.get(275).getChild(40).equals(null)) {
						if (Widgets.get(275).getChild(40).click(true)) {

							if (Widgets.get(275).getChild(47).isOnScreen()
									&& !Widgets.get(275).getChild(47)
											.equals(null)) {
								if (Widgets.get(275).getChild(47).click(true)) {

									if (Widgets.get(275).getChild(16)
											.getChild(155).isOnScreen()
											&& !Widgets.get(275).getChild(16)
													.getChild(155).equals(null)) {
										if (Widgets.get(275).getChild(16)
												.getChild(155).click(true)) {

											Timer l = new Timer(5000);

											while (!Widgets.get(1092)
													.getChild(45).isOnScreen()
													&& l.isRunning()) {

												sleep(200, 400);
											}

											if (Widgets.get(1092).getChild(48)
													.isOnScreen()
													&& !Widgets.get(1092)
															.getChild(48)
															.equals(null)) {
												if (Widgets.get(1092)
														.getChild(48)
														.click(true)) {

													Task.sleep(5000, 7000);

													while (Players.getLocal()
															.getAnimation() == 16385) {
														sleep(300, 500);
													}

												}
											}

										}
									}

								}
							}

						}
					}
				}

			}
		}

	}
}