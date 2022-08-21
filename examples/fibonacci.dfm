event "Join" {
    fibonacci(20);
}

func fibonacci(limit) {
    loop(local n to limit) {
        "player_action":"SendMessage"<>(getFibonacci(n));
    }
}

func getFibonacci(n) { // logic stolen from https://www.geeksforgeeks.org/program-for-nth-fibonacci-number/
    a = 0;
    b = 1;
    if(n < 2) {
        return n;
    } else {
        loop(local i from 2 to n) {
            c = a + b;
            a = b;
            b = c;
        }
        return b;
    }
}