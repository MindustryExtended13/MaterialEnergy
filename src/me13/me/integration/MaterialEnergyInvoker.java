package me13.me.integration;

import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import me13.core.block.instance.AdvancedBlock;
import me13.me.MaterialEnergy;
import me13.me.content.MeErekirBlocks;
import me13.me.content.MeSerpuloBlocks;
import me13.me.integration.mixin.*;
import me13.me.integration.mixin.base.IMeNetBaseMixin;
import me13.me.net.Netting;
import me13.me.world.blocks.Controller;
import me13.me.world.blocks.Terminal;
import me13.me.world.blocks.bus.EIBus;
import me13.me.world.blocks.storage.Adapter;
import me13.me.world.blocks.storage.MeStorageBlock;
import me13.me.world.blocks.storage.StorageMonitor;

import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.world.blocks.liquid.LiquidRouter;
import mindustry.world.blocks.storage.StorageBlock;

public class MaterialEnergyInvoker {
    public static final IMeBuildingMixin NO_STORAGE_MIXIN = new IMeBuildingMixin() {
        @Override
        public boolean canConnectTo(Building self, Building other) {
            return true;
        }

        @Override
        public boolean canUseMixin(Building self, IMeNetBaseMixin mixin) {
            return false;
        }
    };

    public static void load() {
        AdapterTypes.register(StorageBlock.StorageBuild.class);
        AdapterTypes.register(LiquidRouter.LiquidRouterBuild.class);

        NetMixins.register(new ItemNetMixin());
        NetMixins.register(new LiquidNetMixin());

        NetMixins.registerCable(MeSerpuloBlocks.cable);
        NetMixins.registerCable(MeErekirBlocks.cable);

        BuildingMixins.register(StorageMonitor.StorageMonitorBuild.class, NO_STORAGE_MIXIN);
        BuildingMixins.register(Controller.ControllerBuild.class, NO_STORAGE_MIXIN);
        BuildingMixins.register(Terminal.TerminalBuild.class, NO_STORAGE_MIXIN);
        BuildingMixins.register(EIBus.EIBusBuild.class, NO_STORAGE_MIXIN);

        Vars.content.blocks().each(block -> {
            if(block.name.startsWith("me-") && BuildingMixins.isMeBuilding(block.buildType.get())) {
                BuildingMixins.meBlocks.add(block);
            }
        });

        BuildingMixins.register(MeStorageBlock.MeStorageBuild.class, new IMeBuildingMixin() {
            @Override
            public boolean canConnectTo(Building self, Building other) {
                return true;
            }

            @Override
            public int getChannels(Building self) {
                return ((MeStorageBlock) self.block).channels;
            }
        });

        BuildingMixins.register(Adapter.AdapterBuild.class, new IMeBuildingMixin() {
            @Override
            public boolean canConnectTo(Building self, Building other) {
                return other != null && self != null && other != ((AdvancedBlock.AdvancedBuild) self).nearby();
            }

            @Override
            public Building getMixinBuild(Building self) {
                var nearby = ((AdvancedBlock.AdvancedBuild) self).nearby();
                return AdapterTypes.isAdaptive(nearby) && self.team == nearby.team ? nearby : self;
            }
        });

        Vars.content.items().each(item -> {
            BoxMixins.register(new IMeBoxMixin() {
                @Override
                public String name() {
                    return item.localizedName;
                }

                @Override
                public TextureRegion icon() {
                    return item.uiIcon;
                }

                @Override
                public float getCount(Building building) {
                    return Netting.getCapFor(building, ItemNetMixin.class, item);
                }

                @Override
                public String format(float value) {
                    return MaterialEnergy.formatItem(Mathf.round(value));
                }
            });
        });

        Vars.content.liquids().each(liquid -> {
            BoxMixins.register(new IMeBoxMixin() {
                @Override
                public String name() {
                    return liquid.localizedName;
                }

                @Override
                public TextureRegion icon() {
                    return liquid.uiIcon;
                }

                @Override
                public float getCount(Building building) {
                    return Netting.getCapFor(building, LiquidNetMixin.class, liquid);
                }

                @Override
                public String format(float value) {
                    return MaterialEnergy.formatLiquid(Mathf.round(value));
                }
            });
        });
    }
}