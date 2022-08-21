# DFMatic
A transpiled programming language for the [Diamondfire](https://mcdiamondfire.com) Minecraft server.

## Why?
<b>Problem</b>: DF code is inherently hard to read, and hard to read code eventually turns into unmaintainable code. I often find myself starting at DF code I wrote a week ago and not being able to understand it without going block by block, which you can rule out if made something more than a few code spaces. While this problem exists within conventional programming, the effect is nowhere near as persistent because of how readable standard code is.

DFMatic proposes a solution to this with a language specifically designed to compile down to code blocks while having nice, readable, text source code.

## Example Source Code
```
event "Join" {
    sendMessage("Hello player!");
}

func sendMessage(message) {
    "player_action":"SendMessage"<>(message);
}
```

## Features
1. Function arguments and return values
   ```
   var result: save = whatThisIs();
   
   func whatThisIs() {
      return "Pretty cool!";
   }
   ```
2. List support and indexing: `first_item = list[1];`
   ```
   primes = [2, 3, 5, 7, 11, 13, 17, 19, 23];
   ```
3. Dictionary support and properties:
   ```
   months = {"january": 1, "febuary": 2, "march": 3, "april": 4 ... };
   month_num = months.january; // 1
   ```
   
## How To Use
1. Download the JAR from [the releases site](https://github.com/fallow64/dfmatic/releases)
2. Run the JAR with the file path of the source code. `java -jar dfmatic.jar mySourceCode.dfm`

## Credit
* WLGRXD for the idea behind code block serialization.
* lukecashwell for the creation of Spark, a huge inspiration for this project.
* Robert Nystrom, author of [Crafting Interperters](https://craftinginterpreters.com/), a book about creating your own programming language. 