/*******************************************************************************
 * Besiege
 * by Kyle Dhillon
 * Source Code available under a read-only license. Do not copy, modify, or distribute.
 ******************************************************************************/
package kyle.game.besiege.army;

import kyle.game.besiege.*;
import kyle.game.besiege.location.City;
import kyle.game.besiege.location.Location;
import kyle.game.besiege.location.Village;
import kyle.game.besiege.party.PartyType.Type;
import kyle.game.besiege.voronoi.Center;

// A hunting party leaves its village and heads to a hunting destination at a nearby center. It basically wanders around
// that center for a few days, then comes back home.
public class HuntingParty extends Army {
	private final float WAIT_MIN = 0f;
	private final float WAIT_MAX = 4.5f;
	private final static float WANDER_DISTANCE = 75;
	private final static int HUNT_STOPS_MIN = 10;
	private final static int HUNT_STOPS_MAX = 20;

	private boolean waitToggle;
	private Center centerToHuntIn;

	private int huntStopsLeft;

	private Location location;

	private Point lastPos;

	public HuntingParty() {}

	public HuntingParty(Kingdom kingdom, String name, Faction faction,
                        float posX, float posY) {
		super(kingdom, name, faction, posX, posY, Type.FARMERS, null);
//		this.shouldEject = false;
		this.type = ArmyType.HUNTER;
		this.passive = true;
	}
	
	@Override
	public void uniqueAct() {
		hunt();
	}
	
//	@Override
//	public void drawUniqueInfo(SpriteBatch batch, float parentAlpha) {
//		Kingdom.arial.draw(batch, "Farming around " + village.getName(), getX(), getY() - getOffset()*getKingdom().getZoom());
//	}
	
	@Override
	public String getUniqueAction() {
		return "Hunting around " + location.getName();
	}

	@Override
	public void garrisonAct(float delta) {
//		shouldStopRunning();
		detectNearby();
		if (shouldStartHunting() && shouldStopRunning()) {
			//System.out.println("ejecting " + this.getName());
			// double check to see if fixes problem
//			detectNearby();
//			if (shouldStopRunning())
				eject();
				setNewHuntDest();
				this.huntStopsLeft = Random.getRandomInRange(HUNT_STOPS_MIN, HUNT_STOPS_MAX);
				this.lastPos = null;
		}
//		else System.out.println(runFrom.getName());
	}

	public boolean shouldStartHunting() {
		// Always hunt for now.
		return true;
	}

	private boolean shouldKeepHunting() {
		return huntStopsLeft > 0;
	}
	
	public void hunt() {
		if (shouldKeepHunting()) {
			if (this.isGarrisoned()) eject();
			if (this.path.isEmpty()) {
				if (waitToggle) {
					this.waitFor(Random.getRandomInRange(WAIT_MIN, WAIT_MAX));
					huntStopsLeft--;
//					System.out.println("waiting for " + WAIT);
					waitToggle = false;
				}
				else {
					setNewHuntDest();
					waitToggle = true;
				}
			}
			else {
				path.travel();
			}
		}
		else {
			if (this.getTarget() != location) setTarget(location);
		}
	}
	
	private void setNewHuntDest() {
		Point newTarget;
		int count = 0;
		do {
			float dx = (float) ((Math.random()*2-1)*WANDER_DISTANCE); //number btw -1 and 1
			float dy = (float) ((Math.random()*2-1)*WANDER_DISTANCE);
			if (lastPos == null)
				newTarget = new Point(centerToHuntIn.loc.x + dx, Map.HEIGHT - centerToHuntIn.loc.y + dy);
			else
				newTarget = new Point(lastPos.getX() + dx, lastPos.getY() + dy);

			lastPos = newTarget;
			count++;
		}
		while (getKingdom().getMap().isInWater(newTarget) && count < 10); // do this until sets a valid target 
		if (count == 10) System.out.println("count is 10");
		else setTarget(newTarget);
	}
	
	public void setLocation(Location location) {
		this.location = location;
		if (getKingdom().night)
			this.garrisonIn(location);
	}

	public void setCenterToHuntIn(Center center) {
		this.centerToHuntIn = center;
	}
	
	// farmers can garrison in villages
	@Override
	public Location detectNearbyFriendlyLocationForRunning() {
		for (City city : getKingdom().getCities()) {
			if (!isAtWar(city)) {
				if (this.distToCenter(city) < getLineOfSight() && this.distToCenter(city) < getRunFrom().distToCenter(city)) {
					return city;
				}
			}
		} 
		for (Village village : getKingdom().villages) {
			if (this.distToCenter(village) < getLineOfSight() && this.distToCenter(village) < getRunFrom().distToCenter(village)) {
				return village;
			}
		}
		return null;
	}
	
	public Location getLocation() {
		return location;
	}
	
	@Override
	public void destroy() {
		getKingdom().removeArmy(this);
		this.remove();
		location.removeHunter(this);
	}
}