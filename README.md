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
1. Function arguments and return values `wow = whatIsThis();`
2. List support and indexing: `first_item = list[1];`
3. Dictionary support and indexing: `month_num = months["January"];`

## Credit
* WLGRXD for the idea behind code block serialization.
* lukecashwell for the creation of Spark, a huge inspiration for this project.
* Robert Nystrom, author of [Crafting Interperters](https://craftinginterpreters.com/), a book about creating your own programming language. 