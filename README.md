# DFMatic
A transpiled programming language for the [Diamondfire](https://mcdiamondfire.com) Minecraft server.

## <span style="color: red;">WARNING</span>
This project will most likely not be expanded upon further.

## Why?
<b>Problem</b>: DF code is inherently hard to read, and hard to read code eventually turns into unmaintainable code. I
often find myself starting at DF code I wrote a week ago and not being able to understand it without going block by
block, which you can rule out if made something more than a few code spaces. While this problem exists within
conventional programming, the effect is nowhere near as persistent because of how readable standard code is.

DFMatic proposes a solution to this with a language specifically designed to compile down to code
blocks while having nice, readable, text source code.

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
   func whatThisIs() { return "Pretty cool!"; }
   ```
2. List support and indexing:
   ```
   primes = [2, 3, 5, 7, 11, 13, 17, 19, 23];
   seventeen = primes[7]; // 17
   ```
3. Dictionary support and properties:
   ```
   months = {"january": 1, "febuary": 2, "march": 3, "april": 4 ... };
   month_num = months.january; // 1
   ```
   
## Cons

1. The language is very loosely defined, which lets you trip up a lot. For example: this is a valid line:
   ```
   functionCall() = 5;
   ```
   This code generates, and represents changing the return value (`$rv`). Here are some more examples of this weirdness
   that will all compile fine (if you find any other weird things tell me about it and I'll add them here):
   ```
   1 + 1 = 3; a = b = c = b = a; 1;
   ```
2. The resulting code will be longer and harder to read than hand-placing DF blocks. If requesting support or showing
   others your code, it would probably look pretty weird.
3. It's not finished yet, so there could be breaking changes at any moment.
4. Using items is a big pain.

## To-do

- [ ] Processes and Start Process
- [ ] Select Player
- [ ] Easier Action and If declarations
- [ ] Better integration with Item API.
- [ ] Command line arguments for Item API and the REPL.
- [ ] Locations and Vectors (not totally sure about this)
- [ ] Release (and maybe publish?) syntax highlight VSCode plugin
- [ ] Release v1.0 to the public
   
## How To Use
1. Download the JAR from [the releases section](https://github.com/fallow64/dfmatic/releases).
2. Run the JAR with the file path of the source code. `java -jar dfmatic.jar mySourceCode.dfm`
3. For CodeUtils/ItemAPI sport

## Credit
* WLGRXD for the idea behind code block serialization.
* lukecashwell for the creation of Spark, a huge inspiration for this project.
* Robert Nystrom, author of [Crafting Interperters](https://craftinginterpreters.com/), a book about creating your own programming language. 