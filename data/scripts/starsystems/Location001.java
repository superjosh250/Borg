package data.scripts.starsystems;
import java.awt.Color;
import java.util.Random;

import BORG.data.campaign.ids.BorgIDS;
import UFP.data.campaign.ids.DimensionsCrossedIDS;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.impl.campaign.terrain.MagneticFieldTerrainPlugin.MagneticFieldParams;

public class Location001 {

    // Deterministic seed so the “random locations” don’t change every new game load.
    private static final long SEED = 100198017L;

    public void generate(SectorAPI sector) {
        Random rand = new Random(SEED);


        final float BELT_RADIUS = 9800f;
        final float BELT_WIDTH  = 800f;
        final float BELT_MIN_DAYS = 160f;
        final float BELT_MAX_DAYS = 240f;
        final float RING_ORBIT_DAYS = (BELT_MIN_DAYS + BELT_MAX_DAYS) * 0.5f;

        final float YARD_ANGLE_A = 90f;
        final float YARD_ANGLE_B = (YARD_ANGLE_A + 180f) % 360f;



        // --- Create system ---
        StarSystemAPI system = sector.createStarSystem("Location001");
        system.getLocation().set(-8480f, 35000f);
        system.setBackgroundTextureFilename("graphics/backgrounds/background_borg.jpg");

        // A modest yellow star; habitable zone will be around ~6k–9k SU in this layout.
        PlanetAPI star = system.initStar(
                "loc001_star",
                "star_yellow",
                700f, // star radius (in pixels)
                500f  // corona radius
        );

        // Give the system a comfortable map boundary. (Not “real”, but good engine scale.)
        system.setMaxRadiusInHyperspace(32000f);

        // --- Planet orbits (compressed AU-like spacing) ---

        // Inner rocky
        PlanetAPI p1 = system.addPlanet(
                "loc001_ferro",
                star,
                "Ferro",
                "rocky_metallic",
                15f,
                85f,
                2400f,
                90f
        );

        // Inner barren
        PlanetAPI p2 = system.addPlanet(
                "loc001_cinder",
                star,
                "Cinder",
                "barren",
                65f,
                70f,
                3800f,
                130f
        );

        // Habitable zone target: "assimilated-terran"
        PlanetAPI terran = system.addPlanet(
                "loc001_assimilated_terran",
                star,
                "Planet 001",
                "assimilated-terran",
                120f,
                155f,
                7200f, // Habitable zone placement (for this star scale)
                220f
        );
        addBorg1Market(sector, terran);

        // Unimatrix Station (custom entity)
        SectorEntityToken utopiaPlanitiaStation = system.addCustomEntity(
                "unimatrix0001",
                "Station Designation 001",
                "unimatrix",
                "borg");
        utopiaPlanitiaStation.setOrbit(Global.getFactory().createCircularOrbit(terran, 10f, 1400f, 270f));

        // Optional: a small moon for flavor (won’t break requirements)
        PlanetAPI terranMoon = system.addPlanet(
                "loc001_assimila_moon",
                terran,
                "Echo",
                "rocky_ice",
                20f,
                35f,
                520f,
                35f
        );

        // Asteroid belt region between terran and giants
        system.addAsteroidBelt(
                star,
                260,
                9800f,
                800f,
                160f, 240f,
                Terrain.ASTEROID_BELT,
                "Thick Belt"
        );

        // Thick asteroid field (single, heavy concentration)
        system.addAsteroidBelt(
                star,
                260,
                9800f,
                800f,
                160f,
                240f,
                Terrain.ASTEROID_BELT,
                "Thick Belt Stuck"
        );

        system.addRingBand(
                star,
                "misc",                // texture category (settings.json)
                "rings_asteroids0",    // vanilla ring texture id; swap to rings_dust0 / rings_ice0 if desired
                256f,                  // bandWidthInTexture
                0,                     // bandIndex (which strip in the texture)
                new Color(255, 255, 255, 255), // tint; reduce alpha if you want it subtler
                BELT_WIDTH,            // bandWidthInEngine
                BELT_RADIUS,           // middleRadius
                RING_ORBIT_DAYS        // orbitDays
        );

        CustomCampaignEntityAPI uplinkA = system.addCustomEntity(
                "loc001_uplink_yard_01",
                "Uplink Yard Alpha",
                "uplink_yard",
                "borg"
        );
        uplinkA.setCircularOrbitPointingDown(star, YARD_ANGLE_A, BELT_RADIUS, RING_ORBIT_DAYS);

        CustomCampaignEntityAPI uplinkB = system.addCustomEntity(
                "loc001_uplink_yard_02",
                "Uplink Yard Beta",
                "uplink_yard",
                "borg"
        );
        uplinkB.setCircularOrbitPointingDown(star, YARD_ANGLE_B, BELT_RADIUS, RING_ORBIT_DAYS);


        // Gas giant #1
        PlanetAPI g1 = system.addPlanet(
                "loc001_zephyrus",
                star,
                "Zephyrus",
                "gas_giant",
                200f,
                260f,
                13200f,
                360f
        );

        // Gas giant #2
        PlanetAPI g2 = system.addPlanet(
                "loc001_nadir",
                star,
                "Nadir",
                "gas_giant",
                245f,
                240f,
                17200f,
                520f
        );

        // Outer cold world (random-ish pick)
        PlanetAPI p6 = system.addPlanet(
                "loc001_rime",
                star,
                "Rime",
                "frozen",
                310f,
                90f,
                22800f,
                780f
        );

        // --- Jump point ~750f from assimilated-terran ---
        JumpPointAPI jp = Global.getFactory().createJumpPoint(
                "loc001_assimila_jump",
                "Assimila Jump-point"
        );
        jp.setRelatedPlanet(terran);
        jp.setCircularOrbit(terran, 90f, 700f, 140f);
        jp.setStandardWormholeToHyperspaceVisual();
        system.addEntity(jp);

        // --- 2 Comm Relays ---
        CustomCampaignEntityAPI relay1 = system.addCustomEntity(
                "loc001_comm_01",
                "Comm Relay Alpha",
                Entities.COMM_RELAY,
                "borg"
        );
        relay1.setCircularOrbitPointingDown(star, 95f, 8600f, 210f);

        CustomCampaignEntityAPI relay2 = system.addCustomEntity(
                "loc001_comm_02",
                "Comm Relay Beta",
                Entities.COMM_RELAY,
                "borg"
        );
        relay2.setCircularOrbitPointingDown(star, 210f, 19600f, 510f);

        // --- 4 Gates (inactive) ---
        addGate(system, g1, "loc001_gate_01", "Gate I", randAngle(rand), 5200f, 900f);
        addGate(system, g2, "loc001_gate_02", "Gate II", randAngle(rand), 7200f, 980f);
        addGate(system, g1, "loc001_gate_03", "Gate III", randAngle(rand), 3900f, 1080f);
        addGate(system, g2, "loc001_gate_04", "Gate IV", randAngle(rand), 3100f, 1180f);

        // --- Derelict stations in random-ish locations ---
        for (int i = 1; i <= 3; i++) {
            float r = lerp(5200f, 21000f, rand.nextFloat());
            float a = randAngle(rand);
            float p = lerp(180f, 700f, rand.nextFloat());
            addDerelictStation(system, star, "loc001_derelict_" + i, "Derelict Station " + i, a, r, p);
        }

        // --- Magnetic field ---
        MagneticFieldParams magParams = new MagneticFieldParams(
                450f,
                1400f,
                g1,
                1050f,
                1750f,
                new Color(60, 120, 220, 70),
                1.25f,
                new Color(120, 200, 255),
                new Color(190, 120, 255),
                new Color(80, 240, 180)
        );

        CampaignTerrainAPI magField = (CampaignTerrainAPI) system.addTerrain(Terrain.MAGNETIC_FIELD, magParams);
        magField.setCircularOrbit(g1, 0f, 0f, 100f);

        // --- Finish: ensure hyperspace visuals get generated nicely ---
        system.autogenerateHyperspaceJumpPoints(true, true);
    }

    // =========================================================
// Planet 001 market setup (Population 10 + conditions + industries)
// =========================================================
    private void addBorg1Market(SectorAPI sector, PlanetAPI terran) {

        // IMPORTANT: Use the planet/market id you intend to reference later
        // (e.g. in onNewGameAfterEconomyLoad()).
        MarketAPI market = Global.getFactory().createMarket(
                "loc001_assimilated_terran",   // market id (YOU requested this)
                "Planet 001",                  // market name
                10                             // market size
        );

        // Core ownership + entity binding
        terran.setFaction(BorgIDS.BORG);
        market.setPrimaryEntity(terran);
        market.setFactionId(BorgIDS.BORG);

        // --- Market Conditions ---
        market.addCondition(Conditions.POPULATION_10);
        market.addCondition(Conditions.REGIONAL_CAPITAL);
        market.addCondition(Conditions.TERRAN);
        market.addCondition(Conditions.HABITABLE);
        market.addCondition(Conditions.HOT);
        market.addCondition(Conditions.FARMLAND_RICH);
        market.addCondition(Conditions.ORGANICS_ABUNDANT);
        market.addCondition(DimensionsCrossedIDS.DILITHIUM_ORE);
        market.addCondition(Conditions.ORE_ABUNDANT);
        market.addCondition(Conditions.RARE_ORE_MODERATE);
        market.addCondition(Conditions.VOLATILES_DIFFUSE);
        market.addCondition(Conditions.ESTABLISHED_POLITY);

        // --- Industries ---
        market.addIndustry(BorgIDS.ASSIMILATED_POPULATION);
        market.addIndustry(BorgIDS.ASSIMILATED_SPACEPORT);
        market.addIndustry(BorgIDS.TERMINUS_WAYSTATION);
        market.addIndustry(BorgIDS.RESOURCE_ALLOCATOR);
        market.addIndustry(BorgIDS.RESOURCE_ASSEMBLER);
        market.addIndustry(BorgIDS.SHIPYARD);
        market.addIndustry(BorgIDS.TECHNOLOGY_ASSEMBLER);
        market.addIndustry(BorgIDS.ASSIMILATION_NODE);
        market.addIndustry(DimensionsCrossedIDS.SALVAGE_OPS);
        market.addIndustry(BorgIDS.FLEET_PATROL);
        market.addIndustry(BorgIDS.ORBITALSTATION_UNIMATRIX);

        // --- AI Cores & Items ---
        if (market.hasIndustry(BorgIDS.RESOURCE_ALLOCATOR)) {
            market.getIndustry(BorgIDS.RESOURCE_ALLOCATOR).setAICoreId(Commodities.OMEGA_CORE);
        }
        if (market.hasIndustry(BorgIDS.SHIPYARD)) {
            market.getIndustry(BorgIDS.SHIPYARD).setSpecialItem(new SpecialItemData(Items.PRISTINE_NANOFORGE, null));
            market.getIndustry(BorgIDS.SHIPYARD).setAICoreId(Commodities.ALPHA_CORE);
        }
        if (market.hasIndustry(BorgIDS.RESOURCE_ASSEMBLER)) {
            market.getIndustry(BorgIDS.RESOURCE_ASSEMBLER).setAICoreId(Commodities.OMEGA_CORE);
        }
        if (market.hasIndustry(BorgIDS.TECHNOLOGY_ASSEMBLER)) {
            market.getIndustry(BorgIDS.TECHNOLOGY_ASSEMBLER).setSpecialItem(new SpecialItemData(Items.PRISTINE_NANOFORGE, null));
            market.getIndustry(BorgIDS.TECHNOLOGY_ASSEMBLER).setAICoreId(Commodities.OMEGA_CORE);
        }

        // --- Submarkets ---
        market.addSubmarket(Submarkets.SUBMARKET_OPEN);
        market.addSubmarket(Submarkets.GENERIC_MILITARY);
        market.addSubmarket(Submarkets.SUBMARKET_BLACK);
        market.addSubmarket(Submarkets.SUBMARKET_STORAGE);

        // Apply + register
        terran.setMarket(market);
        sector.getEconomy().addMarket(market, true);

        // Let industries finalize now that it's in the economy
        market.reapplyIndustries();

        // DO NOT set admin / comm directory here.
        // It may be overwritten later during economy load lifecycle, which is why NPCs often "don't show".
        // Instead, mark the market so a ModPlugin can attach Samara in onNewGameAfterEconomyLoad().
        market.getMemoryWithoutUpdate().set("$borg_add_samara_admin", true);
        market.getMemoryWithoutUpdate().set("$borg_samara_admin_id", "samara_unit01");
    }


    private static void addGate(StarSystemAPI system, SectorEntityToken star,
                                String id, String name, float angle, float radius, float orbitDays) {
        CustomCampaignEntityAPI gate = system.addCustomEntity(
                id, name, Entities.INACTIVE_GATE, Factions.NEUTRAL
        );
        gate.setCircularOrbitPointingDown(star, angle, radius, orbitDays);
    }

    private static void addDerelictStation(StarSystemAPI system, SectorEntityToken star,
                                           String id, String name, float angle, float radius, float orbitDays) {
        CustomCampaignEntityAPI derelict = system.addCustomEntity(
                id,
                name,
                Entities.DERELICT_SURVEY_PROBE,
                Factions.NEUTRAL
        );

        derelict.setCircularOrbitPointingDown(star, angle, radius, orbitDays);
        derelict.setSensorProfile(32000f);
        derelict.setDiscoverable(true);

        derelict.addDropValue("basic", 15000);
        derelict.addDropValue("indus", 10000);
        derelict.addDropRandom("blueprints", 2);
        derelict.addDropRandom("weapon", 3);
        derelict.addDropRandom("ai_cores", 1);

        derelict.getMemoryWithoutUpdate().set("$salvage_special_data", true);
        derelict.getMemoryWithoutUpdate().set("$isSalvageable", true);
        derelict.getMemoryWithoutUpdate().set("$salvage_size", 3);
    }

    private static float randAngle(Random rand) {
        return rand.nextFloat() * 360f;
    }

    private static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }
}