package me13.me.net;

import arc.func.Cons2;
import arc.math.Mathf;
import arc.struct.Seq;
import me13.me.configuration.Channels;
import me13.me.integration.BuildingMixins;
import me13.me.integration.NetMixins;
import me13.me.integration.mixin.IMeNetMixin;
import me13.me.integration.mixin.IMeSingleNetMixin;
import me13.me.integration.mixin.ItemNetMixin;
import me13.me.integration.mixin.LiquidNetMixin;
import me13.me.integration.mixin.base.IMeNetBaseMixin;
import me13.me.world.blocks.Controller;
import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;

@SuppressWarnings("unchecked")
public class Netting {
    public static boolean inNet(Building building, Building host) {
        if(building != null && building.team == host.team) {
            if(BuildingMixins.isMeBuilding(building)) {
                var mixin = BuildingMixins.getMixin(building);
                return mixin != null && mixin.canConnectTo(building, host);
            } else return NetMixins.getCables().contains(building.block);
        }

        return false;
    }

    public static Seq<Building> getConnections(Building building) {
        Seq<Building> result = new Seq<>();
        var mixin = BuildingMixins.getMixin(building);
        if(mixin != null) {
            mixin.getChildren(building).each(build -> {
                if(inNet(build, building)) {
                    result.add(build);
                }
            });
        } else if(building.enabled()) {
            building.proximity.each(build -> {
                if(inNet(build, building)) {
                    result.add(build);
                }
            });
        }
        return result;
    }

    public static void getConnections(Building source, Seq<Building> buildings) {
        getConnections(source).each(b -> {
            if(!buildings.contains(b)) {
                buildings.add(b);
                getConnections(b, buildings);
            }
        });
    }

    public static boolean isNetEnabled(Building building) {
        return getChannels(building) <= getMaximumChannels(building);
    }

    public static int getMaximumChannels(Building building) {
        var result = new Seq<Building>();
        Netting.getConnections(building, result);
        int channels = 0;
        for(var build : result) {
            if(build instanceof Controller.ControllerBuild controller) {
                channels += controller.getChannelsAdd();
            }
        }
        return channels + Channels.conf.getDefaultChannels();
    }

    public static int getChannels(Building building) {
        var result = new Seq<Building>();
        Netting.getConnections(building, result);
        int channels = 0;
        for(var build : result) {
            var mixin = BuildingMixins.getMixin(build);
            if(mixin != null) {
                channels += mixin.getChannels(build);
            }
        }
        return channels;
    }

    public static Seq<IMeNetBaseMixin> getMixinsOf(Building building) {
        Seq<IMeNetBaseMixin> mixins = new Seq<>();
        var mixin = BuildingMixins.getMixin(building);
        if(mixin != null) {
            var host2 = mixin.getMixinBuild(building);
            var host = host2 == null ? building : host2;
            mixins.addAll(mixin.getMixinsSelf(host));
            NetMixins.getMixins().each(mixin2 -> {
                if(mixin.canUseMixin(host, mixin2) && mixin2.haveMixinIn(host)) {
                    mixins.add(mixin2);
                }
            });
        } else {
            NetMixins.getMixins().each(mixin2 -> {
                if(mixin2.haveMixinIn(building)) {
                    mixins.add(mixin2);
                }
            });
        }
        return mixins;
    }

    public static<T extends IMeNetBaseMixin> void eachMixin(Building building,
                                                            Class<T> mixin,
                                                            Cons2<T, Building> cons) {
        var result = new Seq<Building>();
        Netting.getConnections(building, result);
        for(var build : result) {
            var mixin2 = BuildingMixins.getMixin(build);
            if(mixin2 == null) {
                var mixins = getMixinsOf(build);
                mixins.each(mixin3 -> {
                    if(mixin3.getClass() == mixin || mixin3.getClass().getSuperclass() == mixin) {
                        cons.get((T) mixin3, build);
                    }
                });
            } else {
                var host2 = mixin2.getMixinBuild(build);
                var host = host2 == null ? build : host2;
                var mixins = getMixinsOf(host);
                mixins.each(mixin3 -> {
                    if(mixin3.getClass() == mixin || mixin3.getClass().getSuperclass() == mixin) {
                        cons.get((T) mixin3, host);
                    }
                });
            }
        }
    }

    public static<T extends IMeSingleNetMixin> float includeToNet(Building building,
                                                                  Class<T> mixinClass,
                                                                  float amount) {
        final float[] BUFFER = {0, amount};
        eachMixin(building, mixinClass, (T mixin, Building build) -> {
            if(BUFFER[1] <= 0) {
                return;
            }

            float stored = mixin.getStored(build);
            float add = Math.min(mixin.getMaximumStored(build) - stored, BUFFER[1]);
            BUFFER[0] += add;
            BUFFER[1] -= add;
            mixin.add(build, add);
        });
        return BUFFER[1] < 0 ? 0 : BUFFER[1];
    }

    public static LiquidStack includeToNet(Building building, LiquidStack stack) {
        Liquid liquid = stack.liquid;
        final float[] BUFFER = {0, stack.amount};
        eachMixin(building, LiquidNetMixin.class, (LiquidNetMixin mixin, Building build) -> {
            if(BUFFER[1] <= 0) {
                return;
            }

            float stored = mixin.getStored(build, liquid);
            float add = Math.min(mixin.getMaximumAccepted(build, liquid) - stored, BUFFER[1]);
            BUFFER[0] += add;
            BUFFER[1] -= add;
            mixin.add(build, liquid, add);
        });
        return new LiquidStack(liquid, BUFFER[1] < 0 ? 0 : BUFFER[1]);
    }

    public static ItemStack includeToNet(Building building, ItemStack stack) {
        Item item = stack.item;
        final float[] BUFFER = {0, stack.amount};
        eachMixin(building, ItemNetMixin.class, (ItemNetMixin mixin, Building build) -> {
            if(BUFFER[1] <= 0) {
                return;
            }

            float stored = mixin.getStored(build, item);
            float add = Math.min(mixin.getMaximumAccepted(build, item) - stored, BUFFER[1]);
            BUFFER[0] += add;
            BUFFER[1] -= add;
            mixin.add(build, item, add);
        });
        return new ItemStack(item, Mathf.round(BUFFER[1] < 0 ? 0 : BUFFER[1]));
    }

    public static<T extends IMeSingleNetMixin> float excludeFromNet(Building building,
                                                                    Class<T> mixinClass,
                                                                    float amount) {
        final float[] BUFFER = {0, amount};
        eachMixin(building, mixinClass, (T mixin, Building build) -> {
            if(BUFFER[1] <= 0) {
                return;
            }

            float stored = mixin.getStored(build);
            stored = Math.min(stored, BUFFER[1]);
            BUFFER[0] += stored;
            BUFFER[1] -= stored;
            mixin.remove(build, stored);
        });
        return BUFFER[0];
    }

    public static LiquidStack excludeFromNet(Building building, LiquidStack stack) {
        Liquid liquid = stack.liquid;
        final float[] BUFFER = {0, stack.amount};
        eachMixin(building, LiquidNetMixin.class, (LiquidNetMixin mixin, Building build) -> {
            if(BUFFER[1] <= 0) {
                return;
            }

            float stored = mixin.getStored(build, liquid);
            stored = Math.min(stored, BUFFER[1]);
            BUFFER[0] += stored;
            BUFFER[1] -= stored;
            mixin.remove(build, liquid, stored);
        });
        return new LiquidStack(liquid, BUFFER[0]);
    }

    public static ItemStack excludeFromNet(Building building, ItemStack stack) {
        Item item = stack.item;
        final float[] BUFFER = {0, stack.amount};
        eachMixin(building, ItemNetMixin.class, (ItemNetMixin mixin, Building build) -> {
            float stored = mixin.getStored(build, item);
            stored = Math.min(stored, BUFFER[1]);
            BUFFER[0] += stored;
            BUFFER[1] -= stored;
            mixin.remove(build, item, stored);
        });
        return new ItemStack(item, Mathf.round(BUFFER[0]));
    }

    public static<T, G extends IMeNetMixin<T>> float getCapFor(Building building, Class<G> mixin, T type) {
        float[] total = new float[] {0};
        eachMixin(building, mixin, (G m, Building b) -> {
            total[0] += m.getStored(b, type);
        });
        return total[0];
    }

    public static<T, G extends IMeNetMixin<T>> float getTotalCapFor(Building building, Class<G> mixin, T type) {
        float[] total = new float[] {0};
        eachMixin(building, mixin, (G m, Building b) -> {
            total[0] += m.getMaximumAccepted(b, type);
        });
        return total[0];
    }

    public static<T extends IMeSingleNetMixin> float getSingleCapFor(Building building, Class<T> mixin) {
        float[] total = new float[] {0};
        eachMixin(building, mixin, (T m, Building b) -> {
            total[0] += m.getStored(b);
        });
        return total[0];
    }

    public static<T extends IMeSingleNetMixin> float getSingleTotalCapFor(Building building, Class<T> mixin) {
        float[] total = new float[] {0};
        eachMixin(building, mixin, (T m, Building b) -> {
            total[0] += m.getMaximumStored(b);
        });
        return total[0];
    }
}