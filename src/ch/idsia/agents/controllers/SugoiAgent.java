package ch.idsia.agents.controllers;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.GeneralizerLevelScene;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.engine.sprites.Sprite;
import ch.idsia.benchmark.mario.environments.Environment;
import java.util.Random;

public class SugoiAgent extends BasicMarioAIAgent implements Agent {
	private Random r = null;

	public SugoiAgent() {
	    super("GreatestAgent");
	    reset();
	}
	
	public void reset () {
		r = new Random();
	    action = new boolean[Environment.numberOfKeys];
	    action[Mario.KEY_RIGHT] = true;
	}
	
	public boolean isObstacle(int r, int c){
		return getReceptiveFieldCellValue(r, c)==GeneralizerLevelScene.BRICK
				|| getReceptiveFieldCellValue(r, c)==GeneralizerLevelScene.BORDER_CANNOT_PASS_THROUGH
				|| getReceptiveFieldCellValue(r, c)==GeneralizerLevelScene.FLOWER_POT_OR_CANNON
				|| getReceptiveFieldCellValue(r, c)==GeneralizerLevelScene.LADDER;
	}
	
	public boolean isObstacleInFrontOfMario() {
		boolean retval = false;
		for (int cs = 1; cs <= 2; ++cs) {
			retval = retval || isObstacle(marioEgoRow, marioEgoCol + cs) || 
					getEnemiesCellValue(marioEgoRow, marioEgoCol + cs) != Sprite.KIND_FIREBALL &&
					getEnemiesCellValue(marioEgoRow, marioEgoCol + cs) != Sprite.KIND_NONE;
		}
		return retval;
	}
	
	public boolean isNothing(int r, int c) {
		return getReceptiveFieldCellValue(r, c) == 0 || !isObstacle(r, c);
	}
	
	public boolean isHillInFrontOfMario() {
		boolean retval = true;
		for (int row = marioEgoRow; row <= marioEgoRow + 4; ++row)
			retval = retval && isNothing(row, marioEgoCol + 1);
		return retval;
	}

	public boolean isLargeGap() {
		if (action[Mario.KEY_SPEED] && !isMarioOnGround)
			return true;
		boolean retval = true;
		for (int cs = 2; cs <= 5; ++cs)
			for (int row = marioEgoRow; row <= marioEgoRow + 5; ++row)
				retval = retval && isNothing(row, marioEgoCol + cs);
		return retval;
	}
	
	public boolean isBlockAboveMario() {
		boolean retval = false;
		for (int rs = 2; rs <= 3; ++rs)
			retval = retval || getReceptiveFieldCellValue(marioEgoRow - rs, marioEgoCol) == GeneralizerLevelScene.BRICK;
		return retval;
	}

	public boolean[] getAction () {
		if(isHillInFrontOfMario()) {
			System.out.println(String.format("detect gap in %d", distancePassedCells));
			action[Mario.KEY_RIGHT] = true;
			action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
			action[Mario.KEY_SPEED] = isLargeGap() && distancePassedCells > 5;
			if (action[Mario.KEY_SPEED])
				System.out.println(String.format("large gap in %d", distancePassedCells));
		}else if(isObstacleInFrontOfMario()){
			System.out.println(String.format("detect obstacle in %d", distancePassedCells));
			action[Mario.KEY_RIGHT] = true;
			action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
		}else if(isBlockAboveMario()){
			System.out.println(String.format("detect brick in %d", distancePassedCells));
			action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
		}else{
			action[Mario.KEY_RIGHT] = true;
			action[Mario.KEY_SPEED] = false; 
		}
	    return action;
	}
}
