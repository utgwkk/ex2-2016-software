/*
 * Copyright (c) 2009-2010, Sergey Karakovskiy and Julian Togelius
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Mario AI nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package ch.idsia.agents.controllers;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.mario.engine.GeneralizerLevelScene;
import ch.idsia.benchmark.mario.engine.sprites.Sprite;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: Apr 8, 2009
 * Time: 4:03:46 AM
 */

public class OwnAgent2 extends BasicMarioAIAgent implements Agent {
	public OwnAgent2() {
	    super("OwnAgent2");
	    reset();
	}
	
	public boolean isObstacle (int r, int c) {
		int val = getReceptiveFieldCellValue(r, c);
		return val == GeneralizerLevelScene.BRICK ||
				val == GeneralizerLevelScene.BORDER_CANNOT_PASS_THROUGH ||
				val == GeneralizerLevelScene.FLOWER_POT_OR_CANNON ||
				val == GeneralizerLevelScene.LADDER;
	}

	public boolean isEnemy (int r, int c) {
		return getEnemiesCellValue(r, c) != Sprite.KIND_NONE;
	}
	
	public boolean isObstacleInFrontOfMario () {
		for (int i = -2; i <= 0; i++) {
			for (int j = 0; j <= 2; j++) {
				if (isObstacle (marioEgoRow + i, marioEgoCol + j) || isEnemy(marioEgoRow + i, marioEgoCol + j))
					return true;
			}
		}
		return false;
	}
	
	public boolean isCliffInFrontOfMario () {
		for (int j = 1; j <= 4; ++j) {
			if (getReceptiveFieldCellValue(marioEgoRow + 1, marioEgoCol + j) != 0)
				return true;
		}
		return false;
	}
	
	public void reset() {
	    action = new boolean[Environment.numberOfKeys];
	    action[Mario.KEY_RIGHT] = true;
	    action[Mario.KEY_SPEED] = true; 
	}

	public boolean[] getAction() {
		action[Mario.KEY_JUMP] = !isMarioOnGround || isMarioAbleToJump && (isObstacleInFrontOfMario() || isCliffInFrontOfMario());
	    return action;
	}
}