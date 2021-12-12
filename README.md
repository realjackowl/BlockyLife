Current features:​

Custom pulse modifiers
Hex Color Support
Per world
PlaceholderAPI hook
Pulse simulation
Screenshots:
[​IMG]

Pulse modifiers:
Left interaction: +1.5
Jump: +1.0
Jump while sprinting: +2.0
Move: -0.025
Move < 80: -0.0005
Sprinting: +0.1
Still > 80: -1.25
Still < 80: -0.15
Still < 60: -0.05

Ranges:
Pulse > 220 = death
Pulse > 190 = title
Pulse > 100 = breathing
Pulse < 30 = title
Pulse < 20 = death

These modifiers and ranges are not completely based on real data but I am in contact with provider that could take the data more closer to real ones and also in the future you would be able to completely configure them even with actions that should occur on that range

Configuration:
Code (YAML):
Version: 2 #Config version
AFKCheck: 700 #Frequency of AFKChecker in ticks
Modules:
  Pulse:
    Enabled: true #You can't disable it now because it would be pointless
    Settings:
      ValueDisplay: ActionBar #You can also disable it with "off" or "disabled", ...
      MessageDisplay: Title
      Modifiers:
       LEFT_CLICK: 1.5 # +
       JUMP: 1.0 # +
       JUMP_SPRINT: 2.0 # +
       MOVE: 0.025 # -
       MOVE_LOWER_80: 0.0005 # -
       SPRINT: 0.1 # +
       STILL_HIGHER_80: 1.25 # -
       STILL_LOWER_80: 0.15 # -
       STILL_LOWER_60: 0.05 # -
    Translation:
      Titles:
        CalmTitle: "&cSlow Down"
        CalmSubtitle: "#FF5554NOW"
        MoveTitle: "&cMove"
        MoveSubtitle: "&cNOW"
        
Placeholders:
blockylife_pulse - returns pulse value for current player

Permissions:
blockylife.bypass - plugin ignores player with this permission

Please don't post bugs to review section, post them to issues on Github instead
