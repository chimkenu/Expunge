package me.chimkenu.expunge.game.maps.thedeparture;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.game.Dialogue;

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
    HIGHWAY_CAR_BOOM {
        @Override
        public Dialogue getA() {
            return new Dialogue(null, "A » That just alerted the horde, run!");
        }

        @Override
        public Dialogue getB() {
            return new Dialogue(null, "A » I think that just caught the horde’s attention!");
        }

        @Override
        public Dialogue getSolo() {
            return new Dialogue(null, "A » That explosion’s gonna attract a horde, I need to run!");
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
                    "A » Not again, the path is blocked!",
                            "B » We can take safety in the gas station!"
            );
        }

        @Override
        public Dialogue getSolo() {
            return new Dialogue(null, "A » It’s blocked, I need to get around through the gas station.");
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
    },
    STADIUM_OPENING {
        @Override
        public Dialogue getA() {
            return new Dialogue(null,
                    "A » We’re close to the stadium. We just need to take a left from this road.",
                    "B » All of this walking better be worth it."
            );
        }

        @Override
        public Dialogue getB() {
            return new Dialogue(null,
                    "A » We just got to take a left down this road, and we’ll be at the evacuation site.",
                    "B » I never should have skipped leg day…"
            );
        }

        @Override
        public Dialogue getSolo() {
            return new Dialogue(null, "A » If I take one more left down the highway, I’ll finally be at the stadium.");
        }
    },
    STADIUM_PARKING_LOT {
        @Override
        public Dialogue getA() {
            return new Dialogue(null,
                    "A » I can see the stadium!",
                    "B » Get inside, quick!"
            );
        }

        @Override
        public Dialogue getB() {
            return new Dialogue(null,
                    "A » There’s the stadium!",
                    "B » Quickly, get inside!"
            );
        }

        @Override
        public Dialogue getSolo() {
            return new Dialogue(null, "A » There’s the stadium, I need to get in!");
        }
    },
    STADIUM_ENTER {
        @Override
        public Dialogue getA() {
            return new Dialogue(null,
                    "A » This place is a mess…",
                    "B » Where’s the helicopter?",
                    "A » Looks like we’ve been left behind.",
                    "B » Wait, I have an idea.",
                    "B » If we head to the control room and turn on the stadium screens, it could signal the military to rescue us.",
                    "A » It’s risky, but it might work."
            );
        }

        @Override
        public Dialogue getB() {
            return new Dialogue(null,
                    "A » What happened to everyone…?",
                    "B » There’s no helicopter here.",
                    "A » They abandoned us…!",
                    "B » Calm down, I have an idea.",
                    "B » If we head to the control room and turn on the stadium screens, it could signal the military to rescue us.",
                    "A » Could be dangerous, but it’s worth a try."
            );
        }

        @Override
        public Dialogue getSolo() {
            return new Dialogue(null,
                    "A » What happened to this place…? There’s no helicopter in sight.\n",
                    "A » Hmm... If I head to the control room and turn on the stadium screens, it could signal the military to rescue me."
            );
        }
    },
    STADIUM_ENDING {
        @Override
        public Dialogue getA() {
            return new Dialogue(null,
                    "A » Look, the helicopter’s here!",
                    "B » Run for it, go!"
            );
        }

        @Override
        public Dialogue getB() {
            return new Dialogue(null,
                    "A » Hey, the helicopter’s arriving!",
                    "B » Come on, let’s move!"
            );
        }

        @Override
        public Dialogue getSolo() {
            return new Dialogue(null, "A » There’s the helicopter, I need to run to it!");
        }
    };

    public abstract Dialogue getA();
    public abstract Dialogue getB();
    public abstract Dialogue getSolo();

    public static void playDialogue(DepartureDialogue dialogue) {
        if (Expunge.playing.getKeys().size() > 1) {
            if (Math.random() < 0.5)
                dialogue.getA().displayDialogue(Expunge.playing.getKeys());
            else
                dialogue.getB().displayDialogue(Expunge.playing.getKeys());
        }
        else
            dialogue.getSolo().displayDialogue(Expunge.playing.getKeys());
    }
}
