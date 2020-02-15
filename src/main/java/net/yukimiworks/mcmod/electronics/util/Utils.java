package net.yukimiworks.mcmod.electronics.util;

import net.minecraft.util.text.TextComponentTranslation;
import org.lwjgl.util.Rectangle;

import javax.annotation.Nonnull;
import java.text.NumberFormat;

public final class Utils {

	/**
	 * 入力 X, Y が left, top, right, bottom で示される範囲内にあるかを検証する
	 * @param x 入力の X 座標
	 * @param y 入力の Y 座標
	 * @param left 範囲左上の X 座標
	 * @param top 範囲左上の Y 座標
	 * @param right 範囲右下の X 座標
	 * @param bottom 範囲左下の Y 座標
	 * @return 入力 X, Y が範囲内にある場合はtrue, 範囲外の場合はfalseを返す。また、同値である場合範囲内とする。
	 */
	public static boolean isInRect(int x, int y, int left, int top, int right, int bottom) {
		return x >= left && x <= right && y >= top && y <= bottom;
	}

	public static String getTranslatedUnformattedText(String translationKey, Object... args) {
		return new TextComponentTranslation(translationKey, args).getUnformattedText();
	}

	public static String formattedNumber(long number) {
		return NumberFormat.getInstance().format(number);
	}
}
