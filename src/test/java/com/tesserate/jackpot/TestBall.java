package com.tesserate.jackpot;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestBall {

	@Test
	public void testCollide(){
		Ball b1 = new Ball("");
		Ball b2 = new Ball("");
		b1.setPosition(0, 0);
		b2.setPosition(100,200);
		
		assertFalse(b1.isCollide(b2));
	}
}
