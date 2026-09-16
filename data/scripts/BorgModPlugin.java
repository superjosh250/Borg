package data.scripts;

import BORG.data.campaign.commodity.AssimilatedDrone;
import BORG.data.campaign.ids.BorgIDS;
import BORG.data.campaign.people.AdvancedBorgUnits;
import TERRAN.data.campaign.ids.TerranIDS;
import UFP.data.campaign.ids.DimensionsCrossedIDS;
import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import data.scripts.starsystems.Location001;
import BORG.data.campaign.industry.ResourceAssembler;

public class BorgModPlugin extends BaseModPlugin {

    @Override
    public void onApplicationLoad() throws Exception {
        super.onApplicationLoad();
    }

    @Override
    public void onGameLoad(boolean newGame) {
        AssimilatedDrone.install();
        AdvancedBorgUnits.injectSamaraUnit01_Planet001();
    }

    @Override
    public void onNewGameAfterEconomyLoad() {
        AdvancedBorgUnits.injectSamaraUnit01_Planet001();
    }

    @Override
    public void onNewGame() {
        SectorAPI sector = Global.getSector();
        AssimilatedDrone.install();

        new Location001().generate(sector);

        FactionAPI BORG = sector.getFaction(BorgIDS.BORG);
        FactionAPI UFP = sector.getFaction(DimensionsCrossedIDS.UFP);
        FactionAPI TERRAN_EMPIRE = sector.getFaction(TerranIDS.TERRAN);
        FactionAPI player = sector.getFaction(Factions.PLAYER);
        FactionAPI hegemony = sector.getFaction(Factions.HEGEMONY);
        FactionAPI tritachyon = sector.getFaction(Factions.TRITACHYON);
        FactionAPI pirates = sector.getFaction(Factions.PIRATES);
        FactionAPI independent = sector.getFaction(Factions.INDEPENDENT);
        FactionAPI church = sector.getFaction(Factions.LUDDIC_CHURCH);
        FactionAPI path = sector.getFaction(Factions.LUDDIC_PATH);
        FactionAPI kol = sector.getFaction(Factions.KOL);
        FactionAPI diktat = sector.getFaction(Factions.DIKTAT);
        FactionAPI persean = sector.getFaction(Factions.PERSEAN);
        FactionAPI guard = sector.getFaction(Factions.LIONS_GUARD);
        FactionAPI omega = sector.getFaction(Factions.OMEGA);

        if (BORG != null) {
            if (player != null) BORG.setRelationship(player.getId(), 0f);
            if (omega != null) BORG.setRelationship(omega.getId(), 0.5f);
            if (hegemony != null) BORG.setRelationship(hegemony.getId(), -1f);
            if (tritachyon != null) BORG.setRelationship(tritachyon.getId(), -1f);
            if (pirates != null) BORG.setRelationship(pirates.getId(), -1f);
            if (independent != null) BORG.setRelationship(independent.getId(), -0.75f);
            if (persean != null) BORG.setRelationship(persean.getId(), -1f);
            if (church != null) BORG.setRelationship(church.getId(), -1f);
            if (path != null) BORG.setRelationship(path.getId(), -1f);
            if (kol != null) BORG.setRelationship(kol.getId(), -1f);
            if (diktat != null) BORG.setRelationship(diktat.getId(), -1f);
            if (guard != null) BORG.setRelationship(guard.getId(), -1f);
            if (UFP != null) BORG.setRelationship(UFP.getId(), -1f);
            if (TERRAN_EMPIRE != null) {
                BORG.setRelationship(TERRAN_EMPIRE.getId(), -1f);
            }
        }
    }
}