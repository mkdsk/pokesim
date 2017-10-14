Source: [Bulbapedia](https://bulbapedia.bulbagarden.net/wiki/Damage)

Except for moves that deal direct damage, the damage dealt when a Pokémon uses a damaging move depends on its level, its effective Attack or Special Attack stat, the opponent's effective Defense or Special Defense stat, and the move's effective power. In addition, various factors of damage modification may also affect the damage dealt.

More precisely, damage is calculated as

![Image](https://cdn.bulbagarden.net/upload/4/47/DamageCalc.png)

where

    Level is the level of the attacking Pokémon (or twice the level for a critical hit in Generation I).
    A is the effective Attack stat of the attacking Pokémon if the used move is a physical move, or the effective Special Attack stat of the attacking Pokémon if the used move is a special move (ignoring allGen. II/negativeGen. III+ stat stages for a critical hit).
    D is the effective Defense stat of the target if the used move is a physical move or a special move that uses the target's Defense stat, or the effective Special Defense of the target if the used move is an other special move (ignoring allGen. II/positiveGen. III+ stat stages for a critical hit).
    Power is the effective power of the used move.

and Modifier is

![Image](https://cdn.bulbagarden.net/upload/3/32/ModifierCalc.png)

where

    Targets is 0.75 if the move has more than one target, and 1 otherwise.
    Weather is 1.5 if a Water-type move is being used during rain or a Fire-type move during harsh sunlight, and 0.5 if a Water-type move is used during harsh sunlight or a Fire-type move during rain, and 1 otherwise.
    Badge is applied in Generation II only. It is 1.25 if the attacking Pokémon is controlled by the player and if the player has obtained the Badge corresponding to the used move's type, and 1 otherwise.
    Critical is applied starting in Generation II. It is 2 for a critical hit in Generations II-V, 1.5 for a critical hit from Generation VI onward, and 1 otherwise.
    random is a random factor between 0.85 and 1.00 (inclusive):
        From Generation III onward, it is a random integer percentage between 0.85 and 1.00 (inclusive)
        In Generations I and II, it is realized as a multiplication by a random uniformly distributed integer between 217 and 255 (inclusive), followed by an integer division by 255
    STAB is the same-type attack bonus. This is equal to 1.5 if the move's type matches any of the user's types, 2 if the user of the move additionally has Adaptability, and 1 if otherwise.
    Type is the type effectiveness. This can be 0 (ineffective); 0.25, 0.5 (not very effective); 1 (normally effective); 2 or 4 (super effective) depending on both the move's and target's types.
    Burn is 0.5 (from Generation III onward) if the attacker is burned, its Ability is not Guts, and the used move is a physical move (other than Facade from Generation VI onward), and 1 otherwise.
    other is 1 in most cases, and a different multiplier when specific interactions of moves, Abilities or items take effect:
