package ch.idsia.agents.controllers;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;

public class GreatestAgent extends BasicMarioAIAgent implements Agent {
	boolean isKimeuchi = false;
	final int[] kimeuchi = {
		0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1 | 16, 
		8 | 1, 8 | 1, 8 | 2 | 16, 8 | 2 | 16, 8 | 2 | 16, 8 | 2 | 16, 8 | 2 | 16, 8 | 2 | 16, 2, 2
	};
	final int[] kimeuchi2 = {
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
		16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0,
	};
	/*
	 * 000001 left
	 * 000010 right
	 * 000100 down
	 * 001000 jump
	 * 010000 speed
	 * 100000 up
	 */
	int kimeuchiCount = 0;
	int kimeuchi2Count = 0;

	public GreatestAgent() {
	    super("GreatestAgent");
	    reset();
	}
	
	public void reset () {
	    action = new boolean[Environment.numberOfKeys];
	    action[Mario.KEY_RIGHT] = true;
	    action[Mario.KEY_SPEED] = true;
	}

	public boolean ableToSpeed () {
		return distancePassedCells >= 240 || distancePassedCells < 200 && (distancePassedCells < 40 || distancePassedCells > 50 && distancePassedCells < 122 || kimeuchiCount >= kimeuchi.length);
	}

	public boolean[] bitToAction (int bit) {
		boolean[] act = new boolean[Environment.numberOfKeys];
		for (int i = 0; i < Environment.numberOfKeys; ++i) {
			act[i] = (bit & (1 << i)) > 0;
		}
		return act;
	}
	
	public boolean[] getAction () {
		// ジャンプ可能または空中にいるならジャンプボタンを押しつづける
		if (isKimeuchi && kimeuchiCount < kimeuchi.length) {
			action = bitToAction(kimeuchi[kimeuchiCount]);
			kimeuchiCount++;
		} else if (distancePassedCells > 192 && isKimeuchi && kimeuchiCount >= kimeuchi.length && kimeuchi2Count < kimeuchi2.length) {
			action = bitToAction(kimeuchi2[kimeuchi2Count]);
			kimeuchi2Count++;
		} else {
			isKimeuchi = false;
		    action[Mario.KEY_JUMP] = isMarioAbleToJump || (!isMarioOnGround && action[Mario.KEY_JUMP]);
		    action[Mario.KEY_SPEED] = ableToSpeed();
		    action[Mario.KEY_RIGHT] = distancePassedCells < 123 || kimeuchiCount >= kimeuchi.length; 
		    if (distancePassedCells == 123 && kimeuchiCount < kimeuchi.length || distancePassedCells == 192 && kimeuchi2Count < kimeuchi2.length)
		        isKimeuchi = true;
		}
	    return action;
	}
}
