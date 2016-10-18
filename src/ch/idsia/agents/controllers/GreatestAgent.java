package ch.idsia.agents.controllers;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.environments.Environment;

public class GreatestAgent extends BasicMarioAIAgent implements Agent {
	public GreatestAgent() {
	    super("GreatestAgent");
	    reset();
	}
	
	public void reset () {
		action = new boolean[Environment.numberOfKeys];
	}
	
	public boolean[] getAction () {
		return action;
	}
}
