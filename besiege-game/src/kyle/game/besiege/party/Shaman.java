package kyle.game.besiege.party;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import kyle.game.besiege.Assets;
import kyle.game.besiege.MultiValue;
import kyle.game.besiege.Random;

import static kyle.game.besiege.party.Equipment.MASK_SKULL;
import static kyle.game.besiege.party.Equipment.MASK_WOLF;

// Represents a Shaman or priest unit.
// Shamans are a special unit type that don't fight in battle.
// There is a limited number of Shamans for each
public class Shaman extends Soldier {
    private MultiValue power; // represents a Shaman or priest's spiritual power. in reality, it's an AOE.
    private Type type;

    // These types of shamans give different powers to their subparties during battle.
    // They also increase the healing rate of all units in the subparty.
    // They march into battle behind their BSPs, like their general
    enum Type {
        // Forest
        WOLF("Wolf Shaman", "Forest", "weapon_name", "armor_name", MASK_WOLF),
        // Tundra
        BEAR("Bear Shaman", "Tundra", "weapon_name", "armor_name", MASK_SKULL),
        // Plains
        HAWK("Hawk Shaman", "Plains", "weapon_name", "armor_name", MASK_SKULL),
        // Desert
        DEATH("Death Priest", "Desert", "weapon_name", "armor_name", MASK_SKULL);
        public UnitType unitType;
        private Equipment mask;
        // TODO add weapon (staff), armor, and headdress
        Type(String name, String cultureName, String weapon, String armor, Equipment mask) {
            this.mask = mask;
            weapon = "Staff";
            armor = "Clothes";
            this.unitType = new UnitType();
            this.unitType.tier = 1; // start as level 1 unit
            this.unitType.name = name;
            this.unitType.cultureType = UnitLoader.cultureTypes.get(cultureName); // TODO
            this.unitType.melee = UnitLoader.weaponTypes.get(weapon);
            this.unitType.armor = UnitLoader.armorTypes.get(armor);
        }
    }

    // Instead of attack, Shamans have a "power" level, which corresponds to their healing and AOE
    // Generates a random type for this party.
    public Shaman(Party party) {
        this.party = party;

        // For now, just make the culture accoring to the general. TODO make this more flexible.
        this.type = getRandomType(party.getGeneral().unitType.cultureType);
        this.unitType = type.unitType;

        this.assignBaseStats();
        this.init();

        this.atk = null;
        this.power = new MultiValue();
    }

    @Override
    public boolean isShaman() {
        return true;
    }

    public Type getRandomType(CultureType cultureType) {
         // Fuck it do random sampling.
        Type random = null;
        while (random == null || random.unitType.cultureType != cultureType) {
            random = (Type) Random.getRandomValue(Type.values());
        }
        return random;
    }

    @Override
    public Equipment getHead() {
        return this.type.mask;
    }

    @Override
    public float getAtk() {
        return 0;
    }
}