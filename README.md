## Advent of Code 2020 Solutions in Kotlin

My solutions for [Advent of Code challenges 2023](https://adventofcode.com/2023) in Kotlin (JVM).

[![Kotlin Version](https://img.shields.io/badge/kotlin-1.9.0-blue.svg)](http://kotlinlang.org/)
[![License](https://img.shields.io/badge/License-Apache%202.0-green.svg)](https://opensource.org/licenses/Apache-2.0)

Copyright © 2023 by Christian Jaeggi.

Before I start I want to give a big shout-out to [Todd Ginsberg](https://github.com/tginsberg/advent-2023-kotlin) whose
solutions are very concise as well as idiomatic to read.
His repos are always very inspirational! I learned a lot from him.

My solutions this year are not well tested as in previous years and also not that easily to use.
Sometimes only solution 2 is implemented in the code.

Just use the main class and execute either the appropriate `solve` functions in the according `Day` classes or any `day`
function i.a.

**Enjoy!**

#### Daily Solution Index for 2023

| Day | Title                           | Links                                                                                                                             |
|-----|---------------------------------|-----------------------------------------------------------------------------------------------------------------------------------|
| 1   | Trebuchet?!                     | [\[Code\]](https://github.com/chjaeggi/aoc2023/blob/main/src/main/kotlin/Day1.kt) [\[AoC\]](http://adventofcode.com/2023/day/1)   |
| 2   | Cube Conundrum                  | [\[Code\]](https://github.com/chjaeggi/aoc2023/blob/main/src/main/kotlin/Day2.kt) [\[AoC\]](http://adventofcode.com/2023/day/2)   |
| 3   | Gear Ratios                     | [\[Code\]](https://github.com/chjaeggi/aoc2023/blob/main/src/main/kotlin/Day3.kt) [\[AoC\]](http://adventofcode.com/2023/day/3)   |
| 4   | Scratchcards                    | [\[Code\]](https://github.com/chjaeggi/aoc2023/blob/main/src/main/kotlin/Day4.kt) [\[AoC\]](http://adventofcode.com/2023/day/4)   |
| 5   | If You Give A Seed A Fertilizer | [\[Code\]](https://github.com/chjaeggi/aoc2023/blob/main/src/main/kotlin/Day5.kt) [\[AoC\]](http://adventofcode.com/2023/day/5)   |
| 6   | Wait For It                     | [\[Code\]](https://github.com/chjaeggi/aoc2023/blob/main/src/main/kotlin/Day6.kt) [\[AoC\]](http://adventofcode.com/2023/day/6)   |
| 7   | Camel Cards                     | [\[Code\]](https://github.com/chjaeggi/aoc2023/blob/main/src/main/kotlin/Day7.kt) [\[AoC\]](http://adventofcode.com/2023/day/7)   |
| 8   | Haunted Wasteland               | [\[Code\]](https://github.com/chjaeggi/aoc2023/blob/main/src/main/kotlin/Day8.kt) [\[AoC\]](http://adventofcode.com/2023/day/8)   |
| 9   | Mirage Maintenance              | [\[Code\]](https://github.com/chjaeggi/aoc2023/blob/main/src/main/kotlin/Day9.kt) [\[AoC\]](http://adventofcode.com/2023/day/9)   |
| 10  | Pipe Maze                       | [\[Code\]](https://github.com/chjaeggi/aoc2023/blob/main/src/main/kotlin/Day10.kt) [\[AoC\]](http://adventofcode.com/2023/day/10) |
| 11  | Cosmic Expansion                | [\[Code\]](https://github.com/chjaeggi/aoc2023/blob/main/src/main/kotlin/Day11.kt) [\[AoC\]](http://adventofcode.com/2023/day/11) |
| 12  | Hot Springs                     | [\[Code\]](https://github.com/chjaeggi/aoc2023/blob/main/src/main/kotlin/Day12.kt) [\[AoC\]](http://adventofcode.com/2023/day/12) |
| 13  | Point of Incidence              | [\[Code\]](https://github.com/chjaeggi/aoc2023/blob/main/src/main/kotlin/Day13.kt) [\[AoC\]](http://adventofcode.com/2023/day/13) |
| 14  | Parabolic Reflector Dish        | [\[Code\]](https://github.com/chjaeggi/aoc2023/blob/main/src/main/kotlin/Day14.kt) [\[AoC\]](http://adventofcode.com/2023/day/14) |
| 15  | Lens Library                    | [\[Code\]](https://github.com/chjaeggi/aoc2023/blob/main/src/main/kotlin/Day15.kt) [\[AoC\]](http://adventofcode.com/2023/day/15) |
| 16  | The Floor Will Be Lava          | [\[Code\]](https://github.com/chjaeggi/aoc2023/blob/main/src/main/kotlin/Day16.kt) [\[AoC\]](http://adventofcode.com/2023/day/16) |
| 17  | Clumsy Crucible                 | [\[AoC\]](http://adventofcode.com/2023/day/17)                                                                                    |
| 18  | Lavaduct Lagoon                 | [\[Code\]](https://github.com/chjaeggi/aoc2023/blob/main/src/main/kotlin/Day18.kt) [\[AoC\]](http://adventofcode.com/2023/day/18) |
| 19  | Aplenty                         | [\[Code\]](https://github.com/chjaeggi/aoc2023/blob/main/src/main/kotlin/Day19.kt) [\[AoC\]](http://adventofcode.com/2023/day/18) |
