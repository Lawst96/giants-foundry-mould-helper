package com.giantsfoundrymouldhelper;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class GiantsFoundryOptimizerTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(GiantsFoundryMouldHelper.class);
		RuneLite.main(args);
	}
}