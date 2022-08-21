event "Join" {
    var %default_played: save;
    if(%default_played == 1) {
        "player_action":"SendMessage"<>("Welcome back!");
    } else {
        "player_action":"SendMessage"<>("Welcome new player!");
        %default_played = 1;
    }
}