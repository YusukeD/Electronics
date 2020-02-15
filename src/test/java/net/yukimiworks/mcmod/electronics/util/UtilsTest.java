package net.yukimiworks.mcmod.electronics.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class UtilsTest {

	@Test
	public void testIsInRect_X_左側境界値外側() throws Exception {
		int mouseX = 49;
		int mouseY = 75;

		int left = 50;
		int top = 50;
		int right = 150;
		int bottom = 150;

		assertEquals(false, Utils.isInRect(mouseX, mouseY, left, top, right, bottom));
	}

	@Test
	public void testIsInRect_X_左側境界値上() throws Exception {
		int mouseX = 50;
		int mouseY = 75;

		int left = 50;
		int top = 50;
		int right = 150;
		int bottom = 150;

		assertEquals(true, Utils.isInRect(mouseX, mouseY, left, top, right, bottom));
	}

	@Test
	public void testIsInRect_X_左側境界値内側() throws Exception {
		int mouseX = 51;
		int mouseY = 75;

		int left = 50;
		int top = 50;
		int right = 150;
		int bottom = 150;

		assertEquals(true, Utils.isInRect(mouseX, mouseY, left, top, right, bottom));
	}

	@Test
	public void testIsInRect_X_右側境界値内側() throws Exception {
		int mouseX = 149;
		int mouseY = 75;

		int left = 50;
		int top = 50;
		int right = 150;
		int bottom = 150;

		assertEquals(true, Utils.isInRect(mouseX, mouseY, left, top, right, bottom));
	}

	@Test
	public void testIsInRect_X_右側境界値上() throws Exception {
		int mouseX = 150;
		int mouseY = 75;

		int left = 50;
		int top = 50;
		int right = 150;
		int bottom = 150;

		assertEquals(true, Utils.isInRect(mouseX, mouseY, left, top, right, bottom));
	}

	@Test
	public void testIsInRect_X_右側境界値外側() throws Exception {
		int mouseX = 151;
		int mouseY = 75;

		int left = 50;
		int top = 50;
		int right = 150;
		int bottom = 150;

		assertEquals(false, Utils.isInRect(mouseX, mouseY, left, top, right, bottom));
	}

	@Test
	public void testIsInRect_Y_上側境界値外側() throws Exception {
		int mouseX = 75;
		int mouseY = 49;

		int left = 50;
		int top = 50;
		int right = 150;
		int bottom = 150;

		assertEquals(false, Utils.isInRect(mouseX, mouseY, left, top, right, bottom));
	}

	@Test
	public void testIsInRect_Y_上側境界値上() throws Exception {
		int mouseX = 75;
		int mouseY = 50;

		int left = 50;
		int top = 50;
		int right = 150;
		int bottom = 150;

		assertEquals(true, Utils.isInRect(mouseX, mouseY, left, top, right, bottom));
	}

	@Test
	public void testIsInRect_Y_上側境界値内側() throws Exception {
		int mouseX = 75;
		int mouseY = 51;

		int left = 50;
		int top = 50;
		int right = 150;
		int bottom = 150;

		assertEquals(true, Utils.isInRect(mouseX, mouseY, left, top, right, bottom));
	}

	@Test
	public void testIsInRect_Y_下側境界値内側() throws Exception {
		int mouseX = 75;
		int mouseY = 149;

		int left = 50;
		int top = 50;
		int right = 150;
		int bottom = 150;

		assertEquals(true, Utils.isInRect(mouseX, mouseY, left, top, right, bottom));
	}

	@Test
	public void testIsInRect_Y_下側境界値上() throws Exception {
		int mouseX = 75;
		int mouseY = 150;

		int left = 50;
		int top = 50;
		int right = 150;
		int bottom = 150;

		assertEquals(true, Utils.isInRect(mouseX, mouseY, left, top, right, bottom));
	}

	@Test
	public void testIsInRect_Y_下側境界値外側() throws Exception {
		int mouseX = 75;
		int mouseY = 151;

		int left = 50;
		int top = 50;
		int right = 150;
		int bottom = 150;

		assertEquals(false, Utils.isInRect(mouseX, mouseY, left, top, right, bottom));
	}
}
