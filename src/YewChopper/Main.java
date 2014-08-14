package YewChopper;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.Rectangle;

import org.powerbot.core.event.events.MessageEvent;
import org.powerbot.core.event.listeners.MessageListener;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Summoning;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.methods.widget.Lobby;
import org.powerbot.game.api.methods.widget.WidgetCache;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.GroundItem;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.bot.Context;
import org.powerbot.game.client.Client;

import YewChopper.WoodCutting.treeStatus;

@Manifest(authors = "Zerg", name = "YewChops", version = 1.1, description = "Script is in alpha. Testing procedures on the SDN")
public class Main extends ActiveScript implements PaintListener {

	// Variables
	boolean SAVECPURETURN = true;
	public static final boolean SAVECPU = false;
	public static int goldEarned = 0;
	public int logID = 1515;
	public int logs;
	public int logPrice;
	long startTime = System.currentTimeMillis();
	public static Timer antibanTimer;
	private Client client = Context.client();

	private Tree jobs = null;

	public void onStart() {
		System.out.println("Starting Yew Chops");
		antibanTimer = new Timer(20000);
		Mouse.setSpeed(Mouse.Speed.FAST);
		logPrice = Integer.parseInt(getPrice(logID));
	}

	public int loop() {
		if (Game.getClientState() != Game.INDEX_MAP_LOADED) {
			return 1000;
		}

		if (client != Context.client()) {
			WidgetCache.purge();
			Context.get().getEventManager().addListener(this);
			client = Context.client();
		}

		if (Widgets.get(1401).getChild(37).isOnScreen()) {
			Widgets.get(1401).getChild(37).click(true);
		}

		if (jobs == null) {
			jobs = new Tree(new Node[] { new CombatAlert(), new Antiban(), new Teleport(),
					new Deposit(), new WalkToBox(), new WalkToTree(),
					new WoodCutting() });
		}
		final Node job = jobs.state();
		if (job != null) {
			jobs.set(job);
			getContainer().submit(job);
			job.join();
			return 100;
		}
		if (SAVECPURETURN){
			return Random.nextInt(800, 1000);
		}
		return Random.nextInt(300, 400);
	}

	public static int getTimeLeft() {
		return Math.round((Settings.get(1786) / (float) 2.13333333333));
	}

	public static boolean enableRun() {
		if (!(Settings.get(463) == 1)) {
			Widgets.get(750, 2).click(true);
		}
		return true;
	}

	public static boolean isSummoned() {
		return (getTimeLeft() > 0);
	}

	public static void turnCamera() {
		int yaw = Camera.getYaw();
		if (yaw < 90) {
			Camera.setAngle(Random.nextInt(0, 100));
		} else {
			Camera.setAngle(Random.nextInt(80, 180));
		}

	}

	public void drawTile3D(Tile tile, Graphics2D g, int height) {
		Point[] pointsGround = { tile.getPoint(0, 0, 0),
				tile.getPoint(1, 0, 0), tile.getPoint(1, 1, 0),
				tile.getPoint(0, 1, 0), tile.getPoint(0, 0, 0) };
		Point[] pointsHeight = { tile.getPoint(0, 0, height),
				tile.getPoint(1, 0, height), tile.getPoint(1, 1, height),
				tile.getPoint(0, 1, height), tile.getPoint(0, 0, height) };
		int[] xPointsGround = { pointsGround[0].x, pointsGround[1].x,
				pointsGround[2].x, pointsGround[3].x, pointsGround[4].x };
		int[] yPointsGround = { pointsGround[0].y, pointsGround[1].y,
				pointsGround[2].y, pointsGround[3].y, pointsGround[4].y };
		int[] xPointsHeight = { pointsHeight[0].x, pointsHeight[1].x,
				pointsHeight[2].x, pointsHeight[3].x, pointsHeight[4].x };
		int[] yPointsHeight = { pointsHeight[0].y, pointsHeight[1].y,
				pointsHeight[2].y, pointsHeight[3].y, pointsHeight[4].y };
		g.drawPolyline(xPointsGround, yPointsGround, xPointsGround.length);
		g.drawPolyline(xPointsHeight, yPointsHeight, xPointsHeight.length);
		for (int i = 0; i < pointsGround.length; i++) {
			g.drawLine(xPointsGround[i], yPointsGround[i], xPointsHeight[i],
					yPointsHeight[i]);
		}
	}

	@Override
	public void onRepaint(Graphics g1) {
		if (!Game.isLoggedIn()
				|| Game.getClientState() != Game.INDEX_MAP_LOADED
				|| Lobby.isOpen()) {
			return;
		}

		int x, y;
		x = Mouse.getX();
		y = Mouse.getY();
		Graphics2D g = (Graphics2D) g1;

		g.setColor(Color.DARK_GRAY);
		//g.fill(new Rectangle.Double(0, 0, 120, 50));

		g.setColor(Color.white);
		g.drawString("Time running: " + Time.format((System.currentTimeMillis() - startTime)), 60,
				20);
		
		// drawTile3D(Players.getLocal().getLocation(),g,-1000);

		for (int i = 0; i < WoodCutting.yewTiles.length; i++) {

			if (WoodCutting.yewPositions[i] == treeStatus.GREEN) {
				g.setColor(Color.green);
				// drawTile3D(WoodCutting.yewTiles[i],g,0);
				g.drawString("Tree " + (i + 1), 20, (i * 10) + 10);
			} else {
				g.setColor(Color.red);
				// drawTile3D(WoodCutting.yewTiles[i],g,0);
				g.drawString(
						((int) (System.currentTimeMillis() - WoodCutting.treeTimer[i]) / 1000)
								+ "", 20, (i * 10) + 10);
			}

		}

		// }
		g.setColor(Color.white);

		// MOUSE
		Font font = new Font("Arial", Font.PLAIN, 20);
		g.setFont(font);
		g.drawString("+", x - 7, y + 9);

		/*
		 * // Credit to Buccaneer //
		 * http://www.powerbot.org/community/topic/722969
		 * -tut-how-to-make-a-paint-for-beginners/ while (!mousePath.isEmpty()
		 * && mousePath.peek().isUp()) mousePath.remove(); Point clientCursor =
		 * Mouse.getLocation(); MousePathPoint mpp = new
		 * MousePathPoint(clientCursor.x, clientCursor.y, 300); // 1000 =
		 * lasting time/MS if (mousePath.isEmpty() ||
		 * !mousePath.getLast().equals(mpp)) mousePath.add(mpp); MousePathPoint
		 * lastPoint = null; for (MousePathPoint a : mousePath) { if (lastPoint
		 * != null) { g.drawLine(a.x, a.y, lastPoint.x, lastPoint.y); }
		 * lastPoint = a; }
		 */
	}

	// Credit to Buccaneer
	// http://www.powerbot.org/community/topic/722969-tut-how-to-make-a-paint-for-beginners/
	private final LinkedList<MousePathPoint> mousePath = new LinkedList<MousePathPoint>();

	@SuppressWarnings("serial")
	private class MousePathPoint extends Point { // All credits to Enfilade

		private long finishTime;

		public MousePathPoint(int x, int y, int lastingTime) {
			super(x, y);
			finishTime = System.currentTimeMillis() + lastingTime;
		}

		public boolean isUp() {
			return System.currentTimeMillis() > finishTime;
		}
	}

	public String getPrice(int itemID) {
		try {
			String[] info = { "0", "0" };
			final URL url = new URL(
					"http://www.tip.it/runescape/index.php?gec&itemid="
							+ itemID);
			final BufferedReader br = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String input;
			while ((input = br.readLine()) != null) {
				if (input.startsWith("<h2>")) {
					info[0] = input.substring(4, input.length() - 5);
				}
				if (input
						.startsWith("<tr><td colspan=\"4\"><b>Current Market Price: </b>")) {
					info[1] = input.substring(49, input.lastIndexOf("gp"))
							.replaceAll(",", "");
					return info[1];
				}
			}
		} catch (final Exception ignored) {
		}
		return null;
	}

	private Image getImage(String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (IOException e) {
			return null;
		}
	}



}
