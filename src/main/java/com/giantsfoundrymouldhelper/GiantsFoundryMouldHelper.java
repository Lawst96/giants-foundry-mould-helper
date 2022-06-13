package com.giantsfoundrymouldhelper;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.widgets.Widget;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.ColorUtil;

import javax.inject.Inject;
import java.awt.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.IntStream;

@Slf4j
@PluginDescriptor(name = "Giants' Foundry Mould Helper")
public class GiantsFoundryMouldHelper extends Plugin {
    private static final int GIANT_FOUNDRY_REGION_ID = 13491;

    private static final int[] GIANT_FOUNDRY_SCRIPT_IDS = {6092, 6093, 6095, 6098, 6108};

    private static final HashMap<String, String[]> OPTIMAL_COMMISSIONS;

    static {
        OPTIMAL_COMMISSIONS = new HashMap<>();
        OPTIMAL_COMMISSIONS.put("Broad Heavy", new String[]{FORTES.DEFENDER_BASE.name, BLADES.FLEUR_DE_BLADE.name, TIPS.DEFENDERS_TIP.name});
        OPTIMAL_COMMISSIONS.put("Broad Light", new String[]{FORTES.STILETTO_FORTE.name, BLADES.CHOPPA.name, TIPS.SERRATED_TIP.name});
        OPTIMAL_COMMISSIONS.put("Broad Spiked", new String[]{FORTES.SPIKER.name, BLADES.CLAYMORE_BLADE.name, TIPS.DEFENDERS_TIP.name});
        OPTIMAL_COMMISSIONS.put("Broad Flat", new String[]{FORTES.CHOPPER_FORTE_1.name, BLADES.CLAYMORE_BLADE.name, TIPS.THE_POINT.name});
        OPTIMAL_COMMISSIONS.put("Narrow Heavy", new String[]{FORTES.SERRATED_FORTE.name, BLADES.FLEUR_DE_BLADE.name, TIPS.NEEDLE_POINT.name});
        OPTIMAL_COMMISSIONS.put("Narrow Light", new String[]{FORTES.STILETTO_FORTE.name, BLADES.CHOPPA.name, TIPS.NEEDLE_POINT.name});
        OPTIMAL_COMMISSIONS.put("Narrow Spiked", new String[]{FORTES.SPIKER.name, BLADES.FLAMBERGE_BLADE.name, TIPS.NEEDLE_POINT.name});
        OPTIMAL_COMMISSIONS.put("Narrow Flat", new String[]{FORTES.CHOPPER_FORTE_1.name, BLADES.SERPENT_BLADE.name, TIPS.THE_POINT.name});
        OPTIMAL_COMMISSIONS.put("Heavy Spiked", new String[]{FORTES.SPIKER.name, BLADES.FLEUR_DE_BLADE.name, TIPS.SAW_TIP.name});
        OPTIMAL_COMMISSIONS.put("Heavy Flat", new String[]{FORTES.DEFENDER_BASE.name, BLADES.FLEUR_DE_BLADE.name, TIPS.THE_POINT.name});
        OPTIMAL_COMMISSIONS.put("Light Spiked", new String[]{FORTES.SPIKER.name, BLADES.CHOPPA.name, TIPS.SERRATED_TIP.name});
        OPTIMAL_COMMISSIONS.put("Light Flat", new String[]{FORTES.CHOPPER_FORTE_1.name, BLADES.CHOPPA.name, TIPS.THE_POINT.name});
    }

    @Inject
    private Client client;

    @Override
    protected void startUp() throws Exception {
        log.info("Giants' foundry optimizer started!");
    }

    @Override
    protected void shutDown() throws Exception {
        log.info("Giants' foundry optimizer stopped!");
    }

    private boolean isInGiantsFoundry() {
        if (client.getLocalPlayer() == null) {
            return false;
        }

        return WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation()).getRegionID() == GIANT_FOUNDRY_REGION_ID;
    }

    @Subscribe
    public void onScriptPostFired(ScriptPostFired event) {
        if (client.getGameState() != GameState.LOGGED_IN || !isInGiantsFoundry() || IntStream.of(GIANT_FOUNDRY_SCRIPT_IDS).noneMatch(id -> id == event.getScriptId())) {
            return;
        }

        String commission = Objects.requireNonNull(client.getWidget(718, 27)).getText() + " " + Objects.requireNonNull(client.getWidget(718, 29)).getText();

        String[] combination = OPTIMAL_COMMISSIONS.get(commission);

        Widget[] mouldsArray = Objects.requireNonNull(Objects.requireNonNull(client.getWidget(718, 9)).getChildren());

        for (int i = 2; i < mouldsArray.length; i += 17) {
            if (mouldsArray[i].getText().equals(combination[0]) || mouldsArray[i].getText().equals(combination[1]) || mouldsArray[i].getText().equals(combination[2])) {
                String text = ColorUtil.wrapWithColorTag(mouldsArray[i].getText(), Color.green);
                mouldsArray[i].setText(text);
            }
        }
    }

}
