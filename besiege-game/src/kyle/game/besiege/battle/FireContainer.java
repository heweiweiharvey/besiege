package kyle.game.besiege.battle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;

public class FireContainer extends Group {

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}
	public void addFire(Fire fire) {
		this.addActor(fire);
//		fire.setKingdomRotation(90);
	}
	
	public void updateRotation(float rotation) {
		setRotation(rotation);
	}
}
