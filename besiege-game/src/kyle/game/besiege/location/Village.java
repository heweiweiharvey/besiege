/*******************************************************************************
 * Besiege
 * by Kyle Dhillon
 * Source Code available under a read-only license. Do not copy, modify, or distribute.
 ******************************************************************************/
package kyle.game.besiege.location;


import com.badlogic.gdx.utils.Array;

import kyle.game.besiege.Faction;
import kyle.game.besiege.Kingdom;
import kyle.game.besiege.army.Army;
import kyle.game.besiege.army.Farmer;
import kyle.game.besiege.party.CultureType;
import kyle.game.besiege.party.PartyType;
import kyle.game.besiege.voronoi.Biomes;
import kyle.game.besiege.voronoi.Center;

public class Village extends Location {
    private final static float VILLAGE_WEALTH_FACTOR = 0.5f; // arbitrary, this times pop = wealth
	private static final int MAX_FARMERS = 5;
	private static final float RAID_COUNTDOWN = 120;
//	private final int MED_WEALTH = 50;

	private final String textureRegion = "Village";
		
	public Array<Farmer> farmers;
	
	public float raidTimer = 0;

	public Village(){}
	
	public Village(Kingdom kingdom, String name, int index, Faction faction,
			float posX, float posY, Center center) {
        super(kingdom, name, index, faction, posX, posY, PartyType.Type.VILLAGE_GARRISON, center, null);
        this.type = LocationType.VILLAGE;

        setCenter(center);

        this.DAILY_WEALTH_INCREASE_BASE = 1;
        this.DAILY_POP_INCREASE_BASE = 0.01;

        POP_MIN = 100;
        POP_MAX = 1000;

        this.population = Math.random() * (POP_MAX - POP_MIN) + POP_MIN;

        if (cultureType.name.equals("Plains")) {
            setTextureRegion("tipi");
        } else if (cultureType.name.equals("Forest")) {
            setTextureRegion("longhouse");
        } else if (cultureType.name.equals("Tundra")) {
            if (center.biome == Biomes.TUNDRA || center.biome == Biomes.SNOW)
                setTextureRegion("inuit2");
            else setTextureRegion("inuitgreen");
        } else if (cultureType.name.equals("Desert")){
            setTextureRegion("desert-village");
        }
        else {
		    setTextureRegion(textureRegion);
        }

        farmers = new Array<Farmer>();
		initializeBox();
	}
	
	@Override
	protected void setCenter(Center c) {
		super.setCenter(c);
		setWealth(calcInitialWealth());
	}

	// only do this when center has been set.
	public int calcInitialWealth() {
		return (int) ((VILLAGE_WEALTH_FACTOR + this.getCenter().wealth) * population);
	}
	
	@Override
	public void autoManage() {
		// Organize farmers
		int farmerCount = (int) getWealth()/100 + 1; // arbitrary
		if (farmers.size < farmerCount) {
			createFarmer();
		}
	}

	public void createFarmer() {
		if (this.farmers.size >= MAX_FARMERS) return;
		Farmer farmer = new Farmer(getKingdom(), getName() + " Farmers", getFaction(), getCenterX(), getCenterY());
		getKingdom().addArmy(farmer);
		farmer.setVillage(this);
		farmers.add(farmer);
		setContainerForArmy(farmer);
	}
	
	public void removeFarmer(Farmer farmer) {
		farmers.removeValue(farmer,true);
	}
//	public void setParent(City city) {
//		parent = city;
//	}
	@Override
	public boolean isVillage() {
		return true;
	}
	
	
	public void handleRaidVictory(Army raider) {
		this.raidTimer = RAID_COUNTDOWN;
		System.out.println("handling raid victory");
		this.addFire();
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		if (this.raidTimer > 0) {
			this.raidTimer -= delta;
			if (this.raidTimer < 0) this.removeFire();
		}
	}
	
//	public Militia createMilitia() {
//		Militia militia = new Militia(getKingdom(), getName() + " Militia", getFaction(), getCenterX(), getCenterY());
//		militia.setVillage(this);
//		// transfer all wealth to militia
//		militia.getParty().wealth = this.getParty().wealth;
//		// also decrease village wealth temporarily.
//		this.getParty().wealth = 0;
//		militia.type = ArmyType.MILITIA;
//		return militia;
//	}
	
	@Override
	public void updateToHire() {
//		if (playerIn) System.out.println("updating to hire");
		this.toHire = this.nextHire;
		// some random stuff should happen here, small chance of getting a really good crop of soldiers
		// high chance of sucky ones!
		
		this.nextHire = PartyType.generatePT(PartyType.Type.VILLAGE_HIRE, this).generate();
//		System.out.println("updating to hire for village: " + this.getName());
//		System.out.println(toHire.getAvgSpd());
//		double random = Math.random();
//		double upper;
//		double lower;
//		if (this.getParty().wealth > HIGH_WEALTH) { 
//			lower = .2; 
//			upper = .6;
//		}
//		else if (this.getParty().wealth > MED_WEALTH) {
//			lower = .4; 
//			upper = .7;
//		}
//		else {
//			lower = .5; 
//			upper = .9;
//		}
		// TODO
//		if (random > upper) 		this.nextHire = PartyType.VILLAGE_HIRE_1.generate();
//		else if (random > lower)	this.nextHire = PartyType.VILLAGE_HIRE_2.generate(); // second best
//		else 					this.nextHire = PartyType.VILLAGE_HIRE_3.generate(); // worst
	}
}
