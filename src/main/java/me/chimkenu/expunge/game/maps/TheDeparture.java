package me.chimkenu.expunge.game.maps;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.game.Dialogue;
import me.chimkenu.expunge.game.Director;
import me.chimkenu.expunge.guns.utilities.healing.Adrenaline;
import me.chimkenu.expunge.guns.utilities.healing.Medkit;
import me.chimkenu.expunge.guns.utilities.healing.Pills;
import me.chimkenu.expunge.guns.weapons.melees.*;
import me.chimkenu.expunge.mobs.common.Horde;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TheDeparture extends Map {
    public enum DepartureDialogue {
        OFFICE_OPENING {
            @Override
            public Dialogue getA() {
                return new Dialogue(null,
                        "A » Hey, wake up!",
                        "B » What’s going on? What time is it?",
                        "A » It’s lunch… And it looks like we’re the main course.",
                        "B » We need to get to the elevator…",
                        "A » There’s weapons outside we can use."
                );
            }

            @Override
            public Dialogue getB() {
                return new Dialogue(null,
                        "A » What’s happening?",
                        "B » Looks like Halloween came early.",
                        "A » I don’t think those are costumes…",
                        "B » Either way, let’s get to the elevator and get out of here.",
                        "A » I can see weapons outside we can use."
                );
            }

            @Override
            public Dialogue getSolo() {
                return new Dialogue(null,
                        "A » The office is infested with the living dead… Well, that’s not new.",
                        "A » I need to get to the elevator. I think there’s weapons and gear right outside the door."
                );
            }
        },
        OFFICE_ELEVATOR {
            @Override
            public Dialogue getA() {
                return new Dialogue(null,
                        "A » We’re stopping.",
                        "B » The elevator’s shutting down.",
                        "A » Come on, we gotta find the stairs."
                );
            }

            @Override
            public Dialogue getB() {
                return new Dialogue(null,
                        "A » Uh-oh.",
                        "B » There goes the power.",
                        "A » Let’s try the fire exit."
                );
            }

            @Override
            public Dialogue getSolo() {
                return new Dialogue(null,
                        "A » The power’s down… I need to get to the fire exit."
                );
            }
        },
        OFFICE_JUMP {
            @Override
            public Dialogue getA() {
                return new Dialogue(null, "A » We gotta jump!");
            }

            @Override
            public Dialogue getB() {
                return new Dialogue(null, "B » Quick, through the gaping hole in the wall!");
            }

            @Override
            public Dialogue getSolo() {
                return new Dialogue(null, "A » The only way forward is down!");
            }
        },
        OFFICE_VENTS {
            @Override
            public Dialogue getA() {
                return new Dialogue(null,
                        "A » Into the vents!",
                                "B » Careful, we don’t know what’s in there."
                );
            }

            @Override
            public Dialogue getB() {
                return new Dialogue(null,
                        "A » The exit’s blocked by this rubble!",
                                "B » Look, the vent! Go now!"
                );
            }

            @Override
            public Dialogue getSolo() {
                return new Dialogue(null, "A » The only way around this mess is the vents.");
            }
        },
        OFFICE_SAFE_ROOM {
            @Override
            public Dialogue getA() {
                return new Dialogue(null,
                        "A » I think I see another room!",
                                "B » Give me a second, I’m covered in zombie guts!"
                );
            }

            @Override
            public Dialogue getB() {
                return new Dialogue(null,
                        "A » Quick, I see another room!",
                                "B » I never want to do this again."
                );
            }

            @Override
            public Dialogue getSolo() {
                return new Dialogue(null,
                        "A » I need to hurry up and get into that other room!"
                );
            }
        },
        OFFICE_RADIO {
            final Dialogue dialogue = new Dialogue(null, "&7[Radio] » Attention all citizens, there will be an evacuation site situated at the Takahashi Football Stadium. Avoid contact with all infected individuals and head towards the Takahashi Football Stadium and wait for rescue.");
            @Override
            public Dialogue getA() {
                return dialogue;
            }

            @Override
            public Dialogue getB() {
                return dialogue;
            }

            @Override
            public Dialogue getSolo() {
                return dialogue;
            }
        },
        STREETS_OPENING {
            @Override
            public Dialogue getA() {
                return new Dialogue(null,
                        "A » Where do we go now?",
                                "B » We can get to the football stadium if we go towards the subway.",
                                "A » For now, let’s just head further down the road."
                );
            }

            @Override
            public Dialogue getB() {
                return new Dialogue(null,
                        "A » How do we get to the football stadium?",
                                "B » We need to get to the subway first.",
                                "A » Alright, let’s go further down the road."
                );
            }

            @Override
            public Dialogue getSolo() {
                return new Dialogue(null, "A » I can get closer to the football stadium if I head towards the subway further down the road.");
            }
        },
        STREETS_APARTMENTS {
            @Override
            public Dialogue getA() {
                return new Dialogue(null,
                        "A » There’s a radio tower in our way.",
                                "B » We should try getting around it through the Apartment building!"
                );
            }

            @Override
            public Dialogue getB() {
                return new Dialogue(null,
                        "A » The road’s blocked!",
                                "B » Let’s try getting to the other side through the Apartment Building."
                );
            }

            @Override
            public Dialogue getSolo() {
                return new Dialogue(null, "A » My only option seems to be getting to the other side through the Apartment Building.");
            }
        },
        STREETS_SHED {
            @Override
            public Dialogue getA() {
                return new Dialogue(null,
                        "A » Down the fire escape!",
                                "B » Head into the shed!"
                );
            }

            @Override
            public Dialogue getB() {
                return new Dialogue(null,
                        "A » Climb down the ladder!",
                                "B » Let’s go into the shed!"
                );
            }

            @Override
            public Dialogue getSolo() {
                return new Dialogue(null, "A » I need to get down and into the shed.");
            }
        },
        ALLEYS_OPENING {
            @Override
            public Dialogue getA() {
                return new Dialogue(null,
                        "A » Seems like the road splits here.",
                                "B » Stick to a path, maybe we’ll see the other side of the main road."
                );
            }

            @Override
            public Dialogue getB() {
                return new Dialogue(null,
                        "A » The road forks here, which way do we go?",
                                "B » Follow a path, and I think we’ll end back up on the main road."
                );
            }

            @Override
            public Dialogue getSolo() {
                return new Dialogue(null, "A » If I get through here, maybe I’ll end up on the other side of the main road.");
            }
        },
        PURPLE_CAR {
            final Dialogue dialogue = new Dialogue(null, "A » How’d this car get in here?");
            @Override
            public Dialogue getA() {
                return dialogue;
            }

            @Override
            public Dialogue getB() {
                return dialogue;
            }

            @Override
            public Dialogue getSolo() {
                return dialogue;
            }
        },
        ALLEYS_SAFE_HOUSE {
            @Override
            public Dialogue getA() {
                return new Dialogue(null,
                        "A » Other survivors have definitely been here before us.",
                                "B » There’s another safe room here!"
                );
            }

            @Override
            public Dialogue getB() {
                return new Dialogue(null,
                        "A » Look, people have been here!",
                                "B » Quickly, into the safe room!"
                );
            }

            @Override
            public Dialogue getSolo() {
                return new Dialogue(null, "A » Gotta get into that safe room!");
            }
        },
        SUBWAY_OPENING {
            @Override
            public Dialogue getA() {
                return new Dialogue(null,
                        "A » We made it..!",
                                "B » We’re not home free yet. Let’s enter the subway to get to the evacuation site."
                );
            }

            @Override
            public Dialogue getB() {
                return new Dialogue(null,
                        "A » There’s the subway station!",
                                "B » If we get across, we can get closer to the evacuation site."
                );
            }

            @Override
            public Dialogue getSolo() {
                return new Dialogue(null, "A » The subway station is across the street. I need to get through it to find the evacuation site.");
            }
        },
        SUBWAY_MR_COOKIE {
            @Override
            public Dialogue getA() {
                return new Dialogue(null,
                        "A » Hold on, I’ve got a quarter.",
                                "A » I’m gonna name you “Mr. Cookie”.",
                                "B » That’s one stale biscuit."
                );
            }

            @Override
            public Dialogue getB() {
                return new Dialogue(null,
                        "A » I’ll name you “Mr. Cookie”.",
                                "B » You’re not going to eat that..?",
                                "A » It’s too tough, it’d probably shatter my teeth."
                );
            }

            @Override
            public Dialogue getSolo() {
                return new Dialogue(null, "A » Yeah, I’m gonna name this biscuit “Mr. Cookie”.");
            }
        },
        SUBWAY_MAP {
            @Override
            public Dialogue getA() {
                return new Dialogue(null,
                        "A » According to the map, we should take Station B, then head forwards until the road forks.",
                                "A » From there, we head right.",
                                "B » … This map is impossible to read."
                );
            }

            @Override
            public Dialogue getB() {
                return new Dialogue(null,
                        "A » Let’s see… Based on the map, we should take Station B, then keep going straight until the rails split.",
                                "A » Then, we take the right side.",
                                "B » Looking at this “map” makes my head spin."
                );
            }

            @Override
            public Dialogue getSolo() {
                return new Dialogue(null, "A » Go to Station B, follow the railroad then head right when the road forks. Simple enough.");
            }
        },
        SUBWAY_SAFE_ZONE {
            @Override
            public Dialogue getA() {
                return new Dialogue(null, "A » Take cover in that passenger train!");
            }

            @Override
            public Dialogue getB() {
                return new Dialogue(null, "A » Everyone, into the passenger car!");
            }

            @Override
            public Dialogue getSolo() {
                return new Dialogue(null, "A » I need to get into this passenger train!");
            }
        },
        HIGHWAY_MANHOLE {
            @Override
            public Dialogue getA() {
                return new Dialogue(null,
                        "A » Oh great, the line’s blocked!",
                                "B » Don’t panic, There’s a ladder over there."
                );
            }

            @Override
            public Dialogue getB() {
                return new Dialogue(null,
                        "A » The rest of the subway is blocked.",
                                "B » Hey, there’s a ladder over there!"
                );
            }

            @Override
            public Dialogue getSolo() {
                return new Dialogue(null, "A » The subway line is blocked… Maybe I can head up that ladder over there instead.");
            }
        },
        HIGHWAY_OPENING {
            @Override
            public Dialogue getA() {
                return new Dialogue(null,
                        "A » We’re in the middle of nowhere.",
                                "B » Don't worry, we’re still on the right track.",
                                "A » Look, that billboard over there says we’re close to the stadium!",
                                "B » Let’s keep going then."
                );
            }

            @Override
            public Dialogue getB() {
                return new Dialogue(null,
                        "A » Where are we?",
                                "B » We’re on the highway, we can still make it.",
                                "A » Hey, that billboard says we’re nearing the stadium!",
                                "B » Come on, then."
                );
            }

            @Override
            public Dialogue getSolo() {
                return new Dialogue(null,
                        "A » I made it to the highway…",
                        "A » That billboard says I’m close to the stadium, I should keep going.");
            }
        },
        HIGHWAY_HELICOPTER {
            @Override
            public Dialogue getA() {
                return new Dialogue(null,
                        "A » Hey, watch out!",
                                "B » That just alerted the horde, run!"
                );
            }

            @Override
            public Dialogue getB() {
                return new Dialogue(null,
                        "A » Look above!",
                                "B » I think that just caught the horde’s attention!"
                );
            }

            @Override
            public Dialogue getSolo() {
                return new Dialogue(null, "A » That helicopter’s gonna attract a horde, I need to run!");
            }
        },
        HIGHWAY_SAFE_HOUSE {
            @Override
            public Dialogue getA() {
                return new Dialogue(null,
                        "A » Great, the road’s blocked again!",
                                "B » Let’s get inside the gas station!"
                );
            }

            @Override
            public Dialogue getB() {
                return new Dialogue(null,
                        "A » Look above!",
                        "B » I think that just caught the horde’s attention!"
                );
            }

            @Override
            public Dialogue getSolo() {
                return new Dialogue(null, "A » That helicopter’s gonna attract a horde, I need to run!");
            }
        },
        HIGHWAY_PURPLE_CAR {
            final Dialogue dialogue = new Dialogue(null, "A » How’d that car get up there?");
            @Override
            public Dialogue getA() {
                return dialogue;
            }

            @Override
            public Dialogue getB() {
                return dialogue;
            }

            @Override
            public Dialogue getSolo() {
                return dialogue;
            }
        };

        public abstract Dialogue getA();
        public abstract Dialogue getB();
        public abstract Dialogue getSolo();
    }

    private static void playDialogue(DepartureDialogue dialogue) {
        if (Expunge.playing.getKeys().size() > 1) {
            if (Math.random() < 0.5)
                dialogue.getA().displayDialogue(Expunge.playing.getKeys());
            else
                dialogue.getB().displayDialogue(Expunge.playing.getKeys());
        }
        else
            dialogue.getSolo().displayDialogue(Expunge.playing.getKeys());
    }

    private static ArrayList<Scene> scenes() {
        ArrayList<Scene> scenes = new ArrayList<>();
        World world = Bukkit.getWorld("world");
        if (world == null) return scenes;

        // scene 0 - office
        ArrayList<BoundingBox> pathRegions = new ArrayList<>();
        pathRegions.add(new BoundingBox(950, 8, 891, 963, 8, 901));
        pathRegions.add(new BoundingBox(963, 8, 903, 955, 8, 915));
        pathRegions.add(new BoundingBox(953, 8, 903, 936, 8, 915));
        ArrayList<Location> spawnLocations = new ArrayList<>();
        spawnLocations.add(new Location(world, 972, 9, 876));
        spawnLocations.add(new Location(world, 939.5, 9, 877.5));
        spawnLocations.add(new Location(world, 934, 10, 898));
        spawnLocations.add(new Location(world, 964.5, 9, 902.5));
        spawnLocations.add(new Location(world, 956, 9, 920));
        ArrayList<Location> bossLocations = new ArrayList<>();
        bossLocations.add(new Location(world, 951.5, 9, 909.5));
        ArrayList<Location> itemLocations = new ArrayList<>();
        itemLocations.add(new Location(world, 956, 10, 878.5));
        itemLocations.add(new Location(world, 958, 10, 878.5));
        itemLocations.add(new Location(world, 950, 10, 890));
        itemLocations.add(new Location(world, 941.5, 10, 871.5));
        itemLocations.add(new Location(world, 964.5, 9, 873.5));
        itemLocations.add(new Location(world, 974.5, 10, 907.5));
        itemLocations.add(new Location(world, 978.5, 9, 902.5));
        itemLocations.add(new Location(world, 947.5, 10, 914.5));
        ArrayList<Location> weaponLocations = new ArrayList<>();
        ArrayList<Location> ammoLocations = new ArrayList<>();
        ammoLocations.add(new Location(world, 953.5, 10, 873.5));

        scenes.add(new Scene(
                new Location(world, 952.5, 9, 872.5),
                new BoundingBox(930, 8, 905, 925, 13, 910),
                pathRegions,
                spawnLocations,
                bossLocations,
                itemLocations,
                2,
                weaponLocations,
                ammoLocations,
                new Location(world, 929, 10, 909),
                player -> {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/execute in minecraft:overworld run setblock 952 9 877 minecraft:birch_door[half=lower,facing=south,hinge=left,open=false] destroy");
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/execute in minecraft:overworld run setblock 952 10 877 minecraft:birch_door[half=upper,facing=south,hinge=left,open=false] destroy");
                        for (int i = 0; i < 4; i++) {
                            Director.spawnUtility(world, new Location(world, 957, 10, 878.3), new Medkit());
                        }
                        ArrayList<Melee> meleeWeapons = new ArrayList<>();
                        meleeWeapons.add(new FireAxe());
                        meleeWeapons.add(new Crowbar());
                        meleeWeapons.add(new Nightstick());
                        Director.spawnWeapon(world, new Location(world, 949.5, 10, 878.5), meleeWeapons.get(ThreadLocalRandom.current().nextInt(0, meleeWeapons.size())), true);

                        playDialogue(DepartureDialogue.OFFICE_OPENING);
                    },
                null,
                null,
                null
        ));



        // scene 1 - office
        pathRegions = new ArrayList<>();
        pathRegions.add(new BoundingBox(853, 76, 921, 845, 76, 940));
        pathRegions.add(new BoundingBox(854, 76, 938, 878, 76, 915));
        pathRegions.add(new BoundingBox(870, 76, 919, 863, 76, 904));
        pathRegions.add(new BoundingBox(866, 72, 897, 869, 72, 897));
        pathRegions.add(new BoundingBox(866, 56, 897, 869, 56, 897));
        pathRegions.add(new BoundingBox(865, 52, 904, 870, 52, 933));
        pathRegions.add(new BoundingBox(862, 52, 933, 852, 52, 914));
        pathRegions.add(new BoundingBox(845, 52, 921, 839, 52, 910));
        pathRegions.add(new BoundingBox(836, 43, 924, 828, 43, 904));
        pathRegions.add(new BoundingBox(838, 42, 904, 847, 42, 922));
        spawnLocations = new ArrayList<>();
        spawnLocations.add(new Location(world, 843, 77, 924));
        spawnLocations.add(new Location(world, 858.5, 77, 940.5));
        spawnLocations.add(new Location(world, 875, 77, 949));
        spawnLocations.add(new Location(world, 859, 77, 906));
        spawnLocations.add(new Location(world, 857.5, 77, 930.5));
        spawnLocations.add(new Location(world, 875, 53, 936));
        spawnLocations.add(new Location(world, 860.5, 53, 913.5));
        spawnLocations.add(new Location(world, 849.5, 53, 921.5));
        spawnLocations.add(new Location(world, 849.5, 43, 912.5));
        bossLocations = new ArrayList<>();
        bossLocations.add(new Location(world, 843.5, 44, 913.5));
        itemLocations = new ArrayList<>();
        itemLocations.add(new Location(world, 862.5, 54, 916.5));
        itemLocations.add(new Location(world, 839.5, 52, 928.5));
        itemLocations.add(new Location(world, 845.5, 54, 927.5));
        itemLocations.add(new Location(world, 834.5, 45, 920.5));
        itemLocations.add(new Location(world, 845, 44, 907.5));
        weaponLocations = new ArrayList<>();
        weaponLocations.add(new Location(world, 844.5, 43, 919.7));
        ammoLocations = new ArrayList<>();
        ArrayList<Listener> happenings = new ArrayList<>();
        happenings.add(new Listener() {
            @EventHandler
            public void officeJump(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(849, 51, 915, 847, 56, 921);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.OFFICE_JUMP);
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void officeVent(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(837, 46, 904, 840, 41, 907);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.OFFICE_VENTS);
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void officeSafeRoom(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(874, 45, 924, 876, 47, 922);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.OFFICE_SAFE_ROOM);
                HandlerList.unregisterAll(this);
            }
        });

        scenes.add(new Scene(
                new Location(world, 851, 77, 913),
                new BoundingBox(872, 42, 906, 878, 47, 913),
                pathRegions,
                spawnLocations,
                bossLocations,
                itemLocations,
                2,
                weaponLocations,
                ammoLocations,
                new Location(world, 876, 44, 907),
                player -> {
                    world.getBlockAt(new Location(world, 872, 43, 910)).setType(Material.BEEHIVE);
                    Director.spawnWeapon(world, new Location(world, 864.5, 54, 909.5), new FryingPan(), false);
                    playDialogue(DepartureDialogue.OFFICE_ELEVATOR);
                },
                player -> playDialogue(DepartureDialogue.OFFICE_RADIO),
                new Listener() {
                    @EventHandler
                    public void onEnterVent(PlayerInteractEvent e) {
                        Block block = e.getClickedBlock();
                        if (!Expunge.playing.getKeys().contains(e.getPlayer())) {
                            return;
                        }
                        if (block == null || !(e.getAction().equals(Action.PHYSICAL) && block.getLocation().toVector().equals(new Vector(846, 45, 925)))) {
                            return;
                        }

                        Scene.playCrescendoEventEffect();
                        World world = e.getPlayer().getWorld();
                        new BukkitRunnable() {
                            int i = 0;

                            @Override
                            public void run() {
                                Expunge.runningDirector.spawnMob(new Horde(world, new Location(world, 845.5, 48, 928.5)));
                                Expunge.runningDirector.spawnMob(new Horde(world, new Location(world, 853.5, 47, 939.5)));
                                Expunge.runningDirector.spawnMob(new Horde(world, new Location(world, 866.5, 48, 939.5)));
                                Expunge.runningDirector.spawnMob(new Horde(world, new Location(world, 872.5, 48, 931.5)));
                                Expunge.runningDirector.spawnMob(new Horde(world, new Location(world, 876.5, 48, 925.5)));
                                i++;
                                if (i >= 5) {
                                    this.cancel();
                                }
                                if (Expunge.currentSceneIndex != 1) {
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(Expunge.instance, 1, 20 * 4);

                        // crescendo events are only meant to happen once
                        // hence it unregisters the event when it executes
                        HandlerList.unregisterAll(this);
                    }
                },
                happenings
        ));



        // scene 2 - streets
        pathRegions = new ArrayList<>();
        pathRegions.add(new BoundingBox(870, 42, 908, 864, 42, 921));
        pathRegions.add(new BoundingBox(861, 42, 916, 854, 42, 922));
        pathRegions.add(new BoundingBox(854, 42, 913, 858, 42, 890));
        pathRegions.add(new BoundingBox(863, 41, 889, 1112, 41, 863));
        pathRegions.add(new BoundingBox(1086, 41, 888, 1112, 41, 907));
        pathRegions.add(new BoundingBox(1127, 50, 899, 1129, 50, 920));
        pathRegions.add(new BoundingBox(1129, 56, 920, 1127, 56, 899));
        pathRegions.add(new BoundingBox(1141, 42, 897, 1138, 42, 912));
        spawnLocations = new ArrayList<>();
        spawnLocations.add(new Location(world, 820.5, 43, 896.5));
        spawnLocations.add(new Location(world, 871.5, 43, 918.5));
        spawnLocations.add(new Location(world, 857.5, 43, 928.5));
        spawnLocations.add(new Location(world, 894, 43, 855.5));
        spawnLocations.add(new Location(world, 927, 43, 896.5));
        spawnLocations.add(new Location(world, 949.5, 43, 884.5));
        spawnLocations.add(new Location(world, 956.5, 43, 854.5));
        spawnLocations.add(new Location(world, 981.5, 43, 900.5));
        spawnLocations.add(new Location(world, 984.5, 43, 855.5));
        spawnLocations.add(new Location(world, 1025.5, 43, 896.5));
        spawnLocations.add(new Location(world, 1038, 43, 856.5));
        spawnLocations.add(new Location(world, 1084.5, 43, 852));
        spawnLocations.add(new Location(world, 1145, 43, 895.5));
        spawnLocations.add(new Location(world, 1129.5, 43, 922.5));
        bossLocations = new ArrayList<>();
        bossLocations.add(new Location(world, 1128.5, 56.07, 905.5));
        itemLocations = new ArrayList<>();
        itemLocations.add(new Location(world, 854.5, 44, 910.5));
        itemLocations.add(new Location(world, 1124.5, 51, 901.5));
        itemLocations.add(new Location(world, 1135.5, 51, 911.5));
        itemLocations.add(new Location(world, 1120.5, 50, 917.5));
        itemLocations.add(new Location(world, 1136.5, 51, 920));
        itemLocations.add(new Location(world, 1136, 56.6, 917));
        itemLocations.add(new Location(world, 1118, 56, 912));
        itemLocations.add(new Location(world, 1134, 57, 911.5));
        itemLocations.add(new Location(world, 1124.5, 56, 905.5));
        weaponLocations = new ArrayList<>();
        weaponLocations.add(new Location(world, 1129.5, 58, 896.5));
        ammoLocations = new ArrayList<>();
        happenings = new ArrayList<>();
        happenings.add(new Listener() {
            @EventHandler
            public void streetsOpeningTrigger(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(861, 42, 899, 851, 48, 903);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.STREETS_OPENING);
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void streetsApartmentsTrigger(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1084, 40, 899, 1079, 57, 851);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.STREETS_APARTMENTS);
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void streetsShedTrigger(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1138, 56, 896, 1140, 60, 906);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.STREETS_SHED);
                HandlerList.unregisterAll(this);
            }
        });

        scenes.add(new Scene(
                new Location(world, 875.5, 43, 911.5),
                new BoundingBox(1136, 42, 918, 1141, 47, 913),
                pathRegions,
                spawnLocations,
                bossLocations,
                itemLocations,
                3,
                weaponLocations,
                ammoLocations,
                new Location(world, 1137, 44, 916),
                player -> {
                    world.getBlockAt(new Location(world, 872, 43, 910)).setType(Material.AIR);
                    world.getBlockAt(new Location(world, 1139, 43, 914)).setType(Material.AIR);
                    world.getBlockAt(new Location(world, 1139, 43, 918)).setType(Material.BEEHIVE);
                    for (int i = 0; i < 4; i++) {
                        Director.spawnUtility(world, new Location(world, 877.5, 44, 910.5), new Medkit());
                    }
                    Director.spawnWeapon(world, new Location(world, 873.5, 44, 907.5), Director.getRandomGun(Tier.TIER2), true);
                    Director.spawnWeapon(world, new Location(world, 875.5, 44, 907.5), Director.getRandomGun(Tier.TIER2), true);

                    Director.spawnUtility(world, new Location(world, 1135, 44, 919), new Medkit());
                    Director.spawnWeapon(world, new Location(world, 857.5, 44, 910.5), Director.getRandomGun(Tier.TIER1), false);
                    Director.spawnWeapon(world, new Location(world, 857.5, 44, 907.5), Director.getRandomGun(Tier.TIER1), false);
                    Director.spawnWeapon(world, new Location(world, 1135, 44, 910), Director.getRandomGun(Tier.TIER1), false);
                    Director.spawnWeapon(world, new Location(world, 1135, 44, 907), Director.getRandomMelee(Tier.TIER1), false);

                    Expunge.runningDirector.spawnAtRandomLocations(world, new BoundingBox(873, 41, 889, 932, 41, 863), 30, false);
                },
                null,
                new Listener() {
                    @EventHandler
                    public void crescendoEventApartments(PlayerInteractEvent e) {
                        Block block = e.getClickedBlock();
                        if (!Expunge.playing.getKeys().contains(e.getPlayer())) {
                            return;
                        }
                        if (block == null || !(e.getAction().equals(Action.PHYSICAL) && (block.getLocation().toVector().equals(new Vector(1120, 43, 898)) || block.getLocation().toVector().equals(new Vector(1120, 43, 899))))) {
                            return;
                        }

                        Scene.playCrescendoEventEffect();
                        World world = e.getPlayer().getWorld();
                        new BukkitRunnable() {
                            int i = 0;
                            @Override
                            public void run() {
                                Expunge.runningDirector.spawnMob(new Horde(world, new Location(world, 1121.5, 43, 922.5)));
                                Expunge.runningDirector.spawnMob(new Horde(world, new Location(world, 1118.5, 43, 843)));
                                Expunge.runningDirector.spawnMob(new Horde(world, new Location(world, 1076.5, 43, 894.5)));
                                Expunge.runningDirector.spawnMob(new Horde(world, new Location(world, 1134, 43, 898.5)));
                                Expunge.runningDirector.spawnMob(new Horde(world, new Location(world, 1125.5, 50, 899.5)));
                                Expunge.runningDirector.spawnMob(new Horde(world, new Location(world, 1131.5, 50, 909.5)));
                                Expunge.runningDirector.spawnMob(new Horde(world, new Location(world, 1125.5, 50, 910.5)));
                                Expunge.runningDirector.spawnMob(new Horde(world, new Location(world, 1131.5, 50, 920.5)));
                                Expunge.runningDirector.spawnMob(new Horde(world, new Location(world, 1131.5, 50, 920.5)));
                                Expunge.runningDirector.spawnMob(new Horde(world, new Location(world, 1131.5, 56, 920.5)));
                                Expunge.runningDirector.spawnMob(new Horde(world, new Location(world, 1125.5, 56, 910.5)));
                                Expunge.runningDirector.spawnMob(new Horde(world, new Location(world, 1131.5, 56, 909.5)));
                                Expunge.runningDirector.spawnMob(new Horde(world, new Location(world, 1125.5, 56, 899.5)));
                                i++;
                                if (i >= 3) {
                                    this.cancel();
                                }
                                if (Expunge.currentSceneIndex != 2) {
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(Expunge.instance, 1, 20 * 4);
                        HandlerList.unregisterAll(this);
                    }
                },
                happenings
        ));



        // scene 3 - alleys
        pathRegions = new ArrayList<>();
        pathRegions.add(new BoundingBox(1143, 42, 916, 1156, 42, 923));
        pathRegions.add(new BoundingBox(1151, 42, 922, 1155, 42, 938));
        pathRegions.add(new BoundingBox(1152, 42, 933, 1168, 42, 948));
        pathRegions.add(new BoundingBox(1151, 42, 934, 1131, 42, 961));
        pathRegions.add(new BoundingBox(1147, 42, 951, 1175, 42, 973));
        pathRegions.add(new BoundingBox(1157, 42, 970, 1153, 42, 988));
        pathRegions.add(new BoundingBox(1153, 42, 988, 1130, 42, 984));
        spawnLocations = new ArrayList<>();
        spawnLocations.add(new Location(world, 1155.5, 43, 914.5));
        spawnLocations.add(new Location(world, 1150.5, 43, 929));
        spawnLocations.add(new Location(world, 1167.5, 43, 937));
        spawnLocations.add(new Location(world, 1176.5, 43, 971));
        spawnLocations.add(new Location(world, 1129.5, 43, 950.5));
        spawnLocations.add(new Location(world, 1155.5, 44, 965.5));
        spawnLocations.add(new Location(world, 1166.5, 43, 961.5));
        spawnLocations.add(new Location(world, 1150.5, 43, 958.5));
        spawnLocations.add(new Location(world, 1147.5, 43, 951.5));
        spawnLocations.add(new Location(world, 1139.5, 44, 974.5));
        spawnLocations.add(new Location(world, 1149.5, 43, 990.5));
        spawnLocations.add(new Location(world, 1130.5, 43, 972.5));
        spawnLocations.add(new Location(world, 1126.5, 43, 978.5));
        bossLocations = new ArrayList<>();
        bossLocations.add(new Location(world, 1155, 42, 978));
        itemLocations = new ArrayList<>();
        itemLocations.add(new Location(world, 1148, 44, 980.5));
        itemLocations.add(new Location(world, 1166, 44, 965.5));
        itemLocations.add(new Location(world, 1180.5, 44, 965.5));
        itemLocations.add(new Location(world, 1175.5, 44, 949));
        itemLocations.add(new Location(world, 1167, 44, 934.5));
        itemLocations.add(new Location(world, 1158, 44, 948.5));
        itemLocations.add(new Location(world, 1154.5, 44, 944.5));
        itemLocations.add(new Location(world, 1139.5, 44, 955));
        weaponLocations = new ArrayList<>();
        weaponLocations.add(new Location(world, 1163.5, 45, 922.5));
        weaponLocations.add(new Location(world, 1140, 44, 950.7));
        weaponLocations.add(new Location(world, 1133.5, 43, 943.5));
        weaponLocations.add(new Location(world, 1137.5, 44, 993.5));
        ammoLocations = new ArrayList<>();
        happenings = new ArrayList<>();
        happenings.add(new Listener() {
            @EventHandler
            public void alleysOpening(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1157, 42, 933, 1148, 51, 929);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.ALLEYS_OPENING);
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void alleysPurpleCar(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1147, 43, 935, 1142, 47, 942);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                DepartureDialogue.PURPLE_CAR.getSolo().displayDialogue(List.of(e.getPlayer()));
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void alleysSafeHouse(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1147, 42, 983, 1141, 56, 990);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.ALLEYS_SAFE_HOUSE);
                HandlerList.unregisterAll(this);
            }
        });
        scenes.add(new Scene(
                new Location(world, 1139, 43, 916),
                new BoundingBox(1128, 43, 979, 1118, 53, 1002),
                pathRegions,
                spawnLocations,
                bossLocations,
                itemLocations,
                2,
                weaponLocations,
                ammoLocations,
                new Location(world, 1124, 44, 987),
                player -> {
                    world.getBlockAt(new Location(world, 1119, 43, 988)).setType(Material.BEEHIVE);
                    world.getBlockAt(new Location(world, 1139, 43, 914)).setType(Material.BEEHIVE);
                    world.getBlockAt(new Location(world, 1139, 43, 918)).setType(Material.AIR);
                    world.getBlockAt(new Location(world, 1127, 43, 986)).setType(Material.AIR);

                    Director.spawnUtility(world, new Location(world, 1140.7, 44, 917), new Pills());
                    Director.spawnUtility(world, new Location(world, 1140.7, 44, 915), new Adrenaline());
                    Director.spawnWeapon(world, new Location(world, 1138, 44, 914.3), Director.getRandomGun(Tier.TIER1), true);
                    Director.spawnWeapon(world, new Location(world, 1138, 44, 917.7), Director.getRandomMelee(Tier.TIER1), false);

                    Expunge.runningDirector.spawnAtRandomLocations(world, new BoundingBox(1174, 42, 945, 1124, 42, 977), 30, false);
                    Expunge.runningDirector.spawnAtRandomLocations(world, new BoundingBox(1157, 42, 973, 1129, 42, 989), 20, false);
                },
                null,
                null,
                happenings
        ));



        // scene 4 - subway
        pathRegions = new ArrayList<>();
        pathRegions.add(new BoundingBox(1112, 41, 969, 1086, 41, 1019));
        pathRegions.add(new BoundingBox(1068, 32, 1001, 1064, 32, 1008));
        pathRegions.add(new BoundingBox(1064, 24, 1016, 1069, 42, 1020));
        pathRegions.add(new BoundingBox(1078, 15, 1016, 1089, 15, 1030));
        pathRegions.add(new BoundingBox(1089, 15, 1030, 1103, 15, 1016));
        pathRegions.add(new BoundingBox(1090, 26, 1042, 1104, 26, 1049));
        pathRegions.add(new BoundingBox(1087, 24, 1050, 1133, 24, 1056));
        pathRegions.add(new BoundingBox(1133, 42, 1046, 1145, 24, 1070));
        pathRegions.add(new BoundingBox(1145, 24, 1070, 1163, 24, 1046));
        pathRegions.add(new BoundingBox(1163, 24, 1046, 1177, 24, 1072));
        pathRegions.add(new BoundingBox(1179, 24, 1073, 1198, 24, 1054));
        pathRegions.add(new BoundingBox(1182, 24, 1078, 1206, 24, 1101));
        pathRegions.add(new BoundingBox(1202, 24, 1103, 1186, 24, 1127));
        pathRegions.add(new BoundingBox(1183, 24, 1131, 1195, 24, 1143));
        pathRegions.add(new BoundingBox(1181, 24, 1133, 1169, 24, 1151));
        pathRegions.add(new BoundingBox(1165, 24, 1155, 1147, 24, 1161));
        pathRegions.add(new BoundingBox(1144, 24, 1161, 1168, 24, 1179));
        pathRegions.add(new BoundingBox(1154, 24, 1179, 1142, 24, 1196));
        pathRegions.add(new BoundingBox(1141, 24, 1197, 1129, 24, 1208));
        spawnLocations = new ArrayList<>();
        spawnLocations.add(new Location(world, 1080.5, 43, 977));
        spawnLocations.add(new Location(world, 1082.5, 43, 1017));
        spawnLocations.add(new Location(world, 1093, 21, 1011.5));
        spawnLocations.add(new Location(world, 1085.5, 25, 1053.5));
        spawnLocations.add(new Location(world, 1107.5, 25, 1060.5));
        spawnLocations.add(new Location(world, 1100.5, 21, 1077));
        spawnLocations.add(new Location(world, 1107.5, 25, 1066.5));
        spawnLocations.add(new Location(world, 1131.5, 25, 1060.5));
        spawnLocations.add(new Location(world, 1133.5, 25, 1047.5));
        spawnLocations.add(new Location(world, 1133.5, 25, 1069.5));
        spawnLocations.add(new Location(world, 1161.5, 26, 1076.5));
        spawnLocations.add(new Location(world, 1179.5, 26, 1087.5));
        spawnLocations.add(new Location(world, 1137.5, 26, 1063.5));
        spawnLocations.add(new Location(world, 1183.5, 26, 1052.5));
        spawnLocations.add(new Location(world, 1177.5, 25, 1072.5));
        spawnLocations.add(new Location(world, 1205.5, 25, 1101.5));
        spawnLocations.add(new Location(world, 1193.5, 26, 1089.5));
        spawnLocations.add(new Location(world, 1191.5, 25, 1101.5));
        spawnLocations.add(new Location(world, 1183.5, 26, 1141.5));
        spawnLocations.add(new Location(world, 1156.5, 25, 1162.5));
        spawnLocations.add(new Location(world, 1145.5, 25, 1161.5));
        spawnLocations.add(new Location(world, 1167.5, 25, 1179.5));
        spawnLocations.add(new Location(world, 1151.5, 26, 1164.5));
        spawnLocations.add(new Location(world, 1151.5, 26, 1176.5));
        spawnLocations.add(new Location(world, 1127.5, 25, 1208.5));
        spawnLocations.add(new Location(world, 1127.5, 25, 1202.5));
        bossLocations = new ArrayList<>();
        bossLocations.add(new Location(world, 1189.5, 25, 1063.5));
        itemLocations = new ArrayList<>();
        itemLocations.add(new Location(world, 1191.5, 27, 1051.2));
        itemLocations.add(new Location(world, 1090.3, 28, 1074));
        itemLocations.add(new Location(world, 1165.5, 25, 1195.5));
        weaponLocations = new ArrayList<>();
        weaponLocations.add(new Location(world, 1155.5, 25, 1172));
        weaponLocations.add(new Location(world, 1195, 27, 1094.5));
        weaponLocations.add(new Location(world, 1187, 27, 1053.7));
        weaponLocations.add(new Location(world, 1104.7, 28, 1075));
        ammoLocations = new ArrayList<>();
        ammoLocations.add(new Location(world, 1122.5, 50, 998.5));
        happenings = new ArrayList<>();
        happenings.add(new Listener() {
            @EventHandler
            public void subwayOpening(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1118, 42, 990, 1113, 46, 986);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.SUBWAY_OPENING);
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void subwayPurpleCar(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1074, 19, 1025, 1069, 29, 1030);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                DepartureDialogue.PURPLE_CAR.getSolo().displayDialogue(List.of(e.getPlayer()));
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void subwaySpawnZombies(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1069, 32, 1008, 1064, 38, 1001);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                Expunge.runningDirector.spawnAtRandomLocations(world, new BoundingBox(1103, 15, 1030, 1078, 15, 1016), 30, false);
                Expunge.runningDirector.spawnAtRandomLocations(world, new BoundingBox(1104, 26, 1042, 1090, 26, 1048), 15, false);
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void subwayMrCookie(PlayerInteractEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock() != null && e.getClickedBlock().getType().toString().contains("_BUTTON")) {
                    if (!e.getClickedBlock().getLocation().toVector().equals(new Vector(1085, 16, 1017)))
                        return;
                    Director.spawnWeapon(world, new Location(world, 1084.5, 16.3, 1017.5), new MrCookie(), false);
                    playDialogue(DepartureDialogue.SUBWAY_MR_COOKIE);
                    HandlerList.unregisterAll(this);
                }
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void subwayMap(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1091, 14, 1031, 1104, 23, 1015);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.SUBWAY_MAP);
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void subwaySafeZone(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1129, 24, 1201, 1151, 31, 1197);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.SUBWAY_SAFE_ZONE);
                HandlerList.unregisterAll(this);
            }
        });
        scenes.add(new Scene(
                new Location(world, 1124.5, 43, 986.5),
                new BoundingBox(1128, 25, 1203, 1116, 30, 1207),
                pathRegions,
                spawnLocations,
                bossLocations,
                itemLocations,
                2,
                weaponLocations,
                ammoLocations,
                new Location(world, 1122, 27, 1204),
                player -> {
                    world.getBlockAt(new Location(world, 1127, 43, 986)).setType(Material.BEEHIVE);
                    world.getBlockAt(new Location(world, 1119, 43, 988)).setType(Material.AIR);
                    world.getBlockAt(new Location(world, 1117, 26, 1205)).setType(Material.BEEHIVE);
                    world.getBlockAt(new Location(world, 1127, 26, 1205)).setType(Material.AIR);
                    for (int i = 0; i < 4; i++) {
                        Director.spawnUtility(world, new Location(world, 1123.5, 44, 996.5), new Medkit());
                    }
                    Director.spawnWeapon(world, new Location(world, 1119.3, 44, 983), Director.getRandomGun(Tier.TIER2), true);
                    Director.spawnWeapon(world, new Location(world, 1122.7, 44, 981), Director.getRandomGun(Tier.TIER2), true);
                    Director.spawnWeapon(world, new Location(world, 1119.3, 44.5, 994.5), Director.getRandomMelee(Tier.TIER1), false);
                    Director.spawnUtility(world, new Location(world, 1121, 50.2, 984), new Pills());
                    Director.spawnUtility(world, new Location(world, 1126, 50.2, 983), new Adrenaline());

                    Expunge.runningDirector.spawnAtRandomLocations(world, new BoundingBox(1112, 41, 994, 1086, 41, 1011), 45, false);
                },
                null,
                null,
                happenings
        ));



        // scene 5 - highway
        pathRegions = new ArrayList<>();
        pathRegions.add(new BoundingBox(1115, 24, 1202, 1089, 24, 1212));
        pathRegions.add(new BoundingBox(1099, 35, 1210, 1075, 35, 1221));
        pathRegions.add(new BoundingBox(1069, 35, 1227, 1045, 35, 1241));
        pathRegions.add(new BoundingBox(1045, 35, 1227, 1013, 35, 1255));
        pathRegions.add(new BoundingBox(1012, 35, 1255, 997, 35, 1286));
        pathRegions.add(new BoundingBox(997, 35, 1288, 1011, 35, 1328));
        pathRegions.add(new BoundingBox(1009, 35, 1330, 1028, 35, 1350));
        spawnLocations = new ArrayList<>();
        spawnLocations.add(new Location(world, 988.5, 39, 1218.5));
        spawnLocations.add(new Location(world, 1131.5, 36, 1237.5));
        spawnLocations.add(new Location(world, 1130.5, 37, 1205.5));
        spawnLocations.add(new Location(world, 1087.5, 36, 1233.5));
        spawnLocations.add(new Location(world, 995.5, 37, 1247.5));
        spawnLocations.add(new Location(world, 1016.5, 37, 1261.5));
        spawnLocations.add(new Location(world, 990.5, 37, 1266.5));
        spawnLocations.add(new Location(world, 1020.5, 37, 1284.5));
        spawnLocations.add(new Location(world, 1020.5, 40, 1304.5));
        spawnLocations.add(new Location(world, 987.5, 37, 1354.5));
        spawnLocations.add(new Location(world, 989.5, 41, 1334.5));
        spawnLocations.add(new Location(world, 1037.5, 37, 1340.5));
        spawnLocations.add(new Location(world, 1009.5, 36, 1363.5));
        spawnLocations.add(new Location(world, 990.5, 37, 1264.5));
        spawnLocations.add(new Location(world, 1028.5, 37, 1201.5));
        bossLocations = new ArrayList<>();
        bossLocations.add(new Location(world, 1004.5, 36, 1316.5));
        itemLocations = new ArrayList<>();
        itemLocations.add(new Location(world, 1087.3, 27, 1206));
        itemLocations.add(new Location(world, 1107, 25, 1199));
        itemLocations.add(new Location(world, 1038.5, 37, 1217.5));
        itemLocations.add(new Location(world, 1036.5, 37, 1216.5));
        itemLocations.add(new Location(world, 1125.5, 26, 1203.5));
        weaponLocations = new ArrayList<>();
        weaponLocations.add(new Location(world, 1036.5, 37, 1217.5));
        weaponLocations.add(new Location(world, 1120, 26, 1206.5));
        weaponLocations.add(new Location(world, 1122.5, 27, 1207));
        ammoLocations = new ArrayList<>();
        ammoLocations.add(new Location(world, 1120.5, 26, 1204.5));
        happenings = new ArrayList<>();
        happenings.add(new Listener() {
            @EventHandler
            public void highwayManhole(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1114, 24, 1209, 1108, 33, 1201);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.HIGHWAY_MANHOLE);
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void highwayOpening(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1100, 36, 1209, 1096, 39, 1213);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.HIGHWAY_OPENING);
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void highwaySafeHouse(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(990, 34, 1291, 1021, 56, 1297);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.HIGHWAY_SAFE_HOUSE);
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void highwayPurpleCar(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1019, 39, 1276, 1021, 43, 1273);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.HIGHWAY_PURPLE_CAR);
                HandlerList.unregisterAll(this);
            }
        });
        scenes.add(new Scene(
                new Location(world, 1122.5, 26, 1205.5),
                new BoundingBox(1031, 34, 1352, 1039, 40, 1360),
                pathRegions,
                spawnLocations,
                bossLocations,
                itemLocations,
                2,
                weaponLocations,
                ammoLocations,
                new Location(world, 1033, 37, 1354),
                player -> {
                    world.getBlockAt(new Location(world, 1127, 26, 1205)).setType(Material.BEEHIVE);
                    world.getBlockAt(new Location(world, 1033, 36, 1360)).setType(Material.BEEHIVE);
                    world.getBlockAt(new Location(world, 1032, 36, 1360)).setType(Material.BEEHIVE);
                    world.getBlockAt(new Location(world, 1117, 26, 1205)).setType(Material.AIR);
                    world.getBlockAt(new Location(world, 1031, 36, 1353)).setType(Material.AIR);
                },
                null,
                null,
                happenings
        ));
        return scenes;
    }

    public TheDeparture() {
        super("The Departure", scenes(), Bukkit.getWorld("world"));
    }
}
